package com.sis.util;

import java.util.Scanner;

/**
 * Utility methods for console input and output formatting.
 */
public final class ConsoleUtils {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int LINE_WIDTH  = 60;

    private ConsoleUtils() {}

    // ---------------------------------------------------------------- output helpers

    public static void printHeader(String title) {
        String border = "=".repeat(LINE_WIDTH);
        System.out.println("\n" + border);
        System.out.printf("  %s%n", title.toUpperCase());
        System.out.println(border);
    }

    public static void printSeparator() {
        System.out.println("-".repeat(LINE_WIDTH));
    }

    public static void printSuccess(String msg) {
        System.out.println("[OK]  " + msg);
    }

    public static void printError(String msg) {
        System.out.println("[ERR] " + msg);
    }

    public static void printInfo(String msg) {
        System.out.println("[i]   " + msg);
    }

    // ---------------------------------------------------------------- input helpers

    public static String prompt(String label) {
        System.out.print(label + ": ");
        return SCANNER.nextLine().trim();
    }

    public static int promptInt(String label) {
        while (true) {
            String raw = prompt(label);
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                printError("Please enter a valid integer.");
            }
        }
    }

    public static double promptDouble(String label) {
        while (true) {
            String raw = prompt(label);
            try {
                return Double.parseDouble(raw);
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    public static int promptMenuChoice(int min, int max) {
        while (true) {
            int choice = promptInt("Enter choice");
            if (choice >= min && choice <= max) return choice;
            printError("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        SCANNER.nextLine();
    }

    public static Scanner getScanner() {
        return SCANNER;
    }
}
