package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.application.service.ContractCreateService;
import com.zhoubyte.contract_flow.facade.converter.ContractFacadeConverter;
import com.zhoubyte.contract_flow.facade.dto.request.ContractCreateRequest;
import com.zhoubyte.contract_flow.facade.dto.request.ContractSearchRequest;
import com.zhoubyte.contract_flow.facade.dto.response.ContractSearchResponse;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contract")
@RequiredArgsConstructor
public class ContractFlowController {
    
    private final ContractCreateService contractCreateService;
    private final ContractFacadeConverter contractFacadeConverter;
    
    @PostMapping("/create")
    public ResponseEntity<String> createContract(
            @RequestBody @Validated ContractCreateRequest request,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Name") String userName) {
        
        ContractCreateParam param = contractFacadeConverter.toCreateParam(request, userId, userName);
        ContractId contractId = contractCreateService.createContract(param);
        
        return ResponseEntity.ok(contractId.getValue());
    }
    
    @PostMapping("/list")
    public ResponseEntity<ContractSearchResponse> contractList(
            @RequestBody ContractSearchRequest request) {
        
        ContractSearchResponse response = contractCreateService.contractList(request);
        return ResponseEntity.ok(response);
    }
}
