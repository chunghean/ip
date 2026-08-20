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
