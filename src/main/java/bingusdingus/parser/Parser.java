package bingusdingus.parser;

import bingusdingus.task.Deadline;
import bingusdingus.task.Event;
import bingusdingus.task.Task;
import bingusdingus.task.Todo;

import java.time.format.DateTimeParseException;

/** Parses user commands into the appropriate task subtype. */
public class Parser {

    /**
     * Identifies the type of command without performing the requested action.
     *
     * @param command the command entered by the user
     * @return the command type
     */
    public CommandType parseCommandType(String command) {
        if (command == null) {
            return CommandType.UNKNOWN;
        }

        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (command.startsWith("find ")) {
            return CommandType.FIND;
        } else if (command.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (command.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (command.startsWith("todo ")
                || command.startsWith("deadline ")
                || command.startsWith("event ")) {
            return CommandType.TASK;
        }

        return CommandType.UNKNOWN;
    }

    /**
     * Parses a task command and creates the corresponding task subtype.
     *
     * @param command the command entered by the user
     * @return the task represented by the command
     * @throws InvalidTaskCommandException if the command is invalid
     */
    public Task parseTask(String command) throws InvalidTaskCommandException {
        if (command == null) {
            throw new InvalidTaskCommandException("I've got no idea watchu talkin' about");
        }

        if (command.startsWith("todo ")) {
            String description = command.substring(5).trim();
            if (description.isEmpty()) {
                throw new InvalidTaskCommandException("what todo?");
            }
            return new Todo(description);
        }

        if (command.startsWith("deadline ")) {
            String[] parts = command.substring(9).trim().split("/by", 2);
            if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new InvalidTaskCommandException("deadline requires a description and a date");
            }
            try {
                return new Deadline(parts[0].trim(), parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new InvalidTaskCommandException("deadline date/time must use yyyy-mm-dd or d/M/yyyy HHmm");
            }
        }

        if (command.startsWith("event ")) {
            String[] fromParts = command.substring(6).trim().split("/from", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split("/to", 2) : new String[0];
            if (fromParts.length != 2 || toParts.length != 2
                    || fromParts[0].trim().isEmpty() || toParts[0].trim().isEmpty()
                    || toParts[1].trim().isEmpty()) {
                throw new InvalidTaskCommandException("event requires a description, start, and end");
            }
            try {
                return new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
            } catch (DateTimeParseException e) {
                throw new InvalidTaskCommandException("event date/time must use yyyy-mm-dd or d/M/yyyy HHmm");
            }
        }

        throw new InvalidTaskCommandException("I've got no idea watchu talkin' about");
    }
}
