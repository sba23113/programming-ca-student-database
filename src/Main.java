import java.io.*;
import java.util.Scanner;

public class Main {
    public static Boolean readFromFile(String inFilename, String outFilename) {
        try (BufferedReader inFileReader = new BufferedReader(new FileReader(inFilename))) {
            String firstLine = inFileReader.readLine();

            // make sure input file is not empty
            if (firstLine == null) {
                System.out.println("Error: Input file is empty!");
                return true;
            }
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