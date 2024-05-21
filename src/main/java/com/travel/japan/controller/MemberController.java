package com.travel.japan.controller;

import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String join(@Validated @RequestBody MemberSignUpDto register) {
        try {
            Long memberId = memberService.signup(register);
            return memberId.toString();
        } catch (Exception e) {
            // 예외가 발생한 경우, 적절한 메시지와 함께 반환합니다.
            return "회원가입 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

}