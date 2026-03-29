package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import com.zhoubyte.scorpio.support.ElementInstanceResult;
import com.zhoubyte.scorpio.support.ElementQuery;
import com.zhoubyte.scorpio.support.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElementInstanceService {

    private final Logger logger = LoggerFactory.getLogger(ElementInstanceService.class);
    private final ProcessEngineProvider processEngineProvider;

    public ElementInstanceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        logger.info("ElementInstanceService init");
    }

    public ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey) {
        logger.debug("queryElementInstanceFromKey");
        return processEngineProvider.queryElementInstanceFromKey(elementInstanceKey);
    }

    public List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest query, ElementQuery instanceQuery) {
        logger.debug("executeElementInstanceSearchQuery");
        return processEngineProvider.executeElementInstanceSearchQuery(query, instanceQuery);
    }
}
