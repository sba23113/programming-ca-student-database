import java.io.*;
import java.util.Scanner;

public class Main {
    // does the string contain a name with this regex pattern: word (letters only), space, word (alphanumeric characters)
    public static Boolean isNameValid(String nameString) {
        if (nameString.matches("^[a-zA-Z]+[\\s][\\w]+$")) {
            System.out.println("First name and last name OK");
            return true;
        } else {
            System.out.println("First name and last name NOT OK");
            return false;
        }
    }

    // does next line contain number between 1 and 8 (incl.)
    public static Boolean isClassCountValid(String classString) {
        try {
            int classCount = Integer.parseInt(classString);
            if (classCount < 1 || classCount > 8) {
                System.out.println("Number of classes must be between 1 and 8 inclusive");
                return false;
            } else {
                System.out.println("Class info OK");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Second line is not a number");
            return false;
        }
    }

    public static Boolean readFromFile(String inFilename, String outFilename) {
        try (BufferedReader inFileReader = new BufferedReader(new FileReader(inFilename))) {
            String firstLine = inFileReader.readLine();

            // make sure input file is not empty
            if (firstLine == null) {
                System.out.println("Error: Input file is empty!");
                return true;
            }

            // first line -> regex pattern: word (letters only), space, word (alphanumeric characters)
            isNameValid(firstLine);

            // second line -> confirm next line contains number between 1 and 8 (incl.)
            String secondLine = inFileReader.readLine();
            isClassCountValid(secondLine);
        } catch (Exception e) {
            System.out.println("Input file access error!");
        }

        return false;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inFilename = "students.txt";
        String outFilename = "status.txt";
        int choice;

        while (true) {
            System.out.println();
            System.out.println("**********************************************************************");
            System.out.println("STUDENT DATABASE");
            System.out.println("**********************************************************************");
            System.out.println();
            System.out.println("Available actions:");
            System.out.println("1: Update \"status.txt\" file using data from \"students.txt\" file");
            System.out.println("2: Enter student details manually");
            System.out.println("0: Exit");
            System.out.println();
            System.out.print("What would you like to do: ");
            choice = scanner.nextInt();
            System.out.println();

            if (choice == 0) {
                break;
            } else if (choice == 1) {
                if (readFromFile(inFilename, outFilename)) {
                    break;
                }
            } else if (choice == 2) {
                System.out.println("choice 2");
            }
        }
    }
}