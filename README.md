# API-RESTFUL SISTEMA DE RH UTILIZANDO SPRINGBOOT

## DESCRIÇÃO
Projeto realizado utilizando java springboot para criação de um sistema de RH de fácil manipulação.

## Dependências utilizadas

* Spring web: para criação de projetos web.
* Spring Boot Dev Tools: para melhorias no processo de desenvolvimento.
* Lombok: para diminuição de códigos padrões e uma codificação mais limpa.
* H2 Database: banco em memória para praticidade nos testes.
* Spring Data JPA: para interações com o banco de dados de forma facilitada.
* Spring security: para configurações de segurança da aplicação.
* Java JWT: para criação de access tokens e refresh tokens.
* Springboot starter mail: para envio de emails personalizados.

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
	</dependencies>
```

## Aplicações utilizadas
* IntelliJ para construção do projeto
* Postman para testes de requisições

## Objetivos alcançados

* Ser uma API-RESTFUL
* Utilização de mapper
* Utilização de DTO
* Utilização de hashing + salt para armazenamento de senhas
* Utilização de verificação de tentativas de login
* Controle de permissões por cargo
* Access token
* Refresh token
* Cadastro de entidade
* Atualização de entidade
* Exclusão de entidade
* Registro de logs
* Envio de emails
* Geração de códigos de confirmação
* Estrutura MVC
* Login seguro
* Variáveis de ambiente para chave secreta de algoritmo de encriptação

## Explicações

O sistema foi codificado com divisão de pastas por entidades, onde cada pasta possui os arquivos necessários para a estrutura da entidade (Model. controller, service, repository), algumas entidades que
recebem dados vindos do front-end, possuem um arquivo de classe mapper, que em conjunto com os DTOS, são responsáveis por receber todos os dados necessários para uma requisição, e mapear de forma correta 
para a estrutura que as funções trabalham.

Exemplo: objeto DTO de cadastro recebe tudo que é necessário de uma vez, informações do funcionário, endereço, etc, e constroi de forma correta cada objeto de cada classe utilizada na função de cadastro, 
nesse caso, um objeto da classe funcionário, um objeto da classe endereço e um objeto da classe telefone, para serem utilizados nas funções correspondentes.


![image](https://github.com/user-attachments/assets/573f24d5-cc14-4c25-bfd6-7057da2f81dc)

![image](https://github.com/user-attachments/assets/9a3a899f-77a5-4c09-a17b-163c15a3442a)

Exemplo da classe mapper construindo um objeto da classe funcionário

![image](https://github.com/user-attachments/assets/7c243701-5579-4f7a-8478-aabc00a2388e)

Os arquivos service são responsáveis por conter as funções relacionadas a cada entidade

Os arquivos controller são responsáveis por gerenciar os endpoints relacionados a cada entidade

Os arquivos model são responsáveis por conter a estrutura de cada entidade

### Cadastro

O primeiro usuário ADMIN será inserido manualmente no banco de dados, após isso, ele poderá cadastrar novos usuários RH, que poderão cadastrar novos funcionários comuns.

Após o cadastro, o funcionário recebe em seu email seu par de credenciais inicial.

![image](https://github.com/user-attachments/assets/d9fdf69f-44a5-4439-9559-c8f3c8f9aab5)


### Login com token

Após a criação de um funcionário, a função de login irá utilizar um par de credenciais escolhidas manualmente para realizar a verificação, as credenciais escolhidas para o sistema foram o registro do funcionário, que é um registro
gerado automaticamente pelo sistema contendo o formato 6 letras maiúsculas e 2 números (AAAAAA00) e uma senha. A primeira senha do usuário será gerada automaticamente pelo sistema, e após o primeiro login o usuário será redirecionado 
para uma troca de senha inicial.

Quando um login é feito com sucesso, um par de tokens access token e refresh token é gerado para o usuário.

Caso ocorram muitas tentativas falhas de login, a conta do usuário poderá ser bloqueada temporariamente e depois permanentemente, tendo que solicitar ao DBA a liberação de sua conta.

os tokens serão utilizados para acessar rotas que necessitem de verificação por cargo, por exemplo, a rota de cadastro de novo funcionário poderá ser acessada apenas por um funcionário de cargo RH ou superior.

Os refresh tokens são utilizados para gerar novos access tokens toda vez que são expirados. 

Por motivos de testes, o access token possui duração de 1 minuto e o refresh token 3 minutos.

### Reset de senha

O usuário poderá requisitar a troca de senha em caso de esquecimento, sendo enviado um código de verificação para seu email, o código tem duração definida e é deletado após sua utilização ou expiração.

### Controle de permissões

o controle de permissões é feito por meio da utilização de um atributo Cargo na entidade funcionário, que é passado para o corpo dos tokens de acesso, e é verificado em cada requisição. A definição de nível de acesso
é feita indicando qual o cargo necessário para acessar a função da rota em questão.

![image](https://github.com/user-attachments/assets/84985d41-8e50-45f7-88b5-1828016b38d1)

### LOGS

O sistema gera automaticamente objetos de LOG para cada ação importante no sistema, como por exemplo: cadastro, atualização de dados, logins bem sucedidos, etc.

### Hashing de senha

A encriptação da senha é feita utilizando a técnica de SALT, onde uma chave secreta foi criada em váriavel de ambiente, e é adicionada com o registro do usuário + a senha original, para gerar um hash único e de alta complexidade.

