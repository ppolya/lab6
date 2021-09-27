package mainlib;

import models.Ticket;

import java.io.Serializable;
import java.util.Scanner;

public class CommandNet implements Serializable {
    private boolean isNotConnectCommand;
    String[] enteredCommand;
    Ticket ticket;
    Integer id;

    public CommandNet(String[] enteredCommand){
        this.enteredCommand = enteredCommand;
        TicketFactory ticketFactory = new TicketFactory(new Scanner(System.in));
        if (enteredCommand[0].equals("addmin") | enteredCommand[0].equals("remove") | enteredCommand[0].equals("removelower")) {
            this.id = ticketFactory.readInteger("Enter an id");
        }
        if (enteredCommand[0].equals("add") | enteredCommand[0].equals("addmin") | enteredCommand[0].equals("update") | enteredCommand[0].equals("removelower")){
            this.ticket = ticketFactory.getTicketObj(); //работает
        }
        isNotConnectCommand = true;
    }

    public String[] getEnteredCommand() {
        return enteredCommand;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Integer getId() {
        return id;
    }

    public void setConnectCommand(boolean isNotConnectCommand){
        this.isNotConnectCommand = isNotConnectCommand;
    }

    public boolean isNotConnectCommand(){
        return isNotConnectCommand;
    }

}