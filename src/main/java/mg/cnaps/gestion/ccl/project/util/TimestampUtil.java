package mg.cnaps.gestion.ccl.project.util;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.exception.FrequenceNotFoundException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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

    public static long[] calculateDateDifference(long timestamp1, long timestamp2) {
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp1), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp2), ZoneId.systemDefault());

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Le timestamp de fin doit être postérieur au timestamp de début");
        }

        double hours = ChronoUnit.MINUTES.between(start, end) / 60.0;
        double days = ChronoUnit.HOURS.between(start, end) / 24.0;
        double months = ChronoUnit.DAYS.between(start, end) / 30.0;

        long roundedHours = (long) Math.ceil(hours);
        long roundedDays = (long) Math.ceil(days);
        long roundedMonths = (long) Math.ceil(months);

        return new long[]{roundedHours, roundedDays, roundedMonths};
    }



    public static Double caculateDifferenceTimestamp(CclPropertyService cclPropertyService, Timestamp timestamp1, Timestamp timestamp2 , String frequenceId) {
        long[] durees = calculateDateDifference(timestamp1.getTime(), timestamp2.getTime());
        if(frequenceId.equals( cclPropertyService.getFrequenceHeureId())){
            return (double) durees[0];
        }
        if(frequenceId.equals( cclPropertyService.getFrequenceJourId())){
            return (double) durees[1];
        }
        if(frequenceId.equals( cclPropertyService.getFrequenceNuitId())){
            return (double) durees[1];
        }
        if(frequenceId.equals( cclPropertyService.getFrequenceMoisId())){
            return (double) durees[2];
        }
        throw new FrequenceNotFoundException("Type de frequence non considerer !");
    }
}
