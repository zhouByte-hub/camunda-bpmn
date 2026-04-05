package com.zhoubyte.procure_flow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhoubyte.procure_flow.infra.persistence.dao")
public class ProcureFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcureFlowApplication.class, args);
    }

}
