package com.mthoko.mobile.resource.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mthoko.mobile.annotations.JoinColumn;
import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.CookieUtil;
import com.mthoko.mobile.util.EntityMapper;
import com.mthoko.mobile.util.HttpManager;
import com.mthoko.mobile.util.RequestPackage;
import com.mthoko.mobile.util.UrlConstants;
import com.mthoko.mobile.util.WritableDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class BaseResourceApi<T extends UniqueEntity> implements UrlConstants {

    public static final String APPLICATION_PROPERTIES = "config/application.properties";
    public static final String DATABASE_NAME = "monitor";
    public static String EPIZY;
    private final Class<T> entityType;

    private Context context;

    public BaseResourceApi(Context context, Class<T> entityType) {
        if (context == null) {
            throw new ApplicationException("context cannot be null");
        }
        this.entityType = entityType;
        this.context = context;
        if (EPIZY == null) {
            initEpizyUrl(context);
        }
    }

    public String getMainUrl() {
        String urlName = getAppProperty("main_url");
        return getAppProperty(urlName);
    }

    public String getAppProperty(String propertyName) {
        return getAppProperty(getContext(), propertyName);
    }

    public Context getContext() {
        return context;
    }

    public String getCurrentTimeStamp() {
        String timeQuery = "SELECT CURRENT_DATE AS date, CURRENT_TIME AS time";
        SQLiteDatabase database = getDatabase();
        Cursor cursor = database.rawQuery(timeQuery, null);
        cursor.moveToFirst();
        String time = cursor.getString(0) + " " + cursor.getString(1);
        database.close();
        return time;
    }

    public SQLiteDatabase getDatabase() {
        return new WritableDatabaseHelper(getContext(), DATABASE_NAME).getWritableDatabase();
    }

    public String getURLData(Map<String, String> params) {
        String url = getMainUrl();
        String method = "POST";
        RequestPackage requestPackage = new RequestPackage(method, url, params);
        if (EPIZY.equals(url)) {
            String cookie = getProperty("cookie");
            if (cookie == null) {
                cookie = getCookieForEPIZY();
                if (cookie != null) {
                    setProperty("cookie", cookie);
                }
            }
            requestPackage.setCookie(cookie);
        }
        String response = HttpManager.getData(requestPackage);
        if (EPIZY.equals(url) && response.contains("slowAES")) {
            String cookie = "__test=" + CookieUtil.getCookieFromURLData(response);
            setProperty("cookie", cookie);
            requestPackage.setCookie(getProperty("cookie"));
            response = HttpManager.getData(requestPackage);
        }
        return response.trim();
    }

    public String getCookieForEPIZY() {
        String data = HttpManager.getData(new RequestPackage("POST", EPIZY));
        if (data.contains("slowAES")) {
            return "__test=" + CookieUtil.getCookieFromURLData(data);
        }
        return null;
    }

    public boolean isConnectionAvailable() {
        String url = getMainUrl();
        HashMap<String, String> params = new HashMap<>();
        params.put(TEST, "connectivity");
        RequestPackage requestPackage = new RequestPackage("POST", url, params);
        if (EPIZY.equals(url)) {
            String cookieForEpizy = getProperty("cookie");
            if (cookieForEpizy == null) {
                cookieForEpizy = getCookieForEPIZY();
                if (cookieForEpizy == null) {
                    return false;
                }
                setProperty("cookie", cookieForEpizy);
            }
            requestPackage.setCookie(cookieForEpizy);
            Log.i("cookie for epizy", "" + cookieForEpizy);
        }
        try {
            String response = HttpManager.getData(requestPackage);
            if (response.contains("slowAES")) {
                String cookie = "__test=" + CookieUtil.getCookieFromURLData(response);
                setProperty("cookie", cookie);
                return true;
            }
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.has("server_available") && jsonObject.getString("server_available").equals("true");
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public void setProperty(String key, String value) {
        if (key == null) {
            return;
        }
        SQLiteDatabase database = getDatabase();
        String existingValue = getProperty(key, database);
        if (existingValue == null) { // key not in the database
            if (value != null) {
                String sql = String.format("INSERT INTO %s(key, value) VALUES('%s', '%s')", Property.class.getSimpleName(), key, value);
                database.execSQL(sql);
            }
        } else {
            if (value == null) {
                unsetProperty(key, database);
            } else if (!existingValue.equals(value)) {
                updateProperty(key, value, database);
            }
        }
        database.close();
    }

    public String getProperty(String key) {
        SQLiteDatabase database = getDatabase();
        String value = getProperty(key, database);
        database.close();
        return value;
    }

    public String getProperty(String key, SQLiteDatabase database) {
        String sql = String.format("SELECT value FROM %s WHERE key = '%s'", Property.class.getSimpleName(), key);
        Cursor cursor = database.rawQuery(sql, new String[]{});
        String value = null;
        if (cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex("value"));
        }
        return value;
    }

    protected List<T> retrieveByRequest(Map<String, String> params) {
        try {
            return extractFromJsonArray(new JSONArray(getResponseText(params)));
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    protected Integer retrieveIntByRequest(Map<String, String> params) {
        return Integer.parseInt(getResponseText(params));
    }

    protected T retrieveOneByRequest(Map<String, String> params) {
        try {
            String responseText = getResponseText(params);
            if (String.valueOf(responseText).equals("null")) {
                return null;
            }
            return extractFromJsonObject(new JSONObject(responseText));
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public <V> List<V> getValues(List<T> entities, String fieldName, Class<V> fieldType) {
        return getValues(entities, fieldName, fieldType);
    }

    public <V> void setValues(List<T> entities, List<V> values, String fieldName) {
        setValues(entities, values, fieldName);
    }

    public <V> void setValue(List<T> entities, V value, String fieldName) {
        setValue(entities, value, fieldName);
    }

    private List<Long> stringToLongList(String responseText) {
        List<Long> contactIds = new ArrayList<>();
        try {
            JSONArray jsonDevContacts = new JSONArray(responseText);
            for (int i = 0; i < jsonDevContacts.length(); i++) {
                contactIds.add(jsonDevContacts.getLong(i));
            }
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
        return contactIds;
    }

    private void updateProperty(String key, String value, SQLiteDatabase database) {
        String sql = String.format("UPDATE %s SET value = '%s' WHERE key = '%s'", Property.class.getSimpleName(), value, key);
        database.execSQL(sql);
    }

    private void unsetProperty(String key, SQLiteDatabase database) {
        String sql = String.format("DELETE FROM %s WHERE key = '%s'", Property.class.getSimpleName(), key);
        database.execSQL(sql);
    }

    protected String getResponseText(Map<String, String> params) {
        String url = getMainUrl();
        String method = "POST";
        RequestPackage requestPackage = new RequestPackage(method, url, params);
        if (EPIZY.equals(url)) {
            requestPackage.setCookie(getCookieForEPIZY());
        }
        String response = HttpManager.getData(requestPackage);
        return response;
    }

    protected T extractFromJsonObject(JSONObject jsonObject) {
        return extractFromJsonObject(jsonObject, getEntityClass());
    }

    private Class<T> getEntityClass() {
        return entityType;
    }

    protected List<T> extractFromJsonArray(JSONArray array) {
        return extractFromJsonArray(array, getEntityClass());
    }

    public Long save(T entity) {
        String request = "save" + getEntityName();
        JSONObject jsonObject = toJson(entity);
        Map<String, String> params = new HashMap<>();
        params.put("key", request);
        params.put("value", jsonObject.toString());
        String responseText = getResponseText(params);
        long id = Long.parseLong(responseText);
        entity.setId(id);
        entity.setVerificationId(id);
        return id;
    }

    private String getEntityName() {
        return entityType.getSimpleName();
    }

    public List<Long> saveAll(List<T> entities) {
        String request = "save" + getEntityName() + "List";
        JSONArray array = toJsonArray(entities);
        Map<String, String> params = new HashMap<>();
        params.put("key", request);
        params.put("value", array.toString());
        List<Long> remoteIds = stringToLongList(getResponseText(params));
        for (int i = 0; i < entities.size() && i < remoteIds.size(); i++) {
            entities.get(i).setVerificationId(remoteIds.get(i));
        }
        return remoteIds;
    }

    public Map<String, Long> extractVerificationFromJson(JSONObject jsonObject) {
        Map<String, Long> ids = new HashMap<>();
        Iterator<String> it = jsonObject.keys();
        try {
            while (it.hasNext()) {
                String key = it.next();
                ids.put(key, jsonObject.getLong(key));
            }
            return ids;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public Map<String, Long> extractVerification(UniqueEntity entity) {
        Map<String, Long> verification = new HashMap<>();
        verification.put(entity.getUniqueIdentifier(), entity.getVerificationId());
        return verification;
    }

    public Map<String, Long> extractVerification(List<? extends UniqueEntity> entities) {
        Map<String, Long> verification = new HashMap<>();
        for (UniqueEntity entity : entities) {
            verification.put(entity.getUniqueIdentifier(), entity.getVerificationId());
        }
        return verification;
    }

    public static <V> List<V> extractFromJsonArray(JSONArray array, Class<V> type) {
        try {
            List<V> entities = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                entities.add(extractFromJsonObject(array.getJSONObject(i), type));
            }
            return entities;
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public static <V> V extractFromJsonObject(JSONObject jsonObject, Class<V> type) {
        Map<String, String> columns = EntityMapper.getColumns(type, false);
        try {
            V entity = type.newInstance();
            for (String columnName : columns.keySet()) {
                if (jsonObject.has(columnName)) {
                    Field field = entity.getClass().getDeclaredField(columnName);
                    field.setAccessible(true);
                    Object fieldValue = EntityMapper.getFieldValue(field, jsonObject.getString(columnName));
                    field.set(entity, fieldValue);
                }
            }

            String verificationIdName = "verificationId";
            Field verificationIdField = null;
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getName().equals(verificationIdName)) {
                    verificationIdField = field;
                    break;
                }
            }
            if (verificationIdField != null && jsonObject.has("id") && !jsonObject.has(verificationIdName)) {
                verificationIdField.setAccessible(true);
                verificationIdField.set(entity, jsonObject.getLong("id"));
            }

            Map<Field, JoinColumn> joinFields = EntityMapper.getJoinFields(type);
            if (!joinFields.isEmpty()) {
                for (Map.Entry<Field, JoinColumn> joinColumnEntry : joinFields.entrySet()) {
                    Field field = joinColumnEntry.getKey();
                    Class<?> aClass = joinColumnEntry.getValue().targetEntity();
                    field.setAccessible(true);
                    JSONArray jsonArray = jsonObject.getJSONArray(field.getName());
                    List<?> value = extractFromJsonArray(jsonArray, aClass);
                    field.set(entity, value);
                }
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | JSONException e) {
            throw new ApplicationException(e);
        }

    }

    private static void initEpizyUrl(Context context) {
        EPIZY = getAppProperty(context, "EPIZY");
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

    public Connection getConnection() {
        Connection conn = null;
        try {
            String driverClass = getAppProperty("datasource.driver");
            String timeZonConfig = getAppProperty("datasource.servertimezone");
            String host = getAppProperty("datasource.host");
            String username = getAppProperty("datasource.username");
            String pwd = getAppProperty("datasource.password");
            String db = getAppProperty("datasource.database");
            String url = String.format("jdbc:mysql://%s/%s?serverTimezone=%s&characterEncoding=latin1", host, db, timeZonConfig);
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, username, pwd);
        } catch (SQLException |ClassNotFoundException ex) {
            throw new ApplicationException(ex);
        }
        return conn;
    }

    public JSONArray toJsonArray(List<T> objects) {
        return EntityMapper.toJsonArray(objects);
    }

    public JSONObject toJson(T entity) {
        return EntityMapper.toJson(entity);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
