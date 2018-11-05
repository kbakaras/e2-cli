package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "message",
        header = "Вывести сообщение"
)
public class MessageCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    @Option(names = { "-t", "--stuck" }, description = "Запрос застрявшего сообщения")
    private boolean stuck = false;

    @Option(names = { "--source" }, description = "Запрос сообщения, являющегося исходным для заданного")
    private boolean source = false;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.put("stuck",  stuck);
        request.put("source", source);

        Map<String, Object> result = parent.createConnector().sendPost(
                "Queue/message",
                mapper.writeValueAsString(request),
                null);

        JsonNode tree = mapper.readTree((String) result.get("body"));

        System.out.println(tree.get("message").asText());

        return null;
    }
}