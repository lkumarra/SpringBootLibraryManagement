package com.librarymanagement.librarymanagement.services.Impl;

import com.librarymanagement.librarymanagement.utils.ResponseStatusUtility;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.librarymanagement.librarymanagement.dtos.LoginDTO;
import com.librarymanagement.librarymanagement.services.LoginService;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Value("${token.url}")
    private String tokenUrl;

    @Autowired
    ResponseStatusUtility responseStatusUtil;

    /**
     * Return the token
     *
     * @param clientId     : Client Id
     * @param clientSecret : Client Secret
     * @return Access token
     */
    @Override
    public LoginDTO login(String clientId, String clientSecret) {
        try {
            LoginDTO loginDTO = new LoginDTO();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "client_credentials");
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCodeValue() == 200) {
                JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(response.getBody()));
                JSONObject jsonObject = new JSONObject(jsonTokener);
                String accessToken = jsonObject.getString("access_token");
                int expiredIn = jsonObject.getInt("expires_in");
                int refreshExpiresIn = jsonObject.getInt("refresh_expires_in");
                String tokenType = jsonObject.getString("token_type");
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
