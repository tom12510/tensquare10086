package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 文章搜索业务层
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    /**
     * 文章发布成功
     * @param article
     */
    public void save(Article article) {
        articleSearchDao.save(article);
    }

    /**
     * 根据用户输入的关键字搜索 文章数据
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByTitleOrContent(String keywords, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return articleSearchDao.findByTitleOrContent(keywords,keywords,pageable);
    }
}
