package commands;

import exceptions.EmptyIOException;
import exceptions.NoSuchIdException;
import mainlib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Remove extends AbstractCommand {
    private final CollectionManager collectionManager;


    public Remove(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> removeCommand = new ArrayList<>();
        try {
            if (argument.trim().isEmpty()) throw new EmptyIOException();
            if (collectionManager.isEqualId(id)) throw new NoSuchIdException();
        } catch (EmptyIOException e) {
            removeCommand.add("Error: you entered a null-argument");
        } catch (NoSuchIdException e) {
            removeCommand.add("Error: no element with such id in collection");
        }
        if (collectionManager.remove(id)) {
            removeCommand.add("removed\n");
        } else {
            removeCommand.add("problems with cmd execution");
        }
        collectionManager.setNeedToSort(true);
        return removeCommand;
    }

    @Override
    public String getDescription() {
        return " (id) remove an item from the collection by its id\n";
    }
}
