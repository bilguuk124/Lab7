package server.utility;

import Lab5.common.interactions.Response;
import server.App;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class ResponseSenderThread implements Runnable{
    private Response response;
    private SocketAddress target;
    private int port;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
    private Selector selector;
    private DatagramChannel datagramChannel;

    public ResponseSenderThread(Response response, SocketAddress target, int port, DatagramChannel datagramChannel){
        this.response = response;
        this.target = target;
        this.port = port;
        this.datagramChannel = datagramChannel;
    }
    @Override
    public void run(){
        try{
            sendResponse(response);
            App.logger.info("Успешно проработан команда");
        } catch (IOException exception){
            System.out.println("Произошла ошибка при отправке ответа!");
        }
    }
    private void sendResponse(Response response) throws IOException {
        selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_WRITE);
        makeByteBufferToResponse(response);
        DatagramChannel channel = null;
        while(channel == null){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for(SelectionKey key : selectionKeys){
                if(key.isWritable()){
                    channel = (DatagramChannel) key.channel();
                    channel.send(byteBuffer,target);
                    channel.register(selector, SelectionKey.OP_READ);
                    break;
                }
            }
        }
        byteBuffer.clear();
    }
    private void makeByteBufferToResponse(Response response) throws IOException{
        byteBuffer.put(serialize(response));
        byteBuffer.flip();
    }

    private byte[] serialize(Response response) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }
}
