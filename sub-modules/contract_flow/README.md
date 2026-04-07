# 场景：企业级「跨平台合同全生命周期管理工作流」

## 核心定位
- 多角色：业务申请人、部门负责人、法务专员、风控专员、CEO、印章管理员、财务、外部客户
- 多平台：PC 管理后台、移动端审批、企业微信 / 钉钉、电子签平台、文件存储系统、CRM 系统

## 完整流程链路
1. **业务发起**

    业务人员在 PC 后台 / 移动端填写合同信息、上传合同文档，关联 CRM 客户数据，提交申请。


2. **部门初审**

    部门负责人在企业微信 / 钉钉收到待办，审核业务合理性，可驳回修改或通过。


3. **法务审核**

    法务在 PC 后台审核合同条款、风险点，标注修改意见，支持打回重拟。


4. 风控校验

    系统自动拉取客户风控数据，风控人员人工复核授信、合规风险，高风险合同自动升级审批。


5. 高层审批 

    按合同金额自动分流：普通合同总监审批，大额合同自动流转至 CEO 审批。


6. 用印申请

    审批通过后，自动发起用印流程，印章管理员核对合同版本后完成盖章并上传签章文件。


7. 外部签署

    流程自动对接电子签平台，向客户发送签署链接，客户完成签署后回调流程。


8. 归档与付款

    合同自动归档至文件系统，同步更新 CRM 状态，财务根据合同触发付款 / 收款流程。


9. 到期提醒

    定时器触发合同到期预警，推送至业务负责人跟进续签。

## 适配 Camunda8 核心能力
- 多角色 User Task、条件网关、并行 / 串行审批
- 定时器事件、消息中间事件（对接外部系统）
- Document 文档管理、服务任务（调用电子签 / CRM / 风控接口）
- 跨端待办集成、流程状态实时同步

---

# Contract Flow 实现指南

## 一、项目结构

```
contract_flow/
├── src/main/java/com/zhoubyte/contract_flow/
│   ├── ContractFlowApplication.java              # 启动类
│   ├── domain/                                   # 领域层
│   │   ├── model/                               # 领域模型
│   │   │   ├── Contract.java                    # 合同领域模型
│   │   │   └── Task.java                        # 任务领域模型
│   │   ├── valobj/                              # 值对象
│   │   │   ├── contract/
│   │   │   │   ├── ContractId.java              # 合同 ID
│   │   │   │   ├── ContractStatus.java          # 合同状态
│   │   │   │   ├── ContractType.java            # 合同类型
│   │   │   │   └── ContractAmount.java          # 合同金额
│   │   │   └── task/
│   │   │       ├── TaskId.java                  # 任务 ID
│   │   │       └── TaskStatus.java              # 任务状态
│   │   ├── repository/                          # 仓储接口
│   │   │   ├── IContractRepository.java
│   │   │   └── IContractTaskRepository.java
│   │   ├── service/                             # 领域服务
│   │   │   ├── ContractService.java
│   │   │   └── ContractJobService.java
│   │   └── utils/
│   │       └── IdGenerator.java                 # ID 生成器
│   ├── application/                             # 应用层
│   │   ├── config/
│   │   │   └── ContractConstant.java            # 常量定义
│   │   ├── dto/
│   │   │   ├── ContractCreateParam.java         # 创建合同参数
│   │   │   └── CreateJobParam.java              # 创建任务参数
│   │   └── service/
│   │       ├── ContractCreateService.java       # 创建合同服务
│   │       ├── ContractTaskCompleteService.java # 完成任务服务
│   │       ├── ContractJobCreateService.java    # 创建任务服务
│   │       └── ContractMessageService.java      # 消息服务
│   ├── infra/                                   # 基础设施层
│   │   ├── config/
│   │   │   └── MybatisPageConfig.java           # 分页配置
│   │   ├── persistence/
│   │   │   ├── dao/
│   │   │   │   ├── ContractMapper.java          # 合同 Mapper
│   │   │   │   └── ContractTaskMapper.java      # 任务 Mapper
│   │   │   ├── po/
│   │   │   │   ├── ContractPO.java              # 合同持久化对象
│   │   │   │   └── ContractTaskPO.java          # 任务持久化对象
│   │   │   ├── converter/
│   │   │   │   ├── ContractConverter.java       # 合同转换器
│   │   │   │   └── TaskConverter.java           # 任务转换器
│   │   │   └── repository/
│   │   │       ├── ContractRepository.java      # 合同仓储实现
│   │   │       └── ContractTaskRepository.java  # 任务仓储实现
│   │   └── external/
│   │       ├── ContractCreateJobWorker.java     # 创建任务 JobWorker
│   │       └── ContractServiceTaskWorker.java   # 服务任务 JobWorker
│   └── facade/                                  # 表现层
│       ├── endpoint/
│       │   ├── ContractFlowController.java      # 合同流程控制器
│       │   ├── ContractFlowTaskController.java  # 任务控制器
│       │   └── ContractMessageController.java   # 消息控制器
│       ├── dto/
│       │   ├── request/
│       │   │   ├── ContractCreateRequest.java   # 创建合同请求
│       │   │   ├── AuditRequest.java            # 审批请求
│       │   │   ├── ExternalSignFeedbackRequest.java # 外部签署反馈
│       │   │   ├── PaymentFeedbackRequest.java  # 付款反馈
│       │   │   └── ContractSearchRequest.java   # 查询合同请求
│       │   └── response/
│       │       └── ContractSearchResponse.java  # 查询合同响应
│       └── converter/
│           └── ContractFacadeConverter.java     # Facade 转换器
└── src/main/resources/
    ├── application.yaml                          # 配置文件
    ├── migration/
    │   └── V1.0.1__init.sql                     # 数据库迁移脚本
    └── bpmn/
        └── contract_process.bpmn                 # BPMN 流程定义
```

## 二、核心代码实现

### 1. 启动类

**文件**: `ContractFlowApplication.java`

```java
package com.zhoubyte.contract_flow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhoubyte.contract_flow.infra.persistence.dao")
public class ContractFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractFlowApplication.class, args);
    }
}
```

### 2. 领域层

#### 2.1 值对象

**ContractStatus.java** - 合同状态枚举

```java
package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractStatus {
    
    DRAFT("DRAFT", "草稿"),
    PENDING("PENDING", "待审批"),
    DEPARTMENT_REVIEW("DEPARTMENT_REVIEW", "部门审核中"),
    LEGAL_REVIEW("LEGAL_REVIEW", "法务审核中"),
    RISK_CONTROL("RISK_CONTROL", "风控校验中"),
    HIGH_LEVEL_APPROVAL("HIGH_LEVEL_APPROVAL", "高层审批中"),
    SEAL_APPLICATION("SEAL_APPLICATION", "用印申请中"),
    EXTERNAL_SIGNING("EXTERNAL_SIGNING", "外部签署中"),
    ARCHIVED("ARCHIVED", "已归档"),
    COMPLETED("COMPLETED", "已完成"),
    REJECTED("REJECTED", "已拒绝"),
    CANCELED("CANCELED", "已取消");
    
    private final String value;
    private final String name;
    
    public static ContractStatus fromValue(String value) {
        for (ContractStatus status : ContractStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ContractStatus value: " + value);
    }
}
```

**ContractId.java** - 合同 ID 值对象

```java
package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.Value;

@Value
public class ContractId {
    String value;
    
    public static ContractId form(String value) {
        return new ContractId(value);
    }
}
```

**TaskStatus.java** - 任务状态枚举

```java
package com.zhoubyte.contract_flow.domain.valobj.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    
    CREATE("CREATE", "已创建"),
    PROCESS("PROCESS", "处理中"),
    COMPLETED("COMPLETED", "已完成"),
    FAILED("FAILED", "失败");
    
    private final String value;
    private final String description;
    
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus value: " + value);
    }
}
```

#### 2.2 领域模型

**Contract.java** - 合同领域模型

```java
package com.zhoubyte.contract_flow.domain.model;

import com.zhoubyte.contract_flow.domain.valobj.contract.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Contract {
    
    private ContractId contractId;
    private String contractName;
    private String contractNumber;
    private ContractType contractType;
    private ContractAmount contractAmount;
    private ContractStatus contractStatus;
    
    private String crmCustomerId;
    private String contractDocumentId;
    private String signedDocumentId;
    private String contractRemark;
    
    private Long expectedCompletionTime;
    private Long expiryTime;
    
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    
    private String creatorName;
    private String creatorId;
    private String updateId;
    private String updateName;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String currentAssignee;
    
    // 业务方法：提交审批
    public void submitForApproval(String operatorId, String operatorName) {
        if (!ContractStatus.DRAFT.equals(contractStatus)) {
            throw new RuntimeException("只有草稿状态的合同才能提交审批");
        }
        this.contractStatus = ContractStatus.PENDING;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：部门审核通过
    public void departmentApprove(String operatorId, String operatorName) {
        if (!ContractStatus.PENDING.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许部门审核");
        }
        this.contractStatus = ContractStatus.DEPARTMENT_REVIEW;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：法务审核通过
    public void legalApprove(String operatorId, String operatorName) {
        if (!ContractStatus.DEPARTMENT_REVIEW.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许法务审核");
        }
        this.contractStatus = ContractStatus.LEGAL_REVIEW;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：风控校验通过
    public void riskControlApprove(String operatorId, String operatorName) {
        if (!ContractStatus.LEGAL_REVIEW.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许风控校验");
        }
        this.contractStatus = ContractStatus.RISK_CONTROL;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：高层审批通过
    public void highLevelApprove(String operatorId, String operatorName) {
        if (!ContractStatus.RISK_CONTROL.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许高层审批");
        }
        this.contractStatus = ContractStatus.HIGH_LEVEL_APPROVAL;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：完成签署
    public void completeSigning(String operatorId, String operatorName) {
        this.contractStatus = ContractStatus.COMPLETED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：拒绝
    public void reject(String operatorId, String operatorName, String reason) {
        this.contractStatus = ContractStatus.REJECTED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.contractRemark = reason;
        this.updateTime = LocalDateTime.now();
    }
    
    // 业务方法：取消
    public void cancel(String operatorId, String operatorName) {
        this.contractStatus = ContractStatus.CANCELED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    // 判断是否需要高层审批（金额大于 100 万）
    public boolean needHighLevelApproval() {
        return contractAmount != null && contractAmount.getValue() > 100_000_000L;
    }
    
    // 判断是否高风险合同
    public boolean isHighRisk() {
        // 这里可以根据业务规则判断，例如金额大于 500 万
        return contractAmount != null && contractAmount.getValue() > 500_000_000L;
    }
}
```

**Task.java** - 任务领域模型

```java
package com.zhoubyte.contract_flow.domain.model;

import com.zhoubyte.contract_flow.domain.valobj.task.TaskId;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Task {
    
    private TaskId id;
    private Long taskKey;
    private ContractId contractId;
    private String taskType;
    private Integer processDefinitionVersion;
    private Long processDefinitionKey;
    private String processInstanceId;
    private Long processInstanceKey;
    private String elementId;
    private Long elementInstanceKey;
    private TaskStatus taskStatus;
    private Integer retries;
    private String variables;
    private String candidateGroups;
    private String assignee;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public void toProcess() {
        if (!TaskStatus.CREATE.equals(taskStatus)) {
            throw new RuntimeException(taskStatus.getDescription());
        }
        this.taskStatus = TaskStatus.PROCESS;
        this.updateTime = LocalDateTime.now();
    }
    
    public void toComplete() {
        if (!TaskStatus.PROCESS.equals(taskStatus)) {
            throw new RuntimeException(taskStatus.getDescription());
        }
        this.taskStatus = TaskStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }
}
```

#### 2.3 仓储接口

**IContractRepository.java**

```java
package com.zhoubyte.contract_flow.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.facade.dto.request.ContractSearchRequest;

import java.util.Optional;

public interface IContractRepository {
    
    Contract saveOrUpdate(Contract contract);
    
    Page<Contract> queryContractList(ContractSearchRequest request);
    
    Optional<Contract> findByContractId(ContractId contractId);
}
```

**IContractTaskRepository.java**

```java
package com.zhoubyte.contract_flow.domain.repository;

import com.zhoubyte.contract_flow.domain.model.Task;

import java.util.Optional;

public interface IContractTaskRepository {
    
    Task saveOrUpdate(Task task);
    
    Optional<Task> findByTaskKey(Long taskKey);
}
```

#### 2.4 领域服务

**ContractService.java**

```java
package com.zhoubyte.contract_flow.domain.service;

import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.repository.IContractRepository;
import com.zhoubyte.contract_flow.domain.utils.IdGenerator;
import com.zhoubyte.contract_flow.domain.valobj.contract.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContractService {
    
    private final IContractRepository contractRepository;
    private final IdGenerator idGenerator;
    
    public ContractService(IContractRepository contractRepository, IdGenerator idGenerator) {
        this.contractRepository = contractRepository;
        this.idGenerator = idGenerator;
    }
    
    public Contract createContract(ContractCreateParam param) {
        String contractId = idGenerator.generateContractId();
        
        Contract contract = Contract.builder()
                .contractId(ContractId.form(contractId))
                .contractName(param.getContractName())
                .contractNumber(param.getContractNumber())
                .contractType(ContractType.fromValue(param.getContractType()))
                .contractAmount(new ContractAmount(param.getContractAmount()))
                .contractStatus(ContractStatus.DRAFT)
                .crmCustomerId(param.getCrmCustomerId())
                .contractDocumentId(param.getContractDocumentId())
                .contractRemark(param.getContractRemark())
                .expectedCompletionTime(param.getExpectedCompletionTime())
                .expiryTime(param.getExpiryTime())
                .isNotify(param.getIsNotify())
                .preOverdueDays(param.getPreOverdueDays())
                .postOverdueDays(param.getPostOverdueDays())
                .creatorName(param.getCreatorName())
                .creatorId(param.getCreatorId())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        
        return contractRepository.saveOrUpdate(contract);
    }
    
    public void updateContractStatus(ContractId contractId, String operatorId, 
                                     String operatorName, ContractStatus newStatus) {
        Contract contract = contractRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        
        contract.setContractStatus(newStatus);
        contract.setUpdateId(operatorId);
        contract.setUpdateName(operatorName);
        contract.setUpdateTime(LocalDateTime.now());
        
        contractRepository.saveOrUpdate(contract);
    }
}
```

**ContractJobService.java**

```java
package com.zhoubyte.contract_flow.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhoubyte.contract_flow.application.config.ContractConstant;
import com.zhoubyte.contract_flow.application.dto.CreateJobParam;
import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.repository.IContractTaskRepository;
import com.zhoubyte.contract_flow.domain.utils.IdGenerator;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskId;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ContractJobService {
    
    private final IContractTaskRepository contractTaskRepository;
    private final IdGenerator idGenerator;
    private final ObjectMapper objectMapper;
    
    public ContractJobService(IContractTaskRepository contractTaskRepository, 
                              IdGenerator idGenerator,
                              ObjectMapper objectMapper) {
        this.contractTaskRepository = contractTaskRepository;
        this.idGenerator = idGenerator;
        this.objectMapper = objectMapper;
    }
    
    public Task persistenceTask(CreateJobParam createJobParam) {
        if (createJobParam == null) {
            throw new IllegalArgumentException("CreateJobParam cannot be null");
        }
        
        String generateTaskId = idGenerator.generateTaskId();
        Map<String, Object> variables = createJobParam.getVariablesAsMap();
        
        if (variables == null) {
            throw new RuntimeException("Variables cannot be null");
        }
        
        String contractId = (String) variables.get(ContractConstant.CONTRACT_ID);
        if (contractId == null || contractId.isEmpty()) {
            throw new RuntimeException("Contract ID not found in variables");
        }
        
        Task task = Task.builder()
                .id(TaskId.form(generateTaskId))
                .taskKey(createJobParam.getTaskKey())
                .contractId(ContractId.form(contractId))
                .taskType(createJobParam.getTaskType())
                .processDefinitionVersion(createJobParam.getProcessDefinitionVersion())
                .processDefinitionKey(createJobParam.getProcessDefinitionKey())
                .processInstanceId(createJobParam.getProcessInstanceId())
                .processInstanceKey(createJobParam.getProcessInstanceKey())
                .elementId(createJobParam.getElementId())
                .elementInstanceKey(createJobParam.getElementInstanceKey())
                .taskStatus(TaskStatus.CREATE)
                .variables(createJobParam.getVariables())
                .assignee(createJobParam.getAssignee())
                .build();
        
        if (createJobParam.getCandidateGroups() != null && !createJobParam.getCandidateGroups().isEmpty()) {
            try {
                String candidateGroupsJson = objectMapper.writeValueAsString(createJobParam.getCandidateGroups());
                task.setCandidateGroups(candidateGroupsJson);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert candidate groups to JSON", e);
            }
        }
        
        return contractTaskRepository.saveOrUpdate(task);
    }
    
    public Boolean updateTask(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task or TaskId cannot be null");
        }
        
        Task savedTask = contractTaskRepository.saveOrUpdate(task);
        return savedTask != null;
    }
    
    public Optional<Task> findByTaskKey(Long taskKey) {
        if (taskKey == null) {
            throw new IllegalArgumentException("TaskKey cannot be null");
        }
        
        return contractTaskRepository.findByTaskKey(taskKey);
    }
}
```

### 3. 应用层

#### 3.1 常量定义

**ContractConstant.java**

```java
package com.zhoubyte.contract_flow.application.config;

public class ContractConstant {
    
    public static final String CONTRACT_ID = "contract_id";
    public static final String CONTRACT_NAME = "contract_name";
    public static final String CONTRACT_NUMBER = "contract_number";
    public static final String CONTRACT_TYPE = "contract_type";
    public static final String CONTRACT_AMOUNT = "contract_amount";
    public static final String CONTRACT_STATUS = "contract_status";
    public static final String CRM_CUSTOMER_ID = "crm_customer_id";
    public static final String CONTRACT_DOCUMENT_ID = "contract_document_id";
    public static final String EXPECTED_COMPLETION_TIME = "expected_completion_time";
    public static final String EXPIRY_TIME = "expiry_time";
    public static final String IS_NOTIFY = "is_notify";
    public static final String PRE_OVERDUE_DAYS = "pre_overdue_days";
    public static final String POST_OVERDUE_DAYS = "post_overdue_days";
    public static final String CREATOR_NAME = "creator_name";
    public static final String CREATOR_ID = "creator_id";
    public static final String OVERDUE_DEFINITION = "overdue_definition";
    
    public static final String PROCESS_INSTANCE_ID = "contract_process";
    
    public static final Long HIGH_LEVEL_APPROVAL_THRESHOLD = 100_000_000L; // 100 万
    public static final Long HIGH_RISK_THRESHOLD = 500_000_000L; // 500 万
}
```

#### 3.2 DTO

**ContractCreateParam.java**

```java
package com.zhoubyte.contract_flow.application.dto;

import lombok.Data;

@Data
public class ContractCreateParam {
    
    private String contractName;
    private String contractNumber;
    private String contractType;
    private Long contractAmount;
    private String crmCustomerId;
    private String contractDocumentId;
    private String contractRemark;
    private Long expectedCompletionTime;
    private Long expiryTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    private String creatorName;
    private String creatorId;
}
```

**CreateJobParam.java**

```java
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
```

#### 3.3 应用服务

**ContractCreateService.java**

```java
package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.application.config.ContractConstant;
import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.service.ContractService;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractCreateService {
    
    private final ContractService contractService;
    private final ProcessService processService;
    
    public ContractId createContractAndStartProcess(ContractCreateParam param) {
        log.info("Creating contract and starting process: {}", param);
        
        Contract contract = contractService.createContract(param);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put(ContractConstant.CONTRACT_ID, contract.getContractId().getValue());
        variables.put(ContractConstant.CONTRACT_NAME, contract.getContractName());
        variables.put(ContractConstant.CONTRACT_NUMBER, contract.getContractNumber());
        variables.put(ContractConstant.CONTRACT_TYPE, contract.getContractType().getValue());
        variables.put(ContractConstant.CONTRACT_AMOUNT, contract.getContractAmount().getValue());
        variables.put(ContractConstant.CRM_CUSTOMER_ID, contract.getCrmCustomerId());
        variables.put(ContractConstant.CONTRACT_DOCUMENT_ID, contract.getContractDocumentId());
        variables.put(ContractConstant.EXPECTED_COMPLETION_TIME, contract.getExpectedCompletionTime());
        variables.put(ContractConstant.EXPIRY_TIME, contract.getExpiryTime());
        variables.put(ContractConstant.IS_NOTIFY, contract.getIsNotify());
        variables.put(ContractConstant.PRE_OVERDUE_DAYS, contract.getPreOverdueDays());
        variables.put(ContractConstant.POST_OVERDUE_DAYS, contract.getPostOverdueDays());
        variables.put(ContractConstant.CREATOR_NAME, contract.getCreatorName());
        variables.put(ContractConstant.CREATOR_ID, contract.getCreatorId());
        
        if (contract.getExpiryTime() != null) {
            long overdueDuration = contract.getExpiryTime() - System.currentTimeMillis();
            variables.put(ContractConstant.OVERDUE_DEFINITION, "PT" + (overdueDuration / 1000) + "S");
        }
        
        processService.startProcess(ContractConstant.PROCESS_INSTANCE_ID, 
                                    contract.getContractId().getValue(), 
                                    variables);
        
        log.info("Contract created and process started: contractId = {}", contract.getContractId().getValue());
        
        return contract.getContractId();
    }
}
```

**ContractTaskCompleteService.java**

```java
package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.service.ContractJobService;
import com.zhoubyte.contract_flow.domain.service.ContractService;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractStatus;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractTaskCompleteService {
    
    private final ProcessService processService;
    private final ContractJobService contractJobService;
    private final ContractService contractService;
    
    @Transactional
    public void completeUserTask(Long taskKey, Integer auditResult, String msg, 
                                  String operatorId, String operatorName) {
        log.info("completeUserTask taskKey = {}, auditResult = {}, msg = {}, operatorId = {}, operatorName = {}", 
                taskKey, auditResult, msg, operatorId, operatorName);
        
        Task task = contractJobService.findByTaskKey(taskKey)
                .orElseThrow(() -> new RuntimeException("Task not found with taskKey: " + taskKey));
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("audit_result", auditResult);
        variables.put("audit_msg", msg);
        
        boolean approved = auditResult != null && auditResult == 1;
        variables.put("approved", approved);
        
        processService.completeUserTask(taskKey, variables);
        
        task.toComplete();
        task.setAssignee(operatorId);
        contractJobService.updateTask(task);
        
        updateContractStatusByElementId(task.getElementId(), task.getContractId().getValue(), 
                approved, operatorId, operatorName, msg);
        
        log.info("UserTask 已完成: taskKey = {}, approved = {}", taskKey, approved);
    }
    
    private void updateContractStatusByElementId(String elementId, String contractId, 
            boolean approved, String operatorId, String operatorName, String msg) {
        
        ContractId contractIdObj = ContractId.form(contractId);
        
        switch (elementId) {
            case "department_review" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.DEPARTMENT_REVIEW);
                    log.info("部门审核通过，合同状态更新为部门审核中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("部门审核不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "legal_review" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.LEGAL_REVIEW);
                    log.info("法务审核通过，合同状态更新为法务审核中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("法务审核不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "director_approval" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.HIGH_LEVEL_APPROVAL);
                    log.info("总监审批通过，合同状态更新为高层审批中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("总监审批不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "ceo_approval" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.SEAL_APPLICATION);
                    log.info("CEO 审批通过，合同状态更新为用印申请中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("CEO 审批不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "seal_application" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.EXTERNAL_SIGNING);
                    log.info("用印申请通过，合同状态更新为外部签署中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("用印申请不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            default -> log.debug("节点 {} 不需要更新合同状态", elementId);
        }
    }
}
```

**ContractJobCreateService.java**

```java
package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.application.dto.CreateJobParam;
import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.service.ContractJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractJobCreateService {
    
    private final ContractJobService contractJobService;
    
    public void createJobAndRecordLog(CreateJobParam createJobParam) {
        if (createJobParam == null) {
            throw new IllegalArgumentException("CreateJobParam cannot be null");
        }
        
        Task task = contractJobService.persistenceTask(createJobParam);
        task.toProcess();
        
        if (!contractJobService.updateTask(task)) {
            throw new RuntimeException("任务持久化失败");
        }
        
        log.info("Task created successfully: taskId = {}, taskKey = {}, elementId = {}", 
                task.getId().getValue(), task.getTaskKey(), task.getElementId());
    }
}
```

**ContractMessageService.java**

```java
package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractMessageService {
    
    private final ProcessService processService;
    
    public void sendExternalSignFeedback(String contractId, Boolean signed, String feedbackMsg) {
        log.info("发送外部签署反馈消息: contractId = {}, signed = {}, feedbackMsg = {}", 
                contractId, signed, feedbackMsg);
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("external_signed", signed);
            variables.put("external_feedback_msg", feedbackMsg);
            variables.put("external_feedback_time", System.currentTimeMillis());
            
            processService.publishMessage("external_sign_feedback_message", contractId, variables);
            
            log.info("外部签署反馈消息已发送: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送外部签署反馈消息失败: contractId = {}", contractId, e);
            throw new RuntimeException("发送外部签署反馈消息失败", e);
        }
    }
    
    public void sendPaymentFeedback(String contractId, Boolean paymentSuccess, String paymentMsg) {
        log.info("发送付款反馈消息: contractId = {}, paymentSuccess = {}, paymentMsg = {}", 
                contractId, paymentSuccess, paymentMsg);
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("payment_success", paymentSuccess);
            variables.put("payment_msg", paymentMsg);
            variables.put("payment_time", System.currentTimeMillis());
            
            processService.publishMessage("payment_feedback_message", contractId, variables);
            
            log.info("付款反馈消息已发送: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送付款反馈消息失败: contractId = {}", contractId, e);
            throw new RuntimeException("发送付款反馈消息失败", e);
        }
    }
}
```

## 三、API 接口

### 1. 合同流程控制器

**ContractFlowController.java**

```java
package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.application.service.ContractCreateService;
import com.zhoubyte.contract_flow.facade.dto.request.ContractCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contract")
@Slf4j
@RequiredArgsConstructor
public class ContractFlowController {
    
    private final ContractCreateService contractCreateService;
    
    @PostMapping("/create")
    public ResponseEntity<String> createContract(@RequestBody @Validated ContractCreateRequest request) {
        log.info("Creating contract: {}", request);
        
        ContractCreateParam param = new ContractCreateParam();
        param.setContractName(request.getContractName());
        param.setContractNumber(request.getContractNumber());
        param.setContractType(request.getContractType());
        param.setContractAmount(request.getContractAmount());
        param.setCrmCustomerId(request.getCrmCustomerId());
        param.setContractDocumentId(request.getContractDocumentId());
        param.setContractRemark(request.getContractRemark());
        param.setExpectedCompletionTime(request.getExpectedCompletionTime());
        param.setExpiryTime(request.getExpiryTime());
        param.setIsNotify(request.getIsNotify());
        param.setPreOverdueDays(request.getPreOverdueDays());
        param.setPostOverdueDays(request.getPostOverdueDays());
        param.setCreatorName(request.getCreatorName());
        param.setCreatorId(request.getCreatorId());
        
        var contractId = contractCreateService.createContractAndStartProcess(param);
        
        log.info("Contract created successfully: contractId = {}", contractId.getValue());
        
        return ResponseEntity.ok(contractId.getValue());
    }
}
```

### 2. 任务控制器

**ContractFlowTaskController.java**

```java
package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.service.ContractTaskCompleteService;
import com.zhoubyte.contract_flow.facade.dto.request.AuditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@Slf4j
@RequiredArgsConstructor
public class ContractFlowTaskController {
    
    private final ContractTaskCompleteService contractTaskCompleteService;
    
    @PostMapping("/{task_key}:complete")
    public ResponseEntity<String> completeUserTask(
            @PathVariable("task_key") Long taskKey,
            @RequestBody @Validated AuditRequest auditRequest) {
        
        log.info("Completing user task: taskKey = {}, auditResult = {}, operatorId = {}", 
                taskKey, auditRequest.getAuditResult(), auditRequest.getOperatorId());
        
        contractTaskCompleteService.completeUserTask(
                taskKey,
                auditRequest.getAuditResult(),
                auditRequest.getMsg(),
                auditRequest.getOperatorId(),
                auditRequest.getOperatorName()
        );
        
        log.info("User task completed successfully: taskKey = {}", taskKey);
        
        return ResponseEntity.ok("Task completed successfully");
    }
}
```

### 3. 消息控制器

**ContractMessageController.java**

```java
package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.service.ContractMessageService;
import com.zhoubyte.contract_flow.facade.dto.request.ExternalSignFeedbackRequest;
import com.zhoubyte.contract_flow.facade.dto.request.PaymentFeedbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@RequiredArgsConstructor
public class ContractMessageController {
    
    private final ContractMessageService contractMessageService;
    
    @PostMapping("/external-sign/feedback")
    public ResponseEntity<String> sendExternalSignFeedback(
            @RequestBody @Validated ExternalSignFeedbackRequest request) {
        
        log.info("Sending external sign feedback: contractId = {}, signed = {}", 
                request.getContractId(), request.getSigned());
        
        contractMessageService.sendExternalSignFeedback(
                request.getContractId(),
                request.getSigned(),
                request.getFeedbackMsg()
        );
        
        log.info("External sign feedback sent successfully: contractId = {}", request.getContractId());
        
        return ResponseEntity.ok("External sign feedback sent successfully");
    }
    
    @PostMapping("/payment/feedback")
    public ResponseEntity<String> sendPaymentFeedback(
            @RequestBody @Validated PaymentFeedbackRequest request) {
        
        log.info("Sending payment feedback: contractId = {}, paymentSuccess = {}", 
                request.getContractId(), request.getPaymentSuccess());
        
        contractMessageService.sendPaymentFeedback(
                request.getContractId(),
                request.getPaymentSuccess(),
                request.getPaymentMsg()
        );
        
        log.info("Payment feedback sent successfully: contractId = {}", request.getContractId());
        
        return ResponseEntity.ok("Payment feedback sent successfully");
    }
}
```

## 四、API 使用示例

### 1. 创建合同

```bash
POST /api/v1/contract/create
Content-Type: application/json

{
  "contractName": "测试合同",
  "contractNumber": "HT2024001",
  "contractType": "SALES",
  "contractAmount": 200000000,
  "crmCustomerId": "CRM001",
  "contractDocumentId": "DOC001",
  "contractRemark": "测试合同备注",
  "expectedCompletionTime": 1735689600000,
  "expiryTime": 1767225600000,
  "isNotify": true,
  "preOverdueDays": 7,
  "postOverdueDays": 3,
  "creatorName": "张三",
  "creatorId": "user001"
}
```

### 2. 审批任务

```bash
POST /api/v1/task/12345:complete
Content-Type: application/json

{
  "auditResult": 1,
  "msg": "同意",
  "operatorId": "user001",
  "operatorName": "张三"
}
```

### 3. 外部签署反馈

```bash
POST /api/v1/message/external-sign/feedback
Content-Type: application/json

{
  "contractId": "CONTRACT_1234567890_abc123",
  "signed": true,
  "feedbackMsg": "签署成功"
}
```

### 4. 付款反馈

```bash
POST /api/v1/message/payment/feedback
Content-Type: application/json

{
  "contractId": "CONTRACT_1234567890_abc123",
  "paymentSuccess": true,
  "paymentMsg": "付款成功"
}
```

## 五、架构设计亮点

1. **DDD 架构**：严格遵循领域驱动设计原则
2. **分层清晰**：领域层、应用层、基础设施层、接口层职责明确
3. **事务管理**：使用 Spring 事务保证数据一致性
4. **异常处理**：完善的异常处理机制
5. **日志记录**：完整的操作日志
6. **参数校验**：使用 JSR-303 校验
7. **代码复用**：参考 procure_flow 的最佳实践

以上是完整的实现指南，你可以根据需要逐步实现各个模块。如果在实现过程中遇到问题，可以随时向我提问！
