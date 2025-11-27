# Trabalho LPOO C3

---

## GerenciaTI:

### Sumário:

| Seção                                                          | Conteúdo                                                        |
|----------------------------------------------------------------|-----------------------------------------------------------------|
| [Conceitos do Sistema](#conceitos-do-sistema)                  | Objetivo e Para quem foi feito                                  |
| [Funcionalidades](#funcionalidades)                            | O que faz                                                       |
| [Como rodar?](#como-rodar)                                     | Como Rodar o projeto na sua máquina?                            |
| [Arquitetura](#arquitetura-do-projeto)                         | Como faz                                                        |
| [Classes de Banco de Dados](#comunicação-com-o-banco-de-dados) | Classes e Arquivos do Sistema                                   |
| [Gerenciador de Sessão](#sessão-do-usuário)                    | Classes de Gerência de Sessão do Usuário **(muito importante)** |
| [Classes de Regra de negócio](#regras-de-negócio)              | Classes Service                                                 |
| [Classes Utilitárias](#classes-utilitárias)                    | Classes utilitárias **(muito importante)**                      |
| [Classes Gerenciadoras](#classes-gerenciadoras)                | Classes Controladoras dos Gerenciadores                         |
| [Conceitos de LPOO aplicados](#conceitos-de-lpoo-aplicados)    | Conceitos das aulas aplicados                                   |

---

### Conceitos do Sistema:

#### Objetivo:
Sistema gerenciador de eventos e recursos.

### Público Alvo:
Responsáveis por eventos que precisam de uma solução digital para controle de **recursos** e **informações**.

---

### Funcionalidades:

#### Sistema de Contas:
- Criar conta;
- Entrar na sua conta;
- Separar os dados do sistema por conta;

#### Gerenciador de Clientes:
- Criar um Cliente (Poder gerir os contatos);
- Editar um Cliente;
- Inspecionar um Cliente específico (Ver todos os seus atributos);
- Filtrar Clientes numa tabela, facilitando a pesquisa;
- Remover um Cliente

#### Gerenciador de Estoques:
- Criar um Estoque (para um evento específico ou para um sub-evento do evento principal);
- Editar um Estoque;
- Inspecionar um Estoque;
- Filtrar um Estoque;

#### Gerenciador de Produtos:
- Adicionar um Produto a um Estoque;
- Editar um Produto em um Estoque;
- Inspecionar um Produto em um Estoque;
- Filtrar Produtos de um Estoque;

#### Gerenciador de Eventos:
- Adicionar um Evento
- Editar um Evento
- Inspecionar um Evento
- Filtrar Eventos

#### Gerenciador de Funcionários:
- Adicionar um Funcionário a um evento
- Editar um Funcionário
- Inspecionar um Funcionário
- Filtrar Funcionários

---

## Como Rodar

### Guia de Execução do Projeto

Este guia contém o passo a passo para configurar o ambiente e executar o projeto JavaFX com Banco de Dados via Docker.

### Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

1.  **Java JDK** (Versão 21)
2.  **Maven** (Para build e dependências)
3.  **Docker & Docker Compose** (Para rodar o banco de dados)

---

### Instalação e Configuração do Docker

O Docker Compose é responsável por subir o banco de dados automaticamente.

### Para Linux (Debian/Ubuntu)
No Linux, instalamos o motor e o plugin do compose.

```bash
# Atualize os pacotes e instale dependências

# Instale o Docker
sudo apt update
sudo apt install docker.io docker-compose

# Permita rodar docker sem 'sudo' (Opcional, mas recomendado)
sudo usermod -aG docker $USER
# (Depois disso, faça logoff e login novamente para aplicar)
```

### Verificando a instalação
Abra seu terminal e rode:
```bash
docker compose version
```
*Se aparecer a versão (ex: v2.x.x), está tudo pronto.*

---

### Como Rodar o Projeto

Siga a ordem abaixo. O banco de dados precisa estar rodando **antes** da aplicação tentar conectar.

### Subir a Infraestrutura (Banco de Dados)
Na raiz do projeto (onde está o arquivo `docker-compose.yml`), execute:

```bash
docker compose up -d
```

* **O que isso faz?** Lê o arquivo `docker-compose.yml`, baixa a imagem do banco (se não tiver), cria o container e o inicia em segundo plano (`-d` = detached).
* **Atenção:** Na primeira vez, pode demorar alguns segundos para o banco estar pronto para receber conexões.

### Executar a Aplicação
Com o banco rodando, inicie a aplicação JavaFX via Maven:

```bash
mvn javafx:run
```

* **O que isso faz?** Compila o código Java, baixa as dependências e abre a janela do sistema.

---

## Como Parar e Limpar

Quando terminar de programar ou testar, é uma boa prática desligar os containers para economizar memória do computador.

Execute:
```bash
docker compose down
```

* **O que isso faz?** Para e remove os containers e a rede virtual criada pelo Docker. (Seus dados persistem se houver um volume configurado no compose).

---

## Resumo de Comandos Rápidos

```bash
# 1. Ligar Banco (Antes de abrir o app)
docker compose up -d

# 2. Rodar App
mvn javafx:run

# 3. Desligar Banco (Ao finalizar o trabalho)
docker compose down
```

---

---

## Arquitetura do projeto:

### Tecnologias:
#### Front-End:
- JavaFX (para lógica das telas)
- Arquivos FXML (para escrita da tela)
- Scene Builder (para agilidade usando drag&drop)

#### Back-End:
- Maven (gerenciar as dependências/rodar o projeto)
- Hibernate (ORM para facilitar a comunicação com o banco de dados)
- PostgreSQL (SGBD open source)
- Docker Compose (segurança de ambiente para o SGBD)

---

## Classes principais do projeto e as suas Funções:

### Comunicação com o Banco de Dados:

---
> docker-compose.yml
- Arquivo que configura o container docker do postgreSQL.
---
> src/main/resources/hibernate.cfg.xml
- Arquivo que configura o hibernate, ele define principalmente quais classes poderão ser anotadas com @Entity e afins...
- Configura também questões do ddl-auto e show-sql.
---
>src/main/java/com/devs/trabalho/dao/**HibernateUtil**
- Responsável por criar o objeto que controla as sessões do banco de dados.
- <u><b>Apenas cria um objeto na aplicação toda</b></u>, e é obtido pelo método <u><b>getSessionFactory()</b></u>.
---
>src/main/java/com/devs/trabalho/dao/**BaseDAO**
- DAO = Data Access Object, ou seja, objeto de acesso de dados.
- É uma classe abstrata para padronizar todos os outros DAOs das outras entidades.
- O <u>\<T></u> é um Generic (igual o usado ao fazer List\<T> ex.: List<Integer>), que representará o tipo de classe especifico que o DAO trabalhará.
- O DAO somente executa métodos de banco de dados, não mexe com lógica de negócio.
---
>src/main/java/com/devs/trabalho/dao/**...**
- Dentro do pacote DAO tem os pacotes das classes que herdam da **BaseDAO**, elas podem implementar métodos do banco de dados além dos do BaseDAO.
---
>src/main/java/com/devs/trabalho/model/**...**
- Pacotes das entidades gerenciadas pelo Hibernate. 
- Dentro do main e afins funcionam como classes normais do java, mas ao serem usadas pelos DAOs, elas podem usar as anotações para executar queries no banco de dados.
- Usam a @Entity para marcar como gerenciavel pelo hibernate.

---

### Sessão do usuário
>src/main/java/com/devs/trabalho/utils/**GerenciadorSessao**
- Classe que controla o usuário logado no sistema.
- O método __**getUsuarioLogado()**__ retorna o usuário logado no momento, caso o contrário lança a exceção UsuarioNotLoggedException.
- <u>__**O sistema checa a sessão chamando o getUsuarioLogado(), e trata a exceção caso lançada. (caso a professora pergunte).**__</u>

---

### Regras de Negócio:

>src/main/java/com/devs/trabalho/service/**...**
- São as classes de serviço, elas usam os DAOs para além de executar instruções no banco de dados, executar regras de negócio.
- A baixo as classes e as suas principais regras de negócio caso perguntado:
---
>src/main/java/com/devs/trabalho/service/UsuarioService
- Executa regras de negócio do usuário.
- método __**cadastrar(usuario)**__ que somente deixa cadastrar se o usuário não existir no sistema.
- - método __**autenticar(login, senha)**__ que somente deixa autenticar se o usuário existir no sistema e se as senhas baterem.
>src/main/java/com/devs/trabalho/service/ProdutoService
- Executa regras de negócio do produto.
- métodos de CRUD que só podem ser executados caso o usuário esteja logado.
>src/main/java/com/devs/trabalho/service/EventoService
- Executa regras de negócio do Evento.
- métodos de CRUD que só podem ser executados caso o usuário esteja logado.
- método __**cadastrar(evento)**__ que automaticamente seta como dono do evento o usuário logado na hora.
>src/main/java/com/devs/trabalho/service/EstoqueService
- Executa regras de negócio do Estoque.
- métodos de CRUD que só podem ser executados caso o usuário esteja logado.
- método __**cadastrar(estoque)**__ que automaticamente seta como dono do estoque o usuário logado na hora.
>src/main/java/com/devs/trabalho/service/ClienteService
- Executa regras de negócio do Cliente.
- métodos de CRUD que só podem ser executados caso o usuário esteja logado.
- método __**cadastrar(cliente)**__ que automaticamente seta como dono do cliente o usuário logado na hora.

---

### Classes utilitárias

>src/main/java/com/devs/trabalho/utils/Tela
- Classe utilitária que representa uma Tela.
- **\<Controller>** Generics que representa o controller que controla essa tela.
- Atributo **"scene"** é o container mais alto na hierarquia, seria tipo o **JPanel** porém o mais no topo possível.
- Todos os componentes visuais da tela estão dentro do atributo **"scene"**.
- O método `exibirNoStage(stage, ...)` pega o argumento `stage` passado e o exibe na tela do usuário.
- Mais informações na classe...

>src/main/java/com/devs/trabalho/utils/JanelaPopUp
- Classe utilitária que representa uma janela estilo popup de confirmação (do tipo bloqueante, onde enquanto você não confirmar a ação ele não desaparece)
- O primeiro atributo é o caminho do fxml dele (para mostrar os componentes na tela).
- O segundo atributo é o titulo do "popup".
- O terceiro atributo é a janela "pai" dele (quem invocou o JanelaPopUp).
- Para a funcionalidade de receber a resposta do usuário, ao confirmar ou cancelar, basta:
  - Instanciar como `JanelaPopUp<ConfirmacaoController> janela`, e ai chamar o `janela.getController().getResult()` para receber um booleano.
  - Instanciar como `JanelaPopUp<Subclasse_De_FormularioBaseController> janela`, e ai chamar o `janela.getController().getResult()` para receber uma entidade salva ou null.

>src/main/java/com/devs/trabalho/utils/GerenciadorTelas
- Classe utilitária responsável por guardar a tela principal, trocar a tela principal por outras telas, e mostrar alertas de erro genéricos.

---

### Classes Gerenciadoras

>src/main/java/com/devs/trabalho/controller/entrada/EntradaController
- Controllador da tela de login e cadastro (por isso entrada).
- Responsável por validações de exceções lançadas pelas classes service.
- Responsável pelo login e cadastro do usuário.

>src/main/java/com/devs/trabalho/controller/gerenciador/GerenciadorBaseController
- Interface para padronizar os Controllers que precisam executar operações CRUD.
- Todos devem implementar os métodos: `adicionar(), editar(), remover(), consultar()`

>src/main/java/com/devs/trabalho/controller/gerenciador/GerenciadorHomeController
- Controlador responsável por controlar as telas da home.
- Todos que herdarem tem métodos de trocar telas da home.
- Dê uma olhada no código do HomeController e dos controllers de clientes e afins que fica mais claro...

>src/main/java/com/devs/trabalho/controller/gerenciador/contatos/ControladorContatos
- Interface para padronizar os Controllers que precisarem gerenciar contatos.
- Implementa o método removerContatosLista, responsável por remover um contato de uma lista de contatos.

---

### Conceitos de LPOO aplicados

#### Classes Abstratas

>src/main/java/com/devs/trabalho/model/pessoa/Pessoa
- Classe Model que representa uma Pessoa e que executa métodos no banco de dados.

>src/main/java/com/devs/trabalho/dao/BaseDAO 
- Classe DAO que cria métodos CRUD padrão para os outros DAO.

>src/main/java/com/devs/trabalho/controller/gerenciador/FormularioBaseController
- Classe Controller que representa um formulário que espera salvar ou cancelar o salvamento de uma entidade.

#### Interfaces
>src/main/java/com/devs/trabalho/controller/gerenciador/GerenciadorBaseController
- Interface que possui 1 método abstrato para remover um contato de uma lista, é implementado em controllers que precisam gerir contatos.
- Ele existe para que ItemContatoController possa gerir a lista de contatos. 

>src/main/java/com/devs/trabalho/controller/gerenciador/contatos/GerenciadorBaseController
- Interface que possui métodos CRUD abstratos para controlladores que precisam fazer CRUD em entidades.

#### Herança

>src/main/java/com/devs/trabalho/model/usuario/Usuario
- Classe que herda de Pessoa

>src/main/java/com/devs/trabalho/model/funcionario/Funcionario
- Classe que herda de Pessoa
 
>src/main/java/com/devs/trabalho/model/cliente/Cliente
- Classe que herda de Pessoa

>src/main/java/com/devs/trabalho/dao/...DAO
- Todos os DAOs herdam de BaseDAO

>src/main/java/com/devs/trabalho/gerenciador/.../Gerenciador...Controller
- Todos os Controllers que são Gerenciadores (Sem ser os de Formulário) herdam de GerenciadorHomeController

>src/main/java/com/devs/trabalho/gerenciador/.../...Controller
- Todos os Controllers que são de CRUD/Formulário (Sem ser os de Gerência) herdam de FormularioBaseController

#### Polimorfismo
>src/main/java/com/devs/trabalho/controller/gerenciador/contatos/ControladorContatos
- Interface que força a implementação de alguns métodos

>src/main/java/com/devs/trabalho/controller/gerenciador/clientes/AdicionarClienteController
- Força a implementação do método da interface ControladorContatos

>src/main/java/com/devs/trabalho/controller/gerenciador/clientes/AdicionarFuncionarioController
- Força a implementação do método da interface ControladorContatos

---

>src/main/java/com/devs/trabalho/controller/gerenciador/FormularioBaseController
- Classe abstrata para Controllers que façam CRUD em entidades

>src/main/java/com/devs/trabalho/controller/gerenciador/clientes/AdicionarProdutoController
>src/main/java/com/devs/trabalho/controller/gerenciador/clientes/EditarProdutoController
- Sobrescreve e cria a sua implementação do método salvar da calsse FormularioBaseController


#### Tratamento de Exceção
>src/main/java/com/devs/trabalho/gerenciador/...
- Todos os gerenciadores possuem tratamento de exceção.

#### Coleções
>src/main/java/com/devs/trabalho/dao/...
- Todos os DAOs tem um método findAll que retornam uma Lista

#### Leitura e Gravação
>src/main/java/com/devs/trabalho/dao/...
- Todos os DAOs interagem com o banco de dados.

#### Interface Gráfica
>src/main/resources/com/devs/trabalho/fxml/...
- Todos os arquivos .fxml são ‘interfaces’ gráficas.

