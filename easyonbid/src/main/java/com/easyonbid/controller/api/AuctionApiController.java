package com.easyonbid.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyonbid.dto.response.SaveResult;
import com.easyonbid.entity.AuctionBasic;
import com.easyonbid.service.domain.AuctionBasicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/easyonbid")
@RequiredArgsConstructor
public class AuctionApiController {

	private final AuctionBasicService auctionBasicService;

	@GetMapping(value = "/fetchAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuctionBasic> fetchAll() throws Exception {
	    		
	    List<AuctionBasic> allItems = new ArrayList<>();
	    int numOfRows = 50;
	    int totalPages = 1;
	    
	    log.info("🚀 총 {}페이지, 페이지당 {}개 데이터 호출 시작", totalPages, numOfRows);
	    
	    for (int page = 1; page <= totalPages; page++) {
	        log.info("📡 페이지 {}/{} 호출 중...", page, totalPages);
	        SaveResult<AuctionBasic> pageItems = auctionBasicService.fetchParseSave(page, numOfRows, null, null);
	        allItems.addAll(pageItems.getSuccess());
	        log.info(
	                "✅ 페이지 {} 완료: 성공={}, 실패={}, 누적 성공={}",
	                page,
	                pageItems.getSuccess().size(),
	                pageItems.getFailure().size(),
	                allItems.size()
	            );
	    }
	    log.info("🎉 전체 작업 완료: 총 성공 {}건, 총 페이지 {}개", allItems.size(), totalPages);
	    return allItems;
}
	
//		DB 데이터 - 1페이지당 10개, 전체 조회
		@GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
		public List<AuctionBasic> getItems(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
	    		@RequestParam(name = "numOfRows", defaultValue = "10") int numOfRows) {
			int offset = (pageNo - 1) * numOfRows;
			return auctionBasicService.getAll(offset, numOfRows);
		}
	
}
