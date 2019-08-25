package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangxin
 * @version 1.0
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;
    /**
     * 添加好友
     * @param userid
     * @param friendid
     * @return
     */
    @Transactional
    public int like(String userid, String friendid) {
        //查询当前用户是否已经关注此好友
        //如果已经挂住此好友，无需重复关注
        if(friendDao.selectCount(userid,friendid) >0){//select count(*) from tb_friend where userid = xx and friendid =xx
            return 0;
        }
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");//单向喜欢
        friendDao.save(friend);
        //先根据用户id和好友id查询数据记录是否存在
        if(friendDao.selectCount(friendid,userid) >0){
            //如果被关注的好友如果关注当前登录用户 更新双方的 islike=1
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }
        //变更粉丝数和关注数
        userClient.incFollowcount(userid,1);
        userClient.incFanscount(friendid,1);
        return 1;//1:添加好友成功 0:添加失败
    }

    /**
     * 添加非好友
     * @param userid
     * @param friendid
     */
    public void nolike(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteById(String userid, String friendid) {
        //先将当前用户关注的好友记录删除
        friendDao.deleteFriend(userid,friendid);
        //先根据好友id和当前用户登录id查询对方是否关注自己（当前登录用户）
        int count = friendDao.selectCount(friendid, userid);
        //如果对方已经关注自己（当前登录用户）
        //修改islike=0
        if(count > 0){
            friendDao.updateLike(friendid,userid,"0");
        }
        userClient.incFollowcount(userid,-1);
        userClient.incFanscount(friendid,-1);
    }
}
