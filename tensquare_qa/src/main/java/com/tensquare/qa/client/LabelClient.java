package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 需求：问答微服务调用基础微服务（根据标签id查询标签数据）
 * 1.在问答微服务加入feign依赖
 * 2.在问答微服务启动类上启动feign组件
 * 3.在问答微服务新建接口 （用于调用基础微服务的）
 *   接口上需要指定调用哪个微服务==>@FeignClient("服务名")
 *   接口中加入被调用的方法（直接将被调用的基础微服务控制层方法直接copy）
 *      a.修改被调用基础微服务访问路径
 *      b.请求参数一定要设置别名，不然无法将参数传值到被调用方。
 * 4.在问答微服务控制层中编写一个方法接收请求
 * 5.测试
 *   注册中心  ==>  基础微服务  ==> 问答微服务
 *   http://localhost:9003/problem/mylabel/1
 *   通过测试：
 *   feign组件自带负载均衡 默认是轮训策略
 *
 *  fallback:指定回调类，当调用基础微服务失败后，进入此类返回相应的错误
 * @author wangxin
 * @version 1.0
 */
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    /**
     * 根据标签id查询标签数据
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
