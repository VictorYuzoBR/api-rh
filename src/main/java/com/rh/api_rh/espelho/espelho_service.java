package com.rh.api_rh.espelho;

import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_repository;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class espelho_service {

    @Autowired
    private espelho_repository espelhoRepository;

    @Autowired
    private espelho_item_repository espelhoItemRepository;

    @Autowired
    private entrada_espelho_repository entradaEspelhoRepository;

    @Autowired
    private funcionario_repository funcionarioRepository;

    public List<espelho_model> listar() {
        return espelhoRepository.findAll();
    }

    public espelho_model buscarPorId(Long id) {

        Optional<espelho_model> optional = espelhoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }  else {
            return null;
        }

    }

    public espelho_item_model buscarItemPorId(Long id) {

        Optional<espelho_item_model> optional = espelhoItemRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }   else {
            return null;
        }

    }

    public entrada_espelho_model buscarEntradaPorId(Long id) {

        Optional<entrada_espelho_model> optional = entradaEspelhoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else  {
            return null;
        }

    }

    public List<espelho_model> retornarEspelhosFuncionario(UUID idfuncionario) {

        Optional<funcionario_model> funcionario = funcionarioRepository.findById(idfuncionario);
        if  (funcionario.isPresent()) {

            String registro = funcionario.get().getIdusuario().getRegistro();

            List<espelho_model> espelhosFuncionario = new ArrayList<>();
            espelhosFuncionario = espelhoRepository.findByRegistro(registro);

            return  espelhosFuncionario;

        } else {
            return null;
        }


    }

}
