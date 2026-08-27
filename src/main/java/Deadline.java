import java.time.LocalDateTime;

/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private final LocalDateTime deadline;

    /** Creates a deadline task from a user-entered date or date/time. */
    public Deadline(String description, String deadline) {
        this(description, DateTimeParser.parse(deadline));
    }

    /** Creates a deadline task from an already parsed date/time. */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /** Returns the deadline task's display text. */
    @Override
    public String toString() {
        return "[D][" + (this.isDone() ? "X] " : " ] ") + this.getDescription()
                + " (by: " + DateTimeParser.format(this.deadline) + ")";
    }

    /** Returns the deadline task in the format used by the task storage file. */
    @Override
    public String toFileFormat() {
        return "D | " + (this.isDone() ? "1" : "0") + " | " + this.getDescription()
                + " | " + DateTimeParser.formatForStorage(this.deadline);
    }
}
