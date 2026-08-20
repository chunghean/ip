import java.util.ArrayList;

/** Stores and manages the tasks created by Bingus Dingus. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at the specified zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at the specified zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }
}
