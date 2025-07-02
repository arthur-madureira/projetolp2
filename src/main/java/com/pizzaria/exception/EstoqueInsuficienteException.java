package com.pizzaria.exception;

/**
 * Exceção lançada quando não há estoque suficiente de um ingrediente
 */
public class EstoqueInsuficienteException extends Exception {
    private String nomeIngrediente;
    private int quantidadeDisponivel;
    private int quantidadeSolicitada;

    public EstoqueInsuficienteException(String message) {
        super(message);
    }

    public EstoqueInsuficienteException(String nomeIngrediente, int quantidadeDisponivel, int quantidadeSolicitada) {
        super(String.format("Estoque insuficiente para o ingrediente '%s'. Disponível: %d, Solicitado: %d", 
                          nomeIngrediente, quantidadeDisponivel, quantidadeSolicitada));
        this.nomeIngrediente = nomeIngrediente;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeSolicitada = quantidadeSolicitada;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }
}
