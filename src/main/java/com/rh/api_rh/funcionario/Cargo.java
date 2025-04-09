package com.rh.api_rh.funcionario;

public enum Cargo {

    ADMIN("ADMIN"),

    RH("RH"),

    FUNCIONARIO("FUNCIONARIO");

    private String cargo;

    Cargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }


}
