package com.example.demo.user.controller.port;

public interface AuthenticateService {
    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
