package mainlib;

import models.Ticket;
import models.TicketType;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {
    List<Ticket> tickets;
    private final FileManager fileManager;
    private boolean isNeedToSort = true;
    static List<Integer> ids  = new ArrayList<>();


    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.tickets = fileManager.readData();
        setIdList();
        sortCollection();
        fileManager.checkData(tickets);
    }

    public void setIdList(){
        ids = tickets.stream().map(Ticket::getId).collect(Collectors.toList());
    }

    public static List<Integer> getIds() {
        return ids;
    }

    public String getInformation() {
        String dataSimpleName = tickets.getClass().getSimpleName();
        ZonedDateTime creationDate = ZonedDateTime.now();
        String size = String.valueOf(tickets.size());
        String res = "";
        res += "\n" + "collection type: " + dataSimpleName + "\n";
        res += "collection creation time: " + creationDate + "\n";
        res += "number of items in the collection: " + size + "\n";
        return res;
    }

    public String getStringElements() {
        return String.valueOf(tickets);
    }


    public void save() {
        fileManager.saveData(tickets);
    }


    public void update(Ticket update) {
        tickets = tickets.stream().map(ticket -> ticket.getId().equals(update.getId()) ? update : ticket).collect(Collectors.toCollection(Vector::new));
    }

    public boolean addItem(Ticket ticket) {
        return tickets.add(ticket);
    }

    public boolean addIfMin(Ticket ticket) {
        boolean isAdded = false;
        Integer min = tickets.stream().map(Ticket::getId).reduce(Integer::compareTo).orElse(-1);
        if (ticket.getId() < min) {
             isAdded = tickets.add(ticket);
        }
        return isAdded;
    }

    public boolean isEqualId(Integer ID) {
        for (Ticket t : tickets) {
            if (t.getId().equals(ID)) {
                return false;
            }
        }
        return true;
    }


    public boolean remove(Integer id) {
        return tickets.removeIf(t -> t.getId().equals(id));
    }

    public boolean removeIfLowerId(Ticket ticket) {
        return tickets.removeIf(ticket1 -> (ticket.getId() > ticket1.getId()));
    }


    public String startsWithSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        tickets.stream().filter(s -> s.getName().startsWith(substr.trim())).forEach(list::append);
        return list.toString();
    }

    public String containsSomeSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        tickets.stream().filter(s -> s.getName().contains(substr.trim())).forEach(list::append);
        return list.toString();
    }

    public void clear() {
        tickets.clear();
    }

    public void shuffle(){
        Collections.shuffle(tickets);
    }

    public ArrayList<String> groupCount() {
        ArrayList<String> out = new ArrayList<>();
        Map<TicketType, Long> ticketsByType = tickets.stream().collect(Collectors.groupingBy(Ticket::getType, Collectors.counting()));
        for (Map.Entry<TicketType, Long> item : ticketsByType.entrySet()) {
            out.add(item.getKey() + " - " + item.getValue());
        }
        return out;
    }

    public void sortCollection() {
        if (!tickets.isEmpty() && isNeedToSort) {
            tickets = tickets.stream().sorted((Comparator.comparing(o -> o.getVenue().getType()))).collect(Collectors.toCollection(Vector::new));
        }
    }

    public void setNeedToSort(boolean needToSort) {
        isNeedToSort = needToSort;
    }
}
