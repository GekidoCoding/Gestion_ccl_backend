package mg.cnaps.gestion.ccl.framework.check.util;

import mg.cnaps.gestion.ccl.framework.check.annotation.CheckField;
import mg.cnaps.gestion.ccl.framework.check.annotation.Checkable;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.*;
import java.util.Date;

public class ObjectComparator {

    public static boolean isDegreeAtteint(Object o1, Object o2){
       double percentage= ObjectComparator.compare(o1, o2);
        Class<?> clazz = o1.getClass();
        if(!clazz.isAnnotationPresent(Checkable.class)){
            return false;
        }
        Checkable checkable = clazz.getAnnotation(Checkable.class);
        double degree = checkable.similarityDegree();
        return degree <= percentage;
    }


    public static double compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) return 0;
        if (!o1.getClass().equals(o2.getClass())) return 0;

        Class<?> clazz = o1.getClass();
        if (!clazz.isAnnotationPresent(Checkable.class))
            return 0;

        Field[] fields = clazz.getDeclaredFields();
        double totalSimilarity = 0;
        int validFieldCount = 0;

        for (Field field : fields) {
            if (field.isAnnotationPresent(CheckField.class)) {
                field.setAccessible(true);
                try {
                    Object v1 = field.get(o1);
                    Object v2 = field.get(o2);

                    if (isEmpty(v1) && isEmpty(v2)) {
                        continue;
                    }

                    double similarity = 0;

                    if (v1 == null && v2 == null) {
                        similarity = 100;
                    } else if (v1 != null && v2 != null) {
                        if (v1 instanceof Number && v2 instanceof Number) {
                            double n1 = ((Number) v1).doubleValue();
                            double n2 = ((Number) v2).doubleValue();
                            double max = Math.max(Math.abs(n1), Math.abs(n2));
                            if (max == 0) similarity = 100;
                            else similarity = Math.max(0, 100 - (Math.abs(n1 - n2) / max) * 100);

                        } else if (v1 instanceof String && v2 instanceof String) {
                            similarity = stringSimilarity((String) v1, (String) v2) * 100;

                        } else if (isDateType(v1) && isDateType(v2)) {
                            similarity = dateSimilarity(v1, v2);

                        } else if (v1 instanceof Boolean && v2 instanceof Boolean) {
                            similarity = v1.equals(v2) ? 100 : 0;

                        } else if (v1 instanceof Enum && v2 instanceof Enum) {
                            similarity = v1.equals(v2) ? 100 : 0;

                        } else if (v1.getClass().isAnnotationPresent(Checkable.class)) {
                            similarity = compare(v1, v2);

                        } else if (v1.equals(v2)) {
                            similarity = 100;
                        }
                    }

                    totalSimilarity += similarity;
                    validFieldCount++;

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (validFieldCount == 0) return 0;
        return totalSimilarity / validFieldCount;
    }


    private static boolean isEmpty(Object value) {
        if (value == null) return true;

        if (value instanceof String) {
            String s = ((String) value).trim();
            return s.isEmpty();
        }

        if (value.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(value) == 0;
        }

        if (value instanceof java.util.Collection<?>) {
            return ((java.util.Collection<?>) value).isEmpty();
        }

        if (value instanceof java.util.Map<?, ?>) {
            return ((java.util.Map<?, ?>) value).isEmpty();
        }

        return false;
    }

    private static boolean isDateType(Object obj) {
        return obj instanceof Date ||
                obj instanceof LocalDate ||
                obj instanceof LocalDateTime ||
                obj instanceof LocalTime ||
                obj instanceof OffsetDateTime ||
                obj instanceof ZonedDateTime ||
                obj instanceof Timestamp;
    }


    private static double dateSimilarity(Object d1, Object d2) {
        Instant i1 = convertToInstant(d1);
        Instant i2 = convertToInstant(d2);

        if (i1 == null || i2 == null) return 0;

        long diff = Math.abs(Duration.between(i1, i2).toMillis());
        if (diff == 0) return 100;

        long oneDayMillis = 86_400_000L;
        double similarity = Math.max(0, 100 - (diff * 100.0 / oneDayMillis));
        return Math.min(similarity, 100);
    }


    private static Instant convertToInstant(Object dateObj) {
        if (dateObj instanceof Date) return ((Date) dateObj).toInstant();
        if (dateObj instanceof Timestamp) return ((Timestamp) dateObj).toInstant();
        if (dateObj instanceof LocalDate) return ((LocalDate) dateObj).atStartOfDay(ZoneId.systemDefault()).toInstant();
        if (dateObj instanceof LocalDateTime) return ((LocalDateTime) dateObj).atZone(ZoneId.systemDefault()).toInstant();
        if (dateObj instanceof LocalTime) return ((LocalTime) dateObj).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        if (dateObj instanceof OffsetDateTime) return ((OffsetDateTime) dateObj).toInstant();
        if (dateObj instanceof ZonedDateTime) return ((ZonedDateTime) dateObj).toInstant();
        return null;
    }


    private static double stringSimilarity(String s1, String s2) {
        s1 = Normalizer.normalize(s1, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s2 = Normalizer.normalize(s2, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        s1 = s1.replaceAll("[^a-z0-9]", "");
        s2 = s2.replaceAll("[^a-z0-9]", "");

        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;

        int same = 0;
        int minLen = Math.min(s1.length(), s2.length());
        for (int i = 0; i < minLen; i++) {
            if (s1.charAt(i) == s2.charAt(i)) same++;
        }
        return (double) same / maxLen;
    }

}

