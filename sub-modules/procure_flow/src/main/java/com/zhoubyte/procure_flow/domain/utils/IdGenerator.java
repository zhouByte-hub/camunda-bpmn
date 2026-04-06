package com.zhoubyte.procure_flow.domain.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IdGenerator {

    private static Map<String, AtomicInteger> typeMapper;
    private static String currentDate;

    public IdGenerator() {
        typeMapper = new ConcurrentHashMap<>();
    }

    public String generateTicketId() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        AtomicInteger sequence;
        if(!typeMapper.containsKey("TICKET")){
            sequence = new AtomicInteger(0);
            typeMapper.put("TICKET", sequence);
        }else{
            sequence = typeMapper.get("TICKET");
        }

        if(!today.equals(currentDate)){
            currentDate = today;
            sequence.set(0);
            typeMapper.put("TICKET", sequence);
        }
        int seq = sequence.incrementAndGet();
        return "TICKET_" + currentDate + String.format("%05d", seq);
    }


    public String generateTaskId(String taskType){
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        AtomicInteger sequence;
        if(!typeMapper.containsKey("Task")){
            sequence = new AtomicInteger(0);
            typeMapper.put("Task", sequence);
        }else{
            sequence = typeMapper.get("Task");
        }

        if(!today.equals(currentDate)){
            currentDate = today;
            sequence.set(0);
            typeMapper.put("Task", sequence);
        }
        int seq = sequence.incrementAndGet();
        return taskType + currentDate + String.format("%05d", seq);
    }


}
