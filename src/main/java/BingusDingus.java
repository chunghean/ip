import java.util.Scanner;

public class BingusDingus {
    public static void main(String[] args) {
        String banner = "        .-\"\"\"\"-.\n"
                + "       /  o  o  \\\n"
                + "      |    ∆     |     BINGUS\n"
                + "      |  \\___/   |     DINGUS\n"
                + "       \\        /\n"
                + "        '-.__.-'";
        System.out.println(banner);
        System.out.println("-".repeat(30));
        System.out.println("Hey there, I'm Bingus Dingusss.");
        System.out.println("How can I help ya?");
        System.out.println("-".repeat(30));

        Task[] taskList = new Task[100];
        int textCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye bye!");
                System.out.println("-".repeat(30));
                break;
            }

            else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < textCount; i++) {
                    System.out.println((i + 1) + ". " + taskList[i]);
                }
                System.out.println("-".repeat(30));
            }

            else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                boolean markingDone = command.startsWith("mark ");
                String taskNumberText = command.substring(markingDone ? 5 : 7).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumberText) - 1;
                    if (taskIndex < 0 || taskIndex >= textCount) {
                        System.out.println("Sorry, that task number is invalid.");
                    } else if (markingDone) {
                        taskList[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  [X] " + taskList[taskIndex].getDescription());
                    } else {
                        if (!taskList[taskIndex].isDone()) {
                            System.out.println("That task has not been marked done yet");
                        }
                        else {
                            taskList[taskIndex].markAsNotDone();
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  [ ] " + taskList[taskIndex].getDescription());
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Sorry, please specify a valid task number.");
                }
                System.out.println("-".repeat(30));
            }

            else {
                if (textCount >= taskList.length) {
                    System.out.println("Sorry, you can only store 100 tasks.");
                    System.out.println("-".repeat(30));
                    continue;
                }

                try {
                    taskList[textCount] = createTask(command);
                } catch (InvalidTaskCommandException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Use: todo <description>, deadline <description> /by <date>, or event <description> /from <start> /to <end>.");
                    System.out.println("-".repeat(30));
                    continue;
                }

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + taskList[textCount]);
                textCount++;
                System.out.println("Now you have " + textCount + " tasks in the list.");
                System.out.println("-".repeat(30));
            }
        }
    }

    /** Parses a task command and creates the corresponding task subtype. */
    private static Task createTask(String command) throws InvalidTaskCommandException {
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
                throw new InvalidTaskCommandException("Deadline requires a description and a date");
            }
            return new Deadline(parts[0].trim(), parts[1].trim());
        }

        if (command.startsWith("event ")) {
            String[] fromParts = command.substring(6).trim().split("/from", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split("/to", 2) : new String[0];
            if (fromParts.length != 2 || toParts.length != 2
                    || fromParts[0].trim().isEmpty() || toParts[0].trim().isEmpty()
                    || toParts[1].trim().isEmpty()) {
                throw new InvalidTaskCommandException("Event requires a description, start, and end");
            }
            return new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
        }

        throw new InvalidTaskCommandException("I've got no idea watchu talkin' about");
    }
}
