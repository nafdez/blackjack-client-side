package es.ignaciofp.blackjackclient.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ignaciofp.blackjackclient.callbacks.OnConnectionCompleteCallback;
import es.ignaciofp.blackjackclient.callbacks.OnGameResponseCallback;

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
            } catch (NoRouteToHostException e) {
                onCompleteCallback.showSimpleAlert("Connection failed", "No route to host " + host);
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

    public synchronized void sendCommand(ActionsEnum action, OnGameResponseCallback onCallbackAction) {
        if (socket == null || !socket.isConnected()) return;

        final String[] response = new String[1];

        Thread t = new Thread(() -> {
            try {
//                String response;
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(MessageProcessor.encodeMessage(action.getACTIONCMD()));
                response[0] = MessageProcessor.decodeMessage(input.readUTF());
                Log.d("POKEER", String.format("%s: %s", action.getACTIONCMD(), response[0]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        CACHED_POOL.submit(() -> {
            while (t.isAlive()) ;
            onCallbackAction.setResponse(response[0]);
            onCallbackAction.call();
        });
    }
}
