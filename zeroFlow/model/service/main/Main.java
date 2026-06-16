package model.service.main;

import model.*;
import model.service.HotelService;

import java.util.Scanner;

public class Main {
    private static HotelService hotelService = new HotelService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("КОНСОЛЬНАЯ СИСТЕМА ОТЕЛЯ");

        while (true) {
            printMenu();
            int choice = readIntInput("Выберите действие: ");

            switch (choice) {
                case 1:
                    addGuest();
                    break;
                case 2:
                    addRoom();
                    break;
                case 3:
                    checkIn();
                    break;
                case 4:
                    checkOut();
                    break;
                case 5:
                    extendStay();
                    break;
                case 6:
                    getRoomInfo();
                    break;
                case 7:
                    getBookingHistory();
                    break;
                case 8:
                    getTotalRevenue();
                    break;
                case 9:
                    getAvailableRooms();
                    break;
                case 10:
                    getGuestWithMostNights();
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
        System.out.println(" МЕНЮ ");
        System.out.println("1. Добавить гостя");
        System.out.println("2. Добавить номер");
        System.out.println("3. Заселить");
        System.out.println("4. Выселить");
        System.out.println("5. Продлить проживание");
        System.out.println("6. Информация о номере");
        System.out.println("7. История номера");
        System.out.println("8. Суммарный доход");
        System.out.println("9. Свободные номера по типу");
        System.out.println("10. Гость с наибольшим количеством ночей");
        System.out.println("0. Выход");
    }

    private static void addGuest() {
        System.out.println("\n--- Добавление гостя ---");
        System.out.print("Введите ФИО: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите паспортные данные: ");
        String passport = scanner.nextLine();

        if (fullName.trim().isEmpty() || passport.trim().isEmpty()) {
            System.out.println("Ошибка: ФИО и паспортные данные не могут быть пустыми");
            return;
        }

        Guest guest = new Guest(0, fullName, passport);
        hotelService.addGuest(guest);
    }

    private static void addRoom() {
        System.out.println("\n--- Добавление номера ---");
        System.out.print("Введите номер комнаты (например, 101): ");
        String roomNumber = scanner.nextLine();
        System.out.print("Введите тип (SINGLE/DOUBLE/SUITE): ");
        String type = scanner.nextLine();
        System.out.print("Введите цену за ночь: ");
        double price = readDoubleInput();

        hotelService.addRoom(roomNumber, type, price);
    }

    private static void checkIn() {
        System.out.println("\n--- Заселение ---");
        System.out.print("Введите номер комнаты: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Введите ID гостя (число без GST-): ");
        int guestId = readIntInput("");
        System.out.print("Введите количество ночей: ");
        int nights = readIntInput("");

        hotelService.checkIn(roomNumber, guestId, nights);
    }

    private static void checkOut() {
        System.out.println("\n--- Выселение ---");
        System.out.print("Введите номер комнаты: ");
        String roomNumber = scanner.nextLine();
        hotelService.checkOut(roomNumber);
    }

    private static void extendStay() {
        System.out.println("\n--- Продление проживания ---");
        System.out.print("Введите номер комнаты: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Введите количество дополнительных ночей: ");
        int additionalNights = readIntInput("");
        hotelService.extendStay(roomNumber, additionalNights);
    }

    private static void getRoomInfo() {
        System.out.println("\n--- Информация о номере ---");
        System.out.print("Введите номер комнаты: ");
        String roomNumber = scanner.nextLine();
        hotelService.getRoomInfo(roomNumber);
    }

    private static void getBookingHistory() {
        System.out.println("--- История номера ---");
        System.out.print("Введите номер комнаты: ");
        String roomNumber = scanner.nextLine();
        hotelService.getBookingHistoryByRoom(roomNumber);
    }

    private static void getTotalRevenue() {
        System.out.println("--- Суммарный доход ---");
        hotelService.getTotalRevenue();
    }

    private static void getAvailableRooms() {
        System.out.println("--- Свободные номера по типу ---");
        System.out.print("Введите тип (SINGLE/DOUBLE/SUITE): ");
        String type = scanner.nextLine();
        hotelService.getAvailableRoomsByType(type);
    }

    private static void getGuestWithMostNights() {
        System.out.println("--- Гость с наибольшим количеством ночей ---");
        hotelService.getGuestWithMostNights();
    }

    private static int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
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
                if (value <= 0) {
                    System.out.println("Ошибка: значение должно быть положительным");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }
    }
}