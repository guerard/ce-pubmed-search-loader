package com.conceptualeyes.pubmed.serializer;

import java.util.Locale;

public class DateUtils {
    private static final String YYYY_MM_DD_FORMAT = "%04d-%02d-%02d";

    public static String YYYY_MM_DD_dateString(final String year, final String month, final String day) {
        return String.format(
                Locale.US,
                YYYY_MM_DD_FORMAT,
                Long.valueOf(year),
                Long.valueOf(month),
                Long.valueOf(day));
    }
}
