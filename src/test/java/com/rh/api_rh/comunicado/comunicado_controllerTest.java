package com.rh.api_rh.comunicado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.comunicado.alterarVisto_dto;
import com.rh.api_rh.DTO.aplicacao.comunicado.enviarComunicado_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import com.rh.api_rh.comunicado.comunicado_funcionario.comunicado_funcionario_model;
import com.rh.api_rh.funcionario.Cargo;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.setor.setor_model;
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
class comunicado_controllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    }


    @Test
    @DisplayName("Deve enviar um comunicado corretamente")
    void enviarComunicado() throws  Exception {

        enviarComunicado_dto dtoenviarComunicado = new enviarComunicado_dto();

        MvcResult resultListaFuncionario =  mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andReturn();

        String dataresult = resultListaFuncionario.getResponse().getContentAsString();
        List<funcionario_model> listafuncionarios = objectMapper.readValue(dataresult, new TypeReference<List<funcionario_model>>(){});

        dtoenviarComunicado.setFuncionarios(listafuncionarios);
        dtoenviarComunicado.setTitulo("Comunicado");
        dtoenviarComunicado.setTexto("Comunicado");

        String jsonenviarComunicado = objectMapper.writeValueAsString(dtoenviarComunicado);

        mockMvc.perform(post("/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonenviarComunicado))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve buscar corretamente os comunicados de um funcionario")
    void buscarComunicadosFuncionario() throws  Exception {

        enviarComunicado_dto dtoenviarComunicado = new enviarComunicado_dto();

        MvcResult resultListaFuncionario =  mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andReturn();

        String dataresult = resultListaFuncionario.getResponse().getContentAsString();
        List<funcionario_model> listafuncionarios = objectMapper.readValue(dataresult, new TypeReference<List<funcionario_model>>(){});

        dtoenviarComunicado.setFuncionarios(listafuncionarios);
        dtoenviarComunicado.setTitulo("Comunicado");
        dtoenviarComunicado.setTexto("Comunicado");

        String jsonenviarComunicado = objectMapper.writeValueAsString(dtoenviarComunicado);

        mockMvc.perform(post("/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonenviarComunicado))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk());

        UUID idfuncionario = listafuncionarios.get(0).getId();

        mockMvc.perform(get("/comunicado/funcionario/"+idfuncionario ))
                .andExpect(jsonPath("$[0].visto").value(false))
                .andExpect(jsonPath("$[0].comunicado.titulo").value("Comunicado"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve retornar corretamente os dados de um comunicado")
    void buscarComunicado() throws  Exception {

        enviarComunicado_dto dtoenviarComunicado = new enviarComunicado_dto();

        MvcResult resultListaFuncionario =  mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andReturn();

        String dataresult = resultListaFuncionario.getResponse().getContentAsString();
        List<funcionario_model> listafuncionarios = objectMapper.readValue(dataresult, new TypeReference<List<funcionario_model>>(){});

        dtoenviarComunicado.setFuncionarios(listafuncionarios);
        dtoenviarComunicado.setTitulo("Comunicado");
        dtoenviarComunicado.setTexto("Comunicado");

        String jsonenviarComunicado = objectMapper.writeValueAsString(dtoenviarComunicado);

        MvcResult resultenviarcomunicado = mockMvc.perform(post("/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonenviarComunicado))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk())
                .andReturn();

        String enviarcomunicadodata =  resultenviarcomunicado.getResponse().getContentAsString();
        comunicado_model comunicado = objectMapper.readValue(enviarcomunicadodata, comunicado_model.class);

        Long idcomunicado =  comunicado.getId();

        mockMvc.perform(get("/comunicado/buscar/"+idcomunicado ))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    @DisplayName("Deve alterar o status de visto para true corretamente")
    void alterarVisto() throws  Exception {

        enviarComunicado_dto dtoenviarComunicado = new enviarComunicado_dto();

        MvcResult resultListaFuncionario =  mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andReturn();

        String dataresult = resultListaFuncionario.getResponse().getContentAsString();
        List<funcionario_model> listafuncionarios = objectMapper.readValue(dataresult, new TypeReference<List<funcionario_model>>(){});

        dtoenviarComunicado.setFuncionarios(listafuncionarios);
        dtoenviarComunicado.setTitulo("Comunicado");
        dtoenviarComunicado.setTexto("Comunicado");

        String jsonenviarComunicado = objectMapper.writeValueAsString(dtoenviarComunicado);

        mockMvc.perform(post("/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonenviarComunicado))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk());

        UUID idfuncionario = listafuncionarios.get(0).getId();

        MvcResult resultbuscarcomunicados = mockMvc.perform(get("/comunicado/funcionario/"+idfuncionario ))
                .andExpect(jsonPath("$[0].visto").value(false))
                .andExpect(jsonPath("$[0].comunicado.titulo").value("Comunicado"))
                .andExpect(status().isOk())
                .andReturn();

        String buscarcomunicadosdata = resultbuscarcomunicados.getResponse().getContentAsString();
        List<comunicado_funcionario_model> listaTabelaAuxiliar = objectMapper.readValue(buscarcomunicadosdata, new TypeReference<List<comunicado_funcionario_model>>(){} );

        Long idItemAuxiliar = listaTabelaAuxiliar.get(0).getId();

        alterarVisto_dto dtoalterarVisto = new alterarVisto_dto();
        dtoalterarVisto.setIdcomunicadotabelaaxuiliar(idItemAuxiliar);

        String jsonalterarVisto = objectMapper.writeValueAsString(dtoalterarVisto);
        mockMvc.perform(post("/comunicado/alterarVisto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonalterarVisto))
                .andExpect(jsonPath("$.visto").value(true))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("Deve retornar corretamente os comunicados")
    void buscarComunicados() throws  Exception {

        enviarComunicado_dto dtoenviarComunicado = new enviarComunicado_dto();

        MvcResult resultListaFuncionario =  mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andReturn();

        String dataresult = resultListaFuncionario.getResponse().getContentAsString();
        List<funcionario_model> listafuncionarios = objectMapper.readValue(dataresult, new TypeReference<List<funcionario_model>>(){});

        dtoenviarComunicado.setFuncionarios(listafuncionarios);
        dtoenviarComunicado.setTitulo("Comunicado");
        dtoenviarComunicado.setTexto("Comunicado");

        String jsonenviarComunicado = objectMapper.writeValueAsString(dtoenviarComunicado);

        mockMvc.perform(post("/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonenviarComunicado))
                .andExpect(jsonPath("$.titulo").value("Comunicado"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/comunicado"))
                .andExpect(jsonPath("$[0].titulo").value("Comunicado"))
                .andExpect(status().isOk());

    }
}