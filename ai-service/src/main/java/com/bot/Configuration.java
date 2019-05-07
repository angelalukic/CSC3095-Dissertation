package com.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("ai-service")
@Getter
@Setter
public class Configuration {
	private String resourcesPath;
}
