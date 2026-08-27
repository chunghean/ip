import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;

/** Parses and formats the date and time values used by deadline and event tasks. */
public final class DateTimeParser {
    private static final Locale DISPLAY_LOCALE = Locale.ENGLISH;
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu", DISPLAY_LOCALE);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", DISPLAY_LOCALE);
    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
            strictFormatter("d/M/uuuu HHmm"),
            strictFormatter("uuuu-MM-dd HHmm"),
            strictFormatter("d/M/uuuu HH:mm"),
            strictFormatter("uuuu-MM-dd HH:mm"));
    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            strictFormatter("d/M/uuuu"),
            strictFormatter("uuuu-MM-dd"));

    private DateTimeParser() {
        // Utility class; do not instantiate.
    }

    /**
     * Parses a user-entered date or date/time.
     *
     * <p>Date-only values are represented at midnight so that one typed value can
     * support both deadlines and event start/end times.</p>
     *
     * @param text the date or date/time entered by the user
     * @return the parsed local date/time
     * @throws DateTimeParseException if the value is not in a supported format
     */
    public static LocalDateTime parse(String text) {
        String value = text == null ? "" : text.trim();
        for (DateTimeFormatter formatter : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next supported format.
            }
        }

        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(value, formatter).atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Try the next supported format.
            }
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(value).atStartOfDay();
        }
    }

    /** Returns a human-readable date, including a time when it is not midnight. */
    public static String format(LocalDateTime value) {
        String date = value.format(DISPLAY_DATE_FORMAT);
        return value.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? date
                : date + " " + value.format(DISPLAY_TIME_FORMAT);
    }

    /** Returns a stable ISO value suitable for the task storage file. */
    public static String formatForStorage(LocalDateTime value) {
        return value.toString();
    }

    /** Creates a strict formatter so invalid calendar dates are rejected. */
    private static DateTimeFormatter strictFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern, DISPLAY_LOCALE)
                .withResolverStyle(ResolverStyle.STRICT);
    }
}
