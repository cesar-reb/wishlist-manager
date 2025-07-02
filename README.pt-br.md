# wishlist-manager

[en-US](README.md) | [pt-BR](README.pt-br.md)

Uma aplicação Spring Boot para gerenciamento de listas de desejos de clientes.
Construída com base nos princípios da Clean Architecture, oferece uma API RESTful e é testada com MongoDB embarcado.

---

## Funcionalidades

- Adicionar produtos à lista de desejos de um cliente
- Remover produtos da lista de desejos
- Verificar se um produto existe na lista de desejos
- Recuperar todos os produtos da lista de desejos
- Validação de limite máximo de produtos

---

## Stack utilizada

- Java 21
- Spring Boot 3.x
- MongoDB Embarcado (`flapdoodle.embed.mongo`)
- JUnit 5 + MockMvc (Testes de Integração)
- Maven

---

## Executando a aplicação

### Requisitos:
- Java 21+
- Maven 3.8+

### Configuração

Clone o repositório:
```bash
git clone git@github.com:cesar-reb/wishlist-manager.git 
cd wishlist-manager
```

Build do projeto:
```mvn clean install```

Antes de executar a aplicação, certifique-se de configurar a URI de conexão com o MongoDB.

Você pode defini-la no arquivo application.yml:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/wishlist
```
Ajuste a URI conforme necessário para o seu ambiente local ou de produção.

Execute a aplicação:
```mvn spring-boot:run```

A API estará disponível em `http://localhost:8080`.

## Executando os Testes
```mvn test```

## Documentação da API (Swagger)

A documentação interativa da API está disponível via Swagger UI.

### Acesso

Após iniciar a aplicação, acesse:
```http://localhost:8080/swagger-ui.html```
ou
```http://localhost:8080/swagger-ui/index.html```
## Contribuindo

1. Faça um fork do repositório
2. Crie uma nova branch (`git checkout -b feature/sua-funcionalidade`)
3. Faça commit das suas alterações
4. Faça push para a branch (`git push origin feature/sua-funcionalidade`)
5. Abra um pull request