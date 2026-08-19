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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(command);
            System.out.println("-".repeat(30));

            if (command.equals("bye")) {
                System.out.println("Bye bye!");
                System.out.println("-".repeat(30));
                break;
            }
        }
    }
}
