package com.pizzaria.model;

/**
 * Classe que representa uma bebida do cardápio
 * Herda de ItemCardapio e implementa o cálculo de preço
 */
public class Bebida extends ItemCardapio {
    private double precoFixo;
    private int volumeEmML;

    // Construtor padrão
    public Bebida() {
        super();
    }

    // Construtor com parâmetros
    public Bebida(int id, String nome, String descricao, double precoFixo, int volumeEmML) {
        super(id, nome, descricao);
        this.precoFixo = precoFixo;
        this.volumeEmML = volumeEmML;
    }

    // Getters e Setters
    public double getPrecoFixo() {
        return precoFixo;
    }

    public void setPrecoFixo(double precoFixo) {
        this.precoFixo = precoFixo;
    }

    public int getVolumeEmML() {
        return volumeEmML;
    }

    public void setVolumeEmML(int volumeEmML) {
        this.volumeEmML = volumeEmML;
    }

    /**
     * Calcula o preço da bebida (preço fixo)
     * @return o preço da bebida
     */
    @Override
    public double calcularPreco() {
        return precoFixo;
    }

    @Override
    public String toString() {
        return String.format("Bebida{id=%d, nome='%s', volume=%dml, preco=R$%.2f}", 
                           id, nome, volumeEmML, precoFixo);
    }
}
