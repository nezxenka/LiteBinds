package org.nezxenka.litebinds.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class TimeFormatter {

    private final String notSpecifiedMessage;

    public TimeFormatter(Map<String, String> messages) {
        this.notSpecifiedMessage = messages.getOrDefault(
            "not_specified",
            "Не указано"
        );
    }

    public String format(Timestamp timestamp) {
        if (timestamp == null) return notSpecifiedMessage;
        SimpleDateFormat sdf = new SimpleDateFormat(
            "d MMM HH:mm:ss",
            new Locale("ru", "RU")
        );
        String formattedDate = sdf.format(timestamp);

        formattedDate = formattedDate.replaceFirst(
            "([А-Я])",
            "$1".toLowerCase()
        );

        formattedDate = formattedDate
            .replaceAll("янв\\.?", "янв.")
            .replaceAll("фев\\.?", "фев.")
            .replaceAll("мар\\.?", "мар.")
            .replaceAll("апр\\.?", "апр.")
            .replaceAll("май\\.?", "май.")
            .replaceAll("июн\\.?", "июн.")
            .replaceAll("июл\\.?", "июл.")
            .replaceAll("авг\\.?", "авг.")
            .replaceAll("сен\\.?", "сен.")
            .replaceAll("окт\\.?", "окт.")
            .replaceAll("ноя\\.?", "ноя.")
            .replaceAll("дек\\.?", "дек.");

        return formattedDate;
    }
}
