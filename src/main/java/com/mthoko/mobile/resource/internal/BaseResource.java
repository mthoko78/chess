package com.mthoko.mobile.resource.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.resource.remote.EntityResource;
import com.mthoko.mobile.util.DatabaseWrapper;
import com.mthoko.mobile.util.EntityMapper;
import com.mthoko.mobile.util.WritableDatabaseHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.mthoko.mobile.util.EntityMapper.joinClause;

public class BaseResource<T extends UniqueEntity> extends EntityResource<T> {

    public static final String APPLICATION_PROPERTIES = "config/application.properties";

    public static final String DATABASE_NAME = "monitor";

    private final Class<T> entityType;

    private final DatabaseWrapper databaseWrapper;

    public BaseResource(Context context, Class<T> entityType, DatabaseWrapper databaseWrapper) {
        super(entityType, context);
        if (context == null) {
            throw new ApplicationException("context cannot be null");
        }
        this.databaseWrapper = databaseWrapper;
        this.entityType = entityType;
    }

    public List<Long> retrieveVerificationIdsWhere(String whereClause) {
        return selectNonNullLongsFromWhere(getEntityName() + ".verificationId", getEntityName(), whereClause);
    }

    public List<Long> retrieveVerificationIdsJoiningWhere(Class<?> parentEntityClass, Class<?> childEntityClass, String whereClause) {
        String joinClause = joinClause(parentEntityClass, childEntityClass);
        return selectNonNullLongsFromWhere(getEntityName() + ".verificationId", joinClause, whereClause);
    }

    public SQLiteDatabase getDatabase() {
        return databaseWrapper.getDatabase();
    }

    public void setDatabase(SQLiteDatabase database) {
        this.databaseWrapper.setDatabase(database);
    }

    @Override
    public void setProperty(String key, String value) {
        PropertyResource propertyResource = new PropertyResource(getContext(), databaseWrapper);
        Property property = propertyResource.findByKey(key);
        if (property != null) {
            property.setValue(value);
            propertyResource.update(property);
        } else {
            property = new Property();
            property.setKey(key);
            property.setValue(value);
            propertyResource.save(property);
        }
    }

    @Override
    public String getProperty(String key) {
        PropertyResource propertyResource = new PropertyResource(getContext(), databaseWrapper);
        Property property = propertyResource.findByKey(key);
        if (property != null) {
            return property.getValue();
        }
        return null;
    }

    @Override
    public String getAppProperty(String propertyName) {
        return getAppProperty(getContext(), propertyName);
    }

    @Override
    public boolean openConnection() {
        if (getDatabase() == null) {
            setDatabase(openDatabase());
            return true;
        }
        return false;
    }

    @Override
    public void closeConnection() {
        if (getDatabase() != null) {
            getDatabase().close();
            setDatabase(null);
        }
    }

    @Override
    public boolean beginTransaction() {
        if (!inTransaction()) {
            getDatabase().beginTransaction();
            return true;
        }
        return false;
    }

    @Override
    public void endTransaction() {
        if (inTransaction()) {
            getDatabase().setTransactionSuccessful();
            getDatabase().endTransaction();
        }
    }

    @Override
    public void execSQL(String sql) {
        getDatabase().execSQL(sql);
    }

    @Override
    public <V extends UniqueEntity> List<V> extractFromQuery(Class<V> entityClass, String query) {
        Cursor cursor = executeQuery(query);
        List<V> entities = extractFromCursor(cursor, entityClass);
        cursor.close();
        return entities;
    }

    @Override
    public T retrieveOneByQuery(String query) throws SQLException {
        Cursor cursor = executeQuery(query);
        T one = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                one = extractOneFromCursor(cursor, entityType);
            }
            cursor.close();
        }
        return one;
    }

    @Override
    public Integer retrieveIntFromQuery(String columnName, String sql) {
        Cursor cursor = executeQuery(sql);
        Integer integer = extractIntFromCursor(cursor, columnName);
        if (cursor != null) {
            cursor.close();
        }
        return integer;
    }

    @Override
    public List<Long> retrieveLongsFromQuery(String query, String columnLabel) {
        Cursor cursor = executeQuery(query);
        List<Long> result = extractLongsFromCursor(cursor, columnLabel);
        cursor.close();
        return result;
    }

    private Cursor executeQuery(String query) {
        return getDatabase().rawQuery(query, null);
    }

    @Override
    public void rollBack() {
        getDatabase().endTransaction();
    }

    @Override
    public boolean inTransaction() {
        SQLiteDatabase database = getDatabase();
        boolean inTransaction = false;
        if (database != null) {
            inTransaction = database.inTransaction();
        }
        return inTransaction;
    }

    public SQLiteDatabase openDatabase() {
        return new WritableDatabaseHelper(getContext(), DATABASE_NAME).getWritableDatabase();
    }


    @SuppressLint("MissingPermission")
    public String getCurrentSimNo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String serial = telephonyManager.getSimSerialNumber();
        if (getAppProperty("EMULATOR_SIM_NO").equals(serial)) {
            serial = getAppProperty("MTHOKO_SIM_NO");
        }
        return serial;
    }

    @SuppressLint("MissingPermission")
    public String getImei() {
        TelephonyManager telephonyManager = ((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE));
        String imei;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            imei = telephonyManager.getImei();
        else
            imei = telephonyManager.getDeviceId();
        if (imei.equals(getAppProperty("EMULATOR_IMEI"))) {
            imei = getAppProperty("MTHOKO_IMEI");
        }
        return imei;
    }

//===========================STATIC UTILITY METHODS====================================

    private List<Long> selectNonNullLongsFromWhere(String columnName, String relation, String whereClause) {
        String select = String.format(
                "SELECT %s FROM %s WHERE %s;",
                columnName,
                relation,
                whereClause
        );
        Cursor cursor = executeQuery(select);
        List<Long> longs = extractLongsFromCursor(cursor, columnName);
        cursor.close();
        return longs;
    }

    private static List<Long> extractLongsFromCursor(Cursor cursor, String columnName) {
        List<Long> result = new ArrayList<>();
        if (cursor.moveToNext()) {
            if (columnName.contains(".")) {
                columnName = columnName.substring(columnName.indexOf('.') + 1);
            }
            do {
                result.add(cursor.getLong(cursor.getColumnIndex(columnName)));
            } while (cursor.moveToNext());
        }
        return result;
    }

    private static Integer extractIntFromCursor(Cursor cursor, String columnName) {
        Integer result = null;
        if (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndex(columnName));
        }
        return result;
    }

    private static <V> List<V> extractFromCursor(Cursor cursor, Class<V> aClass) {
        List<V> entities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                entities.add(extractOneFromCursor(cursor, aClass));
            } while (cursor.moveToNext());
        }
        return entities;
    }

    private static <V> V extractOneFromCursor(Cursor cursor, Class<V> entityClass) {
        try {
            V one = entityClass.newInstance();
            Map<String, String> columns = EntityMapper.getColumns(entityClass, false);
            for (Field field : entityClass.getDeclaredFields()) {
                String fieldType = columns.get(field.getName());
                if (fieldType == null) {
                    continue;
                }
                field.setAccessible(true);
                String columnName = cursor.getString(cursor.getColumnIndex(field.getName()));
                field.set(one, EntityMapper.getFieldValue(field, columnName));
            }
            return one;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ApplicationException("Failed to instantiate entity: " + entityClass.getName(), e);
        }
    }

    private static String getAppProperty(Context context, String propertyName) {
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(APPLICATION_PROPERTIES));
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public void verifyAll(List<T> entities, Map<String, Long> verification) {
        for (T entity : entities) {
            verify(entity, verification);
        }
    }

    public void verify(T entity, Map<String, Long> verification) {
        String uniqueIdentifier = entity.getUniqueIdentifier();
        if (verification.containsKey(uniqueIdentifier)) {
            entity.setVerificationId(verification.get(uniqueIdentifier));
            update(entity);
        }
    }

    public String getPhoneNumber() {
        String number = getCurrentSimNo(getContext());
        String entityName = SimCard.class.getSimpleName();
        Cursor cursor = getDatabase().query(entityName,
                new String[]{"phone"},
                "simNo = ?",
                new String[]{number}, null, null, null);
        if (cursor.moveToFirst()) {
            number = cursor.getString(cursor.getColumnIndex("phone"));
        }
        cursor.close();
        return number;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        return manufacturer.toUpperCase() + " " + model;
    }
}
