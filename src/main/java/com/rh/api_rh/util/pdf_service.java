package com.rh.api_rh.util;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarPDF_dto;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_model;
import com.rh.api_rh.espelho.espelho_service;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
public class pdf_service {

    @Autowired
    private espelho_service espelho_service;

    @Autowired
    private funcionario_repository funcionario_repository;

    public byte[] gerarPDF(gerarPDF_dto dto) throws Exception {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){

            espelho_model espelho = espelho_service.buscarPorId(dto.getIdespelho());

            if (espelho == null) {
                throw new RuntimeException();
            }

            int mes = espelho.getPeriodoFim().getMonthValue();
            int ano = espelho.getPeriodoFim().getYear();

            String data = String.valueOf(mes) + "/" +  String.valueOf(ano);

            Optional<funcionario_model> funcionariodata =  funcionario_repository.findByIdusuario_Registro(espelho.getRegistro());
            if (funcionariodata.isEmpty()) {
                throw new RuntimeException();
            } else {
                funcionario_model funcionario = funcionariodata.get();

                ClassPathResource resource = new ClassPathResource("modelo/template.pdf");
                InputStream templateInputStream = resource.getInputStream();

                PdfReader reader = new PdfReader(templateInputStream);
                PdfStamper stamper = new PdfStamper(reader, baos);
                AcroFields form = stamper.getAcroFields();

                form.setField("Text1",espelho.getEmpresa());
                form.setField("Text2",espelho.getEndereco());
                form.setField("Text3",espelho.getBairro());
                form.setField("Text4",espelho.getCnpj());
                form.setField("Text5",espelho.getCep());
                form.setField("Text6",espelho.getRegistro());
                form.setField("Text7",espelho.getNomeFuncionario());
                form.setField("Text8",espelho.getFuncao());
                form.setField("Text9",data);

                int inicio = 10;

                for (espelho_item_model item : espelho.getListaEntradas()) {

                    form.setField("Text"+String.valueOf(inicio), item.getData().toString());

                    inicio++;

                    int numeroEntradas = 4;

                    for (entrada_espelho_model entradas : item.getEntradas()) {

                        form.setField("Text"+String.valueOf(inicio), entradas.getHora().toString());
                        inicio++;
                        numeroEntradas = numeroEntradas - 1;

                    }

                    if (numeroEntradas > 0) {

                        for (int j  = 0; j < numeroEntradas; j++) {
                            form.setField("Text"+String.valueOf(inicio), "   ");
                            inicio++;
                        }

                    }

                    if (item.isAusencia()) {
                        form.setField("Text"+String.valueOf(inicio), "falta");
                        inicio++;
                    } else {
                        form.setField("Text"+String.valueOf(inicio), "presente");
                        inicio++;
                    }

                    if (item.getDescricaoAbono() != null) {
                        form.setField("Text"+String.valueOf(inicio), item.getDescricaoAbono());
                        inicio++;
                    } else {
                        form.setField("Text"+String.valueOf(inicio), "   ");
                        inicio++;
                    }



                }



                stamper.setFormFlattening(true);
                stamper.close();
                reader.close();

                return baos.toByteArray();
            }





        } catch (Exception e) {
            throw new Exception(e);
        }

    }

}
