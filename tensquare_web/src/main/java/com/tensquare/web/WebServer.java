package com.tensquare.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 前台网关微服务
 * @author wangxin
 * @version 1.0
 */
@SpringBootApplication
@EnableZuulProxy
public class WebServer {
    public static void main(String[] args) {
        SpringApplication.run(WebServer.class);
    }
}
