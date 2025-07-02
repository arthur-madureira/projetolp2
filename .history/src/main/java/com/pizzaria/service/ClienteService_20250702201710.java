package com.pizzaria.service;

import com.pizzaria.exception.ClienteNaoEncontradoException;
import com.pizzaria.model.Cliente;
import com.pizzaria.util.JsonPersistence;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo CRUD de clientes
 */
public class ClienteService {
    private static final String CLIENTES_FILE = "clientes.json";
    private List<Cliente> clientes;
    private int proximoId;

    public ClienteService() {
        this.clientes = new ArrayList<>();
        this.proximoId = 1;
        carregarDados();
    }

    /**
     * Cria um novo cliente
     */
    public Cliente criarCliente(Cliente cliente) throws IOException {
        cliente.setId(proximoId++);
        clientes.add(cliente);
        salvarDados();
        return cliente;
    }

    /**
     * Busca um cliente por ID
     */
    public Cliente buscarPorId(int id) throws ClienteNaoEncontradoException {
        return clientes.stream()
                      .filter(c -> c.getId() == id)
                      .findFirst()
                      .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    /**
     * Busca um cliente por telefone
     */
    public Cliente buscarPorTelefone(String telefone) throws ClienteNaoEncontradoException {
        return clientes.stream()
                      .filter(c -> c.getTelefone().equals(telefone))
                      .findFirst()
                      .orElseThrow(() -> new ClienteNaoEncontradoException(telefone, true));
    }

    /**
     * Busca clientes por nome (busca parcial)
     */
    public List<Cliente> buscarPorNome(String nome) {
        return clientes.stream()
                      .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                      .toList();
    }

    /**
     * Lista todos os clientes
     */
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    /**
     * Atualiza um cliente existente
     */
    public Cliente atualizarCliente(Cliente cliente) throws IOException, ClienteNaoEncontradoException {
        Optional<Cliente> clienteExistente = clientes.stream()
                                                    .filter(c -> c.getId() == cliente.getId())
                                                    .findFirst();
        
        if (clienteExistente.isEmpty()) {
            throw new ClienteNaoEncontradoException(cliente.getId());
        }

        // Remove o cliente antigo e adiciona o atualizado
        clientes.removeIf(c -> c.getId() == cliente.getId());
        clientes.add(cliente);
        salvarDados();
        return cliente;
    }

    /**
     * Remove um cliente
     */
    public void removerCliente(int id) throws IOException, ClienteNaoEncontradoException {
        boolean removido = clientes.removeIf(c -> c.getId() == id);
        
        if (!removido) {
            throw new ClienteNaoEncontradoException(id);
        }
        
        salvarDados();
    }

    /**
     * Verifica se um telefone já está cadastrado
     */
    public boolean telefoneJaCadastrado(String telefone) {
        return clientes.stream()
                      .anyMatch(c -> c.getTelefone().equals(telefone));
    }

    /**
     * Verifica se um telefone já está cadastrado por outro cliente
     */
    public boolean telefoneJaCadastradoPorOutro(String telefone, int clienteId) {
        return clientes.stream()
                      .anyMatch(c -> c.getTelefone().equals(telefone) && c.getId() != clienteId);
    }

    /**
     * Obtém o total de clientes cadastrados
     */
    public int getTotalClientes() {
        return clientes.size();
    }

    /**
     * Carrega os dados do arquivo JSON
     */
    private void carregarDados() {
        try {
            Type listType = new TypeToken<List<Cliente>>(){}.getType();
            clientes = JsonPersistence.loadFromFile(CLIENTES_FILE, listType);
            
            // Atualiza o próximo ID
            proximoId = clientes.stream()
                               .mapToInt(Cliente::getId)
                               .max()
                               .orElse(0) + 1;
        } catch (IOException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            clientes = new ArrayList<>();
            proximoId = 1;
        }
    }

    /**
     * Salva os dados no arquivo JSON
     */
    private void salvarDados() throws IOException {
        JsonPersistence.saveToFile(clientes, CLIENTES_FILE);
    }
}
