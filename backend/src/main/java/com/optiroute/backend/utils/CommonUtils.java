package com.optiroute.backend.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
    public static String formatTime(OffsetDateTime time) {
        return time.withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }
}
