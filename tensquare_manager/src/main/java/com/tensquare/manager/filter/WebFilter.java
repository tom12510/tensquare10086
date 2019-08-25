package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 网站后台网关
 * 1.页面发起请求，请求头中带了token参数会丢失（解决此问题）
 * 2.统一鉴权
 * 3.跨域放行  跨域会发送两次请求 第一次请求方式OPTIONS 不带任何参数（预请求）  第二次真实的请求
 * 4.登录放行
 * @author wangxin
 * @version 1.0
 */
@Component
public class WebFilter extends ZuulFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterOrder:过滤器执行顺序 数值越小 优先执行此过滤器
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 开关 是否执行此过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run:具体业务逻辑代码
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("网关前台网关过滤器被执行了。。。");
        //获取网关容器对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取页面request请求对象
        HttpServletRequest request = currentContext.getRequest();
        //通过requset获取请求数据
        String authorization = request.getHeader("Authorization");
        //跨域放行
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }
        //登录放行
        if(request.getRequestURL().indexOf("/admin/login")>0){
            return null;
        }
        //将获取到请求头数据转发给后台微服务
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            //获取请求头中token字符串
            String realToken = authorization.substring(7);
            if(!StringUtils.isEmpty(realToken)){
                Claims claims = jwtUtil.parseJWT(realToken);
                if(claims != null){
                    //|| !claims.get("roles").equals("admin")
                    if(claims.get("roles").equals("admin")){
                        //token转发 鉴权通过
                        currentContext.addZuulRequestHeader("Authorization",authorization);
                        return  null;//放行
                    }
                }
            }
        }

        //鉴权失败 直接返回错误（权限不足）
        currentContext.setResponseStatusCode(401);//没有权限
        currentContext.setResponseBody("权限不足");//响应内容
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");
        currentContext.setSendZuulResponse(false);//终止转发请求
        //return null?放行
        return null;
    }
}
