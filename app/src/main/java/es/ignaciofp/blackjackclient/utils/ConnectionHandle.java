package es.ignaciofp.blackjackclient.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import es.ignaciofp.blackjackclient.callbacks.OnConnectionCompleteCallback;
import kotlinx.coroutines.scheduling.Task;

public class ConnectionHandle {

    private static ConnectionHandle instance;
    private final ExecutorService CACHED_POOL = Executors.newCachedThreadPool();
    private Socket socket;

    private ConnectionHandle() {
    }

    public static ConnectionHandle getInstance() {
        if (instance == null) {
            instance = new ConnectionHandle();
        }
        return instance;
    }

    public void connect(String host, int port, OnConnectionCompleteCallback onCompleteCallback) throws IOException {
        Thread t = new Thread(() -> {
            try {
                this.socket = new Socket(host, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        t.start();

        CACHED_POOL.submit(() -> {
            while (t.isAlive()) ;
            onCompleteCallback.setSocket(socket);
            onCompleteCallback.call();
        });
    }

    public String sendCommand(ActionsEnum action) throws IOException {
        if (socket == null || !socket.isConnected()) return null;
        String response;
        try (DataInputStream input = new DataInputStream(socket.getInputStream()); DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            output.writeUTF(MessageProcessor.encodeMessage(action.getACTIONCMD()));
            response = MessageProcessor.decodeMessage(input.readUTF());
            Log.d("POKEER", String.format("%s: %s", action.getACTIONCMD(), response));
        }
        return response;
    }

    private boolean testConnection() {
        ActionsEnum response = null;
        try {
            response = ActionsEnum.fromString(sendCommand(ActionsEnum.TEST));
        } catch (IOException ignored) {
        }
        return response != null && response.equals(ActionsEnum.TEST);
    }

}
