package es.ignaciofp.blackjackclient.models;

public class Card {

    private String id;
    private int cardImageDrawableId;

    public Card(String id) {
        this.id = id;
        // TODO: Link id with image
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCardImageDrawableId() {
        return cardImageDrawableId;
    }

    public void setCardImageDrawableId(int cardImageDrawableId) {
        this.cardImageDrawableId = cardImageDrawableId;
    }
}
