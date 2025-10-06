package com.rh.api_rh.candidato;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.candidato.atualizarCandidato_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.buscarComBaseHabilidades_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidato_dto;
import com.rh.api_rh.DTO.cadastro.cadastroHabilidade_dto;
import com.rh.api_rh.DTO.cadastro.cadastroIdioma_dto;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_service;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.funcionario.funcionario_model;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private formacaoAcademica_service formacaoAcademicaService;

    @BeforeEach
    void setUp() throws Exception {

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

        cadastroCandidato_dto dtosetup = new cadastroCandidato_dto();

        dtosetup.setPassword("12345678");
        dtosetup.setNome("john krammer");
        dtosetup.setEmail("john@ml.com");
        dtosetup.setTelefone("(11) 91234-5678");
        dtosetup.setLinkedin("linkexemplonaovalido.com");
        dtosetup.setGithub("https://githubsemsentido.com");
        dtosetup.setCidade("cidade exemplo");
        dtosetup.setEstado("estado exemplo");

        List<habilidade_model_apenas_formulario> listahabilidades = new ArrayList<>();
        habilidade_model_apenas_formulario habilidade = new habilidade_model_apenas_formulario();
        habilidade.setHabilidade("java");
        habilidade.setTempoExperiencia(5);
        listahabilidades.add(habilidade);
        dtosetup.setHabilidades(listahabilidades);

        List<formacaoAcademica_model> listaformacoes = new ArrayList<>();
        formacaoAcademica_model formacao = new formacaoAcademica_model();
        formacao.setInstituicao("uniesquina");
        formacao.setCurso("sem futuro");
        formacao.setSituacao("qualquer coisa");
        formacao.setDataInicio(LocalDate.parse("2000-12-12"));
        formacao.setDataFim(LocalDate.parse("2003-12-12"));
        listaformacoes.add(formacao);
        dtosetup.setFormacaoAcademica(listaformacoes);

        List<experiencia_model> listaexperiencias = new ArrayList<>();
        experiencia_model experiencia = new experiencia_model();
        experiencia.setEmpresa("xp");
        experiencia.setDescricao("programacao orientada a gambiarra");
        experiencia.setDataInicio(LocalDate.parse("2005-12-12"));
        experiencia.setDataFim(LocalDate.parse("2008-12-12"));
        listaexperiencias.add(experiencia);
        dtosetup.setExperiencias(listaexperiencias);

        List<idioma_model_apenas_formulario> listaidiomas = new ArrayList<>();
        idioma_model_apenas_formulario idioma = new idioma_model_apenas_formulario();
        idioma.setIdioma("portugues");
        idioma.setNivel("1");
        listaidiomas.add(idioma);
        dtosetup.setIdiomas(listaidiomas);

        String jsonsetup = objectMapper.writeValueAsString(dtosetup);

        mockMvc.perform(post("/candidato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonsetup))
                .andExpect(status().isOk())
                .andReturn();


    }



    @Test
    @DisplayName("Deve cadastrar corretamente um candidato")
    void cadastrar() throws Exception {

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john krammer");
        dto.setEmail("john2@ml.com");
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


    }

    @Test
    @DisplayName("Deve falhar ao enviar um email ja existente")
    void cadastrarFalha() throws Exception {

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john krammer");
        dto.setEmail("john@ml.com");
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
                .andExpect(status().isConflict())
                .andReturn();


    }

    @Test
    @DisplayName(("Deve listar corretamente o candidato cadastrado"))
    void listar() throws Exception {

        mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("john krammer"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve trazer os candidatos que possuem uma certa habilidade")
    void filtrarPorHabilidades() throws Exception {

        buscarComBaseHabilidades_dto dto = new buscarComBaseHabilidades_dto();
        List<String> habilidades = new ArrayList<>();
        habilidades.add("java");
        dto.setHabilidades(habilidades);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/candidato/filtrar")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("john krammer"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void enviarEmailNovaVaga() {
    }

    @Test
    @DisplayName("Deve fazer com o que o candidato aceite os termos corretamente")
    void aceitartermo() throws Exception {

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john krammer");
        dto.setEmail("john2@ml.com");
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

        MvcResult result=  mockMvc.perform(post("/candidato")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        candidato_model candidato =  objectMapper.readValue(json3, candidato_model.class);

        Long candidatoid = candidato.getId();

        mockMvc.perform(post("/candidato/aceitartermo/"+candidatoid))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve retornar corretamente o perfil")
    void perfil() throws Exception {

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john krammer");
        dto.setEmail("john2@ml.com");
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

        MvcResult result=  mockMvc.perform(post("/candidato")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        candidato_model candidato =  objectMapper.readValue(json3, candidato_model.class);

        Long candidatoid = candidato.getId();

        mockMvc.perform(get("/candidato/perfil/"+candidatoid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(candidato.getNome()))
                .andReturn();

    }

    @Test
    void atualizar() throws Exception{

        MvcResult result=  mockMvc.perform(get("/candidato"))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        List<candidato_model> candidato =  objectMapper.readValue(json3, new TypeReference<List<candidato_model>>() {});

        Long idcandidato = candidato.get(0).getId();

        atualizarCandidato_dto dto = new atualizarCandidato_dto();

        dto.setId(idcandidato);
        dto.setTelefone("(11) 9134-5678");
        dto.setLinkedin("linkexemplonaovalido.com");
        dto.setGithub("https://githubsemsentido.com");
        dto.setCidade("cidade");
        dto.setEstado("estado exemplo");

        List<habilidade_model_apenas_formulario> listahabilidades = new ArrayList<>();
        habilidade_model_apenas_formulario habilidade = new habilidade_model_apenas_formulario();
        habilidade.setHabilidade("java");
        habilidade.setTempoExperiencia(5);
        listahabilidades.add(habilidade);
        dto.setHabilidades(listahabilidades);

        List<formacaoAcademica_model> listaformacoes = new ArrayList<>();
        formacaoAcademica_model formacao = new formacaoAcademica_model();
        formacao.setInstituicao("uniesquinaaa");
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

        mockMvc.perform(put("/candidato/atualizar")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$[0].telefone").value("(11) 9134-5678"))
                .andExpect(jsonPath("$[0].cidade").value("cidade"))
                .andExpect(status().isOk())
                .andReturn();

        List<formacaoAcademica_model> formacoes = formacaoAcademicaService.listar();

        assertEquals("uniesquinaaa", formacoes.get(0).getInstituicao());


    }

    @Test
    @DisplayName("Deve excluir corretamente o funcionario")
    void excluir() throws Exception {

        MvcResult result =  mockMvc.perform(get("/candidato"))
                .andExpect(status().isOk())
                .andReturn();

        String json3 = result.getResponse().getContentAsString();
        List<candidato_model> candidato =  objectMapper.readValue(json3, new TypeReference<List<candidato_model>>() {});

        Long idcandidato = candidato.get(0).getId();

        mockMvc.perform(delete("/candidato/"+idcandidato))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/candidato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(content().json("[]"))
                .andReturn();


    }

    @Test
    void buscarCandidaturas() {
    }

    @Test
    void trocarSenha() {
    }
}