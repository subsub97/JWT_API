package com.example.pr_webb.config.oauth;

import com.example.pr_webb.Repository.APIUserRepository;
import com.example.pr_webb.domain.APIUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService  extends DefaultOAuth2UserService {
    // DefaultOAuth2UserService 를 상속밥받아야 loadUser메서드 오버라이딩이 가능함

    private final APIUserRepository apiUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //요청을 바탕으로 유저 정보를 담은 객체 반환
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    private APIUser saveOrUpdate(OAuth2User oAuth2User) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name =(String) attributes.get("name");


        APIUser apiUserEntity = apiUserRepository.findByEmail(email)
                .orElse(null);

        if(apiUserEntity == null) {
            apiUserEntity = APIUser.builder()
                    .mid(email)
                    .mpw(bCryptPasswordEncoder.encode("비밀번호"))
                    .nickname(name)
                    .build();
            apiUserRepository.save(apiUserEntity);
        }

        return apiUserEntity;



    }




}
