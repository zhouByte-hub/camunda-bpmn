package com.zhoubyte.contract_flow.facade.converter;

import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.facade.dto.request.ContractCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractFacadeConverter {
    
    @Mapping(target = "creatorId", source = "userId")
    @Mapping(target = "creatorName", source = "userName")
    ContractCreateParam toCreateParam(ContractCreateRequest request, String userId, String userName);
}
