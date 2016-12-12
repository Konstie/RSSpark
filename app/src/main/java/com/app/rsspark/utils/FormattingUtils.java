package com.app.rsspark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by konstie on 10.12.16.
 */

public final class FormattingUtils {
    private static final String SRC_DATE_FORMATTING_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String DATE_FORMATTING_PATTERN = "MMMM, dd yyyy. HH:mm";

    private FormattingUtils() {}

    public static Date getConvertedDate(String srcPubDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(SRC_DATE_FORMATTING_PATTERN, Locale.ENGLISH);
        Date date;
        try {
            date = formatter.parse(srcPubDate);
        } catch (ParseException exc) {
            date = new Date();
        }
        return date;
    }

    public static String getFormattedDate(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTING_PATTERN, Locale.ENGLISH);
        return sdf.format(srcDate);
    }
}
