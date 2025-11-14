package com.easyonbid.service.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.easyonbid.entity.AuctionBasic;
import com.easyonbid.repository.AuctionBasicMapper;
import com.easyonbid.service.external.OnbidApiClient;
import com.easyonbid.service.parser.AuctionBasicParser;
import com.easyonbid.service.util.DateTimeUtil;
import com.easyonbid.service.util.DateTimeUtil.DateRange;
import com.easyonbid.service.util.XmlParserUtil;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionBasicService {

	private final OnbidApiClient onbidApiClient;
	private final XmlParserUtil xmlParserUtil;
	private final AuctionBasicParser auctionBasicParser;
	private final AuctionBasicMapper auctionBasicMapper;
	private final DateTimeUtil dateTimeUtil;
	
    // 전체 리스트 저장
    public void saveAll(List<AuctionBasic> items) {
        for (AuctionBasic item : items) {
        	saveItem(item);
        }
    }

 // 전체 실행 메서드
    public List<AuctionBasic> fetchParseSave(int pageNo, int numOfRows, LocalDate start, LocalDate end) {

    	 // 날짜 범위 계산 (파라미터가 null인 경우 기본값 사용)
        DateRange dateRange = dateTimeUtil.calculateDateRange();
        LocalDate startDate = (start != null) ? start : dateRange.getStartDate();
        LocalDate endDate = (end != null) ? end : dateRange.getEndDate();
    	 
        String xml = onbidApiClient.fetchXmlData(pageNo, numOfRows);

        JsonNode items;
        
        try {
            items = xmlParserUtil.toJsonNode(xml);
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }

        List<AuctionBasic> parsed = auctionBasicParser.parseAuctionBasic(items, startDate, endDate);

        saveAll(parsed);

        log.info("✅ 총 {}건 저장됨 (날짜 범위: {} ~ {})", parsed.size(), startDate, endDate);
        return parsed;
    }
	
    /**
     * 아이템 저장
     */
	public void saveItem(AuctionBasic item) {
        // UUID가 비어있다면 새로 생성
        if (item.getUuid() == null || item.getUuid().isEmpty()) {
            item.setUuid(UUID.randomUUID().toString());
        }
        auctionBasicMapper.upsert(item);
    }
}
