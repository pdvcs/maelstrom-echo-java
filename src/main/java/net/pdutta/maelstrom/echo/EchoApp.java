package net.pdutta.maelstrom.echo;

import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public class EchoApp {
    public void loopOverStdin() {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String json = input.nextLine().trim();
            String messageType = getMessageType(json);
            switch (messageType.toLowerCase(Locale.ROOT)) {
                case "init" -> respondToInit(json);
                case "echo" -> respondToEcho(json);
                default -> System.out.println("Error: Unknown message type: " + messageType);
            }
        }
    }

    private void respondToInit(String json) {
        var result = parse(json, Map.ofEntries(
                entry("node_id", "$.body.node_id"),
                entry("msg_id", "$.body.msg_id"),
                entry("src", "$.src")
        ));
        String nodeId = (String) result.get("node_id");
        if (nodeId.equals("")) {
            System.out.println("Error: could not fetch node_id");
            return;
        }
        this.nodeId = nodeId;

        String responseTemplate = """
                {
                    "src": "",
                    "dest": "",
                    "body": {
                        "msg_id": 0,
                        "in_reply_to": 0,
                        "type": "init_ok"
                    }
                }""";
        var response = create(responseTemplate, Map.ofEntries(
                entry("$.src", this.nodeId),
                entry("$.dest", result.get("src")),
                entry("$.body.in_reply_to", result.get("msg_id")),
                entry("$.body.msg_id", ++this.nextMessageId)
        ));
        System.out.println(response.replaceAll("[\\r\\n]", ""));
        System.out.flush();
    }

    private void respondToEcho(String json) {
        var result = parse(json, Map.ofEntries(
                entry("echo_msg", "$.body.echo"),
                entry("msg_id", "$.body.msg_id"),
                entry("src", "$.src")
        ));

        String responseTemplate = """
                {
                    "src": "",
                    "dest": "",
                    "body": {
                        "echo": "",
                        "type": "echo_ok",
                        "msg_id": 0,
                        "in_reply_to": 0,
                    }
                }""";
        var response = create(responseTemplate, Map.ofEntries(
                entry("$.src", this.nodeId),
                entry("$.dest", result.get("src")),
                entry("$.body.echo", result.get("echo_msg")),
                entry("$.body.in_reply_to", result.get("msg_id")),
                entry("$.body.msg_id", ++this.nextMessageId)
        ));
        System.out.println(response.replaceAll("[\\r\\n]", ""));
        System.out.flush();
    }

    private String getMessageType(String json) {
        var result = parse(json, Map.ofEntries(
                entry("bodytype", "$.body.type")
        ));
        return (String) result.get("bodytype");
    }

    public Map<String, Object> parse(String json, Map<String, String> searchFields) {
        JsonUtil searcher = new JsonUtil(json);
        return searcher.find(searchFields);
    }

    public String create(String jsonTemplate, Map<String, Object> items) {
        return JsonUtil.create(jsonTemplate, items);
    }

    public EchoApp() {
        this.nodeId = null;
        this.nextMessageId = 0;
    }

    private String nodeId;
    private Integer nextMessageId;
}
