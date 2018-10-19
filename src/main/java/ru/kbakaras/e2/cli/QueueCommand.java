package ru.kbakaras.e2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "queue")
public class QueueCommand implements Callable<Void> {
    public QueueCommand() {}

    @Override
    public Void call() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString("test");
        System.out.println("queue command!");
        ServerConnector connector = new ServerConnector();
        connector.sendPost("Queue/test", json, null);
        return null;
    }
}
