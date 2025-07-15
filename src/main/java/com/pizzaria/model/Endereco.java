package com.pizzaria.model;

import com.google.gson.annotations.Expose;

/**
 * Classe que representa o endereço de um cliente para entregas
 */
public class Endereco {
    @Expose
    private String logradouro;
    @Expose
    private String numero;
    @Expose
    private String bairro;
    @Expose
    private String cidade;
    @Expose
    private String cep;

    // Construtor padrão
    public Endereco() {
    }

    // Construtor com parâmetros
    public Endereco(String logradouro, String numero, String bairro, String cidade, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
    }

    // Getters e Setters
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return String.format("%s, %s - %s, %s - CEP: %s", 
                           logradouro, numero, bairro, cidade, cep);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Endereco endereco = (Endereco) obj;
        return logradouro.equals(endereco.logradouro) &&
               numero.equals(endereco.numero) &&
               bairro.equals(endereco.bairro) &&
               cidade.equals(endereco.cidade) &&
               cep.equals(endereco.cep);
    }

    @Override
    public int hashCode() {
        int result = logradouro.hashCode();
        result = 31 * result + numero.hashCode();
        result = 31 * result + bairro.hashCode();
        result = 31 * result + cidade.hashCode();
        result = 31 * result + cep.hashCode();
        return result;
    }
}
