package com.rh.api_rh.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.rh.api_rh.util.registro_service;

import java.util.List;
import java.util.UUID;

@Service
public class usuario_service {

    @Autowired
    private registro_service registro_service;
    @Autowired
    private usuario_repository usuario_repository;

    public usuarioprovisorio criar() {

        usuarioprovisorio provisorio = new usuarioprovisorio();
        usuario_model usuario = new usuario_model();
        UUID id = UUID.randomUUID();
        String senha = registro_service.gerarsenhaaleatoria();
        String registro = registro_service.gerarregistro();

        provisorio.setSenha(senha);
        provisorio.setRegistro(registro);
        provisorio.setId(id);

        String senhahash = new BCryptPasswordEncoder().encode(senha);
        System.out.print(senha);

        usuario.setId(id);
        usuario.setSenha(senhahash);
        usuario.setRegistro(registro);

        registrar(usuario);
        return provisorio;

    }

    public String registrar(usuario_model usuario) {

        try {
            usuario_repository.save(usuario);
            return "Usuario cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao cadastrar usuario!";
        }

    }

    public List<usuario_model> listar() {
        try {
            return usuario_repository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public usuario_model buscar(UUID id) {
        try {
            return usuario_repository.findById(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    public String trocasenha(String senha, UUID id) {

        usuario_model usuario = buscar(id);
        String senhaoriginal = usuario.getSenha();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(senha, senhaoriginal)) {
            return("A senha nova não pode ser igual a antiga");
        } else {

            String senhahash = new BCryptPasswordEncoder().encode(senha);
            usuario.setSenha(senhahash);
            usuario_repository.save(usuario);
            return("A senha atualizada com sucesso!");

        }

    }

}
