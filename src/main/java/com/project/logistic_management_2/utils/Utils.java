package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

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

    static Date parseFromYearMonth(String yearMonthString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        try {
            LocalDate localDate = LocalDate.parse(yearMonthString, formatter);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("Định dạng chu kỳ không hợp lệ! (yyyyy-MM)");
        }
    }

    public static Date convertToDateOfCurrentMonth(String yearMonth) {
        if (yearMonth == null) return null;
        return parseFromYearMonth(yearMonth);
    }

    public Date convertToDateOfNextMonth(String yearMonth) {
        Date date = convertToDateOfCurrentMonth(yearMonth);
        return moveToNextMonth(date);
    }

    static Date moveToNextMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date convertToDateOfTimestamp(String dateString) {
        if (dateString == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("Định dạng ngày tháng không hợp lệ! (yyyy-MM-dd)");
        }
    }
}
