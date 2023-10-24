package com.example.pr_webb.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class APIUser {

    @Id
    private String mid;
    private String mpw;

    public void changePw(String mpw){
        this.mpw = mpw;
    }
}

//APIUser 는 Access Key를 발급받을 때 자신의 mid와 mpw를 이용하므로 다른 정보들 없이
// 구성하였습니다.