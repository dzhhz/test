package com.feilu.api.common.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class EsItemAttr implements Serializable{

	@Serial
	private static final long serialVersionUID = -7716044973287692888L;

	/**
	 * 属性:数字
	 */
	private String attrKey;
	/**
	 * 属性值:数字，用空格隔开
	 */
	private String attrValue;

    public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

    public void setAttrValue(String attrValue) {
		if(attrValue.indexOf("[") >= 0){
			attrValue = attrValue.substring(1, attrValue.length()-1);
		}
		this.attrValue = attrValue;
	}
	
}
