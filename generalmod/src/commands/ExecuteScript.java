package commands;

import exceptions.LackOfAccessException;
import exceptions.NoSuchCommandException;
import models.Ticket;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ExecuteScript extends AbstractCommand {
    Executor executor;
    static ArrayList<String> executeScriptCommand;

    public ExecuteScript(){
        executor = new Executor();
    }

    @Override
    public String getDescription() {
        return "executes a script of the available commands";
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        String path = argument.trim();
        System.out.println("argument that you really entered as a path: " + argument);
        executeScriptCommand = new ArrayList<>();
        try {
            if (!Files.isExecutable(Paths.get(path))){
                throw new LackOfAccessException();
            }
            if (!argument.trim().equals("")) {
                executor.executeScript(argument.trim());
            } else {
                executeScriptCommand.add("please, enter this command with file name");
            }
            executor.getScript().clear();
        } catch (LackOfAccessException e) {
            executeScriptCommand.add("the file does not have execute permission");
        } catch (NoSuchCommandException e) {
            executeScriptCommand.add("Error: no such command!");
        }
        return executeScriptCommand;
    }

    public static ArrayList<String> getExecuteScriptCommand() {
        return executeScriptCommand;
    }
}
