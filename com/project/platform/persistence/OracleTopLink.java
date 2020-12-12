package com.project.platform.persistence;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;

import com.project.interfacebuilder.InterfaceException;

public class OracleTopLink extends JPAProvider {
	
	public OracleTopLink(final EntityManager manager, final DataSource dataSource){
		super(manager,dataSource);
	}

	@Override
	public Class<?> getDelegateClass() {
		return org.eclipse.persistence.internal.jpa.EntityManagerImpl.class;
	}

	@Override
	public Class<?> getFactoryClass() {
		return org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl.class;
	}

	@Override
	public int getColumnSize(Class<?> type, String fieldName) throws InterfaceException {
		EntityManagerImpl impl;
		try {
			impl = (EntityManagerImpl) getEntityManager().getDelegate();
			Session session = impl.getActiveSession();
			ClassDescriptor desc = session.getClassDescriptor(type);
			DatabaseMapping mapping = desc.getMappingForAttributeName(fieldName);
			DatabaseField field = mapping.getField();
			String tableName = field.getTable().getName();
			String tableFieldName = field.getName();
			return getColumnSize(tableName, tableFieldName);
		} catch (SQLException e) {
			throw new InterfaceException(e);
		}
	}

	@Override
	public JPAVersion getLatestSupportedVersion() {
		return JPAVersion.VERSION_2;
	}

}
