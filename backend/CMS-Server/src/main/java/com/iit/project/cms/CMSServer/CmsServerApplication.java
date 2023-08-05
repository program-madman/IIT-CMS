package com.iit.project.cms.CMSServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CmsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsServerApplication.class, args);
	}
}
