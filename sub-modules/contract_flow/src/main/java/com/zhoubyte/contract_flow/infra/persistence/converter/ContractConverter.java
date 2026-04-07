package com.zhoubyte.contract_flow.infra.persistence.converter;

import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.valobj.contract.*;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractConverter {
    
    @Mapping(source = "contractId.value", target = "ticketId")
    @Mapping(source = "contractName", target = "ticketName")
    @Mapping(source = "contractRemark", target = "ticketRemark")
    ContractPO toPO(Contract contract);
    
    default Long map(ContractAmount value) {
        return value != null ? value.getValue() : null;
    }
    
    default Contract toDomain(ContractPO po) {
        if (po == null) {
            return null;
        }
        
        return Contract.builder()
                .contractId(ContractId.form(po.getTicketId()))
                .contractName(po.getTicketName())
                .contractNumber(po.getContractNumber())
                .contractType(ContractType.fromValue(po.getContractType()))
                .contractAmount(new ContractAmount(po.getContractAmount()))
                .contractStatus(ContractStatus.fromValue(po.getContractStatus()))
                .crmCustomerId(po.getCrmCustomerId())
                .contractDocumentId(po.getContractDocumentId())
                .signedDocumentId(po.getSignedDocumentId())
                .contractRemark(po.getTicketRemark())
                .expectedCompletionTime(po.getExpectedCompletionTime())
                .expiryTime(po.getExpiryTime())
                .isNotify(po.getIsNotify())
                .preOverdueDays(po.getPreOverdueDays())
                .postOverdueDays(po.getPostOverdueDays())
                .creatorName(po.getCreatorName())
                .creatorId(po.getCreatorId())
                .updateId(po.getUpdateId())
                .updateName(po.getUpdateName())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .currentAssignee(po.getCurrentAssignee())
                .build();
    }
}
