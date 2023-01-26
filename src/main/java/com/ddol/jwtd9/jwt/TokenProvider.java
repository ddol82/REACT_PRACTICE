package com.ddol.jwtd9.jwt;

import com.ddol.jwtd9.exception.TokenException;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import com.ddol.jwtd9.member.model.dto.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 20;    //20분
    private final UserDetailsService userDetailsService;
    private final Key key;

    //value : application.yml에 정의된 jwt.security 값
    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        byte[] ketBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(ketBytes);
    }

    //유저의 권한정보를 이용한 토큰 생성
    public TokenDTO generateTokenDTO(MemberDTO member) {
        //권한 가져오기
        List<String> roles = Collections.singletonList(member.getMemberRole());

        //유저 권한정보 담기
        Claims claims = Jwts
                .claims()
                .setSubject(member.getMemberId());    //토큰 제목
        claims.put(AUTHORITIES_KEY, roles);

        //Access Token 생성
        Date tokenExpiresIn = new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                //payload "auth" : "ROLE_USER", aud : Audience, 토큰 대상자
                .setExpiration(tokenExpiresIn)
                //payload "exp" : ~~~, exp : Expiration Time, 토큰 만료시간
                .signWith(key, SignatureAlgorithm.HS512)
                //header "alg" : "HS512", alg : 서명 시 사용하는 알고리즘
                .compact();
        return new TokenDTO(BEARER_TYPE, member.getUsername(), accessToken, tokenExpiresIn.getTime());
    }

    public String getMemberId(String accessToken) {
        return getJwtsBodyByToken(accessToken).getSubject();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        //JWT 토큰을 복호화하여 토큰에 들어있는 정보 꺼냄

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //Claims에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어 Authentication return
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberId(accessToken));

        //UserDeatils 객체를 재생성하여 UsernamePasswordAuthenticationToken 형태로 리턴
        //SecurityContext를 사용하기 위한 절차로 이 곳에 Authentication 객체를 저장하고 있기 때문
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    private Claims parseClaims(String accessToken) {
        try {
            return getJwtsBodyByToken(accessToken);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
            //만료 시에도 정보를 throw
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new TokenException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new TokenException("잘못된 JWT 토큰입니다.");
        }
    }

    private Claims getJwtsBodyByToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();  //권한 정보 담긴 Body
    }
}
