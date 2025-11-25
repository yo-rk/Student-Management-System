import java.util.InputMismatchException;
import java.util.Scanner;

class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

class Student {
    private int rollNumber;
    private String studentName;
    private int[] marks;

    public Student(int rollNumber, String studentName, int[] marks) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.marks = marks;
    }

    public void validateMarks() throws InvalidMarksException {
        if (marks == null) {
            throw new InvalidMarksException("Marks array is null for roll number: " + rollNumber);
        }
        for (int i = 0; i < marks.length; i++) {
            int m = marks[i];
            if (m < 0 || m > 100) {
                throw new InvalidMarksException("Invalid marks for subject " + (i + 1) + ": " + m);
            }
        }
    }

    public double calculateAverage() {
        int sum = 0;
        for (int m : marks) sum += m;
        return sum / (double) marks.length;
    }

    public String getResultStatus() {
        for (int m : marks) {
            if (m < 40) return "Fail";
        }
        return "Pass";
    }

    public void displayResult() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Student Name: " + studentName);
        System.out.print("Marks: ");
        for (int i = 0; i < marks.length; i++) {
            System.out.print(marks[i] + (i < marks.length - 1 ? " " : ""));
        }
        System.out.println();
        System.out.println("Average: " + calculateAverage());
        System.out.println("Result: " + getResultStatus());
    }

    public int getRollNumber() {
        return rollNumber;
    }
}

public class ResultManager {
    private static final int MAX_STUDENTS = 100;
    private Student[] students;
    private int studentCount;
    private Scanner scanner;

    public ResultManager() {
        this.students = new Student[MAX_STUDENTS];
        this.studentCount = 0;
        this.scanner = new Scanner(System.in);
    }

    public void addStudent() throws InvalidMarksException {
        try {
            System.out.print("Enter Roll Number: ");
            String rollLine = scanner.nextLine().trim();
            if (rollLine.isEmpty()) {
                System.out.println("Error: Roll number cannot be empty. Returning to main menu...");
                return;
            }
            int roll = Integer.parseInt(rollLine);

            if (findStudentIndexByRoll(roll) != -1) {
                System.out.println("Error: Student with roll number " + roll + " already exists. Returning to main menu...");
                return;
            }

            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: Student name cannot be empty. Returning to main menu...");
                return;
            }

            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter marks for subject " + (i + 1) + ": ");
                String markLine = scanner.nextLine().trim();
                if (markLine.isEmpty()) {
                    System.out.println("Error: Marks cannot be empty. Returning to main menu...");
                    return;
                }
                try {
                    marks[i] = Integer.parseInt(markLine);
                } catch (NumberFormatException nfe) {
                    System.out.println("Error: Invalid number format for marks: '" + markLine + "'. Returning to main menu...");
                    return;
                }
            }

            Student s = new Student(roll, name, marks);
            s.validateMarks();

            if (studentCount >= MAX_STUDENTS) {
                System.out.println("Error: Student storage full. Cannot add more students.");
                return;
            }
            students[studentCount++] = s;
            System.out.println("Student added successfully. Returning to main menu...");

        } catch (NumberFormatException e) {
            System.out.println("Input error: expected integer value. Returning to main menu...");
        } catch (InputMismatchException ime) {
            System.out.println("Input mismatch error. Returning to main menu...");
            scanner.nextLine();
        }
    }

    public void showStudentDetails() {
        try {
            System.out.print("Enter Roll Number to search: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Error: Roll number cannot be empty. Returning to main menu...");
                return;
            }
            int roll = Integer.parseInt(line);
            int idx = findStudentIndexByRoll(roll);
            if (idx == -1) {
                System.out.println("Student with roll number " + roll + " not found. Returning to main menu...");
                return;
            }
            Student s = students[idx];
            s.displayResult();
            System.out.println("Search completed.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid roll number format. Returning to main menu...");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private int findStudentIndexByRoll(int roll) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getRollNumber() == roll) return i;
        }
        return -1;
    }

    public void mainMenu() {
        try {
            while (true) {
                System.out.println();
                System.out.println("===== Student Result Management System =====");
                System.out.println("1. Add Student");
                System.out.println("2. Show Student Details");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                String choiceLine = scanner.nextLine().trim();
                if (choiceLine.isEmpty()) {
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                    continue;
                }

                int choice;
                try {
                    choice = Integer.parseInt(choiceLine);
                } catch (NumberFormatException nfe) {
                    System.out.println("Please enter a numeric choice (1-3).");
                    continue;
                }

                switch (choice) {
                    case 1:
                        try {
                            addStudent();
                        } catch (InvalidMarksException ime) {
                            System.out.println("Error: " + ime.getMessage() + " Returning to main menu...");
                        }
                        break;
                    case 2:
                        showStudentDetails();
                        break;
                    case 3:
                        System.out.println("Exiting program. Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select 1-3.");
                }
            }
        } finally {
            if (scanner != null) scanner.close();
            System.out.println("Scanner closed. Program terminated.");
        }
    }

    public static void main(String[] args) {
        ResultManager rm = new ResultManager();
        rm.mainMenu();
    }
}
