package es.ignaciofp.blackjackclient.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.ignaciofp.blackjackclient.R;
import es.ignaciofp.blackjackclient.callbacks.OnConnectionCompleteCallback;
import es.ignaciofp.blackjackclient.utils.ConnectionHandle;

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

            if (checkHostAndPort(host, port)) // Checking if host and port are both valid
                ConnectionHandle.getInstance().connect(host, Integer.parseInt(port), new OnConnectionCompleteCallback(this));

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

        int port = 0;
        try {
            port = Integer.parseInt(portStr); // We're not using the value so not storing it
        } catch (NumberFormatException e) { // If cannot parse to int, is an invalid port number
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Invalid port").setMessage("Please, enter a valid port, example: 9999.").setPositiveButton("OK", (dialog, which) -> {
            }).show();
            return false;
        }

        // TODO: Check if port is between the range of valid ports and if host is a valid host
        return true;
    }
}