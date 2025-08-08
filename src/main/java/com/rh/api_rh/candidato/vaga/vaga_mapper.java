package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.cadastrarVagaMapeado_dto;
import com.rh.api_rh.DTO.cadastrarVaga_dto;
import org.springframework.stereotype.Service;

@Service
public class vaga_mapper {

    public cadastrarVagaMapeado_dto convert(cadastrarVaga_dto dto) {

        cadastrarVagaMapeado_dto res =  new cadastrarVagaMapeado_dto();
        vaga_model vaga = new vaga_model();

        try {
            res.setHabilidades(dto.getHabilidades());

            vaga.setPalavrasChave(dto.getPalavrasChave());
            vaga.setInformacoes(dto.getInformacoes());
            vaga.setDescricao(dto.getDescricao());
            vaga.setLocalizacao(dto.getLocalizacao());
            vaga.setModelo(dto.getModelo());
            vaga.setTitulo(dto.getTitulo());
            vaga.setTipoContrato(dto.getTipoContrato());

            res.setVaga(vaga);

        }  catch (Exception e){
            return null;
        }
        return res;
    }

}
