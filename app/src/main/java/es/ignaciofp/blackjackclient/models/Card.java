package es.ignaciofp.blackjackclient.models;

import android.content.Context;
import android.content.res.Resources;

public class Card {

    private final Context CONTEXT;
    private final int cardImageDrawableId;

    public Card(Context context, String id) {
        this.CONTEXT = context;
        this.cardImageDrawableId = parseIdToDrawableId(id);
    }

    private int parseIdToDrawableId(String id) {
        Resources res = CONTEXT.getResources();
        String drawableName = parseIdToName(id);
        return res.getIdentifier(drawableName, "drawable", CONTEXT.getPackageName());
    }

    private String parseIdToName(String id) {
        if (id.equals("**")) return "back_card";
        StringBuilder value = new StringBuilder();
        char symbol = id.replaceAll("\\w+", "").charAt(0);
        switch (symbol) {
            case '♠':
                value.append("spades_");
                break;
            case '♥':
                value.append("hearts_");
                break;
            case '♦':
                value.append("diamonds_");
                break;
            case '♣':
                value.append("clubs_");
                break;
        }

        if (id.matches("[0-9]+.")) {
            value.append(id.substring(0, id.length() - 1)); // All except last character
        } else {
            switch (id.charAt(0)) {
                case 'J':
                    value.append("jack");
                    break;
                case 'Q':
                    value.append("queen");
                    break;
                case 'K':
                    value.append("king");
                    break;
                case 'A':
                    value.append("ace");
                    break;
            }
        }
        return value.toString();
    }

    public int getCardImageDrawableId() {
        return cardImageDrawableId;
    }

}
