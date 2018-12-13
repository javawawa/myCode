package com.linjiao.cmslive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

@EnableScheduling
public class CmsLiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsLiveApplication.class, args);
    }
}
