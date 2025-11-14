package com.easyonbid.service.util;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class XmlParserUtil {

	public JsonNode toJsonNode(String xml) throws Exception {
		
		// XML 응답 내용 로깅
		log.info("📜 원본 XML 응답 길이: {}", xml.length());
		
		XmlMapper xmlMapper = new XmlMapper();
	    JsonNode root = xmlMapper.readTree(xml);
	    
	    // 루트 구조 로깅
	    log.info("🌳 XML 루트 구조: {}", root.toPrettyString());
	    
	    // body 노드 확인
	    JsonNode body = root.path("body");
	    if (body.isMissingNode()) {
	        log.error("❌ body 노드가 없습니다.");
	        return root;
	    }
	    
	    // items 노드 확인
	    JsonNode items = body.path("items");
	    if (items.isMissingNode()) {
	        log.error("❌ items 노드가 없습니다.");
	        return body;
	    }
	    
	    // item 노드 확인
	    JsonNode item = items.path("item");
	    if (item.isMissingNode()) {
	        log.error("❌ item 노드가 없습니다.");
	        return items;
	    }
	    
	    if (!item.isArray()) {
	        log.warn("⚠️ item 노드가 배열이 아닙니다. 단일 객체입니다.");
	    } else {
	        log.info("📦 아이템 노드 수: {}", item.size());
	    }
	    
	return item;
	}
}