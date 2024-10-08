package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final PostService postService;
    public final UserService userService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;


    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.certificationService = new CertificationService(this.mailSender);

        // 이렇게 고정된 시간이랑 UUID 를 사용하면 유동성이 너무 떨어지게 되어 생성자 바깥으로 빼고 빌더 달아주기
        this.postService = PostServiceImpl.builder()
                .userRepository(this.userRepository)
                .postRepository(this.postRepository)
                .clockHolder(clockHolder)
                .build();
        this.userService = UserServiceImpl.builder()
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService)
                .build();

        this.userController = UserController.builder()
                                    .userService(this.userService).build();

        this.userCreateController = UserCreateController.builder()
                .userService(this.userService).build();

        this.postController = PostController.builder()
                .postService(this.postService).build();

        this.postCreateController = PostCreateController.builder()
                .postService(this.postService).build();
    }
}
