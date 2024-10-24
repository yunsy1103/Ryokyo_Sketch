package com.travel.japan.repository;

import com.travel.japan.entity.FilteredMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//지정 위치에서 특정 거리내 있는 사용자를 찾음
public interface LocationRepository extends JpaRepository<FilteredMember, Long> {

    //Haversine 공식을 이용한 거리 계산을 SQL 쿼리
    @Query(value = "SELECT ul FROM Member ul WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(ul.latitude)) * " +
            "cos(radians(ul.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(ul.latitude)))) < :radius " )
    List<FilteredMember> findAllWithinRadius(@Param("latitude") double latitude,
                                              @Param("longitude") double longitude,
                                              @Param("radius") double radius);

}
