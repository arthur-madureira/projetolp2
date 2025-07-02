package com.pizzaria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um cliente da pizzaria
 */
public class Cliente {
    private int id;
    private String nome;
    private String telefone;
    private Endereco endereco;
    private List<Pedido> pedidos;

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

    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = new ArrayList<>(pedidos);
    }

    // Métodos de negócio
    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

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
