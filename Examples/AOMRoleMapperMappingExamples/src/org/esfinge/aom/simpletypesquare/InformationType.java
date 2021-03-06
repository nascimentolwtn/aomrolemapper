package org.esfinge.aom.simpletypesquare;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class InformationType {
	
	public InformationType(String name, Object type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public InformationType() {
		super();
	}

	@Name
	private String name;
	
	@PropertyTypeType
	private Object type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}
}
