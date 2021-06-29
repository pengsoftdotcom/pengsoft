package com.pengsoft.support.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The date utility methods.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * default zone id
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("GMT+8");

    /**
     * default zone offset
     */
    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

    /**
     * default time pattern
     */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * default date pattern
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * default datetime pattern
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * date formatter
     */
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_PATTERN);

    /**
     * time formatter
     */
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_PATTERN);

    /**
     * datetime formatter
     */
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern(DateUtils.DEFAULT_DATETIME_PATTERN);

    /**
     * return current {@link LocalDateTime} of default {@link ZoneId}
     *
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now(DateUtils.DEFAULT_ZONE_ID);
    }

    /**
     * return current {@link LocalDateTime} of specified {@link ZoneId}
     *
     * @param zoneId {@link ZoneId}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime currentDateTime(final ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    /**
     * return current {@link LocalDate} of {@link ZoneId}
     *
     * @return {@link LocalDate}
     */
    public static LocalDate currentDate() {
        return DateUtils.currentDate(DateUtils.DEFAULT_ZONE_ID);
    }

    /**
     * return current {@link LocalDate} of specified {@link ZoneId}
     *
     * @param zoneId {@link ZoneId}
     * @return {@link LocalDate}
     */
    public static LocalDate currentDate(final ZoneId zoneId) {
        return LocalDate.now(zoneId);
    }

    /**
     * format {@link LocalDate} with time formatter
     *
     * @param localDate {@link LocalDate}
     * @return formatted string
     */
    public static String formatDate(final LocalDate localDate) {
        return DateUtils.dateFormatter.format(localDate);
    }

    /**
     * format {@link LocalDateTime} with time formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatDate(final LocalDateTime localDateTime) {
        return DateUtils.dateFormatter.format(localDateTime);
    }

    /**
     * format {@link LocalTime} with time formatter
     *
     * @param localTime {@link LocalTime}
     * @return formatted string
     */
    public static String formatTime(final LocalTime localTime) {
        return DateUtils.timeFormatter.format(localTime);
    }

    /**
     * format {@link LocalDateTime} with time formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatTime(final LocalDateTime localDateTime) {
        return DateUtils.timeFormatter.format(localDateTime);
    }

    /**
     * format {@link LocalDateTime} with datetime formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatDateTime(final LocalDateTime localDateTime) {
        return DateUtils.dateTimeFormatter.format(localDateTime);
    }

    /**
     * parse a string to with date formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalDate}
     */
    public static LocalDate parseDate(final String datetime) {
        return LocalDate.from(dateFormatter.parse(datetime));
    }

    /**
     * parse a string to with time formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalTime}
     */
    public static LocalTime parseTime(final String datetime) {
        return LocalTime.from(timeFormatter.parse(datetime));
    }

    /**
     * parse a string to with datetime formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalDateTime}
     */
    public static LocalDateTime parseDateTime(final String datetime) {
        return LocalDateTime.from(dateTimeFormatter.parse(datetime));
    }

    /**
     * return the beginning {@link LocalDateTime} of today
     *
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime beginningOfToday() {
        return DateUtils.currentDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * return the beginning {@link LocalDateTime} of tomorrow
     *
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime beginningOfTomorrow() {
        return DateUtils.beginningOfToday().plusDays(1);
    }

    /**
     * return the {@link Date} of specified {@link LocalDateTime}
     *
     * @return {@link Date}
     */
    public static Date getDate(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(DateUtils.DEFAULT_ZONE_OFFSET));
    }

}