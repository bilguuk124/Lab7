package server;

import Lab5.common.interactions.Request;
import Lab5.common.utility.Outputer;
import server.utility.CommandManager;
import server.utility.RequestHandler;
import server.utility.ServerConnectionHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


/**
 * Runs the server.
 */
public class Server {
    private int port;
    private DatagramChannel datagramChannel;
    private SocketAddress address;
    private SocketAddress target;
    private Selector selector;
    private Scanner scanner;
    private ExecutorService forkJoinPool = new ForkJoinPool(1);
    private ServerConnectionHandler serverConnectionHandler;
    private CommandManager commandManager;
    private Request request;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(16384);


    private RequestHandler requestHandler;


    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            do {
                App.logger.info("Запуск сервера...");
                startServer();
                App.logger.info("Сервер успешно запущен.");
                try{
                    request = null;
                    if (!forkJoinPool.submit(() -> {
                        request = receive();
                        App.logger.info("Получено команда '" + request.getCommandName() + "'");
                        return true;
                    }).get()) break;
                    forkJoinPool.execute( new ServerConnectionHandler(this,datagramChannel, request,commandManager,target)); ;
                    forkJoinPool.awaitTermination(500,TimeUnit.MILLISECONDS);
                    stopServer();
                }  catch (ExecutionException | InterruptedException e) {
                    App.logger.error("При чтении запроса произошла ошибка при чтении запроса!");
                    forkJoinPool.shutdown();
                }
            } while (!forkJoinPool.isShutdown());
        } catch (IOException e) {
            Outputer.println("Произошла ошибка при попытке завершить соединение с клиентом!");
            App.logger.error("Произошла ошибка при попытке завершить соединение с клиентом!");
        }
    }

    private Request deserialize() throws IOException, ClassNotFoundException{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return request;
    }


    private Request receive() throws IOException, ClassNotFoundException {
        DatagramChannel channel = null;
        while(channel == null){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for(SelectionKey key : selectionKeys){
                if(key.isReadable()){
                    channel = (DatagramChannel) key.channel();
                    target = (InetSocketAddress)((DatagramChannel) key.channel()).receive(byteBuffer);
                    byteBuffer.flip();
                    channel.register(selector, SelectionKey.OP_WRITE);
                    break;
                }
            }
        } return deserialize();
    }



    private void startServer() {
        try {
            datagramChannel = DatagramChannel.open();
            address = new InetSocketAddress(InetAddress.getLocalHost(), port);
            datagramChannel.bind(address);
            datagramChannel.configureBlocking(false);
            selector = Selector.open();
            datagramChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e){
            App.logger.error("Произошла ошибка при попытке запуска сервера.");
        }

    }

    private void stopServer() throws IOException {
        datagramChannel.disconnect();
        datagramChannel.close();
    }

}