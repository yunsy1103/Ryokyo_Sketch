package com.travel.japan.dto;

import com.travel.japan.entity.Nationality;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class MemberProfileUpdateDto {

    @Size(min=2, message = "닉네임이 너무 짧습니다.")

    private String nickname;       // null 허용

    private String gender;         // null 허용

    private LocalDate birth;       // null 허용

    private Nationality nationality; // null 허용

    public MemberProfileUpdateDto() {
    }
}
