package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * pojo实体类
 *
 * @Id  userid + friendid == 联合主键
 */
@Entity
@Table(name = "tb_friend")
@IdClass(Friend.class)
public class Friend implements Serializable{

    @Id
    public String userid;

    @Id
    public String friendid;

    public String islike;//0:单向喜欢 1：双向喜欢

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }
}