package com.example.pr_webb.service;

import com.example.pr_webb.config.interceptor.LoggingRequestInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.Collections;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


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

        //구글에서 토큰을 가져오기위한 메서드
        String accessToken = getAccessToken(code, registrationId);
        log.info("AccessToken issued by Google ---->" + accessToken);
        System.out.println("accessToken = " + accessToken);
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".client-id");
        String clientSecret = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("spring.security.oauth2.client.provider." + registrationId + ".token-uri");

        MultiValueMap<String ,String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "postmessage");
        params.add("grant_type", "authorization_code");

//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUri)
//                .queryParam("code", authorizationCode)
//                .queryParam("client_id", clientId)
//                .queryParam("client_secret", clientSecret)
//                .queryParam("redirect_uri", "postmessage")
//                .queryParam("grant_type", "authorization_code");
//
//        URI finalUri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params,headers);


        //restTemplateWithInterceptor.setInterceptors(Collections.singletonList(new LoggingRequestInterceptor()));
        //ResponseEntity<JsonNode> responseNode2 = restTemplateWithInterceptor.exchange(finalUri, HttpMethod.POST, entity, JsonNode.class);
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
}
//https://oauth2.googleapis.com/token?code=4/0AfJohXk1EKb87Ip9lm6Mt3EowXJnxunoO9cvQQmMuQWSfKiDQ13vRupErcxoiSW729oSMw&client_id=173132819951-ku777uluaf8nkeisn224vh1hfg8mdurm.apps.googleusercontent.com&client_secret=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&redirect_uri=http://localhost:8080/auth/google/callback&grant_type=authorization_code