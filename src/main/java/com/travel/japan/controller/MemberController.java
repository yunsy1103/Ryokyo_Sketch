package com.travel.japan.controller;

import com.travel.japan.dto.LoginResponseDto;
import com.travel.japan.dto.MemberSignInDto;
import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.jwt.TokenInfo;
import com.travel.japan.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")  // 모든 출처 허용
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 등록", description = "회원 가입")
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

   @Operation(summary = "회원 로그인", description = "로그인")
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody MemberSignInDto request) {
       try {
           TokenInfo tokenInfo = memberService.signIn(request);
           LoginResponseDto response = LoginResponseDto.builder().message("로그인 성공").tokenInfo(tokenInfo).build();
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
       }
   }

}