package net.pdutta.maelstrom.echo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    public JsonUtil() {
        this.gson = new GsonBuilder().create();
    }

    public String createJson(EchoInitResponse response) {
        Type type = new TypeToken<EchoInitResponse>() {
        }.getType();
        return gson.toJson(response, type);
    }

    public String createJson(EchoResponse response) {
        Type type = new TypeToken<EchoResponse>() {
        }.getType();
        return gson.toJson(response, type);
    }

    public Map<String, Object> parseToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private final Gson gson;
}
