package com.jeffyoung20.poormansjpa.library;

public class SqlColumInfo {
	private String fieldName;
	private Object value;
	
	public SqlColumInfo(String fieldName, Object objVal) {
		this.setFieldName(fieldName);
		this.setValue(objVal);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
