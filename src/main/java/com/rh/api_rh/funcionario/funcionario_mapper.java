package com.rh.api_rh.funcionario;

import com.rh.api_rh.DTO.cadastro_dto;
import com.rh.api_rh.DTO.emailnotificarcadastro_dto;
import com.rh.api_rh.funcionario.endereco.endereco_mapper;
import com.rh.api_rh.funcionario.endereco.endereco_model;
import com.rh.api_rh.funcionario.endereco.endereco_service;
import com.rh.api_rh.funcionario.telefone.telefone_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_service;
import com.rh.api_rh.funcionario.telefone.telefone_mapper;
import com.rh.api_rh.funcionario.telefone.telefone_model;
import com.rh.api_rh.usuario.usuario_model;
import com.rh.api_rh.usuario.usuario_service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class funcionario_mapper {


    final telefone_mapper telefoneMapper;
    final endereco_mapper enderecoMapper;
    final telefone_service telefoneService;
    final endereco_service enderecoService;
    final setor_service setorService;
    final usuario_service usuarioService;




    public emailnotificarcadastro_dto convert(cadastro_dto dto) {

        /// Nesta parte primeiro precisamos criar as entidades telefone e endereco, e também pesquisar a entidade setor para adicionar ao funcionario
        setor_model setor = setorService.pesquisa(Long.parseLong(dto.getNumerosetor()));

        telefone_model telefone = telefoneMapper.convert(dto);
        endereco_model endereco = enderecoMapper.convert(dto);

        telefoneService.cadastrar(telefone);
        enderecoService.cadastrar(endereco);

        usuarioprovisorio provisorio = usuarioService.criar();

        usuario_model usuario = usuarioService.buscar(provisorio.getId());


        funcionario_model funcionario = new funcionario_model();
        UUID id = UUID.randomUUID();
        funcionario.setId(id);
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCpf(dto.getCpf());
        funcionario.setCargo(dto.getCargo());
        funcionario.setContabancaria(dto.getContabancaria());
        funcionario.setDataentrada(dto.getDataentrada());
        funcionario.setSalario(dto.getSalario());
        funcionario.setData_nascimento(dto.getData_nascimento());
        funcionario.setFuncao(dto.getFuncao());

        funcionario.setId_setor(setor);
        funcionario.setId_telefone(telefone);
        funcionario.setId_endereco(endereco);
        funcionario.setIdusuario(usuario);

        emailnotificarcadastro_dto res = new emailnotificarcadastro_dto();
        res.setFuncionario(funcionario);
        res.setProvisorio(provisorio);

        return res;

    }

}
