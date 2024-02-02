import java.io.*;
import java.util.Scanner;

public class Main {
    // first line -> regex pattern: word (letters only), space, word (alphanumeric characters)
    public static Boolean isNameValid(String nameString) {
        if (nameString.matches("^[a-zA-Z]+[\\s][\\w]+$")) {
            return true;
        } else {
            System.out.println("\nThis entry will not be written to output file:");
            System.out.printf("Student name: %s", nameString);
            System.out.println("\nReason: Student name format invalid");
            System.out.println("Correct format: Word (letters only), space, word (alphanumeric characters)");
            return false;
        }
    }

    // second line -> confirm next line contains number between 1 and 8 (incl.)
    public static Boolean isClassCountValid(String classString, String studentName) {
        try {
            int classCount = Integer.parseInt(classString);
            if (classCount < 1 || classCount > 8) {
                System.out.println("\nThis entry will not be written to output file:");
                System.out.printf("Student name: %s", studentName);
                System.out.println("\nReason: Number of classes must be between 1 and 8 inclusive");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("\nThis entry will not be written to output file:");
            System.out.printf("Student name: %s", studentName);
            System.out.println("\nReason: Second line is not a number\n");
            return false;
        }
    }

    // third line -> regex pattern: (number) 2, (number) 0-4, letter, letter, letter(?), (number) 1-200
    public static Boolean isStudentNumberValid(String studentCode, String studentName) {
        int year;
        try {
            year = Integer.parseInt(studentCode.substring(0, 2));
        } catch (Exception e) {
            studentNumberPrintError(studentName, studentCode);
            return false;
        }

        String courseCode;
        int studentID;
        String studentIdStr;
        if (studentCode.length() > 4) {
            if (Character.isLetter(studentCode.charAt(4))) {
                courseCode = studentCode.substring(2, 5);
                studentIdStr = studentCode.substring(5);
            } else {
                courseCode = studentCode.substring(2, 4);
                studentIdStr = studentCode.substring(4);
            }
        } else {
            studentNumberPrintError(studentName, studentCode);
            return false;
        }

        // check if courseCode string only contains letters
        char[] chars = courseCode.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                studentNumberPrintError(studentName, studentCode);
                return false;
            }
        }

        try {
            studentID = Integer.parseInt(studentIdStr);
            if (studentID < 1 || studentID > 200) {
                studentNumberPrintError(studentName, studentCode);
                return false;
            }
        } catch (Exception e) {
            studentNumberPrintError(studentName, studentCode);
            return false;
        }

        if (year >= 20 && year <= 24) {
            return true;
        } else {
            studentNumberPrintError(studentName, studentCode);
            return false;
        }
    }

    public static void studentNumberPrintError(String studentName, String studentCode) {
        System.out.println("\nThis entry will not be written to output file:");
        System.out.printf("Student name: %s", studentName);
        System.out.printf("\nReason: Student number format invalid (%s)", studentCode);
        System.out.println("\nCorrect format: Last two digits of a year (2020-2024) + course type (MSC, DIP, etc.) + ID number between 1 and 200 (incl.)");
    }
    public static boolean readFromFile(String inFilename, String outFilename) {
        String line;
        String studentName = "";
        String classCountString = "";
        String studentNumber;
        int lineCounter = 0;
        boolean isInfoValid = false;
        try (BufferedReader inFileReader = new BufferedReader(new FileReader(inFilename))) {
            while ((line = inFileReader.readLine()) != null) {
                lineCounter += 1;
                if (lineCounter % 3 == 1) {
                    // first line -> regex pattern: word (letters only), space, word (alphanumeric characters)
                    isInfoValid = isNameValid(line);
                    studentName = line;
                } else if (lineCounter % 3 == 2 && isInfoValid) {
                    // second line -> confirm next line contains number between 1 and 8 (incl.)
                    isInfoValid = isClassCountValid(line, studentName);
                    classCountString = line;
                } else if (lineCounter % 3 == 0 && isInfoValid) {
                    // third line -> regex pattern: (number) 2, (number) 0-4, letter, letter, letter(?), (number) 1-200
                    isInfoValid = isStudentNumberValid(line, studentName);
                    studentNumber = line;
                    if (isInfoValid) {
                        writeToFile(outFilename, studentName, classCountString, studentNumber);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Input file access error!");
            return false;
        }

        return true;
    }

    public static void readFromConsole(Scanner scanner, String outFilename) {
        // consume any newline characters left in scanner buffer
        scanner.nextLine();

        System.out.println("**********************************************************************");
        System.out.println("STUDENT DATABASE - MANUAL STUDENT ENTRY");
        System.out.println("**********************************************************************");

        while (true) {
            System.out.println();
            System.out.println("Enter student details or submit an empty line to return to main menu.");
            if (!getDetailsFromConsole(scanner, outFilename)) {
                break;
            }
        }
    }

    public static boolean getDetailsFromConsole(Scanner scanner, String outFilename) {
        String userInput, studentName, classCountString, studentNumber;

        System.out.print("\nStudent's name: ");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
            return false;
        } else if (!isNameValid(userInput)) {
            return true;
        } else {
            studentName = userInput;
        }

        System.out.print("Number of classes: ");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
            return false;
        } else if (!isClassCountValid(userInput, studentName)) {
            return true;
        } else {
            classCountString = userInput;
        }

        System.out.print("Student's ID number: ");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
            return false;
        }
        else if (!isStudentNumberValid(userInput, studentName)) {
            return true;
        } else {
            studentNumber = userInput;
        }

        writeToFile(outFilename, studentName, classCountString, studentNumber);

        return true;
    }

    public static void writeToFile(String outFilename, String studentName, String yearString, String studentNumber) {
        // write student details to output file (append mode)
        try (BufferedWriter outFileReader = new BufferedWriter(new FileWriter(outFilename, true))) {
            outFileReader.write(studentNumber + " - " + studentName.split(" ")[1] + "\n");
            String workloadStr = "";
            int classCount = Integer.parseInt(yearString);
            if (classCount == 1) {
                workloadStr = "Very Light";
            } else if (classCount == 2) {
                workloadStr = "Light";
            } else if (classCount > 2 && classCount < 6) {
                workloadStr = "Part Time";
            } else if (classCount >= 6) {
                workloadStr = "Full Time";
            }
            outFileReader.write(workloadStr + "\n");
        } catch (IOException e) {
            System.out.println("Error: Output file access error!");
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inFilename = "students.txt";
        String outFilename = "status.txt";
        int choice;
        boolean isUpdated = false;

        while (true) {
            System.out.println();
            System.out.println("**********************************************************************");
            System.out.println("STUDENT DATABASE - MAIN MENU");
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
                System.out.printf("Updating \"%s\" file... \n", outFilename);
                isUpdated = readFromFile(inFilename, outFilename);
            } else if (choice == 2) {
                readFromConsole(scanner, outFilename);
            }
            if (isUpdated) {
                System.out.println("\nUpdate complete!");
            } else {
                System.out.println("\nUpdate not performed!");
            }

        }
    }
}