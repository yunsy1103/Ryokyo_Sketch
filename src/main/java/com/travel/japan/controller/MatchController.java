package com.travel.japan.controller;

import com.travel.japan.entity.FilteredMember;
import com.travel.japan.entity.Nationality;
import com.travel.japan.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
//사용자 매칭(국적,위치)하여 채팅을 시작
public class MatchController {

    private final LocationService locationService;

    @Autowired
    public MatchController(LocationService locationService) {
        this.locationService = locationService;
    }

    // 로그인된 사용자의 이메일을 기반으로 반경 5km 내의 다른 사용자 조회
    @GetMapping("/nearby")
    public List<FilteredMember> getNearbyUsers(@RequestParam String email) {
        double radius = 5.0;  // 반경 5km
        return locationService.getNearbyUsers(email, radius);
    }

    // 국적 반대인 사용자 조회
    public Nationality getOppositeNationality(Nationality nationality) {
        if (nationality == Nationality.KOREAN) {
            return Nationality.JAPANESE;
        } else if (nationality == Nationality.JAPANESE) {
            return Nationality.KOREAN;
        } else {
            //다른 국적일 경우 매칭 제외
            return null;
        }
    }
}