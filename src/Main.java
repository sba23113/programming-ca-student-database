// Github link:
// https://github.com/sba23113/programming-ca-student-database

import java.io.*;
import java.util.Scanner;

public class Main {
    public static Boolean isNameValid(String studentName) {
        // first line -> check name format (regex pattern: word (letters only), space, word (alphanumeric characters))
        if (studentName.matches("^[a-zA-Z]+[\\s][\\w]+$")) {
            return true;
        } else {
            System.out.println("\nThis entry will not be written to output file:");
            System.out.printf("Student's name:    %s", studentName);
            System.out.println("\nReason:            name format invalid");
            System.out.println("Correct format:    word (letters only) + space + word (alphanumeric characters)");
            return false;
        }
    }

    public static Boolean isClassCountValid(String classString, String studentName) {
        // second line -> confirm line consists of a number between 1 and 8 (incl.)
        try {
            int classCount = Integer.parseInt(classString);
            if (classCount < 1 || classCount > 8) {
                // if given number is outside allowed range
                System.out.println("\nThis entry will not be written to output file:");
                System.out.printf("Student's name:  %s", studentName);
                System.out.println("\nReason:          number of classes must be between 1 and 8 (incl.)");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            //  if given value is not a number
            System.out.println("\nThis entry will not be written to output file:");
            System.out.printf("Student's name:  %s", studentName);
            System.out.printf("\nReason:          given value is not a number (%s)\n", classString);
            return false;
        }
    }

    public static Boolean isStudentNumberValid(String studentCode, String studentName) {
        // third line -> check student number format (regex pattern: (number) 2, (number) 0-4, 2-3 letters, (number) 1-200)

        // check if first two characters represent the last two digits of years 2020-2024
        int year;
        try {
            year = Integer.parseInt(studentCode.substring(0, 2));
            if (year < 20 || year > 24) {
                studentNumberPrintError(studentName, studentCode);
                return false;
            }
        } catch (Exception e) {
            studentNumberPrintError(studentName, studentCode);
            return false;
        }

        // course code part of validation
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

        return true;
    }

    public static void studentNumberPrintError(String studentName, String studentCode) {
        System.out.println("\nThis entry will not be written to output file:");
        System.out.printf("Student's name:    %s", studentName);
        System.out.printf("\nReason:            student number format invalid (%s)", studentCode);
        System.out.println("\nCorrect format:    last two digits of a year (2020-2024) + course type (2-3 letter abbreviation: MSC, DIP, etc.) + ID number between 1 and 200 (incl.)");
        System.out.println("Example:           24DIP123");
    }
    public static boolean readFromFile(String inFilename, String outFilename) {
        String line;
        String studentName = "";
        String classCountString = "";
        String studentNumber;
        int lineCounter = 0;
        boolean isDataValid = false;
        
        // read input file
        try (BufferedReader inFileReader = new BufferedReader(new FileReader(inFilename))) {
            // iterate through the file line by line until end reached
            while ((line = inFileReader.readLine()) != null) {
                lineCounter += 1;

                // first line of the block (3 lines per student) -> check name format (regex pattern: word (letters only), space, word (alphanumeric characters))
                if (lineCounter % 3 == 1) {
                    isDataValid = isNameValid(line);
                    studentName = line;
                } else if (lineCounter % 3 == 2 && isDataValid) {
                    // second line -> confirm line consists of a number between 1 and 8 (incl.)
                    isDataValid = isClassCountValid(line, studentName);
                    classCountString = line;
                } else if (lineCounter % 3 == 0 && isDataValid) {
                    // third line -> check student number format (regex pattern: (number) 2, (number) 0-4, 2-3 letters, (number) 1-200)
                    isDataValid = isStudentNumberValid(line, studentName);
                    studentNumber = line;
                    // if format is valid -> write to output file
                    if (isDataValid) {
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

        System.out.print("Number of classes (1-8): ");
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
        } else if (!isStudentNumberValid(userInput, studentName)) {
            return true;
        } else {
            studentNumber = userInput;
        }

        writeToFile(outFilename, studentName, classCountString, studentNumber);

        return true;
    }

    public static void writeToFile(String outFilename, String studentName, String classCountString, String studentNumber) {
        // write student details to output file (append mode)
        try (BufferedWriter outFileReader = new BufferedWriter(new FileWriter(outFilename, true))) {
            outFileReader.write(studentNumber + " - " + studentName.split(" ")[1] + "\n");
            String workloadStr = "";
            int classCount = Integer.parseInt(classCountString);
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
            System.out.printf("\n\"%s\" entry added to output file!\n", studentName);
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

                if (isUpdated) {
                    System.out.println("\nUpdate complete!");
                } else {
                    System.out.println("\nUpdate not performed!");
                }
            } else if (choice == 2) {
                readFromConsole(scanner, outFilename);
            }
        }
    }
}