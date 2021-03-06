import commands.Commandable;
import exceptions.NoSuchCommandException;
import mainlib.*;
import mainlib.Reader;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
    InetAddress address;
    SocketAddress serverAdr = new InetSocketAddress("localhost", 9000);
    DatagramSocket socket;
    CommandNet commandNetNext = null;
    Scanner scanner;
    boolean isEstablishedConnectionWithServer = false;
    boolean isStarted = false;
    boolean isCommand = false;

    public Client() {
        System.out.println("client started");
        scanner = new Scanner(System.in);
        CommanderHolder commanderHolder = new CommanderHolder();
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.out.println("unknown host");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                if (!isStarted) {
                    startClient();
                    continue;
                }
                String line = scanner.nextLine();

                if (line.equals("exit")) {
                    System.exit(0);
                    break;
                }
                String[] message = (line.trim() + " ").split(" ", 2);
                checkCommand(message);
                CommandNet cmd = new CommandNet(message);
                send(cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchCommandException e) {
            System.out.println("Error: you entered incorrect command");
            Reader.PrintMsg("Please, enter 'help' to get list about available commands!");
            run();
        }
    }


    public void startClient() {
        if (isStarted) {
            mainlib.Reader.PrintMsg("the client has already connected to server");
        } else {
            connectServer();
            createThreadRespondent();
            isStarted = true;
        }
    }


    public void connectServer() {
        try {
            mainlib.Reader.PrintMsg("client connected to socket " + socket.toString());
            socket.connect(serverAdr);
            String[] connect = {"connect", " "};
            CommandNet commandNet = new CommandNet(connect);
            System.out.println("the client starts sending 'connect' command to the server");
            send(commandNet);
        } catch (SocketException e) {
            mainlib.Reader.PrintErr("socket connection");
        } catch (IOException e) {
            mainlib.Reader.PrintErr("sending connect command");
        }
    }

    public void send(CommandNet command) throws IOException {
        if (!command.getEnteredCommand()[0].equals("connect")) {
            commandNetNext = command;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(command);
            byte[] sendMessage = out.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, 9000);
            socket.send(packet);
        }
        System.out.println("client send command to server");
    }


    public void createThreadRespondent() {
        Thread respondent = new Thread(() -> {
            while (true) {
                try {
                    byte[] receivedMessage = new byte[65536];
                    DatagramPacket receivedPacket = new DatagramPacket(receivedMessage, receivedMessage.length);
                    socket.receive(receivedPacket);
                    byte[] received = receivedPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(received);
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Answer answer = (Answer) ois.readObject();
                    if (answer.getAnswer().get(0).equals("connected")) {
                        isEstablishedConnectionWithServer = true;
                        Reader.PrintMsg("the program was completed at the request of the user");
                        if (commandNetNext != null) {
                            System.out.println(" i try to send command [ " + commandNetNext.getEnteredCommand()[0] + " ] again!");
                            send(commandNetNext);
                        }
                    } else {
                        commandNetNext = null;
                    }
                    answer.printAnswer();
                } catch (PortUnreachableException e) {
                    try {
                        Reader.PrintMsg("failed to connect to the server :( try after 3 seconds");
                        Thread.sleep(1000);
                        System.out.println(".");
                        Thread.sleep(1000);
                        System.out.println("..");
                        Thread.sleep(1000);
                        System.out.println("...");
                        connectServer();
                    } catch (InterruptedException interruptedException) {
                        //...
                    }
                } catch (ClassNotFoundException e) {
                    Reader.PrintErr("serialization");
                    break;
                } catch (IOException e) {
                    Reader.PrintMsg("receiving/sending data");
                }
                System.out.println("LISTENING: ");
            }
        });
        respondent.start();
    }

    public void checkCommand(String[] cmd) throws NoSuchCommandException {
        if (!cmd[0].trim().equals(" ")) {
            for (Commandable eachCommand : CommanderHolder.getCmdList()) {
                if (cmd[0].trim().equals(eachCommand.getName())) {
                    mainlib.Reader.PrintMsg("you entered a right command");
                    isCommand = true;
                }
            }
        }
        if (!isCommand) {
            throw new NoSuchCommandException();
        }
    }

}
