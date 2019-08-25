package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索微服务控制层
 *
 * 1.页面发起搜索关键字请求
 * 2.tensquare_article搜索微服务接收请求， a方法 保存mysql
 * 3.a方法调用tensquare_search微服务b方法  保存es
 *
 */
@RestController
@RequestMapping("/article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 发布文章
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleSearchService.save(article);
        return new Result(true, StatusCode.OK,"发布文章成功");
    }

    /**
     * 文章搜索分页
     * keywords:用户输入的关键字
     * page:当前页码
     * size:每页显示记录数
     */
    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result search(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> articlePage = articleSearchService.findByTitleOrContent(keywords,page,size);
        return new Result(true, StatusCode.OK,"文章搜索成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
    }


    /**
     * 活动搜索
     */
}
