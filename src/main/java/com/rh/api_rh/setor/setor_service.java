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


    public setor_model cadastrar(setor_model setor) {

        try {

            Optional<setor_model> jaexiste =  setor_repository.findByNome(setor.getNome());
            if (jaexiste.isPresent()) {
                return null;
            }

            setor_repository.save(setor);
            return setor;
        } catch (Exception e) {
            throw  new RuntimeException("Erro ao cadastrar o setor", e);
        }

    }

    public List<setor_model> listar() {
        try {
            return setor_repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar o setor", e);
        }
    }

    public String deletar(Long id) {
        try {
            Optional<setor_model> setor = setor_repository.findById(id);
            if (setor.isPresent()) {
                setor_repository.delete(setor.get());
                return "Deletado com sucesso!";
            } else {
                return "Erro ao deletar o setor";
            }

        }  catch (Exception e) {
            throw new RuntimeException("Erro ao deletar o setor", e);
        }
    }


}
