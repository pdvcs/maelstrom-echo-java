package net.pdutta.maelstrom.echo;

import java.util.Map;
import java.util.Scanner;

public class EchoApp {
    public void loopOverStdin() {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String json = input.nextLine().trim();
            var parseResult = parseMessage(json);
            switch (parseResult.messageType()) {
                case "init" -> respondToInit(parseResult.map());
                case "echo" -> respondToEcho(parseResult.map());
                default -> logError("Error: Unknown message type: " + parseResult.messageType());
            }
        }
    }

    private void respondToInit(Map<String, Object> parsed) {
        String nodeId = "";
        Double msgId = -2.0;
        String src = "";
        try {
            nodeId = (String) ((Map<?, ?>) parsed.get("body")).get("node_id");
            // JSON numbers are stored as Doubles
            msgId = (Double) ((Map<?, ?>) parsed.get("body")).get("msg_id");
            src = (String) parsed.get("src");
        } catch (Exception e) {
            logError("error picking values from JSON: " + e.getMessage());
            return;
        }

        this.nodeId = nodeId;

        final var response = new EchoInitResponse();
        response.src = this.nodeId;
        response.dest = src;
        response.body.in_reply_to = msgId.intValue();
        response.body.type = "init_ok";
        response.body.msg_id = ++this.nextMessageId;

        String json = jsonUtil.createJson(response);
        logOutput(json.replaceAll("[\\r\\n]", ""));
    }

    private void respondToEcho(Map<String, Object> parsed) {
        String echoMsg = "";
        Double msgId = -2.0;
        String src = "";
        try {
            echoMsg = (String) ((Map<?, ?>) parsed.get("body")).get("echo");
            msgId = (Double) ((Map<?, ?>) parsed.get("body")).get("msg_id");
            src = (String) parsed.get("src");
        } catch (Exception e) {
            logError("error picking values from JSON: " + e.getMessage());
            return;
        }

        final var response = new EchoResponse();
        response.src = this.nodeId;
        response.dest = src;
        response.body.echo = echoMsg;
        response.body.in_reply_to = msgId.intValue();
        response.body.type = "echo_ok";
        response.body.msg_id = ++this.nextMessageId;

        String json = jsonUtil.createJson(response);
        logOutput(json.replaceAll("[\\r\\n]", ""));
    }

    private ParseResult parseMessage(String json) {
        Map<String, Object> parsed = null;
        String msgtype = null;
        try {
            parsed = jsonUtil.parseToMap(json);
            msgtype = (String) ((Map<?, ?>) parsed.get("body")).get("type");
        } catch (Exception e) {
            logError("error parsing message: " + e.getMessage());
        }
        return new ParseResult(parsed, msgtype);
    }

    void logError(String message) {
        System.err.println(message);
        System.err.flush();
    }

    void logOutput(String message) {
        System.out.println(message);
        System.out.flush();
    }

    public EchoApp() {
        this.nodeId = null;
        this.nextMessageId = 0;
        this.jsonUtil = new JsonUtil();
    }

    private String nodeId;
    private Integer nextMessageId;
    private final JsonUtil jsonUtil;
}

class EchoInitResponse {
    String src;
    String dest;

    @SuppressWarnings("InnerClassMayBeStatic")
    class Body {
        int msg_id;
        int in_reply_to;
        String type;
    }

    Body body;

    EchoInitResponse() {
        body = new Body();
    }
}

class EchoResponse {
    String src;
    String dest;

    @SuppressWarnings("InnerClassMayBeStatic")
    class Body {
        int msg_id;
        int in_reply_to;
        String type;
        String echo;
    }

    Body body;

    EchoResponse() {
        body = new Body();
    }
}

record ParseResult(Map<String, Object> map, String messageType) {
}
