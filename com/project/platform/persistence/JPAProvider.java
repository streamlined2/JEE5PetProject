package com.project.platform.persistence;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import com.project.Startup;
import com.project.interfacebuilder.InterfaceException;

public abstract class JPAProvider {
	
	private EntityManager manager;
	
	protected DataSource dataSource;
	
	protected JPAProvider(final EntityManager manager, final DataSource dataSource){
		this.manager = manager;
		this.dataSource = dataSource;
	}
	
	protected EntityManager getEntityManager() {
		return manager;
	}
	
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	public static EntityManagerFactory getEntityManagerFactory() throws NamingException {
		return Persistence.createEntityManagerFactory(Startup.UNIT_NAME);
	}
	
	public enum JPAVersion { VERSION_1, VERSION_2 };
	
	public final static JPAVersion getJPAVersion() throws InterfaceException{
		try {
			getEntityManagerFactory().getClass().getMethod("getCriteriaBuilder", new Class<?>[]{});
		} catch (NoSuchMethodException e) {
			return JPAVersion.VERSION_1;
		} catch (SecurityException e) {
			return JPAVersion.VERSION_2;
		} catch (NamingException e) {
			throw new InterfaceException(e);
		}
		return JPAVersion.VERSION_2;
	}
	
	protected DatabaseMetaData getDatabaseMetaData() throws SQLException{
		return getDataSource().getConnection().getMetaData();
	}
	
	private String getCatalogName() throws SQLException{
		return getDataSource().getConnection().getCatalog();
	}

	protected int getColumnSize(String tableName, String columnName) throws SQLException{
		DatabaseMetaData metaData=getDatabaseMetaData();
		ResultSet r=metaData.getColumns(getCatalogName(), null, tableName, columnName);
		while(r.next()){
			if(r.getString("COLUMN_NAME").equalsIgnoreCase(columnName)){
				return r.getInt("COLUMN_SIZE");
			}
		};
		return -1;
	}
	
	public abstract Class<?> getDelegateClass();
	
	public abstract Class<?> getFactoryClass();
	
	public abstract JPAVersion getLatestSupportedVersion();
	
	public abstract int getColumnSize(Class<?> type, String fieldName) throws InterfaceException;
	
}
