package com.zhoubyte.scorpio.dto;

import java.util.Map;

public class ElementQuery {

    private Map<ElementInstance, Sort> sort;
    private Map<ElementInstance, String> filter;

    public ElementQuery() {
    }

    public ElementQuery(Map<ElementInstance, Sort> sort, Map<ElementInstance, String> filter) {
        this.sort = sort;
        this.filter = filter;
    }


    public Map<ElementInstance, Sort> getSort() {
        return sort;
    }

    public void setSort(Map<ElementInstance, Sort> sort) {
        this.sort = sort;
    }

    public Map<ElementInstance, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<ElementInstance, String> filter) {
        this.filter = filter;
    }

    public enum ElementInstance {
        ELEMENT_INSTANCE_KEY,
        PROCESS_INSTANCE_KEY,
        PROCESS_DEFINITION_KEY,
        PROCESS_DEFINITION_ID,
        START_DATE,
        END_DATE,
        ELEMENT_ID,
        ELEMENT_NAME,
        TYPE,
        STATE,
        INCIDENT_KEY,
        TENANT_ID
    }
    
    public enum Sort {
        ASC,
        DESC
    }
    
}
