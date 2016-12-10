package com.app.rsspark.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by konstie on 10.12.16.
 */

public final class FormattingUtils {
    private static final String DATE_FORMATTING_PATTERN = "MMMM, dd yyyy. HH:mm";

    private FormattingUtils() {}

    public static String getFormattedDate(Date srcDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATTING_PATTERN, Locale.ENGLISH);
        return formatter.format(srcDate);
    }
}
