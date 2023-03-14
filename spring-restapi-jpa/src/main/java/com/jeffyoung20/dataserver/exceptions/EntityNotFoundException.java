package com.jeffyoung20.dataserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super();
	}

//	public EntityNotFoundException(String message) {
//		super(message);
//	}
}
