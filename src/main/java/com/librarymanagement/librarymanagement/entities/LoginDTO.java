package com.librarymanagement.librarymanagement.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	
	private String accessToken;
	private int expiredIn;
	private int refreshExpiresIn;
	private String tokenType;

}
