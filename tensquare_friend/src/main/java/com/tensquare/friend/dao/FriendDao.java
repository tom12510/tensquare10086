package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wangxin
 * @version 1.0
 */
public interface FriendDao extends JpaRepository<Friend,String> {
    /**
     * 根据用户id和好友id查询是否关注对象
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend  f where f.userid =?1 and f.friendid = ?2")
    int selectCount(String userid, String friendid);

    /**
     * 根据islike状态
     * @param userid
     * @param friendid
     */
    @Query("update Friend set islike = ?3 where userid = ?1 and friendid = ?2")
    @Modifying
    void updateLike(String userid, String friendid,String islike);

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Query("delete from Friend  where userid = ?1 and friendid = ?2")
    @Modifying
    void deleteFriend(String userid, String friendid);
}
