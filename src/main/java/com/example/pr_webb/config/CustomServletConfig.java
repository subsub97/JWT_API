package com.example.pr_webb.config;//package com.example.pr_jwt.config;
// 나중에 파라미터 타입의 변환을 추가할 수 있도록 클래스를 생성한다.(작성하는 프로젝트의 실행은 이 단계까지 설정되어야 에러가 발생하지 않는다.)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc

public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/");
    }

}
