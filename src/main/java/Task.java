/** Represents a task in the task list. */
public class Task {
    protected String description;
    protected boolean isDone;

    /** Creates an incomplete task. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks the task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks the task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /** Returns the task's display status icon. */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the task description. */
    public String getDescription() {
        return description;
    }
}
