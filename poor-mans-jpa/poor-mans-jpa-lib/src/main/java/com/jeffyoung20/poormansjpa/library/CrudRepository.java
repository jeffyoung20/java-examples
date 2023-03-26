package com.jeffyoung20.poormansjpa.library;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import javax.persistence.Column;
//import javax.persistence.Id;
//import javax.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class CrudRepository<T extends Object, T2> {
	private Logger LOGGER = LoggerFactory.getLogger(CrudRepository.class);
	//private static ObjectMapper mapper = new ObjectMapper();

	private JdbcTemplate jdbcTemplate;
	
	private Class<T> classModelData;
    
	
	/**
	 * @param classModelData class model
	 */
	public CrudRepository(Class<T> classModelData, JdbcTemplate jdbcTemplate) {
		this.classModelData = classModelData;
		this.jdbcTemplate = jdbcTemplate;
	}
	

	public List<T> findAll() throws Exception {
		T modelData = this.classModelData.newInstance();
		StringBuilder sbSql = helperGenerateSqlSelect(modelData);
		String sql = sbSql.toString();
		
		List<T> listModelData = null;
		try {
			LOGGER.info(sql);
			listModelData = jdbcTemplate.query(sql, new CrudRepoMapper(this.classModelData));
		} catch (DataAccessException e) {
			LOGGER.warn("CrudRepository.findAll() returned no results");
		}
		return listModelData;
	}

	/**
	 * @param id id
	 * @return model
	 * @throws Exception exception
	 */
	public T findById(T2 id) throws Exception {
		String querySql = genSqlSelectById(id);
		
		T modelData = null;
		try {
			List<T2> listArr = new ArrayList<T2>();
			listArr.add(id);
			Object[] arr = listArr.toArray();
			LOGGER.info(querySql);
			modelData = (T) jdbcTemplate.queryForObject(querySql, new CrudRepoMapper(this.classModelData), arr);
		} catch (DataAccessException e) {
			LOGGER.warn("CrudRepository.findById returned no results.  id: " + id);
		}
		return modelData;
	}
	
	
	/**
	 * @author j.young
	 *
	 */
	class CrudRepoMapper implements RowMapper<T>{
		private Logger LOGGER = LoggerFactory.getLogger(CrudRepoMapper.class);
		private Class<T> classModelData;
		
		/**
		 * @param classModelData class for model
		 */
		public CrudRepoMapper(Class<T> classModelData) {
			this.classModelData = classModelData;
		}
		
		@Override
		public T mapRow(ResultSet rs, int rowNumber)  {
			try {
				T modelData = this.classModelData.newInstance();
				Field[] fields = modelData.getClass().getDeclaredFields();
				for(Field fld : fields) {
					String fieldName = getFieldName(fld);
					fld.setAccessible(true);
					try {
						fld.set(modelData, rs.getObject(fieldName));
					} catch (Exception e) {
						LOGGER.warn(String.format("CrudRepoMapper Field Name Not found:  %s", fieldName));
						LOGGER.warn(e.getMessage());
					}
				}
				return modelData;
			}
			catch(Exception exc) {
				LOGGER.error(String.format("CrudRepoMapper ERROR:  %s", exc.getMessage()));
				exc.printStackTrace();
				return null;
			}
		}
	}
	

	/**
	 * @param modelData model object
	 */
	public void save(T modelData)  {
		try {
			T updRecord = findById((T2) this.getPrimaryKeyValue(modelData));
			if( updRecord != null) {
				update(modelData);
			}
			else {
				insert( modelData);
			}
		} catch (Exception e) {
			LOGGER.error("CrudRepository upsert Error:  " + e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * @param modelData model object
	 * @throws Exception exception
	 */
	public void insert(T modelData) throws Exception {
		String sqlInsert = genSqlInsert( modelData);
		LOGGER.info(sqlInsert);

		List<SqlColumInfo> listColumns = getColumnValues(modelData);
		List<Object> listColVals = listColumns.stream().map( colVal -> colVal.getValue()).collect(Collectors.toList());
		ArrayList<Object> arrayList = new ArrayList<Object>(listColVals);
		jdbcTemplate.update(sqlInsert, arrayList.toArray());
	}

	
	
	/**
	 * @param modelData model
	 * @return 
	 * @throws Exception exception
	 */
	public int delete(T modelData) throws Exception {
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(String.format("DELETE FROM "));
		if(getSchemaName(modelData) == null) {
			sbSql.append(String.format("%s ", getTableName(modelData)));
		}
		else {
			sbSql.append(String.format("%s.%s ", getSchemaName(modelData), getTableName(modelData)));
		}
		Field pkField = getPrimaryKeyColumn(modelData);
		sbSql.append(String.format("WHERE %s = ?", getFieldName(pkField)));
		String sql = sbSql.toString();
		LOGGER.info(sql);
			
		ArrayList<Object> arrayList = new ArrayList<Object>();
		arrayList.add(this.getPrimaryKeyValue(modelData));
		//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return jdbcTemplate.update(sql, arrayList.toArray());
	}
	/**
	 * @param dcrValidation dcrValidation
	 * @throws Exception 
	 */
	public void update(T modelData) throws Exception {
		try {
			String sqlUpdate = genSqlUpdate( modelData);
			LOGGER.info(sqlUpdate);

			List<SqlColumInfo> listColumns = getColumnValues(modelData);
			List<Object> listColVals = listColumns.stream().map( colVal -> colVal.getValue()).collect(Collectors.toList());
			ArrayList<Object> arrayList = new ArrayList<Object>(listColVals);
			arrayList.add(this.getPrimaryKeyValue(modelData));
			jdbcTemplate.update(sqlUpdate, arrayList.toArray());
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	/**
	 * @param id id
	 * @return sql
	 * @throws Exception exception
	 */
	public String genSqlSelectById(T2 id) throws Exception  {
		T modelData = this.classModelData.newInstance();
		StringBuilder sbSql = helperGenerateSqlSelect(modelData);

		Field pkField = getPrimaryKeyColumn(modelData);
		sbSql.append(String.format("where %s = ?", getFieldName(pkField)));
		return sbSql.toString();
	}


	private StringBuilder helperGenerateSqlSelect(T modelData) throws Exception {
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(String.format("select %s ", String.join(", ", getColumnNames(modelData))));
		if(getSchemaName(modelData) == null) {
			sbSql.append(String.format("from %s ", getTableName(modelData)));
		}
		else {
			sbSql.append(String.format("from %s.%s ", getSchemaName(modelData), getTableName(modelData)));
		}
		return sbSql;
	}
	
	/**
	 * @param dbModel model
	 * @return sql update
	 * @throws Exception exception
	 */
	public String genSqlUpdate(T modelData) throws Exception {
		List<String> listColNames = getColumnNames(modelData);
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(String.format("UPDATE "));
		if(getSchemaName(modelData) == null) {
			sbSql.append(String.format("%s SET ", getTableName(modelData)));
		}
		else {
			sbSql.append(String.format("%s.%s SET ", getSchemaName(modelData), getTableName(modelData)));
		}
		List<String> listUpdClauses = new ArrayList<String>();
		listColNames.stream().forEach(name -> listUpdClauses.add(String.format("%s = ?", name)));
		sbSql.append(String.join(", ", listUpdClauses));
		sbSql.append(String.format(" WHERE %s = ?", this.getFieldName(getPrimaryKeyColumn(modelData))));
		return sbSql.toString();
	}


	/**
	 * @param dbModel model
	 * @return sql
	 * @throws Exception exception
	 */
	public String genSqlInsert(T modelData) throws Exception {
		List<String> listColNames = getColumnNames(modelData);
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(String.format("insert into "));
		if(getSchemaName(modelData) == null) {
			sbSql.append(String.format("%s (", getTableName(modelData)));
		}
		else {
			sbSql.append(String.format("%s.%s (", getSchemaName(modelData), getTableName(modelData)));
		}
		//List<SqlColumnValue> listColumnValues = getColumnValues(modelData);
		sbSql.append(String.join(", ", listColNames));
		sbSql.append(") VALUES(");
		List<String> listQuestMarks = listColNames.stream().map( name -> "?").collect(Collectors.toList());
		String sValues = String.join(", ", listQuestMarks);
		sbSql.append(sValues);
		sbSql.append(")");
		return sbSql.toString();
	}
	
	
	
	/**
	 * @param modelData model object
	 * @return list of column names
	 * @throws Exception exception
	 */
	protected List<String> getColumnNames(T modelData) throws Exception{
		List<String> listColumnNames = new ArrayList<String>();
		Field[] fields = modelData.getClass().getDeclaredFields();
		for(Field fld : fields) {
			String fieldName = getFieldName(fld);
			listColumnNames.add(fieldName);
		}
		return listColumnNames;
	}

	/**
	 * @param modelData modelData
	 * @return list of SQLColumnValue
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	protected List<SqlColumInfo> getColumnValues(T modelData) throws Exception{
		List<SqlColumInfo> listColumnValues = new ArrayList<SqlColumInfo>();
		
		Field[] fields = modelData.getClass().getDeclaredFields();
		for(Field fld : fields) {
			//***** MetaData *****
			String fieldName = getFieldName(fld);
			//SQLDataType dataType = getSqlDataType(fld);
			
			//***** DATA *****			
			fld.setAccessible(true);
			Object objVal = fld.get(modelData);
			SqlColumInfo colVal = new SqlColumInfo(fieldName, objVal);
			listColumnValues.add(colVal);
//			if(objVal != null && dataType != SQLDataType.TIMESTAMP ) { //TODO:  **** Fix this ****
//				SqlColumnValue colVal = new SqlColumnValue(fieldName, objVal);
//				listColumnValues.add(colVal);
//			}
		}
	
		return listColumnValues;
	}
	
	
	/**
	 * @param dbModel model obj from db
	 * @return key value
	 */
	protected Field getPrimaryKeyColumn(T dbModel) {
		Field retField = null;
		Field[] fields = dbModel.getClass().getDeclaredFields();
		for(Field fld : fields) {
			//***** MetaData *****
			Id idAnnotation = fld.getAnnotation(Id.class);
			if(idAnnotation != null) {
				retField = fld;
			}
		}
		return retField;
	}
	

	/**
	 * @param modelData model data
	 * @return primary key field
	 * @throws Exception exception
	 */
	protected Object getPrimaryKeyValue(T modelData) throws Exception {
		Object objRetValue = null;
		Field[] fields = modelData.getClass().getDeclaredFields();
		for(Field fld : fields) {
			//***** MetaData *****
			Id idAnnotation = fld.getAnnotation(Id.class);
			if(idAnnotation != null) {
				fld.setAccessible(true);
				objRetValue = fld.get(modelData);
			}
		}
		return objRetValue;
	}
	
	

	/**
	 * @param field field
	 * @return field name
	 */
	protected String getFieldName(Field field) {
		String retFieldName = field.getName();
		Column columnAnnot = field.getAnnotation(Column.class);
		if(columnAnnot != null) {
			retFieldName = columnAnnot.name();
		}
		return retFieldName;
	}

	
	/**
	 * @param dbModel model obj from db
	 * @return table name
	 */
	protected String getTableName(T dbModel) {
		String className = dbModel.getClass().getName();
		String retTableName = className.substring(className.lastIndexOf('.') + 1).trim();
		Table tableAnnotation = dbModel.getClass().getAnnotation(Table.class);
		if(tableAnnotation != null) {
			retTableName = tableAnnotation.name();
		}
		return retTableName;
	}
	
	/**
	 * @param dbModel model obj from db
	 * @return schema name
	 */
	protected String getSchemaName(T dbModel){
		String retSchemaName = "";
		Table tableAnnotation = dbModel.getClass().getAnnotation(Table.class);
		if(tableAnnotation != null) {
			retSchemaName = tableAnnotation.schema();
		}
		return retSchemaName;
	}

}
