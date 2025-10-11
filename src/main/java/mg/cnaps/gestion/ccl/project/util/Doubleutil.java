package mg.cnaps.gestion.ccl.project.util;

public class Doubleutil {
    private static final String[] UNITS = {
            "", "un", "deux", "trois", "quatre", "cinq", "six",
            "sept", "huit", "neuf", "dix", "onze", "douze",
            "treize", "quatorze", "quinze", "seize",
            "dix-sept", "dix-huit", "dix-neuf"
    };

    private static final String[] TENS = {
            "", "", "vingt", "trente", "quarante", "cinquante",
            "soixante", "soixante", "quatre-vingt", "quatre-vingt"
    };

    public static String convert(long number) {
        if (number == 0) {
            return "zéro";
        }
        return convertNumber(number).trim();
    }

    private static String convertNumber(long number) {
        if (number < 20) {
            return UNITS[(int) number];
        } else if (number < 100) {
            int ten = (int) number / 10;
            int unit = (int) number % 10;
            String result = TENS[ten];

            if (ten == 7 || ten == 9) {
                result = TENS[ten] + "-" + UNITS[10 + unit];
            } else if (unit > 0) {
                result += (unit == 1 && ten < 8 ? " et " : "-") + UNITS[unit];
            }
            return result;
        } else if (number < 1000) {
            int hundred = (int) number / 100;
            int remainder = (int) number % 100;
            String result = (hundred > 1 ? UNITS[hundred] + " " : "") + "cent";
            if (remainder > 0) {
                result += " " + convertNumber(remainder);
            }
            return result;
        } else if (number < 1_000_000) {
            long thousand = number / 1000;
            long remainder = number % 1000;
            String result = (thousand > 1 ? convertNumber(thousand) + " " : "") + "mille";
            if (remainder > 0) {
                result += " " + convertNumber(remainder);
            }
            return result;
        } else if (number < 1_000_000_000) {
            long million = number / 1_000_000;
            long remainder = number % 1_000_000;
            String result = convertNumber(million) + " million" + (million > 1 ? "s" : "");
            if (remainder > 0) {
                result += " " + convertNumber(remainder);
            }
            return result;
        } else {
            long billion = number / 1_000_000_000;
            long remainder = number % 1_000_000_000;
            String result = convertNumber(billion) + " milliard" + (billion > 1 ? "s" : "");
            if (remainder > 0) {
                result += " " + convertNumber(remainder);
            }
            return result;
        }
    }


}
