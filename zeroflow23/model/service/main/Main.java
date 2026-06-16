package model.service.main;

import model.Student;
import model.service.GradeBookService;

import java.util.Scanner;

public class Main {
    private static GradeBookService gradeBookService = new GradeBookService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== КОНСОЛЬНАЯ СИСТЕМА СТУДЕНЧЕСКИХ ОЦЕНОК ===\n");

        while (true) {
            printMenu();
            int choice = readIntInput("Выберите действие: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addSubject();
                    break;
                case 3:
                    addGrade();
                    break;
                case 4:
                    getAverage();
                    break;
                case 5:
                    getStudentGPA();
                    break;
                case 6:
                    getSubjectsByStudent();
                    break;
                case 7:
                    getGradesBySubject();
                    break;
                case 8:
                    getStudentOverallAverage();
                    break;
                case 9:
                    getStudentsWithGPAAbove();
                    break;
                case 10:
                    getStudentWithHighestGPA();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Добавить студента");
        System.out.println("2. Добавить предмет");
        System.out.println("3. Добавить оценку");
        System.out.println("4. Средний балл по предмету");
        System.out.println("5. GPA студента");
        System.out.println("6. Предметы студента");
        System.out.println("7. Все оценки по предмету");
        System.out.println("8. Общий средний балл студента");
        System.out.println("9. Студенты с GPA выше N");
        System.out.println("10. Студент с наибольшим средним баллом");
        System.out.println("0. Выход");
    }

    private static void addStudent() {
        System.out.println("\n--- Добавление студента ---");
        System.out.print("Введите ФИО: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите группу: ");
        String group = scanner.nextLine();

        if (fullName.trim().isEmpty() || group.trim().isEmpty()) {
            System.out.println("Ошибка: ФИО и группа не могут быть пустыми");
            return;
        }

        Student student = new Student(0, fullName, group);
        gradeBookService.addStudent(student);
    }

    private static void addSubject() {
        System.out.println("\n--- Добавление предмета ---");
        System.out.print("Введите код предмета (например, CS101): ");
        String code = scanner.nextLine();
        System.out.print("Введите ID студента (число без STD-): ");
        int studentId = readIntInput("");
        System.out.print("Введите название предмета: ");
        String name = scanner.nextLine();
        System.out.print("Введите количество кредитов: ");
        int credits = readIntInput("");

        gradeBookService.addSubject(code, studentId, name, credits);
    }

    private static void addGrade() {
        System.out.println("\n--- Добавление оценки ---");
        System.out.print("Введите код предмета: ");
        String subjectCode = scanner.nextLine();
        System.out.print("Введите тип (EXAM/TEST/ASSIGNMENT): ");
        String type = scanner.nextLine();
        System.out.print("Введите оценку (0-100): ");
        double score = readDoubleInput();

        gradeBookService.addGrade(subjectCode, type, score);
    }

    private static void getAverage() {
        System.out.println("\n--- Средний балл по предмету ---");
        System.out.print("Введите код предмета: ");
        String subjectCode = scanner.nextLine();
        gradeBookService.getAverage(subjectCode);
    }

    private static void getStudentGPA() {
        System.out.println("\n--- GPA студента ---");
        System.out.print("Введите ID студента (число без STD-): ");
        int studentId = readIntInput("");
        gradeBookService.getStudentGPA(studentId);
    }

    private static void getSubjectsByStudent() {
        System.out.println("\n--- Предметы студента ---");
        System.out.print("Введите ID студента (число без STD-): ");
        int studentId = readIntInput("");
        gradeBookService.getSubjectsByStudent(studentId);
    }

    private static void getGradesBySubject() {
        System.out.println("\n--- Все оценки по предмету ---");
        System.out.print("Введите код предмета: ");
        String subjectCode = scanner.nextLine();
        gradeBookService.getGradesBySubjectCode(subjectCode);
    }

    private static void getStudentOverallAverage() {
        System.out.println("\n--- Общий средний балл студента ---");
        System.out.print("Введите ID студента (число без STD-): ");
        int studentId = readIntInput("");
        gradeBookService.getStudentOverallAverage(studentId);
    }

    private static void getStudentsWithGPAAbove() {
        System.out.println("\n--- Студенты с GPA выше N ---");
        System.out.print("Введите пороговое значение (0-100): ");
        double threshold = readDoubleInput();
        gradeBookService.getStudentsWithGPAAbove(threshold);
    }

    private static void getStudentWithHighestGPA() {
        System.out.println("\n--- Студент с наибольшим средним баллом ---");
        gradeBookService.getStudentWithHighestGPA();
    }

    private static int readIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                int value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Ошибка: значение не может быть отрицательным");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число");
            }
        }
    }

    private static double readDoubleInput() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0 || value > 100) {
                    System.out.println("Ошибка: значение должно быть в диапазоне от 0 до 100");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }
    }
}