package com.rh.api_rh.util;

import com.rh.api_rh.usuario.codigotrocasenha.codigotrocasenha_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class email_service {

    @Autowired
    private registro_service registro_service;

    @Autowired
    private JavaMailSender mail_sender;

    @Autowired
    private codigotrocasenha_service codigotrocasenhaservice;

    @Value("${spring.mail.username}")
    private String remetente;

    public String enviarcodigosenha(String email, UUID id) {

        String codigo = codigotrocasenhaservice.cadastrar(id);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom(remetente);
            message.setSubject("Teste");
            message.setText("Seu código para troca de senha é: " + codigo);
            mail_sender.send(message);

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }


    public String enviarcadastro(String email, usuarioprovisorio usuario) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom(remetente);
            message.setSubject("Notificação de criação de usuário");
            message.setText("Bem vindo à BIKUBE! Seu novo registro de usuário foi criado com as seguintes credenciais:\n\nRegistro de usuario: "+ usuario.getRegistro()+"\n " +
                    "Senha provisória: "+ usuario.getSenha()+"\n\nApós o primeiro login, você será redirecionado para realizar a troca de senha.");
            mail_sender.send(message);

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

}
