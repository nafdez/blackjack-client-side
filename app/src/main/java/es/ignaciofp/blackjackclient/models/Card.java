package es.ignaciofp.blackjackclient.models;

import android.graphics.drawable.Drawable;

public class Card {

    private String id;
    private Drawable cardImageDrawable;

    public Card(String id, Drawable cardImageDrawable) {
        this.id = id;
        this.cardImageDrawable = cardImageDrawable;
        // TODO: Link id with image
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getCardImageDrawable() {
        return cardImageDrawable;
    }

    public void setCardImageDrawable(Drawable cardImageDrawable) {
        this.cardImageDrawable = cardImageDrawable;
    }
}
