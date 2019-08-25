package com.tensquare.spit.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 吐槽微服务实体对象
 * 数据最终要以二进制数据写入磁盘
 */
public class Spit implements Serializable{

    @Id
    private String _id; //主键_id
    private String content;//吐槽内容
    private Date publishtime;//吐槽发布时间
    private String userid;//吐槽用户id
    private String nickname;//吐槽用户昵称
    private Integer visits;//浏览量
    private Integer thumbup;//点赞数
    private Integer share;//分享数
    private Integer comment;//评论数（回复数） 再次吐槽的字段
    private String state;//状态
    private String parentid;//上级id  上级吐槽：parentid=0  下级吐槽：上级吐槽：parentid !=0

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getThumbup() {
        return thumbup;
    }

    public void setThumbup(Integer thumbup) {
        this.thumbup = thumbup;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    @Override
    public String toString() {
        return "Spit{" +
                "_id='" + _id + '\'' +
                ", content='" + content + '\'' +
                ", publishtime=" + publishtime +
                ", userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", visits=" + visits +
                ", thumbup=" + thumbup +
                ", share=" + share +
                ", comment=" + comment +
                ", state='" + state + '\'' +
                ", parentid='" + parentid + '\'' +
                '}';
    }
}
