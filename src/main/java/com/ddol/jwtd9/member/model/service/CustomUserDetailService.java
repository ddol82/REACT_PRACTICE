package com.ddol.jwtd9.member.model.service;

import com.ddol.jwtd9.exception.UserException;
import com.ddol.jwtd9.member.model.dao.MemberMapper;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberMapper mapper;

    public CustomUserDetailService(MemberMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return mapper.findUserById(memberId)
                .map(user -> addAuthorities(user))
                .orElseThrow(() -> new UserException(memberId + "를 찾을 수 없습니다."));
    }
    private MemberDTO addAuthorities(MemberDTO member) {
        member.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(member.getMemberRole())));
        return member;
    }
}
