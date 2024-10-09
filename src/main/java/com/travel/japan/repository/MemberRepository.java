package com.travel.japan.repository;

import com.travel.japan.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);


    // 로그인된 사용자를 제외한 나머지 사용자 조회
    @Query("SELECT m FROM Member m WHERE m.email != :email")
    List<Member> findAllExceptEmail(@Param("email") String email);
}
