# PROMPTS: Grupo 3°B TI

**Integrantes:**

1. Heitor Alves
2. Heloisa Oliveira
3. Nicole Zago

**Prova:** API em MVC

**Usamos IA?** (X) Sim  ( ) Não

**Qual ferramenta e modelo (se usaram):**

> Quem não usou IA: troque "prompt" por "passo". Em cada bloco abaixo, descreva o que vocês fizeram naquele passo, na ordem em que aconteceu, e preencha os outros campos do mesmo jeito.

## Registro, na ordem

Copie o bloco abaixo para cada prompt. Escreva o prompt exatamente como foi enviado.

### Prompt 1

**O prompt (cópia exata):**

> Estou fazendo uma API utilizando Spring para uma escola, que permite o cadastro, listagem, busca por id e remoção de salas

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

**O que a IA devolveu (uma ou duas linhas):**
Corrigiu partes do código que estavam faltando

**O que fizemos com a resposta:** ( ) usamos como veio  (X) alteramos  ( ) descartamos

**Por quê (o que conferimos, o que mudamos):**
faltaram imports e os setters no model, não conseguimos fazer o Service

### Prompt 2

**O prompt (cópia exata):**

> não necessariamente precisa de um service, mas nesse caso quero ter um

**O que a IA devolveu (uma ou duas linhas):**
código de services e modificação de controller

**O que fizemos com a resposta:** ( ) usamos como veio  (X) alteramos  ( ) descartamos

**Por quê (o que conferimos, o que mudamos):**
conferimos o controller e modificamos o acesso direto ao repository para o acesso ao service

(continuem com quantos blocos precisarem)

## Como conferimos

O chatbot confirmou caso estava rodando, mas não utilizamos um agente para realizar isso.

## Onde a IA errou, ou onde nós erramos

Não sabíamos fazer o EscolaService, e fizemos trocas de nome da table Sala por Escola

## Respostas finais, sem consultar a IA

Respondam com as suas palavras, nos últimos 5 minutos. Não copiem de lugar nenhum.

## Quando chega um pedido de reservar uma sala, qual é o caminho até a resposta? Passe por cada camada (Controller, Service, Repository e Model).
- A requisição passa pelo view (usuário) até o controller, que manda um pedido ao service, até o repository, que então vai direto ao BD, sem a validação do model. Então volta do BD dreto ao repository, service, controller e msotra a resposta.
## Por que a regra "uma sala reservada não pode ser reservada de novo" fica na Service e não no Controller?
- O controller não é responsável por validações, e sim apenas por disponibilizar as rotas.
## Se a escola trocar o H2 por outro banco de dados, quais classes precisam mudar? Por quê?
- As classes service, repository e Controller precisam mudar, pois precisariam modificar o endereço do banco de dados. No entanto, caso apenas o repository modificar o endereço original do banco de dados, só ele precisará ser modificado.