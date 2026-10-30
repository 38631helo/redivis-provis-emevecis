Estou fazendo uma API utilizando Spring para uma escola, que permite o cadastro, listagem, busca por id e remoção de salas

application:
package escola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EscolaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EscolaApplication.class, args);
    }
}

Model:
package escola.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "escola")
public class Sala {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nomeSala;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false)
    private boolean isReservada;

    public Sala{};

    public String getNomeSala(){
        return nomeSala;
    }

    public int getCapacidade(){
        return capacidade;
    }

    public boolean getReservada(){
        return isReservada;
    }
}

Controller:
package escola.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import escola.repository.EscolaRepository;

@RestController 
@RequestMapping("/salas")
public class EscolaController{
    private final EscolaRepository repository;

    public EscolaController(EscolaRepository repository){
        this.repository = repository;
    }

    @GetMapping 
    public ResponseEntity<List<Escola>> listar(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping 
    public ResponseEntity<Escola> buscar(@PathVariable Long id){
        return repository.findById(id)
            .map(ResponseEntity.ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Escola> criar(RequestBody Escola sala){
        return ResponseEntity.status(201).body(repository.save(sala));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Escola> excluir(@PathVariable Long id){
        if(!repository.existsById(id)) { return ResponseEntity.notFound().build();}

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

Repository:
package escola.repository;
import escola.model.Sala;

public class EscolaRepository {
    public interface EscolaRepository extends JpaRepository<Escola, Long>{}
}


não possui service

o que poderia estar de errado (além de não ter o maven, etc.)?
O ChatGPT disse:

Seu código está com uma boa estrutura inicial! Você já separou as responsabilidades em model, controller e repository, o que é um bom começo para uma API REST utilizando Spring Boot.

Porém, existem alguns erros de sintaxe, importação, nomenclatura e mapeamento que impediriam a aplicação de funcionar corretamente.

Vou analisar cada classe e mostrar como corrigir.
1. Model — Classe Sala

Seu código:
java

@Entity
@Table(name = "escola")
public class Sala {

Problemas encontrados:

    Falta importar jakarta.persistence.Table.

    Você declarou public Sala{};, mas o construtor está com sintaxe incorreta.

    Faltam os setters, que serão necessários para preencher os atributos ao receber um JSON.

    É recomendável utilizar Boolean ou manter boolean, mas padronizar o nome do atributo e seus métodos.

    O nome da tabela poderia ser salas, para representar melhor a entidade.

Uma versão corrigida:
java

package escola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeSala;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false)
    private boolean reservada;

    public Sala() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeSala() {
        return nomeSala;
    }

    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public boolean isReservada() {
        return reservada;
    }

    public void setReservada(boolean reservada) {
        this.reservada = reservada;
    }
}

Por que os setters são importantes?

Imagine que você envie este JSON para cadastrar uma sala:
json

{
    "nomeSala": "Sala 01",
    "capacidade": 30,
    "reservada": false
}

O Spring utiliza o Jackson para converter o JSON em um objeto Java. Os setters permitem que os valores recebidos sejam atribuídos aos atributos da classe.
2. Repository — EscolaRepository

Seu código atual:
java

package escola.repository;
import escola.model.Sala;

public class EscolaRepository {
    public interface EscolaRepository extends JpaRepository<Escola, Long>{}
}

Aqui existem alguns problemas:

    Você declarou uma classe EscolaRepository e, dentro dela, outra interface com o mesmo nome.

    Está utilizando Escola, mas sua entidade se chama Sala.

    Falta importar JpaRepository.

    O Spring Data JPA trabalha com interfaces para criar automaticamente as implementações dos repositórios.

Versão corrigida:
java

package escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import escola.model.Sala;

public interface EscolaRepository extends JpaRepository<Sala, Long> {
}

Uma das grandes vantagens do Spring Data JPA é que você não precisa implementar manualmente os métodos básicos.

Ao estender JpaRepository, você já recebe métodos como:

Método
	

Função

save(sala)
	

Salva uma sala

findAll()
	

Lista todas as salas

findById(id)
	

Busca uma sala pelo ID

existsById(id)
	

Verifica se uma sala existe

deleteById(id)
	

Remove uma sala
3. Controller — EscolaController

Essa é a classe que apresenta a maior quantidade de problemas.
Problemas encontrados

    Você está utilizando Escola em vez de Sala.

    Falta importar java.util.List.

    Falta importar @RequestBody.

    O método buscar() não possui o /{id} no @GetMapping.

    O método buscar() utiliza ResponseEntity.ok de maneira incorreta.

    O import org.apache.catalina.connector.Response é desnecessário.

    O método criar() está com a anotação @RequestBody incorreta: ela precisa estar antes do parâmetro.

    O método excluir() retorna ResponseEntity<Sala>, mas utiliza noContent(), que representa uma resposta sem corpo.

Versão corrigida
java

package escola.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import escola.model.Sala;
import escola.repository.EscolaRepository;

@RestController
@RequestMapping("/salas")
public class EscolaController {

    private final EscolaRepository repository;

    public EscolaController(EscolaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscar(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sala> criar(@RequestBody Sala sala) {
        return ResponseEntity.status(201).body(repository.save(sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

Entendendo os endpoints

Com esse controller, você terá os seguintes endpoints:

Método HTTP
	

Endpoint
	

Função

GET
	

/salas
	

Lista todas as salas

GET
	

/salas/{id}
	

Busca uma sala pelo ID

POST
	

/salas
	

Cadastra uma sala

DELETE
	

/salas/{id}
	

Remove uma sala
4. Preciso implementar uma Service?

Não necessariamente!

Para uma API pequena, como a que você está desenvolvendo, é perfeitamente possível trabalhar diretamente com o Repository dentro do Controller.

Entretanto, em projetos maiores, é interessante adicionar uma camada Service.

A estrutura ficaria assim:

Controller

Recebe as requisições HTTP e retorna as respostas

Service

Contém as regras de negócio e validações

Repository

Responsável pelo acesso ao banco de dados

Banco de dados

Armazena as informações das salas

Por exemplo, imagine que você queira implementar uma regra:

    Uma sala não pode ter capacidade menor ou igual a zero.

    Uma sala reservada não pode ser removida.

    Não podem existir duas salas com o mesmo nome.

Essas regras poderiam ficar na Service, evitando que o Controller fique sobrecarregado.
5. Outros pontos importantes

Além das correções anteriores, existem algumas coisas que você precisa conferir para a aplicação funcionar.

    Dependências: você precisará de Spring Web, Spring Data JPA e de um driver de banco de dados (H2, MySQL, PostgreSQL etc.).

    Pacotes: mantenha model, controller e repository dentro do pacote principal escola, como já está fazendo.

    Banco de dados: configure o acesso no application.properties.

    Validação: futuramente, considere utilizar @Valid, @NotBlank e @Positive para validar os dados recebidos.

6. Testando sua API

Você pode utilizar Postman ou Insomnia para testar os endpoints.

POST — Cadastrar sala
201 Created

POST http://localhost:8080/salas
json

{
  "nomeSala": "Laboratório 01",
  "capacidade": 25,
  "reservada": false
}

GET — Listar salas
200 OK

GET http://localhost:8080/salas

GET — Buscar por ID
200 OK

GET http://localhost:8080/salas/1

DELETE — Excluir sala
204 No Content

DELETE http://localhost:8080/salas/1

Se o ID não existir, as operações de busca e exclusão retornam 404 Not Found.

Minha recomendação: primeiro faça sua API funcionar sem a Service, utilizando apenas Controller, Model e Repository. Depois, implemente a Service para entender como funciona a separação de responsabilidades.

Você está no caminho certo! Os principais problemas do seu código são erros de sintaxe, inconsistência entre Escola e Sala e alguns mapeamentos incorretos do Spring.

Entre para receber orientação personalizada, passo a passo.
Entrar
Cadastre-se grátis
Você disse:

não necessariamente precisa de um service, mas nesse caso quero ter um
O ChatGPT disse:

Perfeito! Então vamos adicionar uma camada Service à sua API, seguindo uma arquitetura em camadas utilizada em projetos Spring Boot.

A ideia é deixar cada classe responsável por uma coisa:

    Controller: recebe as requisições HTTP e retorna as respostas.

    Service: executa as regras de negócio e coordena as operações.

    Repository: realiza as operações no banco de dados.

    Model: representa a entidade Sala.

1. Estrutura de pastas

Sua estrutura ficará assim:
text

src/main/java/escola/
│
├── EscolaApplication.java
│
├── controller/
│   └── EscolaController.java
│
├── model/
│   └── Sala.java
│
├── repository/
│   └── EscolaRepository.java
│
└── service/
    └── EscolaService.java

2. Repository

Não precisamos mudar muita coisa no Repository:
java

package escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import escola.model.Sala;

public interface EscolaRepository extends JpaRepository<Sala, Long> {

}

Ele continuará responsável exclusivamente pelo acesso ao banco de dados.
3. Service

Aqui ficará a lógica das operações da sua API.

Crie o pacote service e, dentro dele, a classe EscolaService.
java

package escola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import escola.model.Sala;
import escola.repository.EscolaRepository;

@Service
public class EscolaService {

    private final EscolaRepository repository;

    public EscolaService(EscolaRepository repository) {
        this.repository = repository;
    }

    // Listar todas as salas
    public List<Sala> listar() {
        return repository.findAll();
    }

    // Buscar sala pelo ID
    public Optional<Sala> buscar(Long id) {
        return repository.findById(id);
    }

    // Cadastrar sala
    public Sala criar(Sala sala) {
        return repository.save(sala);
    }

    // Excluir sala
    public boolean excluir(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}

Entendendo alguns detalhes

    @Service: informa ao Spring que essa classe é um componente da camada de serviço.

    @Autowired: não é necessário nesse caso, pois estamos utilizando injeção de dependência pelo construtor.

    Optional<Sala>: permite representar uma busca que pode ou não encontrar uma sala.

    boolean no método excluir: indica se a exclusão foi realizada ou se o ID não existia.

Perceba que a Service não precisa conhecer os detalhes das requisições HTTP. Ela apenas executa as operações solicitadas.
4. Controller

Agora vem uma das mudanças mais importantes: o Controller não acessará mais diretamente o Repository.

Ele utilizará a Service.
java

package escola.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import escola.model.Sala;
import escola.service.EscolaService;

@RestController
@RequestMapping("/salas")
public class EscolaController {

    private final EscolaService service;

    public EscolaController(EscolaService service) {
        this.service = service;
    }

    // Listar todas as salas
    @GetMapping
    public ResponseEntity<List<Sala>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // Buscar sala pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscar(@PathVariable Long id) {

        return service.buscar(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Cadastrar sala
    @PostMapping
    public ResponseEntity<Sala> criar(@RequestBody Sala sala) {

        Sala novaSala = service.criar(sala);

        return ResponseEntity.status(201).body(novaSala);
    }

    // Excluir sala
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!service.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

5. Como fica o funcionamento?

Veja como uma requisição percorre as camadas:

1. Controller

Recebe, por exemplo, POST /salas

2. Service

Executa a operação de cadastro

3. Repository

Executa repository.save(sala)

4. Banco de dados

Persiste a nova sala

O resultado retorna pelo mesmo caminho até o Controller, que responde ao cliente.
6. Uma melhoria que você pode implementar depois

Como estamos trabalhando com salas de uma escola, futuramente você pode adicionar regras de negócio na Service.

Por exemplo:
java

public Sala criar(Sala sala) {

    if (sala.getCapacidade() <= 0) {
        throw new IllegalArgumentException(
            "A capacidade deve ser maior que zero."
        );
    }

    return repository.save(sala);
}

Assim, você impede o cadastro de salas com capacidade inválida.

Uma observação: para um projeto mais organizado, futuramente vale adicionar DTOs (Data Transfer Objects) e tratamento global de exceções com @RestControllerAdvice. Mas eu não adicionaria tudo agora.

Minha sugestão é implementar primeiro essa versão com as quatro camadas, testar os quatro endpoints e depois avançar para validações e DTOs. Assim você consegue entender bem o papel de cada camada sem complicar desnecessariamente o projeto.
Você disse:

implementei o service e modifiquei o acesso do controller
a API funcionará?
O ChatGPT disse:

Sim! Considerando as correções que fizemos anteriormente, sua API deverá funcionar com a inclusão da Service e a alteração do Controller, desde que as dependências e a configuração do banco de dados estejam corretas.

Mas existem alguns pontos que eu verificaria antes de executar:
Checklist de funcionamento

    Sala.java possui @Entity e @Table, com os imports corretos.
    Sala.java possui construtor vazio.
    Sala.java possui getters e setters.
    EscolaRepository.java é uma interface que estende JpaRepository<Sala, Long>.
    EscolaService.java possui @Service.
    EscolaController.java possui @RestController.
    O Controller injeta EscolaService, e não mais EscolaRepository.
    Os métodos do Controller chamam os métodos correspondentes da Service.
    O application.properties está configurado corretamente.

O que você deverá conseguir fazer?

Operação
	

Resultado esperado

Cadastrar sala
	

201 Created

Listar salas
	

200 OK

Buscar sala existente
	

200 OK

Buscar sala inexistente
	

404 Not Found

Excluir sala existente
	

204 No Content

Excluir sala inexistente
	

404 Not Found
Minha recomendação

Envie aqui as versões atuais das quatro classes:

    Sala.java

    EscolaRepository.java

    EscolaService.java

    EscolaController.java

Quero conferir o código que você realmente implementou, não apenas o modelo que sugeri.

Assim consigo fazer uma revisão e identificar possíveis erros de compilação, injeção de dependência, mapeamento e funcionamento dos endpoints antes de você executar a aplicação.