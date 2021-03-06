package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.ClassUtils;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.EntityTypeDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.repository.EntityTypeMetadataRepository;
import org.esfinge.aom.model.rolemapper.metadata.repository.FixedPropertyMetadataRepository;

public class AdapterEntityType implements IEntityType {

	private Object dsObject;

	private String packageName;

	private EntityTypeDescriptor entityTypeDescriptor;
	
	private Map<String, AdapterFixedProperty> fixedMetadataPerName = new WeakHashMap<String, AdapterFixedProperty>();

	private static Map<Object, AdapterEntityType> objectMap = new WeakHashMap<Object, AdapterEntityType>();

	private Map<String, IPropertyType> fixedPropertyTypes = new HashMap<String, IPropertyType>();

	private Map<String, RuleObject> operations = new LinkedHashMap<>();

	public AdapterEntityType(String entityTypeClass)
			throws EsfingeAOMException, ClassNotFoundException {
		this(entityTypeClass, null);
	}

	public AdapterEntityType(Class<?> clazz) throws EsfingeAOMException {
		this(clazz, null);
	}

	private AdapterEntityType(String entityTypeClass, Object dsEntityType)
			throws EsfingeAOMException, ClassNotFoundException {
		this(Class.forName(entityTypeClass), dsEntityType);
	}

	private AdapterEntityType(Class<?> clazz, Object dsEntityType)
			throws EsfingeAOMException {
		try {

			packageName = clazz.getName();
			EntityTypeDescriptor entityTypeDescriptor = (EntityTypeDescriptor) EntityTypeMetadataRepository
					.getInstance().getMetadata(clazz);
			this.entityTypeDescriptor = entityTypeDescriptor;

			Object dsObj = dsEntityType;

			if (dsObj == null) {
				dsObj = clazz.newInstance();
			}
			dsObject = dsObj;
			objectMap.put(dsObj, this);

			FixedPropertyDescriptor fixedPropertyDescriptor = FixedPropertyMetadataRepository
					.getInstance().getDescriptor();
			List<Field> fixedFields = fixedPropertyDescriptor
					.getFixedProperties(clazz);
			if (fixedFields != null) {
				for (Field f : fixedFields) {
					AdapterFixedPropertyType fixedPropertyType = new AdapterFixedPropertyType(f);
					fixedPropertyTypes.put(fixedPropertyType.getName(),fixedPropertyType);
				}
			}
			

			List<FieldDescriptor> fixedMetadataDescriptor = entityTypeDescriptor.getFixedMetadataDescriptor();
			for (FieldDescriptor fixedMetadata : fixedMetadataDescriptor)
			{
				Class proptype = fixedMetadata.getFieldClass();
				IPropertyType propertyType = new GenericPropertyType(fixedMetadata.getFieldName(), proptype);
				AdapterFixedProperty property = new AdapterFixedProperty(dsObj, propertyType);
				fixedMetadataPerName.put(fixedMetadata.getFieldName(), property);				
			}	
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	public static AdapterEntityType getAdapter(Object dsEntityType)
			throws EsfingeAOMException {
		if (dsEntityType != null) {
			if (objectMap.containsKey(dsEntityType))
				return objectMap.get(dsEntityType);
			return new AdapterEntityType(dsEntityType.getClass(), dsEntityType);
		}
		return null;
	}

	@Override
	public List<IPropertyType> getPropertyTypes() throws EsfingeAOMException {
		List<IPropertyType> propertyTypes = new ArrayList<IPropertyType>();
		try {
			if(entityTypeDescriptor.getPropertyTypesDescriptor()!= null){
				Method getPropertyTypesMethod = entityTypeDescriptor
						.getPropertyTypesDescriptor().getGetFieldMethod();
				Collection<?> dsPropertyTypes = (Collection<?>) getPropertyTypesMethod
						.invoke(dsObject);
				for (Object dsPropertyType : dsPropertyTypes) {
					propertyTypes.add(AdapterPropertyType
							.getAdapter(dsPropertyType));
				}
			}
			
			// Adding the fixed property types
			for (IPropertyType propertyType : fixedPropertyTypes.values())
				propertyTypes.add(propertyType);
			
			return propertyTypes;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void removePropertyType(IPropertyType propertyType)
			throws EsfingeAOMException {
		try {
			if (fixedPropertyTypes.containsValue(propertyType)) {
				fixedPropertyTypes.remove(propertyType);
				return;
			}

			Method removePropertyTypeMethod = entityTypeDescriptor
					.getPropertyTypesDescriptor().getRemoveElementMethod();

			if (removePropertyTypeMethod != null) {
				removePropertyTypeMethod.invoke(dsObject,
						propertyType.getAssociatedObject());
			} else {
				Method getPropertyTypesMethod = entityTypeDescriptor
						.getPropertyTypesDescriptor().getGetFieldMethod();
				Collection dsPropertyTypes = (Collection<?>) getPropertyTypesMethod
						.invoke(dsObject);
				dsPropertyTypes.remove(propertyType.getAssociatedObject());
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void addPropertyType(IPropertyType propertyType)
			throws EsfingeAOMException {

		try {
			if (!(propertyType instanceof AdapterPropertyType)) {
				Class<?> propertyTypeClass = entityTypeDescriptor
						.getPropertyTypesDescriptor().getInnerFieldClass();
				AdapterPropertyType apt = AdapterPropertyType
						.getAdapter(propertyTypeClass.newInstance());
				apt.setName(propertyType.getName());
				apt.setType(propertyType.getType());
				propertyType = apt;
			}

			Method addPropertyTypeMethod = entityTypeDescriptor
					.getPropertyTypesDescriptor().getAddElementMethod();

			if (addPropertyTypeMethod != null) {
				addPropertyTypeMethod.invoke(dsObject,
						propertyType.getAssociatedObject());
			} else {
				Method getPropertyTypesMethod = entityTypeDescriptor
						.getPropertyTypesDescriptor().getGetFieldMethod();
				Collection dsPropertyTypes = (Collection<?>) getPropertyTypesMethod
						.invoke(dsObject);
				dsPropertyTypes.add(propertyType.getAssociatedObject());
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public IEntity createNewEntity() throws EsfingeAOMException {

		AdapterEntity entity = null;

		Method createEntityMethod = entityTypeDescriptor
				.getCreateEntityMethod();

		if (createEntityMethod != null) {
			try {
				Object dsEntity = createEntityMethod.invoke(dsObject);
				entity = AdapterEntity.getAdapter(this, dsEntity);
				for (IPropertyType propertyType : getPropertyTypes()) {
					Object propertyTypeType = propertyType.getType();
					if (!propertyType.isRelationshipProperty()) {
						Class<?> propertyTypeClazz = (Class<?>) propertyTypeType;
						if (ClassUtils.isAssignable(propertyTypeClazz, Boolean.class)) {
							entity.setProperty(propertyType.getName(), false);
						} else if (ClassUtils.isAssignable(propertyTypeClazz, Number.class)) {
							entity.setProperty(propertyType.getName(), 0);
						} else if (ClassUtils.isAssignable(propertyTypeClazz, Character.class)) {
							entity.setProperty(propertyType.getName(), (char) 0);
						} else {
							entity.setProperty(propertyType.getName(), null);
						}
					} else {
						entity.setProperty(propertyType.getName(), null);
					}
				}
			} catch (Exception e) {
				throw new EsfingeAOMException("Could not create new entity", e);
			}
		} else {
			throw new EsfingeAOMException(
					"Could not create new entity because no creation method was found");
		}

		return entity;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		try {
			Method getNameMethod = entityTypeDescriptor.getNameDescriptor()
					.getGetFieldMethod();
			Object dsName = getNameMethod.invoke(dsObject);
			return (String) dsName;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void setName(String name) throws EsfingeAOMException {
		FieldDescriptor descriptor = entityTypeDescriptor.getNameDescriptor();
		if (descriptor == null) {
			throw new EsfingeAOMException(
					"Metadata \"Name\" not found in entity type");
		}
		try {
			Method setNameMethod = descriptor.getSetFieldMethod();
			setNameMethod.invoke(dsObject, name);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}

	@Override
	public void removePropertyType(String propertyName)
			throws EsfingeAOMException {

		IPropertyType propertyType = getPropertyType(propertyName);
		if (propertyType != null) {
			removePropertyType(propertyType);
		}
	}

	@Override
	public IPropertyType getPropertyType(String propertyName)
			throws EsfingeAOMException {

		for (IPropertyType type : getPropertyTypes()) {
			if (type.getName().equals(propertyName)) {
				return type;
			}
		}

		return null;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		List<IProperty> result = new ArrayList<IProperty>();
		try {
			// Metadatas
			if(entityTypeDescriptor.getMetadataDescriptor() != null){
				Method getMetadadaMethod = entityTypeDescriptor.getMetadataDescriptor().getGetFieldMethod();		
				//TODO We consider that the ds class initializes the collection objects properly
				Collection<?> dsProperties = (Collection<?>) getMetadadaMethod.invoke(dsObject);
			
				for (Object property : dsProperties)
				{
					IProperty adapterProperty = AdapterProperty.getAdapter(property);						
					result.add(adapterProperty);
				}
			}	
			
			// Fixed metadatas
			if(entityTypeDescriptor.getFixedMetadataDescriptor() != null){
				for (IProperty metadata : fixedMetadataPerName.values()) {
					result.add(metadata);
				}
			}
			
			// Map properties
			if(entityTypeDescriptor.getMapMetadataDescriptor() != null){
				Method getMetadataMapMethod = entityTypeDescriptor.getMapMetadataDescriptor().getGetFieldMethod();				
				Map dsMapMetadata = (Map<String, Object>) getMetadataMapMethod.invoke(dsObject);				
				Iterator iterator = dsMapMetadata.entrySet().iterator();				
				while(iterator.hasNext()){
					Map.Entry pair = (Map.Entry) iterator.next();	
					IProperty adapterPropertyMap = AdapterPropertyMap.getAdapter(pair.getKey(), 
							dsMapMetadata.get(pair.getKey()));					
					result.add(adapterPropertyMap);
				}
			}
			
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}		
		return result;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProperty getProperty(String metadataName) throws EsfingeAOMException {
		for (IProperty metadata : getProperties())
		{
			if (metadata.getName().equals(metadataName))
				return metadata;
		}
		return null;
	}

	@Override
	public void addOperation(String name, RuleObject rule) {
		operations.put(name, rule);		
	}

	@Override
	public RuleObject getOperation(String name) {
		return operations.get(name);
	}
}