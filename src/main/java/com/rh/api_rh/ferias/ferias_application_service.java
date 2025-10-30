package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.feriasPorSetor_dto;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_repository;
import com.rh.api_rh.espelho.espelho_model;
import com.rh.api_rh.espelho.espelho_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ferias_application_service {

    @Autowired
    private ferias_repository ferias_repository;

    @Autowired
    private setor_service setor_service;

    @Autowired
    private funcionario_repository funcionario_repository;

    @Autowired
    private espelho_repository espelho_repository;

    @Autowired
    private espelho_item_repository espelho_item_repository;

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

                    funcionario_model funcionario = aux.getFuncionario();
                    funcionario.setDeFerias(true);
                    funcionario_repository.save(funcionario);

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

                    funcionario_model funcionario = aux.getFuncionario();

                    funcionario.setFeriasDisponiveis(funcionario.getFeriasDisponiveis() - aux.getDiasParaDescontar());
                    funcionario.setFracoesDisponiveis(funcionario.getFracoesDisponiveis() - 1);

                    if (aux.isAlterar14dias()) {
                        funcionario.setPeriodo14dias(true);
                    }

                    funcionario.setDeFerias(false);

                    funcionario_repository.save(funcionario);

                    aux.setStatus("finalizado");
                    ferias_repository.save(aux);

                }

            }

        }

    }

    public String finalizarFeriasTesteLogica(Long id) {

        try {

            Optional<ferias_model> ferias = ferias_repository.findById(id);
            if (ferias.isPresent()) {

                ferias_model f = ferias.get();

                funcionario_model funcionario = f.getFuncionario();

                funcionario.setFeriasDisponiveis(funcionario.getFeriasDisponiveis() - f.getDiasParaDescontar());
                funcionario.setFracoesDisponiveis(funcionario.getFracoesDisponiveis() - 1);

                if (f.isAlterar14dias()) {
                    funcionario.setPeriodo14dias(true);
                }

                funcionario_repository.save(funcionario);

                f.setStatus("finalizado");
                ferias_repository.save(f);

                return ("ok");

            } else {
                return null;
            }


        } catch (Exception e) {
            return null;
        }

    }

    public void calcularFeriasApenasLogica() {

        List<funcionario_model> funcionarios = funcionario_repository.findByStatus("ativo");

        List<espelho_model> listaEspelhos = new ArrayList<>();

        funcionario_model admin = funcionarios.get(0);

        LocalDate datafixa = LocalDate.parse("2024-06-01");

        int numeroFaltas = 0;
        int saldofinal = 30;

        for (int i = 0; i < 13; i++) {

            espelho_model espelho = espelho_repository.findByPeriodoInicioAndRegistro(datafixa.plusMonths(i), admin.getIdusuario().getRegistro()).get();
            listaEspelhos.add(espelho);

        }

        for (espelho_model espelho : listaEspelhos) {

            for (espelho_item_model item : espelho.getListaEntradas()) {

                if (item.isAusencia() && item.getDescricaoAbono() == null) {
                    numeroFaltas++;
                }

            }

        }

        if (numeroFaltas > 32) {
            saldofinal = 0;
        } else if  (numeroFaltas > 23) {
            saldofinal = 12;
        } else if (numeroFaltas > 14) {
            saldofinal = 18;
        } else if (numeroFaltas > 5) {
            saldofinal = 24;
        } else {
            saldofinal = 30;
        }

        admin.setPeriodo14dias(false);
        admin.setFracoesDisponiveis(3);
        admin.setFeriasDisponiveis(saldofinal);
        admin.setUltimoCalculo(LocalDate.parse("2025-06-01"));

        funcionario_repository.save(admin);


    }

    public void gerarEspelhosFalsos() {

        List<funcionario_model> funcionarios = funcionario_repository.findAll();
        funcionario_model admin = funcionarios.get(0);

        LocalDate dataInicial = LocalDate.parse(admin.getDataentrada());

        for (int i = 0; i < 13; i++) {

            LocalDate primeiroDia = dataInicial.plusMonths(i);
            YearMonth anoMes = YearMonth.from(primeiroDia);
            LocalDate ultimoDiaDoMes = anoMes.atEndOfMonth();

            espelho_model espelho = new espelho_model();
            espelho.setNomeFuncionario(admin.getNome());
            espelho.setRegistro(admin.getIdusuario().getRegistro());
            espelho.setFuncao(admin.getFuncao());
            espelho.setPeriodoInicio(primeiroDia);
            espelho.setPeriodoFim(ultimoDiaDoMes);
            espelho_repository.save(espelho);

        }

        for (int i = 0; i < 13; i++) {

            espelho_model espelho = espelho_repository.findByPeriodoInicioAndRegistro(dataInicial.plusMonths(i), admin.getIdusuario().getRegistro()).get();

            int numeroDias = espelho.getPeriodoFim().getDayOfMonth();

            for (int j = 0; j < numeroDias; j++) {


                /*
               if (j == 0 || j == 1) {
                   espelho_item_model espelhoItem = new espelho_item_model();
                   espelhoItem.setData(espelho.getPeriodoInicio().plusDays(j));
                   espelhoItem.setAusencia(true);
                   espelhoItem.setEspelho(espelho);
                   espelho_item_repository.save(espelhoItem);
               } else {

                   espelho_item_model espelhoItem = new espelho_item_model();
                   espelhoItem.setData(espelho.getPeriodoInicio().plusDays(j));
                   espelhoItem.setEspelho(espelho);
                   espelho_item_repository.save(espelhoItem);
               }
               */





                 espelho_item_model espelhoItem = new espelho_item_model();
                espelhoItem.setData(espelho.getPeriodoInicio().plusDays(j));
                espelhoItem.setEspelho(espelho);
                espelho_item_repository.save(espelhoItem);

             

            }


        }



    }

}
