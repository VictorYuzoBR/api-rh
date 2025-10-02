package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.feriasPorSetor_dto;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                List<ferias_model> solicitacoes = new ArrayList();
                String setorNome = setor.getNome();
                for (ferias_model ferias : feriasDoMes) {

                    if (ferias.getSetorfuncionario().equals(setorNome)) {
                        solicitacoes.add(ferias);

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
                item.setSolicitacoes(solicitacoes);
                res.add(item);

            }

        }

        return res;

    }

    public List<ferias_model> feriasConflitantes(Long idferias) {
        List<ferias_model> res = new ArrayList<>();


        try {
            Optional<ferias_model> feriasData = ferias_repository.findById(idferias);
            if (feriasData.isPresent()) {

                ferias_model ferias = feriasData.get();

                String setorferias = ferias.getSetorfuncionario();
                int mesinicio = ferias.getDataInicio().getMonthValue();
                int mesfim = ferias.getDataFim().getMonthValue();

                List<LocalDate> diasDeFerias = ferias.getDataInicio().datesUntil(ferias.getDataFim().plusDays(1))
                        .toList();


                List<ferias_model> todas = new ArrayList();
                List<ferias_model> todasAprovadas = ferias_repository.findBySetorfuncionarioAndStatus(setorferias, "aprovado");
                List<ferias_model> todasAndamento = ferias_repository.findBySetorfuncionarioAndStatus(setorferias, "andamento");
                todas.addAll(todasAndamento);
                todas.addAll(todasAprovadas);

                List<ferias_model> feriasDoMes = new ArrayList();

                for (ferias_model aux : todas) {

                    int inicio = aux.getDataInicio().getMonthValue();
                    int fim = aux.getDataFim().getMonthValue();

                    if (inicio == mesinicio || fim == mesfim || inicio == mesfim || fim == mesinicio) {
                        feriasDoMes.add(aux);
                    }

                }

                for (ferias_model aux2 : feriasDoMes) {

                    List<LocalDate> diasDeFeriasAux = aux2.getDataInicio().datesUntil(aux2.getDataFim().plusDays(1)).toList();


                    for (LocalDate diasAux : diasDeFeriasAux) {

                        if (diasDeFerias.contains(diasAux)) {
                            res.add(aux2);
                            break;
                        }

                    }


                }

                return res;

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    @Scheduled(fixedRate = 200000)
    public void iniciarFerias() {

        LocalDate hoje = LocalDate.now();

        List<ferias_model> todasFeriasAprovadas = ferias_repository.findByStatus("aprovado");
        if (!todasFeriasAprovadas.isEmpty()) {

            for  (ferias_model aux : todasFeriasAprovadas) {

                if (aux.getDataInicio().equals(hoje)) {

                    aux.setStatus("andamento");
                    ferias_repository.save(aux);

                }

            }

        }


    }

    @Scheduled(fixedRate = 200000)
    public void finalizarFerias() {

        LocalDate hoje = LocalDate.now();

        List<ferias_model> todasFeriasAprovadas = ferias_repository.findByStatus("andamento");
        if (!todasFeriasAprovadas.isEmpty()) {

            for  (ferias_model aux : todasFeriasAprovadas) {

                if (aux.getDataFim().equals(hoje.minusDays(1))) {

                    aux.setStatus("finalizado");
                    ferias_repository.save(aux);

                }

            }

        }


    }

}
