package bingusdingus.task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Saves and loads tasks using the Bingus Dingus storage format. */
public class TaskStorage {
    private static final Path STORAGE_PATH = Path.of(".", "data", "bingusdingus.txt");

    /** Saves the supplied tasks to disk, replacing the previous contents. */
    public void save(List<Task> tasks) {
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

    /** Returns valid tasks loaded from disk, ignoring malformed records. */
    public List<Task> load() {
        if (!Files.exists(STORAGE_PATH)) {
            return List.of();
        }

        ArrayList<Task> loadedTasks = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseStoredTask(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
        } catch (IOException | SecurityException e) {
            // A damaged or inaccessible storage file should not prevent startup.
            return List.of();
        }
        return loadedTasks;
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
