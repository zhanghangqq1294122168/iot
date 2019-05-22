package cn.iot.kafka.controller;

import cn.iot.kafka.component.KafkaSender;
import cn.iot.kafka.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author zh
 */
@RestController
public class TestController {

    private final KafkaSender<User> kafkaSender;

    @Autowired
    public TestController(KafkaSender<User> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @GetMapping("/test")
    public void kafkaSend() {
        //模拟发消息
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setMsg(UUID.randomUUID().toString());
        user.setSendTime(new Date());
        System.out.println(user.toString());
        kafkaSender.send(user);
    }

}
