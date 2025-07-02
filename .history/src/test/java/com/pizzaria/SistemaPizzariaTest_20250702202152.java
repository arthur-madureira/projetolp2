package com.pizzaria;

import com.pizzaria.model.*;
import com.pizzaria.service.*;
import com.pizzaria.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Testes unitários para o Sistema de Pizzaria
 */
public class SistemaPizzariaTest {
    
    private ClienteService clienteService;
    private IngredienteService ingredienteService;
    private CardapioService cardapioService;
    private PedidoService pedidoService;
    
    @BeforeEach
    void setUp() {
        clienteService = new ClienteService();
        ingredienteService = new IngredienteService();
        cardapioService = new CardapioService();
        pedidoService = new PedidoService(clienteService, ingredienteService, cardapioService);
    }
    
    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    void testCriarCliente() throws IOException {
        // Arrange
        Endereco endereco = new Endereco("Rua A", "123", "Centro", "São Paulo", "01234-567");
        Cliente cliente = new Cliente(0, "João Silva", "(11) 99999-9999", endereco);
        
        // Act
        Cliente clienteCriado = clienteService.criarCliente(cliente);
        
        // Assert
        assertNotNull(clienteCriado);
        assertTrue(clienteCriado.getId() > 0);
        assertEquals("João Silva", clienteCriado.getNome());
        assertEquals("(11) 99999-9999", clienteCriado.getTelefone());
    }
    
    @Test
    @DisplayName("Deve criar um ingrediente com estoque")
    void testCriarIngrediente() throws IOException {
        // Arrange
        Ingrediente ingrediente = new Ingrediente(0, "Mussarela", 2.50, 100);
        
        // Act
        Ingrediente ingredienteCriado = ingredienteService.criarIngrediente(ingrediente);
        
        // Assert
        assertNotNull(ingredienteCriado);
        assertTrue(ingredienteCriado.getId() > 0);
        assertEquals("Mussarela", ingredienteCriado.getNome());
        assertEquals(2.50, ingredienteCriado.getPrecoAdicional());
        assertEquals(100, ingredienteCriado.getQuantidadeEmEstoque());
        assertTrue(ingredienteCriado.temEstoque());
    }
    
    @Test
    @DisplayName("Deve calcular o preço da pizza corretamente")
    void testCalcularPrecoPizza() throws IOException {
        // Arrange
        Ingrediente mussarela = ingredienteService.criarIngrediente(new Ingrediente(0, "Mussarela", 2.00, 50));
        Ingrediente bacon = ingredienteService.criarIngrediente(new Ingrediente(0, "Bacon", 3.00, 30));
        
        List<Ingrediente> ingredientes = List.of(mussarela, bacon);
        Pizza pizza = new Pizza(0, "Pizza Especial", "Pizza com mussarela e bacon", 
                               15.00, Tamanho.GRANDE, ingredientes);
        
        // Act
        double preco = pizza.calcularPreco();
        
        // Assert
        // (15.00 + 2.00 + 3.00) * 1.6 (multiplicador GRANDE) = 32.00
        assertEquals(32.00, preco, 0.01);
    }
    
    @Test
    @DisplayName("Deve calcular o preço da bebida corretamente")
    void testCalcularPrecoBebida() {
        // Arrange
        Bebida bebida = new Bebida(0, "Coca-Cola", "Refrigerante", 4.50, 350);
        
        // Act
        double preco = bebida.calcularPreco();
        
        // Assert
        assertEquals(4.50, preco, 0.01);
    }
    
    @Test
    @DisplayName("Deve criar um pedido e calcular valor total")
    void testCriarPedido() throws IOException, EstoqueInsuficienteException, ClienteNaoEncontradoException {
        // Arrange
        // Criar cliente
        Endereco endereco = new Endereco("Rua B", "456", "Jardim", "São Paulo", "09876-543");
        Cliente cliente = clienteService.criarCliente(new Cliente(0, "Maria", "(11) 88888-8888", endereco));
        
        // Criar ingredientes
        Ingrediente mussarela = ingredienteService.criarIngrediente(new Ingrediente(0, "Mussarela", 2.00, 50));
        
        // Criar pizza
        List<Ingrediente> ingredientes = List.of(mussarela);
        Pizza pizza = cardapioService.criarPizza(new Pizza(0, "Margherita", "Pizza simples", 
                                                           12.00, Tamanho.MEDIA, ingredientes));
        
        // Criar bebida
        Bebida bebida = cardapioService.criarBebida(new Bebida(0, "Água", "Água mineral", 2.50, 500));
        
        // Criar itens do pedido
        List<ItemPedido> itens = List.of(
            new ItemPedido(pizza, 1),
            new ItemPedido(bebida, 2)
        );
        
        // Act
        Pedido pedido = pedidoService.criarPedido(cliente, itens);
        
        // Assert
        assertNotNull(pedido);
        assertTrue(pedido.getId() > 0);
        assertEquals(cliente.getId(), pedido.getCliente().getId());
        assertEquals(2, pedido.getItensDoPedido().size());
        assertEquals(StatusPedido.PENDENTE, pedido.getStatus());
        
        // Verificar valor total: (12.00 + 2.00) * 1.3 + 2.50 * 2 = 18.20 + 5.00 = 23.20
        assertEquals(23.20, pedido.getValorTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando estoque insuficiente")
    void testEstoqueInsuficiente() throws IOException {
        // Arrange
        Ingrediente ingredienteSemEstoque = ingredienteService.criarIngrediente(
            new Ingrediente(0, "Tomate", 1.50, 0));
        
        // Act & Assert
        assertThrows(EstoqueInsuficienteException.class, () -> {
            ingredienteService.reduzirEstoque(ingredienteSemEstoque.getId(), 1);
        });
    }
    
    @Test
    @DisplayName("Deve atualizar status do pedido corretamente")
    void testAtualizarStatusPedido() throws IOException, EstoqueInsuficienteException, 
                                            ClienteNaoEncontradoException, PedidoInvalidoException {
        // Arrange
        Cliente cliente = clienteService.criarCliente(
            new Cliente(0, "Pedro", "(11) 77777-7777", 
                       new Endereco("Rua C", "789", "Vila", "São Paulo", "12345-678")));
        
        Ingrediente ingrediente = ingredienteService.criarIngrediente(
            new Ingrediente(0, "Queijo", 2.50, 10));
        
        Pizza pizza = cardapioService.criarPizza(
            new Pizza(0, "Queijo", "Pizza de queijo", 10.00, Tamanho.PEQUENA, List.of(ingrediente)));
        
        List<ItemPedido> itens = List.of(new ItemPedido(pizza, 1));
        Pedido pedido = pedidoService.criarPedido(cliente, itens);
        
        // Act
        pedidoService.atualizarStatusPedido(pedido.getId(), StatusPedido.EM_PREPARO);
        
        // Assert
        Pedido pedidoAtualizado = pedidoService.buscarPorId(pedido.getId());
        assertEquals(StatusPedido.EM_PREPARO, pedidoAtualizado.getStatus());
        
        // Verificar se o estoque foi reduzido
        Ingrediente ingredienteAtualizado = ingredienteService.buscarPorId(ingrediente.getId());
        assertEquals(9, ingredienteAtualizado.getQuantidadeEmEstoque());
    }
    
    @Test
    @DisplayName("Deve buscar cliente por telefone")
    void testBuscarClientePorTelefone() throws IOException, ClienteNaoEncontradoException {
        // Arrange
        String telefone = "(11) 55555-5555";
        Cliente cliente = clienteService.criarCliente(
            new Cliente(0, "Ana", telefone, 
                       new Endereco("Rua D", "101", "Centro", "São Paulo", "54321-876")));
        
        // Act
        Cliente clienteEncontrado = clienteService.buscarPorTelefone(telefone);
        
        // Assert
        assertNotNull(clienteEncontrado);
        assertEquals(cliente.getId(), clienteEncontrado.getId());
        assertEquals("Ana", clienteEncontrado.getNome());
        assertEquals(telefone, clienteEncontrado.getTelefone());
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado")
    void testClienteNaoEncontrado() {
        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, () -> {
            clienteService.buscarPorTelefone("(11) 00000-0000");
        });
    }
    
    @Test
    @DisplayName("Deve testar polimorfismo no cálculo de preços")
    void testPolimorfismoCalculoPrecos() throws IOException {
        // Arrange
        Ingrediente ingrediente = ingredienteService.criarIngrediente(
            new Ingrediente(0, "Presunto", 3.00, 20));
        
        Pizza pizza = new Pizza(0, "Presunto", "Pizza de presunto", 
                               13.00, Tamanho.MEDIA, List.of(ingrediente));
        Bebida bebida = new Bebida(0, "Suco", "Suco natural", 6.00, 300);
        
        List<ItemCardapio> itens = List.of(pizza, bebida);
        
        // Act & Assert
        for (ItemCardapio item : itens) {
            double preco = item.calcularPreco();
            assertTrue(preco > 0);
            
            if (item instanceof Pizza) {
                // (13.00 + 3.00) * 1.3 = 20.80
                assertEquals(20.80, preco, 0.01);
            } else if (item instanceof Bebida) {
                assertEquals(6.00, preco, 0.01);
            }
        }
    }
}
