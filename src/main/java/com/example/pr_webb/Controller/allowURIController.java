package com.example.pr_webb.Controller;

import com.example.pr_webb.dto.APIAuthCodeDTO;
import com.example.pr_webb.service.APILoginService;
import com.example.pr_webb.util.JWTUtil;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.TokenResponse;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@Log4j2
@RequestMapping(value = "/auth",produces = "application/json")
public class allowURIController {
    @Autowired
    private final JWTUtil jwtUtil = new JWTUtil();

    APILoginService apiLoginService;

    public allowURIController(APILoginService apiLoginService) {
        this.apiLoginService = apiLoginService;
    }

    @PostMapping("/code/{registrationId}/callback")
    public void googleLogin(@RequestBody APIAuthCodeDTO code, @PathVariable String registrationId) {
        // code값으 DTO를 거치지 않으면 JSON형태의 문자열로 들어오게 된다.
        apiLoginService.socialLogin(code.getCode(), registrationId);
    }
}



