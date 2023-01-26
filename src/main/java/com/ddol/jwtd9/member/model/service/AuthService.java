package com.ddol.jwtd9.member.model.service;

import com.ddol.jwtd9.exception.LoginDeniedException;
import com.ddol.jwtd9.exception.UserException;
import com.ddol.jwtd9.jwt.TokenProvider;
import com.ddol.jwtd9.member.model.dao.MemberMapper;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import com.ddol.jwtd9.member.model.dto.TokenDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(MemberMapper memberMapper, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public MemberDTO signup(MemberDTO member) {
        if(memberMapper.checkById(member.getMemberId()) != null) {
            throw new UserException("이미 사용중인 아이디입니다.");
        }
        if(memberMapper.checkByNickname(member.getMemberNickname()) != null) {
            throw new UserException("이미 사용중인 별명입니다.");
        }
        if(memberMapper.checkByEmail(member.getMemberEmail()) != null) {
            throw new UserException("이미 사용중인 이메일입니다.");
        }

        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));

        int result = memberMapper.insertMember(member);
        if(result > 0) {
            return member;
        } else {
            throw new UserException("회원가입이 진행되지 않았습니다.");
        }
    }

    @Transactional
    public TokenDTO login(MemberDTO memberInput) {
        String inputId = memberInput.getMemberId();
        String inputPwd = memberInput.getMemberPwd();

        System.out.println("다음 로그인 대상에 대한 정보 1 : " + inputId);

        //아이디 조회
        MemberDTO member = memberMapper.findUserById(inputId)
                .orElseThrow(() -> new LoginDeniedException("해당 아이디는 존재하지 않습니다."));

        System.out.println("다음 로그인 대상에 대한 정보 2 : " + inputPwd + ", " + member);

        //비밀번호 매칭
        if(!passwordEncoder.matches(inputPwd, member.getMemberPwd())) {
            throw new LoginDeniedException("비밀번호가 일치하지 않습니다.");
        }

        //토큰 발급
        TokenDTO token = tokenProvider.generateTokenDTO(member);
        return token;
    }
}
