package com.rh.api_rh.funcionario;

import com.rh.api_rh.DTO.aplicacao.funcionario.atualizarEndereco_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.atualizarfuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.emailnotificarcadastro_dto;
import com.rh.api_rh.DTO.login.aceitartermo_dto;
import com.rh.api_rh.candidato.candidato_service;
import com.rh.api_rh.funcionario.endereco.endereco_model;
import com.rh.api_rh.funcionario.endereco.endereco_repository;
import com.rh.api_rh.funcionario.endereco.endereco_service;
import com.rh.api_rh.funcionario.fila_exclusao.fila_exclusao_model;
import com.rh.api_rh.funcionario.fila_exclusao.fila_exclusao_repository;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.setor.setor_service;
import com.rh.api_rh.funcionario.telefone.telefone_model;
import com.rh.api_rh.funcionario.telefone.telefone_repository;
import com.rh.api_rh.funcionario.telefone.telefone_service;
import com.rh.api_rh.usuario.usuario_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.rh.api_rh.util.email_service;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class funcionario_service {

    @Autowired
    private funcionario_repository funcionario_repository;
    @Autowired
    private endereco_service endereco_service;
    @Autowired
    private telefone_service telefone_service;
    @Autowired
    private com.rh.api_rh.usuario.usuario_repository usuario_repository;
    @Autowired
    private setor_service setor_service;
    @Autowired
    private telefone_repository telefone_repository;
    @Autowired
    private funcionario_mapper funcionario_mapper;
    @Autowired
    private email_service email_service;
    @Autowired
    private log_repository log_repository;
    @Autowired
    private fila_exclusao_repository fila_exclusao_repository;
    @Autowired
    private endereco_repository endereco_repository;

    public funcionario_model cadastrar(funcionario_model funcionario) {

        try {

            funcionario.setNome(funcionario.getNome().toLowerCase());
            funcionario.setEmail(funcionario.getEmail().toLowerCase());
            funcionario.setFuncao(funcionario.getFuncao().toLowerCase());

            funcionario_repository.save(funcionario);

            log_model log = new log_model();
            log.setRegistro(funcionario.getIdusuario().getRegistro());
            log.setAcao("Novo funcionário cadastrado no sistema com registro: "+funcionario.getIdusuario().getRegistro());
            log.setTipo("funcionario");
            log.setData(new Date());
            log_repository.save(log);


            return funcionario;




        } catch (Exception e) {

            telefone_service.excluir(funcionario.getId_telefone());
            endereco_service.deletar(funcionario.getId_endereco());

            Logger log = LoggerFactory.getLogger(funcionario_service.class);
            log.error("Erro ao criar novo funcionario: " + e.getMessage());

            return null;
        }

    }

    public List<funcionario_model> listar() {

        return funcionario_repository.findByStatus("ativo");

    }

    @Scheduled(fixedRate = 50000)
    public void excluir() {

       try {

           LocalDate hoje = LocalDate.now();

           List<fila_exclusao_model> fila = fila_exclusao_repository.findByDataexclusao(hoje);
           if (fila.size() > 0) {

               for (fila_exclusao_model item : fila) {

                   funcionario_model funcionario = buscar(item.getIdfuncionario());
                   funcionario_repository.delete(funcionario);

               }

           }


       } catch (Exception e) {
           e.printStackTrace();
       }

    }

    public String agendarExclusao(UUID id) {

        try {

            funcionario_model funcionario = buscar(id);
            funcionario.setStatus("desligado");
            funcionario.getIdusuario().setStatus("DESATIVADO");
            funcionario_repository.save(funcionario);
            fila_exclusao_model agendamento = new fila_exclusao_model();
            agendamento.setDataexclusao(LocalDate.now().plusYears(5));
            agendamento.setIdfuncionario(funcionario.getId());
            fila_exclusao_repository.save(agendamento);
            return "exlusao agendada";

        } catch (Exception e) {
            return "erro ao agendar";
        }

    }

    public funcionario_model buscar(UUID id) {

        try {
            Optional<funcionario_model> funcionario = funcionario_repository.findById(id);
            if (funcionario.isPresent()) {
                return funcionario.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o funcionário", e);
        }

    }

    @Transactional(rollbackOn =  Exception.class)
    public String atualizar(atualizarfuncionario_dto dto, UUID idrh) {

        try {
            Optional<funcionario_model> data = funcionario_repository.findByEmail(dto.getEmail());
            if (data.isPresent()) {
                funcionario_model funcionario = data.get();

                float salarioantigo = funcionario.getSalario();
                String contaantiga = funcionario.getContabancaria();

                funcionario.setContabancaria(dto.getContabancaria());
                funcionario.setEmail(dto.getEmail());
                funcionario.setSalario(dto.getSalario());
                funcionario.setFuncao(dto.getFuncao());

                setor_model setor = setor_service.pesquisa(dto.getIdsetor());
                if (setor != null) {
                    funcionario.setIdsetor(setor);
                } else {
                    return "Setor inexistente";
                }

                if (dto.getTelefonenovo() != null) {

                    telefone_model telefone = telefone_service.buscar(dto.getTelefone());
                    if (telefone != null) {
                        try {

                            telefone_model telefonejaexiste = telefone_service.buscar(dto.getTelefonenovo());
                            if (telefonejaexiste != null) {
                                return "novo numero de telefone ja existe";
                            }

                            telefone.setNumero(dto.getTelefonenovo());
                            telefone_repository.save(telefone);
                        } catch (Exception e) {
                            return "Erro ao atualizar telefone";
                        }
                    }

                }

                if (dto.getEmailnovo() != null) {

                    Optional<funcionario_model> funcionariojaexiste = funcionario_repository.findByEmail(dto.getEmailnovo());
                    if (funcionariojaexiste.isPresent()) {
                        return "email novo ja em uso";
                    }

                    funcionario.setEmail(dto.getEmailnovo());

                }



                funcionario_repository.save(funcionario);


                Optional<funcionario_model> funcionariorh = funcionario_repository.findById(idrh);
                if (funcionariorh.isPresent()) {
                    String registroDoRh = funcionariorh.get().getIdusuario().getRegistro();

                    String texto;

                    if (salarioantigo != dto.getSalario() || (!contaantiga.equals(dto.getContabancaria()))) {
                        texto = "O usuário RH de registro: " + registroDoRh + " realizou mudanças nas informações do funcionário de registro: " + funcionario.getIdusuario().getRegistro() + " " +
                                "mudanças críticas foram feitas: salario alterado de: " + salarioantigo+ " para "+dto.getSalario()+ " conta bancaria: " + contaantiga+" para "+dto.getContabancaria();
                    } else {
                        texto = "O usuário RH de registro: " + registroDoRh + " realizou mudanças nas informações do funcionário de registro: " + funcionario.getIdusuario().getRegistro();;
                    }

                    log_model log = new log_model();
                    log.setAcao(texto);
                    log.setRegistro(registroDoRh);
                    log.setData(new Date());
                    log.setTipo("funcionario");
                    log_repository.save(log);


                    return ("Atualizado com sucesso!");
                } else {
                    return ("erro ao buscar funcionario rh");
                }



            }
        } catch (Exception e) {

            Logger log = LoggerFactory.getLogger(funcionario_service.class);
            log.error("Erro ao atualizar o funcionario: " + e.getMessage());

            throw new RuntimeException("Erro ao atualizar o funcionario", e);
        }

        return "Um erro inesperado!";
    }

    public String aceitarTermo(aceitartermo_dto dto) {

        try {
            Optional<funcionario_model> data = funcionario_repository.findById(dto.getIdfuncionario());
            if (data.isPresent()) {
                funcionario_model funcionario = data.get();
                funcionario.getIdusuario().setAceitoutermos(true);
                funcionario_repository.save(funcionario);
                return ("Atualizado com sucesso!");
            } else {
                return ("Deu ruim");
            }
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String generateadmin() {

        cadastroFuncionario_dto dto = new cadastroFuncionario_dto();


        dto.setNome("Adrian");
        dto.setFuncao("Administrador do sistema");
        dto.setData_nascimento("2000-01-01");
        dto.setCpf("000.000.000-00");
        dto.setEmail("victoryuzoumc@gmail.com");
        Cargo cargo = Cargo.ADMIN;
        dto.setCargo(cargo);
        dto.setSalario(20000.00f);
        dto.setContabancaria("56165156");
        dto.setDataentrada("2024-06-01");
        dto.setCep("000000000");
        dto.setLogradouro("rua do adrian");
        dto.setBairro("bairro do adrian");
        dto.setCidade("cidade do adrian");
        dto.setEstado("estado do adrian");
        dto.setNumero("154");
        dto.setComplemento("complemento do adrian");
        dto.setNumerotelefone("11900000000");
        dto.setNumerosetor("1");

        emailnotificarcadastro_dto dados  = funcionario_mapper.convert(dto);
        funcionario_model funcionario = dados.getFuncionario();
        usuarioprovisorio provisorio = dados.getProvisorio();

        System.out.println(provisorio.getRegistro());
        System.out.println(provisorio.getSenha());

        try {
            funcionario_model result = cadastrar(funcionario);
            if (result != null) {
                email_service.enviarcadastro(funcionario.getEmail(), provisorio);

                Logger log = LoggerFactory.getLogger(funcionario_model.class);
                log.info("Novo funcionário cadastrado no sistema com registro: "+funcionario.getIdusuario().getRegistro());

                return ("Cadastrado com sucesso!");
            } else {
                return ("Erro ao cadastrarParaCandidato funcionario!");
            }
        } catch (Exception e) {
        return e.getMessage();}
    }

    public List<funcionario_model> listarPorSetorRecebendoLista(List<Long> idsetor) {

        List<funcionario_model> result = new ArrayList<>();

        try {
            for (Long id : idsetor) {

                setor_model setor = setor_service.pesquisa(id);

                List<funcionario_model> listaauxiliar = funcionario_repository.findByIdsetor(setor);

                result.addAll(listaauxiliar);

            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public List<funcionario_model> listarPorFuncaoRecebendoLista(List<String> funcoes) {

        List<funcionario_model> result = new ArrayList<>();

        try {
            for (String funcao : funcoes) {


                List<funcionario_model> listaauxiliar = funcionario_repository.findByFuncao(funcao);

                result.addAll(listaauxiliar);

            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public List<funcionario_model> listarPorNomeRecebendoLista(List<String> nomes) {

        List<funcionario_model> result = new ArrayList<>();

        try {
            for (String nome : nomes) {


                List<funcionario_model> listaauxiliar = funcionario_repository.findByNome(nome);

                result.addAll(listaauxiliar);

            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public List<fila_exclusao_model> listarExclusao() {
        try {
            return fila_exclusao_repository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(rollbackOn =  Exception.class)
    public funcionario_model atualizarEndereco(atualizarEndereco_dto dto, UUID idrh) {

       try {

           funcionario_model rh = buscar(idrh);

           funcionario_model funcionario = buscar(dto.getFuncionarioid());

           endereco_model endereco = funcionario.getId_endereco();

           endereco.setBairro(dto.getBairro());
           endereco.setCidade(dto.getCidade());
           endereco.setEstado(dto.getEstado());
           endereco.setCep(dto.getCep());
           endereco.setComplemento(dto.getComplemento());
           endereco.setNumero(dto.getNumero());
           endereco.setLogradouro(dto.getLogradouro());

           endereco_repository.save(endereco);

           String registroDoRh = rh.getIdusuario().getRegistro();
           String texto = "O usuário RH de registro: " + registroDoRh + " realizou mudanças nas informações do funcionário de registro: " + funcionario.getIdusuario().getRegistro();;
           log_model log = new log_model();
           log.setAcao(texto);
           log.setRegistro(registroDoRh);
           log.setData(new Date());
           log.setTipo("funcionario");
           log_repository.save(log);

           return  funcionario;

       } catch (Exception e) {
           throw new RuntimeException(e);
       }

    }





}
