package com.pizzaria.service;

import com.pizzaria.exception.*;
import com.pizzaria.model.*;
import com.pizzaria.util.JsonPersistence;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo CRUD de pedidos e lógica de negócio relacionada
 */
public class PedidoService {
    private static final String PEDIDOS_FILE = "pedidos.json";
    
    private List<Pedido> pedidos;
    private int proximoId;
    private ClienteService clienteService;
    private IngredienteService ingredienteService;
    private CardapioService cardapioService;

    public PedidoService(ClienteService clienteService, IngredienteService ingredienteService, CardapioService cardapioService) {
        this.pedidos = new ArrayList<>();
        this.proximoId = 1;
        this.clienteService = clienteService;
        this.ingredienteService = ingredienteService;
        this.cardapioService = cardapioService;
        carregarDados();
    }

    /**
     * Cria um novo pedido
     */
    public Pedido criarPedido(Cliente cliente, List<ItemPedido> itens) throws IOException, EstoqueInsuficienteException {
        // Verifica estoque antes de criar o pedido
        verificarEstoqueParaPedido(itens);
        
        Pedido pedido = new Pedido(proximoId++, cliente);
        pedido.setItensDoPedido(itens);
        pedido.calcularValorTotal();
        
        pedidos.add(pedido);
        // Remover a adição do pedido ao cliente para evitar referência circular
        // cliente.adicionarPedido(pedido);
        
        salvarDados();
        return pedido;
    }

    /**
     * Cria um novo pedido para delivery
     */
    public Pedido criarPedidoDelivery(Cliente cliente, List<ItemPedido> itens, Endereco enderecoEntrega) 
            throws IOException, EstoqueInsuficienteException {
        verificarEstoqueParaPedido(itens);
        
        Pedido pedido = new Pedido(proximoId++, cliente, enderecoEntrega);
        pedido.setItensDoPedido(itens);
        pedido.calcularValorTotal();
        
        pedidos.add(pedido);
        // Remover a adição do pedido ao cliente para evitar referência circular
        // cliente.adicionarPedido(pedido);
        
        salvarDados();
        return pedido;
    }

    /**
     * Busca um pedido por ID
     */
    public Pedido buscarPorId(int id) throws PedidoInvalidoException {
        return pedidos.stream()
                     .filter(p -> p.getId() == id)
                     .findFirst()
                     .orElseThrow(() -> new PedidoInvalidoException(id));
    }

    /**
     * Lista todos os pedidos
     */
    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    /**
     * Lista pedidos por cliente
     */
    public List<Pedido> listarPorCliente(Cliente cliente) {
        return pedidos.stream()
                     .filter(p -> p.getClienteId() == cliente.getId())
                     .toList();
    }

    /**
     * Lista pedidos do dia atual
     */
    public List<Pedido> listarPedidosDoDia() {
        LocalDate hoje = LocalDate.now();
        return pedidos.stream()
                     .filter(p -> p.getDataHora().toLocalDate().equals(hoje))
                     .toList();
    }

    /**
     * Lista pedidos por status
     */
    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidos.stream()
                     .filter(p -> p.getStatus() == status)
                     .toList();
    }

    /**
     * Lista pedidos pendentes
     */
    public List<Pedido> listarPendentes() {
        return listarPorStatus(StatusPedido.PENDENTE);
    }

    /**
     * Lista pedidos em preparo
     */
    public List<Pedido> listarEmPreparo() {
        return listarPorStatus(StatusPedido.EM_PREPARO);
    }

    /**
     * Atualiza o status de um pedido
     */
    public void atualizarStatusPedido(int id, StatusPedido novoStatus) 
            throws IOException, PedidoInvalidoException, EstoqueInsuficienteException {
        Pedido pedido = buscarPorId(id);
        StatusPedido statusAnterior = pedido.getStatus();
        
        // Validações de transição de status
        validarTransicaoStatus(statusAnterior, novoStatus);
        
        // Se está confirmando o pedido (PENDENTE -> EM_PREPARO), verifica e consome estoque
        if (statusAnterior == StatusPedido.PENDENTE && novoStatus == StatusPedido.EM_PREPARO) {
            confirmarPedido(pedido);
        }
        
        pedido.setStatus(novoStatus);
        salvarDados();
    }

    /**
     * Confirma um pedido e consome ingredientes do estoque
     */
    public void confirmarPedido(Pedido pedido) throws EstoqueInsuficienteException, IOException {
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new IllegalStateException("Só é possível confirmar pedidos pendentes");
        }
        
        // Consome ingredientes das pizzas
        for (ItemPedido itemPedido : pedido.getItensDoPedido()) {
            if (itemPedido.getItem() instanceof Pizza pizza) {
                for (int i = 0; i < itemPedido.getQuantidade(); i++) {
                    ingredienteService.consumirIngredientes(pizza.getIngredientes());
                }
            }
        }
        
        pedido.setStatus(StatusPedido.EM_PREPARO);
        salvarDados();
    }

    /**
     * Cancela um pedido
     */
    public void cancelarPedido(int id) throws IOException, PedidoInvalidoException {
        Pedido pedido = buscarPorId(id);
        
        if (!pedido.podeSerCancelado()) {
            throw new PedidoInvalidoException(id, pedido.getStatus(), "cancelar");
        }
        
        pedido.setStatus(StatusPedido.CANCELADO);
        salvarDados();
    }

    /**
     * Adiciona um item a um pedido pendente
     */
    public void adicionarItemAoPedido(int pedidoId, ItemPedido novoItem) 
            throws IOException, PedidoInvalidoException, EstoqueInsuficienteException {
        Pedido pedido = buscarPorId(pedidoId);
        
        if (!pedido.podeSerAlterado()) {
            throw new PedidoInvalidoException(pedidoId, pedido.getStatus(), "alterar");
        }
        
        // Verifica estoque se for pizza
        if (novoItem.getItem() instanceof Pizza pizza) {
            for (int i = 0; i < novoItem.getQuantidade(); i++) {
                ingredienteService.verificarEstoqueDisponivel(pizza.getIngredientes());
            }
        }
        
        pedido.adicionarItem(novoItem);
        salvarDados();
    }

    /**
     * Remove um item de um pedido pendente
     */
    public void removerItemDoPedido(int pedidoId, ItemCardapio item) 
            throws IOException, PedidoInvalidoException {
        Pedido pedido = buscarPorId(pedidoId);
        
        if (!pedido.podeSerAlterado()) {
            throw new PedidoInvalidoException(pedidoId, pedido.getStatus(), "alterar");
        }
        
        pedido.removerItem(item);
        salvarDados();
    }

    /**
     * Calcula o faturamento do dia
     */
    public double calcularFaturamentoDoDia() {
        return listarPedidosDoDia().stream()
                                  .filter(p -> p.getStatus() == StatusPedido.CONCLUIDO)
                                  .mapToDouble(Pedido::getValorTotal)
                                  .sum();
    }

    /**
     * Calcula o faturamento total
     */
    public double calcularFaturamentoTotal() {
        return pedidos.stream()
                     .filter(p -> p.getStatus() == StatusPedido.CONCLUIDO)
                     .mapToDouble(Pedido::getValorTotal)
                     .sum();
    }

    /**
     * Obtém estatísticas dos pedidos
     */
    public String getEstatisticas() {
        int total = pedidos.size();
        int pendentes = listarPendentes().size();
        int emPreparo = listarEmPreparo().size();
        int concluidos = listarPorStatus(StatusPedido.CONCLUIDO).size();
        double faturamentoDia = calcularFaturamentoDoDia();
        
        return String.format(
            "Pedidos: %d total, %d pendentes, %d em preparo, %d concluídos. Faturamento do dia: R$%.2f",
            total, pendentes, emPreparo, concluidos, faturamentoDia
        );
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Verifica se há estoque suficiente para todos os itens do pedido
     */
    private void verificarEstoqueParaPedido(List<ItemPedido> itens) throws EstoqueInsuficienteException {
        for (ItemPedido itemPedido : itens) {
            if (itemPedido.getItem() instanceof Pizza pizza) {
                for (int i = 0; i < itemPedido.getQuantidade(); i++) {
                    ingredienteService.verificarEstoqueDisponivel(pizza.getIngredientes());
                }
            }
        }
    }

    /**
     * Valida se uma transição de status é permitida
     */
    private void validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) 
            throws PedidoInvalidoException {
        // Não permite alterar pedidos já concluídos ou cancelados
        if (statusAtual == StatusPedido.CONCLUIDO || statusAtual == StatusPedido.CANCELADO) {
            throw new PedidoInvalidoException(
                String.format("Não é possível alterar pedido %s", statusAtual)
            );
        }
        
        // Não permite retroceder para PENDENTE
        if (novoStatus == StatusPedido.PENDENTE && statusAtual != StatusPedido.PENDENTE) {
            throw new PedidoInvalidoException(
                "Não é possível retroceder para status PENDENTE"
            );
        }
        
        // Permite todas as outras transições válidas
        boolean transicaoValida = switch (statusAtual) {
            case PENDENTE -> novoStatus == StatusPedido.EM_PREPARO || 
                           novoStatus == StatusPedido.SAIU_PARA_ENTREGA || 
                           novoStatus == StatusPedido.CONCLUIDO || 
                           novoStatus == StatusPedido.CANCELADO;
            case EM_PREPARO -> novoStatus == StatusPedido.SAIU_PARA_ENTREGA || 
                             novoStatus == StatusPedido.CONCLUIDO || 
                             novoStatus == StatusPedido.CANCELADO;
            case SAIU_PARA_ENTREGA -> novoStatus == StatusPedido.CONCLUIDO;
            case CONCLUIDO, CANCELADO -> false; // Estados finais
        };
        
        if (!transicaoValida) {
            throw new PedidoInvalidoException(
                String.format("Transição inválida de %s para %s", statusAtual, novoStatus)
            );
        }
    }

    /**
     * Carrega os dados do arquivo JSON
     */
    private void carregarDados() {
        try {
            Type listType = new TypeToken<List<Pedido>>(){}.getType();
            pedidos = JsonPersistence.loadFromFile(PEDIDOS_FILE, listType);
            
            // Reconstroi as referências dos clientes
            for (Pedido pedido : pedidos) {
                try {
                    Cliente cliente = clienteService.buscarPorId(pedido.getClienteId());
                    pedido.setCliente(cliente);
                } catch (ClienteNaoEncontradoException e) {
                    System.err.println("Cliente não encontrado para pedido " + pedido.getId() + ": " + e.getMessage());
                }
            }
            
            proximoId = pedidos.stream()
                              .mapToInt(Pedido::getId)
                              .max()
                              .orElse(0) + 1;
        } catch (IOException e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
            pedidos = new ArrayList<>();
            proximoId = 1;
        }
    }

    /**
     * Salva os dados no arquivo JSON
     */
    private void salvarDados() throws IOException {
        JsonPersistence.saveToFile(pedidos, PEDIDOS_FILE);
    }
}
