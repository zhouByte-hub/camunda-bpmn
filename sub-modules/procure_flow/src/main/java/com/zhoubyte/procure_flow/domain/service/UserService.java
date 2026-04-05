package com.zhoubyte.procure_flow.domain.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private static String username;
    private static String id;

    public UserService() {
        username = "zhouByte";
        id = UUID.randomUUID().toString().replaceAll("-", "");
    }


    public String getCurrentUsername(){
        return username;
    }

    public String getCurrentUserId(){
        return id;
    }
}
