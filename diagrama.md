```mermaid
classDiagram
    %% Classes Abstratas e Interfaces
    class ItemCardapio {
        <<abstract>>
        #int id
        #String nome
        #String descricao
        +getId() int
        +setId(int) void
        +getNome() String
        +setNome(String) void
        +getDescricao() String
        +setDescricao(String) void
        +calcularPreco()* double
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }

    %% Enums
    class StatusPedido {
        <<enumeration>>
        PENDENTE
        EM_PREPARO
        SAIU_PARA_ENTREGA
        CONCLUIDO
        CANCELADO
    }

    class Tamanho {
        <<enumeration>>
        PEQUENA("Pequena", "P", 1.0)
        MEDIA("Média", "M", 1.5)
        GRANDE("Grande", "G", 2.0)
        -String descricao
        -String sigla
        -double multiplicador
        +getDescricao() String
        +getSigla() String
        +getMultiplicador() double
        +porSigla(String) Tamanho
        +toString() String
    }

    %% Classes de Modelo
    class Cliente {
        -int id
        -String nome
        -String telefone
        -Endereco endereco
        -List~Pedido~ pedidos
        +Cliente()
        +Cliente(int, String, String, Endereco)
        +getId() int
        +setId(int) void
        +getNome() String
        +setNome(String) void
        +getTelefone() String
        +setTelefone(String) void
        +getEndereco() Endereco
        +setEndereco(Endereco) void
        +getPedidos() List~Pedido~
        +setPedidos(List~Pedido~) void
        +adicionarPedido(Pedido) void
        +removerPedido(Pedido) void
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }

    class Endereco {
        -String logradouro
        -String numero
        -String bairro
        -String cidade
        -String cep
        +Endereco()
        +Endereco(String, String, String, String, String)
        +getLogradouro() String
        +setLogradouro(String) void
        +getNumero() String
        +setNumero(String) void
        +getBairro() String
        +setBairro(String) void
        +getCidade() String
        +setCidade(String) void
        +getCep() String
        +setCep(String) void
        +toString() String
    }

    class Pizza {
        -double precoBase
        -Tamanho tamanho
        -List~Ingrediente~ ingredientes
        +Pizza()
        +Pizza(int, String, String, double, Tamanho)
        +Pizza(int, String, String, double, Tamanho, List~Ingrediente~)
        +getPrecoBase() double
        +setPrecoBase(double) void
        +getTamanho() Tamanho
        +setTamanho(Tamanho) void
        +getIngredientes() List~Ingrediente~
        +setIngredientes(List~Ingrediente~) void
        +adicionarIngrediente(Ingrediente) void
        +removerIngrediente(Ingrediente) void
        +calcularPreco() double
        +contemIngrediente(Ingrediente) boolean
        +toString() String
    }

    class Bebida {
        -double precoFixo
        -int volumeEmML
        +Bebida()
        +Bebida(int, String, String, double, int)
        +getPrecoFixo() double
        +setPrecoFixo(double) void
        +getVolumeEmML() int
        +setVolumeEmML(int) void
        +calcularPreco() double
        +toString() String
    }

    class Ingrediente {
        -int id
        -String nome
        -int quantidadeEmEstoque
        +Ingrediente()
        +Ingrediente(int, String, int)
        +getId() int
        +setId(int) void
        +getNome() String
        +setNome(String) void
        +getQuantidadeEmEstoque() int
        +setQuantidadeEmEstoque(int) void
        +temEstoque() boolean
        +reduzirEstoque(int) void
        +aumentarEstoque(int) void
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }

    class Pedido {
        -int id
        -int clienteId
        -Cliente cliente
        -List~ItemPedido~ itensDoPedido
        -double valorTotal
        -StatusPedido status
        -LocalDateTime dataHora
        -Endereco enderecoEntrega
        +Pedido()
        +Pedido(int, Cliente)
        +Pedido(int, Cliente, Endereco)
        +getId() int
        +setId(int) void
        +getCliente() Cliente
        +setCliente(Cliente) void
        +getClienteId() int
        +setClienteId(int) void
        +getItensDoPedido() List~ItemPedido~
        +setItensDoPedido(List~ItemPedido~) void
        +getValorTotal() double
        +setValorTotal(double) void
        +getStatus() StatusPedido
        +setStatus(StatusPedido) void
        +getDataHora() LocalDateTime
        +setDataHora(LocalDateTime) void
        +getEnderecoEntrega() Endereco
        +setEnderecoEntrega(Endereco) void
        +adicionarItem(ItemPedido) void
        +removerItem(ItemPedido) void
        +removerItem(ItemCardapio) void
        +calcularValorTotal() void
        +isDelivery() boolean
        +podeSerCancelado() boolean
        +podeSerAlterado() boolean
        +getQuantidadeTotalItens() int
        +toString() String
    }

    class ItemPedido {
        -ItemCardapio item
        -int quantidade
        -double precoUnitario
        +ItemPedido()
        +ItemPedido(ItemCardapio, int)
        +getItem() ItemCardapio
        +setItem(ItemCardapio) void
        +getQuantidade() int
        +setQuantidade(int) void
        +getPrecoUnitario() double
        +setPrecoUnitario(double) void
        +calcularSubtotal() double
        +toString() String
    }

    %% Classes de Serviço
    class ClienteService {
        -String CLIENTES_FILE
        -List~Cliente~ clientes
        -int proximoId
        +ClienteService()
        +criarCliente(Cliente) Cliente
        +buscarPorId(int) Cliente
        +buscarPorTelefone(String) Cliente
        +buscarPorNome(String) List~Cliente~
        +listarTodos() List~Cliente~
        +atualizarCliente(Cliente) Cliente
        +removerCliente(int) void
        +telefoneJaCadastrado(String) boolean
        +telefoneJaCadastradoPorOutro(String, int) boolean
        +getTotalClientes() int
        -carregarDados() void
        -salvarDados() void
    }

    class IngredienteService {
        -String INGREDIENTES_FILE
        -List~Ingrediente~ ingredientes
        -int proximoId
        +IngredienteService()
        +criarIngrediente(Ingrediente) Ingrediente
        +buscarPorId(int) Ingrediente
        +buscarPorNome(String) List~Ingrediente~
        +listarTodos() List~Ingrediente~
        +listarEmFalta() List~Ingrediente~
        +atualizarIngrediente(Ingrediente) Ingrediente
        +removerIngrediente(int) void
        +adicionarEstoque(int, int) void
        +reduzirEstoque(int, int) void
        +verificarEstoqueDisponivel(List~Ingrediente~) void
        +getTotalIngredientes() int
        -carregarDados() void
        -salvarDados() void
    }

    class CardapioService {
        -String PIZZAS_FILE
        -String BEBIDAS_FILE
        -List~Pizza~ pizzas
        -List~Bebida~ bebidas
        -int proximoIdPizza
        -int proximoIdBebida
        +CardapioService()
        +criarPizza(Pizza) Pizza
        +criarPizzaPersonalizada(String, Tamanho, double, List~Ingrediente~) Pizza
        +criarBebida(Bebida) Bebida
        +buscarPizzaPorId(int) Pizza
        +buscarBebidaPorId(int) Bebida
        +listarTodasPizzas() List~Pizza~
        +listarTodasBebidas() List~Bebida~
        +buscarPizzasPorNome(String) List~Pizza~
        +buscarBebidasPorNome(String) List~Bebida~
        +buscarItemPorId(int) ItemCardapio
        +listarTodosItens() List~ItemCardapio~
        +atualizarPizza(Pizza) Pizza
        +atualizarBebida(Bebida) Bebida
        +removerPizza(int) void
        +removerBebida(int) void
        +getTotalPizzas() int
        +getTotalBebidas() int
        -carregarDados() void
        -salvarPizzas() void
        -salvarBebidas() void
    }

    class PedidoService {
        -String PEDIDOS_FILE
        -List~Pedido~ pedidos
        -int proximoId
        -ClienteService clienteService
        -IngredienteService ingredienteService
        -CardapioService cardapioService
        +PedidoService(ClienteService, IngredienteService, CardapioService)
        +criarPedido(Cliente, List~ItemPedido~) Pedido
        +criarPedidoDelivery(Cliente, List~ItemPedido~, Endereco) Pedido
        +buscarPorId(int) Pedido
        +listarTodos() List~Pedido~
        +listarPorCliente(Cliente) List~Pedido~
        +listarPedidosDoDia() List~Pedido~
        +listarPorStatus(StatusPedido) List~Pedido~
        +listarPendentes() List~Pedido~
        +atualizarStatusPedido(int, StatusPedido) void
        +cancelarPedido(int) void
        +adicionarItemAoPedido(int, ItemPedido) void
        +removerItemDoPedido(int, ItemCardapio) void
        +calcularFaturamentoDoDia() double
        +calcularFaturamentoTotal() double
        +getEstatisticas() String
        -verificarEstoqueParaPedido(List~ItemPedido~) void
        -validarTransicaoStatus(StatusPedido, StatusPedido) void
        -carregarDados() void
        -salvarDados() void
    }

    %% Classes de Exceção
    class ClienteNaoEncontradoException {
        +ClienteNaoEncontradoException(int)
        +ClienteNaoEncontradoException(String)
    }

    class EstoqueInsuficienteException {
        +EstoqueInsuficienteException(String, int, int)
    }

    class PedidoInvalidoException {
        +PedidoInvalidoException(int)
        +PedidoInvalidoException(int, StatusPedido, String)
        +PedidoInvalidoException(String)
    }

    %% Classes Utilitárias
    class JsonPersistence {
        <<utility>>
        +loadFromFile(String, Type) T
        +saveToFile(T, String) void
        +saveObjectToFile(T, String) void
        +loadObjectFromFile(String, Class) T
        +fileExists(String) boolean
        +deleteFile(String) boolean
    }

    class LocalDateTimeAdapter {
        +serialize(LocalDateTime, Type, JsonSerializationContext) JsonElement
        +deserialize(JsonElement, Type, JsonDeserializationContext) LocalDateTime
    }

    class ItemCardapioAdapter {
        +serialize(ItemCardapio, Type, JsonSerializationContext) JsonElement
        +deserialize(JsonElement, Type, JsonDeserializationContext) ItemCardapio
    }

    class SistemaPizzaria {
        -ClienteService clienteService
        -IngredienteService ingredienteService
        -CardapioService cardapioService
        -PedidoService pedidoService
        -Scanner scanner
        +SistemaPizzaria()
        +main(String[]) void
        -inicializar() void
        -criarDadosExemplo() void
        -executarMenuPrincipal() void
        -menuClientes() void
        -menuIngredientes() void
        -menuCardapio() void
        -menuPedidos() void
        -criarNovoPedido() void
        -buscarOuCadastrarCliente() Cliente
        -cadastrarNovoClienteRapido(String) Cliente
        -obterEnderecoEntrega(Cliente) Endereco
        -montarItensPedido() List~ItemPedido~
        -adicionarPizza(List~ItemPedido~) void
        -adicionarBebida(List~ItemPedido~) void
        -mostrarItensPedido(List~ItemPedido~) void
        -mostrarResumoPedido(Pedido, Cliente, int, Endereco) void
        -demonstrarCasoDeUso() void
        -lerOpcao() int
    }

    %% --- RELACIONAMENTOS CORRIGIDOS ---

    %% Relacionamentos de Herança
    Pizza --|> ItemCardapio
    Bebida --|> ItemCardapio

    %% Relacionamentos de Composição e Agregação
    Cliente "1" *-- "1" Endereco : contains
    Pedido "1" o-- "0..1" Endereco : delivery address
    Cliente "1" o-- "*" Pedido : places
    Pedido "1" o-- "*" ItemPedido : contains
    Pizza "*" o-- "*" Ingrediente : uses

    %% Relacionamentos de Associação
    ItemPedido "*" -- "1" ItemCardapio : references
    Pedido "*" -- "1" StatusPedido : has
    Pizza "*" -- "1" Tamanho : has

    %% Relacionamentos de Dependência (Services)
    ClienteService ..> Cliente : manages
    ClienteService ..> ClienteNaoEncontradoException : throws
    IngredienteService ..> Ingrediente : manages
    IngredienteService ..> EstoqueInsuficienteException : throws
    CardapioService ..> Pizza : manages
    CardapioService ..> Bebida : manages
    PedidoService ..> Pedido : manages
    PedidoService ..> PedidoInvalidoException : throws
    PedidoService ..> EstoqueInsuficienteException : throws
    PedidoService ..> ClienteService : uses
    PedidoService ..> IngredienteService : uses
    PedidoService ..> CardapioService : uses

    %% Sistema Principal
    SistemaPizzaria -- ClienteService : uses
    SistemaPizzaria -- IngredienteService : uses
    SistemaPizzaria -- CardapioService : uses
    SistemaPizzaria -- PedidoService : uses

    %% Utilitários
    ClienteService ..> JsonPersistence : uses
    IngredienteService ..> JsonPersistence : uses
    CardapioService ..> JsonPersistence : uses
    PedidoService ..> JsonPersistence : uses
    JsonPersistence ..> LocalDateTimeAdapter : uses
    JsonPersistence ..> ItemCardapioAdapter : uses
