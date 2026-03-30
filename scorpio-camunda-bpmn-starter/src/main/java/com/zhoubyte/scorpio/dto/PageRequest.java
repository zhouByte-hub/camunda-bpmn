package com.zhoubyte.scorpio.dto;

public class PageRequest {

    private Integer from;
    private Integer limit;
    private String cursorBefore;
    private String cursorAfter;

    public PageRequest() {
    }

    public PageRequest(Integer from, Integer limit, String cursorBefore, String cursorAfter) {
        this.from = from;
        this.limit = limit;
        this.cursorBefore = cursorBefore;
        this.cursorAfter = cursorAfter;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getCursorBefore() {
        return cursorBefore;
    }

    public void setCursorBefore(String cursorBefore) {
        this.cursorBefore = cursorBefore;
    }

    public String getCursorAfter() {
        return cursorAfter;
    }

    public void setCursorAfter(String cursorAfter) {
        this.cursorAfter = cursorAfter;
    }
}
