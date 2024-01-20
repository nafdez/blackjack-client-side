package es.ignaciofp.blackjackclient.ui.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.ignaciofp.blackjackclient.R;
import es.ignaciofp.blackjackclient.adapters.AdapterCard;
import es.ignaciofp.blackjackclient.callbacks.OnConnectionCompleteCallback;
import es.ignaciofp.blackjackclient.callbacks.OnGameResponseCallback;
import es.ignaciofp.blackjackclient.models.Card;
import es.ignaciofp.blackjackclient.utils.ActionsEnum;
import es.ignaciofp.blackjackclient.utils.ConnectionHandle;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionHandle connectionHandle;
    private RecyclerView crupierRecyclerView;
    private RecyclerView playerRecyclerView;
    private TextView crupierScoreTextView;
    private TextView playerScoreTextView;
    private TextView winnerTextView;
    private Button hitButton;
    private Button standButton;
    private Button resetButton;

    private OnGameResponseCallback onResponseCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        connectionHandle = ConnectionHandle.getInstance();

        crupierRecyclerView = findViewById(R.id.crupierRecyclerView);
        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        crupierScoreTextView = findViewById(R.id.crupierScoreTextView);
        playerScoreTextView = findViewById(R.id.playerScoreTextView);
        winnerTextView = findViewById(R.id.winnerTextView);
        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);
        resetButton = findViewById(R.id.resetButton);

        hitButton.setOnClickListener(this);
        standButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

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

        onResponseCallback = new OnGameResponseCallback(this) {
            @Override
            public Void call() {
                parseResponse(this.getResponse());
                return super.call();
            }
        };

        startGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishGame();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == hitButton.getId())
            connectionHandle.sendCommand(ActionsEnum.HIT, onResponseCallback);
        else if (v.getId() == standButton.getId())
            connectionHandle.sendCommand(ActionsEnum.STAND, onResponseCallback);
        else if (v.getId() == resetButton.getId()) restartGame();
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

    private void parseResponse(String response) {
        if (response == null || response.isEmpty()) return;

        String[] bjResult = response.split("\n");

        if (bjResult.length != 2 && bjResult.length != 3) return; // TODO: handle server errors

        String playerScore = calculateScore(bjResult[1]);
        runOnUiThread(() -> playerScoreTextView.setText(playerScore));
        if (bjResult.length == 3) { // One of the parties won
            if (Integer.parseInt(playerScore) <= 21) runOnUiThread(() -> {
                crupierScoreTextView.setText(calculateScore(bjResult[0]));
                crupierScoreTextView.setVisibility(View.VISIBLE);
            });
            onGameEnded(bjResult[2]);
            Log.d(GameActivity.class.getName(), "onGameEnded " + bjResult[2]);
        }


        // TODO: set data

//        runOnUiThread(() -> playerScoreTextView.setText(response));
    }

    private String calculateScore(String input) {
        int totalScoreStartIndex = input.lastIndexOf(":") + 1;
        return input.substring(totalScoreStartIndex);
    }

    private void onGameEnded(String winnerText) {
        runOnUiThread(() -> {
            winnerTextView.setText(winnerText);
            toggleButtonFunction(hitButton);
            toggleButtonFunction(standButton);
            toggleButtonFunction(resetButton);
        });
    }

    private void toggleButtonFunction(Button button) {
        if (button.getVisibility() == View.VISIBLE) {
            button.setVisibility(View.INVISIBLE);
            button.setEnabled(false);
        } else {
            button.setVisibility(View.VISIBLE);
            button.setEnabled(true);
        }
    }

    private void startGame() {
        connectionHandle.sendCommand(ActionsEnum.START_GAME, onResponseCallback);
    }

    private void finishGame() {
        connectionHandle.sendCommand(ActionsEnum.STOP_GAME, null);
    }

    private void restartGame() {
        finishGame();
        connectionHandle.reconnect(new OnConnectionCompleteCallback(this) {
            @Override
            public Void call() {
                if (this.getSocket().isConnected()) {
                    runOnUiThread(() -> {
                        startGame();
                        crupierScoreTextView.setVisibility(View.GONE);
                        toggleButtonFunction(hitButton);
                        toggleButtonFunction(standButton);
                        toggleButtonFunction(resetButton);
                        winnerTextView.setText("");
                    });
                } else {
                    showSimpleAlert("Connection failed", "Some awkward error. Quite strange in fact.");
                }
                return null;
            }
        });

    }
}