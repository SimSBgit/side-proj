package com.easyonbid.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.easyonbid.entity.AuctionBasic;

@Mapper
public interface AuctionBasicMapper {

	void insert(AuctionBasic item);

	List<AuctionBasic> findPagedAll(@Param("offset") int offset, @Param("numOfRows") int numOfRows);

	
}
