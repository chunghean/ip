# Bingus Dingus User Guide

Bingus Dingus is a command-line task manager for keeping track of todos,
deadlines, and events. Start the program and enter one command per line.

## Quick start

Try this example session:

```text
todo Buy groceries
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.

deadline Submit report /by Friday
Got it. I've added this task:
  [D][ ] Submit report (by: Friday)
Now you have 2 tasks in the list.

list
Here are the tasks in your list:
1. [T][ ] Buy groceries
2. [D][ ] Submit report (by: Friday)

mark 1
Nice! I've marked this task as done:
[T][X] Buy groceries

bye
Bye bye!
```

The task number is shown on the left when you use `list`. Use that number when
marking, unmarking, or deleting a task.

## Adding tasks

### Todo

Use `todo` followed by a description:

```text
todo Buy groceries
```

Output:

```text
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

### Deadline

Use `/by` to separate the description from the deadline:

```text
deadline Submit report /by Friday
```

Output:

```text
Got it. I've added this task:
  [D][ ] Submit report (by: Friday)
Now you have 1 tasks in the list.
```

### Event

Use `/from` and `/to` to specify the start and end times:

```text
event Team meeting /from Monday 10am /to Monday 11am
```

Output:

```text
Got it. I've added this task:
  [E][ ] Team meeting (from: Monday 10am to Monday 11am)
Now you have 1 tasks in the list.
```

## Managing tasks

### List tasks

Use `list` to display all tasks and their current order:

```text
list
```

```text
Here are the tasks in your list:
1. [T][ ] Buy groceries
2. [D][X] Submit report (by: Friday)
```

`[T]`, `[D]`, and `[E]` identify the task type. `[ ]` means not done and
`[X]` means done.

### Mark a task as done

Use `mark` followed by a task number:

```text
mark 1
```

### Mark a task as not done

Use `unmark` followed by a task number:

```text
unmark 1
```

### Delete a task

Use `delete` followed by a task number:

```text
delete 1
```

The task is removed, and the remaining number of tasks is displayed.

## Invalid commands

Commands must include the required information. For example, a deadline needs
both a description and a date:

```text
deadline Submit report
Deadline requires a description and a date
```

If a command refers to a task number that does not exist, Bingus Dingus shows:

```text
Sorry, that task number is invalid.
```

For `mark`, `unmark`, and `delete`, use a whole-number task index such as `1`
or `2`.

## Exiting the application

Use `bye` to stop the program:

```text
bye
```

Output:

```text
Bye bye!
```
