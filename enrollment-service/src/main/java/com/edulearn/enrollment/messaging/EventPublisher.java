package com.edulearn.enrollment.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.edulearn.enrollment.config.RabbitMQConfig.EXCHANGE;

@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, Object event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, routingKey, event);
            log.info("?? Published event [{}] to exchange [{}]", routingKey, EXCHANGE);
        } catch (Exception e) {
            log.warn("RabbitMQ publish failed ({}): {} — continuing without notification",
                     routingKey, e.getMessage());
        }
    }
}
