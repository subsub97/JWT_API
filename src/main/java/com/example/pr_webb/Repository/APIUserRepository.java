package com.example.pr_webb.Repository;

import com.example.pr_webb.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}
