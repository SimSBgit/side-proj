package com.easyonbid.service.util;

import org.springframework.stereotype.Service;

@Service
public class ParsingUtil {

	public Integer tryParseInt(String value) {
	    try {
	        return Integer.parseInt(value);
	    } catch (Exception e) {
	        return null;
	    }
	}
	
}
