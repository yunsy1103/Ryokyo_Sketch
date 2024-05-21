package com.travel.japan.service;

import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.entity.Member;
import com.travel.japan.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
