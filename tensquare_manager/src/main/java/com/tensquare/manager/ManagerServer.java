package com.tensquare.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * 后台网关微服务
 * @author wangxin
 * @version 1.0
 */
@SpringBootApplication
@EnableZuulProxy //启用微服务网关
public class ManagerServer {
    public static void main(String[] args) {
        SpringApplication.run(ManagerServer.class);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
