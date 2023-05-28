import java.util.*;
import java.io.IOException;

@SuppressWarnings("all")

public class Main {
    public static void main(String args[]) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n+-----------------------------------+");
            System.out.println("|        Scheduling Algorithm       |");
            System.out.println("+-----------------------------------+");
            System.out.println("| 1 - Non-preemptive SJF            |");
            System.out.println("| 2 - Non-preemptive Priority       |");
            System.out.println("| 3 - Round Robin                   |");
            System.out.println("| 0 - Exit                          |");
            System.out.println("+-----------------------------------+");
            int role = getInput("Please select an algorithm", 3);

            switch (role) {
                case 1 :
                    SJF.nonPreemptive();
                    pressEnterToContinue();
                    break;
                case 2 :
                    NonPrePriority.run();
                    pressEnterToContinue();
                    break;
                case 3 :
                    RR.run();
                    pressEnterToContinue();
                    break;
                case 0 :
                    exit = exit();
                    break;
            }
        }
    }

    private static int getInput(String question, int options) {
        int choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("\n" + question + " (0-" + options + "): " );

            try {
                choice = input.nextInt();
                if (choice >= 0 && choice <= options) 
                    return choice;
                else {
                    System.err.println("Invalid input, please try again.");
                    input.nextLine();   // discard unwanted line
                }
            }
            catch (InputMismatchException e) {
                System.err.println("Invalid input, please try again.");
                input.nextLine();       // discard unwanted line
            }
        }
    }

    private static void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        try {
            System.in.read();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private static boolean exit() {
        while (true) {
            Scanner input = new Scanner(System.in);
            boolean exit = false;
            System.out.print("\nAre you sure you want to exit? (Y/N): ");
            String yesOrNo = input.next();
            if (yesOrNo.equals("Y") || yesOrNo.equals("y")) {
                exit = true;
                return exit;
            }
            else if (yesOrNo.equals("N") || yesOrNo.equals("n")) {
                return exit;
            }  
            else {
                System.out.println("\nPlease enter \'Y\'or\'y\' if you wish to quit the program, " +
                                "or enter \'N\'or\'n\' if you wish to continue.");
            }
        }
    }

}
