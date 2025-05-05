package com.rh.api_rh.funcionario;

import com.rh.api_rh.DTO.atualizarfuncionario_dto;
import com.rh.api_rh.endereco.endereco_mapper;
import com.rh.api_rh.endereco.endereco_service;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_repository;
import com.rh.api_rh.setor.setor_service;
import com.rh.api_rh.telefone.telefone_mapper;
import com.rh.api_rh.telefone.telefone_model;
import com.rh.api_rh.telefone.telefone_repository;
import com.rh.api_rh.telefone.telefone_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class funcionario_service {

    @Autowired
    private funcionario_repository funcionario_repository;
    @Autowired
    private endereco_service endereco_service;
    @Autowired
    private telefone_service telefone_service;
    @Autowired
    private com.rh.api_rh.usuario.usuario_repository usuario_repository;
    @Autowired
    private setor_service setor_service;
    @Autowired
    private telefone_repository telefone_repository;


    public String cadastrar(funcionario_model funcionario) {

        try {

            funcionario_repository.save(funcionario);
            return "Cadastrado com sucesso!";
        } catch (Exception e) {

            telefone_service.excluir(funcionario.getId_telefone());
            endereco_service.deletar(funcionario.getId_endereco());
            return "Erro ao cadastrar funcionario.";
        }

    }

    public List<funcionario_model> listar() {

        return funcionario_repository.findAll();

    }

    public String excluir(funcionario_model funcionario) {
        funcionario_repository.delete(funcionario);
        telefone_service.excluir(funcionario.getId_telefone());
        endereco_service.deletar(funcionario.getId_endereco());
        return "Excluido com sucesso!";

    }

    public funcionario_model buscar(UUID id) {

        try {
            Optional<funcionario_model> funcionario = funcionario_repository.findById(id);
            if (funcionario.isPresent()) {
                return funcionario.get();
            } else {
                throw new RuntimeException("Funcionário não encontrado com o id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o funcionário", e);
        }

    }

    public String atualizar(atualizarfuncionario_dto dto) {

        try {
            Optional<funcionario_model> data = funcionario_repository.findByEmail(dto.getEmail());
            if (data.isPresent()) {
                funcionario_model funcionario = data.get();

                funcionario.setContabancaria(dto.getContabancaria());
                funcionario.setEmail(dto.getEmail());
                funcionario.setSalario(dto.getSalario());
                funcionario.setFuncao(dto.getFuncao());

                setor_model setor = setor_service.pesquisa(dto.getIdsetor());
                if (setor != null) {
                    funcionario.setId_setor(setor);
                } else {
                    return "Setor inexistente";
                }

                telefone_model telefone = telefone_service.buscar(dto.getTelefone());
                if (telefone != null) {
                    try {
                        telefone.setNumero(dto.getTelefone());
                        telefone_repository.save(telefone);
                    } catch (Exception e) {
                        return "Erro ao atualizar telefone";
                    }
                }

                funcionario_repository.save(funcionario);
                return funcionario.getIdusuario().getRegistro();


            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Um erro inesperado!";
    }

}
