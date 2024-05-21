package com.travel.japan.service;

import com.travel.japan.dto.MemberSignUpDto;
import com.travel.japan.entity.Member;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    public Long signup(MemberSignUpDto memberSignUpDto) throws Exception;
   // public String register(MemberSignUpDto memberSignUptDto) throws Exception;
}


