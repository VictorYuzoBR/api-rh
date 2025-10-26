package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.atualizarFerias_dto;
import com.rh.api_rh.DTO.aplicacao.ferias.feriasPorSetor_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarFerias_dto;
import com.rh.api_rh.infra.security.token_service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ferias")
@RequiredArgsConstructor
public class ferias_controller {

    private final ferias_service ferias_service;
    private final ferias_application_service ferias_application_service;
    private final token_service token_service;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastrarFerias_dto dto) {

        try {

            return ferias_service.cadastrar(dto);

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody atualizarFerias_dto dto, HttpServletRequest request) {

        try {

            UUID idrh = UUID.fromString(token_service.returnIdRh(request));


            String res = ferias_service.atualizar(dto, idrh);
            if (res.equals("ferias atualizado com sucesso")) {
                return ResponseEntity.ok().body(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    @GetMapping("/feriasporsetor/{mes}")
    public ResponseEntity<List<feriasPorSetor_dto>> listarFeriasPorSetor(@PathVariable int mes) {

        try {
            List<feriasPorSetor_dto> res = ferias_application_service.feriasPorSetor(mes);
            return ResponseEntity.ok().body(res);
        }  catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<ferias_model>> listarFeriasFuncionario(@PathVariable UUID id) {

        try {

            List<ferias_model> lista = ferias_service.listarTodosFuncionario(id);
            return ResponseEntity.ok().body(lista);

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/feriasConflitantes/{id}")
    public ResponseEntity<?> listarFeriasConflitantes(@PathVariable Long id) {

        try {
            List<ferias_model> lista = ferias_application_service.feriasConflitantes(id);
            if (lista == null) {
                return ResponseEntity.badRequest().build();
            }
            if (lista.isEmpty()) {
                return ResponseEntity.ok().body("Não há ferias conflitantes para está solicitacao");
            } else {
                return ResponseEntity.ok().body(lista);
            }

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping
    public ResponseEntity<List<ferias_model>> listarSolicitacoesPendentes() {

        try {
            List<ferias_model> lista = ferias_service.listarTodosSolicitado();
            return ResponseEntity.ok().body(lista);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /// apenas para mostrar que a logica funciona
    @PostMapping("/testelogica")
    public ResponseEntity<String> finalizarFeriasTesteLogica(@RequestParam Long id) {

        try {

            String res = ferias_application_service.finalizarFeriasTesteLogica(id);
            if (res != null) {
                return ResponseEntity.ok().body(res);
            } else {
                return  ResponseEntity.badRequest().build();
            }

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /// apenas para mostrar que a logica funciona
    @GetMapping("/gerarEspelhosFalsos")
    public ResponseEntity<String>  gerarEspelhosFalsos() {

        try {
            ferias_application_service.gerarEspelhosFalsos();
            return ResponseEntity.ok().body("gerado");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /// apenas para mostrar que a logica funciona
    @GetMapping("/calcularFeriasFalso")
    public ResponseEntity<String>  calcularFeriasFalso() {

        try {
            ferias_application_service.calcularFeriasApenasLogica();
            return ResponseEntity.ok().body("calculado");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
