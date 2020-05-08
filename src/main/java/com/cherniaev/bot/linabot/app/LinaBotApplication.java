package com.cherniaev.bot.linabot.app;

import com.cherniaev.bot.linabot.repo.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@ComponentScan(basePackages = { "com.cherniaev.bot.linabot.*", "com.cherniaev.bot.linabot.model" })
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EntityScan(basePackages = {"com.cherniaev.bot.linabot.*"})
public class LinaBotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();

	    SpringApplication.run(LinaBotApplication.class, args);
	}

}
