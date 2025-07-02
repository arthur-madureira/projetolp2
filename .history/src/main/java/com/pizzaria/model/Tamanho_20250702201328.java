package com.pizzaria.model;

/**
 * Enum que representa os tamanhos disponíveis para pizzas
 */
public enum Tamanho {
    PEQUENA("Pequena", 1.0),
    MEDIA("Média", 1.3),
    GRANDE("Grande", 1.6);

    private final String descricao;
    private final double multiplicador;

    Tamanho(String descricao, double multiplicador) {
        this.descricao = descricao;
        this.multiplicador = multiplicador;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
