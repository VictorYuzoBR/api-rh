package com.rh.api_rh.espelho;

import com.rh.api_rh.DTO.aplicacao.espelho.descreverAbono_dto;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_repository;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class espelho_application_service {

    @Autowired
    private funcionario_service funcionarioService;

    @Autowired
    private espelho_repository espelhoRepository;

    @Autowired
    private espelho_item_repository espelhoItemRepository;

    @Autowired
    private entrada_espelho_repository entradaEspelhoRepository;

    ///  tirar um 0 quando quiser testar
    @Scheduled(fixedRate = 200000)
    public void gerarEspelho() {

        List<funcionario_model> funcionarios = funcionarioService.listar();
        YearMonth anoMesAtual = YearMonth.now();
        LocalDate ultimoDiaDoMes = anoMesAtual.atEndOfMonth();
        LocalDate primeiroDia = anoMesAtual.atDay(1);

        if (funcionarios.size() > 0) {

            for (funcionario_model funcionario : funcionarios) {

                Optional<espelho_model> aux = espelhoRepository.findByRegistroAndPeriodoInicioAndPeriodoFim(funcionario.getIdusuario().getRegistro(), primeiroDia, ultimoDiaDoMes);
                if (aux.isEmpty()) {

                    espelho_model espelho = new espelho_model();
                    espelho.setNomeFuncionario(funcionario.getNome());
                    espelho.setRegistro(funcionario.getIdusuario().getRegistro());
                    espelho.setFuncao(funcionario.getFuncao());
                    espelho.setPeriodoInicio(primeiroDia);
                    espelho.setPeriodoFim(ultimoDiaDoMes);
                    espelhoRepository.save(espelho);
                }

            }
        }

    }

    @Scheduled(fixedRate = 300000)
    public void gerarItemDiario() {

        YearMonth anoMesAtual = YearMonth.now();
        LocalDate primeiroDia = anoMesAtual.atDay(1);

        LocalDate hoje = LocalDate.now();

        List<espelho_model> espelhos = espelhoRepository.findByPeriodoInicio(primeiroDia);

        if (espelhos.size() > 0) {

            for (espelho_model espelho : espelhos) {

                Optional<espelho_item_model> jaexiste = espelhoItemRepository.findByDataAndEspelho(hoje, espelho);
                if (jaexiste.isEmpty()) {

                    espelho_item_model  espelhoItem = new espelho_item_model();
                    espelhoItem.setData(hoje);
                    espelhoItem.setEspelho(espelho);
                    espelhoItemRepository.save(espelhoItem);

                }

            }

        }


    }

    public String baterPonto(UUID idfuncionario) {

        funcionario_model funcionario = funcionarioService.buscar(idfuncionario);
        String registro = funcionario.getIdusuario().getRegistro();
        YearMonth anoMesAtual = YearMonth.now();
        LocalDate primeiroDia = anoMesAtual.atDay(1);
        LocalDate hoje = LocalDate.now();

        Optional<espelho_model> espelho = espelhoRepository.findByRegistroAndPeriodoInicio(registro, primeiroDia);
        if (espelho.isPresent()) {

            Optional<espelho_item_model> espelhoItem = espelhoItemRepository.findByDataAndEspelho(hoje, espelho.get());
            if (espelhoItem.isPresent()) {

                List<entrada_espelho_model> entradas = entradaEspelhoRepository.findByItem(espelhoItem.get());
                int tamanho = entradas.size();
                if (tamanho > 0) {
                    if (tamanho > 3) {
                        return ("maximo de pontos diarios atingido");
                    }

                    entrada_espelho_model aux = entradas.get(tamanho - 1);
                    if (aux.getTipo().equals("entrada")) {
                        entrada_espelho_model entrada = new entrada_espelho_model();
                        entrada.setItem(espelhoItem.get());
                        entrada.setTipo("saida");
                        entrada.setHora(LocalTime.now());
                        entradaEspelhoRepository.save(entrada);


                    } else {
                        entrada_espelho_model entrada = new entrada_espelho_model();
                        entrada.setItem(espelhoItem.get());
                        entrada.setTipo("entrada");
                        entrada.setHora(LocalTime.now());
                        entradaEspelhoRepository.save(entrada);


                    }
                    return("ponto criado com sucesso");

                } else {

                    entrada_espelho_model entrada = new entrada_espelho_model();
                    entrada.setItem(espelhoItem.get());
                    entrada.setTipo("entrada");
                    entrada.setHora(LocalTime.now());
                    entradaEspelhoRepository.save(entrada);

                    return("ponto criado com sucesso");

                }

            } else {
                return ("erro ao criar ponto");
            }

        } else {
            return ("erro ao criar ponto");
        }


    }

    @Scheduled(fixedRate = 400000)
    public void gerarAusencia() {

        LocalDate hoje = LocalDate.now();

        List<espelho_item_model> lista = espelhoItemRepository.findByData(hoje);
        if (lista.size() > 0) {
            for (espelho_item_model item : lista) {

                List<entrada_espelho_model> entradas = entradaEspelhoRepository.findByItem(item);
                if (entradas.size() == 0) {

                    item.setAusencia(true);
                    espelhoItemRepository.save(item);

                }

            }
        }


    }

    public String descreverAbono(descreverAbono_dto dto) {

        Optional<espelho_item_model> item = espelhoItemRepository.findById(dto.getIditem());
        if (item.isPresent()) {
            item.get().setDescricaoAbono(dto.getDescricao());
            espelhoItemRepository.save(item.get());
            return ("sucesso");
        } else {
            return("erro");
        }


    }




}
