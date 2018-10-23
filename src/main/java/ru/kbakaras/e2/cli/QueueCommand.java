package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "queue",
        header = "Отобразить состояние очередей"
)
public class QueueCommand implements Callable<Void> {
    private ObjectMapper mapper = new ObjectMapper();

    @Option(names = { "-s", "--server" })
    private String server;

    @Override
    public Void call() throws Exception {
        if (server == null) {
            String env = System.getenv("e2.server");
            server = env != null ? env : "localhost";
        }

        String serverAddress = "http://" + server + ":10100/manage/";
        ServerConnector connector = new ServerConnector(serverAddress);

        String json = mapper.writeValueAsString("test");

        Map<String, Object> result = connector.sendPost("Queue/stats", json, null);
        JsonNode tree = mapper.readTree((String) result.get("body"));

        ConsoleTable table = new ConsoleTable();
        table.setHeaders(FIELDS);

        tree.fieldNames().forEachRemaining(queueName -> {
            table.addValue(queueName);
            JsonNode queueJson = tree.get(queueName);
            for (String field: FIELDS_Values) {
                table.addValue(queueJson.get(field).asText());
            }
            table.addValue(queueJson.get("stopped").asBoolean() ? "***" : null);
        });

        System.out.print(table.toString());

        return null;
    }

    private static final String[] FIELDS = new String[] {
            "queue", "count", "stuck", "delivered", "processed", "stopped"
    };
    private static final String[] FIELDS_Values =
            Arrays.copyOfRange(FIELDS, 1, FIELDS.length - 1);
}