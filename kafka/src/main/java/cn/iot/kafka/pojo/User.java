package cn.iot.kafka.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zh
 */
@Data
public class User {

    public Long id;

    public String msg;

    public Date sendTime;

}
