package es.ignaciofp.blackjackclient.callbacks;

import java.util.concurrent.Callable;

public class OnGameResponseCallback implements Callable<Void> {

    private String response = "";

    public OnGameResponseCallback() {
    }

    @Override
    public Void call() {
        return null;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

}
