package edu.vt.cs.cs5254.dreamcatcher.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.controller.DreamFragment; //used for entry action
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;

public class EntryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private DreamEntry mEntry;
    private Button mButton;
    private static final int REALIZED_COLOR = 0xff008f00;
    private static final int DEFERRED_COLOR = 0xffff0000;
    private static final int COMMENT_COLOR = 0xffffd479;
    private static final int REVEALED_COLOR = 0xff0000ff;


    public EntryHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_entry, parent, false));
        // set the button to an entry button view item
        mButton = itemView.findViewById(R.id.entry_button);
        // set a listener on the entry buttons
        mButton.setOnClickListener(this);
    }

    public void bind(DreamEntry entry) {
        mEntry = entry;

        switch (entry.getKind()) {
            case REVEALED:
                setRevealedStyle(mButton);
                setNotificationStyle(mButton);
                mButton.setText(entry.getText());
                break;
            case DEFERRED:
                setDeferredStyle(mButton);
                setNotificationStyle(mButton);
                mButton.setText(entry.getText());
                break;
            case REALIZED:
                setRealizedStyle(mButton);
                setNotificationStyle(mButton);
                mButton.setText(entry.getText());
                break;
            default:
                setCommentStyle(mButton);
                String text = entry.getText();
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                String date = df.format(entry.getDate());
                String fullCommentString = text + " (" + date + ")";
                //
                Spannable spannable = new SpannableString(fullCommentString);
                int start = text.length();
                int end = fullCommentString.length();
                spannable.setSpan(
                        new ForegroundColorSpan(Color.GRAY),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                //
                mButton.setText(spannable);
        }

    }

    private void setNotificationStyle(Button button) {
        button.setTextColor(Color.WHITE);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setAllCaps(true);
    }

    private void setRevealedStyle(Button button) {
        button.getBackground().setColorFilter(REVEALED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
    }

    private void setRealizedStyle(Button button) {
        button.getBackground().setColorFilter(REALIZED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
        //imageView.setImageResource(R.drawable.star);
    }

    private void setDeferredStyle(Button button) {
        button.getBackground().setColorFilter(DEFERRED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
        //imageView.setImageResource(R.drawable.sleepy);
    }

    private void setCommentStyle(Button button) {
        button.getBackground().setColorFilter(COMMENT_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        button.setAllCaps(false);
        button.setTextColor(Color.BLACK);
    }

    // method for clicking a dream
    @Override
    public void onClick(View view) {
        Log.v("Entry OnClick()","THIS DREAM WAS CLICKED");
        Context context = view.getContext();
        DreamFragment.Callbacks callbacks = (DreamFragment.Callbacks) context;
        callbacks.onEntrySelected(mEntry);
    }

}
