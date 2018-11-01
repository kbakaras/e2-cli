package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import ru.kbakaras.e2.cli.support.ConsoleTable;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "stats",
        header = "Отобразить состояние очередей"
)
public class StatsCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        Map<String, Object> result = parent.createConnector().sendPost(
                "Queue/stats",
                mapper.writeValueAsString("test"),
                null);

        JsonNode tree = mapper.readTree((String) result.get("body"));

        ConsoleTable table = new ConsoleTable();
        table.setHeaders(FIELDS);

        tree.fieldNames().forEachRemaining(queueName -> {
            table.addValue(queueName);
            JsonNode queueJson = tree.get(queueName);
            for (String field: FIELDS_Values) {
                if (field.equals("|")) {
                    table.addValue("|");
                } else {
                    table.addValue(queueJson.get(field).asText());
                }
            }
            table.addValue(queueJson.get("stopped").asBoolean() ? "***" : null);
        });

        System.out.print(table.toString());

        return null;
    }

    private static final String[] FIELDS = new String[] {
            "queue", "count", "stuck", "|", "delivered", "processed", "stopped"
    };
    private static final String[] FIELDS_Values =
            Arrays.copyOfRange(FIELDS, 1, FIELDS.length - 1);
}