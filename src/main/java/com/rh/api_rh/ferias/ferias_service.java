package com.rh.api_rh.ferias;

import com.rh.api_rh.DTO.aplicacao.ferias.atualizarFerias_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarFerias_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.funcionario.funcionario_service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ferias_service {


    @Autowired
    private funcionario_service funcionario_service;

    @Autowired
    private ferias_repository ferias_repository;

    @Autowired
    private funcionario_repository funcionario_repository;


    public ResponseEntity<String> cadastrar(cadastrarFerias_dto dto) {

        try {

            LocalDate hoje = LocalDate.now();

            funcionario_model funcionario = funcionario_service.buscar(dto.getIdfuncionario());

            if (funcionario.getFeriasDisponiveis() == 0) {
                return ResponseEntity.badRequest().body("sem saldo de férias disponível");
            }

            if (funcionario.getFracoesDisponiveis() == 0) {
                String res = "3 frações de férias ja foram utilizadas";
                if (funcionario.getFeriasDisponiveis() > 0) {
                    res = res + ", converse com um funcionário de recursos humanos para negociar os dias restantes";
                }
                return ResponseEntity.badRequest().body(res);
            }

            Optional<ferias_model> jaexiste = ferias_repository.findByFuncionarioAndStatus(funcionario, "andamento");
            Optional<ferias_model> jaexiste2 = ferias_repository.findByFuncionarioAndStatus(funcionario, "solicitado");
            Optional<ferias_model> jaexiste3 = ferias_repository.findByFuncionarioAndStatus(funcionario, "aprovado");
            if (jaexiste.isPresent() || jaexiste2.isPresent() || jaexiste3.isPresent()) {

                return ResponseEntity.badRequest().body("você já possui solicitação em aberto ou em andamento");

            } else {

                LocalDate umdia = dto.getDataInicio().plusDays(1);
                LocalDate doisdias = dto.getDataInicio().plusDays(2);

                String umdiastring = umdia.getDayOfWeek().toString();
                String doisdiasstring = doisdias.getDayOfWeek().toString();

                if (dto.getDataInicio().isBefore(hoje)) {
                    return ResponseEntity.badRequest().body("data de inicio requisitada é retroativa");
                }




                if (umdiastring.equals("SATURDAY") || umdiastring.equals("SUNDAY") || doisdiasstring.equals("SUNDAY") || doisdiasstring.equals("SATURDAY")) {
                    return ResponseEntity.badRequest().body("solicitação não pode começar 1 ou 2 dias antes de um final de semana");
                }

                List<LocalDate> feriados = buscarFeriados(dto.getDataInicio().getYear());

                for (LocalDate date : feriados) {

                    LocalDate umdiaantes = date.minusDays(1);
                    LocalDate doisdiaantes = date.minusDays(2);


                    if (umdiaantes.equals(dto.getDataInicio())  || doisdiaantes.equals(dto.getDataInicio())) {
                        return ResponseEntity.badRequest().body("solicitação não pode começar 1 ou 2 dias antes de um feriado nacional");
                    }

                }


                Long dias = (ChronoUnit.DAYS.between(dto.getDataInicio(), dto.getDataFim()));
                dias = dias + 1;


                LocalDate aposUmAno = funcionario.getUltimoCalculo().plusDays(1);
                LocalDate ultimoDiaPossivelParaEssePeriodo = aposUmAno.minusDays(dias);

                if (dto.getDataInicio().isAfter(ultimoDiaPossivelParaEssePeriodo)) {
                    return ResponseEntity.badRequest().body("sua solicitação precisa terminar antes de um periodo de 1 ano desde a aquisição de seu saldo");
                }


                if (dias > funcionario.getFeriasDisponiveis()) {
                    return ResponseEntity.badRequest().body("periodo solicitado é maior que o saldo disponível");
                }

                if (dias < 5) {
                    return ResponseEntity.badRequest().body("uma fração de férias não pode possuir menos que 5 dias");
                }

                Long dias2 = (ChronoUnit.DAYS.between(hoje, dto.getDataInicio()));
                dias2  = dias2 + 1;
                if (dias2 < 30) {
                    return ResponseEntity.badRequest().body("uma solicitação deve ser feita com no minimo 30 dias de antecedência");
                }

                Long diasRestantesCasoAprovado = Long.valueOf(funcionario.getFeriasDisponiveis()) - dias;

                if (!funcionario.isPeriodo14dias()) {

                    if (funcionario.getFracoesDisponiveis() == 1 ) {

                        if (dias < 14) {
                            return ResponseEntity.badRequest().body("esta é sua ultima fração de férias e é necesseário que seja um período igual ou maior à 14 dias");
                        } else if (dias >= 14 && diasRestantesCasoAprovado > 0) {

                            ferias_model novaferias = new ferias_model();
                            novaferias.setFuncionario(funcionario);
                            novaferias.setStatus("solicitado");
                            novaferias.setDataInicio(dto.getDataInicio());
                            novaferias.setDataFim(dto.getDataFim());
                            novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                            novaferias.setDiasParaDescontar(dias.intValue());
                            novaferias.setAlterar14dias(true);
                            ferias_repository.save(novaferias);



                            return ResponseEntity.ok().body("AVISO essa solicitação foi enviada porém você ficará com saldo de férias que não poderá mais ser utilizado em caso de aprovação, em caso de erro entre em contato com um funcionário de recursos humanos para alteração");
                        } else {
                            ferias_model novaferias = new ferias_model();
                            novaferias.setFuncionario(funcionario);
                            novaferias.setStatus("solicitado");
                            novaferias.setDataInicio(dto.getDataInicio());
                            novaferias.setDataFim(dto.getDataFim());
                            novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                            novaferias.setDiasParaDescontar(dias.intValue());
                            novaferias.setAlterar14dias(true);
                            ferias_repository.save(novaferias);


                            return ResponseEntity.ok().body("solicitação enviada com sucesso");



                        }

                    }

                    if (funcionario.getFracoesDisponiveis() == 2) {

                        if (dias < 14) {

                            if (diasRestantesCasoAprovado < 14) {
                                return ResponseEntity.badRequest().body("esta solicitação é inválida pois você ainda não teve um período igual ou maior a 14 dias de férias e o saldo disponível restante após esta solicitação seria menor que 14 dias");
                            } else {
                                ferias_model novaferias = new ferias_model();
                                novaferias.setFuncionario(funcionario);
                                novaferias.setStatus("solicitado");
                                novaferias.setDataInicio(dto.getDataInicio());
                                novaferias.setDataFim(dto.getDataFim());
                                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                                novaferias.setDiasParaDescontar(dias.intValue());
                                ferias_repository.save(novaferias);


                                return ResponseEntity.ok().body("solicitação enviada com sucesso");



                            }

                        } else {

                            if (diasRestantesCasoAprovado < 5 && diasRestantesCasoAprovado > 0) {

                                ferias_model novaferias = new ferias_model();
                                novaferias.setFuncionario(funcionario);
                                novaferias.setStatus("solicitado");
                                novaferias.setDataInicio(dto.getDataInicio());
                                novaferias.setDataFim(dto.getDataFim());
                                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                                novaferias.setDiasParaDescontar(dias.intValue());
                                novaferias.setAlterar14dias(true);
                                ferias_repository.save(novaferias);


                                return ResponseEntity.ok().body("AVISO essa solicitação foi enviada porém você ficará com saldo de férias que não poderá " +
                                        "mais ser utilizado em caso de aprovação pois seu saldo restante será menor que 5 dias, em caso de erro entre em contato com um funcionário de recursos humanos para alteração");

                            } else {

                                ferias_model novaferias = new ferias_model();
                                novaferias.setFuncionario(funcionario);
                                novaferias.setStatus("solicitado");
                                novaferias.setDataInicio(dto.getDataInicio());
                                novaferias.setDataFim(dto.getDataFim());
                                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                                novaferias.setDiasParaDescontar(dias.intValue());
                                novaferias.setAlterar14dias(true);
                                ferias_repository.save(novaferias);



                                return ResponseEntity.ok().body("solicitação enviada com sucesso");

                            }

                        }

                    }

                    if (funcionario.getFracoesDisponiveis() == 3) {

                        if (dias > 14) {

                            if (diasRestantesCasoAprovado < 5 && diasRestantesCasoAprovado > 0) {

                                ferias_model novaferias = new ferias_model();
                                novaferias.setFuncionario(funcionario);
                                novaferias.setStatus("solicitado");
                                novaferias.setDataInicio(dto.getDataInicio());
                                novaferias.setDataFim(dto.getDataFim());
                                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                                novaferias.setDiasParaDescontar(dias.intValue());
                                novaferias.setAlterar14dias(true);
                                ferias_repository.save(novaferias);



                                return ResponseEntity.ok().body("AVISO essa solicitação foi enviada porém você ficará com saldo de férias que não poderá " +
                                        "mais ser utilizado em caso de aprovação pois seu saldo restante será menor que 5 dias, em caso de erro entre em contato com um funcionário de recursos humanos para alteração");

                            } else {

                                ferias_model novaferias = new ferias_model();
                                novaferias.setFuncionario(funcionario);
                                novaferias.setStatus("solicitado");
                                novaferias.setDataInicio(dto.getDataInicio());
                                novaferias.setDataFim(dto.getDataFim());
                                novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                                novaferias.setDiasParaDescontar(dias.intValue());
                                novaferias.setAlterar14dias(true);
                                ferias_repository.save(novaferias);

                                buscarFeriados(2025);

                                return ResponseEntity.ok().body("solicitação enviada com sucesso");



                            }


                        } else {
                            ferias_model novaferias = new ferias_model();
                            novaferias.setFuncionario(funcionario);
                            novaferias.setStatus("solicitado");
                            novaferias.setDataInicio(dto.getDataInicio());
                            novaferias.setDataFim(dto.getDataFim());
                            novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                            novaferias.setDiasParaDescontar(dias.intValue());
                            ferias_repository.save(novaferias);
                            return ResponseEntity.ok().body("solicitação enviada com sucesso");
                        }

                    }

                } else {

                        if (diasRestantesCasoAprovado < 5 && diasRestantesCasoAprovado > 0) {

                            ferias_model novaferias = new ferias_model();
                            novaferias.setFuncionario(funcionario);
                            novaferias.setStatus("solicitado");
                            novaferias.setDataInicio(dto.getDataInicio());
                            novaferias.setDataFim(dto.getDataFim());
                            novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                            novaferias.setDiasParaDescontar(dias.intValue());
                            ferias_repository.save(novaferias);


                            return ResponseEntity.ok().body("AVISO essa solicitação foi enviada porém você ficará com saldo de férias que não poderá " +
                                    "mais ser utilizado em caso de aprovação pois seu saldo restante será menor que 5 dias, em caso de erro entre em contato com um funcionário de recursos humanos para alteração");

                        } else {

                            ferias_model novaferias = new ferias_model();
                            novaferias.setFuncionario(funcionario);
                            novaferias.setStatus("solicitado");
                            novaferias.setDataInicio(dto.getDataInicio());
                            novaferias.setDataFim(dto.getDataFim());
                            novaferias.setSetorfuncionario(funcionario.getIdsetor().getNome());
                            novaferias.setDiasParaDescontar(dias.intValue());
                            ferias_repository.save(novaferias);


                            return ResponseEntity.ok().body("solicitação enviada com sucesso");

                        }

                }

                return ResponseEntity.badRequest().body("erro inesperado");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("falha ao tentar cadastrar ferias");
        }
    }

    public String atualizar(atualizarFerias_dto dto, UUID idrh) {

        Optional<ferias_model> ferias =  ferias_repository.findById(dto.getIdferias());
        if (ferias.isPresent()) {

            ferias_model f = ferias.get();

            funcionario_model funcionariorh = funcionario_service.buscar(idrh);

            if (dto.getNovoStatus().equals("recusado") && dto.getMotivoRecusa() != null ) {

                f.setMotivoRecusa(dto.getMotivoRecusa());

            }

            f.setAtualizadoPor(funcionariorh);
            f.setStatus(dto.getNovoStatus());
            ferias_repository.save(f);
            return ("ferias atualizado com sucesso");
        } else{
            return ("falha ao tentar atualizar ferias");
        }

    }

    public List<ferias_model> listarTodosFuncionario(UUID funcionario_id) {

        List<ferias_model> lista =  new ArrayList<>();

        funcionario_model funcionario =  funcionario_service.buscar(funcionario_id);

        lista = ferias_repository.findByFuncionario(funcionario);

        return lista;

    }

    public List<ferias_model> listarTodosSolicitado() {

        try {

            List<ferias_model> lista =  ferias_repository.findByStatus("solicitado");
            return lista;

        } catch (Exception e) {
            return  null;
        }

    }

    public List<LocalDate> buscarFeriados(int ano) {
        String url = "https://brasilapi.com.br/api/feriados/v1/" + ano;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        List<LocalDate> res = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();


            JSONArray feriados = new JSONArray(responseBody);

            for (int i = 0; i < feriados.length(); i++) {

                JSONObject primeiroFeriado = feriados.getJSONObject(i);

                LocalDate dataaux =  LocalDate.parse(primeiroFeriado.getString("date"));

                res.add(dataaux);

            }

            return res;


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
