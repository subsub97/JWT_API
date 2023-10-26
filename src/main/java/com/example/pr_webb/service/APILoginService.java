package com.example.pr_webb.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@Service
@Log4j2
public class APILoginService {

    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public APILoginService(Environment env) {
        this.env = env;
    }

    public void socialLogin(String code,String registrationId) {
        log.info("SocialLogin code ---->" + code);

        String accessToken = getAccessToken(code, registrationId);
        log.info("AccessToken issued by Google ---->" + accessToken);
        System.out.println("accessToken = " + accessToken);
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".client-id");
        String clientSecret = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("spring.security.oauth2.client.provider." + registrationId + ".token-uri");

        System.out.println("tokenURI : " + tokenUri);
        System.out.println("ClientSecret : " + clientSecret);
        System.out.println("RedirectUri : " + redirectUri);
        System.out.println("ClientID :" + clientId);

        MultiValueMap<String ,String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);
        System.out.println(entity.getBody());
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);

        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_Token").asText();
    }
}
