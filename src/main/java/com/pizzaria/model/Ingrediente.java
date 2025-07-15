package com.pizzaria.model;

import com.google.gson.annotations.Expose;

/**
 * Classe que representa um ingrediente usado nas pizzas
 */
public class Ingrediente {
    @Expose
    private int id;
    @Expose
    private String nome;
    @Expose
    private int quantidadeEmEstoque;

    // Construtor padrão
    public Ingrediente() {
    }

    // Construtor com parâmetros
    public Ingrediente(int id, String nome, int quantidadeEmEstoque) {
        this.id = id;
        this.nome = nome;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
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

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    // Métodos de negócio
    public boolean temEstoque() {
        return quantidadeEmEstoque > 0;
    }

    public void reduzirEstoque(int quantidade) {
        if (quantidade <= quantidadeEmEstoque) {
            this.quantidadeEmEstoque -= quantidade;
        } else {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }
    }

    public void aumentarEstoque(int quantidade) {
        this.quantidadeEmEstoque += quantidade;
    }

    @Override
    public String toString() {
        return String.format("Ingrediente{id=%d, nome='%s', estoque=%d}", 
                           id, nome, quantidadeEmEstoque);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Ingrediente that = (Ingrediente) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
