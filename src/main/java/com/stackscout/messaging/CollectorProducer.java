package com.stackscout.messaging;

import com.stackscout.config.RabbitMQConfig;
import com.stackscout.dto.ScanMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendScanRequest(String source, String packageName) {
        log.info("Sending scan request for package: {} source: {}", packageName, source);
        ScanMessage message = ScanMessage.builder()
                .source(source)
                .packageName(packageName)
                .build();
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SCAN_EXCHANGE,
                RabbitMQConfig.SCAN_ROUTING_KEY,
                message
        );
    }
}
