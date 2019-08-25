package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 吐槽微服务控制层
 */
@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布吐槽
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"发布吐槽成功");
    }


    /**
     * 修改吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result updateById(@RequestBody Spit spit,@PathVariable String spitId){
        spitService.updateById(spit,spitId);
        return new Result(true, StatusCode.OK,"修改吐槽成功");
    }



    /**
     * 删除吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除吐槽成功");
    }


    /**
     * 查询所有吐槽
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> spitList = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询所有吐槽成功",spitList);
    }

    /**
     * 根据id查询吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK,"查询所有吐槽成功",spit);
    }


    /**
     *  根据上级ID查询吐槽数据（分页）
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> spitPage = spitService.comment(parentid,page,size);
        return new Result(true, StatusCode.OK,"根据上级ID查询吐槽数据成功",new PageResult<>(spitPage.getTotalElements(),spitPage.getContent()));
    }

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        //控制层 限制同一个用户只能为同一个吐槽点赞一次
        //key: 业务唯一标识+用户唯一id+吐槽唯一id
        String userId = "123456";//后续等加入jwt就有userId
        String isThumbup = (String)redisTemplate.opsForValue().get("thumbup_" + userId + "_" + spitId);
        if(!StringUtils.isEmpty(isThumbup)){
            return new Result(false, StatusCode.ERROR,"已经点赞无需重复操作");
        }
        spitService.thumbup(spitId);
        //点赞后 将点赞记录存入redis
        redisTemplate.opsForValue().set("thumbup_" + userId + "_" + spitId,"1");
        return new Result(true, StatusCode.OK,"点赞成功");
    }

}
