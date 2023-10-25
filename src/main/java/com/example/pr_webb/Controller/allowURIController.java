package com.example.pr_webb.Controller;

import com.example.pr_webb.util.JWTUtil;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.TokenResponse;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@NoArgsConstructor
@RestController
@Log4j2
public class allowURIController {
    @Autowired
    private final JWTUtil jwtUtil = new JWTUtil();
    @PostMapping("/auth/google/callback")
    public ResponseEntity<String> googleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // code를 받아왔는지 확인한다.
        log.info("## [REQUEST] code = {}", request.getParameter("code"));

        Map<String , Object> claim = Map.of("mid","testID");
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);

        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        Gson gson = new Gson();

        Map<String ,Object> keyMap = Map.of(
                "accessToken" , accessToken,
                "refreshToken" , refreshToken);

        String jsonStr = gson.toJson(keyMap); //gson이 json으로 만들어주는듯!?


        System.out.println("djdjdjdjdjdjdjdjdjdjdjdjd" + jsonStr);

        return new ResponseEntity<>(jsonStr, HttpStatus.OK);
    }
}
//        try{
//            //Access Token 얻기
//            String accessToken = getGoogleAccessToken(request,response);
//            // 사용자 정보 얻기
//            getGoogleUserInfo(accessToken);
//        } catch (Exception e) {
//            log.error(e);
//            throw e;
//        }