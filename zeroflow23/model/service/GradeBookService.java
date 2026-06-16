package model.service;

import model.Student;
import model.Subject;
import model.Grade;

import java.util.*;

public class GradeBookService {
    private List<Student> students;
    private Map<String, Subject> subjects;
    private List<Grade> grades;
    private int nextStudentId;
    private int nextGradeId;

    public GradeBookService() {
        students = new ArrayList<>();
        subjects = new HashMap<>();
        grades = new ArrayList<>();
        nextStudentId = 1;
        nextGradeId = 1;
    }

    // ========== Таск 2: Бизнес-логика ==========

    public void addStudent(Student student) {
        if (student == null) {
            System.out.println("Ошибка: студент не может быть null");
            return;
        }
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            System.out.println("Ошибка: ФИО студента не может быть пустым");
            return;
        }
        if (student.getGroup() == null || student.getGroup().trim().isEmpty()) {
            System.out.println("Ошибка: группа студента не может быть пустой");
            return;
        }

        student.setId(nextStudentId++);
        students.add(student);
        System.out.println("Студент добавлен. ID: STD-" + student.getId());
    }

    public void addSubject(String code, int studentId, String name, int credits) {
        if (code == null || code.trim().isEmpty()) {
            System.out.println("Ошибка: код предмета не может быть пустым");
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Ошибка: название предмета не может быть пустым");
            return;
        }
        if (credits <= 0) {
            System.out.println("Ошибка: количество кредитов должно быть положительным");
            return;
        }

        if (subjects.containsKey(code)) {
            System.out.println("Ошибка: предмет с кодом " + code + " уже существует");
            return;
        }

        Student student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Ошибка: студент с ID " + studentId + " не найден");
            return;
        }

        // Проверяем, нет ли уже такого предмета у студента
        for (Subject existingSubject : subjects.values()) {
            if (existingSubject.getStudentId() == studentId && 
                existingSubject.getName().equalsIgnoreCase(name)) {
                System.out.println("Ошибка: у студента " + student.getFullName() + 
                                   " уже есть предмет " + name);
                return;
            }
        }

        Subject subject = new Subject(code, studentId, name, credits);
        subjects.put(code, subject);
        System.out.println("Предмет " + name + " (код: " + code + ") добавлен студенту " + 
                          student.getFullName());
    }

    public void addGrade(String subjectCode, String type, double score) {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            System.out.println("Ошибка: код предмета не может быть пустым");
            return;
        }
        if (score <= 0 || score > 100) {
            System.out.println("Ошибка: оценка должна быть в диапазоне от 0 до 100");
            return;
        }

        Subject subject = subjects.get(subjectCode);
        if (subject == null) {
            System.out.println("Ошибка: предмет с кодом " + subjectCode + " не найден");
            return;
        }

        String upperType = type.toUpperCase();
        if (!upperType.equals("EXAM") && !upperType.equals("TEST") && !upperType.equals("ASSIGNMENT")) {
            System.out.println("Ошибка: тип оценки должен быть EXAM, TEST или ASSIGNMENT");
            return;
        }

        Grade grade = new Grade(
            nextGradeId++,
            subjectCode,
            upperType,
            score,
            "Оценка по предмету " + subject.getName()
        );
        grades.add(grade);
        System.out.println("Оценка " + score + " (" + upperType + ") добавлена по предмету " + 
                          subject.getName());
    }

    public double getAverage(String subjectCode) {
        Subject subject = subjects.get(subjectCode);
        if (subject == null) {
            System.out.println("Ошибка: предмет с кодом " + subjectCode + " не найден");
            return 0.0;
        }

        List<Grade> subjectGrades = getGradesBySubjectCode(subjectCode);
        if (subjectGrades.isEmpty()) {
            System.out.println("Для предмета " + subject.getName() + " нет оценок");
            return 0.0;
        }

        double sum = 0.0;
        for (Grade grade : subjectGrades) {
            sum += grade.getScore();
        }
        double average = sum / subjectGrades.size();
        System.out.printf("Средний балл по предмету %s: %.2f\n", subject.getName(), average);
        return average;
    }

    public double getStudentGPA(int studentId) {
        Student student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Ошибка: студент с ID " + studentId + " не найден");
            return 0.0;
        }

        List<Subject> studentSubjects = getSubjectsByStudent(studentId);
        if (studentSubjects.isEmpty()) {
            System.out.println("У студента " + student.getFullName() + " нет предметов");
            return 0.0;
        }

        double totalWeightedScore = 0.0;
        int totalCredits = 0;

        for (Subject subject : studentSubjects) {
            List<Grade> subjectGrades = getGradesBySubjectCode(subject.getCode());
            if (!subjectGrades.isEmpty()) {
                double avgScore = 0.0;
                for (Grade grade : subjectGrades) {
                    avgScore += grade.getScore();
                }
                avgScore /= subjectGrades.size();
                totalWeightedScore += avgScore * subject.getCredits();
                totalCredits += subject.getCredits();
            }
        }

        if (totalCredits == 0) {
            System.out.println("У студента " + student.getFullName() + " нет оценок");
            return 0.0;
        }

        double gpa = totalWeightedScore / totalCredits;
        System.out.printf("GPA студента %s: %.2f\n", student.getFullName(), gpa);
        return gpa;
    }

    public List<Subject> getSubjectsByStudent(int studentId) {
        Student student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Ошибка: студент с ID " + studentId + " не найден");
            return new ArrayList<>();
        }

        List<Subject> result = new ArrayList<>();
        for (Subject subject : subjects.values()) {
            if (subject.getStudentId() == studentId) {
                result.add(subject);
            }
        }

        if (result.isEmpty()) {
            System.out.println("У студента " + student.getFullName() + " нет предметов");
        } else {
            System.out.println("\n=== Предметы студента " + student.getFullName() + " ===");
            for (Subject subject : result) {
                System.out.println(subject.getCode() + " - " + subject.getName() + 
                                  " (" + subject.getCredits() + " кредитов)");
            }
            System.out.println();
        }
        return result;
    }

    // ========== Таск 3: Аналитика и поиск ==========

    public List<Grade> getGradesBySubjectCode(String subjectCode) {
        Subject subject = subjects.get(subjectCode);
        if (subject == null) {
            System.out.println("Ошибка: предмет с кодом " + subjectCode + " не найден");
            return new ArrayList<>();
        }

        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getSubjectCode().equals(subjectCode)) {
                result.add(grade);
            }
        }

        System.out.println("\n=== Оценки по предмету " + subject.getName() + " ===");
        if (result.isEmpty()) {
            System.out.println("Оценок по этому предмету нет");
        } else {
            for (Grade grade : result) {
                System.out.println("ID: " + grade.getId() + ", Тип: " + grade.getType() + 
                                   ", Оценка: " + grade.getScore() + ", Комментарий: " + grade.getComment());
            }
        }
        System.out.println();
        return result;
    }

    public double getStudentOverallAverage(int studentId) {
        Student student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Ошибка: студент с ID " + studentId + " не найден");
            return 0.0;
        }

        List<Grade> studentGrades = new ArrayList<>();
        List<Subject> studentSubjects = getSubjectsByStudent(studentId);
        
        for (Subject subject : studentSubjects) {
            studentGrades.addAll(getGradesBySubjectCode(subject.getCode()));
        }

        if (studentGrades.isEmpty()) {
            System.out.println("У студента " + student.getFullName() + " нет оценок");
            return 0.0;
        }

        double sum = 0.0;
        for (Grade grade : studentGrades) {
            sum += grade.getScore();
        }
        double average = sum / studentGrades.size();
        System.out.printf("Общий средний балл студента %s: %.2f\n", student.getFullName(), average);
        return average;
    }

    public List<Student> getStudentsWithGPAAbove(double threshold) {
        if (threshold < 0 || threshold > 100) {
            System.out.println("Ошибка: порог должен быть в диапазоне от 0 до 100");
            return new ArrayList<>();
        }

        List<Student> result = new ArrayList<>();
        System.out.println("\n=== Студенты с GPA выше " + threshold + " ===");
        
        for (Student student : students) {
            double gpa = calculateStudentGPA(student.getId());
            if (gpa > threshold) {
                result.add(student);
                System.out.println("STD-" + student.getId() + " - " + student.getFullName() + 
                                   " (GPA: " + String.format("%.2f", gpa) + ")");
            }
        }

        if (result.isEmpty()) {
            System.out.println("Студентов с GPA выше " + threshold + " не найдено");
        }
        System.out.println();
        return result;
    }

    public Student getStudentWithHighestGPA() {
        if (students.isEmpty()) {
            System.out.println("Нет зарегистрированных студентов");
            return null;
        }

        Student bestStudent = null;
        double highestGPA = -1.0;

        for (Student student : students) {
            double gpa = calculateStudentGPA(student.getId());
            if (gpa > highestGPA) {
                highestGPA = gpa;
                bestStudent = student;
            }
        }

        if (bestStudent != null && highestGPA > 0) {
            System.out.println("\n=== Студент с наибольшим средним баллом ===");
            System.out.println("STD-" + bestStudent.getId() + " - " + bestStudent.getFullName() + 
                               " (GPA: " + String.format("%.2f", highestGPA) + ")");
            System.out.println();
        } else {
            System.out.println("Нет студентов с оценками");
        }

        return bestStudent;
    }

    // ========== Вспомогательные методы ==========

    private Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    private double calculateStudentGPA(int studentId) {
        List<Subject> studentSubjects = getSubjectsByStudent(studentId);
        if (studentSubjects.isEmpty()) {
            return 0.0;
        }

        double totalWeightedScore = 0.0;
        int totalCredits = 0;

        for (Subject subject : studentSubjects) {
            List<Grade> subjectGrades = getGradesBySubjectCode(subject.getCode());
            if (!subjectGrades.isEmpty()) {
                double avgScore = 0.0;
                for (Grade grade : subjectGrades) {
                    avgScore += grade.getScore();
                }
                avgScore /= subjectGrades.size();
                totalWeightedScore += avgScore * subject.getCredits();
                totalCredits += subject.getCredits();
            }
        }

        if (totalCredits == 0) {
            return 0.0;
        }
        return totalWeightedScore / totalCredits;
    }

    // Геттеры для отладки
    public List<Student> getStudents() {
        return students;
    }

    public Map<String, Subject> getSubjects() {
        return subjects;
    }

    public List<Grade> getGrades() {
        return grades;
    }
}