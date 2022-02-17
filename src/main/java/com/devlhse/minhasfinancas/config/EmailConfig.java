package com.devlhse.minhasfinancas.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EmailConfig {

	@Value("${email.username}")
	private String userEmail;

	@Value("${email.password}")
	private String userPassword;
}
