package com.easyonbid.service.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.easyonbid.entity.AuctionBasic;
import com.easyonbid.service.util.DateTimeUtil;
import com.easyonbid.service.util.ParsingUtil;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionBasicParser {

	private final ParsingUtil parsingUtil;
	private final DateTimeUtil dateTimeUtil;

	public Map<Long, AuctionBasic> parseAuctionBasic(JsonNode body, LocalDate startDate, LocalDate endDate) {

		Map<Long, AuctionBasic> auctionBasicMap = new ConcurrentHashMap<>();

		try {
			JsonNode itemList = body.path("items").path("item");
			
			if (itemList.isMissingNode()) {
	            log.warn("⚠️ items.item 노드가 없습니다.");
	            return auctionBasicMap;
	        }
			
			// itemList가 배열이 아닐 수 있으니 안전 처리
	        Iterable<JsonNode> iterable;
	        if (itemList.isArray()) {
	            iterable = itemList;
	        } else {
	            iterable = Collections.singletonList(itemList);
	        }
			
			for (JsonNode itemNode : iterable) {

				AuctionBasic item = new AuctionBasic();

				// 날짜 범위 필터링: +30일, -60일
				String begnDtm = itemNode.path("PBCT_BEGN_DTM").asText("");
				if (!dateTimeUtil.isValidDateRange(begnDtm, startDate, endDate)) {
					continue; // skip filtered items
				}

				// 중복제거를 위해 String을 Long으로 파싱
				Long plnmNo = itemNode.path("PLNM_NO").asLong();
				Long pbctNo = itemNode.path("PBCT_NO").asLong();
//		        item.setPlnmNo(itemNode.path("PLNM_NO").asText());
//		        item.setPbctNo(itemNode.path("PBCT_NO").asText());

				// 값 유효성 검사 (필요하면 건너뜀)
		        if (plnmNo == 0L || pbctNo == 0L) {
		            log.warn("유효하지 않은 번호: plnmNo={}, pbctNo={}", plnmNo, pbctNo);
		            continue;
		        }
		        
				item.setPlnmNo(plnmNo.toString());
				item.setPbctNo(pbctNo.toString());
				
				// Basic fields parsing
				item.setCltrNm(itemNode.path("CLTR_NM").asText());
				item.setPbctCdtnNo(itemNode.path("PBCT_CDTN_NO").asText());
				item.setBidMnmtNo(itemNode.path("BID_MNMT_NO").asText());
				item.setCltrMnmtNo(itemNode.path("CLTR_MNMT_NO").asText());
				item.setScrnGrpCd(itemNode.path("SCRN_GRP_CD").asText());
				item.setCtgrFullNm(itemNode.path("CTGR_FULL_NM").asText());
				item.setPbctCltrStatNm(itemNode.path("PBCT_CLTR_STAT_NM").asText());
				item.setDpslMtdCd(itemNode.path("DPSL_MTD_CD").asText());
				item.setDpslMtdNm(itemNode.path("DPSL_MTD_NM").asText());
				item.setBidMtdNm(itemNode.path("BID_MTD_NM").asText());

				// XML datetime → LocalDateTime 변환
				item.setPbctBegnDtm(dateTimeUtil.parseXmlDateTime(begnDtm));

				item.setPbctClsDtm(dateTimeUtil.parseXmlDateTime(itemNode.path("PBCT_CLS_DTM").asText("")));

				// 문자열을 int로 변환
				item.setUscbdCnt(itemNode.path("USCBD_CNT").isInt() ? itemNode.path("USCBD_CNT").asInt()
						: parsingUtil.tryParseInt(itemNode.path("USCBD_CNT").asText("")));

				item.setIqryCnt(itemNode.path("IQRY_CNT").isInt() ? itemNode.path("IQRY_CNT").asInt()
						: parsingUtil.tryParseInt(itemNode.path("IQRY_CNT").asText("")));
				
				auctionBasicMap.compute(plnmNo,
					    (k, existing) -> mergeLatest(existing, item, pbctNo, plnmNo)
					);
				
				// 하면 안됨
//				auctionBasicMap.put(plnmNo, item);
			}
		} catch (Exception e) {
			log.error("❌ AuctionBasic 파싱 중 오류 발생", e);
		}
		return auctionBasicMap;
	}
	
	/**
	 * 같은 공고번호가 존재하면 최신 공매번호만 저장
	 */
	private AuctionBasic mergeLatest(AuctionBasic existing, AuctionBasic candidate, Long candidatePbctNo, Long plnmNo) {
		LocalDateTime now = LocalDateTime.now();

		if (existing == null) {
			log.debug("➕ 신규 저장: 공고번호 {} - 공매번호 {}", plnmNo, candidatePbctNo);
			candidate.setCreatedAt(now);
			candidate.setUpdatedAt(now);
			return candidate;
		}

		long existingPbct = -1L;
		try {
			existingPbct = Long.parseLong(existing.getPbctNo());
		} catch (Exception e) {
			log.warn("⚠ 기존 공매번호 파싱 실패: plnmNo={}, pbctNo={}", plnmNo, existing.getPbctNo());
		}

		if (existingPbct < candidatePbctNo) {
			log.debug("🔄 업데이트: 공고번호 {} - 기존={}, 신규={}", plnmNo, existingPbct, candidatePbctNo);
			
			// 기존 createdAt은 유지, updatedAt은 현재 시간
			candidate.setCreatedAt(existing.getCreatedAt() != null ? existing.getCreatedAt() : now);
			candidate.setUpdatedAt(now);
			
			return candidate;
		} else {
			log.debug("⏭️ 스킵: 공고번호 {} - 기존={}, 신규={}", plnmNo, existingPbct, candidatePbctNo);
			return existing;
		}
	}
}
