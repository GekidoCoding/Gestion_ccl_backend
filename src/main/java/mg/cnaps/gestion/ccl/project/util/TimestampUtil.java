package mg.cnaps.gestion.ccl.project.util;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.exception.FrequenceNotFoundException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimestampUtil {

    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        return sdf.format(timestamp);
    }
    public static Timestamp parseTimestamp(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = (Date) sdf.parse(dateString);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatTimestampNoHours(Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
    /**
     * Retourne true si les deux intervalles [startA,endA] et [startB,endB] se chevauchent.
     * La comparaison est inclusive : si endA == startB ou startA == endB -> considéré comme chevauchement.
     * Si tu veux exclure les bornes (fin == début), utilise strictement > / < (after/before).
     */
    public static boolean isOverlap(Timestamp startA, Timestamp endA, Timestamp startB, Timestamp endB) {
        return !endA.before(startB) && !startA.after(endB);
    }
}
