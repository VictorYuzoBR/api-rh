package com.rh.api_rh.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class email_service {

    @Autowired
    private registro_service registro_service;

    @Autowired
    private JavaMailSender mail_sender;

    @Value("${spring.mail.username}")
    private String remetente;

    public String enviarcodigosenha() {

        String codigo = registro_service.gerarcodigotrocasenha();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("victoryuzofb4@gmail.com");
            message.setFrom(remetente);
            message.setSubject("Teste");
            message.setText("Seu código para troca de senha é: " + codigo);
            mail_sender.send(message);

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

}
