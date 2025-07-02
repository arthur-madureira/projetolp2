package com.pizzaria.exception;

/**
 * Exceção lançada quando um cliente não é encontrado
 */
public class ClienteNaoEncontradoException extends Exception {
    private int clienteId;
    private String telefone;

    public ClienteNaoEncontradoException(String message) {
        super(message);
    }

    public ClienteNaoEncontradoException(int clienteId) {
        super(String.format("Cliente com ID %d não encontrado", clienteId));
        this.clienteId = clienteId;
    }

    public ClienteNaoEncontradoException(String telefone, boolean isTelefone) {
        super(String.format("Cliente com telefone '%s' não encontrado", telefone));
        this.telefone = telefone;
    }

    public int getClienteId() {
        return clienteId;
    }

    public String getTelefone() {
        return telefone;
    }
}
