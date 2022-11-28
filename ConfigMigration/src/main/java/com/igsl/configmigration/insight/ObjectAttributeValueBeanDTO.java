package com.igsl.configmigration.insight;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igsl.configmigration.JiraConfigDTO;
import com.igsl.configmigration.JiraConfigUtil;
import com.riadalabs.jira.plugins.insight.services.model.ObjectAttributeValueBean;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class ObjectAttributeValueBeanDTO extends JiraConfigDTO {

	private Long id;
	private String additionalValue;
	private Boolean booleanValue;
	private Date dateValue;
	private Double doubleValue;
	private Integer integerValue;
	private String invalidValue;
	private Integer referencedObjectBeanId;
	private String textValue;
	private Object value;
	
	@Override
	public void fromJiraObject(Object obj, Object... params) throws Exception {
		ObjectAttributeValueBean o = (ObjectAttributeValueBean) obj;
		this.additionalValue = o.getAdditionalValue();
		this.booleanValue = o.getBooleanValue();
		this.dateValue = o.getDateValue();
		this.doubleValue = o.getDoubleValue();
		this.id = o.getId();
		this.integerValue = o.getIntegerValue();
		this.invalidValue = o.getInvalidValue();
		this.referencedObjectBeanId = o.getReferencedObjectBeanId();
		this.textValue = o.getTextValue();
		this.value = o.getValue();
	}

	@Override
	public String getUniqueKey() {
		return Long.toString(this.getId());
	}

	@Override
	public String getInternalId() {
		return Long.toString(this.getId());
	}

	@Override
	protected List<String> getCompareMethods() {
		return Arrays.asList(
				"getAdditionalValue",
				"getBooleanValue",
				"getDateValue",
				"getDoubleValue",
				"getIntegerValue",
				"getInvalidValue",
				"getReferencedObjectBeanId",
				"getTextValue",
				"getValue");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAdditionalValue() {
		return additionalValue;
	}

	public void setAdditionalValue(String additionalValue) {
		this.additionalValue = additionalValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public String getInvalidValue() {
		return invalidValue;
	}

	public void setInvalidValue(String invalidValue) {
		this.invalidValue = invalidValue;
	}

	public Integer getReferencedObjectBeanId() {
		return referencedObjectBeanId;
	}

	public void setReferencedObjectBeanId(Integer referencedObjectBeanId) {
		this.referencedObjectBeanId = referencedObjectBeanId;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public Class<? extends JiraConfigUtil> getUtilClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
