package es.ignaciofp.blackjackclient.callbacks;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import es.ignaciofp.blackjackclient.models.Card;

public class OnGameResponseCallback implements Callable<Void> {

    private final Context CONTEXT;
    private String response = "";

    OnGameResponseCallback(Context context) {
        this.CONTEXT = context;
    }

    @Override
    public Void call() {

        return null;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private void parseInputIntoInfo(String input) {
        List<Card> crupierCards = new ArrayList<>();
        List<Card> playerCards = new ArrayList<>();
        int crupierScore = 0;
        int playerScore = 0;


    }
}
