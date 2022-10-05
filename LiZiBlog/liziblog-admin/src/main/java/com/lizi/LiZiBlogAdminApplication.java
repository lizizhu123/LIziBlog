package com.lizi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.lizi.mapper")
@EnableScheduling
public class LiZiBlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiZiBlogAdminApplication.class,args);
    }
}