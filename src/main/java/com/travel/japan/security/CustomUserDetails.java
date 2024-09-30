package com.travel.japan.security;

import com.travel.japan.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private Long id;        // 사용자 ID
    private String email;   // 이메일
    private String password; // 비밀번호
    private Collection<? extends GrantedAuthority> authorities;




    public CustomUserDetails(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.authorities = Collections.emptyList(); // 권한 설정 (필요에 따라)
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한을 설정하는 로직이 필요하다면 여기에 추가
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // 이메일을 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}