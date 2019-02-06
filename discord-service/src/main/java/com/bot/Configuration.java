package com.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/*
 * Anything that starts with prefix "discord-service" in
 * application.properties file gets read in
 */

@Component
@ConfigurationProperties("discord-service")
@Getter
@Setter
public class Configuration {
	private String id;
	private String secret;
	private String token;
}
