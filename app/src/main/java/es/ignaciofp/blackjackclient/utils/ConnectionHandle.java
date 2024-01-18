package es.ignaciofp.blackjackclient.utils;

public class ConnectionHandle {

    private static ConnectionHandle instance;

    private ConnectionHandle() {
    }

    public static ConnectionHandle getInstance() {
        if(instance == null) instance = new ConnectionHandle();
        return instance;
    }

    private boolean sendData(String data) {
        return false;
    }

    private String desencryptData(String data) {
        String output = "";
        return output;
    }

    private String encryptData(String data) {
        String output = "";
        return output;
    }


}
