package com.example.demo.user.controller;

import com.example.demo.user.controller.port.*;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저(users)")
@RestController
@Builder
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCreateService userCreateService;
    private final UserUpdateService userUpdateService;
    private final UserReadService userReadService;
    private final AuthenticateService authenticateService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponse.from(userReadService.getById(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        authenticateService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponse> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
    ) {
        User user = userReadService.getByEmail(email);
        authenticateService.login(user.getId());
        /*
        내 정보를 조회하는 api 가 변경된 유저 정보를 내려주지 않고 있었음
        그래서 아래 코드로 변경된 유저 정보를 응답하도록 추가
         */
        user = userReadService.getByEmail(email);
        return ResponseEntity
            .ok()
            .body(MyProfileResponse.from(user));
    }

    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<MyProfileResponse> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
        @RequestBody UserUpdate userUpdate
    ) {
        User user = userReadService.getByEmail(email);
        user = userUpdateService.update(user.getId(), userUpdate);
        return ResponseEntity
            .ok()
            .body(MyProfileResponse.from(user));
    }
}