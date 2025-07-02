package com.pizzaria.exception;

import com.pizzaria.model.StatusPedido;

/**
 * Exceção lançada quando uma operação inválida é realizada em um pedido
 */
public class PedidoInvalidoException extends Exception {
    private int pedidoId;
    private StatusPedido statusAtual;

    public PedidoInvalidoException(String message) {
        super(message);
    }

    public PedidoInvalidoException(int pedidoId, StatusPedido statusAtual, String operacao) {
        super(String.format("Não é possível %s o pedido %d no status '%s'", 
                          operacao, pedidoId, statusAtual));
        this.pedidoId = pedidoId;
        this.statusAtual = statusAtual;
    }

    public PedidoInvalidoException(int pedidoId) {
        super(String.format("Pedido com ID %d não encontrado", pedidoId));
        this.pedidoId = pedidoId;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public StatusPedido getStatusAtual() {
        return statusAtual;
    }
}
