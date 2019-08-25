package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.List;

/**
 * 吐槽业务层
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 发布吐槽
     *
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId()+"");
        //后台初始化的数据
        spit.setVisits(0);
        spit.setThumbup(0);
        spit.setShare(0);
        spit.setComment(0);
        spit.setState("0");//0:未审核 1:已经审核
        //parentid:如果是上级吐槽 此数据为空
        //parentid:如果是下级吐槽 此数据不会为空
        if(!StringUtils.isEmpty(spit.getParentid())){
            //为上级吐槽的评论数+1
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));// where _id = xxx
            //Update update：更新的字段 点赞数每次+1
            Update update = new Update();
            update.inc("comment",1);//inc运算符比较高效  每次+1
            // String collectionName：spit
            mongoTemplate.updateFirst(query,update,"spit");
        }
        else
        {
            spit.setParentid("0");//上级id默认值为0
        }
        spitDao.save(spit);
    }

    /**
     * 修改吐槽
     */
    public void updateById(Spit spit, String spitId) {
        spit.set_id(spitId);
        spitDao.save(spit);
    }

    /**
     * 删除吐槽
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }

    /**
     * 查询所有吐槽
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id查询吐槽
     */
    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    /**
     * 根据上级ID查询吐槽数据（分页）
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> comment(String parentid, int page, int size) {
        //方法命名规则查询
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    /**
     * 吐槽点赞
     * @param spitId
     */
    public void thumbup(String spitId) {
        //Query query:查询条件spitId
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));// where _id = xxx
        //Update update：更新的字段 点赞数每次+1
        Update update = new Update();
        update.inc("thumbup",1);//inc运算符比较高效  每次+1
        // String collectionName：spit
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
