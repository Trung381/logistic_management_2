package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Utils {
    static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter YM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String genID(IDKey key) {
        if (key == null) return null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddyyMMmmssHH");
        String code = now.format(formatter);
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return key.label + code + randomPart;
    }

    static Date toNextMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    static Date convertStringToDate(String dateString, DateTimeFormatter formatter, boolean isOffset) {
        if (dateString == null) return null;
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            if (isOffset) localDate = localDate.plusDays(1);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeParseException ex) {
            throw new InvalidParameterException("Định dạng yêu cầu không hợp lệ! (" + formatter + ")");
        }
    }

    /**
     * @param fromDateStr ngày bắt đầu (String: yyyy-MM-dd)
     * @param toDateStr ngày kết thúc (String: yyyy-MM-dd)
     * @return mảng gồm ngày bắt dầu và ngày kết thúc (Date: yyyy-MM-dd 00:00:00)
     */
    public static Date[] createDateRange(String fromDateStr, String toDateStr) {
        Date[] range = new Date[2];
        range[0] = convertStringToDate(fromDateStr, YMD_FORMATTER, false);
        range[1] = convertStringToDate(toDateStr, YMD_FORMATTER, true);
        return range;
    }

    /**
     * @param periodStr chu kỳ theo tháng (String: yyyy-MM)
     * @return mảng gồm ngày đầu chu kỳ và ngày cuối chu kỳ (Date: yyyy-MM-dd 00:00:00)
     */
    public static Date[] createDateRange(String periodStr) {
        Date[] range = new Date[2];
        range[0] = convertStringToDate(periodStr, YM_FORMATTER, false);
        range[1] = toNextMonth(range[0]);
        return range;
    }
}
