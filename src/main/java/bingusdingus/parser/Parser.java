package bingusdingus.parser;

import java.time.format.DateTimeParseException;

import bingusdingus.task.Deadline;
import bingusdingus.task.Event;
import bingusdingus.task.Task;
import bingusdingus.task.Todo;

/** Parses user commands into the appropriate task subtype. */
public class Parser {

    private static final String INVALID_COMMAND_MESSAGE = "I've got no idea watchu talkin' about";

    /**
     * Identifies the type of command without performing the requested action.
     *
     * @param command the command entered by the user.
     * @return the command type.
     */
    public CommandType parseCommandType(String command) {
        if (!isWellFormedCommand(command)) {
            return CommandType.UNKNOWN;
        }

        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (command.equals("undo")) {
            return CommandType.UNDO;
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
     * @param command the command entered by the user.
     * @return the task represented by the command.
     * @throws InvalidTaskCommandException if the command is invalid.
     */
    public Task parseTask(String command) throws InvalidTaskCommandException {
        if (!isWellFormedCommand(command)) {
            throw new InvalidTaskCommandException(INVALID_COMMAND_MESSAGE);
        }

        if (command.startsWith("todo ")) {
            return parseTodo(command);
        }

        if (command.startsWith("deadline ")) {
            return parseDeadline(command);
        }

        if (command.startsWith("event ")) {
            return parseEvent(command);
        }

        throw new InvalidTaskCommandException(INVALID_COMMAND_MESSAGE);
    }

    /** Returns whether a command uses the supported spacing and whitespace conventions. */
    private boolean isWellFormedCommand(String command) {
        return command != null
                && !command.isBlank()
                && command.equals(command.trim())
                && !command.contains("  ")
                && !command.contains("\t")
                && !command.contains("\n")
                && !command.contains("\r");
    }

    /** Parses a todo command into a todo task. */
    private Task parseTodo(String command) throws InvalidTaskCommandException {
        String description = command.substring(5).trim();
        if (description.isEmpty()) {
            throw new InvalidTaskCommandException("what todo?");
        }
        try {
            return new Todo(description);
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskCommandException(e.getMessage());
        }
    }

    /** Parses a deadline command into a deadline task. */
    private Task parseDeadline(String command) throws InvalidTaskCommandException {
        String remainder = command.substring(9);
        int byParameterCount = countOccurrences(remainder, "/by");
        if (byParameterCount > 1) {
            throw new InvalidTaskCommandException("deadline requires exactly one /by parameter");
        }
        String[] parts = remainder.split("/by", -1);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new InvalidTaskCommandException("deadline requires a description and a date");
        }
        try {
            return new Deadline(parts[0].trim(), parts[1].trim());
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new InvalidTaskCommandException("deadline date/time must use yyyy-mm-dd or d/M/yyyy HHmm");
        }
    }

    /** Parses an event command into an event task. */
    private Task parseEvent(String command) throws InvalidTaskCommandException {
        String remainder = command.substring(6);
        int fromParameterCount = countOccurrences(remainder, "/from");
        int toParameterCount = countOccurrences(remainder, "/to");
        if (fromParameterCount > 1 || toParameterCount > 1) {
            throw new InvalidTaskCommandException("event requires exactly one /from and one /to parameter");
        }
        String[] fromParts = remainder.split("/from", -1);
        String[] toParts = fromParts.length == 2 ? fromParts[1].split("/to", -1) : new String[0];
        if (fromParts.length != 2 || toParts.length != 2
                || fromParts[0].trim().isEmpty() || toParts[0].trim().isEmpty()
                || toParts[1].trim().isEmpty()) {
            throw new InvalidTaskCommandException("event requires a description, start, and end");
        }
        try {
            return new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new InvalidTaskCommandException("event date/time must use yyyy-mm-dd or d/M/yyyy HHmm");
        }
    }

    /** Returns the number of occurrences of a parameter marker in the supplied text. */
    private int countOccurrences(String text, String marker) {
        int count = 0;
        int position = 0;
        while ((position = text.indexOf(marker, position)) >= 0) {
            count++;
            position += marker.length();
        }
        return count;
    }
}
