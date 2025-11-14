package com.easyonbid.service.parser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

	public List<AuctionBasic> parseAuctionBasic(JsonNode items, LocalDate startDate, LocalDate endDate) {
		
		List<AuctionBasic> auctionBasicList = new ArrayList<>();
		
		try {
			if (!items.isArray()) {
	            log.warn("⚠️ items 노드가 배열이 아닙니다. 구조를 확인하세요.");
	        }
		        for (JsonNode itemNode : items) {

		            // 날짜 필터링
		            String begnDtm = itemNode.path("PBCT_BEGN_DTM").asText("");
		            if (!dateTimeUtil.isValidDateRange(begnDtm, startDate, endDate)) {
		                continue; // skip filtered items
		            }

		            AuctionBasic item = new AuctionBasic();

		            // Basic fields parsing
		            item.setPlnmNo(itemNode.path("PLNM_NO").asText());
		            item.setPbctNo(itemNode.path("PBCT_NO").asText());
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

		            item.setPbctClsDtm(
		                    dateTimeUtil.parseXmlDateTime(itemNode.path("PBCT_CLS_DTM").asText(""))
		            );

		            // 문자열을 int로 변환
		            item.setUscbdCnt(
		                    itemNode.path("USCBD_CNT").isInt()
		                            ? itemNode.path("USCBD_CNT").asInt()
		                            : parsingUtil.tryParseInt(itemNode.path("USCBD_CNT").asText(""))
		            );

		            item.setIqryCnt(
		                    itemNode.path("IQRY_CNT").isInt()
		                            ? itemNode.path("IQRY_CNT").asInt()
		                            : parsingUtil.tryParseInt(itemNode.path("IQRY_CNT").asText(""))
		            );

		            // created_at, updated_at은 DB에서 올 때 timestamp 형식일 수도 있음
		            item.setCreatedAt(dateTimeUtil.parseDbDateTime(itemNode.path("CREATED_AT").asText("")));
		            item.setUpdatedAt(dateTimeUtil.parseDbDateTime(itemNode.path("UPDATED_AT").asText("")));

		            auctionBasicList.add(item);
		        }
		} catch (Exception e) {
			log.error("❌ AuctionBasic 파싱 중 오류 발생", e);
		}
		return auctionBasicList;
	}
}
