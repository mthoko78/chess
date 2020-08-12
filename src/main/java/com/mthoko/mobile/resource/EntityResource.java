package com.mthoko.mobile.resource;

import static com.mthoko.mobile.util.EntityMapper.deleteByIdsQuery;
import static com.mthoko.mobile.util.EntityMapper.getInsertQuery;
import static com.mthoko.mobile.util.EntityMapper.getTimeStampFromDate;
import static com.mthoko.mobile.util.EntityMapper.joinClause;
import static com.mthoko.mobile.util.EntityMapper.setForeignKeyOnChildrenFromParentPrimaryKey;
import static com.mthoko.mobile.util.EntityMapper.setJoinField;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.mthoko.mobile.annotations.JoinColumn;
import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.EntityMapper;

public abstract class EntityResource<T extends UniqueEntity> {

	public static final String NULL = "NULL";

	private Class<T> entityType;

	public EntityResource(Class<T> entityType) {
		this.entityType = entityType;
	}

	public <V> List<V> getValues(List<T> entities, String fieldName, Class<V> fieldType) {
		if (entities.size() > 0) {
			return EntityMapper.getValues(entities, fieldName);
		}
		return new ArrayList<>();
	}

	public <V> void setValues(List<T> entities, List<V> values, String fieldName) {
		EntityMapper.setValues(entities, values, fieldName);
	}

	public Collection<String> ignoreFields() {
		ArrayList<String> strings = new ArrayList<>();
		strings.add(EntityMapper.getPrimaryField(entityType).getName());
		return strings;
	}

	public <V extends UniqueEntity> void joinChildrenToParents(String relation, String whereClause, List<V> entities,
			Map<Field, JoinColumn> joinColumnMap) {
		if (entities == null || entities.isEmpty()) {
			return;
		}
		Class<?> entityClass = entities.get(0).getClass();
		Field primaryFieldOnParent = EntityMapper.getPrimaryField(entityClass);
		for (Map.Entry<Field, JoinColumn> joinColumnEntry : joinColumnMap.entrySet()) {
			Class<? extends UniqueEntity> childClass = joinColumnEntry.getValue().targetEntity();

			String joinClause = joinClause(entityClass, childClass);

			if (relation.contains(" JOIN ")) {
				relation = relation + joinClause.substring(joinClause.indexOf(" JOIN "));
			} else {
				relation = joinClause;
			}
			String columnsSelectClause = getColumnNames(childClass, true).toString().replaceAll("[\\[\\]]", "");
			List<?> children = selectFromWhere(childClass, columnsSelectClause, relation, whereClause);
			Field foreignFieldOnChild = EntityMapper.getForeignField(entityClass, childClass);
			Field joinFieldOnParent = joinColumnEntry.getKey();
			setJoinField(entities, children, foreignFieldOnChild, primaryFieldOnParent, joinFieldOnParent);
		}
	}

	public Collection<String> getColumnNames(Class<? extends UniqueEntity> childClass, boolean includeEntityName) {
		return EntityMapper.getColumnNames(childClass, includeEntityName);
	}

	public <V extends UniqueEntity> void joinChildrenToParent(String whereClause, V parent,
			Map<Field, JoinColumn> joinColumnMap) {
		for (Map.Entry<Field, JoinColumn> joinColumnEntry : joinColumnMap.entrySet()) {
			Class<? extends UniqueEntity> childClass = joinColumnEntry.getValue().targetEntity();
			Class<T> entityClass = (Class<T>) parent.getClass();
			List<?> children = findWhereJoining(entityClass, childClass, whereClause);
			Field joinFieldOnParent = joinColumnEntry.getKey();
			setJoinField(parent, children, joinFieldOnParent);
		}
	}

	public <K extends UniqueEntity, V extends UniqueEntity> List<K> findWhereJoining(Class<V> parentEntityClass,
			Class<K> childClass, String whereClause, String... order) {
		String childColumns = columnsSelectClause(true);
		String from = joinClause(parentEntityClass, childClass);
		return selectFromWhere(childClass, childColumns, from, whereClause, order);
	}

	public <K extends UniqueEntity, V extends UniqueEntity> T findOneWhereJoining(Class<V> parentEntityClass,
			Class<K> childClass, String whereClause, String... order) {
		String childColumns = columnsSelectClause(true);
		String from = joinClause(parentEntityClass, childClass);
		return selectOneFromWhere(childColumns, from, whereClause, order);
	}

	public <V extends UniqueEntity> List<V> selectFromWhere(Class<V> entityClass, String columns, String relation,
			String whereClause, String... order) {
		if (order != null && order.length > 2) {
			whereClause += " ORDER BY " + order[0] + " " + order[1];
		}
		String select = String.format("SELECT %s FROM %s WHERE %s;", columns, relation, whereClause);
		List<V> entities = extractFromQuery(entityClass, select);
		Map<Field, JoinColumn> joinColumnMap = EntityMapper.getJoinFields(entityClass);
		if (!joinColumnMap.isEmpty()) {
			joinChildrenToParents(relation, whereClause, entities, joinColumnMap);
		}
		System.out.println("entities = " + entities);
		return entities;
	}

	private T selectOneFromWhere(String columns, String relation, String whereClause, String... order) {
		try {
			if (order != null && order.length == 2) {
				whereClause += " ORDER BY " + order[0] + " " + order[1];
			}

			String select = String.format("SELECT %s FROM %s WHERE %s;", columns, relation, whereClause);
			T entity = retrieveOneByQuery(select);
			if (entity != null) {
				Map<Field, JoinColumn> joinColumnMap = EntityMapper.getJoinFields(entity.getClass());
				if (!joinColumnMap.isEmpty()) {
					joinChildrenToParent(whereClause, entity, joinColumnMap);
				}
			}
			System.out.println("entity = " + entity);
			return entity;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public T findOneWhere(String whereClause, String... order) {
		return selectOneFromWhere(columnsSelectClause(true), getEntityName(), whereClause, order);
	}

	public String getEntityName() {
		return entityType.getSimpleName();
	}

	public List<T> findWhere(String whereClause, String... order) {
		return selectFromWhere(entityType, columnsSelectClause(true), getEntityName(), whereClause, order);
	}

	public T findById(Long id) {
		if (id != null) {
			return findOneWhere(getEntityName() + ".id = " + id);
		}
		return null;
	}

	public int countWhere(String whereClause) {
		try {
			String columnName = "total";
			String sql = String.format("SELECT COUNT(*) AS %s FROM %s WHERE %s", columnName, getEntityName(),
					whereClause);
			return retrieveIntFromQuery(columnName, sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public int countWhereJoining(Class<?> parentEntity, Class<?> childEntity, String whereClause) {
		try {
			String joinClause = joinClause(parentEntity, childEntity);
			String sql = String.format("SELECT COUNT(*) AS total FROM %s WHERE %s", joinClause, whereClause);
			String columnName = "total";
			return retrieveIntFromQuery(columnName, sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public <E extends UniqueEntity> List<Long> saveAll(List<E> entities) {
		return saveList(entities);
	}

	private <V extends UniqueEntity> List<Long> saveList(List<V> entities) {
		if (entities == null || entities.isEmpty()) {
			return new ArrayList<>();
		}
		try {
			List<Long> ids = new ArrayList<>();
			ids.addAll(persistList(entities));
			for (Map.Entry<Field, JoinColumn> columnEntry : EntityMapper.getJoinFields(entities.get(0).getClass())
					.entrySet()) {
				List<UniqueEntity> children = extractChildrenAndSetForeignKey(entities, columnEntry.getKey(),
						columnEntry.getValue());
				if (!children.isEmpty()) {
					ids.addAll(saveList(children));
				}
			}
			return ids;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	private <V extends UniqueEntity> List<UniqueEntity> extractChildrenAndSetForeignKey(List<V> entities, Field key,
			JoinColumn value) {
		List<UniqueEntity> subChildren = new ArrayList<>();
		Class<? extends UniqueEntity> targetEntity = value.targetEntity();
		Class<? extends UniqueEntity> entityClass = entities.get(0).getClass();
		for (V entity : entities) {
			List<UniqueEntity> children = EntityMapper.getChildren(entity, key);
			if (!children.isEmpty()) {
				subChildren.addAll(children);
				setForeignKeyOnChildrenFromParentPrimaryKey(entityClass, entity.getId(), targetEntity, children);
			}
		}
		return subChildren;
	}

	private <V extends UniqueEntity> List<Long> persistList(List<V> entities) throws SQLException {
		String sql = getInsertQuery(entities, ignoreFields(), leftEmbrace(), rightEmbrace()) + ";";
		execSQL(sql);
		String idsSelectQuery = "SELECT id FROM " + entities.get(0).getClass().getSimpleName()
				+ " ORDER BY id DESC LIMIT " + entities.size();
		List<Long> ids = retrieveLongsFromQuery(idsSelectQuery, "id");
		if (ids.size() != entities.size()) {
			throw new ApplicationException("Unable to complete process please try again later");
		}
		for (int i = 0; i < ids.size(); i++) {
			V entity = entities.get(i);
			Long id = ids.get(ids.size() - 1 - i);
			entity.setId(id);
			entity.setVerificationId(id);
		}
		return ids;
	}

	public <E extends UniqueEntity> Long save(E entity) {
		Class<? extends UniqueEntity> parentClass = entity.getClass();
		execSQL(getInsertQuery(entity, ignoreFields(), leftEmbrace(), rightEmbrace()));
		entity.setId(selectLastId());
		entity.setVerificationId(entity.getId());
		Map<Field, JoinColumn> joinFields = EntityMapper.getJoinFields(parentClass);
		if (!joinFields.isEmpty()) {
			for (Map.Entry<Field, JoinColumn> columnEntry : joinFields.entrySet()) {
				List<UniqueEntity> children = EntityMapper.getChildren(entity, columnEntry.getKey());
				setForeignKeyOnChildrenFromParentPrimaryKey(parentClass, entity.getId(),
						columnEntry.getValue().targetEntity(), children);
				saveList(children);
			}
		}
		return entity.getId();
	}

	public String leftEmbrace() {
		return "";
	}

	public String rightEmbrace() {
		return "";
	}

	public abstract String getProperty(String key);

	public void setProperty(String key, String value) {
		String sql = String.format("UPDATE %s SET %s = '%s' WHERE key = '%s'", Property.class.getSimpleName(),
				embrace("value"), value, key);
		execSQL(sql);
	}

	public void unsetProperty(String key) {
		String sql = String.format("DELETE FROM %s WHERE %s = '%s'", Property.class.getSimpleName(), embrace("key"),
				key);
		execSQL(sql);
	}

	public String embrace(String labelName) {
		return leftEmbrace() + labelName + rightEmbrace();
	}

	public Class<T> getEntityClass() {
		return entityType;
	}

	public Map<String, Long> extractVerification(UniqueEntity entity) {
		Map<String, Long> verification = new HashMap<>();
		if (entity != null) {
			verification.put(entity.getUniqueIdentifier(), entity.getVerificationId());
		}
		return verification;
	}

	public Map<String, Long> extractVerification(List<? extends UniqueEntity> entities) {
		Map<String, Long> verification = new HashMap<>();
		for (UniqueEntity entity : entities) {
			verification.put(entity.getUniqueIdentifier(), entity.getVerificationId());
		}
		return verification;
	}

	public void deleteAll(List<T> entities) {
		if (entities.isEmpty()) {
			return;
		}
		Stack<List<? extends UniqueEntity>> allDescendants = getAllDescendants(entities);
		String sql = "";
		while (!allDescendants.empty()) {
			List<? extends UniqueEntity> descendants = allDescendants.pop();
			if (!descendants.isEmpty()) {
				sql += deleteByIdsQuery(descendants);
			}
		}
		sql += deleteByIdsQuery(entities);
		execSQL(sql);
	}

	public void delete(T entity) {
		List<T> entityAsList = new ArrayList<>();
		entityAsList.add(entity);
		deleteAll(entityAsList);
	}

	public boolean existsWith(long id) {
		return countById(id) > 0;
	}

	public List<T> retrieveAll(String... order) {
		String entityName = getEntityName();
		String primaryFieldName = EntityMapper.getPrimaryField(getEntityClass()).getName();
		String whereClause = String.format("%s.%s = %s", entityName, primaryFieldName, primaryFieldName);
		return findWhere(whereClause, order);
	}

	public void closeConnectionIf(boolean isOpenConnection) {
		if (isOpenConnection) {
			closeConnection();
		}
	}

	public void endTransactionIf(boolean inTransaction) {
		if (inTransaction) {
			endTransaction();
		}
	}

	public String getCurrentTimeStamp() {
		return getTimeStampFromDate(new Date());
	}

	public String columnsSelectClause(boolean includeEntityName) {
		return getColumnNames(getEntityClass(), includeEntityName).toString().replaceAll("[\\[\\]]", "");
	}

	public Long selectLastId() {
		String selectLastId = " SELECT MAX(id) AS lastId FROM " + getEntityName() + ";";
		long result = -1;
		try {
			result = retrieveIntFromQuery("lastId", selectLastId);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
		return result;
	}

	public int countById(long id) {
		return countWhere(String.format("%s.id = %s", getEntityName(), id));
	}

	public void deleteById(Long id) {
		delete(findById(id));
	}

	public void deleteByIdsIn(List<Long> ids) {
		String idsAsValues = ids.toString().replace('[', '(').replace(']', ')');
		deleteAll(findWhere(getEntityName() + ".id IN " + idsAsValues));
	}

	public void deleteWhere(String whereClause) {
		deleteAll(findWhere(whereClause));
	}

	public void update(T entity) {
		updateEntity(entity);
	}

	public void updateAll(List<T> entities) {
		updateEntities(entities);
	}

	private void updateEntities(List<T> entities) {
		String updateQuery = getUpdateQuery(entities);
		if (!updateQuery.isEmpty()) {
			execSQL(updateQuery);
		}
	}

	private String getUpdateQuery(List<T> entities) {
		Stack<List<? extends UniqueEntity>> allDescendants = getAllDescendants(entities);
		allDescendants.push(entities);
		StringBuilder sql = new StringBuilder();
		while (!allDescendants.isEmpty()) {
			List<? extends UniqueEntity> subList = allDescendants.pop();
			for (UniqueEntity entity : subList) {
				sql.append(EntityMapper.getUpdateQuery(entity, ignoreFields(), leftEmbrace(), rightEmbrace()))
						.append(";");
			}
		}
		return sql.toString();
	}

	private Stack<List<? extends UniqueEntity>> getAllDescendants(List<T> entities) {
		Stack<List<? extends UniqueEntity>> allChildren = new Stack<>();
		loadAllDescendants(entities, allChildren);
		return allChildren;
	}

	private <V extends UniqueEntity> void loadAllDescendants(List<V> entities,
			Stack<List<? extends UniqueEntity>> allChildren) {
		Class<V> entityClass = (Class<V>) entities.get(0).getClass();
		for (Field foreignField : EntityMapper.getJoinFields(entityClass).keySet()) {
			for (V entity : entities) {
				List<UniqueEntity> children = EntityMapper.getChildren(entity, foreignField);
				if (!children.isEmpty()) {
					allChildren.push(children);
					loadAllDescendants(children, allChildren);
				}
			}
		}
	}

	private <V extends UniqueEntity> void updateEntity(V entity) {
		execSQL(EntityMapper.getUpdateQuery(entity, ignoreFields(), leftEmbrace(), rightEmbrace()));
	}

	public abstract String getAppProperty(String propertyName);

	public abstract boolean openConnection();

	public abstract void closeConnection();

	public abstract boolean beginTransaction();

	public abstract void endTransaction();

	public abstract void execSQL(String sql);

	public abstract <V extends UniqueEntity> List<V> extractFromQuery(Class<V> entityClass, String query);

	public abstract T retrieveOneByQuery(String query) throws SQLException;

	public abstract Integer retrieveIntFromQuery(String columnName, String sql) throws SQLException;

	public abstract List<Long> retrieveLongsFromQuery(String query, String labelName) throws SQLException;

	public abstract boolean inTransaction();

	public abstract void rollBack();

	public <K extends UniqueEntity> List<Long> getIds(List<K> entities) {
		return EntityMapper.getIds(entities);
	}
}
