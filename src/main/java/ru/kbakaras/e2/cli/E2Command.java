package ru.kbakaras.e2.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.kbakaras.e2.cli.support.ServerConnector;

import java.util.concurrent.Callable;

@Command(
        name = "e2",
        mixinStandardHelpOptions = true,
        subcommands = {
                StatsCommand.class,
                ResumeCommand.class,
                ProcessCommand.class,
                ListCommand.class,
                ReadCommand.class,
                ReconvertCommand.class
})
public class E2Command implements Callable<Void> {

    @Option(names = { "-s", "--server" })
    private String server;


    public static void main(String[] args) throws Exception {
        new StatsCommand();
        //Class.forName(StatsCommand.class.getName());
        CommandLine.call(new E2Command(), args);
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    public String server() {
        if (server == null) {
            String env = System.getenv("e2.server");
            server = env != null ? env : "localhost";
        }
        return server;
    }

    public String serverAddress() {
        return "http://" + server() + ":10100/manage/";
    }

    public ServerConnector createConnector() {
        return new ServerConnector(serverAddress());
    }

    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_ERROR   = "ERROR";
}