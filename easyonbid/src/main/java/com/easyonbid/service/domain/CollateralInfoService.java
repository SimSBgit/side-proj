//package com.easyonbid.service.domain;
//
//import java.time.LocalDate;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.easyonbid.dto.response.SaveResult;
//import com.easyonbid.entity.AuctionBasic;
//import com.easyonbid.entity.CollateralInfo;
//import com.easyonbid.repository.AuctionBasicMapper;
//import com.easyonbid.repository.CollateralInfoMapper;
//import com.easyonbid.service.external.OnbidApiClient;
//import com.easyonbid.service.parser.AuctionBasicParser;
//import com.easyonbid.service.parser.CollateralInfoParser;
//import com.easyonbid.service.util.DateTimeUtil;
//import com.easyonbid.service.util.XmlParserUtil;
//import com.easyonbid.service.util.DateTimeUtil.DateRange;
//import com.fasterxml.jackson.databind.JsonNode;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CollateralInfoService {
//
//	private final OnbidApiClient onbidApiClient;
//	private final XmlParserUtil xmlParserUtil;
//	private final CollateralInfoParser collateralInfoParser;
//	private final CollateralInfoMapper collateralInfoMapper;
//	private final DateTimeUtil dateTimeUtil;
//	
//
//	// 전체 실행 메서드
//	public SaveResult fetchParseSave(int pageNo, int numOfRows, LocalDate start, LocalDate end) {
//
//		// 날짜 범위 계산 (파라미터가 null인 경우 기본값 사용)
//		DateRange dateRange = dateTimeUtil.calculateDateRange();
//		LocalDate startDate = (start != null) ? start : dateRange.getStartDate();
//		LocalDate endDate = (end != null) ? end : dateRange.getEndDate();
//
//		// API 호출
//		String xml = onbidApiClient.fetchXmlData(pageNo, numOfRows);
//
//		JsonNode body;
//
//		// json으로 변경
//		try {
//			body = xmlParserUtil.toJsonNode(xml);
//		} catch (Exception e) {
//			throw new RuntimeException("XML 파싱 실패", e);
//		}
//
//		// 데이터 중복 제거 후, 데이터 매핑
//		Map<Long, CollateralInfo> parsed = collateralInfoParser.parseAuctionBasic(body, startDate, endDate);
//
//		// 데이터 저장 + UUID 생성
//		SaveResult savedItems = saveAll(parsed);
//
//		log.info("📦 페이지 {} 저장결과: 성공={}, 실패={}", pageNo, savedItems.getSuccess().size(),
//				savedItems.getFailure().size());
//
//		return savedItems;
//	}
//}
