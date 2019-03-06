package com.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("twitch-service")
@Getter
@Setter
public class Configuration {
	private String id;
	private String secret;
	private String irc;
}
