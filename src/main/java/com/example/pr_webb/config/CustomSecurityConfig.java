package com.example.pr_webb.config;//package com.example.pr_jwt.config;

import com.example.pr_webb.Repository.APIUserRepository;
import com.example.pr_webb.config.oauth.OAuth2SuccessHandler;
import com.example.pr_webb.config.oauth.OAuth2UserCustomService;
import com.example.pr_webb.service.APIUserDetailsService;
import com.example.pr_webb.security.filter.APILoginFilter;
import com.example.pr_webb.security.filter.RefreshTokenFilter;
import com.example.pr_webb.security.filter.TokenCheckFilter;
import com.example.pr_webb.security.handler.APILoginSuccessHandler;
import com.example.pr_webb.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    //주입
    private final APIUserDetailsService apiUserDetailsService;

    private final JWTUtil jwtUtil;

    @Autowired
    private OAuth2UserCustomService oAuth2UserCustomService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("--------web configure--------");

        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        log.info("--------------------------------configure--------------------------------");
        http.cors().configurationSource(corsConfigurationSource());

        //AuthenticationManager 설정 (실제 인증처리를 담당)
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);


        authenticationManagerBuilder
                .userDetailsService(apiUserDetailsService)
                .passwordEncoder(passwordEncoder());

        //Get AuthenticationManager
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        // 반드시 필요 (현재의 HttpSecurity 객체에 설정
        http.authenticationManager(authenticationManager);

        //APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        // SuccessHandler Setting
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // APILoginFilter의 위치 고정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        //api로 시작하는 모든 경로는 TokenCheckFilter 동작
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );

        //refreshToken 호출처리
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken",jwtUtil),
                TokenCheckFilter.class);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// <- 세션을 사용하지 않음





        //OauthLogin 로직
        http.oauth2Login()
                .successHandler(new OAuth2SuccessHandler(jwtUtil)) //인증 성공 시 실행할 핸들러
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/files/apiLogin.html");


        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }

    @Bean // cors Error 해결 빈 등록
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
