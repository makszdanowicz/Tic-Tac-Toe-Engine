package com.pwr.client;

import java.io.*;
import java.net.Socket;

public class ClientCommunicator {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientCommunicator(Socket socket)
    {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    // Sending message to server
    public void sendMessage(String message)
    {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String sendWatcherMessage(String message, String roomToken) throws IOException {
        bufferedWriter.write(message + ":" + roomToken);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        return bufferedReader.readLine();
    }

    // Listening message from server
    public void listenMessage()
    {
        try {
            String messageFromSession = bufferedReader.readLine();
            System.out.println("Server: " + messageFromSession);
        } catch (IOException e) {
            System.out.println("Closing");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    // Closing connection and communication tools
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
