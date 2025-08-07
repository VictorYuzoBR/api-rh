package com.rh.api_rh.DTO;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_model;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import lombok.Data;

import java.util.List;

@Data
public class cadastroCandidatoMapeado_dto {

    private candidato_model candidato;

    private List<idioma_model_apenas_formulario> idiomas;

    private List<habilidade_model_apenas_formulario> habilidades;

    private List<experiencia_model> experiencias;

    private List<formacaoAcademica_model>  formacaoAcademica;

}
