package com.pizzaria.service;

import com.pizzaria.exception.EstoqueInsuficienteException;
import com.pizzaria.model.Ingrediente;
import com.pizzaria.util.JsonPersistence;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo CRUD de ingredientes e controle de estoque
 */
public class IngredienteService {
    private static final String INGREDIENTES_FILE = "ingredientes.json";
    private List<Ingrediente> ingredientes;
    private int proximoId;

    public IngredienteService() {
        this.ingredientes = new ArrayList<>();
        this.proximoId = 1;
        carregarDados();
    }

    /**
     * Cria um novo ingrediente
     */
    public Ingrediente criarIngrediente(Ingrediente ingrediente) throws IOException {
        ingrediente.setId(proximoId++);
        ingredientes.add(ingrediente);
        salvarDados();
        return ingrediente;
    }

    /**
     * Busca um ingrediente por ID
     */
    public Ingrediente buscarPorId(int id) {
        return ingredientes.stream()
                          .filter(i -> i.getId() == id)
                          .findFirst()
                          .orElse(null);
    }

    /**
     * Busca ingredientes por nome (busca parcial)
     */
    public List<Ingrediente> buscarPorNome(String nome) {
        return ingredientes.stream()
                          .filter(i -> i.getNome().toLowerCase().contains(nome.toLowerCase()))
                          .toList();
    }

    /**
     * Lista todos os ingredientes
     */
    public List<Ingrediente> listarTodos() {
        return new ArrayList<>(ingredientes);
    }

    /**
     * Lista ingredientes disponíveis (com estoque > 0)
     */
    public List<Ingrediente> listarDisponiveis() {
        return ingredientes.stream()
                          .filter(Ingrediente::temEstoque)
                          .toList();
    }

    /**
     * Lista ingredientes em falta (estoque = 0)
     */
    public List<Ingrediente> listarEmFalta() {
        return ingredientes.stream()
                          .filter(i -> !i.temEstoque())
                          .toList();
    }

    /**
     * Atualiza um ingrediente existente
     */
    public Ingrediente atualizarIngrediente(Ingrediente ingrediente) throws IOException {
        Optional<Ingrediente> ingredienteExistente = ingredientes.stream()
                                                                .filter(i -> i.getId() == ingrediente.getId())
                                                                .findFirst();
        
        if (ingredienteExistente.isEmpty()) {
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }

        ingredientes.removeIf(i -> i.getId() == ingrediente.getId());
        ingredientes.add(ingrediente);
        salvarDados();
        return ingrediente;
    }

    /**
     * Remove um ingrediente
     */
    public void removerIngrediente(int id) throws IOException {
        boolean removido = ingredientes.removeIf(i -> i.getId() == id);
        
        if (!removido) {
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }
        
        salvarDados();
    }

    /**
     * Adiciona estoque de um ingrediente
     */
    public void adicionarEstoque(int ingredienteId, int quantidade) throws IOException {
        Ingrediente ingrediente = buscarPorId(ingredienteId);
        if (ingrediente == null) {
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }

        ingrediente.aumentarEstoque(quantidade);
        salvarDados();
    }

    /**
     * Reduz estoque de um ingrediente
     */
    public void reduzirEstoque(int ingredienteId, int quantidade) throws IOException, EstoqueInsuficienteException {
        Ingrediente ingrediente = buscarPorId(ingredienteId);
        if (ingrediente == null) {
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }

        if (ingrediente.getQuantidadeEmEstoque() < quantidade) {
            throw new EstoqueInsuficienteException(
                ingrediente.getNome(), 
                ingrediente.getQuantidadeEmEstoque(), 
                quantidade
            );
        }

        ingrediente.reduzirEstoque(quantidade);
        salvarDados();
    }

    /**
     * Verifica se há estoque suficiente para uma lista de ingredientes
     */
    public void verificarEstoqueDisponivel(List<Ingrediente> ingredientesNecessarios) throws EstoqueInsuficienteException {
        for (Ingrediente ingrediente : ingredientesNecessarios) {
            Ingrediente ingredienteEstoque = buscarPorId(ingrediente.getId());
            if (ingredienteEstoque == null || !ingredienteEstoque.temEstoque()) {
                throw new EstoqueInsuficienteException(
                    ingrediente.getNome(), 
                    ingredienteEstoque != null ? ingredienteEstoque.getQuantidadeEmEstoque() : 0, 
                    1
                );
            }
        }
    }

    /**
     * Consome ingredientes do estoque (para preparar uma pizza)
     */
    public void consumirIngredientes(List<Ingrediente> ingredientes) throws IOException, EstoqueInsuficienteException {
        // Primeiro verifica se todos os ingredientes estão disponíveis
        verificarEstoqueDisponivel(ingredientes);
        
        // Se passou na verificação, consome todos
        for (Ingrediente ingrediente : ingredientes) {
            reduzirEstoque(ingrediente.getId(), 1);
        }
    }

    /**
     * Verifica se um nome de ingrediente já está cadastrado
     */
    public boolean nomeJaCadastrado(String nome) {
        return ingredientes.stream()
                          .anyMatch(i -> i.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Verifica se um nome já está cadastrado por outro ingrediente
     */
    public boolean nomeJaCadastradoPorOutro(String nome, int ingredienteId) {
        return ingredientes.stream()
                          .anyMatch(i -> i.getNome().equalsIgnoreCase(nome) && i.getId() != ingredienteId);
    }

    /**
     * Obtém o total de ingredientes cadastrados
     */
    public int getTotalIngredientes() {
        return ingredientes.size();
    }

    /**
     * Carrega os dados do arquivo JSON
     */
    private void carregarDados() {
        try {
            Type listType = new TypeToken<List<Ingrediente>>(){}.getType();
            ingredientes = JsonPersistence.loadFromFile(INGREDIENTES_FILE, listType);
            
            proximoId = ingredientes.stream()
                                   .mapToInt(Ingrediente::getId)
                                   .max()
                                   .orElse(0) + 1;
        } catch (IOException e) {
            System.err.println("Erro ao carregar ingredientes: " + e.getMessage());
            ingredientes = new ArrayList<>();
            proximoId = 1;
        }
    }

    /**
     * Salva os dados no arquivo JSON
     */
    private void salvarDados() throws IOException {
        JsonPersistence.saveToFile(ingredientes, INGREDIENTES_FILE);
    }
}
