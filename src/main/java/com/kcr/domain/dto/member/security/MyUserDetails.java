package com.kcr.domain.dto.member.security;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {

    private final Member member; // 1

    public Member getMember() {
        return member;
    } // 2

    @Override
    public String getPassword() {
        return member.getLoginPw();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    // 사용자의 권한을 GrantedAuthority 로 추상화 및 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 3
        RoleType roleType = member.getRoleType();
        String authority = roleType.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
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
