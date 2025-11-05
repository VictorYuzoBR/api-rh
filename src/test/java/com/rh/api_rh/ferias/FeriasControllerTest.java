package com.rh.api_rh.ferias;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.ferias.atualizarFerias_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarFerias_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import com.rh.api_rh.funcionario.Cargo;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_repository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class FeriasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private token_service tokenService;

    @Autowired
    private ferias_repository feriasRepository;

    @Autowired
    private setor_repository setorRepository;

    @Autowired
    private funcionario_repository funcionarioRepository;

    @BeforeEach
    void setUp() throws Exception {

        cadastroSetor_dto dto2 = new cadastroSetor_dto();
        dto2.setNome("teste");

        String json = objectMapper.writeValueAsString(dto2);

        MvcResult resultsetor =  mockMvc.perform(post("/setor")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();

        String jsonsetor =  resultsetor.getResponse().getContentAsString();
        setor_model setor = objectMapper.readValue(jsonsetor, setor_model.class);

        String setorid = setor.getId().toString();

        cadastroFuncionario_dto dto =  new cadastroFuncionario_dto();
        dto.setNome("teste");
        dto.setFuncao("teste");
        dto.setData_nascimento("12-12-12");
        dto.setCpf("123456789");
        dto.setEmail("victoryuzoumc@gmail.com");
        dto.setCargo(Cargo.FUNCIONARIO);
        dto.setSalario((Float.valueOf(1000.00f)));
        dto.setContabancaria("351352-0");
        dto.setDataentrada("2024-06-01");
        dto.setCep("3516556");
        dto.setLogradouro("Rua dos Santos");
        dto.setBairro("Bairro dos Santos");
        dto.setCidade("Cidade dos Santos");
        dto.setEstado("Estado dos Santos");
        dto.setNumero("12345");
        dto.setComplemento("Complemento dos Santos");
        dto.setNumerotelefone("123456789");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        List<funcionario_model> funcionarios = funcionarioRepository.findAll();

        funcionario_model f = funcionarios.get(0);

        f.setUltimoCalculo(LocalDate.parse("2025-06-01"));
        f.setFeriasDisponiveis(30);
        f.setFracoesDisponiveis(3);


    }



    @Test
    @DisplayName("Deve criar uma solicitação de ferias corretamente")
    void cadastrar() throws Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();

    }

    @Test
    @DisplayName("Deve alterar o status de uma solicitação corretamente")
    void atualizar() throws Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        String token = tokenService.generateToken(funcionario);

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();


        List<ferias_model> listaferias = feriasRepository.findAll();

        assertEquals(1,listaferias.size());

        Long idferias =  listaferias.get(0).getId();


        atualizarFerias_dto dtoatualizarferias =  new atualizarFerias_dto();
        dtoatualizarferias.setNovoStatus("aprovado");
        dtoatualizarferias.setIdferias(idferias);

        String jsonatualizarferias =  objectMapper.writeValueAsString(dtoatualizarferias);

        mockMvc.perform(put("/ferias" )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizarferias))
                .andExpect(status().isOk())
                .andExpect(content().string("ferias atualizado com sucesso"))
                .andReturn();




    }

    @Test
    @DisplayName("Deve retornar as solicitações do setor")
    void listarFeriasPorSetor() throws  Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        String token = tokenService.generateToken(funcionario);

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();


        List<ferias_model> listaferias = feriasRepository.findAll();

        assertEquals(1,listaferias.size());

        Long idferias =  listaferias.get(0).getId();


        atualizarFerias_dto dtoatualizarferias =  new atualizarFerias_dto();
        dtoatualizarferias.setNovoStatus("aprovado");
        dtoatualizarferias.setIdferias(idferias);

        String jsonatualizarferias =  objectMapper.writeValueAsString(dtoatualizarferias);

        mockMvc.perform(put("/ferias" )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizarferias))
                .andExpect(status().isOk())
                .andExpect(content().string("ferias atualizado com sucesso"))
                .andReturn();

        assertEquals("aprovado",listaferias.get(0).getStatus());


        mockMvc.perform(get("/ferias/feriasporsetor/1"))
                .andExpect(jsonPath("$[0].quantidadeAprovadas").value(1))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    @DisplayName("Deve retornar a lista de solicitações do funcionário corretamente")
    void listarFeriasFuncionario() throws  Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        String token = tokenService.generateToken(funcionario);

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();

        mockMvc.perform(get("/ferias/funcionario/"+funcioarioId.toString()))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    @DisplayName("Deve retornar férias que conflitam com a data de uma solicitação corretamente")
    void listarFeriasConflitantes() throws  Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        String token = tokenService.generateToken(funcionario);

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();


        List<ferias_model> listaferias = feriasRepository.findAll();

        assertEquals(1,listaferias.size());

        Long idferias =  listaferias.get(0).getId();


        atualizarFerias_dto dtoatualizarferias =  new atualizarFerias_dto();
        dtoatualizarferias.setNovoStatus("aprovado");
        dtoatualizarferias.setIdferias(idferias);

        String jsonatualizarferias =  objectMapper.writeValueAsString(dtoatualizarferias);

        mockMvc.perform(put("/ferias" )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizarferias))
                .andExpect(status().isOk())
                .andExpect(content().string("ferias atualizado com sucesso"))
                .andReturn();


        List<setor_model> listasetor = setorRepository.findAll();
        String setorid = listasetor.get(0).getId().toString();

        cadastroFuncionario_dto dto3 =  new cadastroFuncionario_dto();
        dto3.setNome("teste");
        dto3.setFuncao("teste");
        dto3.setData_nascimento("12-12-12");
        dto3.setCpf("123445789");
        dto3.setEmail("victoryuzofb4c@gmail.com");
        dto3.setCargo(Cargo.RH);
        dto3.setSalario((Float.valueOf(1000.00f)));
        dto3.setContabancaria("355425452-0");
        dto3.setDataentrada("2024-06-01");
        dto3.setCep("35162556");
        dto3.setLogradouro("Rua dos Sansdftos");
        dto3.setBairro("Bairro dos Ssdfantos");
        dto3.setCidade("Cidade dos sdfSantos");
        dto3.setEstado("Estado dos sdfSantos");
        dto3.setNumero("123445");
        dto3.setComplemento("Complemento dos Santos");
        dto3.setNumerotelefone("1235689");
        dto3.setNumerosetor(setorid);

        String json3 = objectMapper.writeValueAsString(dto3);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(status().isOk())
                .andReturn();


        List<funcionario_model> funcionarios2 = funcionarioRepository.findAll();

        funcionario_model f2 = funcionarios2.get(1);

        f2.setUltimoCalculo(LocalDate.parse("2025-06-01"));
        f2.setFeriasDisponiveis(30);
        f2.setFracoesDisponiveis(3);
        funcionarioRepository.save(f2);



        MvcResult resultBuscarFuncionarios2 = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().isOk())
                .andReturn();


        String buscarFuncionariosData2 =  resultBuscarFuncionarios2.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios2 = objectMapper.readValue(buscarFuncionariosData2, new TypeReference<List<funcionario_model>>() {});


        UUID funcioarioId2 = listaFuncionarios2.get(1).getId();

        cadastrarFerias_dto dtoferias2 = new cadastrarFerias_dto();

        dtoferias2.setIdfuncionario(funcioarioId2);

        dtoferias2.setDataInicio(inicio);
        dtoferias2.setDataFim(fim);

        String jsonferias2 =  objectMapper.writeValueAsString(dtoferias2);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias2))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();

        List<ferias_model> listaferias2 = feriasRepository.findAll();

        Long idferias2 = listaferias2.get(1).getId();

        mockMvc.perform(get("/ferias/feriasConflitantes/"+ idferias2))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk());



    }

    @Test
    @DisplayName("Deve listar solicitações em situação pendente")
    void listarSolicitacoesPendentes() throws Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        String token = tokenService.generateToken(funcionario);

        UUID funcioarioId = funcionario.getId();

        cadastrarFerias_dto dtoferias = new cadastrarFerias_dto();

        dtoferias.setIdfuncionario(funcioarioId);

        LocalDate inicioaux = funcionario.getUltimoCalculo().plusDays(240);

        boolean quarta = false;

        LocalDate inicioaux2 = inicioaux;

        while (!quarta) {

            if (inicioaux2.getDayOfWeek().toString().equals("WEDNESDAY")) {
                quarta = true;
            } else {
                inicioaux2 =  inicioaux2.plusDays(1);
            }

        }


        LocalDate inicio = inicioaux2;
        LocalDate fim = inicioaux2.plusDays(14);

        dtoferias.setDataInicio(inicio);
        dtoferias.setDataFim(fim);

        String jsonferias =  objectMapper.writeValueAsString(dtoferias);


        mockMvc.perform(post("/ferias" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonferias))
                .andExpect(status().isOk())
                .andExpect(content().string("solicitação enviada com sucesso"))
                .andReturn();

        mockMvc.perform(get("/ferias"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk());

    }
}