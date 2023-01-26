package com.ddol.jwtd9.member.model.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberDTO implements UserDetails {
    private String memberCode;
    private String memberId;
    private String memberPwd;
    private String memberNickname;
    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private String memberBirth;
    private String memberGender;
    private Date enrollDate;
    private String memberStatus;
    private Date banDate;
    private String memberRole;

    //UserDetails 구현체
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.memberPwd;
    }

    @Override
    public String getUsername() {
        return this.memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "memberCode='" + memberCode + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", memberNickname='" + memberNickname + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", memberBirth='" + memberBirth + '\'' +
                ", memberGender='" + memberGender + '\'' +
                ", enrollDate=" + enrollDate +
                ", memberStatus='" + memberStatus + '\'' +
                ", banDate=" + banDate +
                ", memberRole='" + memberRole + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
