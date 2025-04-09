package com.rh.api_rh.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class registro_service {

    private final String[] numeros = {"1","2","3","4","5","6","7","8","9"};
    private final String[] alfabeto = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private final Random rand = new Random();

    public String gerarregistro() {

        String registro = "";
        for (int i = 0; i < 6; i++) {

            int letra = rand.nextInt(alfabeto.length);
            registro = registro + alfabeto[letra];

        }

        for (int i = 0; i < 2; i++) {

            int numero = rand.nextInt(numeros.length);
            registro = registro + numeros[numero];

        }

        return registro;

    }

    public String gerarsenhaaleatoria() {

        String senha = "";

        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < 2; j++) {

                int letra = rand.nextInt(alfabeto.length);
                senha = senha + alfabeto[letra];

            }

            for (int y = 0; y < 2; y++) {

                int numero = rand.nextInt(numeros.length);
                senha = senha + numeros[numero];

            }

        }

        return senha;
    }

}
