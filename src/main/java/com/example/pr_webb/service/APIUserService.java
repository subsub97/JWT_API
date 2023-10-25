package com.example.pr_webb.service;

import com.example.pr_webb.Repository.APIUserRepository;
import com.example.pr_webb.domain.APIUser;
import com.example.pr_webb.dto.APIAddUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class APIUserService {

    private final APIUserRepository apiUserRepository;

    public String save(APIAddUserDTO dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return apiUserRepository.save(APIUser.builder()
                .mid(dto.getMid())
                .mpw(encoder.encode(dto.getMpw()))
                .build()).getMid();
    }

    public APIUser findByMid(String email) {
        return apiUserRepository.findByMid(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
