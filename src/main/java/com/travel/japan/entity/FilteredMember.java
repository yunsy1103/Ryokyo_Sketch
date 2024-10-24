package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "filteredmember")
@Entity
public class FilteredMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    // 기본 생성자
    public FilteredMember() {}

    // id를 제외한 생성자
    public FilteredMember(String email, String nickname, String gender, LocalDate birth, Nationality nationality) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.nationality = nationality;
    }

}