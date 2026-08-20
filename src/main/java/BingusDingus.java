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
                    System.out.println((i + 1) + ".[" + taskList[i].getStatusIcon() + "] "
                            + taskList[i].getDescription());
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

                taskList[textCount] = new Task(command);
                textCount++;
                System.out.println("added: " + command);
                System.out.println("-".repeat(30));
            }
        }
    }
}
