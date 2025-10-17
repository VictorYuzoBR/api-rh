package com.rh.api_rh.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.cadastro.*;
import com.rh.api_rh.DTO.login.loginFuncionario_dto;
import com.rh.api_rh.DTO.login.login_candidato_dto;
import com.rh.api_rh.DTO.login.trocasenha_dto;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.funcionario.Cargo;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_service;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private funcionario_service funcionario_service;


    @Test
    @DisplayName("Deve realizar o login corretamente")
    void login() throws Exception {

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


        trocasenha_dto dtotrocasenha = new trocasenha_dto();

        dtotrocasenha.setEmail("victoryuzoumc@gmail.com");
        dtotrocasenha.setSenha("123456789");

        String jsontrocasenha = objectMapper.writeValueAsString(dtotrocasenha);

        mockMvc.perform(put("/usuario/novasenha")
                        .contentType(MediaType.APPLICATION_JSON).content(jsontrocasenha))
                .andExpect(status().isOk());

        List<funcionario_model> funcionarios = funcionario_service.listar();

        String registro = funcionarios.get(0).getIdusuario().getRegistro();


        loginFuncionario_dto dtoLogin = new loginFuncionario_dto(registro,"123456789");

        String jsonLogin = objectMapper.writeValueAsString(dtoLogin);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON).content(jsonLogin))
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(status().isOk());

    }

    @Test
    void refresh() {
    }

    @Test
    @DisplayName("Deve realizar o login de um candidato corretamente")
    void loginCandidato() throws Exception {

        cadastroHabilidade_dto dtohabilidade = new cadastroHabilidade_dto();
        dtohabilidade.setHabilidade(("java"));
        String jsonsetuphabilidade = objectMapper.writeValueAsString(dtohabilidade);

        mockMvc.perform(post("/habilidade")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonsetuphabilidade))
                .andExpect(status().isOk())
                .andReturn();

        cadastroIdioma_dto dtoidioma = new cadastroIdioma_dto();
        dtoidioma.setIdioma(("portugues"));
        String jsonsetupidioma = objectMapper.writeValueAsString(dtoidioma);

        mockMvc.perform(post("/idioma")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonsetupidioma))
                .andExpect(status().isOk())
                .andReturn();

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john krammer");
        dto.setEmail("victoryuzoumc@gmail.com");
        dto.setTelefone("(11) 912234-5678");
        dto.setLinkedin("linkexemplonaovalido.com");
        dto.setGithub("https://githubsemsentido.com");
        dto.setCidade("cidade exemplo");
        dto.setEstado("estado exemplo");

        List<habilidade_model_apenas_formulario> listahabilidades = new ArrayList<>();
        habilidade_model_apenas_formulario habilidade = new habilidade_model_apenas_formulario();
        habilidade.setHabilidade("java");
        habilidade.setTempoExperiencia(5);
        listahabilidades.add(habilidade);
        dto.setHabilidades(listahabilidades);

        List<formacaoAcademica_model> listaformacoes = new ArrayList<>();
        formacaoAcademica_model formacao = new formacaoAcademica_model();
        formacao.setInstituicao("uniesquina");
        formacao.setCurso("sem futuro");
        formacao.setSituacao("qualquer coisa");
        formacao.setDataInicio(LocalDate.parse("2000-12-12"));
        formacao.setDataFim(LocalDate.parse("2003-12-12"));
        listaformacoes.add(formacao);
        dto.setFormacaoAcademica(listaformacoes);

        List<experiencia_model> listaexperiencias = new ArrayList<>();
        experiencia_model experiencia = new experiencia_model();
        experiencia.setEmpresa("xp");
        experiencia.setDescricao("programacao orientada a gambiarra");
        experiencia.setDataInicio(LocalDate.parse("2005-12-12"));
        experiencia.setDataFim(LocalDate.parse("2008-12-12"));
        listaexperiencias.add(experiencia);
        dto.setExperiencias(listaexperiencias);

        List<idioma_model_apenas_formulario>  listaidiomas = new ArrayList<>();
        idioma_model_apenas_formulario idioma = new idioma_model_apenas_formulario();
        idioma.setIdioma("portugues");
        idioma.setNivel("1");
        listaidiomas.add(idioma);
        dto.setIdiomas(listaidiomas);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/candidato")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();


        login_candidato_dto dtologin = new login_candidato_dto();

        dtologin.setEmail("victoryuzoumc@gmail.com");
        dtologin.setPassword("12345678");

        String jsonLogin = objectMapper.writeValueAsString(dtologin);

        mockMvc.perform(post("/auth/logincandidato")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonLogin))
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(status().isOk());


    }
}