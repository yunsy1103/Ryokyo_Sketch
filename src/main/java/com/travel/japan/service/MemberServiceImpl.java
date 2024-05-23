package com.travel.japan.service;

import com.travel.japan.dto.MemberSignInDto;
import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.entity.Member;
import com.travel.japan.jwt.JwtUtil;
import com.travel.japan.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiredMs}")
    private Long expiredMs;

    @Override
    @Transactional
    public Long signup(MemberSignUpDto memberSignUptDto) throws Exception {
        if (memberRepository.findByEmail(memberSignUptDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (!memberSignUptDto.getPassword().equals(memberSignUptDto.getCheckedPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }


        Member member = memberRepository.save(memberSignUptDto.toEntity(passwordEncoder));
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

        return member.getId();

    }
    public String signIn(MemberSignInDto requestDto){

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        return JwtUtil.createJwt(member.getNickname(), secretKey, expiredMs);
    }
}
