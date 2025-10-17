package com.rh.api_rh.espelho;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.espelho.descreverAbono_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarFeriado_dto;
import com.rh.api_rh.DTO.aplicacao.espelho.gerarPDF_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_repository;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_item.espelho_item_repository;
import com.rh.api_rh.funcionario.Cargo;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_repository;
import jakarta.persistence.EntityManager;
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
import java.time.YearMonth;
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
class EspelhoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private espelho_application_service espelhoService;

    @Autowired
    espelho_item_repository espelho_item_repository;

    @Autowired
    entrada_espelho_repository entrada_espelho_repository;

    @Autowired
    token_service tokenService;

    @Autowired
    setor_repository setor_repository;

    @Autowired
    espelho_repository espelho_repository;

    @Autowired
    private EntityManager entityManager;


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
        dto.setDataentrada("12-12-12");
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

        espelhoService.gerarEspelho();
        espelhoService.gerarItemDiario();

        entityManager.flush();
        entityManager.clear();


    }


    @Test
    @DisplayName("Deve listar corretamente os espelhos criados")
    void listar() throws Exception {

       mockMvc.perform(get("/espelho"))
               .andExpect(jsonPath("$.length()").value(1))
               .andExpect(jsonPath("$[0].nomeFuncionario").value("teste"))
               .andDo(print())
               .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve bater um ponto corretamente")
    void baterponto() throws Exception {


        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        UUID funcioarioId = funcionario.getId();

        mockMvc.perform(post("/espelho/baterponto/"+ funcioarioId))
                .andExpect(status().isOk());

        List<espelho_item_model> listaItems = espelho_item_repository.findAll();

        assertEquals(1,listaItems.size());

        List<entrada_espelho_model> listaEntradas = entrada_espelho_repository.findAll();

        assertEquals(1,listaEntradas.size());
        assertEquals("entrada",listaEntradas.get(0).getTipo());


    }

    @Test
    @DisplayName("Deve listar os espelhos de um funcionario corretamente")
    void espelhosFuncionario() throws Exception {

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        UUID funcioarioId = funcionario.getId();

        mockMvc.perform(get("/espelho/"+ funcioarioId))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeFuncionario").value("teste"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve inserir uma descrição de abono em uma entrada corretamente")
    void descreverAbono() throws Exception {

        List<espelho_item_model> listaItems = espelho_item_repository.findAll();

        assertEquals(1,listaItems.size());

        Long iditem = listaItems.get(0).getId();

        descreverAbono_dto dto =  new descreverAbono_dto();
        dto.setIditem(iditem);
        dto.setDescricao("descricao");

        String jsonabono = objectMapper.writeValueAsString(dto);


        mockMvc.perform(post("/espelho/abono" )
                        .contentType(MediaType.APPLICATION_JSON).content(jsonabono))
                .andExpect(jsonPath("$.descricaoAbono").value("descricao"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve simular um rh criando um feriado corretamente")
    void gerarFeriado() throws Exception {

        List<setor_model> listaSetor = setor_repository.findAll();
        assertEquals(1,listaSetor.size());

        String setorid = listaSetor.get(0).getId().toString();

        cadastroFuncionario_dto dto3 =  new cadastroFuncionario_dto();
        dto3.setNome("teste");
        dto3.setFuncao("teste");
        dto3.setData_nascimento("12-12-12");
        dto3.setCpf("123445789");
        dto3.setEmail("victoryuzofb4c@gmail.com");
        dto3.setCargo(Cargo.RH);
        dto3.setSalario((Float.valueOf(1000.00f)));
        dto3.setContabancaria("355425452-0");
        dto3.setDataentrada("12-12-12");
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

        MvcResult result =  mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(status().isOk())
                .andReturn();

        String jsonfuncionario = result.getResponse().getContentAsString();
        funcionario_model funcionario =  objectMapper.readValue(jsonfuncionario, funcionario_model.class);

        String token = tokenService.generateToken(funcionario);

        gerarFeriado_dto gerarferiadodto =  new gerarFeriado_dto();

        LocalDate hoje = LocalDate.now();

        gerarferiadodto.setData(hoje);


        String jsongerarferiado =  objectMapper.writeValueAsString(gerarferiadodto);

        mockMvc.perform(post("/espelho/gerarFeriado")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsongerarferiado))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/espelho"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nomeFuncionario").value("teste"))
                .andExpect(jsonPath("$[0].listaEntradas[0].descricaoAbono").value("feriado"))
                .andDo(print())
                .andExpect(status().isOk());





    }


    @Test
    @DisplayName("Deve retornar corretamente o espelho do mes do usuario")
    void espelhoDoMes() throws Exception {

        YearMonth anoMesAtual = YearMonth.now();
        LocalDate primeiroDia = anoMesAtual.atDay(1);

        MvcResult resultBuscarFuncionarios = mockMvc.perform(get("/funcionario"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String buscarFuncionariosData =  resultBuscarFuncionarios.getResponse().getContentAsString();
        List<funcionario_model> listaFuncionarios = objectMapper.readValue(buscarFuncionariosData, new TypeReference<List<funcionario_model>>() {});

        funcionario_model funcionario = listaFuncionarios.get(0);

        assertEquals("teste",funcionario.getNome());

        UUID funcioarioId = funcionario.getId();

        mockMvc.perform(get("/espelho/espelhoDoMes/" +funcioarioId))
                .andExpect(jsonPath("$.periodoInicio").value(String.valueOf(primeiroDia)))
                .andExpect(status().isOk());



    }

    @Test
    @DisplayName("Deve gerar um pdf corretamente")
    void gerarPDF() throws Exception {

        List<espelho_model> listaespelhos = espelho_repository.findAll();

        Long idespelho = listaespelhos.get(0).getId();

        gerarPDF_dto dtopdf =  new gerarPDF_dto();
        dtopdf.setIdespelho(idespelho);

        String jsonpdf =  objectMapper.writeValueAsString(dtopdf);

        mockMvc.perform(post("/espelho/gerarPDF")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonpdf))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk());

    }



}