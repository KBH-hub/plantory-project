package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.global.vo.MemberVO;
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
    public boolean signUp(MemberVO memberVo) {

        if (memberMapper.countByMembername(memberVo.getMembername()) > 0) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }
        if (memberMapper.countByNickname(memberVo.getNickname()) > 0) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }
        memberVo.setPassword(bCryptPasswordEncoder.encode(memberVo.getPassword()));
        return memberMapper.insertMember(memberVo) > 0;


    }

    @Override
    public MemberVO login(String membername, String password) {
        MemberVO memberVO = memberMapper.selectByMembername(membername);

        if (memberVO == null) {
            return null;
        }

        if (memberVO.getStopDay() != null) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime stopDay = memberVO.getStopDay();

            if (stopDay.isAfter(now)) {
                long days = ChronoUnit.DAYS.between(now, stopDay); //Chrono시간 + Unit 단위
                throw new IllegalStateException(String.format("정지 해제까지 %d일 남았습니다.", days));
            }
        }

        if (!bCryptPasswordEncoder.matches(password, memberVO.getPassword())) {
            return null;
        }

        return memberVO;
    }


}
