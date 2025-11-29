package com.zero.plantory.global.security;

import com.zero.plantory.domain.myProfile.dto.MemberResponse;
import com.zero.plantory.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberResponse memberResponse = memberMapper.selectByMembername(username);

        if(memberResponse == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username);
        }

        return new MemberDetail(memberResponse);
    }

}
