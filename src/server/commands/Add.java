package server.commands;


import client.Validator;
import server.lib.CollectionManager;
import server.models.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

public class Add extends AbstractCommand implements Serializable {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager, Validator validator) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> addCommand = new ArrayList<>();
        if (collectionManager.getTickets().isEmpty()){
            collectionManager.getTickets().add(ticket);
            addCommand.add("the new item added to the empty collection");
            return addCommand;
        }
        collectionManager.addItem(ticket);
        addCommand.add("the new item has been successfully added to the collection\n");
        return addCommand;
    }

    @Override
    public String getDescription() {
        return "add new element to collection\n";
    }
}
