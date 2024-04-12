package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean // 스프링에 있는 JavaMailSender 라는 Bean 객체를 Mock 으로 선언된 객체로 덮어쓰기 하는 것 -> 테스트 실행 시 MockBean 값이 주입되어 실행
    private JavaMailSender mailSender;

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "ohnam00@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

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
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        UserEntity result = userService.getById(3);

        // then
        assertThat(result.getNickname()).isEqualTo("ohnam");
    }

    @Test
    void getById_은_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(4);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하면_유저를_생성할_수_있다() {

        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("ohnam00@naver.com")
                .address("Seoul")
                .nickname("ohnam")
                .build();

        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreateDto);

        // then
        assertThat(result.getId()).isNotNull(); // id 가 잘 생성되었는지 보는 것
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING); // 처음 회원 가입하면, PENDING 상태인 것
        // 랜덤 값도 잘 만들어졌는지 보고 싶은데, UUID 랜덤 값을 테스트할 방법이 없음
        // assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

}