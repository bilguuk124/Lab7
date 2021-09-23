package server.utility;

import Lab5.common.interactions.Request;
import Lab5.common.interactions.Response;
import org.jetbrains.annotations.NotNull;
import server.App;
import server.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.*;

import static server.App.port;

public class ServerConnectionHandler implements Runnable {

    private Server server;
    private RequestHandler requestHandler;
    private Selector selector;
    private CommandManager commandManager;
    private Request request;
    private SocketAddress target;
    private ExecutorService forkJoinPool = ForkJoinPool.commonPool();
    private DatagramChannel channel;
    private ByteBuffer byteBuffer;

    public ServerConnectionHandler(Server server, DatagramChannel channel,Request request, CommandManager commandManager, SocketAddress target){
        this.server = server;
        this.channel = channel;
        this.commandManager = commandManager;
        this.request = request;
        this.target = target;
    }

    @Override
    public void run(){
        Response response = null;
        System.out.println("I have started");
        response = new RequestHandler(request,commandManager).compute();
        System.out.println("I computed");
        Response finalResponse = response;
        forkJoinPool.submit(new ResponseSenderThread(finalResponse,target,port,channel));
        forkJoinPool.shutdown();
    }



}
