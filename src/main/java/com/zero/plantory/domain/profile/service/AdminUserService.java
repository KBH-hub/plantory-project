package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final SessionRegistry sessionRegistry;

    public void forceLogout(String username) {

        List<Object> principals = sessionRegistry.getAllPrincipals();

        for (Object principal : principals) {
            if (principal instanceof MemberDetail userDetail) {

                if (userDetail.getUsername().equals(username)) {

                    List<SessionInformation> sessions =
                            sessionRegistry.getAllSessions(principal, false);

                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}
