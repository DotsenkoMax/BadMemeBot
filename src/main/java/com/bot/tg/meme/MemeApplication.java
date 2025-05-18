package com.bot.tg.meme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@AutoConfiguration
@ComponentScan
@EnableScheduling
@EnableConfigurationProperties
@EnableCaching
public class MemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemeApplication.class, args);
	}

}
