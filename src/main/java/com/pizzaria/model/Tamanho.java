package com.pizzaria.model;

/**
 * Enum que representa os tamanhos disponíveis para pizzas
 */
public enum Tamanho {
    PEQUENA("Pequena", "P", 1.0),
    MEDIA("Média", "M", 1.5),
    GRANDE("Grande", "G", 2.0);

    private final String descricao;
    private final String sigla;
    private final double multiplicador;

    Tamanho(String descricao, String sigla, double multiplicador) {
        this.descricao = descricao;
        this.sigla = sigla;
        this.multiplicador = multiplicador;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    /**
     * Obtém um tamanho pela sigla (P, M, G)
     */
    public static Tamanho porSigla(String sigla) {
        for (Tamanho tamanho : values()) {
            if (tamanho.getSigla().equalsIgnoreCase(sigla)) {
                return tamanho;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao + " (" + sigla + ")";
    }
}
