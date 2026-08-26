# UI Test Plan

This file defines command-line UI test sessions for Bingus Dingus. Each session is run independently and in document order. A failure stops the test run.

## Test case 1: Add a deadline task

Aim: Verify that a deadline command creates and displays a deadline task with its due date.

Inputs:

```text
deadline return book /by Sunday
bye
```

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
------------------------------
Bye bye!
------------------------------
```

## Test case 2: Add an event task

Aim: Verify that an event command parses the description, start time, and end time.

Inputs:

```text
event project meeting /from Mon 2pm /to 4pm
bye
```

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
------------------------------
Bye bye!
------------------------------
```

## Test case 3: Reject malformed task commands

Aim: Verify that malformed todo, deadline, event, and unknown commands are handled without terminating the application or adding tasks.

Inputs:

```text
todo
deadline return book
event project meeting /from Mon 2pm
unknown command
list
bye
```

Expected output:

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
I've got no idea watchu talkin' about
Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.
------------------------------
Deadline requires a description and a date
Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.
------------------------------
Event requires a description, start, and end
Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.
------------------------------
I've got no idea watchu talkin' about
Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.
------------------------------
Here are the tasks in your list:
------------------------------
Bye bye!
------------------------------
```

## Test case 4: Handle invalid task numbers

Aim: Verify that non-numeric and out-of-range task numbers are rejected without crashing.

Inputs:

```text
todo buy milk
mark abc
mark 2
unmark 0
unmark 1
bye
```

Expected output:

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
Got it. I've added this task:
  [T][ ] buy milk
Now you have 1 tasks in the list.
------------------------------
Sorry, please specify a valid task number.
------------------------------
Sorry, that task number is invalid.
------------------------------
Sorry, that task number is invalid.
------------------------------
That task has not been marked done yet
------------------------------
Bye bye!
------------------------------
```

## Test case 5: Delete a task

Aim: Verify that deleting a task removes it and shifts the remaining task numbers.

Inputs:

```text
todo buy milk
todo return book
delete 1
list
bye
```

Expected output:

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
Got it. I've added this task:
  [T][ ] buy milk
Now you have 1 tasks in the list.
------------------------------
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
------------------------------
I've removed this task:
  buy milk
Now you have 1 tasks in the list.
------------------------------
Here are the tasks in your list:
1. [T][ ] return book
------------------------------
Bye bye!
------------------------------
```

## Test case 6: Save task-list changes to disk

Aim: Verify that adding, completing, and deleting tasks writes the current task list to `./data/duke.txt`.

Inputs:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
delete 3
bye
```

Expected output:

```text
        .-""""-.
       /  o  o  \
      |    ∆     |     BINGUS
      |  \___/   |     DINGUS
       \        /
        '-.__.-'
------------------------------
Hey there, I'm Bingus Dingusss.
How can I help ya?
------------------------------
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
------------------------------
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
------------------------------
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
------------------------------
Nice! I've marked this task as done:
[T][X] read book
------------------------------
I've removed this task:
  project meeting
Now you have 2 tasks in the list.
------------------------------
Bye bye!
------------------------------
```

After the session, `data/duke.txt` should contain:

```text
T | 1 | read book
D | 0 | return book | June 6th
```
