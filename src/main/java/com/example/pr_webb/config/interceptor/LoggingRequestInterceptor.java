package com.example.pr_webb.config.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("URI: " + request.getURI());
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("HTTP Headers: " + request.getHeaders());

        // body의 내용을 출력하려면 아래 코드를 추가하세요.
        System.out.println("Request Body: " + new String(body, StandardCharsets.UTF_8));

        return execution.execute(request, body);
    }
}
