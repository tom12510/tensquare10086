package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签业务层
 */
@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加
     * @param label
     */
    public void save(Label label) {
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 根据id删除标签数据
     * @param labelId
     */
    public void deleteById(String labelId) {
        labelDao.deleteById(labelId);
    }
    /**
     * 根据id更新标签数据
     * @param labelId
     */
    public void updateById(String labelId, Label label) {
        label.setId(labelId);//restful风格 id以地址栏中的id为准
        labelDao.save(label);
    }

    /**
     * 查询所有标签数据
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据id查询标签数据
     * @param labelId
     */
    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();
    }

    /**
     * 标签条件查询
     * @param map
     * @return
     */
    public List<Label> search(Map map) {
        //拼接查询条件
        /*Specification<Label> spec = new Specification<Label>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //map  :标签名称 state：状态
                String labelname = (String)map.get("labelname");
                String state = (String)map.get("state");

                //将Predicate放入list中
                List<Predicate> list = new ArrayList<>();

                if(!StringUtils.isEmpty(labelname)){
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + labelname + "%");
                    list.add(p1);
                }
                if(!StringUtils.isEmpty(state)){
                    Predicate p1 = criteriaBuilder.equal(root.get("state").as(String.class), state);
                    list.add(p1);
                }
                if(list == null || list.size() == 0){
                    return null;
                }
                //最终需要转成的数组对象
                Predicate[] predicates = new Predicate[list.size()];
                //将list集合转Predicate数组
                Predicate[] predicatesArray = list.toArray(predicates);
                return criteriaBuilder.and(predicatesArray);/// 条件1 = xx  or 条件2=yyy
            }
        };*/
        return labelDao.findAll(getSpecification(map));
    }

    /**
     * 条件分页查询
     * @param map
     * @return
     */
    public Page<Label> searchByPage(Map map,int page,int size) {
        //拼接分页条件（当前页码和每页显示记录数）
        Pageable pageable = PageRequest.of(page-1,size);//new PageRequest(page,size);
        return  labelDao.findAll(getSpecification(map),pageable);
    }

    /**
     * 抽取用户输入条件公共方法
     */
    public Specification<Label> getSpecification(Map map){
        return new Specification<Label>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //map  :标签名称 state：状态
                String labelname = (String)map.get("labelname");
                String state = (String)map.get("state");

                //将Predicate放入list中
                List<Predicate> list = new ArrayList<>();

                if(!StringUtils.isEmpty(labelname)){
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + labelname + "%");
                    list.add(p1);
                }
                if(!StringUtils.isEmpty(state)){
                    Predicate p1 = criteriaBuilder.equal(root.get("state").as(String.class), state);
                    list.add(p1);
                }
                if(list == null || list.size() == 0){
                    return null;
                }
                //最终需要转成的数组对象
                Predicate[] predicates = new Predicate[list.size()];
                //将list集合转Predicate数组
                Predicate[] predicatesArray = list.toArray(predicates);
                return criteriaBuilder.and(predicatesArray);/// 条件1 = xx  or 条件2=yyy
            }
        };
    }
}
