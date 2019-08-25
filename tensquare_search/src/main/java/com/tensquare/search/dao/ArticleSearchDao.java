package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章搜索持久层接口
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {
    /**
     * 搜索文章数据
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContent(String keywords, String keywords1, Pageable pageable);
}
