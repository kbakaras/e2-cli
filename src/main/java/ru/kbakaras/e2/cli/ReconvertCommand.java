package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import picocli.CommandLine.ParentCommand;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        name = "reconvert",
        header = "Повторная конвертация указанного сообщения очереди на доставку"
)
public class ReconvertCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    @CommandLine.Option(names = { "-i", "--id" }, required = true, description = "Идентификатор сообщения (UUID)")
    private UUID id;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.put("id",    mapper.writeValueAsString(id));

        Map<String, Object> response = parent.createConnector().sendPost(
                "Queue/reconvert",
                mapper.writeValueAsString(request),
                null);

        JsonNode tree = mapper.readTree((String) response.get("body"));
        String result = tree.get("result").textValue();

        if (E2Command.RESULT_SUCCESS.equals(result)) {
            System.out.println(tree.get("newMessage"));

        } else if (E2Command.RESULT_ERROR.equals(result)) {
            System.err.println("Reconversion ERROR:");
            System.err.println(tree.get("error").textValue());
        }

        return null;
    }
}