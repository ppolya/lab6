package server.commands;

import client.Validator;
import server.lib.CollectionManager;
import server.models.Ticket;

import java.util.ArrayList;

public class RemoveLower extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final Validator validator;

    public RemoveLower(CollectionManager collectionManager, Validator validator) {
        this.collectionManager = collectionManager;
        this.validator = validator;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> removeLowerCommand = new ArrayList<>();
        if (collectionManager.isEqualId(id)) {
            collectionManager.removeLower(ticket);
        }
        removeLowerCommand.add("removed\n");
        return removeLowerCommand;
    }


    @Override
    public String getDescription() {
        return " remove all elements from the collection that are less than the given one\n";
    }
}
