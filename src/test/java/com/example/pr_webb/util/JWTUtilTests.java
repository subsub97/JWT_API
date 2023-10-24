package com.example.pr_webb.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {
    @Autowired
    private JWTUtil jwtUtil;


    @DisplayName("설정된 비밀키가 정상적으로 로딩되었습니다.")
    @Test
    public void testGenerate() {

        Map<String,Object> claimMap = Map.of("mid","ABCDE");

        String jwtStr = jwtUtil.generateToken(claimMap,1);

        log.info(jwtStr);
    }

    @DisplayName("토큰 유효성 검사합니다.")
    @Test
    public void testValidate() {

        // 유효 시간이 지난 토큰
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTc2NTE4NTMsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNjk3NjUxNzkzfQ.Y3V_E6a-O_S6I2l_XmolVVVsHXm6TTNNubl9wuZ1dA4";

        //만약 고의로 sign부분에 문자를 추가한다면 SignatureException가 발생한다.
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        log.info(claim);
    }

    @DisplayName("토큰을 발급하고 유효성까지 검사합니다.")
    @Test
    public void testAll() {
        //jwtStr 는 JWT 문자열
        String jwtStr = jwtUtil.generateToken(Map.of("mid","AAAAA","email","aaaa@bbb.com"),1);

        log.info(jwtStr);

        Map<String ,Object> claim = jwtUtil.validateToken(jwtStr);

        log.info("MID: " + claim.get("mid"));

        log.info("EMAIL: " + claim.get("email"));
    }
}
