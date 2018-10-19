package ru.kbakaras.e2.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "e2", subcommands = {
        QueueCommand.class
})
public class E2Command implements Callable<Void> {
    public static void main(String[] args) throws Exception {
        new QueueCommand();
        //Class.forName(QueueCommand.class.getName());
        CommandLine.call(new E2Command(), args);
    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}