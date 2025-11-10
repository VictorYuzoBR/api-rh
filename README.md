# BIKUBE RH

## DESCRIÇÃO
Bikube rh é um projeto web de sistema de recursos humanos, criado com intenção de facilitar e otimizar processos relacionados a recrutamento e gerenciamento de funcionários da área de ti.
O projeto foi criado como projeto de conclusão de curso para a Universidade de Mogi das Cruzes no curso de engenharia de software em 2025.

Entre suas principais funcionalidades, bikube rh conta com:

- Sistema de recrutamento, contendo: área do candidato, criação de vagas, criação de candidaturas, banco de talentos(envio automático de notificações de criação de novas vagas relacionadas ao perfil do usuário),
gerenciamento de etapas (triagem, entrevista, oferta), e como principal funcionalidade, um dashboard informativo contendo diversas informações, entre elas, algumas se destacam: número de candidatos que possuem todas as
habilidades requisitadas, porcentagem de compatibilidade de um candidato com a vaga e classificação de melhores candidatos.

- Sistema de espelho de pontos, permitindo funcionários baterem pontos online, verificar os pontos do mês e baixarem um arquivo pdf do espelho de pontos. Para o lado do funcionário de recursos humanos, ele poderá:
visualizar espelhos de todos os funcionários, aplicar atestados, descrever abonos de faltas, gerar feriados internos e exportar espelhos para arquivos csv.

- Sistema de solicitação de férias, permitindo funcionários comuns realizarem a solicitação de pedido de férias de forma online, contendo todas as verificações legislativas. O sistema realiza o calculo de saldo de férias com
os dados gerados nos espelhos de pontos. O funcionário de recursos humanos poderá realizar o desconto do saldo de férias em caso de venda.

- Sistema de comunicados, criado para facilitar a comunicação entre funcionários, permitindo funcionários de recursos humanos enviarem comunicados para funcionários da empresa.


## Dependências utilizadas

* Spring web: para criação de projetos web.
* Spring Boot Dev Tools: para melhorias no processo de desenvolvimento.
* Lombok: para diminuição de códigos padrões e uma codificação mais limpa.
* H2 Database: banco em memória para praticidade nos testes.
* Spring Data JPA: para interações com o banco de dados de forma facilitada.
* Spring security: para configurações de segurança da aplicação.
* Java JWT: para criação de access tokens e refresh tokens.
* Springboot starter mail: para envio de emails personalizados.
* Sendgrid: depêndencia do sistema sendgrid de envio de emails, para realizar o envio de emails.
* Itextpdf: para facilitar a criação de arquivos pdf. (AVISO: Este projeto é de caráter educacional e sem fins lucrativos, fazendo uso da dependência itext licenciada sob AGPL)
* Json: para manipulação de arquivos json.
* Postgresql: para integração com banco de dados postgresql.
* Spring starter validation: para validação de campos em requisições.

```java
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.sendgrid</groupId>
			<artifactId>sendgrid-java</artifactId>
			<version>4.9.3</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>itextpdf</artifactId>
			<version>5.5.13.3</version>
		</dependency>

		<dependency>
			<groupId>org.json</groupId>
			<artifactId>json</artifactId>
			<version>20230227</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
				<dependency>
					<groupId>com.h2database</groupId>
					<artifactId>h2</artifactId>
					<scope>runtime</scope>
				</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>42.7.5</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>4.5.0</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
			<version>3.4.4</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>



	</dependencies>
```

## Aplicações utilizadas
* IntelliJ para construção do projeto
* Docker para criação de container do projeto
* Postman para testes de requisições
* Railway para deploy gratuíto do projeto

## Objetivos alcançados

* Utilização de status em retornos HTTP
* Utilização de mapper
* Utilização de DTO
* Utilização de hashing + salt para armazenamento de senhas
* Utilização de verificação de tentativas de login
* Controle de permissões por cargo
* Access token
* Cadastro de entidade
* Atualização de entidade
* Exclusão de entidade
* Registro de logs
* Envio de emails
* Geração de códigos de confirmação
* Estrutura híbrida MVC + DDD
* Login seguro com dois providers
* Criação de testes de integração com Junit
* Criação de container
* Deploy
* Integração com bando de dados relacional
* Programação orientada a objetos


## Arquitetura

O sistema foi codificado em uma arquitetura em camadas, combinando conceitos de MVC e DDD, com uma divisão de pastas por entidade, onde cada pasta contem todos os arquivos relacionados a ela.

<img width="269" height="238" alt="image" src="https://github.com/user-attachments/assets/8d599138-e38d-4ea1-a2b9-92ab30571240" />

## Métodos de execução do projeto

### 1 - Download zip

Pré-requisito -> PostgreSQL + PgAdmin, Conta no site SendGrid, Postman.

- Faça o download do zip do projeto
- Realize a extração da pasta em algum diretório
- Abra a pasta do projeto utilizando alguma IDE de sua escolha
- Caso sua IDE não realize o download das dependências automaticamente, realize a instalação manualmente.
- Abra o arquivo chamado Application.properties dentro do projeto, localizado na pasta Resources
- Troque as variáveis de banco de dados para corresponder ao seu banco de dados.
- No mesmo arquivo, na variável chamada SENDGRID_API_KEY, coloque sua chave gerada no site do SendGrid.
- Execute o projeto para que o banco de dados seja populado com as tabelas
- Realize a inserção manualmente utilizando PgAdmin de um setor, o dado deve ser criado na tabela setor_model.
- Realize uma requisição utilizando postman na rota GET /funcionario/generateadmin para gerar o seu admin inicial
- Os dados de acesso irão aparecer no terminal.

### - Docker image

Pré-requisito -> Docker, PostgreSQL + PgAdmin, Conta no site SendGrid, Postman.

- Utilize docker pull victoryuzo/api-rh:latest para realizar o download da imagem do container
- Execute o container substituindo os valores das variáveis de ambiente:

  docker run -d \
  --name api-rh \
  -p 8080:8080 \
  -e DB_URL=digite seu banco \
  -e DB_USER=digite o usuario do banco \
  -e DB_PASS=digite a senha do banco \
  -e DDL_AUTO=digite a forma de inicialização do banco \
  -e JWT_SECRET=digite um valor aleatorio para servir de chave para o token \
  -e SALT_SECRETWORD=digite um valor aleatorio para servir de criptografia \
  -e API_KEY=digite a sua api key do SendGrid \
  victoryuzo/api-rh:latest

- Realize a inserção manualmente utilizando PgAdmin de um setor, o dado deve ser criado na tabela setor_model.
- Realize uma requisição utilizando postman na rota GET /funcionario/generateadmin para gerar o seu admin inicial
- Os dados de acesso irão aparecer no terminal.

### Créditos

Desenvolvido por: 

- Victor Matsunaga: https://github.com/VictorYuzoBR.
- Marcio Araujo: https://github.com/MarcioPAraujo.
- Adrian Felipe: https://github.com/Adrian128018.

Repositório Front-end do projeto: https://github.com/MarcioPAraujo/bikube-frontend.




