package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserServiceImpl.builder()
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .certificationService(new CertificationService(fakeMailSender))
                .build();


        fakeUserRepository.save(User.builder()
                .id(3L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .id(4L)
                .email("ohnam01@naver.com")
                .nickname("ohnam01")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "ohnam00@naver.com";

        // when
        User result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("ohnam");
    }

    @Test
    void getByEmail_은_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        String email = "ohnam01@naver.com";

        // when
        // then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        User result = userService.getById(3);

        // then
        assertThat(result.getNickname()).isEqualTo("ohnam");
    }

    @Test
    void getById_은_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            User result = userService.getById(4);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하면_유저를_생성할_수_있다() {

        // given
        UserCreate userCreate = UserCreate.builder()
                .email("ohnam00@naver.com")
                .address("Seoul")
                .nickname("ohnam")
                .build();

        // when
        User result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull(); // id 가 잘 생성되었는지 보는 것
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING); // 처음 회원 가입하면, PENDING 상태인 것
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"); // FIXME
    }

    @Test
    void userUpdateDto_를_이용하면_유저를_수정할_수_있다() {

        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("ohnam03")
                .build();

        // when
        userService.update(3, userUpdate);

        // then
        User result = userService.getById(3);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("Incheon");
        assertThat(result.getNickname()).isEqualTo("ohnam03");
    }

    @Test
    void user_를_로그인_시키면_마지막_로그인_시간이_변경된다() {

        // given
        // when
        userService.login(3);

        // then
        User result = userService.getById(3);
        assertThat(result.getLastLoginAt()).isEqualTo(1678530673958L); // FIXME
    }

    // PENDING 상태의 사용자는 인증코드로 활성화시킬 수 있다
    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {

        // given
        // when
        userService.verifyEmail(4, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        User result = userService.getById(4);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    // 인증이 실패하는 경우
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(4, "wrong!");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}