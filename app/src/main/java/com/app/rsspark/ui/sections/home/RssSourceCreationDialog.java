package com.app.rsspark.ui.sections.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.PatternsCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.rsspark.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by konstie on 10.12.16.
 */

public class RssSourceCreationDialog extends DialogFragment {
    private static final String TAG = "RssSourceCreationDialog";

    private RssDialogListener listener;

    @BindView(R.id.root_view) LinearLayout rootLayout;
    @BindView(R.id.feed_title_edit_text) EditText titleEditText;
    @BindView(R.id.feed_url_edit_text) EditText urlEditText;

    public interface RssDialogListener {
        void onSaveNewRssFeedClicked(String rssTitle, String rssUrl);
        void showInvalidNewRssMessage(@StringRes int messageRes);
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
                .setPositiveButton(R.string.feed_dialog_save, null)
                .setNegativeButton(R.string.feed_dialog_cancel, ((dialogInterface, i) ->
                        dismiss())
                );

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button buttonSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            buttonSave.setOnClickListener(view -> onSavePressed());
        });

        return dialog;
    }

    private void onSavePressed() {
        String currentUrl = urlEditText.getText().toString();
        String currentTitle = titleEditText.getText().toString();
        Log.d(TAG, "Clicked on Save button. Listener: " + listener + ", currentUrl: " + currentUrl
                + ", currentTitle: " + currentTitle);
        if (listener == null) {
            return;
        }
        if (!currentTitle.isEmpty() && !currentUrl.isEmpty() && isUrlMatching(currentUrl)) {
            listener.onSaveNewRssFeedClicked(currentTitle, currentUrl);
            dismiss();
        } else if (currentTitle.isEmpty()) {
            listener.showInvalidNewRssMessage(R.string.feed_dialog_empty_title);
        } else if (currentUrl.isEmpty() || !isUrlMatching(currentUrl)) {
            listener.showInvalidNewRssMessage(R.string.feed_dialog_invalid_url);
        } else {
            listener.showInvalidNewRssMessage(R.string.feed_dialog_could_not_save_feed);
        }
    }

    private boolean isUrlMatching(String urlToCheck) {
        return PatternsCompat.WEB_URL.matcher(urlToCheck).matches();
    }
}