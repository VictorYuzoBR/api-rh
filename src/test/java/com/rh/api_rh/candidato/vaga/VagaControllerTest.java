package com.rh.api_rh.candidato.vaga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.aplicacao.vaga.avancarEtapa_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.compatibilidadeUnica_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.finalizarVaga_dto;
import com.rh.api_rh.DTO.cadastro.*;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_service;
import com.rh.api_rh.candidato.habilidade.habilidade_apenas_formulario_vaga;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.funcionario.Cargo;
import com.rh.api_rh.funcionario.funcionario_model;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class VagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private token_service tokenService;

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

        cadastrarVaga_dto dtovagasetup  = new cadastrarVaga_dto();
        dtovagasetup.setTitulo("titulo");
        dtovagasetup.setDescricao("descricao");
        dtovagasetup.setNivel("iniciante");
        dtovagasetup.setInformacoes("informacoes");
        dtovagasetup.setModelo("presencial");
        dtovagasetup.setLocalizacao("sp");
        dtovagasetup.setPalavrasChave("teste alo");
        dtovagasetup.setTipoContrato("clt");

        List<habilidade_apenas_formulario_vaga> lista = new ArrayList<>();
        habilidade_apenas_formulario_vaga hab1  = new habilidade_apenas_formulario_vaga();
        hab1.setHabilidade("java");
        hab1.setPeso(1);
        lista.add(hab1);

        dtovagasetup.setHabilidades(lista);

        String jsonvaga = objectMapper.writeValueAsString(dtovagasetup);

        mockMvc.perform(post("/vaga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonvaga))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    @DisplayName("Deve cadastrar corretamente uma vaga")
    void cadastrar() throws Exception {

        cadastrarVaga_dto dtovaga  = new cadastrarVaga_dto();
        dtovaga.setTitulo("titulo");
        dtovaga.setDescricao("descricao");
        dtovaga.setNivel("iniciante");
        dtovaga.setInformacoes("informacoes");
        dtovaga.setModelo("presencial");
        dtovaga.setLocalizacao("sp");
        dtovaga.setPalavrasChave("teste alo");
        dtovaga.setTipoContrato("clt");

        List<habilidade_apenas_formulario_vaga> lista = new ArrayList<>();
        habilidade_apenas_formulario_vaga hab1  = new habilidade_apenas_formulario_vaga();
        hab1.setHabilidade("java");
        hab1.setPeso(1);
        lista.add(hab1);

        dtovaga.setHabilidades(lista);

        String jsonvaga = objectMapper.writeValueAsString(dtovaga);

        mockMvc.perform(post("/vaga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonvaga))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve falhar ao tentar enviar um idioma ou habilidade inexistente")
    void cadastrarFalhaInexistente() throws Exception {

        cadastrarVaga_dto dtovaga  = new cadastrarVaga_dto();
        dtovaga.setTitulo("titulo");
        dtovaga.setDescricao("descricao");
        dtovaga.setNivel("iniciante");
        dtovaga.setInformacoes("informacoes");
        dtovaga.setModelo("presencial");
        dtovaga.setLocalizacao("sp");
        dtovaga.setPalavrasChave("teste alo");
        dtovaga.setTipoContrato("clt");

        List<habilidade_apenas_formulario_vaga> lista = new ArrayList<>();
        habilidade_apenas_formulario_vaga hab1  = new habilidade_apenas_formulario_vaga();
        hab1.setHabilidade("linux");
        hab1.setPeso(1);
        lista.add(hab1);

        dtovaga.setHabilidades(lista);

        String jsonvaga = objectMapper.writeValueAsString(dtovaga);

        mockMvc.perform(post("/vaga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonvaga))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    @DisplayName("Deve listar corretamente a vaga criada")
    void listar() throws Exception {

        mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("titulo"))
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    @DisplayName("Deve criar corretamente a candidatura em uma vaga")
    void candidatar() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve retornar corretamente a porcentagem de candidatos que possuem todas as habilidades, neste caso: 100")
    void calcularporcentagem() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/vaga/teste/"+idvaga))
                .andExpect(status().isOk())
                .andExpect(content().string("a porcentagem de candidados que possuem todas as habilidades é: 100"))
                .andReturn();

    }

    @Test
    @DisplayName("Deve retornar a quantidade de pessoas em cada etapa corretamente, neste caso, 1 pessoa na triagem")
    void calcularPessoasEtapa() throws Exception{

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/vaga/etapas/"+idvaga))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapas.TRIAGEM").value(1))
                .andReturn();


    }

    @Test
    @DisplayName("Deve retornar os melhores candidatos em ordem utilizando o algoritmo de 10 melhores")
    void melhores() throws Exception{

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        cadastroCandidato_dto dto = new cadastroCandidato_dto();

        dto.setPassword("12345678");
        dto.setNome("john marcio");
        dto.setEmail("marcio@hotmail.com");
        dto.setTelefone("(11) 912234-5678");
        dto.setLinkedin("linkexemplonaovalido.com");
        dto.setGithub("https://githubsemsentido.com");
        dto.setCidade("cidade exemplo");
        dto.setEstado("estado exemplo");

        List<habilidade_model_apenas_formulario> listahabilidades = new ArrayList<>();
        habilidade_model_apenas_formulario habilidade = new habilidade_model_apenas_formulario();
        habilidade.setHabilidade("java");
        habilidade.setTempoExperiencia(20);
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

        String jsoncadastro2 = objectMapper.writeValueAsString(dto);

        MvcResult resultcadastro2 = mockMvc.perform(post("/candidato")
                        .contentType(MediaType.APPLICATION_JSON).content(jsoncadastro2))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato2 = resultcadastro2.getResponse().getContentAsString();
        candidato_model candidato2 = objectMapper.readValue(dadoscandidato2, candidato_model.class);

        Long idcandidato2 = candidato2.getId();

        cadastroCandidatura_dto dtocandidatura2 = new cadastroCandidatura_dto();
        dtocandidatura2.setIdvaga(idvaga);
        dtocandidatura2.setIdcandidato(idcandidato2);

        String jsoncandidatura2 = objectMapper.writeValueAsString(dtocandidatura2);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura2))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/vaga/melhores/"+idvaga))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].candidato.nome").value("john marcio"))
                .andExpect(jsonPath("$[1].candidato.nome").value("john krammer"))
                .andReturn();

    }

    @Test
    @DisplayName("Deve retornar corretamente a lista de compatbilidades")
    void listaCompatibilidades() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/vaga/listaCompatibilidades/"+idvaga))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].compatibilidadeEmPorcentagem").value(27))
                .andReturn();



    }

    @Test
    @DisplayName("Deve retornar corretamente a compatibilidade de apenas um candidato")
    void calcularCompatibilidade() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        compatibilidadeUnica_dto dtocalcularcompatibilidade = new compatibilidadeUnica_dto();
        dtocalcularcompatibilidade.setVagaid(idvaga);
        dtocalcularcompatibilidade.setCandidatoid(idcandidato);

        String jsoncompatibilidade = objectMapper.writeValueAsString(dtocalcularcompatibilidade);

        mockMvc.perform(post("/vaga/calcularCompatibilidade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncompatibilidade))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.compatibilidadeEmPorcentagem").value(27))
                .andReturn();

    }

    @Test
    @DisplayName("Deve avançar a etapa corretamente")
    void avancarEtapa() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();


        avancarEtapa_dto dtoavancar = new avancarEtapa_dto();
        dtoavancar.setVagaid(idvaga);
        dtoavancar.setCandidatoid(idcandidato);

        String jsonavancar = objectMapper.writeValueAsString(dtoavancar);

        mockMvc.perform(post("/vaga/avancarEtapa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonavancar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapa").value("ENTREVISTA"))
                .andReturn();

    }

    @Test
    @DisplayName("Deve desistir da vaga corretamente")
    void desistencia() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        avancarEtapa_dto dtoavancar = new avancarEtapa_dto();
        dtoavancar.setVagaid(idvaga);
        dtoavancar.setCandidatoid(idcandidato);

        String jsonavancar = objectMapper.writeValueAsString(dtoavancar);

        mockMvc.perform(post("/vaga/desistencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonavancar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapa").value("DESISTENCIA"))
                .andReturn();


    }

    @Test
    @DisplayName("Deve finalizar uma candidatura corretamente")
    void finalizar() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        avancarEtapa_dto dtoavancar = new avancarEtapa_dto();
        dtoavancar.setVagaid(idvaga);
        dtoavancar.setCandidatoid(idcandidato);

        String jsonavancar = objectMapper.writeValueAsString(dtoavancar);

        mockMvc.perform(post("/vaga/finalizarAplicacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonavancar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapa").value("FINALIZADO"))
                .andReturn();

    }

    @Test
    @DisplayName("Deve finalizar a vaga corretamente")
    void finalizarVaga() throws Exception {

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

        MvcResult result2 = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result2.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});
        Long idvaga = vagas.get(0).getId();

        finalizarVaga_dto dto4 = new finalizarVaga_dto();
        dto4.setVagaid(idvaga);

        String jsonfinalizar = objectMapper.writeValueAsString(dto4);


        mockMvc.perform(post("/vaga/finalizarVaga")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonfinalizar))
                .andExpect(jsonPath("$.status").value("finalizado"))
                .andExpect(status().isOk());



    }

    @Test
    @DisplayName("Deve retornar corretamente vagas ativas")
    void listarVagasAtivas() throws Exception {

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

        MvcResult result2 = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result2.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});
        Long idvaga = vagas.get(0).getId();

        finalizarVaga_dto dto4 = new finalizarVaga_dto();
        dto4.setVagaid(idvaga);

        String jsonfinalizar = objectMapper.writeValueAsString(dto4);


        mockMvc.perform(post("/vaga/finalizarVaga")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonfinalizar))
                .andExpect(jsonPath("$.status").value("finalizado"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/vaga/listarVagasAtivas"))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("Deve retornar os candidatos em uma etapa corretamente, neste caso triagem")
    void listarPorUmaEtapa() throws Exception {

        MvcResult result = mockMvc.perform(get("/vaga"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadosvaga = result.getResponse().getContentAsString();
        List<vaga_model> vagas = objectMapper.readValue(dadosvaga, new  TypeReference<List<vaga_model>>(){});

        MvcResult result2 = mockMvc.perform(get("/candidato"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(status().isOk())
                .andReturn();

        String dadoscandidato = result2.getResponse().getContentAsString();
        List<candidato_model> candidatos = objectMapper.readValue(dadoscandidato, new  TypeReference<List<candidato_model>>(){});

        Long idvaga = vagas.get(0).getId();
        Long idcandidato = candidatos.get(0).getId();

        cadastroCandidatura_dto dtocandidatura = new cadastroCandidatura_dto();
        dtocandidatura.setIdvaga(idvaga);
        dtocandidatura.setIdcandidato(idcandidato);

        String jsoncandidatura = objectMapper.writeValueAsString(dtocandidatura);

        mockMvc.perform(post("/vaga/candidatura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsoncandidatura))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/vaga/listarPorUmaEtapa?idvaga=" + idvaga+"&etapa=TRIAGEM"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].candidato.nome").value("john krammer"))
                .andExpect(jsonPath("$[0].compatibilidadeEmPorcentagem").value(27))
                .andExpect(status().isOk())
                .andReturn();



    }





}