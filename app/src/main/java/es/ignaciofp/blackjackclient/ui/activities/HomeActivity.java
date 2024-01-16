package es.ignaciofp.blackjackclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.ignaciofp.blackjackclient.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EditText hostEditText = findViewById(R.id.editTextHost);
        EditText portEditText = findViewById(R.id.editTextPort);
        Button playButton = findViewById(R.id.buttonPlay);

        playButton.setOnClickListener((v) -> {
            String host = hostEditText.getText().toString().trim();
            String port = portEditText.getText().toString().trim();

            if (!checkHostAndPort(host, port)) return; // Checking if host and port are both valid

            // Starting GameActivity and sending the host and port
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("host", host);
            intent.putExtra("port", port);
            startActivity(intent);
        });
    }

    /**
     * Checks if host is a valid host and port a valid port number.
     *
     * @param host    the host provided by the user
     * @param portStr the port provided by the user
     * @return true if host and port are valid
     */
    private boolean checkHostAndPort(String host, String portStr) {
        if (host.isEmpty() || portStr.isEmpty()) {
            // Showing toast instead of alert to no scare the users
            Toast.makeText(this, "Host and port must not be empty", Toast.LENGTH_SHORT).show();
            return false; // Early return if host or port are empty
        }

        try {
            Integer.parseInt(portStr); // We're not using the value so not storing it
        } catch (NumberFormatException e) { // If cannot parse to int, is an invalid port number
            showSimpleAlert("Invalid port", "Please, enter a valid port, example: 9999.");
            return false;
        }

        // TODO: Check if port is between the range of valid ports and if host is a valid host
        return false;
    }

    /**
     * Displays an Alert Dialog
     *
     * @param title   the title to show in the alert
     * @param message the message to show in the alert
     */
    private void showSimpleAlert(String title, String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton("OK", (dialog, which) -> {
        }).show();
    }
}