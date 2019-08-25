package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Enterprise;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>,JpaSpecificationExecutor<Enterprise>{
    /**
     * 热门企业 //方法名称规则查询
     * 查询哪个实体类 Enterprise  条件：ishot  0：非热门 1：热门
     */
    List<Enterprise> findByIshot(String s);
}
