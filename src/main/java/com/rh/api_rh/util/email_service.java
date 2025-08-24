package com.rh.api_rh.util;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.codigotrocasenhaCandidato.codigotrocasenhaCandidato_service;
import com.rh.api_rh.candidato.vaga.vaga_model;
import com.rh.api_rh.comunicado.comunicado_model;
import com.rh.api_rh.funcionario.funcionario_model;
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

    @Autowired
    private codigotrocasenhaCandidato_service  codigotrocasenhaCandidatoservice;

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

    public String enviarcodigosenhacandidato(String email, String codigo) {

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

    public String enviarAvancoEtapa(candidato_model candidato, vaga_model vaga) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(candidato.getEmail());
            message.setFrom(remetente);
            message.setSubject("Atualização de status de candidatura");
            message.setText("Prezado candidato "+candidato.getNome()+", gostariamos de informar que você foi selecionado para avançar para a próxima etapa " +
                    "da vaga "+ vaga.getId()+", entraremos em contato por email em breve para mais detalhes, Atenciosamente, TIME BIKUBE.");
            mail_sender.send(message);
            return("Email enviado com sucesso!");

        }  catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarFinalizacaoCandidatura(candidato_model candidato, vaga_model vaga) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(candidato.getEmail());
            message.setFrom(remetente);
            message.setSubject("Atualização de status de candidatura");
            message.setText("Prezado candidato "+candidato.getNome()+", gostariamos de informar sobre sua candidatura para vaga de " +
                    + vaga.getId()+", após uma grande análise de currículos, decidimos continuar com outro candidato cujo perfil está mais próximo" +
                    "dos requisitos específicos da vaga, mas isso não significa que não teremos mais oportunidades futuramente que sejam compatíveis com seu currículo!" +
                    "guardaremos suas informações em nosso banco de talentos para enviar emails quando novas oportunidades adequadas ao seu perfil surgirem!" +
                    "lembrando que caso não queira mais receber nossos emails, você pode pedir a exclusão de seus dados do nosso sistema utilizando nosso site! Atenciosamente, TIME BIKUBE.");
            mail_sender.send(message);
            return("Email enviado com sucesso!");

        }  catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarEmailNovaVaga(candidato_model candidato, String titulo) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(candidato.getEmail());
            message.setFrom(remetente);
            message.setSubject("Nova oportunidade");
            message.setText("Prezado "+candidato.getNome()+", Nós do time BIKUBE gostariamos de informar que uma nova vaga para "+titulo+" foi aberta em nosso site, verificamos que " +
                    "seu perfil atende aos requisitos exigidos na vaga e gostariamos de convida-lo para participar do processo seletivo! Caso possua interesse, " +
                    "responda este email com uma mensagem de confirmação e logo uma pessoa de nosso time irá entrar em contato para mais informações, atenciosamente, time BIKUBE.");
            mail_sender.send(message);
            return("Email enviado com sucesso!");

        }   catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarComunicado(comunicado_model comunicado, funcionario_model funcionario) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(funcionario.getEmail());
            message.setFrom(remetente);
            message.setSubject("Comunicado BIKUBE");
            message.setText(comunicado.getTexto());
            mail_sender.send(message);
            return("Email enviado com sucesso!");

        }   catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

}
