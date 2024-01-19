package es.ignaciofp.blackjackclient.callbacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.net.Socket;
import java.util.concurrent.Callable;

import es.ignaciofp.blackjackclient.ui.activities.GameActivity;
import es.ignaciofp.blackjackclient.utils.ConnectionHandle;

public class OnConnectionCompleteCallback implements Callable<Void> {

    private final Context CONTEXT;
    private Socket clientSocket;

    public OnConnectionCompleteCallback(Context context) {
        this.CONTEXT = context;
    }

    public void setSocket(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public Void call() {
        if (clientSocket.isConnected()) {
            Intent intent = new Intent(CONTEXT, GameActivity.class);
            CONTEXT.startActivity(intent);
            ((Activity) CONTEXT).finish();
        } else {
            showSimpleAlert("Connection failed", "Some awkward error. Quite strange in fact.");
        }
        return null;
    }

    /**
     * Displays an Alert Dialog
     *
     * @param title   the title to show in the alert
     * @param message the message to show in the alert
     */
    private void showSimpleAlert(String title, String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CONTEXT);
        builder.setTitle(title).setMessage(message).setPositiveButton("OK", (dialog, which) -> {
        }).show();
    }
}
