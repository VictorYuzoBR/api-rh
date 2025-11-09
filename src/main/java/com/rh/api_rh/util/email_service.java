package com.rh.api_rh.util;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.codigotrocasenhaCandidato.codigotrocasenhaCandidato_service;
import com.rh.api_rh.candidato.vaga.vaga_model;
import com.rh.api_rh.comunicado.comunicado_model;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.usuario.codigotrocasenha.codigotrocasenha_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class email_service {

    @Autowired
    private registro_service registro_service;


    @Autowired
    private codigotrocasenha_service codigotrocasenhaservice;

    @Autowired
    private codigotrocasenhaCandidato_service  codigotrocasenhaCandidatoservice;

    @Value("${SENDGRID_API_KEY}")
    private String apikey;

    private SendGrid sendgrid;

    @PostConstruct
    private void init() {
        this.sendgrid = new SendGrid(apikey);
    }

    private String remetente = "victoryuzoumc@gmail.com";


    public String enviarcodigosenha(String email, UUID id) {

        String codigo = codigotrocasenhaservice.cadastrar(id);

        Email from = new Email(remetente);
        Email to = new Email(email);
        Content content = new Content("text/plain", "Seu código para troca de senha é: " + codigo);

        Mail mail = new Mail(from,"Codigo troca de senha", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");


        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarcodigosenhacandidato(String email, String codigo) {


        Email from = new Email(remetente);
        Email to = new Email(email);
        Content content = new Content("text/plain", "Seu código para troca de senha é: " + codigo);

        Mail mail = new Mail(from,"Codigo troca de senha", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");


        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }






    public String enviarcadastro(String email, usuarioprovisorio usuario) {



            Email from = new Email(remetente);
            Email to = new Email(email);
            Content content = new Content("text/plain", "Bem vindo à BIKUBE! Seu novo registro de usuário foi criado com as seguintes credenciais:\n\nRegistro de usuario: "+ usuario.getRegistro()+"\n " +
                    "Senha provisória: "+ usuario.getSenha()+"\n\nApós o primeiro login, você será redirecionado para realizar a troca de senha.");

            Mail mail = new Mail(from,"Notificação de criação de usuário", to, content);

            Request request = new Request();

            try {

                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sendgrid.api(request);

                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
                System.out.println("Response Headers: " + response.getHeaders());

                return("Email enviado com sucesso!");


            }

         catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarAvancoEtapa(candidato_model candidato, vaga_model vaga) {

        Email from = new Email(remetente);
        Email to = new Email(candidato.getEmail());
        Content content = new Content("text/plain", "Prezado candidato "+candidato.getNome()+", gostariamos de informar que você foi selecionado para avançar para a próxima etapa " +
                "da vaga "+ vaga.getTitulo()+", entraremos em contato por email em breve para mais detalhes, Atenciosamente, TIME BIKUBE.");

        Mail mail = new Mail(from,"Atualização de status de candidatura", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarFinalizacaoCandidatura(candidato_model candidato, vaga_model vaga) {

        Email from = new Email(remetente);
        Email to = new Email(candidato.getEmail());
        Content content = new Content("text/plain", "Prezado candidato "+candidato.getNome()+", gostariamos de informar sobre sua candidatura para vaga de " +
                 vaga.getTitulo()+", após uma grande análise de currículos, decidimos continuar com outro candidato cujo perfil está mais próximo" +
                "dos requisitos específicos da vaga, mas isso não significa que não teremos mais oportunidades futuramente que sejam compatíveis com seu currículo!" +
                "guardaremos suas informações em nosso banco de talentos para enviar emails quando novas oportunidades adequadas ao seu perfil surgirem!" +
                "lembrando que caso não queira mais receber nossos emails, você pode pedir a exclusão de seus dados do nosso sistema utilizando nosso site! Atenciosamente, TIME BIKUBE.");

        Mail mail = new Mail(from,"Atualização de status de candidatura", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarEmailNovaVaga(candidato_model candidato, String titulo) {

        Email from = new Email(remetente);
        Email to = new Email(candidato.getEmail());
        Content content = new Content("text/plain", "Prezado "+candidato.getNome()+", Nós do time BIKUBE gostariamos de informar que uma nova vaga para "+titulo+" foi aberta em nosso site, verificamos que " +
                "seu perfil atende aos requisitos exigidos na vaga e gostariamos de convida-lo para participar do processo seletivo! Caso possua interesse, " +
                "responda este email com uma mensagem de confirmação e logo uma pessoa de nosso time irá entrar em contato para mais informações, atenciosamente, time BIKUBE.");

        Mail mail = new Mail(from,"Nova oportunidade", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

    public String enviarComunicado(comunicado_model comunicado, funcionario_model funcionario) {

        Email from = new Email(remetente);
        Email to = new Email(funcionario.getEmail());
        Content content = new Content("text/plain", comunicado.getTexto());

        Mail mail = new Mail(from,"Comunicado BIKUBE", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }


    }

    public String enviarCodigoConfirmarEmailCandidato(String email, String codigo) {

        Email from = new Email(remetente);
        Email to = new Email(email);
        Content content = new Content("text/plain", "Olá, seu código para confirmação de email é: "+codigo+", este é um email automático e não precisa ser respondido, por favor, retorne para página de" +
                "confirmação de código e digite o código enviado. Time BIKUBE.");

        Mail mail = new Mail(from,"Codigo para confirmação de email", to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            return("Email enviado com sucesso!");

        } catch (Exception e) {
            return ("Erro ao enviar e-mail");
        }

    }

}
