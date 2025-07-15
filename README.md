# Sistema de Gerenciamento de Pizzaria

## Descrição

O **Sistema de Gerenciamento de Pizzaria** é uma aplicação em Java desenvolvida para automatizar e gerenciar as operações diárias de uma pizzaria. O sistema permite o gerenciamento completo de clientes, cardápio (incluindo personalização de pizzas), criação e acompanhamento de pedidos, e controle de estoque de ingredientes.

Este projeto demonstra a aplicação prática de conceitos fundamentais de **Programação Orientada a Objetos (POO)**, incluindo herança, polimorfismo, encapsulamento, abstração, e implementa um sistema CRUD completo para múltiplas entidades com relacionamentos complexos.

## Características Principais

- **Gerenciamento de Clientes**: CRUD completo com busca por telefone e nome
- **Controle de Estoque**: Gerenciamento de ingredientes com controle de quantidade em estoque
- **Cardápio Flexível**: Pizzas pré-definidas em três tamanhos (P, M, G) e bebidas variadas
- **Sistema de Pedidos**: Criação manual e automática, acompanhamento por status com transições flexíveis
- **Cálculo Dinâmico de Preços**: Preços calculados automaticamente baseados no tamanho das pizzas
- **Persistência de Dados**: Armazenamento completo em arquivos JSON com tratamento de referências circulares
- **Tratamento de Exceções**: Exceções customizadas para todas as regras de negócio
- **Arquitetura Orientada a Objetos**: Implementação completa dos pilares da POO

## Estrutura do Projeto

```
src/main/java/com/pizzaria/
├── model/              # Entidades do domínio (5 entidades principais)
│   ├── Cliente.java         # Cliente com endereço (relacionamento 1:1)
│   ├── Endereco.java        # Endereço do cliente
│   ├── Ingrediente.java     # Ingrediente com controle de estoque
│   ├── ItemCardapio.java    # Classe abstrata (herança)
│   ├── Pizza.java           # Herda de ItemCardapio (polimorfismo)
│   ├── Bebida.java          # Herda de ItemCardapio (polimorfismo)
│   ├── Pedido.java          # Pedido com itens (relacionamento 1:n)
│   ├── ItemPedido.java      # Item do pedido
│   ├── StatusPedido.java    # Enum para status do pedido
│   └── Tamanho.java         # Enum para tamanhos das pizzas
├── service/            # Lógica de negócio (CRUD para cada entidade)
│   ├── ClienteService.java      # CRUD de clientes
│   ├── IngredienteService.java  # CRUD de ingredientes
│   ├── CardapioService.java     # CRUD de pizzas e bebidas
│   └── PedidoService.java       # CRUD de pedidos com regras de negócio
├── exception/          # Exceções customizadas
│   ├── ClienteNaoEncontradoException.java
│   ├── EstoqueInsuficienteException.java
│   └── PedidoInvalidoException.java
├── util/              # Utilitários para persistência
│   ├── JsonPersistence.java         # Gerenciamento de arquivos JSON
│   ├── LocalDateTimeAdapter.java    # Adaptador para LocalDateTime
│   └── ItemCardapioAdapter.java     # Adaptador para herança em JSON
└── SistemaPizzaria.java  # Classe principal com interface de usuário
```

## Requisitos Atendidos do Projeto

### ✅ Programação Orientada a Objetos (POO)
- **Encapsulamento**: Todos os atributos são privados com métodos de acesso controlado
- **Herança**: `Pizza` e `Bebida` herdam de `ItemCardapio` (classe abstrata)
- **Polimorfismo**: Método `calcularPreco()` implementado de forma específica em cada subclasse
- **Abstração**: Classe abstrata `ItemCardapio` define interface comum para itens do cardápio

### ✅ CRUD de 5 Entidades
1. **Cliente**: Create, Read, Update, Delete completo
2. **Ingrediente**: Gestão completa com controle de estoque
3. **Pizza**: CRUD com personalização e cálculo de preços
4. **Bebida**: Gestão completa de bebidas do cardápio
5. **Pedido**: CRUD com controle de status e validações de negócio

### ✅ Relacionamentos Implementados
- **1:1**: Cliente ↔ Endereço (composição)
- **1:n**: Cliente → Pedidos, Pedido → ItemPedido
- **n:m**: Pizza ↔ Ingredientes (uma pizza pode ter vários ingredientes, um ingrediente pode estar em várias pizzas)

### ✅ Tratamento de Exceções
- `ClienteNaoEncontradoException`: Cliente não existe no sistema
- `EstoqueInsuficienteException`: Estoque insuficiente para atender pedido
- `PedidoInvalidoException`: Operações inválidas em pedidos

### ✅ Interface/Abstração
- Classe abstrata `ItemCardapio` com método abstrato `calcularPreco()`
- Interfaces implícitas através de polimorfismo
- Enums `StatusPedido` e `Tamanho` para controle de estados

### ✅ Apresentação
- Interface de console interativa com menus organizados
- Demonstração de caso de uso completo
- Navegação intuitiva entre funcionalidades

## Requisitos Técnicos

- Java 17 ou superior
- Maven 3.6+ (para gerenciamento de dependências)

## Dependências

- **Gson 2.10.1**: Para serialização/deserialização JSON com suporte a herança

## Como Executar

### Usando Maven (Recomendado)

1. **Compilar o projeto:**
   ```bash
   mvn clean compile
   ```

2. **Executar a aplicação:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.pizzaria.SistemaPizzaria"
   ```

3. **Criar JAR executável:**
   ```bash
   mvn clean package
   java -jar target/sistema-pizzaria-1.0.0.jar
   ```

### Usando Java diretamente

1. **Compilar:**
   ```bash
   javac -cp "lib/*" -d target/classes src/main/java/com/pizzaria/*.java src/main/java/com/pizzaria/*/*.java
   ```

2. **Executar:**
   ```bash
   java -cp "target/classes:lib/*" com.pizzaria.SistemaPizzaria
   ```

## Funcionalidades Detalhadas

### 1. Gerenciamento de Clientes (CRUD Completo)
- **Criar**: Cadastro de novos clientes com endereço completo
- **Ler**: Busca por ID, telefone ou nome (busca parcial)
- **Atualizar**: Modificação de dados pessoais e endereço
- **Deletar**: Remoção de clientes (com validação de pedidos)
- **Validações**: Telefone único, dados obrigatórios

### 2. Controle de Ingredientes (CRUD com Estoque)
- **Cadastro**: Novos ingredientes com controle de estoque
- **Gestão de Estoque**: Adição e redução de quantidades
- **Relatórios**: Lista de ingredientes em falta (estoque < 10)
- **Validação**: Verificação de estoque antes de criar pedidos
- **Busca**: Por nome e listagem completa

### 3. Gerenciamento do Cardápio (CRUD Hierárquico)
#### Pizzas
- **Tamanhos**: Pequena (P), Média (M), Grande (G) com multiplicadores de preço
- **Preços Dinâmicos**: Cálculo automático baseado no tamanho (1.0x, 1.5x, 2.0x)
- **Ingredientes**: Relação n:m com controle de disponibilidade
- **Personalização**: Criação de pizzas personalizadas
- **Exibição**: Preços para todos os tamanhos simultaneamente

#### Bebidas
- **Preço Fixo**: Valor único independente de tamanho
- **Volume**: Controle em ml
- **Variedade**: Múltiplos tipos e sabores

### 4. Sistema de Pedidos (CRUD com Regras de Negócio)
- **Tipos**: Pedidos para balcão e delivery
- **Status Flexível**: Transições permitidas entre todos os status
  - PENDENTE → EM_PREPARO → SAIU_PARA_ENTREGA → CONCLUIDO
  - Cancelamento direto de PENDENTE para CANCELADO
- **Cálculo Automático**: Valor total baseado em itens e quantidades
- **Validações**: Verificação de estoque antes da confirmação
- **Histórico**: Manutenção completa de pedidos realizados

### 5. Relatórios e Estatísticas
- **Pedidos do Dia**: Filtro por data atual
- **Por Status**: Listagem de pedidos por situação
- **Faturamento**: Cálculo por dia e total acumulado
- **Estatísticas**: Resumo geral do sistema
- **Por Cliente**: Histórico de pedidos por cliente

## Exemplo de Uso - Caso de Uso Completo

O sistema inclui uma demonstração automática completa que mostra:

### Fluxo: "Realizar um Pedido para Entrega"

1. **Busca de Cliente**: 
   - Busca por telefone existente ou cadastro rápido
   - Validação de dados únicos (telefone)

2. **Montagem do Pedido**:
   - Seleção de pizzas com escolha de tamanho
   - Adição de bebidas variadas
   - Controle de quantidades

3. **Cálculos Automáticos**:
   - Preço das pizzas baseado no tamanho selecionado
   - Valor total do pedido
   - Exibição detalhada de todos os itens

4. **Validações de Estoque**:
   - Verificação de ingredientes disponíveis
   - Tratamento de exceções de estoque insuficiente

5. **Confirmação e Persistência**:
   - Criação do pedido no sistema
   - Salvamento em arquivo JSON
   - Exibição do resumo final

### Menu Interativo

```
=== SISTEMA DE GERENCIAMENTO DE PIZZARIA ===
1. Clientes
2. Ingredientes
3. Cardápio
4. Pedidos
5. Demonstrar Caso de Uso
0. Sair
```

Cada seção oferece operações CRUD completas com validações apropriadas.

## Conceitos de Orientação a Objetos Implementados

### 1. Herança (Inheritance)
```java
// Classe abstrata base
public abstract class ItemCardapio {
    protected int id;
    protected String nome;
    protected String descricao;
    
    public abstract double calcularPreco();
}

// Classes filhas especializadas
public class Pizza extends ItemCardapio { ... }
public class Bebida extends ItemCardapio { ... }
```

### 2. Polimorfismo (Polymorphism)
- **Método `calcularPreco()`**: Implementado de forma específica em cada subclasse
  - `Pizza`: `precoBase * tamanho.getMultiplicador()`
  - `Bebida`: `precoFixo` (valor constante)
- **Tratamento uniforme**: Lista de `ItemCardapio` pode conter pizzas e bebidas

### 3. Encapsulamento (Encapsulation)
- **Atributos privados**: Todos os campos são privados
- **Métodos de acesso**: Getters e setters com validações
- **Controle de estado**: Validações internas para manter consistência
- **Exemplo**: Status de pedido só pode ser alterado através de métodos controlados

### 4. Abstração (Abstraction)
- **Classe abstrata `ItemCardapio`**: Define interface comum sem implementação concreta
- **Serviços especializados**: Cada service encapsula lógica específica de negócio
- **Ocultação de complexidade**: Cliente não precisa conhecer detalhes internos

### 5. Composição e Agregação
```java
// Composição (1:1) - Cliente "possui" Endereco
public class Cliente {
    private Endereco endereco; // Parte integral do cliente
}

// Agregação (1:n) - Pedido "contém" ItemPedido
public class Pedido {
    private List<ItemPedido> itensDoPedido; // Itens existem independentemente
}

// Associação (n:m) - Pizza "usa" Ingredientes
public class Pizza {
    private List<Ingrediente> ingredientes; // Relacionamento flexível
}
```

## Tratamento de Exceções

O sistema implementa três exceções customizadas para tratar regras específicas de negócio:

### 1. EstoqueInsuficienteException
```java
// Lançada quando não há ingredientes suficientes para atender um pedido
throw new EstoqueInsuficienteException("Queijo", 5, 3); // Precisa de 5, tem apenas 3
```

### 2. ClienteNaoEncontradoException  
```java
// Lançada quando um cliente não é encontrado no sistema
throw new ClienteNaoEncontradoException(123); // Cliente ID 123 não existe
throw new ClienteNaoEncontradoException("(11) 99999-9999"); // Telefone não existe
```

### 3. PedidoInvalidoException
```java
// Lançada para operações inválidas em pedidos
throw new PedidoInvalidoException(456, StatusPedido.CONCLUIDO, "Não é possível cancelar");
```

### Tratamento Centralizado
- **Try-catch**: Captura e tratamento em pontos apropriados
- **Mensagens informativas**: Feedback claro para o usuário
- **Recuperação graceful**: Sistema continua funcionando após erros

## Persistência de Dados e Arquitetura JSON

### Arquivos de Dados
Os dados são armazenados em arquivos JSON estruturados no diretório `data/`:

```
data/
├── clientes.json      # Dados dos clientes com endereços
├── ingredientes.json  # Estoque de ingredientes (sem preços)
├── pizzas.json        # Pizzas do cardápio com ingredientes
├── bebidas.json       # Bebidas com preços fixos
└── pedidos.json       # Histórico completo de pedidos
```

### Tratamento de Herança em JSON
```java
// Adaptador customizado para serializar herança Pizza/Bebida
public class ItemCardapioAdapter implements JsonSerializer<ItemCardapio>, JsonDeserializer<ItemCardapio> {
    // Adiciona campo "type" para identificar a classe concreta
    // Permite deserialização correta dos objetos polimórficos
}
```

### Solução de Referências Circulares
- **Cliente ↔ Pedido**: Uso de `@JsonIgnore` em `Cliente.pedidos`
- **Reconstrução**: Relacionamentos reconstruídos durante o carregamento
- **Integridade**: Manutenção automática das referências bidirecionais

### Adaptadores Especializados
- **LocalDateTimeAdapter**: Serialização de datas no formato ISO
- **ItemCardapioAdapter**: Polimorfismo para Pizza/Bebida
- **Gson customizado**: Configuração específica para o domínio

## Arquitetura e Padrões de Design

### Padrão de Camadas (Layered Architecture)
```
┌─────────────────────────────────────┐
│     Apresentação (UI)               │ ← SistemaPizzaria.java
├─────────────────────────────────────┤
│     Camada de Serviço (Business)    │ ← *Service.java
├─────────────────────────────────────┤
│     Camada de Modelo (Domain)       │ ← model/*.java
├─────────────────────────────────────┤
│     Camada de Persistência (Data)   │ ← JsonPersistence.java
└─────────────────────────────────────┘
```

### Padrões Implementados
- **Service Layer**: Lógica de negócio encapsulada em services
- **Data Transfer**: Objetos de domínio como DTOs
- **Exception Handling**: Exceções específicas do domínio
- **Adapter Pattern**: Adaptadores para serialização JSON

### Princípios SOLID Aplicados
- **SRP**: Cada classe tem uma responsabilidade única
- **OCP**: Extensível através de herança (ItemCardapio)
- **LSP**: Subclasses são substituíveis (Pizza/Bebida)
- **ISP**: Interfaces específicas (métodos abstratos)
- **DIP**: Dependência de abstrações (ItemCardapio)

## Melhorias e Recursos Avançados

### Funcionalidades Implementadas
- ✅ **Preços dinâmicos**: Cálculo automático baseado em tamanhos
- ✅ **Estoque inteligente**: Verificação antes de confirmar pedidos
- ✅ **Status flexível**: Transições permitidas entre todos os status
- ✅ **Interface amigável**: Menus organizados e feedback claro
- ✅ **Persistência robusta**: Tratamento de herança e referências circulares
- ✅ **Validações completas**: Dados únicos e regras de negócio

### Possíveis Extensões Futuras
- 🔄 Interface gráfica (JavaFX/Swing)
- 🔄 Banco de dados relacional (JPA/Hibernate)
- 🔄 API REST com Spring Boot
- 🔄 Sistema de desconto e promoções
- 🔄 Relatórios em PDF
- 🔄 Notificações por email/SMS

## Documentação Adicional

- **Diagrama UML**: Consulte `diagrama.md` para visualização completa das classes
- **Código fonte**: Comentários inline explicando lógica complexa
- **Arquivo de configuração**: `pom.xml` com dependências e plugins Maven

## Contribuição e Desenvolvimento

Este projeto foi desenvolvido como demonstração educacional completa dos conceitos de **Programação Orientada a Objetos** em Java, incluindo:

- ✅ **5 entidades CRUD**: Cliente, Ingrediente, Pizza, Bebida, Pedido
- ✅ **Herança e Polimorfismo**: ItemCardapio → Pizza/Bebida
- ✅ **Relacionamentos**: 1:1, 1:n, n:m implementados
- ✅ **Exceções customizadas**: 3 tipos específicos do domínio
- ✅ **Interface/Abstração**: Classe abstrata com métodos abstratos
- ✅ **Apresentação**: Menu interativo completo

## Autor

Desenvolvido para o projeto de **Linguagem de Programação 2**, demonstrando aplicação prática e completa dos conceitos fundamentais de POO em Java.