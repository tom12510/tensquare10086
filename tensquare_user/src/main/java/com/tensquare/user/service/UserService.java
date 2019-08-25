package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加(后台管理系统-管理员-添加用户功能)
     * encode:密码加密
     * matches:匹配密码是否正确（将用户输入的密码加密+ 将两个加密的密码进行对比）
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {
        int min = 100000;
        int max = 999999;
        //生成验证码
        Random random = new Random();
        int newNum = random.nextInt(max);//不一定是6位
        if (newNum < min) {
            newNum = newNum + min;//最终一定是6位
        }

        //RabbitMQ存入 手机号码 + 验证码
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", newNum + "");
        System.out.println("手机号码：：：：："+mobile+"验证码：：：：：："+newNum);
        //将验证码存入RabbitMQ
        rabbitTemplate.convertAndSend("sms", map);
        //最后存入redis（后续校验）
        redisTemplate.opsForValue().set("code_" + mobile, newNum + "", 5, TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     * @param map
     */
    public void register(Map map,String code) {
        //获取手机号码
        String mobile = (String)map.get("mobile");
        //获取密码
        String password = (String)map.get("password");
        String nickname = (String)map.get("nickname");
        //1.验证码是否正确
       //根据手机号码从redis中获取的验证码
        String redisCode = (String)redisTemplate.opsForValue().get("code_" + mobile);
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(redisCode) || !code.equals(redisCode)){
            throw new RuntimeException("验证码错误");
        }
        //2.将用户数据存入数据
        User user = new User();
        user.setId(idWorker.nextId()+"");
        user.setMobile(mobile);
        user.setRegdate(new Date());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setFanscount(0);
        user.setNickname(nickname);
        user.setFollowcount(0);
        userDao.save(user);
    }

    /**
     * 用户登录
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        String dbPassword = user.getPassword();
        //参数1：用户输入的密码  参数2：数据库查询的密码
        if(encoder.matches(password,dbPassword)){
            return user;
        }
        return null;
    }

    /**
     * 关注数变更
     * @param userid
     * @param x
     */
    @Transactional
    public void incFollowcount(String userid, int x) {
        userDao.incFollowcount(userid,x);
    }
    /**
     * 粉丝数变更
     * @param userid
     * @param x
     */
    @Transactional
    public void incFanscount(String userid, int x) {
        userDao.incFanscount(userid,x);
    }
}
