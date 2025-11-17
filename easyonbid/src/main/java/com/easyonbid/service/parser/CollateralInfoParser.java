//package com.easyonbid.service.parser;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.springframework.stereotype.Component;
//
//import com.easyonbid.entity.AuctionBasic;
//import com.easyonbid.entity.CollateralInfo;
//import com.easyonbid.service.util.DateTimeUtil;
//import com.easyonbid.service.util.ParsingUtil;
//import com.fasterxml.jackson.databind.JsonNode;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CollateralInfoParser {
//
//    private final DateTimeUtil dateTimeUtil;
//    private final ParsingUtil parsingUtil;
//   
//	public Map<Long, CollateralInfo> parseCollateralInfo(JsonNode body, LocalDate startDate, LocalDate endDate) {
//
//		Map<Long, CollateralInfo> collateralMap = new ConcurrentHashMap<>();
//
//		try {
//			JsonNode itemList = body.path("items").path("item");
//			
//			if (itemList.isMissingNode()) {
//	            log.warn("⚠️ items.item 노드가 없습니다.");
//	            return collateralMap;
//	        }
//			
//			// itemList가 배열이 아닐 수 있으니 안전 처리
//	        Iterable<JsonNode> iterable;
//	        if (itemList.isArray()) {
//	            iterable = itemList;
//	        } else {
//	            iterable = Collections.singletonList(itemList);
//	        }
//			
//			for (JsonNode itemNode : iterable) {
//
//				CollateralInfo item = new CollateralInfo();
//
//				// 날짜 범위 필터링: +30일, -60일
//				String begnDtm = itemNode.path("PBCT_BEGN_DTM").asText("");
//				if (!dateTimeUtil.isValidDateRange(begnDtm, startDate, endDate)) {
//					continue; // skip filtered items
//				}
//
//				// 중복제거를 위해 String을 Long으로 파싱
//				Long plnmNo = itemNode.path("PLNM_NO").asLong();
//				Long pbctNo = itemNode.path("PBCT_NO").asLong();
////		        item.setPlnmNo(itemNode.path("PLNM_NO").asText());
////		        item.setPbctNo(itemNode.path("PBCT_NO").asText());
//
//				// 값 유효성 검사 (필요하면 건너뜀)
//		        if (plnmNo == 0L || pbctNo == 0L) {
//		            log.warn("유효하지 않은 번호: plnmNo={}, pbctNo={}", plnmNo, pbctNo);
//		            continue;
//		        }
//		        
//				item.setPlnmNo(plnmNo.toString());
//				item.setPbctNo(pbctNo.toString());
//				
//				// Basic fields parsing
//				item.setCltrNo(itemNode.path("CLTR_NM").asText());
//				item.setCltrHstrNo(itemNode.path("PBCT_CDTN_NO").asText());
//				item.setCltrNm(itemNode.path("BID_MNMT_NO").asText());
//				item.setLdnmAdrs(itemNode.path("CLTR_MNMT_NO").asText());
//				item.setNmrdAdrs(itemNode.path("SCRN_GRP_CD").asText());
//				item.setLdnmPnu(itemNode.path("CTGR_FULL_NM").asText());
//
//				collateralMap.compute(plnmNo,
//					    (k, existing) -> mergeLatest(existing, item, pbctNo, plnmNo)
//					);
//				
//				// 하면 안됨
////				auctionBasicMap.put(plnmNo, item);
//			}
//		} catch (Exception e) {
//			log.error("❌ CollateralInfo 파싱 중 오류 발생", e);
//		}
//		return collateralMap;
//	}
//	
//	/**
//	 * 같은 공고번호가 존재하면 최신 공매번호만 저장
//	 */
//	private CollateralInfo mergeLatest(CollateralInfo existing, CollateralInfo candidate, Long candidatePbctNo, Long plnmNo) {
//		LocalDateTime now = LocalDateTime.now();
//
//		if (existing == null) {
//			log.debug("➕ 신규 저장: 공고번호 {} - 공매번호 {}", plnmNo, candidatePbctNo);
//			candidate.setCreatedAt(now);
//			candidate.setUpdatedAt(now);
//			return candidate;
//		}
//
//		long existingPbct = -1L;
//		try {
//			existingPbct = Long.parseLong(existing.getPbctNo());
//		} catch (Exception e) {
//			log.warn("⚠ 기존 공매번호 파싱 실패: plnmNo={}, pbctNo={}", plnmNo, existing.getPbctNo());
//		}
//
//		if (existingPbct < candidatePbctNo) {
//			log.debug("🔄 업데이트: 공고번호 {} - 기존={}, 신규={}", plnmNo, existingPbct, candidatePbctNo);
//			
//			// 기존 createdAt은 유지, updatedAt은 현재 시간
//			candidate.setCreatedAt(existing.getCreatedAt() != null ? existing.getCreatedAt() : now);
//			candidate.setUpdatedAt(now);
//			
//			return candidate;
//		} else {
//			log.debug("⏭️ 스킵: 공고번호 {} - 기존={}, 신규={}", plnmNo, existingPbct, candidatePbctNo);
//			return existing;
//		}
//	}
//	
//}
