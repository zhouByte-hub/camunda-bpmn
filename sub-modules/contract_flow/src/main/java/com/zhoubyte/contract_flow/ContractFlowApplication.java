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
