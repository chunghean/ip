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
  [D] [ ] return book (by: Sunday)
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
  [E] [ ] project meeting (from: Mon 2pm to: 4pm)
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

Expected output for each malformed command:

```text
I've got no idea watchu talkin' about
Todo description cannot be empty
Deadline requires a description and a date
Event requires a description, start, and end
Unknown task command
Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.
```

The `list` command should show no tasks.

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
Sorry, please specify a valid task number.
Sorry, that task number is invalid.
Sorry, that task number is invalid.
That task has not been marked done yet
```
