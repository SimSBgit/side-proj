package com.easyonbid.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsDetail {

	  	private Long id;
	    private Long auctionBasicId;
	    private String goodsNm;
	    private String manf;
	    private String mdl;
	    private String nrgt;
	    private String grbx;
	    private String endpc;
	    private String vhclMlge;
	    private String fuel;
	    private String scrtNm;
	    private String tpbz;
	    private String itmNm;
	    private String mmbRgtNm;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
}
