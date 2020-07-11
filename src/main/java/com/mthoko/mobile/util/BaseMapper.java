package com.mthoko.mobile.util;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;
import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.FileInfo;
import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMapper {

    public static final List<Class<?>> SUPPORTED_TYPES = Arrays.asList(
            new Class<?>[]{
                    Boolean.class, boolean.class,
                    Character.class, char.class,
                    Byte.class, byte.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class,
                    String.class, Date.class
            });

    public static String getColumnDefinition(String columnName, String columnType, Map<String, String> constraints, Field primaryField) {
        String columnDefinition = columnName + " " + columnType;
        if (columnName.equals(primaryField.getName())) {
            columnDefinition += " PRIMARY KEY";
            if (primaryField.getAnnotation(PrimaryKey.class).autoIncrement()) {
                columnDefinition += " AUTOINCREMENT";
            }
        }
        if (constraints.containsKey(columnName)) {
            columnDefinition += " " + constraints.get(columnName);
        }
        return columnDefinition;
    }

    public static Map<String, String> getConstraints(Class<?> entityClass) {
        Map<String, String> results = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            Constraints constraints = field.getAnnotation(Constraints.class);
            if (constraints != null) {
                String constraint = "";
                if (constraints.autoIncrement()) {
                    constraint += " AUTOINCREMENT";
                }
                if (!constraints.defaultValue().isEmpty()) {
                    constraint += " DEFAULT " + constraints.defaultValue();
                }
                if (constraints.nullable()) {
                    constraint += " NULL";
                } else {
                    constraint += " NOT NULL";
                }
                results.put(field.getName(), constraint);
            }
        }
        return results;
    }

    public static String getEntityName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    public static String getType(Class<?> aClass) {
        if (SUPPORTED_TYPES.contains(aClass)) {
            if (Date.class == aClass || String.class == aClass || Character.class == aClass || char.class == aClass) {
                return "TEXT";
            }
            if (Double.class == aClass || double.class == aClass || Float.class == aClass || float.class == aClass) {
                return "NUMBER";
            }
            return "INTEGER";
        }
        return null;
    }

    public static Field getPrimaryField(Class<?> entityClass) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static List<Class<? extends UniqueEntity>> getSortedEntities(String entitiesPackage) {

        List<Class<?>> classes;
//            classes = getClasses(entitiesPackage);
            Class<? extends UniqueEntity>[] classes1 = new Class[]{
                    Member.class, Property.class, Credentials.class, SimCard.class, SimContact.class, Device.class,
                    DevContact.class, DevContactValue.class, RecordedCall.class, Sms.class, FileInfo.class, LocationStamp.class
            };
            return Arrays.asList(classes1);
//        return getClassesByPackageName(classes);
    }

    public static List<Class<?>> getForeignEntities(Class<?> aClass) {
        List<Class<?>> result = new ArrayList<>();
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ForeignKey.class)) {
                result.add(field.getAnnotation(ForeignKey.class).referencedEntity());
            }
        }
        return result;
    }

    public static Field getForeignField(Class<?> aClass, Class<?> child) {
        List<Class<?>> result = new ArrayList<>();
        for (Field field : child.getDeclaredFields()) {
            if (field.isAnnotationPresent(ForeignKey.class)) {
                if (field.getAnnotation(ForeignKey.class).referencedEntity().equals(aClass)) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static List<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    public static List<Class<?>> getClassesByPackageName(List<Class<?>> classes) {
        List<Class<?>> sortedClasses = new ArrayList<>();
        while (sortedClasses.size() < classes.size()) {
            int initialSize = sortedClasses.size();
            for (Class<?> aClass : classes) {
                if (sortedClasses.contains(aClass)) {
                    continue;
                }
                List<Class<?>> foreignEntities = getForeignEntities(aClass);
                if (foreignEntities.isEmpty() || sortedClasses.containsAll(foreignEntities)) {
                    sortedClasses.add(aClass);
                }
            }
            if (initialSize == sortedClasses.size()) {
                throw new ApplicationException("Cyclic dependencies exists in entities");
            }
        }
        return sortedClasses;
    }
}
