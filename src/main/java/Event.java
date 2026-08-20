/** Represents a task that takes place during a specified time period. */
public class Event extends Task {
    private final String start;
    private final String end;

    /** Creates an event task with a start and end time. */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /** Returns the event task's display text. */
    @Override
    public String toString() {
        return "[E][" + (this.isDone() ? "X] " : " ] ") + this.getDescription() + " (from: " + this.start + " to: " + this.end + ")";
    }
}
