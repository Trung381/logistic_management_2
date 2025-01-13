package com.project.logistic_management_2.utils;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.enums.IDKey;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Utils {
    public static String genID(IDKey key) {
        if (key == null) return null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddyyMMmmssHH");
        String code = now.format(formatter);
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return key.label + code + randomPart;
    }

    public static Pair<Timestamp, Timestamp> parseAndValidateDates(String fromDate, String toDate) throws IllegalArgumentException {
        Timestamp fromTimestamp = null;
        Timestamp toTimestamp = null;

        if (fromDate != null) {
            try {
                fromTimestamp = Timestamp.valueOf(fromDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid fromDate format.");
            }
        }

        if (toDate != null) {
            try {
                toTimestamp = Timestamp.valueOf(toDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid toDate format.");
            }
        }

        return Pair.of(fromTimestamp, toTimestamp);
    }
}
