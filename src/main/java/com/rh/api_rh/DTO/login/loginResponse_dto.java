package com.rh.api_rh.DTO.login;

import lombok.Data;

@Data
public class loginResponse_dto {

    private String access_token;
    private String refresh_token;
    private String role;
    private String email;
    private String termo;
    private String primeiro_login;

}
