package com.ddol.jwtd9.member.controller;

import com.ddol.jwtd9.common.ResponseDTO;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import com.ddol.jwtd9.member.model.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/regist")
    public ResponseEntity<ResponseDTO> insertMember(@RequestBody MemberDTO member) {
        System.out.println("다음 대상에 대한 회원가입을 시도합니다 : " + member);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "등록 성공", authService.signup(member)));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody MemberDTO member) {
        System.out.println("다음 대상에 대한 로그인을 시도합니다 : " + member);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "로그인 성공", authService.login(member)));
    }
}
