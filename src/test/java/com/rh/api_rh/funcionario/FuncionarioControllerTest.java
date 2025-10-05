package com.rh.api_rh.funcionario;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.funcionario.atualizarfuncionario_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoFuncao_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoNome_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoSetor_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import com.rh.api_rh.DTO.login.aceitartermo_dto;
import com.rh.api_rh.infra.security.token_service;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private token_service tokenService;

    @Test
    @Order(1)
    @DisplayName("Rota utilizada apenas em testes")
    void generateadmin() throws Exception {

        cadastroSetor_dto dto2 = new cadastroSetor_dto();
        dto2.setNome("teste");

        String json = objectMapper.writeValueAsString(dto2);

        mockMvc.perform(post("/setor")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/funcionario/generateadmin"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve cadastrar com sucesso um setor e funcionario")
    void cadastrar() throws Exception {

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
    @DisplayName("Deve falhar ao tentar cadastrar com qualquer campo not nullable vazio nesse caso salario")
    void cadastrarFalhaCampoVazio() throws Exception {

        cadastroSetor_dto dto2 = new cadastroSetor_dto();
        dto2.setNome("teste");

        String json = objectMapper.writeValueAsString(dto2);

        mockMvc.perform(post("/setor")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        cadastroFuncionario_dto dto =  new cadastroFuncionario_dto();
        dto.setNome("teste");
        dto.setFuncao("teste");
        dto.setData_nascimento("12-12-12");
        dto.setCpf("123456789");
        dto.setEmail("victoryuzoumc@gmail.com");
        dto.setCargo(Cargo.FUNCIONARIO);
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
        dto.setNumerosetor("1");

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Deve listar corretamente o objeto cadastrado")
    void listar() throws Exception {

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

        mockMvc.perform(get("/funcionario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("teste"));

    }

    @Test
    @DisplayName("Deve retornar os dados do funcionario corretamente")
    void perfil() throws Exception {

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

        MvcResult result = mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        funcionario_model f =  objectMapper.readValue(json3, funcionario_model.class);

        UUID id = f.getId();

        mockMvc.perform(get("/funcionario/"+id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("teste"));

    }

    @Test
    @DisplayName("Deve falhar ao enviar id que não existe")
    void perfilFalha() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/funcionario/"+id))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("deve retornar uma lista de funcionarios que possuem um certo cargo")
    void buscarporcargo() throws Exception {

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

        mockMvc.perform(get("/funcionario/buscarporcargo/FUNCIONARIO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("teste"));

    }

    @Test
    @DisplayName("deve retornar uma lista vazia caso não existam cadastros de um cargo")
    void buscarporcargoVazio() throws Exception {


        mockMvc.perform(get("/funcionario/buscarporcargo/FUNCIONARIO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

    }

    @Test
    @DisplayName(("Deve excluir um funcionario a partir de um id"))
    void excluir() throws Exception {

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

        MvcResult result = mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        funcionario_model f =  objectMapper.readValue(json3, funcionario_model.class);

        UUID id = f.getId();

        mockMvc.perform(delete("/funcionario/"+id))
                .andExpect(status().isOk());



    }

    @Test
    @DisplayName("deve criar um funcionario, um funcionario rh, e simular o funcionario rh atualizando os dados do funcionario")
    void atualizar() throws Exception {

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

        /// cadastro do rh
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

        atualizarfuncionario_dto dtoatualizar = new atualizarfuncionario_dto();
        dtoatualizar.setContabancaria("teste");
        dtoatualizar.setIdsetor(Long.valueOf(setorid));
        dtoatualizar.setTelefone("teste");
        dtoatualizar.setSalario((Float.valueOf(1000.00f)));
        dtoatualizar.setEmail("victoryuzoumc@gmail.com");
        dtoatualizar.setFuncao("teste");

        String jsonatualizar = objectMapper.writeValueAsString(dtoatualizar);

        mockMvc.perform(put("/funcionario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizar))
                .andExpect(status().isOk());




    }

    @Test
    @DisplayName("deve falhar pois o rh esqueceu de colocar algum campo, nesse caso: apagou sem querer o campo funcão")
    void atualizarFaltandodados() throws Exception {

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

        /// cadastro do rh
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

        atualizarfuncionario_dto dtoatualizar = new atualizarfuncionario_dto();
        dtoatualizar.setContabancaria("teste");
        dtoatualizar.setIdsetor(1L);
        dtoatualizar.setTelefone("teste");
        dtoatualizar.setSalario((Float.valueOf(1000.00f)));
        dtoatualizar.setEmail("victoryuzoumc@gmail.com");
        dtoatualizar.setFuncao("");

        String jsonatualizar = objectMapper.writeValueAsString(dtoatualizar);

        mockMvc.perform(put("/funcionario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizar))
                .andExpect(status().isBadRequest());




    }

    @Test
    @DisplayName("deve falhar pois tentou colocar um telefone que ja existe ao atualizar telefone")
    void atualizarTelefoneJaExiste() throws Exception {

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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        /// cadastro do rh
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
        dto3.setNumerotelefone("12345");
        dto3.setNumerosetor(setorid);

        String json3 = objectMapper.writeValueAsString(dto3);

        MvcResult result =  mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(status().isOk())
                .andReturn();

        String jsonfuncionario = result.getResponse().getContentAsString();
        funcionario_model funcionario =  objectMapper.readValue(jsonfuncionario, funcionario_model.class);

        String token = tokenService.generateToken(funcionario);

        atualizarfuncionario_dto dtoatualizar = new atualizarfuncionario_dto();
        dtoatualizar.setContabancaria("teste");
        dtoatualizar.setIdsetor(Long.valueOf(setorid));
        dtoatualizar.setTelefone("1234");
        dtoatualizar.setSalario((Float.valueOf(1000.00f)));
        dtoatualizar.setEmail("victoryuzoumc@gmail.com");
        dtoatualizar.setFuncao("teste");
        dtoatualizar.setTelefonenovo("12345");

        String jsonatualizar = objectMapper.writeValueAsString(dtoatualizar);

        mockMvc.perform(put("/funcionario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizar))
                .andExpect(status().isBadRequest());


    }

    @Test
    @DisplayName("deve falhar pois tentou colocar um telefone que ja existe ao atualizar telefone")
    void atualizarEmailJaExiste() throws Exception {

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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        /// cadastro do rh
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
        dto3.setNumerotelefone("12345");
        dto3.setNumerosetor(setorid);

        String json3 = objectMapper.writeValueAsString(dto3);

        MvcResult result =  mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(status().isOk())
                .andReturn();

        String jsonfuncionario = result.getResponse().getContentAsString();
        funcionario_model funcionario =  objectMapper.readValue(jsonfuncionario, funcionario_model.class);

        String token = tokenService.generateToken(funcionario);

        atualizarfuncionario_dto dtoatualizar = new atualizarfuncionario_dto();
        dtoatualizar.setContabancaria("teste");
        dtoatualizar.setIdsetor(Long.valueOf(setorid));
        dtoatualizar.setTelefone("1234");
        dtoatualizar.setSalario((Float.valueOf(1000.00f)));
        dtoatualizar.setEmail("victoryuzoumc@gmail.com");
        dtoatualizar.setFuncao("teste");
        dtoatualizar.setEmailnovo("victoryuzofb4c@gmail.com");

        String jsonatualizar = objectMapper.writeValueAsString(dtoatualizar);

        mockMvc.perform(put("/funcionario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonatualizar))
                .andExpect(status().isBadRequest());


    }

    @Test
    @DisplayName("deve atualizar o status de aceitação de termo de uso do funcionário corretamente")
    void aceitartermo() throws Exception {

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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        MvcResult result =  mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk())
                .andReturn();

        String jsonfuncionario = result.getResponse().getContentAsString();
        funcionario_model funcionario =  objectMapper.readValue(jsonfuncionario, funcionario_model.class);

        aceitartermo_dto dtoatualizar = new aceitartermo_dto();
        dtoatualizar.setIdfuncionario(funcionario.getId());

        String jsonaceitartermo = objectMapper.writeValueAsString(dtoatualizar);

        mockMvc.perform(put("/funcionario/aceitartermo")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonaceitartermo))
                .andExpect(status().isOk())
                .andReturn();


    }


    @Test
    @DisplayName("Deve retornar corretamente o funcionario pois corresponde ao setor da busca")
    void buscarFuncionariosPorSetor() throws Exception {

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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        List<Long> listaidsetores = new ArrayList<Long>();
        listaidsetores.add(Long.valueOf(setorid));

        buscarParaEnviarComunicadoSetor_dto dtosetores =  new buscarParaEnviarComunicadoSetor_dto();
        dtosetores.setIdsetores(listaidsetores);

        String json3 = objectMapper.writeValueAsString(dtosetores);


        mockMvc.perform(post("/funcionario/buscarParaEnviarComunicadoSetor")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("Deve retornar corretamente o funcionario pois corresponde a funcao da busca")
    void buscarFuncionariosPorFuncao() throws Exception {

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
        dto.setFuncao("atendente");
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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        List<String> listafuncoes = new ArrayList<String>();
        listafuncoes.add("atendente");

        buscarParaEnviarComunicadoFuncao_dto dtofuncoes =  new buscarParaEnviarComunicadoFuncao_dto();
        dtofuncoes.setFuncoes(listafuncoes);

        String json3 = objectMapper.writeValueAsString(dtofuncoes);


        mockMvc.perform(post("/funcionario/buscarParaEnviarComunicadoFuncao")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("Deve retornar corretamente o funcionario pois corresponde ao nomme da busca")
    void buscarFuncionariosPorNome() throws Exception {

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
        dto.setNome("joao");
        dto.setFuncao("atendente");
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
        dto.setNumerotelefone("1234");
        dto.setNumerosetor(setorid);

        String json2 = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isOk());

        List<String> listanomes = new ArrayList<String>();
        listanomes.add("joao");

        buscarParaEnviarComunicadoNome_dto dtonomes =  new buscarParaEnviarComunicadoNome_dto();
        dtonomes.setNomes(listanomes);

        String json3 = objectMapper.writeValueAsString(dtonomes);


        mockMvc.perform(post("/funcionario/buscarParaEnviarComunicadoNome")
                        .contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk());


    }



}