package com.MyNote.MyNote.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {
    private String Id;
    private String accessToken;
    private String refreshToken;
}