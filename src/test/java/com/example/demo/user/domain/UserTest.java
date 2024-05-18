package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    public void User_는_UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull(); // Wrapper 클래스라서 기본값 null
        assertThat(user.getEmail()).isEqualTo("ohnam00@naver.com");
        assertThat(user.getNickname()).isEqualTo("ohnam");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void User_는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // given
        User user = User.builder().id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("ohnam2000")
                .address("Canada").build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("ohnam00@naver.com");
        assertThat(user.getNickname()).isEqualTo("ohnam2000");
        assertThat(user.getAddress()).isEqualTo("Canada");
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void User_는_로그인을_할_수_있고_로그인_시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder().id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.login(new TestClockHolder(1678530673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    public void User_는_유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder().id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    public void User_가_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
        // given
        User user = User.builder().id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        // then
        assertThatThrownBy(() ->
            user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);

    }
}
