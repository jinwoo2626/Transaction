package com.example.DBridge2.config.dto;

import lombok.*;
import org.springframework.security.core.Authentication;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    private String token;
    private String authentication;
    private Long userid;
    private String nickname;

}
