package es.ignaciofp.blackjackclient.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private List<Card> cardListCrupier;
    private List<Card> cardListPlayer;

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

        cardListCrupier = new ArrayList<>();
        cardListCrupier.add(new Card(this, "K♦"));
        cardListCrupier.add(new Card(this, "**"));

        cardListPlayer = new ArrayList<>();
        cardListPlayer.add(new Card(this, "10♠"));
        cardListPlayer.add(new Card(this, "A♣"));

        initRecyclerView(crupierRecyclerView, cardListCrupier);
        initRecyclerView(playerRecyclerView, cardListPlayer);

        onResponseCallback = new OnGameResponseCallback() {
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
        }

        // Card assignment
        String crupierCardsRaw = bjResult[0].replaceAll("Crupier: ", "").split(":")[0];
        String playerCardsRaw = bjResult[1].replaceAll("Jugador: ", "").split(":")[0];

        cardListCrupier = cardsAsList(crupierCardsRaw);
        cardListPlayer = cardsAsList(playerCardsRaw);
        runOnUiThread(() -> {
            ((AdapterCard) Objects.requireNonNull(crupierRecyclerView.getAdapter())).swapModels(cardListCrupier);
            ((AdapterCard) Objects.requireNonNull(playerRecyclerView.getAdapter())).swapModels(cardListPlayer);
        });
    }

    private List<Card> cardsAsList(String cards) {
        List<Card> cardList = new ArrayList<>();
        Pattern p = Pattern.compile("([0-9]+.)|(\\w.)|(\\*\\*)");
        Matcher m = p.matcher(cards);
        while (m.find()) cardList.add(new Card(this, m.group(0)));
        return cardList;
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