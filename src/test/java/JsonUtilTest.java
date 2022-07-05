import net.pdutta.maelstrom.echo.EchoApp;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonUtilTest {
    @Test
    public void testJsonParse() {
        String jsonTemplate = """
                {
                  "src": "",
                  "dest": "",
                  "body": {
                    "msg_id": 0,
                    "type": "init",
                    "node_id": "temp",
                    "node_ids": [
                      "temp1"
                    ]
                  }
                }""";
        final var app = new EchoApp();

        final String constructedJson = app.create(jsonTemplate, Map.ofEntries(
                entry("$.src", "c1"),
                entry("$.dest", "n1"),
                entry("$.body.msg_id", 1)
        ));
        System.out.println("constructedJson = " + constructedJson);

        Map<String, String> searchFields = Map.ofEntries(
                entry("src", "$.src"),
                entry("dest", "$.dest"),
                entry("msgid", "$.body.msg_id"),
                entry("bodytype", "$.body.xtype"), // doesn't exist
                entry("body", "$.body") // doesn't exist
        );
        var result = app.parse(constructedJson, searchFields);
        assertNotNull(result);
        System.out.println(result.get("src") + " //" + result.get("src").getClass());
        System.out.println(result.get("dest") + " //" + result.get("dest").getClass());
        System.out.println(result.get("msgid") + " //" + result.get("msgid").getClass());
        System.out.println(result.get("bodytype") + " //" + result.get("bodytype").getClass());
        System.out.println(result.get("body") + " //" + result.get("body").getClass());

        assertEquals("c1", result.get("src"));
        assertEquals("n1", result.get("dest"));
        assertEquals(1, result.get("msgid"));
        assertEquals("", result.get("bodytype"));
    }
}
