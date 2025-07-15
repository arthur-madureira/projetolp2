package com.pizzaria.model;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma pizza do cardápio
 * Herda de ItemCardapio e implementa o cálculo de preço
 */
public class Pizza extends ItemCardapio {
    @Expose
    private double precoBase;
    @Expose
    private Tamanho tamanho;
    @Expose
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
     * Calcula o preço da pizza baseado no preço base e tamanho
     * @return o preço total da pizza
     */
    @Override
    public double calcularPreco() {
        return precoBase * tamanho.getMultiplicador();
    }

    @Override
    public String toString() {
        double precoP = precoBase * Tamanho.PEQUENA.getMultiplicador();
        double precoM = precoBase * Tamanho.MEDIA.getMultiplicador();
        double precoG = precoBase * Tamanho.GRANDE.getMultiplicador();
        
        return String.format("Pizza{id=%d, nome='%s', ingredientes=%d}%n" +
                           "Preços: P=R$%.2f | M=R$%.2f | G=R$%.2f%n" +
                           "Ingredientes: %s", 
                           id, nome, ingredientes.size(), 
                           precoP, precoM, precoG,
                           ingredientes.stream().map(Ingrediente::getNome).toList());
    }
}
