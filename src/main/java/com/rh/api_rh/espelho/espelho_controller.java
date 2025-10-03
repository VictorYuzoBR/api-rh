package com.rh.api_rh.espelho;

import com.rh.api_rh.DTO.aplicacao.espelho.descreverAbono_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarFeriado_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/espelho")
@RequiredArgsConstructor
public class espelho_controller {

    private final espelho_service espelhoService;
    private final espelho_application_service espelhoApplicationService;

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
            String res = espelhoApplicationService.baterPonto(id);
            if (res.equals("ponto criado com sucesso")) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }
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
            String res = espelhoApplicationService.descreverAbono(dto);
            if (res.equals("sucesso")) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/gerarFeriado")
    public ResponseEntity<String>  gerarFeriado(@RequestBody gerarFeriado_dto dto) {

        try {
            String res = espelhoApplicationService.gerarFeriado(dto.getData());
            if (!res.equals("feriado gerado com sucesso")) {
                return  ResponseEntity.badRequest().body(res);
            } else {
                return ResponseEntity.ok().body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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





}
