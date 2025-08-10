package com.rh.api_rh.DTO.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class quantidadePessoasEtapa_dto {

    private Map<String, Integer> etapas;

    public quantidadePessoasEtapa_dto() {
        this.etapas = new HashMap<>();
    }

    public Map<String, Integer> getEtapas() {
        return etapas;
    }

    public void setEtapas(Map<String, Integer> etapas) {
        this.etapas = etapas;
    }

}
