package server.commands;

import bridge.Message;
import server.lib.CommanderHolder;
import server.exceptions.NoSuchCommandException;
import server.lib.ScriptManager;
import server.models.Ticket;
import server.serverside.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static client.ConsoleManager.PrintMsg;
import static client.Reader.PrintErr;

public class Executor implements Serializable {
    Message infoToClient;
    Scanner scanner;
    List<String> script;

    {
        script = new ArrayList<>();
    }

    public Executor() {
        scanner = new Scanner(System.in);
    }

    public void executeScript(String argument) throws NoSuchCommandException {
        ScriptManager scriptManager = new ScriptManager(argument.trim());
        script.add(argument.trim());
        while (true) {
            String nextLine = scriptManager.nextLine();
            if (nextLine == null)
                break;
            String[] cmd = (nextLine.trim() + " ").split(" ", 2);
            if (cmd[0].trim().equals("executescript")) {
                if (!cmd[1].equals(" ")) {
                    if (script.contains(cmd[1].trim())) {
                        ExecuteScript.getExecuteScriptCommand().add("Error: trying to recursively call the script");
                        break;
                    } else {
                        executeScript(cmd[1].trim());
                    }
                } else {
                    ExecuteScript.getExecuteScriptCommand().add("enter file name");
                }
            } else if (!cmd[0].trim().equals("")) {
                boolean isFindCommand = false;
                for (Commandable commandable : CommanderHolder.getCmdList()) {
                    if (cmd[0].trim().equals(commandable.getName())) {
                        if (commandable.getName().trim().equals("add")) {
                            changeScanner(scriptManager.getScriptReader());
                            try {
                                commandable.execute(cmd[1],null,null );
                                isFindCommand = true;
                                break;
                            } catch (NoSuchElementException e) {
                                ExecuteScript.getExecuteScriptCommand().add("Error: reading element from file");
                            }
                            changeScanner(new Scanner(System.in));
                        } else {
                            commandable.execute(cmd[1], null ,null);
                            isFindCommand = true;
                            break;
                        }
                    }
                }
                if (!isFindCommand) {
                    throw new NoSuchCommandException();
                }
            }
        }
    }


    public Answer execute(String receivedCommand, String argument, Ticket ticket, Integer id) {
        boolean isRightCommand = false;
        Commandable commandToExecute = null;
        Answer answer = null;
        if (receivedCommand != null) {
            for (Commandable listCommand : CommanderHolder.getCmdList()) {
                if (receivedCommand.equals(listCommand.getName())) {
                    PrintMsg("it is a right command");
                    commandToExecute = listCommand;
                    isRightCommand = true;
                    break;
                }
            }
            if (isRightCommand) {
                answer = new Answer(commandToExecute.execute(argument,ticket, id));
                PrintMsg("command was successfully executed!\n");
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            String error = "it isn't a right command. Theres is no way to execute this command";
            PrintErr(error);
            list.add(error);
            list.add("You can enter 'help' to get list about available commands!");
            answer = new Answer(list);
        }
        return answer;
    }

    /*public Message execute(String cmd) {
        String[] command = (cmd.trim() + " ").split(" ", 2);
        Message infoToClient = new Message("");
          if (!command[0].trim().equals("")) {
            for (Commandable each : commandsList) {
                if (command[0].trim().equals(each.getName())) {
                    infoToClient = new Message("you entered a right command");
                    each.execute(command[1]);
                    break;
                }
            }
        }
        String finalRes = (infoToClient.toString() + "\n" + "");
        PrintMsg("command was executed");
        return infoToClient;
    }*/

    public Message getInfoToClient() {
        return infoToClient;
    }

    public List<String> getScript() {
        return script;
    }

    public void changeScanner(Scanner sc) {
        scanner = sc;
    }
}
