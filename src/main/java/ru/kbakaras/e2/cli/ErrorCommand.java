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
        name = "error",
        header = "Вывести ошибку по сообщению"
)
public class ErrorCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    @Option(names = { "-t", "--stuck" })
    private boolean stuck = false;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.put("stuck", stuck);

        Map<String, Object> result = parent.createConnector().sendPost(
                "Queue/error",
                mapper.writeValueAsString(request),
                null);

        JsonNode tree = mapper.readTree((String) result.get("body"));

        System.out.println(tree.get("error").asText());

        return null;
    }
}