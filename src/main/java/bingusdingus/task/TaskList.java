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
        int previousSize = tasks.size();
        tasks.add(task);
        // Adding must append exactly one task and preserve the caller's task object.
        assert tasks.size() == previousSize + 1 && tasks.get(previousSize) == task;
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
        int previousSize = tasks.size();
        Task removedTask = tasks.remove(index);
        // A successful removal changes the list size by exactly one.
        assert tasks.size() == previousSize - 1;
        try {
            save();
        } catch (IllegalStateException e) {
            tasks.add(index, removedTask);
            throw e;
        }
        return removedTask;
    }

    /** Inserts a task at the specified zero-based index and saves the list. */
    public void insert(int index, Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Cannot insert a null task");
        }

        tasks.add(index, task);
        try {
            save();
        } catch (IllegalStateException e) {
            tasks.remove(index);
            throw e;
        }
    }

    /** Marks the task at the specified zero-based index as done and saves the list. */
    public void markAsDone(int index) {
        Task task = tasks.get(index);
        boolean wasDone = task.isDone();
        task.markAsDone();
        // The completed-state mutator must leave the selected task marked done.
        assert task.isDone();
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
        // The incomplete-state mutator must leave the selected task marked not done.
        assert !task.isDone();
        try {
            save();
        } catch (IllegalStateException e) {
            if (wasDone) {
                task.markAsDone();
            }
            throw e;
        }
    }

    /** Sets the completion state of the task at the specified zero-based index and saves the list. */
    public void setDone(int index, boolean isDone) {
        Task task = tasks.get(index);
        boolean wasDone = task.isDone();
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        try {
            save();
        } catch (IllegalStateException e) {
            if (wasDone) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
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
        // Search results must refer only to valid positions in the current list.
        assert keyword != null;
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Integer> matchingIndexes = IntStream.range(0, tasks.size())
                .filter(index -> tasks.get(index).getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .boxed()
                .toList();
        assert matchingIndexes.stream().allMatch(index -> index >= 0 && index < tasks.size());
        return matchingIndexes;
    }

    /**
     * Writes the current task list to disk, replacing the previous contents.
     * The parent directory is created on the first task-list change.
     */
    private void save() {
        storage.save(tasks);
    }
}
