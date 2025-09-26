package com.rh.api_rh.setor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class SetorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private setor_service setorService;

    @Test
    @DisplayName("Deve listar setores criados corretamente e verificar o objeto")
    void listar() throws Exception{

        setor_model setor = new setor_model();
        setor.setNome("teste");
        setor_model setorresult = setorService.cadastrar(setor);

        assertNotNull(setorresult);

        mockMvc.perform(get("/setor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("teste"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia")
    void listarVazio() throws Exception{


        mockMvc.perform(get("/setor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("Deve criar corretamente um setor")
    void cadastrar() throws Exception{

        cadastroSetor_dto dto = new cadastroSetor_dto();
        dto.setNome("teste");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/setor")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve falhar ao enviar setor ja existente")
    void cadastrarJaExiste() throws Exception{

        setor_model setor = new setor_model();
        setor.setNome("teste");
        setor_model setorresult = setorService.cadastrar(setor);

        assertNotNull(setorresult);

        cadastroSetor_dto dto = new cadastroSetor_dto();
        dto.setNome("teste");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/setor")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());

    }

    @Test
    @DisplayName("Deve falhar ao enviar setor vazio")
    void cadastrarCazio() throws Exception{


        cadastroSetor_dto dto = new cadastroSetor_dto();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/setor")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError());

    }



    @Test
    @DisplayName("Deve buscar corretamente um setor")
    void buscarsetor() throws  Exception{

        setor_model setor = new setor_model();
        setor.setNome("teste");
        setor_model setorresult = setorService.cadastrar(setor);

        assertNotNull(setorresult);

        Long id = setor.getId();

        mockMvc.perform(get("/setor/"+id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("teste"));

    }

    @Test
    @DisplayName("Deve falhar caso setor não exista")
    void buscarsetorFalho() throws  Exception{

        mockMvc.perform(get("/setor/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve deletar um setor corretamente")
    void deletarsetor() throws Exception{

        setor_model setor = new setor_model();
        setor.setNome("teste");
        setor_model setorresult = setorService.cadastrar(setor);

        assertNotNull(setorresult);

        Long id = setor.getId();

        mockMvc.perform(delete("/setor/"+id))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve falhar em deletar caso não exista")
    void deletarsetorFalho() throws Exception{


        mockMvc.perform(delete("/setor/1"))
                .andExpect(status().isBadRequest());

    }
}