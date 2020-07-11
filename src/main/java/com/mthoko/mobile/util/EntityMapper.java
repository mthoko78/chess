package com.mthoko.mobile.util;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.JoinColumn;
import com.mthoko.mobile.annotations.PrimaryKey;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.json.JSONObject.NULL;

public class EntityMapper<T> extends BaseMapper {

    public static final String INTEGER = "INTEGER";
    public static final String NUMBER = "NUMBER";
    public static final String TEXT = "TEXT";

    public static <V extends UniqueEntity> V extractOneFromResultSet(Class<V> entityClass, ResultSet resultSet) {
        try {
            V one = entityClass.newInstance();
            Map<String, String> columns = EntityMapper.getColumns(entityClass, false);
            for (Field field : entityClass.getDeclaredFields()) {
                String fieldType = columns.get(field.getName());
                if (fieldType == null || field.getName().equals("verificationId")) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldValue = EntityMapper.getFieldValue(field, resultSet.getString(field.getName()));
                field.set(one, fieldValue);
            }
            one.setVerificationId(one.getId());
            return one;
        } catch (IllegalAccessException | InstantiationException | SQLException e) {
            throw new ApplicationException("Failed to instantiate entity: " + entityClass.getName(), e);
        }
    }

    public static <V extends UniqueEntity> String getInsertQuery(V entity, Collection<String> ignoreFields, String l, String r) {
        return getInsertQuery(Arrays.asList(entity), ignoreFields, l, r);
    }

    public static <V extends UniqueEntity> String getInsertQuery(List<V> entities, Collection<String> ignoreFields, String l, String r) {
        StringBuilder valuesClause = new StringBuilder();
        String insertClause = getInsertClause(entities.get(0).getClass(), ignoreFields, l, r);
        for (V entity : entities) {
            Map<String, Object> values = getEntityValues(entity);
            for (String fieldName : ignoreFields) {
                values.remove(fieldName);
            }
            valuesClause.append(",(" + getColumnInsertValues(values, ignoreFields) + ")");
        }
        return insertClause + valuesClause.substring(1);
    }

    private static <V extends UniqueEntity> String getInsertClause(Class<V> entityClass, Collection<String> ignoreFields, String l, String r) {
        String table = entityClass.getSimpleName();
        Collection<String> columnNames = getColumnNames(entityClass, false);
        columnNames.removeAll(ignoreFields);
        String columns = columnNames.toString().replaceAll("[\\[\\]\\s]", "");
        columns = l + columns.replaceAll(",", r + "," + l) + r;
        return String.format("INSERT INTO %s(%s) VALUES ", table, columns);
    }

    public static Collection<String> getColumnNames(Class<?> entityClass, boolean includeEntityName) {
        return getColumns(entityClass, includeEntityName).keySet();
    }

    public static Map<String, String> getColumns(Class<?> entityClass, boolean includeEntityName) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        Map<String, String> fields = new LinkedHashMap<>();
        for (Field field : declaredFields) {
            String fieldType = getType(field.getType());
            if (fieldType != null) {
                String fieldName = field.getName();
                if (includeEntityName) {
                    fieldName = entityClass.getSimpleName() + "." + fieldName;
                }
                fields.put(fieldName, fieldType);
            }
        }
        return fields;
    }

    public static <V> Map<String, Object> getEntityValues(V entity) {
        try {
            Map<String, Object> contentValues = new LinkedHashMap<>();
            Collection<String> columns = getColumnNames(entity.getClass(), false);
            for (String key : columns) {
                Field field = entity.getClass().getDeclaredField(key);
                if (!BaseMapper.SUPPORTED_TYPES.contains(field.getType())) {
                    continue;
                }
                if (field.isAnnotationPresent(PrimaryKey.class)
                        && field.getAnnotation(PrimaryKey.class).autoIncrement()) {
                    continue;
                }
                field.setAccessible(true);
                putFieldValue(contentValues, field, entity);
            }
            return contentValues;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ApplicationException(e);
        }
    }

    public static <V> void putFieldValue(Map<String, Object> contentValues, Field field, V entity)
            throws IllegalAccessException {
        Object value = field.get(entity);
        String name = field.getName();
        if (value == null) {
            if (getDefaultValue(field) == null) {
                contentValues.put(name, null);
            }
            return;
        }
        Class<?> type = field.getType();
        if (BaseMapper.SUPPORTED_TYPES.contains(type)) {
            if (Integer.class.equals(type) || int.class.equals(type)) {
                contentValues.put(name, (Integer) value);
            } else if (Long.class.equals(type) || long.class.equals(type)) {
                contentValues.put(name, (Long) value);
            } else if (Short.class.equals(type) || short.class.equals(type)) {
                contentValues.put(name, (Short) value);
            } else if (Byte.class.equals(type) || byte.class.equals(type)) {
                contentValues.put(name, (Byte) value);
            } else if (Float.class.equals(type) || float.class.equals(type)) {
                contentValues.put(name, (Float) value);
            } else if (Double.class.equals(type) || double.class.equals(type)) {
                contentValues.put(name, (Double) value);
            } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                contentValues.put(name, (Boolean) value);
            } else if (Character.class.equals(type) || char.class.equals(type) || String.class.equals(type)) {
                contentValues.put(name, String.valueOf(value));
            } else if (Date.class.equals(type)) {
                contentValues.put(name, getTimeStampFromDate((Date) value));
            }
        }
    }

    public static String getDefaultValue(Field field) {
        if (field.isAnnotationPresent(Constraints.class)) {
            Constraints constraints = field.getAnnotation(Constraints.class);
            if (!constraints.defaultValue().isEmpty()) {
                return constraints.defaultValue();
            }
        }
        return null;
    }

    public static Map<Object, List<Object>> groupChildrenByForeignKeys(List<?> children, Field foreignFieldOnChild)
            throws IllegalAccessException {
        Map<Object, List<Object>> foreignKeyMapping = new LinkedHashMap<>();
        for (Object child : children) {
            Object foreignKeyValue = foreignFieldOnChild.get(child);
            if (!foreignKeyMapping.containsKey(foreignKeyValue)) {
                foreignKeyMapping.put(foreignKeyValue, new ArrayList<>());
            }
            foreignKeyMapping.get(foreignKeyValue).add(child);
        }
        return foreignKeyMapping;
    }

    public static <V extends UniqueEntity> void setJoinField(V entity, List<?> children, Field joinFieldOnParent) {
        joinFieldOnParent.setAccessible(true);
        try {
            joinFieldOnParent.set(entity, children);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static <V extends UniqueEntity> void setJoinField(List<V> entities, List<?> children, Field foreignFieldOnChild,
                                                             Field primaryFieldOnParent, Field joinFieldOnParent) {
        foreignFieldOnChild.setAccessible(true);
        primaryFieldOnParent.setAccessible(true);
        joinFieldOnParent.setAccessible(true);
        try {
            Map<Object, List<Object>> foreignKeyMapping = groupChildrenByForeignKeys(children, foreignFieldOnChild);
            for (V entity : entities) {
                Object primaryKeyValue = primaryFieldOnParent.get(entity);
                if (foreignKeyMapping.containsKey(primaryKeyValue)) {
                    setJoinField(entity, foreignKeyMapping.get(primaryKeyValue), joinFieldOnParent);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static String joinClause(Class<?> parentEntityClass, Class<?> childEntityClass) {
        String parent = parentEntityClass.getSimpleName();
        String child = EntityMapper.getEntityName(childEntityClass);
        String childForeignField = EntityMapper.getForeignField(parentEntityClass, childEntityClass).getName();
        String parentPrimaryField = EntityMapper.getPrimaryField(parentEntityClass).getName();
        return String.format("%s JOIN %s ON %s.%s = %s.%s", parent, child, child, childForeignField, parent,
                parentPrimaryField);
    }

    public static String getColumnInsertValues(Map<String, Object> values, Collection<String> ignoreFields) {
        List<String> valuesAsList = new ArrayList<>();
        for (String key : values.keySet()) {
            if (!ignoreFields.contains(key)) {
                valuesAsList.add(getStringValue(values.get(key)));
            }
        }
        return valuesAsList.toString().replaceAll("[\\[\\]]", "");
    }

    public static Map<String, String> getInsertValuesAsMap(Map<String, Object> values) {
        Map<String, String> valuesAsList = new LinkedHashMap<>();
        for (String key : values.keySet()) {
            valuesAsList.put(key, getStringValue(values.get(key)));
        }
        return valuesAsList;
    }

    public static String getStringValue(Object value) {
        String stringValue;
        if (value == null) {
            stringValue = null;
        } else {
            Class<?> valueClass = value.getClass();
            if (valueClass == Integer.class || valueClass == int.class || valueClass == Long.class
                    || valueClass == long.class || valueClass == Short.class
                    || valueClass == short.class || valueClass == Byte.class
                    || valueClass == byte.class || valueClass == Number.class) {
                stringValue = String.valueOf(value);
            } else if (valueClass == Boolean.class || valueClass == boolean.class) {
                stringValue = (boolean) value ? String.valueOf(1) : String.valueOf(0);
            } else if (valueClass == Date.class) {
                stringValue = "'" + getTimeStampFromDate((Date) value) + "'";
            } else {
                stringValue = "'" + parseText(String.valueOf(value)) + "'";
            }
        }
        return stringValue;
    }

    public static String parseText(String value) {
        String SLASH = "[\\\\]";
        String SLASH_ESCAPE = "\\\\\\\\";
        String SINGLE_QUOTE = "'";
        String SINGLE_QUOTE_ESCAPE = "''";
        return value.replaceAll(SINGLE_QUOTE, SINGLE_QUOTE_ESCAPE).replaceAll(SLASH, SLASH_ESCAPE);
    }

    public static void setForeignKeyOnChildrenFromParentPrimaryKey(Class<? extends UniqueEntity> parentClass, long id, Class<? extends UniqueEntity> childClass, List<UniqueEntity> children) {
        try {
            Field foreignField = EntityMapper.getForeignField(parentClass, childClass);
            foreignField.setAccessible(true);
            for (UniqueEntity child : children) {
                foreignField.set(child, id);
            }
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static <V extends UniqueEntity> List<UniqueEntity> getChildren(V entity, Field foreignField) {
        return EntityMapper.getForeignObjects(entity, foreignField);
    }

    public static <V extends UniqueEntity> List<Long> getIds(List<V> entities) {
        List<Long> ids = new ArrayList<>();
        for (V entity : entities) {
            ids.add(entity.getId());
        }
        return ids;
    }

    public static <V extends UniqueEntity> String getUpdateQuery(V entity, Collection<String> ignoreFields, String l, String r) {
        Map<String, Object> values = getEntityValues(entity);
        for (String fieldName : ignoreFields) {
            values.remove(fieldName);
        }
        Map<String, String> valuesAsList = getInsertValuesAsMap(values);
        String entityName = entity.getClass().getSimpleName();
        StringBuilder updateValues = new StringBuilder();
        for (Map.Entry<String, String> entry : valuesAsList.entrySet()) {
            updateValues.append("," + l + entry.getKey() + r + "=" + entry.getValue());
        }
        String whereClause = String.format("%s.%s = %s", entityName, "id", entity.getId());
        return String.format("UPDATE %s SET %s WHERE %s", entityName, updateValues.substring(1), whereClause);
    }

    public static String getTimeStampFromDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date getDateFromTimeStamp(String timeStamp) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStamp);
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
    }

    public static String deleteByIdsQuery(List<? extends UniqueEntity> entities) {
        String idsValues = getIds(entities).toString().replace('[', '(').replace(']', ')');
        String whereClause = "id IN " + idsValues + ";";
        return getDeleteClause(entities.get(0).getClass(), whereClause);
    }

    public static String getDeleteClause(Class<? extends UniqueEntity> entityClass, String whereClause) {
        return String.format("DELETE FROM %s WHERE %s", entityClass.getSimpleName(), whereClause);
    }

    public static String getCreateQuery(Class<? extends UniqueEntity> entityClass) {
        String entityName = getEntityName(entityClass);
        Field primaryField = getPrimaryField(entityClass);
        Map<String, String> columns = getColumns(entityClass, false);
        Map<String, String> constraints = getConstraints(entityClass);
        Map<String, String[]> foreignKeys = getForeignKeys(entityClass);
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + entityName + " (\n");
        boolean firstColumn = true;
        for (Map.Entry<String, String> columnEntry : columns.entrySet()) {
            if (!firstColumn) {
                query.append(", \n");
            } else {
                firstColumn = false;
            }
            query.append(getColumnDefinition(columnEntry.getKey(), columnEntry.getValue(), constraints, primaryField));
        }
        for (Map.Entry<String, String[]> entry : foreignKeys.entrySet()) {
            String val = ", \n" + String.format("FOREIGN KEY(%s) REFERENCES %s(%s)", entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
            query.append(val);
        }
        query.append("\n);");
        return query.toString();
    }

    public static Map<String, String[]> getForeignKeys(Class<? extends UniqueEntity> entityClass) {
        Map<String, String[]> foreignKeyNames = new LinkedHashMap<>();
        for (Field field : getForeignFields(entityClass)) {
            String fieldName = field.getName();
            Class<?> referencedEntity = field.getAnnotation(ForeignKey.class).referencedEntity();
            String entityName = getEntityName(referencedEntity);
            String referencedKeyName = getPrimaryField(referencedEntity).getName();
            foreignKeyNames.put(fieldName, new String[]{entityName, referencedKeyName});
        }
        return foreignKeyNames;
    }

    public static List<Field> getForeignFields(Class<? extends UniqueEntity> entityClass) {
        List<Field> fields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(ForeignKey.class) != null) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static <T extends UniqueEntity> T newInstance(Class<T> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ApplicationException(e);
        }
    }

    public static <T extends UniqueEntity, V> List<V> getValues(List<T> entities, String fieldName) {
        try {
            Class<? extends UniqueEntity> entityClass = entities.get(0).getClass();
            return getValues(entities, entityClass.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new ApplicationException(e);
        }
    }

    public static <T extends UniqueEntity, V> List<V> getValues(List<T> entities, Field field) {
        try {
            List<V> values = new ArrayList<>();
            for (T entity : entities) {
                field.setAccessible(true);
                values.add((V) field.get(entity));
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static <T extends UniqueEntity, V> void setValues(List<T> entities, List<V> values, String fieldName) {
        try {
            setValues(entities, values, entities.get(0).getClass().getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new ApplicationException(e);
        }
    }

    public static <T extends UniqueEntity, V> void setValues(List<T> entities, List<V> values, Field field) {
        for (int i = 0; i < entities.size(); i++) {
            try {
                T entity = entities.get(i);
                field.setAccessible(true);
                field.set(entity, values.get(i));
            } catch (IllegalAccessException e) {
                throw new ApplicationException(e);
            }
        }
    }

    public static <T extends UniqueEntity, V> void setValue(List<T> entities, V value, String fieldName) {
        try {
            Class<T> entityClass = (Class<T>) entities.get(0).getClass();
            setValue(entityClass, entities, value, entityClass.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new ApplicationException(e);
        }
    }

    public static <T extends UniqueEntity, V> void setValue(Class<T> entityClass, List<T> entities, V value, Field field) {
        try {
            for (int i = 0; i < entities.size(); i++) {
                T entity = entities.get(i);
                field.setAccessible(true);
                field.set(entity, value);
            }
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static Object getFieldValue(Field field, Object entity) {
        try {
            field.setAccessible(true);
            Object object = field.get(entity);
            if (object instanceof Date) {
                object = getTimeStampFromDate(((Date) object));
            }
            String stringValue = String.valueOf(object);
            return getFieldValue(field, stringValue);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static Object getFieldValue(Field field, String stringValue) {
        if ("NULL".equalsIgnoreCase(stringValue) || stringValue == null) {
            return null;
        }
        Class<?> fieldType = field.getType();
        switch (getType(fieldType)) {
            case INTEGER: {
                if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
                    return Long.parseLong(stringValue);
                }
                if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                    return Integer.parseInt(stringValue);
                }
                if (Short.class.equals(fieldType) || short.class.equals(fieldType)) {
                    return Short.parseShort(stringValue);
                }
                if (Byte.class.equals(fieldType) || byte.class.equals(fieldType)) {
                    return Byte.parseByte(stringValue);
                }
                if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                    return Integer.parseInt(stringValue) == 1 ? true : false;
                }
            }
            case NUMBER: {
                if (Double.class.equals(fieldType) || double.class.equals(fieldType)) {
                    return Double.parseDouble(stringValue);
                }
                if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                    return Float.parseFloat(stringValue);
                }
            }
            case TEXT: {
                if (Character.class.equals(fieldType) || char.class.equals(fieldType)) {
                    return stringValue.charAt(0);
                }
                if (String.class.equals(fieldType)) {
                    return stringValue;
                }
                if (Date.class.equals(fieldType)) {
                    return getDateFromTimeStamp(stringValue);
                }
            }
        }
        return null;
    }

    public static Map<Field, JoinColumn> getJoinFields(Class<?> entityClass) {
        Map<Field, JoinColumn> fields = new LinkedHashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                fields.put(field, joinColumn);
            }
        }
        return fields;
    }

    public static JSONObject toJson(UniqueEntity entity) {
        try {
            JSONObject jsonObject = new JSONObject();
            Class<?> entityClass = entity.getClass();
            Map<String, String> columns = getColumns(entityClass, false);
            for (String columnName : columns.keySet()) {
                Field field = entityClass.getDeclaredField(columnName);
                Object fieldValue = EntityMapper.getFieldValue(field, entity);
                if (fieldValue == null) {
                    fieldValue = NULL;
                } else if (fieldValue instanceof Date) {
                    fieldValue = ((Date) fieldValue).getTime();
                }
                jsonObject.put(columnName, fieldValue);
            }
            Map<Field, JoinColumn> joinFields = getJoinFields(entityClass);
            if (!joinFields.isEmpty()) {
                for (Map.Entry<Field, JoinColumn> joinColumnEntry : joinFields.entrySet()) {
                    JSONArray list = getJoinColumnObjects(entity, joinColumnEntry.getKey());
                    jsonObject.put(joinColumnEntry.getKey().getName(), list);
                }
            }
            return jsonObject;
        } catch (JSONException | NoSuchFieldException e) {
            throw new ApplicationException(e);
        }
    }

    public static JSONArray getJoinColumnObjects(UniqueEntity entity, Field joinColumnEntry) {
        List<UniqueEntity> objects = getForeignObjects(entity, joinColumnEntry);
        return toJsonArray(objects);
    }

    public static List<UniqueEntity> getForeignObjects(UniqueEntity entity, Field foreign) {
        foreign.setAccessible(true);
        try {
            return (List<UniqueEntity>) foreign.get(entity);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    public static JSONArray toJsonArray(List objects) {
        JSONArray array = new JSONArray();
        for (Object o : objects) {
            array.put(toJson((UniqueEntity) o));
        }
        return array;
    }


}