package com.librarymanagement.librarymanagement.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResponseStatusUtility {

	public ResponseStatusException responseStatusException(HttpStatus httpStatus, String message) {
		return new ResponseStatusException(httpStatus, message);
	}

	public ResponseStatusException badRequestStatusException(String message) {
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}

	public ResponseStatusException notFoundStatusException(String message) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
	}

	public ResponseStatusException internalServerErrorStatusException(String message) {
		return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public ResponseStatusException conflictStatusException(String message) {
		return new ResponseStatusException(HttpStatus.CONFLICT, message);
	}

}
