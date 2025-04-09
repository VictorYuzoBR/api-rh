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

    public usuario_model criar() {

        usuario_model usuario = new usuario_model();
        UUID id = UUID.randomUUID();
        String senha = registro_service.gerarsenhaaleatoria();
        String registro = registro_service.gerarregistro();
        String senhahash = new BCryptPasswordEncoder().encode(senha);
        System.out.print(senha);

        usuario.setId(id);
        usuario.setSenha(senhahash);
        usuario.setRegistro(registro);

        registrar(usuario);
        return usuario;

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

}
