package com.librarymanagement.librarymanagement.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.librarymanagement.librarymanagement.entities.LoginDTO;
import com.librarymanagement.librarymanagement.services.LoginService;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Service
public class LoginServiceImpl implements LoginService {

	@Value("${token.url}")
	private String tokenUrl;

	@Autowired
	ResponseStatusUtility responseStatusUtil;

	@Override
	public LoginDTO login(String clientId, String clientSecret) {
		try {
			LoginDTO loginDTO = new LoginDTO();
			Response response = RestAssured.given().contentType(ContentType.URLENC).formParam("client_id", clientId)
					.formParam("client_secret", clientSecret).formParam("grant_type", "client_credentials")
					.post(tokenUrl);
			if (response.getStatusCode() == 200) {
				String accessToken = response.jsonPath().getString("access_token");
				int expiredIn = response.jsonPath().getInt("expires_in");
				int refreshExpiresIn = response.jsonPath().getInt("refresh_expires_in");
				String tokenType = response.jsonPath().getString("token_type");
				loginDTO.setAccessToken(accessToken);
				loginDTO.setExpiredIn(expiredIn);
				loginDTO.setRefreshExpiresIn(refreshExpiresIn);
				loginDTO.setTokenType(tokenType);
				return loginDTO;
			} else {
				throw responseStatusUtil.responseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Something went wrong please try again after some time");
			}
		} catch (Exception e) {
			throw responseStatusUtil.responseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Something went wrong please try again after some time");
		}
	}

}
