package client;
import Lab5.common.interactions.ResponseCode;
import Lab5.common.interactions.User;
import  client.utility.UserHandler;
import Lab5.common.interactions.Response;
import Lab5.common.interactions.Request;
import Lab5.common.utility.Outputer;
import client.utility.AuthHandler;


import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private AuthHandler authHandler;
    private DatagramSocket socket;
    private Scanner scanner = new Scanner(System.in);
    private Request lastRequest;
    private boolean isCrashed = false;
    private User user;



    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler,
                  AuthHandler authHandler){
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authHandler = authHandler;
    }
    public void run(){
        while(true) {
            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(5 * 1000);
                processAuthentication();
                processRequestToServer();
                break;
            } catch (SocketTimeoutException e) {
                Outputer.printerror("Сервер временно не работает.");
                Outputer.println("Хотите попробовать ещё раз? (y/n)");
                String tryAgain = scanner.nextLine();
                if (tryAgain.equals("y")) {
                    isCrashed = true;
                    Outputer.println("Попробую ещё раз!");
                    socket.close();
                    socket.disconnect();
                    try {
                        doLastThing();
                    } catch (ClassNotFoundException | IOException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                } else if (tryAgain.equals("n")) {
                    Outputer.println("Завершаю работу клиента!");
                    socket.close();
                    socket.disconnect();
                    System.exit(0);
                }
            } catch (IOException | ClassNotFoundException exception) {
                Outputer.printerror("Произошла ошибка при работе с сервером!");
                exception.printStackTrace();
                System.exit(0);
            }
        } socket.close();
        Outputer.println("Работа клиента завершена.");
        System.exit(0);
    }

    private void doLastThing() throws IOException, ClassNotFoundException,SocketTimeoutException {
            Response response = null;
            socket = new DatagramSocket();
            socket.setSoTimeout(3 * 1000);
            Outputer.println("Попробую команду '" + lastRequest.getCommandName() + "' ");
            sendRequest(lastRequest);
            response = receive();
            Outputer.println(response.getResponseBody());
            socket.close();
            socket.disconnect();
            run();
    }
    private void processRequestToServer() throws IOException,SocketTimeoutException,ClassNotFoundException{
        Request requestToServer = null;
        Response serverResponse = null;
        do{
            requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(),user) : userHandler.handle(null,user);
            if (requestToServer.isEmpty()) continue;
            if (requestToServer.getCommandName().equals("exit")) break;
            lastRequest = requestToServer;
            sendRequest(requestToServer);
            serverResponse = receive();
            Outputer.println(serverResponse.getResponseBody());
        } while(!requestToServer.getCommandName().equals("exit"));
        Outputer.println("\n Завершение работы клиента...");
        System.exit(0);
    }

    private void processAuthentication() throws SocketTimeoutException{
        Request requestToServer = null;
        Response serverResponse = null;
        do{
            try{
                requestToServer = authHandler.handle();
                if(requestToServer.isEmpty()) continue;
                sendRequest(requestToServer);
                serverResponse = (Response) receive();
                Outputer.println(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException e){
                Outputer.printerror("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException e){
                Outputer.printerror("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
               Outputer.printerror("Соединение с сервером разорвано!");
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }

    private Response deserialize(DatagramPacket getPacket, byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Response response = (Response) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return response;
    }
    private void sendRequest(Request request) throws IOException{
        byte[] sendBuffer = serialize(request);
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer,sendBuffer.length, InetAddress.getLocalHost(),port);
        socket.send(sendPacket);
    }



    private byte[] serialize(Request request) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }
    private Response receive() throws IOException, ClassNotFoundException, SocketTimeoutException {
        byte[] getBuffer = new byte[socket.getReceiveBufferSize()];
        DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
        socket.receive(getPacket);
        return deserialize(getPacket,getBuffer);
    }
}

