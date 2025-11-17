package com.easyonbid.repository;

import org.apache.ibatis.annotations.Mapper;

import com.easyonbid.entity.AuctionBasic;

@Mapper
public interface AuctionBasicMapper {

	void insert(AuctionBasic item);

	
}
