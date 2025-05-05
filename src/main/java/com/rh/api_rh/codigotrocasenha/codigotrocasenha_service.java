package com.rh.api_rh.codigotrocasenha;

import com.rh.api_rh.util.registro_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class codigotrocasenha_service {

    @Autowired
    codigotrocasenha_repository repo;
    @Autowired
    registro_service registro_service;

    public String cadastrar(UUID iduser) {

        codigotrocasenha_model filter = repo.findByIdusuario(iduser);
        if (filter != null) {
            repo.delete(filter);
        }

        String codigo = registro_service.gerarcodigotrocasenha();
        Date data = new Date();
        codigotrocasenha_model codigotrocasenha = new codigotrocasenha_model();
        codigotrocasenha.setCodigo(codigo);
        codigotrocasenha.setIdusuario(iduser);
        codigotrocasenha.setData(data);
        repo.save(codigotrocasenha);

        return(codigo);

    }

    public codigotrocasenha_model buscar(UUID codigo) {
        codigotrocasenha_model codigotrocasenha = repo.findByIdusuario(codigo);

        return(codigotrocasenha);
    }

    public String deletar(UUID codigo) {

        codigotrocasenha_model codigotrocasenha = repo.findByIdusuario(codigo);
        repo.delete(codigotrocasenha);
        return("Deletado com sucesso");
    }

    public List<codigotrocasenha_model> listar() {

        return(repo.findAll());
    }

    /// SE AINDA NÃO TIVER PASSADO 30 SEGUNDOS PARA TESTES RETORNA TRUE POIS O CÓDIGO AINDA ESTÁ VALIDO
    public boolean validartempo(codigotrocasenha_model codigotrocasenha) {

        Date datacodigo = codigotrocasenha.getData();
        Date dataagora = new Date();
        if (dataagora.getTime() - datacodigo.getTime() <  30000) {
            repo.delete(codigotrocasenha);
            return true;
        } else {
            return false;
        }

    }

    public codigotrocasenha_model validarcodigo(UUID idusuario, String codigo) {

        codigotrocasenha_model codigobanco = repo.findByIdusuario(idusuario);
        if (codigobanco != null) {
            if (codigobanco.getCodigo().equals(codigo)) {
                return codigobanco;
            } else {
                return null;
            }
        } else {
            return null;
        }


    }

}
