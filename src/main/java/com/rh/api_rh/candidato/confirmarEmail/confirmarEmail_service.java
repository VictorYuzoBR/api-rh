package com.rh.api_rh.candidato.confirmarEmail;

import com.rh.api_rh.DTO.cadastro.confirmarEmail_dto;
import com.rh.api_rh.DTO.cadastro.validarCodigoConfirmacao_dto;
import com.rh.api_rh.util.email_service;
import com.rh.api_rh.util.registro_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class confirmarEmail_service {

    @Autowired
    private registro_service registroService;

    @Autowired
    private email_service emailService;

    @Autowired
    private confirmarEmail_repository confirmarEmailRepository;

    public String enviarEmail(confirmarEmail_dto dto) {

        try {

            Optional<confirmarEmail_model> existeRegistro = confirmarEmailRepository.findByEmail(dto.getEmail());
            if (existeRegistro.isPresent()) {

                confirmarEmailRepository.delete(existeRegistro.get());

            }

            String codigo = registroService.gerarcodigotrocasenha();

            String aux = emailService.enviarCodigoConfirmarEmailCandidato(dto.getEmail(), codigo);
            if (aux.equals("Email enviado com sucesso!")) {

                confirmarEmail_model registroTabela = new confirmarEmail_model();
                registroTabela.setEmail(dto.getEmail());
                registroTabela.setCodigo(codigo);
                Date data = new Date();
                registroTabela.setData(data);
                confirmarEmailRepository.save(registroTabela);

                return ("email enviado com sucesso!");

            } else {
                return ("erro ao enviar o e-mail!");
            }
        } catch (Exception e) {
            return ("erro ao enviar o e-mail!");
        }

    }

    public String validarCodigoConfirmacaoEmail(validarCodigoConfirmacao_dto dto) {

        Optional<confirmarEmail_model> registroTabela = confirmarEmailRepository.findByEmail(dto.getEmail());
        if (registroTabela.isPresent()) {

            if (registroTabela.get().getCodigo().equals(dto.getCodigo())) {

                Date datacodigo = registroTabela.get().getData();
                Date dataagora = new Date();
                if (dataagora.getTime() - datacodigo.getTime() <  30000) {

                    confirmarEmailRepository.delete(registroTabela.get());
                    return ("email confirmado com sucesso!");

                } else {

                    confirmarEmailRepository.delete(registroTabela.get());
                    return ("codigo expirado");

                }

            } else {
                return ("codigo incorreto!");
            }

        } else {
            return("email não possui código cadastrado");
        }








    }

}
