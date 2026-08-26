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
        load();
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

    /** Loads valid tasks from the storage file when it exists. */
    private void load() {
        if (!Files.exists(STORAGE_PATH)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseStoredTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load tasks from " + STORAGE_PATH, e);
        }
    }

    /** Parses one stored task line, returning null for malformed lines. */
    private Task parseStoredTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            return null;
        }

        Task task;
        switch (parts[0]) {
        case "T":
            task = parts.length == 3 && !parts[2].isBlank() ? new Todo(parts[2]) : null;
            break;
        case "D":
            task = parts.length == 4 && !parts[2].isBlank() && !parts[3].isBlank()
                    ? new Deadline(parts[2], parts[3]) : null;
            break;
        case "E":
            task = parts.length == 5 && !parts[2].isBlank() && !parts[3].isBlank() && !parts[4].isBlank()
                    ? new Event(parts[2], parts[3], parts[4]) : null;
            break;
        default:
            task = null;
        }

        if (task != null && parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
