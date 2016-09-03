package net.sf.esfinge.aom.example.medical;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class ObservationType {

	@Name
	private String observationId;
	
	@PropertyTypeType
	private Object observationType;

	public String getObservationId() {
		return observationId;
	}

	public void setObservationId(String observationId) {
		this.observationId = observationId;
	}

	public Object getObservationType() {
		return observationType;
	}

	public void setObservationType(Object observationType) {
		this.observationType = observationType;
	}
	
	
}