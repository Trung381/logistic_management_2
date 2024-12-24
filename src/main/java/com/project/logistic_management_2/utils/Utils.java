package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.enums.IDKey;
import org.springframework.stereotype.Component;

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
}
