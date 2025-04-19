package com.rh.api_rh.DTO;

import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.usuario.usuarioprovisorio;
import lombok.Data;

@Data
public class emailnotificarcadastro_dto {

    private funcionario_model funcionario;

    private usuarioprovisorio provisorio;

}
