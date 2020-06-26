package com.hmp.jwt.utils;

//Since usually ones imports StringUtils just for using isBlank/isNotBlank method
public class StringUtils {
	
	public static boolean isEmpty(String value) {
		return value == null || value.isBlank();
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

}
