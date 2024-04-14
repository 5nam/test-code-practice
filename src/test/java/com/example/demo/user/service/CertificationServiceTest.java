package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.port.MailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // when
        certificationService.send("ohnam00@naver.com", 3, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(fakeMailSender.email).isEqualTo("ohnam00@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo(
                "Please click the following link to certify your email address: http://localhost:8080/api/users/3/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
}