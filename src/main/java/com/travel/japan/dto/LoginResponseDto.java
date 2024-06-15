package com.travel.japan.dto;

import com.travel.japan.jwt.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginResponseDto {
    private String message;
    private TokenInfo tokenInfo;

}