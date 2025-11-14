package com.easyonbid.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollateralInfo {

	 private Long id;
	    private Long auctionBasicId;
	    private String cltrNo;
	    private String cltrHstrNo;
	    private String cltrNm;
	    private String ldnmAdrs;
	    private String nmrdAdrs;
	    private String ldnmPnu;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
}
