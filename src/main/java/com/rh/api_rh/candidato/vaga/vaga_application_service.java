package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.response.melhoresCandidatos_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.experiencia.experiencia_repository;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class vaga_application_service {

    @Autowired
    private candidato_vaga_repository candidatoVagaRepository;

    @Autowired
    private vaga_habilidade_repository vagaHabilidadeRepository;

    @Autowired
    private candidato_habilidade_repository candidatoHabilidadeRepository;

    @Autowired
    private experiencia_repository experienciaRepository;

    public Integer calcularPorcentagemCandidatos(vaga_model vaga) {

        List<candidato_vaga_model> listaAplicacoes = candidatoVagaRepository.findByVaga(vaga);

        Double totalAplicacoes = (double) listaAplicacoes.size();

        if (totalAplicacoes.equals(0)) {
            return null;
        }

        List<habilidade_model> habilidadesRequisitadas = new ArrayList<>();

        List<vaga_habilidade_model> intermediaria = vagaHabilidadeRepository.findByVaga(vaga);

        Double totalCandidatosComHabilidades = 0.0;

        Integer res = 0;

        for  (vaga_habilidade_model auxiliar : intermediaria) {
            habilidadesRequisitadas.add(auxiliar.getHabilidade());
        }

        Integer totalHabilidadesRequisitadas =  habilidadesRequisitadas.size();

        for (candidato_vaga_model aplicacao : listaAplicacoes) {
            Integer totalCorrespondido = 0;

            List<habilidade_model> habilidadesCandidato = new ArrayList<>();
            List<candidato_habilidade_model> intermediaria2 = candidatoHabilidadeRepository.findByCandidato(aplicacao.getCandidato());

            for (candidato_habilidade_model auxiliar2 : intermediaria2) {
                habilidadesCandidato.add(auxiliar2.getHabilidade());
            }

            for (habilidade_model auxiliar3 : habilidadesCandidato) {

                for (habilidade_model auxiliar4 : habilidadesRequisitadas) {
                    if (auxiliar3.equals(auxiliar4)) {
                        totalCorrespondido++;
                        break;
                    }
                }

            }

            if (totalCorrespondido.equals(totalHabilidadesRequisitadas)) {

                totalCandidatosComHabilidades++;

            }

        }

        Double divisao = (totalCandidatosComHabilidades / totalAplicacoes) * 100;
        res = divisao.intValue();

        return res;

    }

    public List<melhoresCandidatos_dto> melhoresCandidatos(vaga_model vaga) {

        try {
            List<melhoresCandidatos_dto> dezMelhores =  new ArrayList<>();

            List<candidato_vaga_model>  listaAplicacoes = candidatoVagaRepository.findByVaga(vaga);


            List<vaga_habilidade_model> habilidadesRequisitadas = vagaHabilidadeRepository.findByVaga(vaga);


            List<String> listaPalavrasChave = Arrays.asList(vaga.getPalavrasChave().split("\\s+"));

            Integer numeroPalavrasChave = listaPalavrasChave.size();

            Integer numeroHabilidadesRequisitadas = habilidadesRequisitadas.size();

            for (candidato_vaga_model aplicacao : listaAplicacoes) {

                Integer pontuacao = 0;

                Integer palavrasChavesPossuidas = 0;

                Integer habilidadesRequisitadasPossuidas = 0;

                candidato_model candidato = aplicacao.getCandidato();

                List<experiencia_model> experienciasCandidato = new ArrayList<>();

                experienciasCandidato = experienciaRepository.findByCandidato(candidato);

                List<List<String>> arrayPalavrasExperiencias = new ArrayList<>();

                for (experiencia_model experiencia : experienciasCandidato) {
                    List<String> palavras = Arrays.asList(experiencia.getDescricao().split("\\s+"));
                    arrayPalavrasExperiencias.add(palavras);
                }

                List<candidato_habilidade_model> habilidadesCandidato = candidatoHabilidadeRepository.findByCandidato(candidato);

                for (candidato_habilidade_model habilidade : habilidadesCandidato) {

                    Integer pontuacaoHabilidade = 0;

                    pontuacaoHabilidade = pontuacaoHabilidade + habilidade.getExperienciaEmMeses();

                    for (vaga_habilidade_model aux: habilidadesRequisitadas) {
                        if (aux.getHabilidade().equals(habilidade.getHabilidade())) {
                            pontuacaoHabilidade =  pontuacaoHabilidade * aux.getPeso();
                            habilidadesRequisitadasPossuidas++;
                            break;
                        }
                    }
                    pontuacao = pontuacao + pontuacaoHabilidade;


                }

                if (habilidadesRequisitadasPossuidas.equals(numeroHabilidadesRequisitadas)) {
                    pontuacao = pontuacao + 10;
                }

                for (List<String> item : arrayPalavrasExperiencias) {
                    if (palavrasChavesPossuidas.equals(numeroPalavrasChave)) {
                        break;
                    }

                    for (String palavra : listaPalavrasChave) {
                        if (item.contains(palavra)) {
                            pontuacao = pontuacao + 5;
                            palavrasChavesPossuidas++;
                            if (palavrasChavesPossuidas.equals(numeroPalavrasChave)) {
                                pontuacao = pontuacao + 10;
                                break;
                            }
                        }
                    }

                }

                melhoresCandidatos_dto candidatoPontuacao = new melhoresCandidatos_dto();
                candidatoPontuacao.setCandidato(candidato);
                candidatoPontuacao.setPontuacao(pontuacao);

                if (dezMelhores.size() < 10) {
                    dezMelhores.add(candidatoPontuacao);
                } else {

                    for (int i = 0; i < dezMelhores.size(); i++) {
                        if (dezMelhores.get(i).getPontuacao() < pontuacao) {
                            dezMelhores.set(i, candidatoPontuacao);
                        }
                    }

                }


            }

            return dezMelhores;


        } catch (Exception e) {
            return null;
        }

    }


}
