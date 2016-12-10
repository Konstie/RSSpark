package com.app.rsspark.ui.sections.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.app.rsspark.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by konstie on 10.12.16.
 */

public class RssSourceCreationDialog extends DialogFragment {
    private RssDialogListener listener;

    @BindView(R.id.feed_title_edit_text) EditText titleEditText;
    @BindView(R.id.feed_url_edit_text) EditText urlEditText;

    public interface RssDialogListener {
        void onSaveNewRssFeedClicked(String rssTitle, String rssUrl);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RssDialogListener) context;
        } catch (ClassCastException exc) {
            throw new ClassCastException("Should have implemented RssDialogListener interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogContentView = inflater.inflate(R.layout.content_rss_feed_dialog, null);
        ButterKnife.bind(this, dialogContentView);

        builder.setView(dialogContentView)
                .setTitle(R.string.drawer_add_new_rss)
                .setPositiveButton(R.string.feed_dialog_save, (dialogInterface, i) -> {
                    String currentUrl = urlEditText.getText().toString();
                    String currentTitle = titleEditText.getText().toString();
                    if (listener != null && !currentTitle.isEmpty() && !currentUrl.isEmpty()) {
                        listener.onSaveNewRssFeedClicked(currentTitle, currentUrl);
                    }
                })
                .setNegativeButton(R.string.feed_dialog_cancel, ((dialogInterface, i) ->
                        dismiss())
                );

        return builder.create();
    }
}