package com.rh.api_rh.comunicado;

import com.rh.api_rh.DTO.aplicacao.comunicado.enviarComunicado_dto;
import com.rh.api_rh.comunicado.comunicado_funcionario.comunicado_funcionario_model;
import com.rh.api_rh.comunicado.comunicado_funcionario.comunicado_funcionario_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.util.email_service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.RollbackOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class comunicado_service {

    @Autowired
    private comunicado_repository comunicadorepository;

    @Autowired
    private email_service emailservice;

    @Autowired
    private funcionario_repository funcionariorepository;

    @Autowired
    private comunicado_funcionario_repository comunicadofuncionariorepository;

    @Transactional(rollbackOn = Exception.class)
    public String enviar(enviarComunicado_dto dto) {

        try {
            comunicado_model comunicado = new comunicado_model();
            comunicado.setTexto(dto.getTexto());
            comunicado.setTitulo(dto.getTitulo());
            comunicado.setDatacriacao(LocalDate.now());

            comunicadorepository.save(comunicado);

            for (funcionario_model funcionario : dto.getFuncionarios()) {

                String result = emailservice.enviarComunicado(comunicado, funcionario);
                if (!result.equals("Email enviado com sucesso!")) {
                    throw new RuntimeException();
                } else {

                    comunicado_funcionario_model intermediaria = new comunicado_funcionario_model();
                    intermediaria.setFuncionario(funcionario);
                    intermediaria.setComunicado(comunicado);
                    comunicadofuncionariorepository.save(intermediaria);

                }

            }

            return ("emails enviados com sucesso!");

        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public List<comunicado_funcionario_model> buscarComunicadosFuncionario(UUID id) {

        try {
            Optional<funcionario_model> funcionario = funcionariorepository.findById(id);
            if (funcionario.isPresent()) {

                List<comunicado_funcionario_model> comunicados = comunicadofuncionariorepository.findByFuncionario(funcionario.get());

                return comunicados;

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    public List<comunicado_model> listarComunicados() {
        return comunicadorepository.findAll();
    }

    public comunicado_model buscarComunicado(Long id) {

        Optional<comunicado_model> comunicado = comunicadorepository.findById(id);
        if (comunicado.isPresent()) {
            return comunicado.get();
        } else {
            return null;
        }


    }

    public String alterarVisto(Long id) {

        Optional<comunicado_funcionario_model> comunicado = comunicadofuncionariorepository.findById(id);
        if (comunicado.isPresent()) {
            comunicado.get().setVisto(true);
            comunicadofuncionariorepository.save(comunicado.get());
            return("ok");
        } else {
            return null;
        }

    }



}
