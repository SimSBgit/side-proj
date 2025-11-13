package com.easyonbid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class EasyonbidApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyonbidApplication.class, args);
	}

}
