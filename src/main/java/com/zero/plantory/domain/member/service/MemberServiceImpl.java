package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Override
    public boolean isDuplicateMembername(String membername) {
        return memberMapper.countByMembername(membername) > 0;
    }

    @Override
    public boolean isDuplicateNickname(String nickname) {
        return memberMapper.countByNickname(nickname) > 0;
    }

    @Override
    public boolean signUpMember(MemberVO memberVO) {
        return memberMapper.insertMember(memberVO) > 0;
    }

    @Override
    public MemberVO login(String membername, String password) {
        MemberVO memberVo = memberMapper.selectByMembername(membername);

        if (memberVo == null) {
            return null;
        }

        if (memberVo.getStopDay() != null) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime stopDay = memberVo.getStopDay();

            if (stopDay.isAfter(now)) {
                long days = ChronoUnit.DAYS.between(now, stopDay); //Chrono시간 + Unit 단위
                throw new IllegalStateException(String.format("정지 해제까지 %d일 %d시간 남았습니다.", days));
            }
        }

//        if (!passwordEncoder.matches(password, member.getPassword())) {
//            return null;
//        }

        return memberVo;
    }


}
