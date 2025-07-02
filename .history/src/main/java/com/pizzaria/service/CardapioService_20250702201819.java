package com.pizzaria.service;

import com.pizzaria.model.*;
import com.pizzaria.util.JsonPersistence;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo CRUD de itens do cardápio (Pizzas e Bebidas)
 */
public class CardapioService {
    private static final String PIZZAS_FILE = "pizzas.json";
    private static final String BEBIDAS_FILE = "bebidas.json";
    
    private List<Pizza> pizzas;
    private List<Bebida> bebidas;
    private int proximoIdPizza;
    private int proximoIdBebida;

    public CardapioService() {
        this.pizzas = new ArrayList<>();
        this.bebidas = new ArrayList<>();
        this.proximoIdPizza = 1;
        this.proximoIdBebida = 1000; // Começar com ID diferente para evitar conflitos
        carregarDados();
    }

    // ========== MÉTODOS PARA PIZZAS ==========

    /**
     * Cria uma nova pizza
     */
    public Pizza criarPizza(Pizza pizza) throws IOException {
        pizza.setId(proximoIdPizza++);
        pizzas.add(pizza);
        salvarPizzas();
        return pizza;
    }

    /**
     * Busca uma pizza por ID
     */
    public Pizza buscarPizzaPorId(int id) {
        return pizzas.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

    /**
     * Busca pizzas por nome
     */
    public List<Pizza> buscarPizzasPorNome(String nome) {
        return pizzas.stream()
                    .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
    }

    /**
     * Lista todas as pizzas
     */
    public List<Pizza> listarTodasPizzas() {
        return new ArrayList<>(pizzas);
    }

    /**
     * Atualiza uma pizza existente
     */
    public Pizza atualizarPizza(Pizza pizza) throws IOException {
        Optional<Pizza> pizzaExistente = pizzas.stream()
                                              .filter(p -> p.getId() == pizza.getId())
                                              .findFirst();
        
        if (pizzaExistente.isEmpty()) {
            throw new IllegalArgumentException("Pizza não encontrada");
        }

        pizzas.removeIf(p -> p.getId() == pizza.getId());
        pizzas.add(pizza);
        salvarPizzas();
        return pizza;
    }

    /**
     * Remove uma pizza
     */
    public void removerPizza(int id) throws IOException {
        boolean removido = pizzas.removeIf(p -> p.getId() == id);
        
        if (!removido) {
            throw new IllegalArgumentException("Pizza não encontrada");
        }
        
        salvarPizzas();
    }

    /**
     * Cria uma pizza personalizada
     */
    public Pizza criarPizzaPersonalizada(String nome, Tamanho tamanho, double precoBase, List<Ingrediente> ingredientes) throws IOException {
        Pizza pizza = new Pizza(proximoIdPizza++, nome, "Pizza personalizada", precoBase, tamanho, ingredientes);
        pizzas.add(pizza);
        salvarPizzas();
        return pizza;
    }

    // ========== MÉTODOS PARA BEBIDAS ==========

    /**
     * Cria uma nova bebida
     */
    public Bebida criarBebida(Bebida bebida) throws IOException {
        bebida.setId(proximoIdBebida++);
        bebidas.add(bebida);
        salvarBebidas();
        return bebida;
    }

    /**
     * Busca uma bebida por ID
     */
    public Bebida buscarBebidaPorId(int id) {
        return bebidas.stream()
                     .filter(b -> b.getId() == id)
                     .findFirst()
                     .orElse(null);
    }

    /**
     * Busca bebidas por nome
     */
    public List<Bebida> buscarBebidasPorNome(String nome) {
        return bebidas.stream()
                     .filter(b -> b.getNome().toLowerCase().contains(nome.toLowerCase()))
                     .toList();
    }

    /**
     * Lista todas as bebidas
     */
    public List<Bebida> listarTodasBebidas() {
        return new ArrayList<>(bebidas);
    }

    /**
     * Atualiza uma bebida existente
     */
    public Bebida atualizarBebida(Bebida bebida) throws IOException {
        Optional<Bebida> bebidaExistente = bebidas.stream()
                                                 .filter(b -> b.getId() == bebida.getId())
                                                 .findFirst();
        
        if (bebidaExistente.isEmpty()) {
            throw new IllegalArgumentException("Bebida não encontrada");
        }

        bebidas.removeIf(b -> b.getId() == bebida.getId());
        bebidas.add(bebida);
        salvarBebidas();
        return bebida;
    }

    /**
     * Remove uma bebida
     */
    public void removerBebida(int id) throws IOException {
        boolean removido = bebidas.removeIf(b -> b.getId() == id);
        
        if (!removido) {
            throw new IllegalArgumentException("Bebida não encontrada");
        }
        
        salvarBebidas();
    }

    // ========== MÉTODOS GERAIS ==========

    /**
     * Busca um item do cardápio por ID (pizza ou bebida)
     */
    public ItemCardapio buscarItemPorId(int id) {
        ItemCardapio item = buscarPizzaPorId(id);
        if (item == null) {
            item = buscarBebidaPorId(id);
        }
        return item;
    }

    /**
     * Lista todos os itens do cardápio
     */
    public List<ItemCardapio> listarTodosItens() {
        List<ItemCardapio> todosItens = new ArrayList<>();
        todosItens.addAll(pizzas);
        todosItens.addAll(bebidas);
        return todosItens;
    }

    /**
     * Busca itens por nome (pizzas e bebidas)
     */
    public List<ItemCardapio> buscarItensPorNome(String nome) {
        List<ItemCardapio> itensEncontrados = new ArrayList<>();
        itensEncontrados.addAll(buscarPizzasPorNome(nome));
        itensEncontrados.addAll(buscarBebidasPorNome(nome));
        return itensEncontrados;
    }

    /**
     * Verifica se um nome de pizza já está cadastrado
     */
    public boolean nomePizzaJaCadastrado(String nome) {
        return pizzas.stream()
                    .anyMatch(p -> p.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Verifica se um nome de bebida já está cadastrado
     */
    public boolean nomeBebidaJaCadastrado(String nome) {
        return bebidas.stream()
                     .anyMatch(b -> b.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Obtém estatísticas do cardápio
     */
    public String getEstatisticas() {
        return String.format("Cardápio: %d pizzas, %d bebidas, %d itens total", 
                           pizzas.size(), bebidas.size(), pizzas.size() + bebidas.size());
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Carrega os dados dos arquivos JSON
     */
    private void carregarDados() {
        carregarPizzas();
        carregarBebidas();
    }

    private void carregarPizzas() {
        try {
            Type listType = new TypeToken<List<Pizza>>(){}.getType();
            pizzas = JsonPersistence.loadFromFile(PIZZAS_FILE, listType);
            
            proximoIdPizza = pizzas.stream()
                                  .mapToInt(Pizza::getId)
                                  .max()
                                  .orElse(0) + 1;
        } catch (IOException e) {
            System.err.println("Erro ao carregar pizzas: " + e.getMessage());
            pizzas = new ArrayList<>();
            proximoIdPizza = 1;
        }
    }

    private void carregarBebidas() {
        try {
            Type listType = new TypeToken<List<Bebida>>(){}.getType();
            bebidas = JsonPersistence.loadFromFile(BEBIDAS_FILE, listType);
            
            proximoIdBebida = bebidas.stream()
                                    .mapToInt(Bebida::getId)
                                    .max()
                                    .orElse(999) + 1;
        } catch (IOException e) {
            System.err.println("Erro ao carregar bebidas: " + e.getMessage());
            bebidas = new ArrayList<>();
            proximoIdBebida = 1000;
        }
    }

    private void salvarPizzas() throws IOException {
        JsonPersistence.saveToFile(pizzas, PIZZAS_FILE);
    }

    private void salvarBebidas() throws IOException {
        JsonPersistence.saveToFile(bebidas, BEBIDAS_FILE);
    }
}
