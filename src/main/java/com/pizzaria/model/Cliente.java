package com.pizzaria.model;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um cliente da pizzaria
 */
public class Cliente {
    @Expose
    private int id;
    @Expose
    private String nome;
    @Expose
    private String telefone;
    @Expose
    private Endereco endereco;
    // Não incluir pedidos na serialização para evitar referência circular
    private transient List<Pedido> pedidos;

    // Construtor padrão
    public Cliente() {
        this.pedidos = new ArrayList<>();
    }

    // Construtor com parâmetros
    public Cliente(int id, String nome, String telefone, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.pedidos = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    /**
     * Obtém os pedidos do cliente.
     * Nota: Como pedidos é transient, esta lista estará sempre vazia após desserialização.
     * Use PedidoService.listarPorCliente(cliente) para obter os pedidos do cliente.
     */
    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    /**
     * Define os pedidos do cliente.
     * Nota: Este método é usado apenas para manter compatibilidade com o código legado.
     * A relação cliente-pedido é mantida através do clienteId nos pedidos.
     */
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = new ArrayList<>(pedidos);
    }

    // Métodos de negócio
    /**
     * Adiciona um pedido à lista local do cliente.
     * Nota: Este método não persiste a relação. Use PedidoService para criar pedidos.
     */
    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

    /**
     * Remove um pedido da lista local do cliente.
     * Nota: Este método não remove da persistência. Use PedidoService para gerenciar pedidos.
     */
    public void removerPedido(Pedido pedido) {
        this.pedidos.remove(pedido);
    }

    @Override
    public String toString() {
        return String.format("Cliente{id=%d, nome='%s', telefone='%s', endereco=%s}", 
                           id, nome, telefone, endereco);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cliente cliente = (Cliente) obj;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
