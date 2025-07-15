package com.pizzaria.util;

import com.google.gson.*;
import com.pizzaria.model.ItemCardapio;
import com.pizzaria.model.Pizza;
import com.pizzaria.model.Bebida;

import java.lang.reflect.Type;

/**
 * Adapter para serialização/deserialização de ItemCardapio
 * Resolve o problema de classe abstrata no Gson
 */
public class ItemCardapioAdapter implements JsonSerializer<ItemCardapio>, JsonDeserializer<ItemCardapio> {

    @Override
    public JsonElement serialize(ItemCardapio src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src instanceof Pizza) {
            result.addProperty("type", "Pizza");
            result.add("data", context.serialize(src, Pizza.class));
        } else if (src instanceof Bebida) {
            result.addProperty("type", "Bebida");
            result.add("data", context.serialize(src, Bebida.class));
        }
        
        return result;
    }

    @Override
    public ItemCardapio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Tenta ler o tipo, se não existir, tenta detectar pela estrutura
        JsonElement typeElement = jsonObject.get("type");
        String type = null;
        
        if (typeElement != null) {
            type = typeElement.getAsString();
            JsonElement data = jsonObject.get("data");
            
            switch (type) {
                case "Pizza":
                    return context.deserialize(data, Pizza.class);
                case "Bebida":
                    return context.deserialize(data, Bebida.class);
                default:
                    throw new JsonParseException("Tipo de ItemCardapio desconhecido: " + type);
            }
        } else {
            // Fallback: detecta o tipo pela estrutura do JSON
            if (jsonObject.has("precoBase") && jsonObject.has("tamanho") && jsonObject.has("ingredientes")) {
                return context.deserialize(json, Pizza.class);
            } else if (jsonObject.has("precoFixo") && jsonObject.has("volumeEmML")) {
                return context.deserialize(json, Bebida.class);
            } else {
                throw new JsonParseException("Não foi possível determinar o tipo de ItemCardapio");
            }
        }
    }
}
