package com.easyonbid.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnbidApiClient {
	
//	Onbid API 호출용 클래스
	
    private final WebClient webClient;

	@Value("${openapi.easyonbid.serviceKey}")
    private String serviceKey;

	public String fetchXmlData(int pageNo, int numOfRows) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", pageNo)
                        .queryParam("numOfRows", numOfRows)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
