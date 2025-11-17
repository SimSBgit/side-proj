package com.easyonbid.service.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.easyonbid.dto.response.SaveResult;
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

	// 전체 실행 메서드
	public SaveResult<AuctionBasic> fetchParseSave(int pageNo, int numOfRows, LocalDate start, LocalDate end) {

		// 날짜 범위 계산 (파라미터가 null인 경우 기본값 사용)
		DateRange dateRange = dateTimeUtil.calculateDateRange();
		LocalDate startDate = (start != null) ? start : dateRange.getStartDate();
		LocalDate endDate = (end != null) ? end : dateRange.getEndDate();

		// API 호출
		String xml = onbidApiClient.fetchXmlData(pageNo, numOfRows);

		JsonNode body;

		// json으로 변경
		try {
			body = xmlParserUtil.toJsonNode(xml);
		} catch (Exception e) {
			throw new RuntimeException("XML 파싱 실패", e);
		}

		// 데이터 중복 제거 후, 데이터 매핑
		Map<Long, AuctionBasic> parsed = auctionBasicParser.parseAuctionBasic(body, startDate, endDate);

		// 데이터 저장 + UUID 생성
		SaveResult<AuctionBasic> savedItems = saveAll(parsed);

		log.info("📦 페이지 {} 저장결과: 성공={}, 실패={}", pageNo, savedItems.getSuccess().size(),
				savedItems.getFailure().size());

		return savedItems;
	}

	// 전체 리스트 저장
	public SaveResult<AuctionBasic> saveAll(Map<Long, AuctionBasic> auctionBasicMap) {
		List<AuctionBasic> savedItems = new ArrayList<>();
		List<AuctionBasic> failedItems = new ArrayList<>();

		for (AuctionBasic item : auctionBasicMap.values()) {
			try {
				saveItem(item); // upsert 내부 동작 확인 필요
				savedItems.add(item);
				log.info("✅ 저장 완료: 공고번호={}, 공매번호={}, 물건명={}", item.getPlnmNo(), item.getPbctNo(), item.getCltrNm());
			} catch (Exception e) {
				failedItems.add(item);
				log.error("❌ DB 저장 실패: 공고번호={}, 공매번호={}, 오류={}", item.getPlnmNo(), item.getPbctNo(), e.getMessage());
			}
		}
		return new SaveResult<>(savedItems, failedItems);
	}

	/**
	 * 아이템 저장
	 */
	public void saveItem(AuctionBasic item) {
		// UUID가 비어있다면 새로 생성
		if (item.getUuid() == null || item.getUuid().isEmpty()) {
			item.setUuid(UUID.randomUUID().toString());
		}
		auctionBasicMapper.insert(item);
	}
}
