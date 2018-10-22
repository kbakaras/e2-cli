package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;

import java.util.Arrays;
import java.util.concurrent.Callable;

@Command(name = "queue")
public class QueueCommand implements Callable<Void> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        String json = mapper.writeValueAsString("test");
        var connector = new ServerConnector();

        var result = connector.sendPost("Queue/stats", json, null);
        var tree = mapper.readTree((String) result.get("body"));

        var table = new ConsoleTable();
        table.setHeaders(FIELDS);

        tree.fieldNames().forEachRemaining(queueName -> {
            table.addValue(queueName);
            var queueJson = tree.get(queueName);
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