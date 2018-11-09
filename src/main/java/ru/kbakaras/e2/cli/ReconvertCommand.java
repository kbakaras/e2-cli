package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import picocli.CommandLine.ParentCommand;

import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "reconvert",
        header = "Повторная конвертация указанного сообщения очереди на доставку"
)
public class ReconvertCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.put("queue", "delivery"); //TODO Create parameter

        Map<String, Object> result = parent.createConnector().sendPost(
                "Queue/resume",
                mapper.writeValueAsString(request),
                null);

        JsonNode tree = mapper.readTree((String) result.get("body"));

        return null;
    }
}
