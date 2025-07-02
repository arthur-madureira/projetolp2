package com.pizzaria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um pedido feito por um cliente
 * É a entidade central do sistema
 */
public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemPedido> itensDoPedido;
    private double valorTotal;
    private StatusPedido status;
    private LocalDateTime dataHora;
    private Endereco enderecoEntrega;

    // Construtor padrão
    public Pedido() {
        this.itensDoPedido = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.valorTotal = 0.0;
    }

    // Construtor com parâmetros
    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itensDoPedido = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.valorTotal = 0.0;
        this.enderecoEntrega = cliente.getEndereco();
    }

    // Construtor completo
    public Pedido(int id, Cliente cliente, Endereco enderecoEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.itensDoPedido = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.valorTotal = 0.0;
        this.enderecoEntrega = enderecoEntrega;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItensDoPedido() {
        return new ArrayList<>(itensDoPedido);
    }

    public void setItensDoPedido(List<ItemPedido> itensDoPedido) {
        this.itensDoPedido = new ArrayList<>(itensDoPedido);
        calcularValorTotal();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    // Métodos de negócio
    public void adicionarItem(ItemPedido itemPedido) {
        // Verifica se o item já existe no pedido
        ItemPedido itemExistente = itensDoPedido.stream()
                .filter(item -> item.getItem().equals(itemPedido.getItem()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            // Se existe, aumenta a quantidade
            itemExistente.setQuantidade(itemExistente.getQuantidade() + itemPedido.getQuantidade());
        } else {
            // Se não existe, adiciona o novo item
            this.itensDoPedido.add(itemPedido);
        }
        calcularValorTotal();
    }

    public void removerItem(ItemPedido itemPedido) {
        this.itensDoPedido.remove(itemPedido);
        calcularValorTotal();
    }

    public void removerItem(ItemCardapio item) {
        this.itensDoPedido.removeIf(itemPedido -> itemPedido.getItem().equals(item));
        calcularValorTotal();
    }

    public void calcularValorTotal() {
        this.valorTotal = itensDoPedido.stream()
                                      .mapToDouble(ItemPedido::calcularSubtotal)
                                      .sum();
    }

    public boolean isDelivery() {
        return enderecoEntrega != null && !enderecoEntrega.equals(cliente.getEndereco());
    }

    public boolean podeSerCancelado() {
        return status == StatusPedido.PENDENTE || status == StatusPedido.EM_PREPARO;
    }

    public boolean podeSerAlterado() {
        return status == StatusPedido.PENDENTE;
    }

    public int getQuantidadeTotalItens() {
        return itensDoPedido.stream()
                           .mapToInt(ItemPedido::getQuantidade)
                           .sum();
    }

    @Override
    public String toString() {
        return String.format("Pedido{id=%d, cliente='%s', itens=%d, valorTotal=R$%.2f, status=%s, data=%s}", 
                           id, cliente.getNome(), itensDoPedido.size(), valorTotal, status, 
                           dataHora.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Pedido pedido = (Pedido) obj;
        return id == pedido.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
