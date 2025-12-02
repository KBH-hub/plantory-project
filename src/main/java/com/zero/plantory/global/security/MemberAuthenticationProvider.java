package com.zero.plantory.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        MemberDetail user = (MemberDetail) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        LocalDateTime stopDay = user.getMemberResponse().getStopDay();

        if (stopDay != null) {

            LocalDateTime now = LocalDateTime.now();

            if (stopDay.isAfter(now)) {
                long days = ChronoUnit.DAYS.between(now, stopDay);
                throw new LockedException("정지 해제까지 " + days + "일 남았습니다.");
            }

            if (stopDay.isBefore(now)) {
                userDetailsService.resetStopDay(user.getMemberResponse().getMemberId());
                user.getMemberResponse().setStopDay(null);
            }
        }


        return new UsernamePasswordAuthenticationToken(
                user,
                password,
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

