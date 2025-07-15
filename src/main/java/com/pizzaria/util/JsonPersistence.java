package com.pizzaria.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pizzaria.model.ItemCardapio;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para persistência de dados em arquivos JSON
 */
public class JsonPersistence {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ItemCardapio.class, new ItemCardapioAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    private static final String DATA_DIRECTORY = "data/";

    /**
     * Salva uma lista de objetos em um arquivo JSON
     */
    public static <T> void saveToFile(List<T> data, String filename) throws IOException {
        createDataDirectoryIfNotExists();
        
        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + filename)) {
            gson.toJson(data, writer);
        }
    }

    /**
     * Carrega uma lista de objetos de um arquivo JSON
     */
    public static <T> List<T> loadFromFile(String filename, Type typeOfT) throws IOException {
        File file = new File(DATA_DIRECTORY + filename);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            List<T> result = gson.fromJson(reader, typeOfT);
            return result != null ? result : new ArrayList<>();
        }
    }

    /**
     * Salva um único objeto em um arquivo JSON
     */
    public static <T> void saveObjectToFile(T data, String filename) throws IOException {
        createDataDirectoryIfNotExists();
        
        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + filename)) {
            gson.toJson(data, writer);
        }
    }

    /**
     * Carrega um único objeto de um arquivo JSON
     */
    public static <T> T loadObjectFromFile(String filename, Class<T> classOfT) throws IOException {
        File file = new File(DATA_DIRECTORY + filename);
        
        if (!file.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, classOfT);
        }
    }

    /**
     * Cria o diretório de dados se não existir
     */
    private static void createDataDirectoryIfNotExists() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Verifica se um arquivo existe
     */
    public static boolean fileExists(String filename) {
        return new File(DATA_DIRECTORY + filename).exists();
    }

    /**
     * Deleta um arquivo
     */
    public static boolean deleteFile(String filename) {
        return new File(DATA_DIRECTORY + filename).delete();
    }
}
