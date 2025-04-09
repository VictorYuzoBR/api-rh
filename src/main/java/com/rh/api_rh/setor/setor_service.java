package com.rh.api_rh.setor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class setor_service {

    @Autowired
    private setor_repository setor_repository;

    public setor_model pesquisa(Long id) {

        try {
            Optional<setor_model> setor = setor_repository.findById(id);

            if (setor.isPresent()) {
                return setor.get();
            } else {
                throw new RuntimeException("Setor não encontrado com o id: " + id);
            }
        } catch (Exception e) {

            throw new RuntimeException("Erro ao buscar o setor", e);

        }

    }


    public String cadastrar(setor_model setor) {

        try {
            setor_repository.save(setor);
            return "Cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao cadastrar o setor";
        }

    }

    public List<setor_model> listar() {
        try {
            return setor_repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar o setor", e);
        }
    }


}
