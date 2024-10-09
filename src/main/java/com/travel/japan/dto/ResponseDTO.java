package com.travel.japan.dto;

import com.travel.japan.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ResponseDTO {

    private String nickname;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 255, message = "Content cannot exceed 255 characters")
    private String content;

    private List<String> imageUrls; // 이미지 URL 리스트

    @Builder
    public ResponseDTO( String nickname,String title, String content,List<String> imageUrls) {
        this.nickname =nickname;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }
}

