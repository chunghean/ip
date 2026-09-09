package bingusdingus.task;

import java.time.LocalDateTime;
import java.util.Objects;

import bingusdingus.parser.DateTimeParser;

/** Represents a task that takes place during a specified time period. */
public class Event extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /** Creates an event task from user-entered start and end dates or date/times. */
    public Event(String description, String start, String end) {
        this(description, DateTimeParser.parse(start), DateTimeParser.parse(end));
    }

    /** Creates an event task from already parsed start and end date/times. */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = Objects.requireNonNull(start, "start");
        this.end = Objects.requireNonNull(end, "end");
        if (!this.end.isAfter(this.start)) {
            throw new IllegalArgumentException("Event end must be after start");
        }
    }

    /** Returns the event task's display text. */
    @Override
    public String toString() {
        return "[E][" + this.getStatusIcon() + "] " + this.getDescription()
                + " (from: " + DateTimeParser.format(this.start)
                + " to: " + DateTimeParser.format(this.end) + ")";
    }

    /** Returns the event task in the format used by the task storage file. */
    @Override
    public String toFileFormat() {
        return "E | " + (this.isDone() ? "1" : "0") + " | " + this.getDescription()
                + " | " + DateTimeParser.formatForStorage(this.start)
                + " | " + DateTimeParser.formatForStorage(this.end);
    }
}
