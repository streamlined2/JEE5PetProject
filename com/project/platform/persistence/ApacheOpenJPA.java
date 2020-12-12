package com.project.platform.persistence;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.MappingRepository;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerSPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.project.interfacebuilder.InterfaceException;

public class ApacheOpenJPA extends JPAProvider {
	
	public ApacheOpenJPA(final EntityManager manager,final DataSource dataSource){
		super(manager,dataSource);
	}

	@Override
	public Class<?> getDelegateClass() {
		return org.apache.openjpa.persistence.EntityManagerImpl.class;
	}

	@Override
	public Class<?> getFactoryClass() {
		return org.apache.openjpa.persistence.EntityManagerFactoryImpl.class;
	}
	
	private Column getApacheJPAColumn(Class<?> type, String fieldName) throws InterfaceException {
		OpenJPAEntityManager em = OpenJPAPersistence.cast(getEntityManager());
		OpenJPAEntityManagerSPI spi=(OpenJPAEntityManagerSPI)em;
		OpenJPAConfiguration conf=spi.getConfiguration();
		MetaDataRepository repository=conf.getMetaDataRepositoryInstance();
		MappingRepository mappingRepository=(MappingRepository)repository;
		ClassMapping classMapping=mappingRepository.getMapping(type, null, true);
		FieldMapping fieldMapping=classMapping.getFieldMapping(fieldName);
		return fieldMapping.getColumns()[0];
	}

	@Override
	public int getColumnSize(Class<?> type, String fieldName) throws InterfaceException {
		try {
			return getColumnSize(
					getApacheJPAColumn(type, fieldName).getTable().getFullName().toUpperCase(), 
					getApacheJPAColumn(type, fieldName).getName().toUpperCase());
		} catch (SQLException e) {
			throw new InterfaceException(e);
		}
	}

	@Override
	public JPAVersion getLatestSupportedVersion() {
		return JPAVersion.VERSION_1;
	}

}
