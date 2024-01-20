package es.ignaciofp.blackjackclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

import es.ignaciofp.blackjackclient.models.Card;

public class CardStackLayout extends LinearLayout {

    private LinkedList<Card> models;

    public CardStackLayout(Context context) {
        this(context, null);
    }

    public CardStackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardStackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CardStackLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void addCards(List<Card> cards) {
        ImageView img;
        float rotationAngle;
        int totalCards = cards.size();

        for (int i = 0; i < totalCards; i++) {
            // Set rotation based on the position in the deck
            rotationAngle = (i - (totalCards - 1) / 2.0f) * 10; // Adjust the angle as needed
            img = new ImageView(getContext());
            img.setImageResource(cards.get(i).getCardImageDrawableId());
            img.setRotation(rotationAngle);
        }
    }
}
