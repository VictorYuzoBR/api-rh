package com.rh.api_rh.espelho;

import com.rh.api_rh.DTO.aplicacao.espelho.aplicarAtestado_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.descreverAbono_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarFeriado_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarPDF_dto;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.util.csv_service;
import com.rh.api_rh.util.pdf_service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/espelho")
@RequiredArgsConstructor
public class espelho_controller {

    private final espelho_service espelhoService;
    private final espelho_application_service espelhoApplicationService;
    private final pdf_service pdfService;
    private final token_service tokenService;
    private final csv_service csvService;

    @GetMapping
    public ResponseEntity<List<espelho_model>> listar() {

        try {
            List<espelho_model> lista = espelhoService.listar();

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/baterponto/{id}")
    public ResponseEntity<?> baterponto(@PathVariable UUID id) {

        try {
            espelho_item_model res = espelhoApplicationService.baterPonto(id);

                return ResponseEntity.ok(res);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<espelho_model>> espelhosFuncionario(@PathVariable UUID id) {

        try {
            List<espelho_model> res = espelhoService.retornarEspelhosFuncionario(id);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }

        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/abono")
    public ResponseEntity<?> descreverAbono(@RequestBody descreverAbono_dto dto) {

        try {
            espelho_item_model res = espelhoApplicationService.descreverAbono(dto);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/gerarFeriado")
    public ResponseEntity<String>  gerarFeriado(@RequestBody gerarFeriado_dto dto, HttpServletRequest request) {

        try {

            UUID idrh = UUID.fromString(tokenService.returnIdRh(request));

            String res = espelhoApplicationService.gerarFeriado(dto.getData(),idrh);
            if (!res.equals("feriado gerado com sucesso")) {
                return  ResponseEntity.badRequest().body(res);
            } else {
                return ResponseEntity.ok().body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    ///  retorna espelho do mes de um funcionario
    @GetMapping("/espelhoDoMes/{id}")
    public ResponseEntity<espelho_model> espelhoDoMes(@PathVariable UUID id) {

        try {
            espelho_model res = espelhoService.retornarEspelhoDoMesDoFuncionario(id);
            if  (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/gerarPDF")
    public void gerarPDF(@RequestBody gerarPDF_dto dto, HttpServletResponse response ) {

        try {

            byte[] conteudoPDF = pdfService.gerarPDF(dto);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Holerite.pdf\"");
            response.setContentLength(conteudoPDF.length);

            OutputStream os = response.getOutputStream();
            os.write(conteudoPDF);
            os.flush();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/gerarCSV/{datainicio}")
    public void gerarCSV(@PathVariable LocalDate datainicio,  HttpServletResponse response ) {

        try {

            byte[] conteudoCSV = csvService.gerarCSV(datainicio);


            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"espelhos.zip\"");
            response.setContentLength(conteudoCSV.length);

            OutputStream os = response.getOutputStream();
            os.write(conteudoCSV);
            os.flush();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }




    @GetMapping("/data")
    public String getCurrentTime() {
        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        String currentTime = Instant.now().atZone(zone).toString();
        return currentTime;
    }

    @PutMapping("/atestado")
    public ResponseEntity<?> aplicarAtestado(@RequestBody aplicarAtestado_dto dto) {

        try {

            funcionario_model res =  espelhoApplicationService.aplicarAtestado(dto);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().body("funcionario não encontrado");
            }

        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }



    }




