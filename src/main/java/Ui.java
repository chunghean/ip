/** Handles all user-facing output for Bingus Dingus. */
public class Ui {

    /** Displays the application banner and welcome message. */
    public void showWelcome() {
        String banner = "        .-\"\"\"\"-.\n"
                + "       /  o  o  \\\n"
                + "      |    ∆     |     BINGUS\n"
                + "      |  \\___/   |     DINGUS\n"
                + "       \\        /\n"
                + "        '-.__.-'";
        System.out.println(banner);
        showSeparator();
        System.out.println("Hey there, I'm Bingus Dingusss.");
        System.out.println("How can I help ya?");
        showSeparator();
    }

    /** Displays the goodbye message. */
    public void showGoodbye() {
        System.out.println("Bye bye!");
        showSeparator();
    }

    /** Displays all tasks with their one-based list numbers. */
    public void showTasks(TaskList taskList) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + ". " + taskList.get(i));
        }
        showSeparator();
    }

    /** Displays the result of adding a task. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showSeparator();
    }

    /** Displays the result of marking a task as done. */
    public void showTaskMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /** Displays the result of marking a task as not done. */
    public void showTaskMarkedNotDone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /** Displays the result of deleting a task. */
    public void showTaskDeleted(String description, int remainingTasks) {
        System.out.println("I've removed this task:");
        System.out.println("  " + description);
        System.out.println("Now you have " + remainingTasks + " tasks in the list.");
        showSeparator();
    }

    /** Displays an error for an invalid task number. */
    public void showInvalidTaskNumber() {
        System.out.println("Sorry, that task number is invalid.");
    }

    /** Displays an error for a non-numeric task number. */
    public void showInvalidTaskNumberFormat() {
        System.out.println("Sorry, please specify a valid task number.");
    }

    /** Displays a message when a task is already done. */
    public void showTaskAlreadyDone() {
        System.out.println("That task has already been marked done");
    }

    /** Displays a message when a task is not currently done. */
    public void showTaskNotDone() {
        System.out.println("That task has not been marked done yet");
    }

    /** Displays an invalid-command message and the valid task formats. */
    public void showInvalidCommand(String message) {
        System.out.println(message);
        System.out.println("Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.");
        showSeparator();
    }

    /** Displays an error when the task list cannot be saved. */
    public void showStorageError() {
        System.out.println("I couldn't save your tasks. Your latest change was not kept.");
        showSeparator();
    }

    /** Displays a separator between interactions. */
    public void showSeparator() {
        System.out.println("-".repeat(30));
    }
}
