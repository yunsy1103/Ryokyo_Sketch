package com.travel.japan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResponseDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;
    @Size(max = 255, message = "Content cannot exceed 255 characters")
    private String content;

    private Integer viewCnt;
    // Getters and setters

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Integer getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(Integer viewCnt) {
        this.viewCnt = viewCnt;
    }
}
