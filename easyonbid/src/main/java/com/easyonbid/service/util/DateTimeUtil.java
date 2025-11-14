package com.easyonbid.service.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DateTimeUtil {
	
	 /**
     * 날짜 범위를 표현하는 내부 클래스
     */
    @Getter
    @AllArgsConstructor
    public static class DateRange {
        private final LocalDate startDate;
        private final LocalDate endDate;
    }
	
    /**
     * 날짜 범위 계산 (오늘 기준 60일 이전 ~ 30일 이후)
     */
    public DateRange calculateDateRange() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(60);
        LocalDate endDate = today.plusDays(30);
        
        return new DateRange(startDate, endDate);
    }
    
    /**
     * 날짜 범위 검증
     */
    public boolean isValidDateRange(String begnDtm, LocalDate startDate, LocalDate endDate) {
        try {
            if (begnDtm == null || begnDtm.length() != 14) {
                log.debug("⏭️ 날짜 형식 불일치로 스킵: {}", begnDtm);
                return false;
            }
            
            DateTimeFormatter inFmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDate bidStartDate = LocalDateTime.parse(begnDtm, inFmt).toLocalDate();
            
            if (bidStartDate.isBefore(startDate) || bidStartDate.isAfter(endDate)) {
                log.debug("⏭️ 입찰시작일 범위 밖: {}", begnDtm);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            log.warn("⚠️ pbctBegnDtm 파싱 실패: {}", e.getMessage());
            return false;
        }
    }
    
    private static final DateTimeFormatter XML_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final DateTimeFormatter DB_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // XML datetime → LocalDateTime
    public LocalDateTime parseXmlDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDateTime.parse(value, XML_FORMAT);
    }

    // DB datetime → LocalDateTime (필요하면 사용)
    public LocalDateTime parseDbDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDateTime.parse(value, DB_FORMAT);
    }
    
}
