package mg.cnaps.gestion.ccl.project.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimestampUtil {
    public static String formatTimestamp(Timestamp timestamp) {
//        if (timestamp == null) return "";
//
//        LocalDateTime dateTime = timestamp.toLocalDateTime();
//
//        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
//        int day = dateTime.getDayOfMonth();
//        Month month = dateTime.getMonth();
//        int year = dateTime.getYear();
//        int hour = dateTime.getHour();
//        int minute = dateTime.getMinute();
//        int second = dateTime.getSecond();
//
//        String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH);
//        String monthName = month.getDisplayName(TextStyle.SHORT, Locale.FRENCH);
//
//        return String.format(
//                "%s %02d %s %d à %02d:%02d:%02d",
//                dayName, day, monthName, year, hour, minute, second
//        );
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        return sdf.format(timestamp);
    }
    public static String formatTimestampWithT(Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(timestamp);
    }
    public static String formatTimestampWithoutT(Timestamp timestamp) {
        return formatTimestampWithT(timestamp);
    }
}
