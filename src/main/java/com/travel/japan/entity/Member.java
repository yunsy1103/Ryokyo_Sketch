package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Data
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "nickname",nullable = true)
    private String nickname;

    @Column(name = "gender",nullable = true)
    private String gender;

    @Column(name = "birthday",nullable = true)
    private LocalDate birth;

    @Column(name = "nationality",nullable = true)
    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    // 상태 필드 추가
    @Builder.Default
    @Column(name = "status",nullable = false)
    private String status = "active";  // 기본값을 'active'로 설정

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}