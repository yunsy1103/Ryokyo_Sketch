package com.travel.japan.dto;

import com.travel.japan.entity.Member;
import com.travel.japan.entity.Nationality;
import com.travel.japan.jwt.TokenInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class MemberSignUpDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String checkedPassword;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "닉네임이 너무 짧습니다.")
    private String nickname;

    @NotNull(message = "성별을 입력해주세요")
    private String gender;

    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birth;

    @NotNull(message = "국적을 입력해주세요")
    private Nationality nationality;



    @Builder
    public Member toEntity(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .birth(birth)
                .password(password)
                .nationality(nationality)
                .build();
    }
}