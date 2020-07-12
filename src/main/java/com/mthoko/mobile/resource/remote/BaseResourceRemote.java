package com.mthoko.mobile.resource.remote;

import static com.mthoko.mobile.util.EntityMapper.getDateFromTimeStamp;
import static com.mthoko.mobile.util.EntityMapper.getTimeStampFromDate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DataManager;
import com.mthoko.mobile.util.EntityMapper;

public class BaseResourceRemote<T extends UniqueEntity> extends EntityResource<T> {

    public static final String APPLICATION_PROPERTIES = "config/application.properties";

    private final ConnectionWrapper connectionWrapper;

    public BaseResourceRemote(Class<T> entityType,ConnectionWrapper connectionWrapper) {
        super(entityType);
        this.connectionWrapper = connectionWrapper;
    }

    public Connection getConnection() {
        return connectionWrapper.getConnection();
    }

    public void setConnection(Connection connection) {
        connectionWrapper.setConnection(connection);
    }

    @Override
    public void rollBack() {
        if (getConnection() != null) {
            try {
                getConnection().rollback();
            } catch (SQLException e) {
                throw new ApplicationException(e);
            }
        }
    }

    @Override
    public boolean inTransaction() {
        boolean inTransaction = false;
        if (getConnection() != null) {
            try {
                inTransaction = getConnection().getAutoCommit() == false;
            } catch (SQLException e) {
                throw new ApplicationException(e);
            }
        }
        return inTransaction;

    }

    @Override
    public void closeConnection() {
        if (getConnection() == null) {
            return;
        }
        try {
            getConnection().close();
            setConnection(null);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public boolean openConnection() {
        if (getConnection() == null) {
            try {
                String driverClass = "com.mysql.jdbc.Driver";
                String timeZonConfig = "UTC";
                String host = "www.remotemysql.com";
                String username = "7h7gMHqFJu";
                String pwd = "5OIB1qX9gt";
                String db = "7h7gMHqFJu";
                String url = String.format("jdbc:mysql://%s/%s?serverTimezone=%s&characterEncoding=latin1", host, db,
                		timeZonConfig);
                Class.forName(driverClass);
                setConnection(DriverManager.getConnection(url, username, pwd));
            } catch (ClassNotFoundException | SQLException ex) {
                throw new ApplicationException(ex);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Long> retrieveLongsFromQuery(String query, String columnLabel) throws SQLException {
        List<Long> ids = new ArrayList<>();
        ResultSet resultSet = executeQuery(query);
        if (resultSet.first()) {
            do {
                ids.add(resultSet.getLong(columnLabel));
            } while (resultSet.next());
        }
        resultSet.close();
        return ids;
    }

    @Override
    public boolean beginTransaction() {
        try {
            if (!inTransaction()) {
                this.getConnection().setAutoCommit(false);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void endTransaction() {
        try {
            if (inTransaction()) {
                this.getConnection().commit();
                this.getConnection().setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void execSQL(String sql) {
        try {
            System.out.println(">>>>>>\n\t" + sql + "\n<<<<<<");
            getConnection().createStatement().execute(sql);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public String leftEmbrace() {
        return "`";
    }

    @Override
    public String rightEmbrace() {
        return "`";
    }

    @Override
    public T retrieveOneByQuery(String query) throws SQLException {
        ResultSet resultSet = executeQuery(query);
        T entity = null;
        if (resultSet.first()) {
            entity = EntityMapper.extractOneFromResultSet(getEntityClass(), resultSet);
        }
        resultSet.close();
        return entity;
    }

    @Override
    public void setProperty(String key, String value) {
        PropertyResourceRemote propertyResource = new PropertyResourceRemote(connectionWrapper);
        propertyResource.setConnection(getConnection());
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
        PropertyResourceRemote propertyResource = new PropertyResourceRemote(connectionWrapper);
        propertyResource.setConnection(getConnection());
        Property property = propertyResource.findByKey(key);
        if (property != null) {
            return property.getValue();
        }
        return null;
    }

    @Override
    public <V extends UniqueEntity> List<V> extractFromQuery(Class<V> entityClass, String query) {
        return extractFromResultSet(entityClass, executeQuery(query));
    }

    @Override
    public Integer retrieveIntFromQuery(String columnName, String sql) throws SQLException {
        ResultSet countCursor = executeQuery(sql);
        Integer result = null;
        if (countCursor.next()) {
            result = countCursor.getInt(columnName);
        }
        return result;
    }

    public String getCurrentTimeStampFromServer() {
        try {
            String timeQuery = "SELECT CURRENT_TIMESTAMP AS time";
            String columnLabel = "time";
            String result = retrieveStringFromQuery(timeQuery, columnLabel);
            Date dateFromTimeStamp = getDateFromTimeStamp(result);
            long twoHoursGTM = 2 * (60 * 60 * 1000);
            dateFromTimeStamp.setTime(dateFromTimeStamp.getTime() + twoHoursGTM);
            return getTimeStampFromDate(dateFromTimeStamp);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public boolean isConnectionAvailable() {
        try {
            DataManager.getURLData(getAppProperty(getAppProperty("main_url")));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<String> ignoreFields() {
        Collection<String> ignoreFields = super.ignoreFields();
        ignoreFields.add("verificationId");
        return ignoreFields;
    }

    @Override
    public Collection<String> getColumnNames(Class<? extends UniqueEntity> entityClass, boolean includeEntityName) {
        Collection<String> columnNames = super.getColumnNames(entityClass, includeEntityName);
        String transientField = "verificationId";
        if (includeEntityName) {
            transientField = entityClass.getSimpleName() + "." + transientField;
        }
        columnNames.remove(transientField);
        return columnNames;
    }

    private String retrieveStringFromQuery(String query, String columnLabel) throws SQLException {
        ResultSet resultSet = executeQuery(query);
        String result = null;
        if (resultSet.first()) {
            result = resultSet.getString(columnLabel);
        }
        resultSet.close();
        return result;
    }

    private ResultSet executeQuery(String sql) {
        try {
            System.out.println(">>>>>>\n\t" + sql + "\n<<<<<<");
            return getConnection().createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }


    private <V extends UniqueEntity> List<V> extractFromResultSet(Class<V> entityClass, ResultSet resultSet) {
        List<V> entities = new ArrayList<>();
        try {
            if (resultSet.first()) {
                do {
                    entities.add(EntityMapper.extractOneFromResultSet(entityClass, resultSet));
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return entities;
    }

	@Override
	public String getAppProperty(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}
}
