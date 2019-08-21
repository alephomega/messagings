package com.xxx.messaging;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HookJsonDeserializer implements JsonDeserializer<Hook> {
    private static final Map<String, Class<? extends Hook>> hookClasses = new HashMap<>();

    static {
        List<Class> classes = PackageScanner.getSubTypesOf("com.xxx.messaging", Hook.class);
        for (Class<? extends Hook> hookClass : classes) {
            String identifier = symbolOf(hookClass);
            if (identifier != null) {
                hookClasses.put(identifier, hookClass);
            }
        }
    }

    private static String symbolOf(Class<? extends Hook> clss) {
        Symbol symbol = clss.getAnnotation(Symbol.class);
        if (symbol == null) {
            return null;
        }

        return symbol.value();
    }

    private static Class<? extends Hook> findClass(String name) {
        return hookClasses.get(name);
    }

    @Override
    public Hook deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject hookJson = json.getAsJsonObject();
        String symbol = hookJson.get("type").getAsString();

        Class<? extends Hook> clss = findClass(symbol);
        if (clss == null) {
            throw new JsonParseException(String.format("No hook with symbol: %s was found", symbol));
        }

        return context.deserialize(hookJson, clss);
    }
}