package com.ddol.jwtd9.member.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TokenDTO {
    private String grantType;
    private String memberName;
    private String accessToken;
    private Long tokenExpiresIn;
}
