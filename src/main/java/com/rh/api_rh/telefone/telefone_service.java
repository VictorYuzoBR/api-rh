package com.rh.api_rh.telefone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class telefone_service {

    @Autowired
    private telefone_repository telefone_repository;

    public String cadastrar(telefone_model telefone) {

        try{
            telefone_repository.save(telefone);
            return "Cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao cadastrar telefone!";
        }
    }

    public List<telefone_model> listar() {
        try {
            return telefone_repository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public void excluir(telefone_model telefone) {
        telefone_repository.delete(telefone);
    }

}
