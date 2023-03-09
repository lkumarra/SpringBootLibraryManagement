package com.hospitalmanagement.hospitalmanagement.services.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResponseStatusUtility {

	public ResponseStatusException responseStatusException(HttpStatus httpStatus, String message) {
		return new ResponseStatusException(httpStatus, message);
	}

}
