package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.aplicacao.vaga.avancarEtapa_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.compatibilidadeUnica_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.listaCompatibilidade_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.melhoresCandidatos_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.candidato_vaga.etapas;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.experiencia.experiencia_repository;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import com.rh.api_rh.util.email_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private vaga_repository vagaRepository;

    @Autowired
    private candidato_repository candidatoRepository;
    @Autowired
    private email_service email_service;

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

            List<candidato_vaga_model>  listaAplicacoes = candidatoVagaRepository.findByVagaAndEtapa(vaga, etapas.TRIAGEM);


            List<vaga_habilidade_model> habilidadesRequisitadas = vagaHabilidadeRepository.findByVaga(vaga);


            List<String> listaPalavrasChave = Arrays.stream(vaga.getPalavrasChave().split("\\s+"))
                    .map(p -> p.replaceAll("\\p{Punct}", ""))
                    .filter(p -> !p.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());


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

                    List<String> palavras = Arrays.stream(experiencia.getDescricao().split("\\s+"))
                            .map(p -> p.replaceAll("\\p{Punct}", ""))
                            .filter(p -> !p.isEmpty())
                            .map(String::toLowerCase)
                            .collect(Collectors.toList());

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

                if (dezMelhores.isEmpty()) {
                    dezMelhores.add(candidatoPontuacao);

                } else {
                    boolean foiadicionado = false;

                    for (int i = 0; i < dezMelhores.size(); i++) {

                        if (pontuacao >= dezMelhores.get(i).getPontuacao()) {

                            dezMelhores.add(i, candidatoPontuacao);
                            foiadicionado = true;
                            break;
                        }

                    }
                    if (!foiadicionado) {
                        dezMelhores.add(candidatoPontuacao);
                    }

                    if (dezMelhores.size() > 10) {
                        dezMelhores.remove(10);
                    }

                }


            }

            return dezMelhores;


        } catch (Exception e) {
            return null;
        }

    }

    public List<listaCompatibilidade_dto> listarCompatibilidades(Long id) {

        try {
            vaga_model vaga = new vaga_model();

            Optional<vaga_model> optionalvaga = vagaRepository.findById(id);
            if (optionalvaga.isPresent()) {
                vaga = optionalvaga.get();
            } else {

                return null;
            }


            List<listaCompatibilidade_dto> listaRes = new ArrayList<>();

            List<candidato_vaga_model>  listaAplicacoes = candidatoVagaRepository.findByVagaAndEtapa(vaga, etapas.TRIAGEM);

            if (listaAplicacoes.size() == 0) {
                return listaRes;
            }

            List<vaga_habilidade_model> habilidadesRequisitadas = vagaHabilidadeRepository.findByVaga(vaga);

            List<String> listaPalavrasChave = Arrays.stream(vaga.getPalavrasChave().split("\\s+"))
                    .map(p -> p.replaceAll("\\p{Punct}", ""))
                    .filter(p -> !p.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());



            Integer numeroPalavrasChave = listaPalavrasChave.size();

            Integer numeroHabilidadesRequisitadas = habilidadesRequisitadas.size();

            Integer pontuacaoMaximaEsperada = 0;
            pontuacaoMaximaEsperada = pontuacaoMaximaEsperada + (numeroPalavrasChave * 5) + 20;

            Integer auxParaMultiplicacao = 0;
            if (vaga.getNivel().equals("iniciante")) {
                auxParaMultiplicacao = 24;
            }

            if (vaga.getNivel().equals("intermediario")) {
                auxParaMultiplicacao = 60;
            }

            if (vaga.getNivel().equals("avancado")) {
                auxParaMultiplicacao = 120;
            }


            for (vaga_habilidade_model habilidade : habilidadesRequisitadas) {

                pontuacaoMaximaEsperada = pontuacaoMaximaEsperada + (habilidade.getPeso() * auxParaMultiplicacao);

            }


            for (candidato_vaga_model aplicacao : listaAplicacoes) {

                Integer pontuacao = 0;

                Integer palavrasChavesPossuidas = 0;

                Integer habilidadesRequisitadasPossuidas = 0;

                candidato_model candidato = aplicacao.getCandidato();

                List<experiencia_model> experienciasCandidato = new ArrayList<>();

                experienciasCandidato = experienciaRepository.findByCandidato(candidato);

                List<List<String>> arrayPalavrasExperiencias = new ArrayList<>();

                for (experiencia_model experiencia : experienciasCandidato) {

                    List<String> palavras = Arrays.stream(experiencia.getDescricao().split("\\s+"))           // Divide pelo espaço
                            .map(p -> p.replaceAll("\\p{Punct}", ""))        // Remove pontuação
                            .filter(p -> !p.isEmpty())                        // Remove strings vazias
                            .map(String::toLowerCase)                         // Converte para lowercase
                            .collect(Collectors.toList());

                    arrayPalavrasExperiencias.add(palavras);

                }

                List<candidato_habilidade_model> habilidadesCandidato = candidatoHabilidadeRepository.findByCandidato(candidato);

                for (candidato_habilidade_model habilidade : habilidadesCandidato) {

                    Integer pontuacaoHabilidade = 0;

                    for (vaga_habilidade_model aux : habilidadesRequisitadas) {
                        if (aux.getHabilidade().equals(habilidade.getHabilidade())) {

                            int aux10 = habilidade.getExperienciaEmMeses();

                            if (habilidade.getExperienciaEmMeses() > auxParaMultiplicacao) {
                                aux10 = auxParaMultiplicacao;
                            }

                            pontuacaoHabilidade = pontuacaoHabilidade + (aux10 * aux.getPeso());
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

                listaCompatibilidade_dto item = new listaCompatibilidade_dto();

                Double auxDiv = (double) pontuacao;
                Double auxDiv2 = (double) pontuacaoMaximaEsperada;

                Double porcentagem = ((auxDiv / auxDiv2) * 100);
                Integer porcentagemRes = porcentagem.intValue();

                if  (porcentagemRes > 100) {
                    porcentagemRes = 100;
                }

                item.setCompatibilidadeEmPorcentagem(porcentagemRes);
                item.setCandidato(candidato);

                listaRes.add(item);



            }



            return listaRes;

        } catch (Exception e) {

            return null;
        }




    }

    public listaCompatibilidade_dto compatibilidadeUnica(compatibilidadeUnica_dto dto) {

        try {
            vaga_model vaga = new vaga_model();

            Optional<vaga_model> optionalvaga = vagaRepository.findById(dto.getVagaid());
            if (optionalvaga.isPresent()) {
                vaga = optionalvaga.get();
            } else {

                return null;
            }


            listaCompatibilidade_dto Res = new listaCompatibilidade_dto();

            Optional<candidato_model> candidato = candidatoRepository.findById(dto.getCandidatoid());

            candidato_model candidatoData = new candidato_model();
            if (candidato.isPresent()) {

                candidatoData =  candidato.get();
            } else {
                return null;
            }


            List<vaga_habilidade_model> habilidadesRequisitadas = vagaHabilidadeRepository.findByVaga(vaga);

            List<String> listaPalavrasChave = Arrays.stream(vaga.getPalavrasChave().split("\\s+"))
                    .map(p -> p.replaceAll("\\p{Punct}", ""))
                    .filter(p -> !p.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());



            Integer numeroPalavrasChave = listaPalavrasChave.size();

            Integer numeroHabilidadesRequisitadas = habilidadesRequisitadas.size();

            Integer pontuacaoMaximaEsperada = 0;
            pontuacaoMaximaEsperada = pontuacaoMaximaEsperada + (numeroPalavrasChave * 5) + 20;

            Integer auxParaMultiplicacao = 0;
            if (vaga.getNivel().equals("iniciante")) {
                auxParaMultiplicacao = 24;
            }

            if (vaga.getNivel().equals("intermediario")) {
                auxParaMultiplicacao = 60;
            }

            if (vaga.getNivel().equals("avancado")) {
                auxParaMultiplicacao = 120;
            }


            for (vaga_habilidade_model habilidade : habilidadesRequisitadas) {

                pontuacaoMaximaEsperada = pontuacaoMaximaEsperada + (habilidade.getPeso() * auxParaMultiplicacao);

            }


                Integer pontuacao = 0;

                Integer palavrasChavesPossuidas = 0;

                Integer habilidadesRequisitadasPossuidas = 0;


                List<experiencia_model> experienciasCandidato = new ArrayList<>();

                experienciasCandidato = experienciaRepository.findByCandidato(candidatoData);

                List<List<String>> arrayPalavrasExperiencias = new ArrayList<>();

                for (experiencia_model experiencia : experienciasCandidato) {

                    List<String> palavras = Arrays.stream(experiencia.getDescricao().split("\\s+"))           // Divide pelo espaço
                            .map(p -> p.replaceAll("\\p{Punct}", ""))        // Remove pontuação
                            .filter(p -> !p.isEmpty())                        // Remove strings vazias
                            .map(String::toLowerCase)                         // Converte para lowercase
                            .collect(Collectors.toList());

                    arrayPalavrasExperiencias.add(palavras);

                }

                List<candidato_habilidade_model> habilidadesCandidato = candidatoHabilidadeRepository.findByCandidato(candidatoData);

                for (candidato_habilidade_model habilidade : habilidadesCandidato) {

                    Integer pontuacaoHabilidade = 0;

                    for (vaga_habilidade_model aux : habilidadesRequisitadas) {
                        if (aux.getHabilidade().equals(habilidade.getHabilidade())) {

                            int aux10 = habilidade.getExperienciaEmMeses();

                            if (habilidade.getExperienciaEmMeses() > auxParaMultiplicacao) {
                                aux10 = auxParaMultiplicacao;
                            }

                            pontuacaoHabilidade = pontuacaoHabilidade + (aux10 * aux.getPeso());
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

                listaCompatibilidade_dto item = new listaCompatibilidade_dto();

                Double auxDiv = (double) pontuacao;
                Double auxDiv2 = (double) pontuacaoMaximaEsperada;

                Double porcentagem = ((auxDiv / auxDiv2) * 100);
                Integer porcentagemRes = porcentagem.intValue();

            if  (porcentagemRes > 100) {
                porcentagemRes = 100;
            }

                item.setCompatibilidadeEmPorcentagem(porcentagemRes);
                item.setCandidato(candidatoData);


            return item;

        } catch (Exception e) {

            return null;
        }

    }

    public candidato_vaga_model avancarEtapa(avancarEtapa_dto dto) {

        try {
            vaga_model vaga = new vaga_model();
            candidato_model candidato = new candidato_model();

            Optional<candidato_model> candidatoData = candidatoRepository.findById(dto.getCandidatoid());

            Optional<vaga_model> vagaData = vagaRepository.findById(dto.getVagaid());

            if (candidatoData.isPresent() && vagaData.isPresent()) {
                candidato = candidatoData.get();
                vaga = vagaData.get();
            } else {
                return null;
            }

            Optional<candidato_vaga_model> aplicacaoData = candidatoVagaRepository.findByCandidatoAndVaga(candidato, vaga);
            if (aplicacaoData.isPresent()) {

                candidato_vaga_model aplicacao = aplicacaoData.get();

                if (aplicacao.getEtapa().equals(etapas.TRIAGEM)) {
                    aplicacao.setEtapa(etapas.ENTREVISTA);
                } else if (aplicacao.getEtapa().equals(etapas.ENTREVISTA)) {
                    aplicacao.setEtapa(etapas.OFERTA);
                } else if (aplicacao.getEtapa().equals(etapas.OFERTA)) {
                    return aplicacao;
                }

                candidatoVagaRepository.save(aplicacao);
                email_service.enviarAvancoEtapa(candidato, vaga);
                return aplicacao;

            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    public candidato_vaga_model desistencia(avancarEtapa_dto dto) {

        try {
            vaga_model vaga = new vaga_model();
            candidato_model candidato = new candidato_model();

            Optional<candidato_model> candidatoData = candidatoRepository.findById(dto.getCandidatoid());

            Optional<vaga_model> vagaData = vagaRepository.findById(dto.getVagaid());

            if (candidatoData.isPresent() && vagaData.isPresent()) {
                candidato = candidatoData.get();
                vaga = vagaData.get();
            } else {
                return null;
            }

            Optional<candidato_vaga_model> aplicacaoData = candidatoVagaRepository.findByCandidatoAndVaga(candidato, vaga);
            if (aplicacaoData.isPresent()) {

                candidato_vaga_model aplicacao = aplicacaoData.get();

                aplicacao.setEtapa(etapas.DESISTENCIA);
                candidatoVagaRepository.save(aplicacao);
                return aplicacao;

            }
            return null;
        }catch (Exception e) {
            return null;
        }

    }

    public candidato_vaga_model finalizarAplicacao(avancarEtapa_dto dto) {

        try {
            vaga_model vaga = new vaga_model();
            candidato_model candidato = new candidato_model();

            Optional<candidato_model> candidatoData = candidatoRepository.findById(dto.getCandidatoid());

            Optional<vaga_model> vagaData = vagaRepository.findById(dto.getVagaid());

            if (candidatoData.isPresent() && vagaData.isPresent()) {
                candidato = candidatoData.get();
                vaga = vagaData.get();
            } else {
                return null;
            }

            Optional<candidato_vaga_model> aplicacaoData = candidatoVagaRepository.findByCandidatoAndVaga(candidato, vaga);
            if (aplicacaoData.isPresent()) {

                candidato_vaga_model aplicacao = aplicacaoData.get();

                aplicacao.setEtapa(etapas.FINALIZADO);
                candidatoVagaRepository.save(aplicacao);
                email_service.enviarFinalizacaoCandidatura(candidato, vaga);
                return aplicacao;

            }
            return null;
        }catch (Exception e) {
            return null;
        }

    }




}
