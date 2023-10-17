package com.chat.chattingtest2.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackages = {"com.chat.chattingtest2.domain.member.repository",
		"com.chat.chattingtest2.domain.crew.repository",
	}
)
public class JpaConfig {
}
