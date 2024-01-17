package es.ignaciofp.blackjackclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.ignaciofp.blackjackclient.R;
import es.ignaciofp.blackjackclient.adapters.AdapterCard;
import es.ignaciofp.blackjackclient.models.Card;

public class GameActivity extends AppCompatActivity {

    RecyclerView crupierRecyclerView;
    RecyclerView playerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        crupierRecyclerView = findViewById(R.id.crupierRecyclerView);
        playerRecyclerView = findViewById(R.id.playerRecyclerView);

        List<Card> cardListCrupier = new ArrayList<>();
        cardListCrupier.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.ace_of_clubs)));
        cardListCrupier.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.card_back)));
        cardListCrupier.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.card_back)));

        List<Card> cardListPlayer = new ArrayList<>();
        cardListPlayer.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.ace_of_clubs)));
        cardListPlayer.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.ace_of_spades)));
        cardListPlayer.add(new Card("1", AppCompatResources.getDrawable(this, R.drawable.ace_of_hearts)));

        initRecyclerView(crupierRecyclerView, cardListCrupier);
        initRecyclerView(playerRecyclerView, cardListPlayer);
    }

    private void initRecyclerView(RecyclerView rv, List<Card> models) {
        // Overriding the layout to disable the horizontal scroll
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new AdapterCard(models));
    }
}