package bingusdingus;

import java.util.Scanner;

import bingusdingus.parser.CommandType;
import bingusdingus.parser.InvalidTaskCommandException;
import bingusdingus.parser.Parser;
import bingusdingus.task.TaskList;
import bingusdingus.ui.Ui;

/** Runs the Bingus Dingus command-line task manager. */
public class BingusDingus {
    private static final String INVALID_COMMAND_MESSAGE = "I've got no idea watchu talkin' about";

    private final TaskList taskList;
    private final Parser parser;
    private final Ui ui;

    /** Creates an application with its parser, task list, and response formatter. */
    public BingusDingus() {
        taskList = new TaskList();
        parser = new Parser();
        ui = new Ui();
    }

    /** Starts the application and processes commands entered by the user. */
    public static void main(String[] args) {
        BingusDingus bingusDingus = new BingusDingus();
        System.out.println(bingusDingus.ui.showWelcome());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(bingusDingus.getResponse(command));
            if (bingusDingus.parser.parseCommandType(command) == CommandType.BYE) {
                break;
            }
        }
    }

    /** Executes a command and returns the response for the graphical interface. */
    public String getResponse(String input) {
        CommandType commandType = parser.parseCommandType(input);

        return switch (commandType) {
            case BYE -> ui.showGoodbye();
            case LIST -> ui.showTasks(taskList);
            case FIND -> handleFindCommand(input);
            case MARK, UNMARK -> handleMarkCommand(input, commandType);
            case DELETE -> handleDeleteCommand(input);
            case TASK -> handleAddCommand(input);
            case UNKNOWN -> ui.showInvalidCommand(INVALID_COMMAND_MESSAGE);
        };
    }

    /** Handles a find command and displays the matching tasks. */
    private String handleFindCommand(String input) {
        String keyword = getCommandArgument(input, "find");
        if (keyword.isEmpty()) {
            return ui.showMissingFindKeyword();
        }
        return ui.showMatchingTasks(taskList, taskList.findIndexes(keyword));
    }

    /** Returns the trimmed argument following a command word. */
    private String getCommandArgument(String input, String command) {
        return input.substring(command.length()).trim();
    }

    private String handleMarkCommand(String input, CommandType commandType) {
        try {
            String command = commandType == CommandType.MARK ? "mark" : "unmark";
            int taskIndex = parseTaskIndex(input, command);
            if (!isValidTaskIndex(taskIndex)) {
                return ui.showInvalidTaskNumber();
            }

            return commandType == CommandType.MARK
                    ? markTaskAsDone(taskIndex)
                    : markTaskAsNotDone(taskIndex);
        } catch (NumberFormatException e) {
            return ui.showInvalidTaskNumberFormat();
        } catch (IllegalStateException e) {
            return ui.showStorageError();
        }
    }

    /** Returns the zero-based task index represented by a command argument. */
    private int parseTaskIndex(String input, String command) {
        return Integer.parseInt(getCommandArgument(input, command)) - 1;
    }

    /** Returns whether a zero-based task index refers to an existing task. */
    private boolean isValidTaskIndex(int taskIndex) {
        return taskIndex >= 0 && taskIndex < taskList.size();
    }

    /** Marks a task as done and returns the corresponding response. */
    private String markTaskAsDone(int taskIndex) {
        if (taskList.get(taskIndex).isDone()) {
            return ui.showTaskAlreadyDone();
        }

        taskList.markAsDone(taskIndex);
        return ui.showTaskMarkedDone(taskList.get(taskIndex));
    }

    /** Marks a task as not done and returns the corresponding response. */
    private String markTaskAsNotDone(int taskIndex) {
        if (!taskList.get(taskIndex).isDone()) {
            return ui.showTaskNotDone();
        }

        taskList.markAsNotDone(taskIndex);
        return ui.showTaskMarkedNotDone(taskList.get(taskIndex));
    }

    private String handleDeleteCommand(String input) {
        try {
            int taskIndex = Integer.parseInt(getCommandArgument(input, "delete")) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                return ui.showInvalidTaskNumber();
            }
            String deletedDescription = taskList.remove(taskIndex).getDescription();
            return ui.showTaskDeleted(deletedDescription, taskList.size());
        } catch (NumberFormatException e) {
            return ui.showInvalidTaskNumberFormat();
        } catch (IllegalStateException e) {
            return ui.showStorageError();
        }
    }

    private String handleAddCommand(String input) {
        try {
            taskList.add(parser.parseTask(input));
            return ui.showTaskAdded(taskList.get(taskList.size() - 1), taskList.size());
        } catch (InvalidTaskCommandException | IllegalStateException e) {
            return e instanceof InvalidTaskCommandException
                    ? ui.showInvalidCommand(e.getMessage()) : ui.showStorageError();
        }
    }
}
