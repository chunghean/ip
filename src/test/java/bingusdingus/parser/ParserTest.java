package bingusdingus.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bingusdingus.task.Deadline;
import bingusdingus.task.Event;
import bingusdingus.task.Task;
import bingusdingus.task.Todo;


/** Tests command classification and conversion of commands into tasks. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseCommandType_recognizesSupportedCommandsAndRejectsUnknownCommands() {
        assertEquals(CommandType.BYE, parser.parseCommandType("bye"));
        assertEquals(CommandType.LIST, parser.parseCommandType("list"));
        assertEquals(CommandType.FIND, parser.parseCommandType("find book"));
        assertEquals(CommandType.MARK, parser.parseCommandType("mark 1"));
        assertEquals(CommandType.UNMARK, parser.parseCommandType("unmark 1"));
        assertEquals(CommandType.DELETE, parser.parseCommandType("delete 1"));
        assertEquals(CommandType.TASK, parser.parseCommandType("todo buy milk"));
        assertEquals(CommandType.TASK,
                parser.parseCommandType("deadline return book /by 2026-09-02"));
        assertEquals(CommandType.TASK,
                parser.parseCommandType("event meeting /from 2026-09-02 0900 /to 2026-09-02 1000"));
        assertEquals(CommandType.UNKNOWN, parser.parseCommandType(null));
        assertEquals(CommandType.UNKNOWN, parser.parseCommandType("mark"));
        assertEquals(CommandType.UNKNOWN, parser.parseCommandType("unknown command"));
    }

    @Test
    void parseTask_createsEachTaskTypeWithTrimmedValues() throws InvalidTaskCommandException {
        Task todo = parser.parseTask("todo   buy milk  ");
        assertInstanceOf(Todo.class, todo);
        assertEquals("buy milk", todo.getDescription());

        Task deadline = parser.parseTask("deadline return book /by 2/12/2019 1800");
        assertInstanceOf(Deadline.class, deadline);
        assertEquals("[D][ ] return book (by: Dec 02 2019 6:00 PM)", deadline.toString());

        Task event = parser.parseTask("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        assertInstanceOf(Event.class, event);
        assertEquals("[E][ ] project meeting (from: Oct 15 2019 2:00 PM to: Oct 15 2019 4:00 PM)",
                event.toString());
    }

    @Test
    void parseTask_rejectsMissingAndMalformedTaskDetails() {
        InvalidTaskCommandException emptyTodo = assertThrows(
                InvalidTaskCommandException.class, () -> parser.parseTask("todo"));
        assertEquals("I've got no idea watchu talkin' about", emptyTodo.getMessage());

        InvalidTaskCommandException missingDeadline = assertThrows(
                InvalidTaskCommandException.class, () -> parser.parseTask("deadline return book"));
        assertEquals("deadline requires a description and a date", missingDeadline.getMessage());

        InvalidTaskCommandException invalidDate = assertThrows(
                InvalidTaskCommandException.class, () -> parser.parseTask("deadline return book /by not-a-date"));
        assertEquals("deadline date/time must use yyyy-mm-dd or d/M/yyyy HHmm", invalidDate.getMessage());

        InvalidTaskCommandException missingEventPart = assertThrows(
                InvalidTaskCommandException.class, () -> parser.parseTask("event meeting /from 2026-09-02 0900"));
        assertEquals("event requires a description, start, and end", missingEventPart.getMessage());
    }
}
