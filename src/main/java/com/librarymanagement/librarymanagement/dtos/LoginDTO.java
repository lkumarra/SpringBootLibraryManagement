package com.librarymanagement.librarymanagement.dtos;

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
