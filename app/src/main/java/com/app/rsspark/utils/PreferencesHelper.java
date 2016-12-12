package com.app.rsspark.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.rsspark.domain.contract.RSSParkConstants;

/**
 * Created by konstie on 12.12.16.
 */

public class PreferencesHelper {
    private SharedPreferences getSharedPreferencesInstance(Context context, String key) {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    private void saveToPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferencesInstance(context, key);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getPreferenceValue(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferencesInstance(context, key);
        return sharedPreferences.getBoolean(key, false);
    }

    public void setSeenFeedRemovalWarningDialog(Context context, boolean seen) {
        saveToPreferences(context, RSSParkConstants.EXTRA_SEEN_WARNING_DIALOG, seen);
    }

    public boolean hasSeenFeedRemovalWarningDialog(Context context) {
        return getPreferenceValue(context, RSSParkConstants.EXTRA_SEEN_WARNING_DIALOG);
    }
}
