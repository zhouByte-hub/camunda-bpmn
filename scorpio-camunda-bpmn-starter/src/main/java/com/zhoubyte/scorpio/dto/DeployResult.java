package com.zhoubyte.scorpio.dto;

import java.util.List;

public class DeployResult {

    private Long key;
    private String tenantId;
    private List<DeployProcess> processes;
    private List<DeployForm> form;
    private List<DeployDecision> decisions;

    public DeployResult() {
    }

    public DeployResult(Long key, String tenantId, List<DeployProcess> processes, List<DeployForm> form, List<DeployDecision> decisions) {
        this.key = key;
        this.tenantId = tenantId;
        this.processes = processes;
        this.form = form;
        this.decisions = decisions;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<DeployProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<DeployProcess> processes) {
        this.processes = processes;
    }

    public List<DeployForm> getForm() {
        return form;
    }

    public void setForm(List<DeployForm> form) {
        this.form = form;
    }

    public List<DeployDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<DeployDecision> decisions) {
        this.decisions = decisions;
    }

    public static class DeployProcess {
        private Long processDefinitionKey;
        private String bpmnProcessId;
        private Integer version;
        private String resourceName;
        private String tenantId;

        public DeployProcess() {
        }

        public DeployProcess(Long processDefinitionKey, String bpmnProcessId, Integer version, String resourceName, String tenantId) {
            this.processDefinitionKey = processDefinitionKey;
            this.bpmnProcessId = bpmnProcessId;
            this.version = version;
            this.resourceName = resourceName;
            this.tenantId = tenantId;
        }

        public Long getProcessDefinitionKey() {
            return processDefinitionKey;
        }

        public void setProcessDefinitionKey(Long processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
        }

        public String getBpmnProcessId() {
            return bpmnProcessId;
        }

        public void setBpmnProcessId(String bpmnProcessId) {
            this.bpmnProcessId = bpmnProcessId;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
    }

    public static class DeployForm {
        private String formId;
        private Integer version;
        private Long formKey;
        private String resourceName;
        private String tenantId;

        public DeployForm() {
        }

        public DeployForm(String formId, Integer version, Long formKey, String resourceName, String tenantId) {
            this.formId = formId;
            this.version = version;
            this.formKey = formKey;
            this.resourceName = resourceName;
            this.tenantId = tenantId;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Long getFormKey() {
            return formKey;
        }

        public void setFormKey(Long formKey) {
            this.formKey = formKey;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
    }

    public static class DeployDecision {
        private String decisionId;
        private String decisionName;
        private Integer version;
        private Long decisionKey;
        private String decisionRequirementsId;
        private Long decisionRequirementsKey;
        private String tenantId;

        public DeployDecision() {
        }

        public DeployDecision(String decisionId, String decisionName, Integer version, Long decisionKey, String decisionRequirementsId, Long decisionRequirementsKey, String tenantId) {
            this.decisionId = decisionId;
            this.decisionName = decisionName;
            this.version = version;
            this.decisionKey = decisionKey;
            this.decisionRequirementsId = decisionRequirementsId;
            this.decisionRequirementsKey = decisionRequirementsKey;
            this.tenantId = tenantId;
        }

        public String getDecisionId() {
            return decisionId;
        }

        public void setDecisionId(String decisionId) {
            this.decisionId = decisionId;
        }

        public String getDecisionName() {
            return decisionName;
        }

        public void setDecisionName(String decisionName) {
            this.decisionName = decisionName;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Long getDecisionKey() {
            return decisionKey;
        }

        public void setDecisionKey(Long decisionKey) {
            this.decisionKey = decisionKey;
        }

        public String getDecisionRequirementsId() {
            return decisionRequirementsId;
        }

        public void setDecisionRequirementsId(String decisionRequirementsId) {
            this.decisionRequirementsId = decisionRequirementsId;
        }

        public Long getDecisionRequirementsKey() {
            return decisionRequirementsKey;
        }

        public void setDecisionRequirementsKey(Long decisionRequirementsKey) {
            this.decisionRequirementsKey = decisionRequirementsKey;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
    }
}
