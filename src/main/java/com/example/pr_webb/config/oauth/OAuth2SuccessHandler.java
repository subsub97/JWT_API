package com.example.pr_webb.config.oauth;

import com.example.pr_webb.Repository.APIUserRepository;
import com.example.pr_webb.domain.APIUser;
import com.example.pr_webb.dto.APIAddUserDTO;
import com.example.pr_webb.service.APIUserService;
import com.example.pr_webb.util.JWTUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Login SuccessHandler------------------------------");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //응답은 JSON형태

        log.info(authentication);
        log.info(authentication.getName()); //username

        Map<String , Object> claim = Map.of("mid",authentication.getName());
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);

        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        Gson gson = new Gson();

        Map<String ,Object> keyMap = Map.of(
                "accessToken" , accessToken,
                "refreshToken" , refreshToken);

        String jsonStr = gson.toJson(keyMap); //gson이 json으로 만들어주는듯!?

        response.getWriter().println(jsonStr);


    }
 }


