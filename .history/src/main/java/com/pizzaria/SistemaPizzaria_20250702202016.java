package com.pizzaria;

import com.pizzaria.exception.*;
import com.pizzaria.model.*;
import com.pizzaria.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal do Sistema de Gerenciamento de Pizzaria
 * Demonstra o uso das funcionalidades do sistema
 */
public class SistemaPizzaria {
    private ClienteService clienteService;
    private IngredienteService ingredienteService;
    private CardapioService cardapioService;
    private PedidoService pedidoService;
    private Scanner scanner;

    public SistemaPizzaria() {
        this.clienteService = new ClienteService();
        this.ingredienteService = new IngredienteService();
        this.cardapioService = new CardapioService();
        this.pedidoService = new PedidoService(clienteService, ingredienteService, cardapioService);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        SistemaPizzaria sistema = new SistemaPizzaria();
        sistema.inicializar();
        sistema.executarMenuPrincipal();
    }

    /**
     * Inicializa o sistema com dados de exemplo
     */
    private void inicializar() {
        System.out.println("=== Sistema de Gerenciamento de Pizzaria ===");
        System.out.println("Inicializando sistema...");
        
        try {
            criarDadosExemplo();
            System.out.println("Sistema inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar sistema: " + e.getMessage());
        }
    }

    /**
     * Cria dados de exemplo para demonstração
     */
    private void criarDadosExemplo() throws IOException {
        // Verifica se já existem dados
        if (ingredienteService.getTotalIngredientes() > 0) {
            return; // Dados já existem
        }

        // Criar ingredientes
        ingredienteService.criarIngrediente(new Ingrediente(0, "Mussarela", 2.00, 50));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Presunto", 3.00, 30));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Bacon", 4.00, 20));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Catupiry", 3.50, 25));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Calabresa", 3.00, 40));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Cebola", 1.00, 60));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Tomate", 1.50, 45));
        ingredienteService.criarIngrediente(new Ingrediente(0, "Azeitona", 2.50, 35));

        // Criar bebidas
        cardapioService.criarBebida(new Bebida(0, "Coca-Cola 350ml", "Refrigerante de cola", 4.50, 350));
        cardapioService.criarBebida(new Bebida(0, "Guaraná 350ml", "Refrigerante de guaraná", 4.00, 350));
        cardapioService.criarBebida(new Bebida(0, "Suco de Laranja 300ml", "Suco natural de laranja", 5.00, 300));
        cardapioService.criarBebida(new Bebida(0, "Água 500ml", "Água mineral", 2.50, 500));

        // Criar pizzas padrão
        List<Ingrediente> ingredientesMargherita = new ArrayList<>();
        ingredientesMargherita.add(ingredienteService.buscarPorId(1)); // Mussarela
        ingredientesMargherita.add(ingredienteService.buscarPorId(7)); // Tomate
        
        Pizza margherita = new Pizza(0, "Margherita", "Pizza clássica com mussarela e tomate", 
                                   15.00, Tamanho.MEDIA, ingredientesMargherita);
        cardapioService.criarPizza(margherita);

        List<Ingrediente> ingredientesPortuguesa = new ArrayList<>();
        ingredientesPortuguesa.add(ingredienteService.buscarPorId(1)); // Mussarela
        ingredientesPortuguesa.add(ingredienteService.buscarPorId(2)); // Presunto
        ingredientesPortuguesa.add(ingredienteService.buscarPorId(6)); // Cebola
        ingredientesPortuguesa.add(ingredienteService.buscarPorId(8)); // Azeitona
        
        Pizza portuguesa = new Pizza(0, "Portuguesa", "Pizza com presunto, mussarela, cebola e azeitona", 
                                    18.00, Tamanho.GRANDE, ingredientesPortuguesa);
        cardapioService.criarPizza(portuguesa);

        // Criar um cliente exemplo
        Endereco endereco = new Endereco("Rua das Flores", "123", "Centro", "São Paulo", "01234-567");
        Cliente cliente = new Cliente(0, "João Silva", "(11) 99999-9999", endereco);
        clienteService.criarCliente(cliente);
    }

    /**
     * Executa o menu principal do sistema
     */
    private void executarMenuPrincipal() {
        while (true) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();
            
            try {
                switch (opcao) {
                    case 1 -> menuClientes();
                    case 2 -> menuIngredientes();
                    case 3 -> menuCardapio();
                    case 4 -> menuPedidos();
                    case 5 -> exibirEstatisticas();
                    case 6 -> demonstrarCasoDeUso();
                    case 0 -> {
                        System.out.println("Encerrando sistema...");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Ingredientes");
        System.out.println("3. Gerenciar Cardápio");
        System.out.println("4. Gerenciar Pedidos");
        System.out.println("5. Exibir Estatísticas");
        System.out.println("6. Demonstrar Caso de Uso");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void menuClientes() {
        System.out.println("\n=== GERENCIAR CLIENTES ===");
        System.out.println("1. Listar todos os clientes");
        System.out.println("2. Buscar cliente por telefone");
        System.out.println("3. Cadastrar novo cliente");
        System.out.print("Escolha uma opção: ");
        
        int opcao = lerOpcao();
        
        try {
            switch (opcao) {
                case 1 -> listarClientes();
                case 2 -> buscarClientePorTelefone();
                case 3 -> cadastrarCliente();
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void menuIngredientes() {
        System.out.println("\n=== GERENCIAR INGREDIENTES ===");
        System.out.println("1. Listar todos os ingredientes");
        System.out.println("2. Listar ingredientes em falta");
        System.out.println("3. Adicionar estoque");
        System.out.print("Escolha uma opção: ");
        
        int opcao = lerOpcao();
        
        try {
            switch (opcao) {
                case 1 -> listarIngredientes();
                case 2 -> listarIngredientesEmFalta();
                case 3 -> adicionarEstoque();
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void menuCardapio() {
        System.out.println("\n=== GERENCIAR CARDÁPIO ===");
        System.out.println("1. Listar pizzas");
        System.out.println("2. Listar bebidas");
        System.out.println("3. Criar pizza personalizada");
        System.out.print("Escolha uma opção: ");
        
        int opcao = lerOpcao();
        
        try {
            switch (opcao) {
                case 1 -> listarPizzas();
                case 2 -> listarBebidas();
                case 3 -> criarPizzaPersonalizada();
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void menuPedidos() {
        System.out.println("\n=== GERENCIAR PEDIDOS ===");
        System.out.println("1. Listar pedidos pendentes");
        System.out.println("2. Listar pedidos em preparo");
        System.out.println("3. Atualizar status do pedido");
        System.out.println("4. Listar pedidos do dia");
        System.out.print("Escolha uma opção: ");
        
        int opcao = lerOpcao();
        
        try {
            switch (opcao) {
                case 1 -> listarPedidosPendentes();
                case 2 -> listarPedidosEmPreparo();
                case 3 -> atualizarStatusPedido();
                case 4 -> listarPedidosDoDia();
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // ========== IMPLEMENTAÇÕES DOS MÉTODOS ==========

    private void listarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        
        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    private void buscarClientePorTelefone() {
        System.out.print("Digite o telefone: ");
        String telefone = scanner.nextLine();
        
        try {
            Cliente cliente = clienteService.buscarPorTelefone(telefone);
            System.out.println("Cliente encontrado: " + cliente);
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarCliente() throws IOException {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Logradouro: ");
        String logradouro = scanner.nextLine();
        System.out.print("Número: ");
        String numero = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("CEP: ");
        String cep = scanner.nextLine();
        
        Endereco endereco = new Endereco(logradouro, numero, bairro, cidade, cep);
        Cliente cliente = new Cliente(0, nome, telefone, endereco);
        
        clienteService.criarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void listarIngredientes() {
        List<Ingrediente> ingredientes = ingredienteService.listarTodos();
        if (ingredientes.isEmpty()) {
            System.out.println("Nenhum ingrediente cadastrado.");
            return;
        }
        
        System.out.println("\n=== INGREDIENTES ===");
        for (Ingrediente ingrediente : ingredientes) {
            System.out.println(ingrediente);
        }
    }

    private void listarIngredientesEmFalta() {
        List<Ingrediente> ingredientesEmFalta = ingredienteService.listarEmFalta();
        if (ingredientesEmFalta.isEmpty()) {
            System.out.println("Todos os ingredientes têm estoque disponível.");
            return;
        }
        
        System.out.println("\n=== INGREDIENTES EM FALTA ===");
        for (Ingrediente ingrediente : ingredientesEmFalta) {
            System.out.println(ingrediente);
        }
    }

    private void adicionarEstoque() throws IOException {
        listarIngredientes();
        System.out.print("ID do ingrediente: ");
        int id = lerOpcao();
        System.out.print("Quantidade a adicionar: ");
        int quantidade = lerOpcao();
        
        ingredienteService.adicionarEstoque(id, quantidade);
        System.out.println("Estoque adicionado com sucesso!");
    }

    private void listarPizzas() {
        List<Pizza> pizzas = cardapioService.listarTodasPizzas();
        if (pizzas.isEmpty()) {
            System.out.println("Nenhuma pizza cadastrada.");
            return;
        }
        
        System.out.println("\n=== PIZZAS DO CARDÁPIO ===");
        for (Pizza pizza : pizzas) {
            System.out.println(pizza);
            System.out.println("Ingredientes: " + pizza.getIngredientes().stream()
                                                    .map(Ingrediente::getNome)
                                                    .toList());
            System.out.println();
        }
    }

    private void listarBebidas() {
        List<Bebida> bebidas = cardapioService.listarTodasBebidas();
        if (bebidas.isEmpty()) {
            System.out.println("Nenhuma bebida cadastrada.");
            return;
        }
        
        System.out.println("\n=== BEBIDAS DO CARDÁPIO ===");
        for (Bebida bebida : bebidas) {
            System.out.println(bebida);
        }
    }

    private void criarPizzaPersonalizada() throws IOException {
        System.out.print("Nome da pizza: ");
        String nome = scanner.nextLine();
        
        System.out.println("Tamanhos disponíveis:");
        for (Tamanho tamanho : Tamanho.values()) {
            System.out.println(tamanho.ordinal() + ". " + tamanho);
        }
        System.out.print("Escolha o tamanho: ");
        int tamanhoEscolhido = lerOpcao();
        Tamanho tamanho = Tamanho.values()[tamanhoEscolhido];
        
        System.out.print("Preço base: ");
        double precoBase = Double.parseDouble(scanner.nextLine());
        
        List<Ingrediente> ingredientesEscolhidos = new ArrayList<>();
        listarIngredientes();
        
        System.out.println("Escolha os ingredientes (digite -1 para finalizar):");
        while (true) {
            System.out.print("ID do ingrediente: ");
            int id = lerOpcao();
            if (id == -1) break;
            
            Ingrediente ingrediente = ingredienteService.buscarPorId(id);
            if (ingrediente != null) {
                ingredientesEscolhidos.add(ingrediente);
                System.out.println("Ingrediente adicionado: " + ingrediente.getNome());
            } else {
                System.out.println("Ingrediente não encontrado!");
            }
        }
        
        Pizza pizza = cardapioService.criarPizzaPersonalizada(nome, tamanho, precoBase, ingredientesEscolhidos);
        System.out.println("Pizza criada: " + pizza);
    }

    private void listarPedidosPendentes() {
        List<Pedido> pedidos = pedidoService.listarPendentes();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido pendente.");
            return;
        }
        
        System.out.println("\n=== PEDIDOS PENDENTES ===");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void listarPedidosEmPreparo() {
        List<Pedido> pedidos = pedidoService.listarEmPreparo();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido em preparo.");
            return;
        }
        
        System.out.println("\n=== PEDIDOS EM PREPARO ===");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void atualizarStatusPedido() throws IOException, PedidoInvalidoException, EstoqueInsuficienteException {
        System.out.print("ID do pedido: ");
        int id = lerOpcao();
        
        System.out.println("Novos status disponíveis:");
        for (StatusPedido status : StatusPedido.values()) {
            System.out.println(status.ordinal() + ". " + status);
        }
        System.out.print("Escolha o novo status: ");
        int statusEscolhido = lerOpcao();
        StatusPedido novoStatus = StatusPedido.values()[statusEscolhido];
        
        pedidoService.atualizarStatusPedido(id, novoStatus);
        System.out.println("Status atualizado com sucesso!");
    }

    private void listarPedidosDoDia() {
        List<Pedido> pedidos = pedidoService.listarPedidosDoDia();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido hoje.");
            return;
        }
        
        System.out.println("\n=== PEDIDOS DO DIA ===");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS DO SISTEMA ===");
        System.out.println("Clientes: " + clienteService.getTotalClientes());
        System.out.println("Ingredientes: " + ingredienteService.getTotalIngredientes());
        System.out.println(cardapioService.getEstatisticas());
        System.out.println(pedidoService.getEstatisticas());
    }

    /**
     * Demonstra o caso de uso "Realizar um Pedido para Entrega"
     */
    private void demonstrarCasoDeUso() {
        System.out.println("\n=== DEMONSTRAÇÃO: CASO DE USO - REALIZAR PEDIDO PARA ENTREGA ===");
        
        try {
            // 1. Buscar cliente
            System.out.println("1. Buscando cliente por telefone...");
            Cliente cliente = clienteService.buscarPorTelefone("(11) 99999-9999");
            System.out.println("Cliente encontrado: " + cliente.getNome());
            
            // 2. Montar pedido
            System.out.println("\n2. Montando pedido...");
            List<ItemPedido> itens = new ArrayList<>();
            
            // Adicionar bebidas
            Bebida cocaCola = cardapioService.buscarBebidaPorId(1000);
            itens.add(new ItemPedido(cocaCola, 2));
            System.out.println("Adicionado: 2x " + cocaCola.getNome());
            
            // Adicionar pizza personalizada
            List<Ingrediente> ingredientesPizza = new ArrayList<>();
            ingredientesPizza.add(ingredienteService.buscarPorId(1)); // Mussarela
            ingredientesPizza.add(ingredienteService.buscarPorId(3)); // Bacon
            ingredientesPizza.add(ingredienteService.buscarPorId(4)); // Catupiry
            
            Pizza pizzaPersonalizada = new Pizza(999, "Pizza Especial", "Pizza personalizada", 
                                               20.00, Tamanho.GRANDE, ingredientesPizza);
            itens.add(new ItemPedido(pizzaPersonalizada, 1));
            System.out.println("Adicionado: 1x Pizza Especial (Grande)");
            
            // 3. Calcular total
            System.out.println("\n3. Calculando valor total...");
            double total = itens.stream().mapToDouble(ItemPedido::calcularSubtotal).sum();
            System.out.println("Valor total: R$" + String.format("%.2f", total));
            
            // 4. Criar pedido
            System.out.println("\n4. Criando pedido...");
            Endereco enderecoEntrega = new Endereco("Rua Nova", "456", "Jardim", "São Paulo", "09876-543");
            Pedido pedido = pedidoService.criarPedidoDelivery(cliente, itens, enderecoEntrega);
            System.out.println("Pedido criado: " + pedido);
            
            // 5. Confirmar pedido
            System.out.println("\n5. Confirmando pedido (verificando estoque)...");
            pedidoService.atualizarStatusPedido(pedido.getId(), StatusPedido.EM_PREPARO);
            System.out.println("Pedido confirmado e em preparo!");
            
            System.out.println("\n=== CASO DE USO CONCLUÍDO COM SUCESSO! ===");
            
        } catch (Exception e) {
            System.err.println("Erro na demonstração: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
