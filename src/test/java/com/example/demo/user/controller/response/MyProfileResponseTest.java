package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    public void User_로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("ohnam00@naver.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("ohnam");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}