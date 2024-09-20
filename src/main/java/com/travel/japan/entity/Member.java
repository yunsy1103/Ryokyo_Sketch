package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 255)
    private String nickname;

    @Column(length = 10)
    private String gender;

    @Column(length = 50)
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Nationality nationality;

    // 상태 필드 추가
    @Column(length = 255, nullable = false)
    private String status = "active";  // 기본값을 'active'로 설정

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}