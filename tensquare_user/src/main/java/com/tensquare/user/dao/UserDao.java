package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机号码查询用户对象
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 关注数变更
     * @param userid
     * @param x
     */
    @Query("update User set followcount = followcount+?2 where  id = ?1")
    @Modifying
    void incFollowcount(String userid, int x);

    /**
     * 粉丝数变更
     * @param userid
     * @param x
     */
    @Query("update User set fanscount = fanscount+?2 where  id = ?1")
    @Modifying
    void incFanscount(String userid, int x);
}
