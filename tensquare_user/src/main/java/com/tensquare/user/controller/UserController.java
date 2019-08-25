package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        Claims adminClaims = (Claims)request.getAttribute("admin_claims");
        if(adminClaims == null || !adminClaims.get("roles").equals("admin")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 用户删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //Authorization ,内容为Bearer+空格+token
        //从请求头中获取token数据
        /*String authorization = request.getHeader("Authorization");
        //格式化校验
        if(StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer ")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        //获取请求头中token字符串
        String realToken = authorization.substring(7);
        if(StringUtils.isEmpty(realToken)){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        Claims claims = jwtUtil.parseJWT(realToken);
        if(claims == null || !claims.get("roles").equals("admin")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }*/
        //从request中获取当前用户信息
        Claims adminClaims = (Claims)request.getAttribute("admin_claims");
        if(adminClaims == null || !adminClaims.get("roles").equals("admin")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        //根据用户名和密码进行校验
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    /**
     * 发送验证码
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, "发送验证码成功");
    }
    /**
     * 用户注册
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    //public Result register(@RequestBody User user,@PathVariable String code)
    public Result register(@RequestBody Map map,@PathVariable String code) {
        userService.register(map,code);
        return new Result(true, StatusCode.OK, "用户注册成功");
    }

    /**
     * 普通用户登录（t_user表）
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map map) {
        User user = userService.login((String)map.get("mobile"),(String)map.get("password"));
        if(user == null){
            return new Result(false, StatusCode.ERROR, "用户登录失败");
        }
        //如果账号密码正确 生成token字符串
        String userToken = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
        Map<String,Object> rsMap = new HashMap<>();
        rsMap.put("user",user);
        rsMap.put("token",userToken);
        return new Result(true, StatusCode.OK, "用户登录成功",rsMap);
    }


    /**
     * 关注数变更
     * x：1  -1
     */
    @RequestMapping(value="/incfollow/{userid}/{x}",method= RequestMethod.POST)
    public void incFollowcount(@PathVariable String userid,@PathVariable int x){
        userService.incFollowcount(userid,x);
    }


    /**
     * 粉丝数变更
     * x：1  -1
     */
    @RequestMapping(value="/incfans/{userid}/{x}",method= RequestMethod.POST)
    public void incFanscount(@PathVariable String userid,@PathVariable int x){
        userService.incFanscount(userid,x);
    }

}
