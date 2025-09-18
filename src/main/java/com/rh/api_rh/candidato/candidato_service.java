package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.aplicacao.candidato.atualizarCandidato_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.enviarEmailNovaVaga_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.retornarPerfil_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatoMapeado_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidato_dto;
import com.rh.api_rh.DTO.login.trocaSenhaCandidato_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.experiencia.experiencia_repository;
import com.rh.api_rh.candidato.experiencia.experiencia_service;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_repository;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_service;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_service;
import com.rh.api_rh.util.email_service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class candidato_service {

    @Autowired
    private candidato_repository candidatorepository;

    @Autowired
    private habilidade_service habilidadeService;

    @Autowired
    private experiencia_service experienciaService;

    @Autowired
    private formacaoAcademica_service formacaoAcademicaService;

    @Autowired
    private experiencia_repository experienciarepository;

    @Autowired
    private idioma_service idiomaService;

    @Autowired
    private candidato_habilidade_repository candidatohabilidaderepository;

    @Autowired
    private candidato_idioma_repository candidatoidiomarepository;


    @Autowired
    private cadastroCandidatoMapper mapper;

    @Autowired
    private email_service emailService;

    @Autowired
    private formacaoAcademica_repository formacaoAcademicarepository;

    @Autowired
    private candidato_vaga_repository candidatovagaRepository;

    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;

    @Transactional(rollbackOn = Exception.class)
    public String cadastrar(cadastroCandidato_dto dto){

        cadastroCandidatoMapeado_dto dto_mapeado = mapper.convert(dto);

        List<experiencia_model> experiencias = dto_mapeado.getExperiencias();
        List<habilidade_model_apenas_formulario> habilidades = dto_mapeado.getHabilidades();
        List<idioma_model_apenas_formulario> idiomas = dto_mapeado.getIdiomas();
        List<formacaoAcademica_model> formacao =  dto_mapeado.getFormacaoAcademica();

        try {

            String palavraSalt = dto_mapeado.getCandidato().getEmail() + dto_mapeado.getCandidato().getPassword() + salt_secret;

            String senhahash = new BCryptPasswordEncoder().encode(palavraSalt);

            dto_mapeado.getCandidato().setPassword(senhahash);


            candidato_model candidato = candidatorepository.save(dto_mapeado.getCandidato());

            if (habilidades.size() > 0) {
                String res1 = habilidadeService.cadastrarParaCandidato(habilidades, candidato);
                if (!res1.equals("sucesso")) {
                    throw new IllegalArgumentException("Alguma habilidade não está no sistema");
                }
            }

            if (experiencias.size() > 0) {
                experienciaService.cadastrar(experiencias, candidato);
            }

            if (idiomas.size() > 0) {
                String res2 = idiomaService.cadastrarParaCandidato(idiomas, candidato);
                if  (!res2.equals("sucesso")) {
                    throw new IllegalArgumentException("Algum idioma não está no sistema");
                }
            }

            if (formacao.size() > 0) {
                formacaoAcademicaService.cadastrar(formacao, candidato);
            }


        } catch (Exception e) {
                throw e;
        }

        return("candidato criado com sucesso ");

    }

    public List<candidato_model> listar(){
        try {
            return candidatorepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_habilidade_model> listarhabilidades(Long id){
        try {
            Optional<List<candidato_habilidade_model>> lista = candidatohabilidaderepository.findByCandidatoId(id);

            if (lista.isPresent()) {
                return lista.get();
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_idioma_model> listaridiomas(Long id){
        try {
            Optional<List<candidato_idioma_model>> lista = candidatoidiomarepository.findByCandidatoId(id);

            if (lista.isPresent()) {
                return lista.get();
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_model> listarComBaseHabilidades(List<String> requisitos ){

        List<candidato_model> res =  new ArrayList<candidato_model>();

        List<candidato_model> todos = candidatorepository.findAll();

        Integer tamanhorequisitos = requisitos.size();

        for (candidato_model candidato : todos) {
            Integer cumprido = 0;
            List<candidato_habilidade_model> habilidadesCandidato = candidatohabilidaderepository.findByCandidato(candidato);

            for (candidato_habilidade_model habilidade : habilidadesCandidato) {
                String habilidadeString = habilidade.getHabilidade().getHabilidade();

                for (String requisito : requisitos) {

                    if (requisito.equals(habilidadeString)) {

                        cumprido++;
                        break;

                    }

                }

            }
            if (cumprido.equals(tamanhorequisitos)) {
                res.add(candidato);
            }

        }

        return res;

    }

    public String enviarEmailParaCandidatosComHabilidade(enviarEmailNovaVaga_dto dto) {

        if (dto.getCandidatos().isEmpty()) {
            return "lista vazia";
        }

        try {

            for (candidato_model candidato : dto.getCandidatos()) {

                String aux = emailService.enviarEmailNovaVaga(candidato, dto.getTituloVaga());
                if (!aux.equals("Email enviado com sucesso!")) {
                    return "erro ao enviar um dos emails";
                }

            }

            return "emails enviados com sucesso!";

        }  catch (Exception e) {
            return "erro ao enviar um dos emails";
        }

    }

    public candidato_model buscar(String email) {

        try{
            Optional<candidato_model> candidato = candidatorepository.findByEmail(email);
            if (candidato.isPresent()) {
                return candidato.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_vaga_model> buscarCandidaturasUsuario(Long id) {

        try {
            List<candidato_vaga_model> lista =  new ArrayList<>();

            Optional<candidato_model> candidato = candidatorepository.findById(id);
            if (candidato.isPresent()) {

                lista = candidatovagaRepository.findByCandidato(candidato.get());
                return lista;

            } else {
                return lista;
            }

        } catch (Exception e) {
            return null;
        }

    }

    public String aceitartermo(Long id) {

        try {
            Optional<candidato_model> candidato = candidatorepository.findById(id);
            if (candidato.isPresent()) {
                candidato.get().setAceitouTermo(true);
                candidatorepository.save(candidato.get());
                return ("termo aceito");
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public retornarPerfil_dto perfil(Long id) {

        try {

            retornarPerfil_dto res =  new retornarPerfil_dto();

            Optional<candidato_model> candidato = candidatorepository.findById(id);
            if (candidato.isPresent()) {

                res.setCidade(candidato.get().getCidade());
                res.setEstado(candidato.get().getEstado());
                res.setNome(candidato.get().getNome());
                res.setTelefone(candidato.get().getTelefone());
                res.setEmail(candidato.get().getEmail());
                res.setGithub(candidato.get().getGithub());
                res.setLinkedin(candidato.get().getLinkedin());

                List<formacaoAcademica_model> listaFormacao = formacaoAcademicarepository.findByCandidato(candidato.get());
                res.setFormacaoAcademica(listaFormacao);

                List<experiencia_model> listaExperiencia = experienciarepository.findByCandidato(candidato.get());
                res.setExperiencias(listaExperiencia);

                List<habilidade_model_apenas_formulario> listaHabilidadeRes = new ArrayList<>();
                List<candidato_habilidade_model> listaHabilidade = candidatohabilidaderepository.findByCandidato(candidato.get());
                for (candidato_habilidade_model habilidade : listaHabilidade) {

                    habilidade_model_apenas_formulario item =  new habilidade_model_apenas_formulario();
                    item.setHabilidade(habilidade.getHabilidade().getHabilidade());
                    item.setTempoExperiencia(habilidade.getExperienciaEmMeses());
                    listaHabilidadeRes.add(item);

                }
                res.setHabilidades(listaHabilidadeRes);

                List<idioma_model_apenas_formulario> listaIdiomaRes = new ArrayList<>();
                Optional<List<candidato_idioma_model>> listaIdioma = candidatoidiomarepository.findByCandidatoId(candidato.get().getId());
                if (listaIdioma.isPresent()) {

                    for (candidato_idioma_model idioma : listaIdioma.get()) {

                        idioma_model_apenas_formulario item =   new idioma_model_apenas_formulario();
                        item.setIdioma(idioma.getIdioma().getIdioma());
                        item.setNivel(idioma.getNivel());
                        listaIdiomaRes.add(item);

                    }

                }

                res.setIdiomas(listaIdiomaRes);

                return res;


            } else {
                return null;
            }


        } catch (Exception e) {
            return null;
        }

    }

    @Transactional(rollbackOn =  Exception.class)
    public candidato_model atualizar(atualizarCandidato_dto dto) {

        try {
            retornarPerfil_dto candidatoAntigo = perfil(dto.getId());

            Optional<candidato_model> candidato = candidatorepository.findById(dto.getId());
            if (candidato.isPresent()) {

                candidato.get().setCidade(dto.getCidade());
                candidato.get().setEstado(dto.getEstado());
                candidato.get().setGithub(dto.getGithub());
                candidato.get().setLinkedin(dto.getLinkedin());
                candidato.get().setTelefone(dto.getTelefone());



                for (experiencia_model experiencia : candidatoAntigo.getExperiencias()) {

                    System.out.println("Empresa: " + experiencia.getEmpresa());
                    System.out.println("Descrição: " + experiencia.getDescricao());
                    System.out.println("Data de Início: " + experiencia.getDataInicio());
                    System.out.println("Data de Fim: " + experiencia.getDataFim());

                    Boolean aindaExiste = false;
                    for (experiencia_model aux : dto.getExperiencias()) {

                        System.out.println("Empresa aux " + aux.getEmpresa());
                        System.out.println("Descrição aux: " + aux.getDescricao());
                        System.out.println("Data de Início aux: " + aux.getDataInicio());
                        System.out.println("Data de Fim aux: " + aux.getDataFim());

                        if (experienciaService.comparar(experiencia, aux)) {
                            aindaExiste = true;
                            dto.getExperiencias().remove(aux);
                            break;
                        } else {
                            System.out.println("nao é igual");
                        }

                    }
                    if (!aindaExiste) {

                        experienciarepository.delete(experiencia);

                    }


                }

                if (dto.getExperiencias().size() > 0) {

                    experienciaService.cadastrar(dto.getExperiencias(), candidato.get());

                }



                for (formacaoAcademica_model formacao : candidatoAntigo.getFormacaoAcademica()) {

                    Boolean aindaExiste = false;
                    for (formacaoAcademica_model aux : dto.getFormacaoAcademica()) {

                        if (formacaoAcademicaService.comparar(formacao, aux)) {
                            aindaExiste = true;
                            dto.getFormacaoAcademica().remove(aux);
                            break;
                        }

                    }
                    if (!aindaExiste) {

                        formacaoAcademicarepository.delete(formacao);

                    }

                }

                if  (dto.getFormacaoAcademica().size() > 0) {

                    formacaoAcademicaService.cadastrar(dto.getFormacaoAcademica(), candidato.get());

                }



                List<candidato_habilidade_model> listaHabilidade = candidatohabilidaderepository.findByCandidato(candidato.get());
                for (candidato_habilidade_model habilidade : listaHabilidade) {

                    Boolean aindaExiste = false;
                    for (habilidade_model_apenas_formulario aux : dto.getHabilidades()) {

                        if (habilidade.getHabilidade().getHabilidade().equals(aux.getHabilidade())) {
                            aindaExiste = true;
                            dto.getHabilidades().remove(aux);

                            if (!habilidade.getExperienciaEmMeses().equals(aux.getTempoExperiencia())) {

                                habilidade.setExperienciaEmMeses(aux.getTempoExperiencia());
                                candidatohabilidaderepository.save(habilidade);

                            }

                            break;
                        }

                    }

                    if (!aindaExiste) {
                        candidatohabilidaderepository.delete(habilidade);
                    }


                }

                habilidadeService.cadastrarParaCandidato(dto.getHabilidades(), candidato.get());


                Optional<List<candidato_idioma_model>> listaIdioma = candidatoidiomarepository.findByCandidatoId(candidato.get().getId());
                if (listaIdioma.isPresent()) {

                    for (candidato_idioma_model idioma : listaIdioma.get()) {

                        Boolean aindaExiste = false;
                        for (idioma_model_apenas_formulario aux : dto.getIdiomas()) {

                            if (idioma.getIdioma().getIdioma().equals(aux.getIdioma())) {

                                aindaExiste = true;
                                dto.getIdiomas().remove(aux);
                                if (!idioma.getNivel().equals(aux.getNivel())) {

                                    idioma.setNivel(aux.getNivel());
                                    candidatoidiomarepository.save(idioma);

                                }
                                break;

                            }

                        }

                        if (!aindaExiste) {
                            candidatoidiomarepository.delete(idioma);

                        }

                    }


                }
                idiomaService.cadastrarParaCandidato(dto.getIdiomas(), candidato.get());


                candidatorepository.save(candidato.get());

                return candidato.get();


            } else {
                return null;
            }
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

    }

    @Transactional(rollbackOn =  Exception.class)
    public String excluir(Long id) {

        try {



            Optional<candidato_model> candidato = candidatorepository.findById(id);
            if (candidato.isPresent()) {

                candidatohabilidaderepository.deleteByCandidato(candidato.get());
                candidatoidiomarepository.deleteByCandidato(candidato.get());

                candidatorepository.delete(candidato.get());
                return ("excluido com sucesso");
            } else {
                return ("falha ao excluir");
            }

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

    }

    public String trocarSenha(trocaSenhaCandidato_dto dto) {

        try {
            System.out.println(dto.getEmail());

            Optional<candidato_model> data = candidatorepository.findByEmail(dto.getEmail());
            if (data.isPresent()) {

                candidato_model candidato = data.get();

                String senhanova = candidato.getEmail() + dto.getNovasenha() + salt_secret;
                String senhaoriginal = candidato.getPassword();

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                if (encoder.matches(senhanova, senhaoriginal)) {
                    return "não pode ser a mesma senha";
                } else {

                    String senhanovahash = encoder.encode(senhanova);
                    candidato.setPassword(senhanovahash);
                    candidatorepository.save(candidato);

                    return "sucesso";

                }

            } else {
                return ("candidato não encontrado");
            }
        } catch (Exception e) {
            return ("falha ao trocar senha");
        }

    }


}
