package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.dto.MemberSignUpRequest;
import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.global.dto.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean isDuplicateMembername(String membername) {
        return memberMapper.countByMembername(membername) > 0;
    }

    @Override
    public boolean isDuplicateNickname(String nickname) {
        return memberMapper.countByNickname(nickname) > 0;
    }

    @Override
    @Transactional
    public void signUp(MemberSignUpRequest request) {

        log.info(String.valueOf(request));

        if (memberMapper.countByMembername(request.getMembername()) > 0) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }
        if (memberMapper.countByNickname(request.getNickname()) > 0) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        log.info(String.valueOf(request));

        MemberResponse memberResponse = MemberResponse.builder()
                .membername(request.getMembername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .address(request.getAddress())
                .noticeEnabled(1)
                .sharingRate(7)
                .role(Role.USER)
                .build();

                 memberMapper.insertMember(memberResponse);
    }

//    @Override
//    public MemberResponse login(String membername, String password) {
//        MemberResponse memberResponse = memberMapper.selectByMembername(membername);
//
//        if (memberResponse == null) {
//            return null;
//        }
//
//        if (memberResponse.getStopDay() != null) {
//
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime stopDay = memberResponse.getStopDay();
//
//            if (stopDay.isAfter(now)) {
//                long days = ChronoUnit.DAYS.between(now, stopDay); //Chrono시간 + Unit 단위
//                throw new IllegalStateException(String.format("정지 해제까지 %d일 남았습니다.", days));
//            } else {
//                memberMapper.resetStopDay(memberResponse.getMemberId());
//                memberResponse.setStopDay(null);
//            }
//        }
//
//        if (!bCryptPasswordEncoder.matches(password, memberResponse.getPassword())) {
//            return null;
//        }
//
//        return memberResponse;
//    }


}
