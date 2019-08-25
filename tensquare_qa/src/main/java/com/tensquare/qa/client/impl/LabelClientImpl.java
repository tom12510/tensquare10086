package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 熔断器类
 * 负责qa问答微服务调用基础微服务失败
 * 1.返回被调用微服务错误信息
 * 2.记录日志
 * 3.当调用微服务失败，可以尝试从缓存中查询数据
 * @author wangxin
 * @version 1.0
 */
@Component
public class LabelClientImpl implements LabelClient {
    @Override
    public Result findById(String labelId) {
        System.out.println("熔断器启动了。。。。");
        return new Result(false, StatusCode.REMOTEERROR,"基础微服务出问题了");
    }
}
