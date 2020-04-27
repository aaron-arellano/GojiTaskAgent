package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import edu.vt.cs.cs5254.dreamcatcher.R;

/**
 * Created by Aaron on 3/16/2018. This class is the fragment that handles dialog text boxes and
 * enters text in dreams and entries.
 */

public class DialogPickerFragment extends DialogFragment {
    public static final String EXTRA_TEXT = "dreamcatcher.comment";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_comment, null);

        // contains the entryID, check for null
        String arg;
        try {
            arg = getArguments().getString("entryID");
        } catch(NullPointerException npe) {
            // if null pointer is caught, the comment is new
            arg = "";
        }

        // set arg to effectively final for lambda
        String finalArg = arg;

        EditText mEditText = v.findViewById(R.id.edit_text);
        // create the dialog box for entry
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) ->
                        { sendResult(Activity.RESULT_OK, mEditText.getText().toString()+ finalArg); })
                .setNegativeButton(
                        android.R.string.cancel,
                        (dialog, which) -> { })
                .create();
        }
     // send the result of the text entry back to the calling fragment
    private void sendResult(int resultCode, String comment) {
        if (getTargetFragment() == null) { return; }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TEXT, comment);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
