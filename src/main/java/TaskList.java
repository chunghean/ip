import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;

/** Stores and manages the tasks created by Bingus Dingus. */
public class TaskList {
    private static final Path STORAGE_PATH = Path.of(".", "data", "bingusdingus.txt");
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
        save();
    }

    /** Returns the task at the specified zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at the specified zero-based index. */
    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        save();
        return removedTask;
    }

    /** Marks the task at the specified zero-based index as done and saves the list. */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
        save();
    }

    /** Marks the task at the specified zero-based index as not done and saves the list. */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
        save();
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Writes the current task list to disk, replacing the previous contents.
     * The parent directory is created on the first task-list change.
     */
    private void save() {
        try {
            Files.createDirectories(STORAGE_PATH.getParent());
            Files.write(STORAGE_PATH,
                    tasks.stream().map(Task::toFileFormat).collect(Collectors.toList()),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save tasks to " + STORAGE_PATH, e);
        }
    }
}
