package commands;

import mainlib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class GroupCountByType extends AbstractCommand {
    private final CollectionManager collectionManager;


    public GroupCountByType (CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    /**
     * execute group_count_by_type command
     * @param argument ticket type as string
     */

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> groupCommand = new ArrayList<>(collectionManager.groupCount());
        groupCommand.add("groupcountbytype executed\n");
        collectionManager.setNeedToSort(true);
        return groupCommand;
    }

    @Override
    public String getDescription() {
        return " group the elements of the collection by the value of the type field, display the number of elements in the group\n";
    }
}
