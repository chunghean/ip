package bingusdingus.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests task state transitions and the display/storage representations of task types. */
class TaskTest {
    @Test
    void task_markingChangesStatusAndSerialization() {
        Task task = new Todo("buy milk");
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
        assertEquals("buy milk", task.getDescription());
        assertEquals("T | 0 | buy milk", task.toFileFormat());

        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
        assertEquals("[T][X] buy milk", task.toString());
        assertEquals("T | 1 | buy milk", task.toFileFormat());

        task.markAsNotDone();
        assertFalse(task.isDone());
    }

    @Test
    void deadlineAndEvent_includeDatesInDisplayAndStorageFormats() {
        Deadline deadline = new Deadline("return book", LocalDateTime.of(2019, 6, 6, 0, 0));
        assertEquals("[D][ ] return book (by: Jun 06 2019)", deadline.toString());
        assertEquals("D | 0 | return book | 2019-06-06T00:00", deadline.toFileFormat());

        Event event = new Event("project meeting",
                LocalDateTime.of(2019, 10, 15, 14, 0),
                LocalDateTime.of(2019, 10, 15, 16, 0));
        assertEquals("[E][ ] project meeting (from: Oct 15 2019 2:00 PM to: Oct 15 2019 4:00 PM)", event.toString());
        assertEquals("E | 0 | project meeting | 2019-10-15T14:00 | 2019-10-15T16:00", event.toFileFormat());
    }
}
