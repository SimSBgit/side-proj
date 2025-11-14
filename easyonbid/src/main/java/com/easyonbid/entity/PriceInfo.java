package com.easyonbid.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PriceInfo {

		private Long id;
	    private Long auctionBasicId;
	    private Long minBidPrc;
	    private Long apslAsesAvgAmt;
	    private String feeRate;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
}
