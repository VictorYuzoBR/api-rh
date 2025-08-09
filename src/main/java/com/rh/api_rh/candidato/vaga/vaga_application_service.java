package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class vaga_application_service {

    @Autowired
    private candidato_vaga_repository candidatoVagaRepository;

    @Autowired
    private vaga_habilidade_repository vagaHabilidadeRepository;

    @Autowired
    private candidato_habilidade_repository candidatoHabilidadeRepository;

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


}
