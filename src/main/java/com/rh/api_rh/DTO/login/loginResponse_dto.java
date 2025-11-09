package com.rh.api_rh.DTO.login;

import lombok.Data;

import java.util.UUID;

@Data
public class loginResponse_dto {

    private String access_token;
    private String refresh_token;
    private String role;
    private UUID idfuncionario;
    private String termo;
    private String primeiro_login;

}
