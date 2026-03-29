package com.zhoubyte.scorpio.spi;


import com.zhoubyte.scorpio.support.DeployResult;
import com.zhoubyte.scorpio.support.ElementInstanceResult;
import com.zhoubyte.scorpio.support.ElementQuery;
import com.zhoubyte.scorpio.support.PageRequest;

import java.util.List;

// 流程引擎提供者
public interface ProcessEngineProvider {

    String engineName();

    DeployResult deployResource(List<String> resourcePaths);

    ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey);

    List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest pageRequest, ElementQuery instanceQuery);


}
