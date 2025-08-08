package com.rh.api_rh.funcionario.endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class endereco_service {

    @Autowired
    private endereco_repository endereco_repository;

    public String cadastrar(endereco_model endereco) {

        try{
            endereco_repository.save(endereco);
            return "Cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao cadastrarParaCandidato endereco!";
        }
    }

    public List<endereco_model> listar() {
        try {
            return endereco_repository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public void deletar(endereco_model endereco) {
        endereco_repository.delete(endereco);
    }

}
