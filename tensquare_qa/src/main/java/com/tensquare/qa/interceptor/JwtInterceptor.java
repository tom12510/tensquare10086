package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一鉴权拦截器类
 * @author wangxin
 * @version 1.0
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求之前进行拦截请求
     * 拦截处理请求得到当前用户信息放入request对象中
     * 经过拦截器后进入控制层代码
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器被访问了.....");
        String authorization = request.getHeader("Authorization");
        //格式化校验
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            //获取请求头中token字符串
            String realToken = authorization.substring(7);
            if(!StringUtils.isEmpty(realToken)){
                Claims claims = jwtUtil.parseJWT(realToken);
                if(claims != null){
                    //|| !claims.get("roles").equals("admin")
                    if(claims.get("roles").equals("admin")){
                        //将拦截器处理的结果写入request对象中
                        request.setAttribute("admin_claims",claims);
                    }
                    if(claims.get("roles").equals("user")){
                        //将拦截器处理的结果写入request对象中
                        request.setAttribute("user_claims",claims);
                    }
                }
            }

        }

        return true;
    }

}
