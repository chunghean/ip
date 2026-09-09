package bingusdingus.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

/** Stores and manages the tasks created by Bingus Dingus. */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final TaskStorage storage;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
        storage = new TaskStorage();
        tasks.addAll(storage.load());
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
        storage.save(tasks);
    }
}
