package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.cadastroCandidatoMapeado_dto;
import com.rh.api_rh.DTO.cadastroCandidato_dto;
import org.springframework.stereotype.Service;

@Service
public class cadastroCandidatoMapper {

    public cadastroCandidatoMapeado_dto convert(cadastroCandidato_dto dto) {

        cadastroCandidatoMapeado_dto res =  new cadastroCandidatoMapeado_dto();

        candidato_model candidato = new candidato_model();

        candidato.setNome(dto.getNome());
        candidato.setEmail(dto.getEmail());
        candidato.setTelefone(dto.getTelefone());
        candidato.setCidade(dto.getCidade());
        candidato.setEstado(dto.getEstado());
        candidato.setGithub(dto.getGithub());
        candidato.setLinkedin(dto.getLinkedin());
        candidato.setPassword(dto.getPassword());

        res.setCandidato(candidato);
        res.setExperiencias(dto.getExperiencias());
        res.setHabilidades(dto.getHabilidades());
        res.setFormacaoAcademica(dto.getFormacaoAcademica());
        res.setIdiomas(dto.getIdiomas());

        return res;

    }

}
