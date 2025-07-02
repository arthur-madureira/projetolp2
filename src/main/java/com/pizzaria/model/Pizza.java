package com.pizzaria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma pizza do cardápio
 * Herda de ItemCardapio e implementa o cálculo de preço
 */
public class Pizza extends ItemCardapio {
    private double precoBase;
    private Tamanho tamanho;
    private List<Ingrediente> ingredientes;

    // Construtor padrão
    public Pizza() {
        super();
        this.ingredientes = new ArrayList<>();
    }

    // Construtor com parâmetros
    public Pizza(int id, String nome, String descricao, double precoBase, Tamanho tamanho) {
        super(id, nome, descricao);
        this.precoBase = precoBase;
        this.tamanho = tamanho;
        this.ingredientes = new ArrayList<>();
    }

    // Construtor completo
    public Pizza(int id, String nome, String descricao, double precoBase, Tamanho tamanho, List<Ingrediente> ingredientes) {
        super(id, nome, descricao);
        this.precoBase = precoBase;
        this.tamanho = tamanho;
        this.ingredientes = new ArrayList<>(ingredientes);
    }

    // Getters e Setters
    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(Tamanho tamanho) {
        this.tamanho = tamanho;
    }

    public List<Ingrediente> getIngredientes() {
        return new ArrayList<>(ingredientes);
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = new ArrayList<>(ingredientes);
    }

    // Métodos de negócio
    public void adicionarIngrediente(Ingrediente ingrediente) {
        this.ingredientes.add(ingrediente);
    }

    public void removerIngrediente(Ingrediente ingrediente) {
        this.ingredientes.remove(ingrediente);
    }

    public boolean contemIngrediente(Ingrediente ingrediente) {
        return this.ingredientes.contains(ingrediente);
    }

    /**
     * Calcula o preço da pizza baseado no preço base, tamanho e ingredientes
     * @return o preço total da pizza
     */
    @Override
    public double calcularPreco() {
        double precoIngredientes = ingredientes.stream()
                                             .mapToDouble(Ingrediente::getPrecoAdicional)
                                             .sum();
        
        return (precoBase + precoIngredientes) * tamanho.getMultiplicador();
    }

    @Override
    public String toString() {
        return String.format("Pizza{id=%d, nome='%s', tamanho=%s, precoBase=R$%.2f, ingredientes=%d, precoTotal=R$%.2f}", 
                           id, nome, tamanho, precoBase, ingredientes.size(), calcularPreco());
    }
}
