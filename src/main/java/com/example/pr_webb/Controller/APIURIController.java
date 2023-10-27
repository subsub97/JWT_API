//package com.example.pr_webb.Controller;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.nimbusds.oauth2.sdk.TokenResponse;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.net.URI;
//
//@RestController
//public class APIURIController {
//    private final String clientId = "613421683569-m1fssvetfcrmp1kf8sip6rl2n9u3uh3d.apps.googleusercontent.com";
//    private final String clientSecret = "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";
//    private final String redirectUri = "postmessage";
//
//    @PostMapping("/callback")
//    public TokenResponse callback(@RequestBody String code) throws IOException {
//        GoogleCredentials credentials = GoogleCredentials
//                .newBuilder()
//                .setClientId(clientId)
//                .setClientSecret(clientSecret)
//                .setRedirectUri(redirectUri)
//                .build();
//
//        TokenResponse tokenResponse = credentials
//                .getRequestMetadata(new URI(redirectUri))
//                .getTokenValue();
//
//        return tokenResponse;
//}
