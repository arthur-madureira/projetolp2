package com.pizzaria.model;

import com.google.gson.annotations.Expose;

/**
 * Classe associativa que liga um Pedido a um ItemCardapio
 * Permite definir a quantidade de cada item no pedido
 */
public class ItemPedido {
    @Expose
    private ItemCardapio item;
    @Expose
    private int quantidade;
    @Expose
    private double precoUnitario;

    // Construtor padrão
    public ItemPedido() {
    }

    // Construtor com parâmetros
    public ItemPedido(ItemCardapio item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
        this.precoUnitario = item.calcularPreco();
    }

    // Construtor completo
    public ItemPedido(ItemCardapio item, int quantidade, double precoUnitario) {
        this.item = item;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public ItemCardapio getItem() {
        return item;
    }

    public void setItem(ItemCardapio item) {
        this.item = item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    // Métodos de negócio
    public double calcularSubtotal() {
        return precoUnitario * quantidade;
    }

    public void atualizarPrecoUnitario() {
        this.precoUnitario = item.calcularPreco();
    }

    @Override
    public String toString() {
        return String.format("ItemPedido{item='%s', quantidade=%d, precoUnitario=R$%.2f, subtotal=R$%.2f}", 
                           item.getNome(), quantidade, precoUnitario, calcularSubtotal());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemPedido that = (ItemPedido) obj;
        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }
}
