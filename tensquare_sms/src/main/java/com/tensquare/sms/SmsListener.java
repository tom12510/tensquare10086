package com.tensquare.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;
/**
 * 消息监听者（消费者）
 * @author wangxin
 * @version 1.0
 */
@RabbitListener(queues = "sms")
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    /**
     * 新增方法 调用短信接口发送验证码
     */
    @RabbitHandler
    public void sendMessage(Map<String,String> map){
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("手机号码：：：：："+mobile+"验证码：：：：："+code);
        try {
            smsUtil.sendSms(mobile,templateCode,signName,"{\"code\":"+code+"}");
            System.out.println("短信发送成功了。。。");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
