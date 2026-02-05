package com.stackscout.messaging;

import com.stackscout.config.RabbitMQConfig;
import com.stackscout.dto.ScanMessage;
import com.stackscout.service.CollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorConsumer {

    private final CollectorService collectorService;

    @RabbitListener(queues = RabbitMQConfig.SCAN_QUEUE)
    public void consumeScanRequest(ScanMessage message) {
        log.info("Received scan request for package: {}", message.getPackageName());
        try {
            collectorService.collect(message.getSource(), message.getPackageName());
        } catch (Exception e) {
            log.error("Error processing scan request for package {}: {}", message.getPackageName(), e.getMessage());
        }
    }
}
