package com.zhoubyte.contract_flow.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CreateJobParam {
    
    private String ticketId;
    private Long taskKey;
    private String taskType;
    private Integer processDefinitionVersion;
    private Long processDefinitionKey;
    private String processInstanceId;
    private Long processInstanceKey;
    private String elementId;
    private Long elementInstanceKey;
    private String variables;
    private Map<String, Object> variablesAsMap;
    private List<String> candidateGroups;
    private String assignee;
    private Integer retries;
}
