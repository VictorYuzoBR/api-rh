package com.rh.api_rh.util;

import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_model;
import com.rh.api_rh.espelho.espelho_repository;
import com.rh.api_rh.espelho.espelho_service;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class csv_service {

    @Autowired
    private espelho_service espelho_service;

    @Autowired
    private espelho_repository espelho_repository;

    @Autowired
    private funcionario_service funcionario_service;

    public byte[] gerarCSV(LocalDate datainicio) throws Exception {

        List<espelho_model> espelhos = espelho_repository.findByPeriodoInicio(datainicio);

        if (espelhos.isEmpty()) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zipOut = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {


            for  (espelho_model espelho : espelhos) {

                StringBuilder sb = new StringBuilder();

                sb.append("DATA;ENTRADA 1;ENTRADA 2;ENTRADA 3;ENTRADA 4;AUSENCIA;ABONO\n");


                for (espelho_item_model item : espelho.getListaEntradas()) {

                    StringBuilder entrada = new StringBuilder();

                    entrada.append(item.getData().toString());
                    entrada.append(";");

                    int numeroPontos = 0;

                    for (entrada_espelho_model ponto : item.getEntradas()) {

                        entrada.append(ponto.getHora().toString());
                        entrada.append(";");
                        numeroPontos++;

                    }

                    if (numeroPontos < 4 ) {

                        int dif= 4 - numeroPontos;

                        for (int i = 0; i < dif; i++) {

                            entrada.append(";");

                        }

                    }

                    if (item.isAusencia()) {
                        entrada.append("FALTA;");
                    } else {
                        entrada.append("PRESENTE;");
                    }

                    if (item.getDescricaoAbono() != null) {
                        entrada.append(item.getDescricaoAbono());
                    }

                    entrada.append("\r\n");

                    sb.append(entrada);


                }

                String nomecsv = espelho.getNomeFuncionario()+"_"+espelho.getRegistro()+"_"+espelho.getPeriodoInicio().toString()+".csv";
                ZipEntry  zipEntry = new ZipEntry(nomecsv);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                zipOut.closeEntry();


            }


        }

        byte[] zipBytes = baos.toByteArray();

        return zipBytes;


    }

    public byte[] gerarCSVdemissao(UUID idfuncionario) throws Exception {

        funcionario_model funcionario = funcionario_service.buscar(idfuncionario);

        String registro = funcionario.getIdusuario().getRegistro();


        List<espelho_model> espelhos = espelho_repository.findByRegistro(registro);

        if (espelhos.isEmpty()) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zipOut = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {


            for  (espelho_model espelho : espelhos) {

                StringBuilder sb = new StringBuilder();

                sb.append("DATA;ENTRADA 1;ENTRADA 2;ENTRADA 3;ENTRADA 4;AUSENCIA;ABONO\n");


                for (espelho_item_model item : espelho.getListaEntradas()) {

                    StringBuilder entrada = new StringBuilder();

                    entrada.append(item.getData().toString());
                    entrada.append(";");

                    int numeroPontos = 0;

                    for (entrada_espelho_model ponto : item.getEntradas()) {

                        entrada.append(ponto.getHora().toString());
                        entrada.append(";");
                        numeroPontos++;

                    }

                    if (numeroPontos < 4 ) {

                        int dif= 4 - numeroPontos;

                        for (int i = 0; i < dif; i++) {

                            entrada.append(";");

                        }

                    }

                    if (item.isAusencia()) {
                        entrada.append("FALTA;");
                    } else {
                        entrada.append("PRESENTE;");
                    }

                    if (item.getDescricaoAbono() != null) {
                        entrada.append(item.getDescricaoAbono());
                    }

                    entrada.append("\r\n");

                    sb.append(entrada);


                }

                String nomecsv = espelho.getNomeFuncionario()+"_"+espelho.getRegistro()+"_"+espelho.getPeriodoInicio().toString()+".csv";
                ZipEntry  zipEntry = new ZipEntry(nomecsv);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                zipOut.closeEntry();


            }


        }

        byte[] zipBytes = baos.toByteArray();

        return zipBytes;


    }




}


