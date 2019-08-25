package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin//解决跨域
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 增
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"保存标签成功");
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除标签成功");
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String labelId,@RequestBody Label label){
        labelService.updateById(labelId,label);
        return new Result(true, StatusCode.OK,"更新标签成功");
    }

    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Label> labelList =  labelService.findAll();
        return new Result(true, StatusCode.OK,"查询所有标签成功",labelList);
    }

    /**
     * 根据id查询单个标签对象
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
       Label label =  labelService.findById(labelId);
        return new Result(true, StatusCode.OK,"根据id查询标签成功",label);
    }

    /**
     * 带条件（用户在页面输入的）的查询
     * 使用map接收请求体 更为灵活
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result search(@RequestBody Map map){
        List<Label> labelList =  labelService.search(map);
        return new Result(true, StatusCode.OK,"条件查询标签成功",labelList);
    }


    /**
     * 带条件分页查询
     * 使用map接收请求体 更为灵活
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result searchByPage(@RequestBody Map map,@PathVariable int page,@PathVariable int size){
        Page<Label> labelPage =  labelService.searchByPage(map,page,size);
        return new Result(true, StatusCode.OK,"条件分页查询标签成功",new PageResult<>(labelPage.getTotalElements(),labelPage.getContent()));
    }

}
