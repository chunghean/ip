import bingusdingus.parser.CommandType;
import bingusdingus.parser.InvalidTaskCommandException;
import bingusdingus.parser.Parser;
import bingusdingus.task.TaskList;
import bingusdingus.ui.Ui;

import java.util.Scanner;

/** Runs the Bingus Dingus command-line task manager. */
public class BingusDingus {
    /** Starts the application and processes commands entered by the user. */
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        Parser parser = new Parser();
        Ui ui = new Ui();
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            CommandType commandType = parser.parseCommandType(command);

            if (commandType == CommandType.BYE) {
                ui.showGoodbye();
                break;
            }

            else if (commandType == CommandType.LIST) {
                ui.showTasks(taskList);
            }

            else if (commandType == CommandType.MARK || commandType == CommandType.UNMARK) {
                boolean markingDone = commandType == CommandType.MARK;
                String taskNumberText = command.substring(markingDone ? 5 : 7).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumberText) - 1;
                    if (taskIndex < 0 || taskIndex >= taskList.size()) {
                        ui.showInvalidTaskNumber();
                    } else if (markingDone) {
                        if (taskList.get(taskIndex).isDone()) {
                            ui.showTaskAlreadyDone();
                        }
                        else {
                            try {
                                taskList.markAsDone(taskIndex);
                                ui.showTaskMarkedDone(taskList.get(taskIndex));
                            } catch (IllegalStateException e) {
                                ui.showStorageError();
                            }
                        }
                    } else {
                        if (!taskList.get(taskIndex).isDone()) {
                            ui.showTaskNotDone();
                        }
                        else {
                            try {
                                taskList.markAsNotDone(taskIndex);
                                ui.showTaskMarkedNotDone(taskList.get(taskIndex));
                            } catch (IllegalStateException e) {
                                ui.showStorageError();
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    ui.showInvalidTaskNumberFormat();
                }
                ui.showSeparator();
            }

            else if (commandType == CommandType.DELETE) {
                String taskNumberText = command.substring(7).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumberText) - 1;
                    if (taskIndex < 0 || taskIndex >= taskList.size()) {
                        ui.showInvalidTaskNumber();
                    } else {
                        try {
                            String deletedDescription = taskList.remove(taskIndex).getDescription();
                            ui.showTaskDeleted(deletedDescription, taskList.size());
                        } catch (IllegalStateException e) {
                            ui.showStorageError();
                        }
                    }
                } catch (NumberFormatException e) {
                    ui.showInvalidTaskNumberFormat();
                }
            }

            else if (commandType == CommandType.TASK) {
                try {
                    taskList.add(parser.parseTask(command));
                } catch (InvalidTaskCommandException e) {
                    ui.showInvalidCommand(e.getMessage());
                    continue;
                } catch (IllegalStateException e) {
                    ui.showStorageError();
                    continue;
                }

                ui.showTaskAdded(taskList.get(taskList.size() - 1), taskList.size());
            }

            else {
                ui.showInvalidCommand("I've got no idea watchu talkin' about");
            }
        }
    }

}
