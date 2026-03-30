package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.dto.ActivityMessageSubscription;
import com.zhoubyte.scorpio.dto.CorrelationMessageSubscription;
import com.zhoubyte.scorpio.dto.MessagePublishResult;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final ProcessEngineProvider processEngineProvider;

    public MessageService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        logger.info("MessageService init");
    }

    public MessagePublishResult publishMessage(String messageName, String correlationName, Map<String, Object> variables) {
        logger.info("MessageService publishMessage, messageName={}, correlationName={}, variables={}", messageName, correlationName, variables);
        return processEngineProvider.publishMessage(messageName, correlationName, variables);
    }

    public List<ActivityMessageSubscription> searchActivityMessageSubscription(String messageName) {
        logger.info("MessageService searchActivityMessageSubscription, messageName={}", messageName);
        return processEngineProvider.searchActivityMessageSubscriptions(messageName);
    }

    public List<CorrelationMessageSubscription> searchCorrelationMessageSubscription(String messageName) {
        logger.info("MessageService searchCorrelationMessageSubscription, messageName={}", messageName);
        return processEngineProvider.searchCorrelatedMessageSubscriptions(messageName);
    }




}
