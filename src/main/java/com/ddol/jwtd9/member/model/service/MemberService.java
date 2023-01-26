package com.ddol.jwtd9.member.model.service;

import com.ddol.jwtd9.member.model.dao.MemberMapper;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MemberService {
    private final MemberMapper memberMapper;


    public MemberDTO selectTargetInfo(String memberId) {
        return memberMapper.findMemberById(memberId);
    }
}
