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

        // Initialize array to store user text
        String[] textList = new String[100];
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
                for (int i = 0; i < textCount; i++) {
                    System.out.println((i + 1) + ". " + textList[i]);
                }
                System.out.println("-".repeat(30));
            }

            else {
                if (textCount >= textList.length) {
                    System.out.println("Sorry, you can only store 100 tasks.");
                    System.out.println("-".repeat(30));
                    continue;
                }

                textList[textCount] = command;
                textCount++;
                System.out.println("added: " + command);
                System.out.println("-".repeat(30));
            }
        }
    }
}
