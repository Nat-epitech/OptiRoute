package com.optiroute.backend.utils;

import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonUtils {
    public static String formatTime(OffsetDateTime time) {
        return time.withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

    public static Integer kmhToMs(Integer kmh) {
        return (int) Math.round(kmh / 3.6);
    }

    public static String normalizeText(String value) {
        String normalized = Normalizer.normalize(value.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD);

        return normalized.replaceAll("\\p{M}", "").replaceAll("[^a-z0-9]", "");
    }
}
