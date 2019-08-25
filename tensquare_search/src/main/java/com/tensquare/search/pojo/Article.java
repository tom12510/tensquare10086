package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 文章实体类
 * indexName：索引 type:类型 document:文档
 */
@Document(indexName="tensquare",type="article")
public class Article implements Serializable {
    @Id
    private String id;//ID

    /*
    index:是否索引 true:索引 通过此字段进行搜索
    analyzer：存入es使用ik分词器
    searchAnalyzer：搜索分词
     */
    @Field(index= true ,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;//标题

    @Field(index= true ,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String content;//文章正文

    /**
     * 文章状态 1.文章已经审核 2.文章未审核 3.文章删除 4.。。。。。状态  （文档域）
     */
    private String state;

    //getter and setter ......


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}