package com.ddol.jwtd9.member.controller;

import com.ddol.jwtd9.common.ResponseDTO;
import com.ddol.jwtd9.member.model.dto.MemberDTO;
import com.ddol.jwtd9.member.model.service.AuthService;
import com.ddol.jwtd9.member.model.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> selectTargetMemberInfo(@PathVariable String memberId) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", memberService.selectTargetInfo(memberId)));
    }
}
