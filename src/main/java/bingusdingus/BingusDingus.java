package bingusdingus;

import java.util.Scanner;

import bingusdingus.parser.CommandType;
import bingusdingus.parser.InvalidTaskCommandException;
import bingusdingus.parser.Parser;
import bingusdingus.task.TaskList;
import bingusdingus.ui.Ui;

/** Runs the Bingus Dingus command-line task manager. */
public class BingusDingus {
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

        if (commandType == CommandType.BYE) {
            return ui.showGoodbye();
        } else if (commandType == CommandType.LIST) {
            return ui.showTasks(taskList);
        } else if (commandType == CommandType.FIND) {
            String keyword = input.substring(5).trim();
            return keyword.isEmpty() ? ui.showMissingFindKeyword() : ui.showMatchingTasks(taskList.find(keyword));
        } else if (commandType == CommandType.MARK || commandType == CommandType.UNMARK) {
            return handleMarkCommand(input, commandType);
        } else if (commandType == CommandType.DELETE) {
            return handleDeleteCommand(input);
        } else if (commandType == CommandType.TASK) {
            return handleAddCommand(input);
        }

        return ui.showInvalidCommand("I've got no idea watchu talkin' about");
    }

    private String handleMarkCommand(String input, CommandType commandType) {
        boolean markingDone = commandType == CommandType.MARK;
        String taskNumberText = input.substring(markingDone ? 5 : 7).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumberText) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                return ui.showInvalidTaskNumber();
            } else if (markingDone) {
                if (taskList.get(taskIndex).isDone()) {
                    return ui.showTaskAlreadyDone();
                }
                taskList.markAsDone(taskIndex);
                return ui.showTaskMarkedDone(taskList.get(taskIndex));
            } else if (!taskList.get(taskIndex).isDone()) {
                return ui.showTaskNotDone();
            }
            taskList.markAsNotDone(taskIndex);
            return ui.showTaskMarkedNotDone(taskList.get(taskIndex));
        } catch (NumberFormatException e) {
            return ui.showInvalidTaskNumberFormat();
        } catch (IllegalStateException e) {
            return ui.showStorageError();
        }
    }

    private String handleDeleteCommand(String input) {
        try {
            int taskIndex = Integer.parseInt(input.substring(7).trim()) - 1;
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
