package bingusdingus.task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if (task == null) {
            throw new IllegalArgumentException("Cannot add a null task");
        }
        tasks.add(task);
        try {
            save();
        } catch (IllegalStateException e) {
            tasks.remove(tasks.size() - 1);
            throw e;
        }
    }

    /** Returns the task at the specified zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at the specified zero-based index. */
    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        try {
            save();
        } catch (IllegalStateException e) {
            tasks.add(index, removedTask);
            throw e;
        }
        return removedTask;
    }

    /** Marks the task at the specified zero-based index as done and saves the list. */
    public void markAsDone(int index) {
        Task task = tasks.get(index);
        boolean wasDone = task.isDone();
        task.markAsDone();
        try {
            save();
        } catch (IllegalStateException e) {
            if (!wasDone) {
                task.markAsNotDone();
            }
            throw e;
        }
    }

    /** Marks the task at the specified zero-based index as not done and saves the list. */
    public void markAsNotDone(int index) {
        Task task = tasks.get(index);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            save();
        } catch (IllegalStateException e) {
            if (wasDone) {
                task.markAsDone();
            }
            throw e;
        }
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns the zero-based indexes of tasks whose descriptions contain the keyword. */
    public List<Integer> findIndexes(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return IntStream.range(0, tasks.size())
                .filter(index -> tasks.get(index).getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .boxed()
                .toList();
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
        } catch (IOException | SecurityException e) {
            throw new IllegalStateException("Unable to save tasks to " + STORAGE_PATH, e);
        }
    }

    /** Loads valid tasks from the storage file when it exists. */
    private void load() {
        if (!Files.exists(STORAGE_PATH)) {
            return;
        }

        ArrayList<Task> loadedTasks = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseStoredTask(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
            tasks.addAll(loadedTasks);
        } catch (IOException | SecurityException e) {
            // A damaged or inaccessible storage file should not prevent startup.
            tasks.clear();
        }
    }

    /** Parses one stored task line, returning null for malformed lines. */
    private Task parseStoredTask(String line) {
        String[] parts = normalizeStoredParts(line);
        if (parts == null) {
            return null;
        }

        Task task;
        try {
            task = createStoredTask(parts);
        } catch (DateTimeParseException e) {
            // Invalid typed date/time values are ignored just like other malformed records.
            task = null;
        }

        if (task != null && parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Normalizes a stored record and returns null when its basic structure is invalid. */
    private String[] normalizeStoredParts(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length < 3 || !isValidStatus(parts[1])) {
            return null;
        }

        parts[0] = parts[0].trim().replace("\uFEFF", "");
        for (int i = 1; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    /** Returns whether a stored status represents an incomplete or completed task. */
    private boolean isValidStatus(String status) {
        String normalizedStatus = status.trim();
        return normalizedStatus.equals("0") || normalizedStatus.equals("1");
    }

    /** Creates a task from normalized stored fields, returning null for an invalid record. */
    private Task createStoredTask(String[] parts) {
        switch (parts[0]) {
            case "T":
                return parseStoredTodo(parts);
            case "D":
                return parseStoredDeadline(parts);
            case "E":
                return parseStoredEvent(parts);
            default:
                return null;
        }
    }

    /** Creates a todo task from normalized stored fields. */
    private Task parseStoredTodo(String[] parts) {
        return parts.length == 3 && !parts[2].isBlank() ? new Todo(parts[2]) : null;
    }

    /** Creates a deadline task from normalized stored fields. */
    private Task parseStoredDeadline(String[] parts) {
        return parts.length == 4 && !parts[2].isBlank() && !parts[3].isBlank()
                ? new Deadline(parts[2], parts[3]) : null;
    }

    /** Creates an event task from normalized stored fields. */
    private Task parseStoredEvent(String[] parts) {
        return parts.length == 5 && !parts[2].isBlank() && !parts[3].isBlank() && !parts[4].isBlank()
                ? new Event(parts[2], parts[3], parts[4]) : null;
    }
}
