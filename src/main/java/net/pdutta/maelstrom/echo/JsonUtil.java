package net.pdutta.maelstrom.echo;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public JsonUtil(String json) {
        this.document = Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    /**
     * Find items from the specified JSON using the JSONPath queries provided.
     *
     * @param items a Map<String, String> structured as field => JSONPathQuery
     * @return a Map<String, Object> with values found from the JSON.
     * The structure is field => item found from JSON.
     * If no value is found, the empty string is returned.
     */
    public Map<String, Object> find(Map<String, String> items) {
        Map<String, Object> result = new HashMap<>();
        for (String field : items.keySet()) {
            String query = items.get(field);
            try {
                result.put(field, JsonPath.read(document, query));
            } catch (PathNotFoundException ex) {
                System.out.println("Error reading JSON: " + ex.getMessage());
                result.put(field, "");
            }
        }
        return result;
    }

    /**
     * creates JSON using a template and specified variables
     *
     * @param jsonTemplate a String containing placeholder JSON
     * @param items        a Map<String, Object> with JSONPathQuery => value
     *                     to insert into the JSON
     * @return a JSON string with specified variables inserted
     */
    public static String create(String jsonTemplate, Map<String, Object> items) {
        DocumentContext ctx = JsonPath.parse(jsonTemplate);
        for (String query : items.keySet()) {
            ctx = ctx.set(query, items.get(query));
        }
        return ctx.jsonString();
    }

    private final Object document;
}
