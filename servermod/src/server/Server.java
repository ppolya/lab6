package server;


import mainlib.Reader;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    DatagramChannel channel;
    static Selector selector;
    public static boolean running = false;
    private static final int PORT = 9000;
    private static final Logger log = Logger.getLogger(Server.class.getName());


    public void startServer() {
        Reader.PrintMsg("server started");
        if (running) {
            log.log(Level.SEVERE, "the server has already been started!");
        }
        try {
            running = true;
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            DatagramSocket server = channel.socket();
            selector = Selector.open();
            SocketAddress socketAddress = new InetSocketAddress(PORT);
            server.bind(socketAddress);
            Thread console = new Thread(new ServerConsole());
            console.start();
            channel.register(selector, SelectionKey.OP_READ, new DataHolder());
            log.info("the server is listening on the " + PORT + " port");
            new DataManager().manageData();
        } catch (IOException e) {
            Reader.PrintErr("channel wasn't open");
        }
    }


    public static Selector getSelector() {
        return selector;
    }

    public static void stop() {
        running = false;
    }

}
