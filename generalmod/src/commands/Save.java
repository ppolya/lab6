package commands;

import mainlib.CollectionManager;
import mainlib.Reader;
import models.Ticket;

import java.util.ArrayList;

public class Save extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> saveCommand = new ArrayList<>();
        collectionManager.save();
        String message = "saved\n";
        Reader.PrintMsg(message);
        saveCommand.add(message);
        collectionManager.setNeedToSort(true);
        return saveCommand;
    }

    @Override
    public String getDescription() {
        return " save collection to file\n";
    }
}
