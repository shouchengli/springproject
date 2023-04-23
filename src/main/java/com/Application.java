package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mybatisplus.mapper") // 配置mybatisplus扫描的mapper包
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
