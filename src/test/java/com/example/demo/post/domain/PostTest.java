package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    public void PostCreate_로_게시물을_만들_수_잇다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        User writer = User.builder()
                .id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        TestClockHolder testClockHolder = new TestClockHolder(1678530673958L);

        // when
        Post post = Post.from(writer, postCreate, testClockHolder);

        // then
        assertThat(post.getWriter().getEmail()).isEqualTo("ohnam00@naver.com");
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getNickname()).isEqualTo("ohnam");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void PostUpdate_로_게시물을_수정할_수_잇다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("Hello World :)")
                .build();

        User writer = User.builder()
                .id(1L)
                .email("ohnam00@naver.com")
                .nickname("ohnam")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("helloworld")
                .writer(writer)
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .build();

        // when
        post = post.update(postUpdate, new TestClockHolder(1678530673958L));

        // then
        assertThat(post.getWriter().getEmail()).isEqualTo("ohnam00@naver.com");
        assertThat(post.getContent()).isEqualTo("Hello World :)");
        assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);
    }


}