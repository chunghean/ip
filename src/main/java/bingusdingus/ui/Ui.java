package bingusdingus.ui;

import java.util.List;

import bingusdingus.task.Task;
import bingusdingus.task.TaskList;

/** Handles all user-facing output for Bingus Dingus. */
public class Ui {

    /** Displays the application banner and welcome message. */
    public String showWelcome() {
        String banner = "        .-\"\"\"\"-.\n"
                + "       /  o  o  \\\n"
                + "      |    ∆     |     BINGUS\n"
                + "      |  \\___/   |     DINGUS\n"
                + "       \\        /\n"
                + "        '-.__.-'";
        return banner + "\n"
                + "Hey there, I'm Bingus Dingusss.\n"
                + "How can I help ya?";
    }

    /** Displays the goodbye message. */
    public String showGoodbye() {
        return "Bye bye!";
    }

    /** Displays all tasks with their one-based list numbers. */
    public String showTasks(TaskList taskList) {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(taskList.get(i));
        }
        return response.toString();
    }

    /** Displays tasks whose descriptions contain the search keyword. */
    public String showMatchingTasks(List<Task> matchingTasks) {
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(matchingTasks.get(i));
        }
        return response.toString();
    }

    /** Displays an error when a find command has no keyword. */
    public String showMissingFindKeyword() {
        return "Please specify a keyword to search for.";
    }

    /** Displays the result of adding a task. */
    public String showTaskAdded(Task task, int taskCount) {
        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + taskCount + " tasks in the list.";
    }

    /** Displays the result of marking a task as done. */
    public String showTaskMarkedDone(Task task) {
        return "Nice! I've marked this task as done:\n" + task;
    }

    /** Displays the result of marking a task as not done. */
    public String showTaskMarkedNotDone(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /** Displays the result of deleting a task. */
    public String showTaskDeleted(String description, int remainingTasks) {
        return "I've removed this task:\n"
                + "  " + description + "\n"
                + "Now you have " + remainingTasks + " tasks in the list.";
    }

    /** Displays an error for an invalid task number. */
    public String showInvalidTaskNumber() {
        return "Sorry, that task number is invalid.";
    }

    /** Displays an error for a non-numeric task number. */
    public String showInvalidTaskNumberFormat() {
        return "Sorry, please specify a valid task number.";
    }

    /** Displays a message when a task is already done. */
    public String showTaskAlreadyDone() {
        return "That task has already been marked done";
    }

    /** Displays a message when a task is not currently done. */
    public String showTaskNotDone() {
        return "That task has not been marked done yet";
    }

    /** Displays an invalid-command message and the valid task formats. */
    public String showInvalidCommand(String message) {
        return message + "\n"
                + "Use:\ntodo <description>\ndeadline <description> /by <date>, or\nevent <description>"
                + " /from <start> /to <end>.";
    }

    /** Displays an error when the task list cannot be saved. */
    public String showStorageError() {
        return "I couldn't save your tasks. Your latest change was not kept.";
    }
}
