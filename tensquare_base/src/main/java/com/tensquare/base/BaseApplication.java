package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * 基础微服务启动类
 */
@SpringBootApplication
@EnableEurekaClient //启用注册中心客户端组件
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    //将idwork放入spring容器中 @Autowired private Idwork idwork;
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
}
