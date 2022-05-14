package scm.cptam3.witch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class howToPlay2Dialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("How To Play")
                .setMessage("Your mission is to guide a lost witch back to her home.\n" +
                        "Since there are portals teleporting the witch back to her original position around her house, you have to guide her to dodge them. "+
                        "By using the controller, you change the witch's walking direction.\n"+
                        "\n" +
                        "Press Big/Small to change the size of game objects.\n"+
                        "Press Mode to change the game mode.\n"+
                        "Press Reset to start or re-start the game.\n"+
                        "Press Respawn to teleport the witch back to her original position.\n"+
                        "\n"+ "**Find a big space to play when you set your size of game objects to big. Or You have to adjust position of the game objects spawned by your own.\n"
                        );

        return builder.create();
    }
}