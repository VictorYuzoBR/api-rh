package com.rh.api_rh.funcionario;

import com.rh.api_rh.DTO.atualizarfuncionario_dto;
import com.rh.api_rh.DTO.cadastro_dto;
import com.rh.api_rh.DTO.emailnotificarcadastro_dto;
import com.rh.api_rh.endereco.endereco_mapper;
import com.rh.api_rh.endereco.endereco_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_repository;
import com.rh.api_rh.setor.setor_service;
import com.rh.api_rh.telefone.telefone_mapper;
import com.rh.api_rh.telefone.telefone_model;
import com.rh.api_rh.telefone.telefone_repository;
import com.rh.api_rh.telefone.telefone_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.rh.api_rh.util.email_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Autowired
    private funcionario_mapper funcionario_mapper;
    @Autowired
    private email_service email_service;
    @Autowired
    private log_repository log_repository;

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

    public String aceitarTermo(String email) {

        try {
            Optional<funcionario_model> data = funcionario_repository.findByEmail(email);
            if (data.isPresent()) {
                funcionario_model funcionario = data.get();
                funcionario.getIdusuario().setAceitoutermos(true);
                funcionario_repository.save(funcionario);
                return ("Atualizado com sucesso!");
            } else {
                return ("Deu ruim");
            }
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String generateadmin() {

        cadastro_dto dto = new cadastro_dto();
        dto.setNome("Adrian");
        dto.setFuncao("DBA");
        dto.setData_nascimento("01/01/2001");
        dto.setCpf("145.145.14.45");
        dto.setEmail("victoryuzofb4@gmail.com");
        Cargo cargo = Cargo.ADMIN;
        dto.setCargo(cargo);
        dto.setSalario(20000.00f);
        dto.setContabancaria("56165156");
        dto.setDataentrada("02/02/2002");
        dto.setCep("6546565");
        dto.setLogradouro("rua do adrian");
        dto.setBairro("bairro do adrian");
        dto.setCidade("cidade do adrian");
        dto.setEstado("estado do adrian");
        dto.setNumero("154");
        dto.setComplemento("teste");
        dto.setNumerotelefone("45545");
        dto.setNumerosetor("1");

        emailnotificarcadastro_dto dados  = funcionario_mapper.convert(dto);
        funcionario_model funcionario = dados.getFuncionario();
        usuarioprovisorio provisorio = dados.getProvisorio();

        try {
            String resposta = cadastrar(funcionario);
            if (resposta.equals("Cadastrado com sucesso!")) {
                email_service.enviarcadastro(funcionario.getEmail(), provisorio);

                log_model log = new log_model();
                log.setRegistro(funcionario.getIdusuario().getRegistro());
                log.setAcao("Novo funcionário cadastrado no sistema com registro: "+funcionario.getIdusuario().getRegistro());
                log.setData(new Date());
                log_repository.save(log);

                return ("Cadastrado com sucesso!");
            } else {
                return ("Erro ao cadastrar funcionario!");
            }
        } catch (Exception e) {
        return e.getMessage();}
    }





}
