package com.rh.api_rh.funcionario.telefone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class telefone_service {

    @Autowired
    private telefone_repository telefone_repository;

    public String cadastrar(telefone_model telefone) {

        try{
            telefone_repository.save(telefone);
            return "Cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao cadastrarParaCandidato telefone!";
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

    public telefone_model buscar(String numero) {
        try {
            Optional<telefone_model> data = telefone_repository.findByNumero(numero);
            if (data.isPresent()) {
            return data.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

}
