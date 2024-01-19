package es.ignaciofp.blackjackclient.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ignaciofp.blackjackclient.callbacks.OnConnectionCompleteCallback;

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

    public void sendCommand(ActionsEnum action, Callable<Void> onCallbackAction) {
        if (socket == null || !socket.isConnected()) return;

        Thread t = new Thread(() -> {
            try {
                String response;
                DataInputStream input = null;
                input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(MessageProcessor.encodeMessage(action.getACTIONCMD()));
                response = MessageProcessor.decodeMessage(input.readUTF());
                Log.d("POKEER", String.format("%s: %s", action.getACTIONCMD(), response));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        CACHED_POOL.submit(() -> {
            while (t.isAlive()) ;
            try {
                onCallbackAction.call();
            } catch (Exception ignored) {
            }
        });
    }
}
