package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import ru.kbakaras.e2.cli.support.E2Queue;

import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        name = "revert",
        header = "Отмена признака обработки последнего сообщения в очереди"
)
public class RevertCommand implements Callable<Void> {
    @ParentCommand
    private E2Command parent;

    @Option(names = { "-q", "--queue"}, required = true, description = "Запрашиваемая очередь")
    private E2Queue queue;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void call() throws Exception {
        ObjectNode request = mapper.createObjectNode();
        request.put("queue", queue.name());

        Map<String, Object> response = parent.createConnector().sendPost(
                "Queue/revert",
                mapper.writeValueAsString(request),
                null);

        JsonNode tree = mapper.readTree((String) response.get("body"));
        String result = tree.get("result").textValue();

        if (E2Command.RESULT_SUCCESS.equals(result)) {
            System.out.println(tree.get("messageId").textValue());

        } else if (E2Command.RESULT_ERROR.equals(result)) {
            System.err.println("Revert ERROR:");
            System.err.println(tree.get("error").textValue());
        }

        return null;
    }
}
