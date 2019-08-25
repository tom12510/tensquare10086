package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台网关
 * 1.页面发起请求，请求头中带了token参数会丢失（解决此问题）
 * @author wangxin
 * @version 1.0
 */
@Component
public class WebFilter extends ZuulFilter{
    /**
     * filterType:过滤器类型
     * - pre：	可以在请求被路由之前调用
     * - route：在路由请求时候被调用
     * - post：在route和error过滤器之后被调用
     * - error：处理请求时发生错误时被调用
     * @return
     */
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
        //将获取到请求头数据转发给后台微服务
        if(!StringUtils.isEmpty(authorization)){
            //网关提供请求头可以将数据转发给后台微服务
            currentContext.addZuulRequestHeader("Authorization",authorization);
        }
        //return null?放行
        return null;
    }
}
