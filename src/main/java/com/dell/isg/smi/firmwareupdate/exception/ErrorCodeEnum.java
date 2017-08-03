/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.firmwareupdate.exception;

import com.dell.isg.smi.commons.elm.messaging.IMessageEnum;

/**
 * @author Michael_Regert
 *
 */
public enum ErrorCodeEnum implements IMessageEnum {
	ENUM_INACCESSIBLE_CATALOG("ENUM_INACCESSIBLE_CATALOG", 300001),
	ENUM_INACCESSIBLE_IP("ENUM_INACCESSIBLE_IP", 300002);
	
	private Integer id;
	private String value;
	
	private ErrorCodeEnum(String value, Integer id) {
		this.value = value;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.isg.smi.commons.elm.messaging.IMessageEnum#getValue()
	 */
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.isg.smi.commons.elm.messaging.IMessageEnum#getId()
	 */
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.value;
	}
}
