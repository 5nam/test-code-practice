package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

//    @Test
//    void UserRepository_가_제대로_연결되었다() { // jpa 가 h2 랑 제대로 연동되는지 확인
//        // given
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("ohnam00@naver.com");
//        userEntity.setAddress("Seoul");
//        userEntity.setNickname("lua");
//        userEntity.setStatus(UserStatus.ACTIVE);
//        userEntity.setCertificationCode("aaaaa-aaaaa");
//
//        // when
//        UserEntity result = userRepository.save(userEntity);
//
//        // then
//        assertThat(result.getId()).isNotNull(); // 저장이 잘 되어 있다면, id 가 할당되어 있을 것!
//    }

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("ohnam00@naver.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("ohnam00@naver.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}