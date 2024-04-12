insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'ohnam01@naver.com', 'ohnam01', 'Seoul', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ACTIVE', 0);
insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (2, 'helloworld', 1678530673958, 0, 1);
-- post id 2 로 한 이유는 create 함수 실행할 때, 자동으로 생성하는 id 가 1부터 시작하므로