package com.zero.plantory.global.security;

import com.zero.plantory.global.vo.MemberVO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberDetail implements UserDetails {

    private final MemberVO memberVO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + memberVO.getRole().name()));
    }


    @Override
    public String getPassword() {
        return memberVO.getPassword();
    }

    @Override
    public String getUsername() {
        return memberVO.getMembername();
    }

}
