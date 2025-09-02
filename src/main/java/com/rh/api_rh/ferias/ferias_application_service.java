package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.feriasPorSetor_dto;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ferias_application_service {

    @Autowired
    private ferias_repository ferias_repository;

    @Autowired
    private setor_service setor_service;

    public List<feriasPorSetor_dto> feriasPorSetor(int mes) {

        List<feriasPorSetor_dto> res = new ArrayList<>();
        List<setor_model> setores = setor_service.listar();

        if (setores.isEmpty()) {
            return res;
        }

        List<ferias_model> todasFeriasAndamento = ferias_repository.findByStatus("andamento");
        List<ferias_model> todasFeriasAprovadas = ferias_repository.findByStatus("aprovado");


        List<ferias_model> todasFerias = new ArrayList<>();
        todasFerias.addAll(todasFeriasAndamento);
        todasFerias.addAll(todasFeriasAprovadas);

        List<ferias_model> feriasDoMes = new ArrayList();

        if (todasFerias.isEmpty()) {
            return res;
        } else {

            for (ferias_model ferias : todasFerias) {

                int inicio = ferias.getDataInicio().getMonthValue();
                int fim = ferias.getDataFim().getMonthValue();

                if (inicio == mes || fim == mes) {
                    feriasDoMes.add(ferias);
                }

            }

        }

        if (feriasDoMes.isEmpty()) {
            return res;
        } else {

            for (setor_model setor : setores) {
                int contagemAndamento = 0;
                int contagemAprovado = 0;
                String setorNome = setor.getNome();
                for (ferias_model ferias : feriasDoMes) {

                    if (ferias.getSetorfuncionario().equals(setorNome)) {

                        if (ferias.getStatus().equals("aprovado")) {
                            contagemAprovado = contagemAprovado + 1;
                        } else {
                            contagemAndamento = contagemAndamento + 1;
                        }

                    }

                }

                feriasPorSetor_dto item =  new feriasPorSetor_dto();
                item.setSetor(setorNome);
                item.setQuantidadeAprovadas(contagemAprovado);
                item.setQuantidadeAndamento(contagemAndamento);
                res.add(item);

            }

        }

        return res;

    }

}
