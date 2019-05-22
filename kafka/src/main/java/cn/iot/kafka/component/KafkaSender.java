package cn.iot.kafka.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

/**
 * 消息生产者
 *
 * @author zh
 */

@Slf4j
@Component
public class KafkaSender<T> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * kafka 发送消息
     *
     * @param obj 消息对象
     */
    public void send(T obj) {
        String jsonObj = JSON.toJSONString(obj);
        log.info("------------ message = {}", jsonObj);

        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("kafka.tut", jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //TODO 业务处理
                log.info("Produce: The message was sent successfully:");
                log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
            }
        });
    }

    /**
     * 监听kafka.tut 的 topic
     *
     * @param record record
     * @param topic  topic
     */
    @KafkaListener(id = "tut", topics = "kafka.tut")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        //判断是否NULL
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            //获取消息
            Object message = kafkaMessage.get();
            log.info("Receive： +++++++++++++++ Topic:" + topic);
            log.info("Receive： +++++++++++++++ Record:" + record);
            log.info("Receive： +++++++++++++++ Message:" + message);
        }
    }

}
