package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;

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

    @Column(length = 50)
    private String password;

    @Column(length = 100 )
    private String nickname;

    private String gender;

    @Column(length = 45)
    private String birth;

    @Column(length = 45, unique = true)
    private String email;



    public Long getId() {
        return id;
    }


    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}