package com.easyonbid.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuctionBasic {

	private Long id;
    private String uuid;
    private String plnmNo;
    private String pbctNo;
    private String pbctCdtnNo;
    private String bidMnmtNo;
    private String cltrMnmtNo;
    private String scrnGrpCd;
    private String ctgrFullNm;
    private String pbctCltrStatNm;
    private String dpslMtdCd;
    private String dpslMtdNm;
    private String bidMtdNm;
    private LocalDateTime pbctBegnDtm;
    private LocalDateTime pbctClsDtm;
    private Integer uscbdCnt;
    private Integer iqryCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
