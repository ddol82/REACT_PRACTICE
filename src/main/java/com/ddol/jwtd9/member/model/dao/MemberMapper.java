package com.ddol.jwtd9.member.model.dao;

import com.ddol.jwtd9.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    MemberDTO findMemberById(String memberId);

    String checkByEmail(String memberEmail);

    String checkById(String memberId);

    String checkByNickname(String memberNickname);

    int insertMember(MemberDTO member);

    Optional<MemberDTO> findUserById(String memberId);
}
