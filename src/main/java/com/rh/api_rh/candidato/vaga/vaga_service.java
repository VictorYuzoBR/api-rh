package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.aplicacao.vaga.compatibilidadeUnica_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.listaCompatibilidade_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarVagaMapeado_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatura_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.quantidadePessoasEtapa_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.candidato_vaga.etapas;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.util.email_service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class vaga_service {

    @Autowired
    private vaga_repository vagarepository;

    @Autowired
    private vaga_mapper mapper;

    @Autowired
    private habilidade_service habilidadeservice;

    @Autowired
    private vaga_habilidade_repository vagahabilidaderepository;

    @Autowired
    private candidato_repository candidatorepository;

    @Autowired
    private candidato_vaga_repository candidatovagarepository;

    @Autowired
    private funcionario_service funcionarioservice;

    @Autowired
    private log_repository logrepository;

    @Autowired
    private vaga_application_service vagaapplicationservice;

    @Autowired
    private email_service emailService;


    /// recebe os dados necessarios de um objeto vaga e realiza o salvamento no banco
    @Transactional(rollbackOn =  Exception.class)
    public vaga_model cadastrar(cadastrarVaga_dto dto) {

        try {

            cadastrarVagaMapeado_dto dados = mapper.convert(dto);
            if (dados == null) {
                return null;
            } else {

                vaga_model vaga = vagarepository.save(dados.getVaga());
                String res = habilidadeservice.cadastrarParaVaga(dados.getHabilidades(), vaga);
                if (!res.equals("sucesso")) {
                    throw new IllegalStateException("uma das habilidade requisitadas não está cadastrada");
                } else {
                    return vaga;
                }

            }

        }  catch (Exception e) {
            throw e;
        }


    }

    public List<vaga_model> listar() {
        return vagarepository.findAll();
    }

    public List<vaga_habilidade_model> listarTabelaIntermediaria() {

        return vagahabilidaderepository.findAll();

    }

    ///  recebe o id de um objeto vaga, um objeto candidato, e realiza a criacao de um objeto intermediario candidatura
    public String candidatar(cadastroCandidatura_dto dto) {

        Optional<candidato_model> candidato = candidatorepository.findById(dto.getIdcandidato());
        Optional<vaga_model> vaga = vagarepository.findById(dto.getIdvaga());

        if(candidato.isPresent() && vaga.isPresent()) {

            Optional<candidato_vaga_model> jaexiste = candidatovagarepository.findByCandidatoAndVaga(candidato.get(), vaga.get());
            if(jaexiste.isPresent()) {
                return"candidato ja aplicou para esta vaga";
            }


            candidato_vaga_model aplicacao = new candidato_vaga_model();
            aplicacao.setCandidato(candidato.get());
            aplicacao.setVaga(vaga.get());
            aplicacao.setEtapa(etapas.TRIAGEM);
            candidatovagarepository.save(aplicacao);
            return "sucesso";
        } else {
            return "falha ao criar candidatura";
        }

    }

    /// recebe o id de um objeto vaga e retorna a quantidade de candidatos em cada etapa
    public quantidadePessoasEtapa_dto calcularPessoasEtapa(Long idvaga) {
        quantidadePessoasEtapa_dto dto = new quantidadePessoasEtapa_dto();
        try {
            Optional<vaga_model> data =  vagarepository.findById(idvaga);

            if(data.isPresent()) {

                vaga_model vaga = data.get();
                List<candidato_vaga_model> aplicacoes = candidatovagarepository.findByVaga(vaga);

                Integer contagemTriagem = 0;
                Integer contagemEntrevistas = 0;
                Integer contagemOferta = 0;
                Integer contagemDesistencia = 0;
                Integer contagemFinalizado = 0;

                for (candidato_vaga_model aplicacao : aplicacoes) {
                    if (aplicacao.getEtapa().equals(etapas.TRIAGEM)) {
                        contagemTriagem++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.DESISTENCIA)) {
                        contagemDesistencia++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.FINALIZADO)) {
                        contagemFinalizado++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.OFERTA)) {
                        contagemOferta++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.ENTREVISTA)) {
                        contagemEntrevistas++;
                    }
                }

                dto.getEtapas().put("TRIAGEM", contagemTriagem);
                dto.getEtapas().put("DESISTENCIA", contagemDesistencia);
                dto.getEtapas().put("FINALIZADO", contagemFinalizado);
                dto.getEtapas().put("OFERTA", contagemOferta);
                dto.getEtapas().put("ENTREVISTA", contagemEntrevistas);

            } else {
                return null;
            }



        } catch (Exception e) {
            return null;
        }
        return dto;


    }


    /// muda o status de um objeto vaga para finalizado, retirando da listagem de outras funções e enviando um email para cada candidato que estava
    /// aplicando para vaga
    public vaga_model finalizarVaga(Long idvaga, UUID idrh) {

        try {

            Optional<vaga_model> vaga = vagarepository.findById(idvaga);
            if (vaga.isPresent()) {

                List<candidato_vaga_model>  listaAplicacoes = candidatovagarepository.findByVaga(vaga.get());

                for (candidato_vaga_model aplicacao : listaAplicacoes) {

                    if (!aplicacao.getEtapa().equals(etapas.OFERTA)) {

                        aplicacao.setEtapa(etapas.FINALIZADO);
                        candidatovagarepository.save(aplicacao);

                        emailService.enviarFinalizacaoCandidatura(aplicacao.getCandidato(), aplicacao.getVaga());


                    } else {
                        aplicacao.setEtapa(etapas.FINALIZADO);
                        candidatovagarepository.save(aplicacao);
                    }

                }

                vaga.get().setStatus("finalizado");
                vagarepository.save(vaga.get());

                funcionario_model funcionario = funcionarioservice.buscar(idrh);
                log_model log = new log_model();

                log.setTipo(("funcionario"));
                log.setRegistro(funcionario.getIdusuario().getRegistro());
                log.setAcao("Funcionario de registro: "+funcionario.getIdusuario().getRegistro()+" encerrou a vaga de id: "+idvaga);
                log.setData(new Date());
                logrepository.save(log);

                return vaga.get();


            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    ///  lista objetos vaga com status ativo
    public List<vaga_model> listarVagasAtivas() {

        List<vaga_model> vagas = new ArrayList<vaga_model>();
        vagas =  vagarepository.findByStatus("ativo");
        return vagas;

    }

    /// recebe o id de um objeto vaga, uma etapa, e retorna uma lista de candidatos que estão na etapa
    public List<listaCompatibilidade_dto> listarPorUmaEtapa(Long idvaga, etapas etapa) {

        List<listaCompatibilidade_dto> res = new ArrayList<>();

        try {

            Optional<vaga_model> vaga = vagarepository.findById(idvaga);
            if (vaga.isPresent()) {

                List<candidato_vaga_model> candidaturas = candidatovagarepository.findByVagaAndEtapa(vaga.get(), etapa);

                for (candidato_vaga_model c : candidaturas) {

                    compatibilidadeUnica_dto dto = new compatibilidadeUnica_dto();
                    dto.setCandidatoid(c.getCandidato().getId());
                    dto.setVagaid(idvaga);

                    listaCompatibilidade_dto item = vagaapplicationservice.compatibilidadeUnica(dto);

                    res.add(item);

                }

                return res;

            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }



}
