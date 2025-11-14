package com.easyonbid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.easyonbid.repository")
@SpringBootApplication
public class EasyonbidApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyonbidApplication.class, args);
	}

}
