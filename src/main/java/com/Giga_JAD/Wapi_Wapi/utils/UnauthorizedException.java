package com.Giga_JAD.Wapi_Wapi.utils;


public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
        super(message);
    }
}