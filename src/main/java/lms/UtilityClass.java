package lms;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class UtilityClass {

    // getting input choice with a min and max range
    static int getInput(int minValue, int maxValue) {
        int choice;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException exception) {
                System.out.println("Enter a valid number input");
                return getInput(minValue, maxValue);
            }

            if (!(minValue > choice || choice > maxValue)) {
                return choice;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    // getting input choice with a given set of values
    static long getInput(List<Long> valuesList) {
        long choice;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextLong();
            } catch (InputMismatchException exception) {
                System.out.println("Enter a valid number input");
                return getInput(valuesList);
            }

            if (valuesList.contains(choice)) {
                return choice;
            } else {
                System.out.println("Invalid input");
            }
        }
    }
}
