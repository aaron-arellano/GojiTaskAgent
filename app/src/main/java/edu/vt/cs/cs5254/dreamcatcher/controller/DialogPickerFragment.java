package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import edu.vt.cs.cs5254.dreamcatcher.R;

/**
 * Created by Aaron on 3/16/2018.
 */

public class DialogPickerFragment extends DialogFragment {
    public static final String EXTRA_TEXT = "dreamcatcher.comment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_comment, null);

        EditText mEditText = v.findViewById(R.id.edit_text);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) ->
                        { sendResult(Activity.RESULT_OK, mEditText.getText().toString()); })
                .setNegativeButton(
                        android.R.string.cancel,
                        (dialog, which) -> { })
                .create();
        }

    private void sendResult(int resultCode, String comment) {
        if (getTargetFragment() == null) { return; }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TEXT, comment);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
