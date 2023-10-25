package com.example.pr_webb.Repository;

import com.example.pr_webb.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface APIUserRepository extends JpaRepository<APIUser, String> {

    Optional<APIUser> findByMid(String mid);
}
