package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EsItemSkuImage implements Serializable {

	@Serial
	private static final long serialVersionUID = 7029291894974616497L;

	private String propPath;
	private String colorImage;

}
