package com.tensquare.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心微服务启动类
 * @author wangxin
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigServer //启用配置中心服务端
public class ConfigServer {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class);
    }
}
