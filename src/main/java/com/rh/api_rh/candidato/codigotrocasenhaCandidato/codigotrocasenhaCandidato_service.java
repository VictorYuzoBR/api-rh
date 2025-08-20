package com.rh.api_rh.candidato.codigotrocasenhaCandidato;

import com.rh.api_rh.DTO.login.validarcodigotrocasenhaCandidato_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.util.email_service;
import com.rh.api_rh.util.registro_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class codigotrocasenhaCandidato_service {

    @Autowired
    private codigotrocasenhaCandidato_repository codigotrocasenhaCandidatoRepository;

    @Autowired
    private registro_service registro_service;

    @Autowired
    private candidato_repository candidato_repository;


    public String cadastrarcodigo(String email) {
        try {
            Long idcandidato = null;
            Optional<candidato_model> candidato = candidato_repository.findByEmail(email);
            if (candidato.isPresent()) {
                idcandidato = candidato.get().getId();
            }
            if (idcandidato == null) {
                return "candidato não encontrado";
            }

            Optional<codigotrocasenhaCandidato_model> filter = codigotrocasenhaCandidatoRepository.findById(idcandidato);
            if (filter.isPresent()) {
                codigotrocasenhaCandidatoRepository.delete(filter.get());
            }

            String codigo = registro_service.gerarcodigotrocasenha();
            Date data = new Date();
            codigotrocasenhaCandidato_model codigotrocasenhacandidato = new codigotrocasenhaCandidato_model();
            codigotrocasenhacandidato.setCodigo(codigo);
            codigotrocasenhacandidato.setIdcandidato(idcandidato);
            codigotrocasenhacandidato.setData(data);
            codigotrocasenhaCandidatoRepository.save(codigotrocasenhacandidato);

            return (codigo);
        } catch (Exception e) {
            return ("erro");
        }

    }

    public String deletar(Long id) {

        Optional<codigotrocasenhaCandidato_model> codigotrocasenha = codigotrocasenhaCandidatoRepository.findByIdcandidato(id);
        if (codigotrocasenha.isPresent()) {
            codigotrocasenhaCandidatoRepository.delete(codigotrocasenha.get());
        }
        return("Deletado com sucesso");
    }

    public boolean validartempo(codigotrocasenhaCandidato_model codigotrocasenha) {

        Date datacodigo = codigotrocasenha.getData();
        Date dataagora = new Date();
        if (dataagora.getTime() - datacodigo.getTime() <  30000) {
            codigotrocasenhaCandidatoRepository.delete(codigotrocasenha);
            return true;
        } else {
            return false;
        }

    }


    public codigotrocasenhaCandidato_model validarcodigo(Long idcandidato, String codigo) {

        Optional<codigotrocasenhaCandidato_model> codigobanco = codigotrocasenhaCandidatoRepository.findByIdcandidato(idcandidato);
        if (!codigobanco.isEmpty()) {
            if (codigobanco.get().getCodigo().equals(codigo)) {
                return codigobanco.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ResponseEntity<String> validarTudo(validarcodigotrocasenhaCandidato_dto dto){

        Optional<candidato_model> candidatoData = candidato_repository.findByEmail(dto.getEmail());
        if (candidatoData.isPresent()) {

            Long id =  candidatoData.get().getId();

            codigotrocasenhaCandidato_model codigo = validarcodigo(id, dto.getCodigo()) ;

            if (codigo != null) {

                if(validartempo(codigo)){
                    return ResponseEntity.ok().body("troca de senha autorizada");
                } else {
                    return ResponseEntity.badRequest().body("codigo expirado");
                }

            } else {
                return  ResponseEntity.badRequest().body("codigo incorreto");
            }

        }  else {
            return  ResponseEntity.badRequest().body("erro");
        }

    }



}
