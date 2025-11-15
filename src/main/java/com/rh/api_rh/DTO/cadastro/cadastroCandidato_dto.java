package com.rh.api_rh.DTO.cadastro;

import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class cadastroCandidato_dto {

    private String password;

    private String nome;

    private String email;

    private String telefone;

    private String linkedin;

    private LocalDate datanascimento;

    private String github;

    private String cidade;

    private String estado;

    private List<habilidade_model_apenas_formulario>  habilidades;

    private List<formacaoAcademica_model>  formacaoAcademica;

    private List<experiencia_model>  experiencias;

    private List<idioma_model_apenas_formulario> idiomas;

}
