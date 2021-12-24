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
    static String getInput(List<String> valuesList) {
        String choice;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.next();
            } catch (InputMismatchException exception) {
                System.out.println("Enter a valid number input");
                System.out.print("Wish to continue?\n1. Yes\n2. Go back\nEnter your input: ");
                if (UtilityClass.getInput(1, 2) == 2) {
                    return null;
                }
                return getInput(valuesList);
            }

            if (valuesList.contains(choice)) {
                return choice;
            } else {
                System.out.println("Invalid input");
                System.out.print("Wish to continue?\n1. Yes\n2. Go back\nEnter your input: ");
                if (UtilityClass.getInput(1, 2) == 2) {
                    return null;
                }
            }
        }
    }
}
