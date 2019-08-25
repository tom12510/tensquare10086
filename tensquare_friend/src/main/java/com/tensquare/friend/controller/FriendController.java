package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友微服务控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友或非好友
     *
     * @param friendid
     * @param type     类型 1:喜欢 2：不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result like(@PathVariable String friendid, @PathVariable String type) {
        //鉴权完成后 userid
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null || !claims.get("roles").equals("user")) {
            return new Result(false, StatusCode.ERROR, "权限不足");
        }
        String userid = claims.getId();
        if (type.equals("1")) {
            //添加好友 tb_friend  userid  friendid islike=0
            //rs 添加好友有可能失败
            int rs = friendService.like(userid, friendid);
            if (rs != 1) {
                return new Result(false, StatusCode.ERROR, "无需重复添加好友");
            }
        } else {
            //添加非好友 tb_nofriend
            friendService.nolike(userid, friendid);
        }
        return new Result(true, StatusCode.OK, "操作成功");
    }


    /**
     * 删除好友
     */
    @RequestMapping(value = "/{friendid}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String friendid) {
        //鉴权完成后 userid
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null || !claims.get("roles").equals("user")) {
            return new Result(false, StatusCode.ERROR, "权限不足");
        }
        String userid = claims.getId();//当前登录用户id
        friendService.deleteById(userid, friendid);
        return new Result(true, StatusCode.OK, "删除好友成功");
    }

}
