package com.feilu.api.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable{

	@Serial
	private static final long serialVersionUID = -7102129155309986956L;

	private T result;
	private String state;
	private int loginStatus;
	private String imageHost;
	private String message;
	private Object data;

}
