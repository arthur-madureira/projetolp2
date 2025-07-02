package com.pizzaria.model;

/**
 * Classe abstrata que representa um item do cardápio
 * Serve como classe base para Pizza e Bebida
 */
public abstract class ItemCardapio {
    protected int id;
    protected String nome;
    protected String descricao;

    // Construtor padrão
    public ItemCardapio() {
    }

    // Construtor com parâmetros
    public ItemCardapio(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Método abstrato que deve ser implementado pelas subclasses
    public abstract double calcularPreco();

    @Override
    public String toString() {
        return String.format("%s{id=%d, nome='%s', descricao='%s'}", 
                           getClass().getSimpleName(), id, nome, descricao);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemCardapio that = (ItemCardapio) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
