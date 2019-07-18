package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.File;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        name = "config",
        header = "Загрузка jar-файла конфигурации на сервер"
)
public class ConfigCommand implements Callable<Void> {

    @ParentCommand
    private E2Command parent;

    @Option(names = { "-f", "--file"}, required = true, description = "Jar-файл конфигурации")
    private File file;

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Void call() throws Exception {

        if (!file.exists()) {
            System.err.println(MessageFormat.format("File {0} not exist!", file.getName()));
            return null;
        }

        ObjectNode request = mapper.createObjectNode();
        request.put("fileName", file.getName());
        request.put("data", IOUtils.toByteArray(file.toURI()));

        Map<String, Object> result = parent.createConnector().sendPost(
                "Queue/config",
                mapper.writeValueAsString(request),
                null);

        //JsonNode tree = mapper.readTree((String) result.get("body"));

        return null;
    }
}
