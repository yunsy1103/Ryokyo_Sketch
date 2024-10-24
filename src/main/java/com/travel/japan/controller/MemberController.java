package com.travel.japan.controller;

import com.travel.japan.dto.LoginResponseDto;
import com.travel.japan.dto.MemberProfileUpdateDto;
import com.travel.japan.dto.MemberSignInDto;
import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.entity.Member;
import com.travel.japan.jwt.TokenInfo;
import com.travel.japan.repository.MemberRepository;
import com.travel.japan.security.CustomUserDetails;
import com.travel.japan.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @Operation(summary = "회원 등록", description = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<String> join(@Validated @RequestBody MemberSignUpDto register) {
        try {
            Long memberId = memberService.signup(register);
            return ResponseEntity.ok(memberId.toString()); // 상태 코드 200과 함께 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다: " + e.getMessage()); // 예외 발생 시 상태 코드 500과 메시지 반환
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

    @Operation(summary = "회원 페이지", description = "마이페이지")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody MemberProfileUpdateDto profileDto) {
        // 현재 인증된 사용자의 이메일을 SecurityContext에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 이메일을 가져옴

        // 서비스 계층으로 이메일과 프로필 정보를 전달하여 업데이트 수행
        memberService.updateProfile(currentEmail, profileDto);

        return ResponseEntity.ok("프로필 업데이트 성공");
    }
}
