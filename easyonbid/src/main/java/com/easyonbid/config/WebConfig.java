package com.easyonbid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

	@Value("${openapi.easyonbid.url}")
	private String baseUrl;

//	@Bean
//	public WebClient  webClient(WebClient.Builder builder) {
//		return builder.baseUrl(baseUrl).build();
//	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl(baseUrl).exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
				).build()).build();
	}
}
