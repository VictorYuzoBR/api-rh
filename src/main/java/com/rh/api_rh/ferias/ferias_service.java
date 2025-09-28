package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.atualizarFerias_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarFerias_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ferias_service {


    @Autowired
    private funcionario_service funcionario_service;

    @Autowired
    private ferias_repository ferias_repository;


    public String cadastrar(cadastrarFerias_dto dto) {

        try {


            funcionario_model funcionario = funcionario_service.buscar(dto.getIdfuncionario());

            Optional<ferias_model> jaexiste = ferias_repository.findByFuncionarioAndStatus(funcionario, "andamento");
            Optional<ferias_model> jaexiste2 = ferias_repository.findByFuncionarioAndStatus(funcionario, "solicitado");
            Optional<ferias_model> jaexiste3 = ferias_repository.findByFuncionarioAndStatus(funcionario, "aprovado");
            if (jaexiste.isPresent() || jaexiste2.isPresent() || jaexiste3.isPresent()) {

                return ("funcionario ja possui solicitação aberta ou em andamento");

            } else {

                Long dias = (ChronoUnit.DAYS.between(dto.getDataInicio(), dto.getDataFim()));
                if (dias < 5) {
                    return ("uma fracao de ferias não pode ter menos que 5 dias");
                }

                ferias_model novaferias = new ferias_model();
                novaferias.setFuncionario(funcionario);
                novaferias.setStatus("solicitado");
                novaferias.setDataInicio(dto.getDataInicio());
                novaferias.setDataFim(dto.getDataFim());
                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                ferias_repository.save(novaferias);
                return ("solicitação enviada com sucesso");
            }

        } catch (Exception e) {
            return ("falha ao tentar cadastrar ferias");
        }
    }

    public String atualizar(atualizarFerias_dto dto, UUID idrh) {

        Optional<ferias_model> ferias =  ferias_repository.findById(dto.getIdferias());
        if (ferias.isPresent()) {

            ferias_model f = ferias.get();

            funcionario_model funcionariorh = funcionario_service.buscar(idrh);

            if (dto.getNovoStatus().equals("recusado") && dto.getMotivoRecusa() != null ) {

                f.setMotivoRecusa(dto.getMotivoRecusa());

            }

            f.setAtualizadoPor(funcionariorh);
            f.setStatus(dto.getNovoStatus());
            ferias_repository.save(f);
            return ("ferias atualizado com sucesso");
        } else{
            return ("falha ao tentar atualizar ferias");
        }

    }

    public List<ferias_model> listarTodosFuncionario(UUID funcionario_id) {

        List<ferias_model> lista =  new ArrayList<>();

        funcionario_model funcionario =  funcionario_service.buscar(funcionario_id);

        lista = ferias_repository.findByFuncionario(funcionario);

        return lista;

    }

}
