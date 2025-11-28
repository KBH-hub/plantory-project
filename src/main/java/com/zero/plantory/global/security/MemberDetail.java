package com.zero.plantory.global.security;

import com.zero.plantory.domain.myProfile.dto.MemberResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberDetail implements UserDetails {

    private final MemberResponse memberResponse;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + memberResponse.getRole().name()));
    }


    @Override
    public String getPassword() {
        return memberResponse.getPassword();
    }

    @Override
    public String getUsername() {
        return memberResponse.getMembername();
    }

}
