/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private final String deadline;

    /** Creates a deadline task. */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /** Returns the deadline task's display text. */
    @Override
    public String toString() {
        return "[D][" + (this.isDone() ? "X] " : " ] ") + this.getDescription() + " (by: " + this.deadline + ")";
    }
}
