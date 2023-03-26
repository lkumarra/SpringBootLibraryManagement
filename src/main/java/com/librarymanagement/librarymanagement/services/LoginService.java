package com.librarymanagement.librarymanagement.services;

import com.librarymanagement.librarymanagement.dtos.LoginDTO;

public interface LoginService {
	
	LoginDTO login(String clientId, String clientSecret);

}
