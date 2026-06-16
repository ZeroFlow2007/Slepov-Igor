package model.service;

import model.*;

import java.util.*;

public class HotelService {
    private List<Guest> guests;
    private Map<String, Room> rooms;
    private List<Booking> bookingHistory;
    private int nextGuestId;
    private int nextBookingId;

    public HotelService() {
        guests = new ArrayList<>();
        rooms = new HashMap<>();
        bookingHistory = new ArrayList<>();
        nextGuestId = 1;
        nextBookingId = 1;
    }


    public void addGuest(Guest guest) {
        if (guest == null) {
            System.out.println("Ошибка: гость не может быть null");
            return;
        }
        guest.setId(nextGuestId++);
        guests.add(guest);
        System.out.println("Гость добавлен. ID: GST-" + guest.getId());
    }

    public void addRoom(String roomNumber, String type, double price) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            System.out.println("Ошибка: номер комнаты не может быть пустым");
            return;
        }
        if (rooms.containsKey(roomNumber)) {
            System.out.println("Ошибка: комната с номером " + roomNumber + " уже существует");
            return;
        }
        if (price <= 0) {
            System.out.println("Ошибка: цена должна быть положительной");
            return;
        }
        String upperType = type.toUpperCase();
        if (!upperType.equals("SINGLE") && !upperType.equals("DOUBLE") && !upperType.equals("SUITE")) {
            System.out.println("Ошибка: тип комнаты должен быть SINGLE, DOUBLE или SUITE");
            return;
        }
        Room room = new Room(roomNumber, upperType, price);
        rooms.put(roomNumber, room);
        System.out.println("Номер " + roomNumber + " добавлен");
    }

    public void checkIn(String roomNumber, int guestId, int nights) {
        if (nights <= 0) {
            System.out.println("Ошибка: количество ночей должно быть положительным");
            return;
        }

        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("Ошибка: комната " + roomNumber + " не найдена");
            return;
        }

        Guest guest = findGuestById(guestId);
        if (guest == null) {
            System.out.println("Ошибка: гость с ID " + guestId + " не найден");
            return;
        }

        if (room.isOccupied()) {
            System.out.println("Ошибка: комната " + roomNumber + " уже занята");
            return;
        }

        room.setOccupied(true);
        room.setGuestId(guestId);

        Booking booking = new Booking(
            nextBookingId++,
            roomNumber,
            "CHECK_IN",
            nights,
            "Гость: " + guest.getFullName()
        );
        bookingHistory.add(booking);

        System.out.println("Гость " + guest.getFullName() + " заселён в комнату " + roomNumber + 
                           " на " + nights + " ночей. Сумма к оплате: " + (nights * room.getPricePerNight()));
    }

    public void checkOut(String roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("Ошибка: комната " + roomNumber + " не найдена");
            return;
        }

        if (!room.isOccupied()) {
            System.out.println("Ошибка: комната " + roomNumber + " не занята");
            return;
        }

        Guest guest = findGuestById(room.getGuestId());
        String guestName = (guest != null) ? guest.getFullName() : "Неизвестный гость";

        double totalCost = calculateStayCost(roomNumber);
        
        Booking booking = new Booking(
            nextBookingId++,
            roomNumber,
            "CHECK_OUT",
            0,
            "Гость: " + guestName + ". Итоговая стоимость: " + totalCost
        );
        bookingHistory.add(booking);

        room.setOccupied(false);
        room.setGuestId(-1);

        System.out.println("Гость " + guestName + " выселен из комнаты " + roomNumber + 
                           ". Итоговая стоимость проживания: " + totalCost);
    }

    public void extendStay(String roomNumber, int additionalNights) {
        if (additionalNights <= 0) {
            System.out.println("Ошибка: количество дополнительных ночей должно быть положительным");
            return;
        }

        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("Ошибка: комната " + roomNumber + " не найдена");
            return;
        }

        if (!room.isOccupied()) {
            System.out.println("Ошибка: комната " + roomNumber + " не занята");
            return;
        }

        Guest guest = findGuestById(room.getGuestId());
        String guestName = (guest != null) ? guest.getFullName() : "Неизвестный гость";

        Booking booking = new Booking(
            nextBookingId++,
            roomNumber,
            "EXTEND",
            additionalNights,
            "Гость: " + guestName + ". Продление на " + additionalNights + " ночей"
        );
        bookingHistory.add(booking);

        System.out.println("Проживание в комнате " + roomNumber + " продлено на " + additionalNights + 
                           " ночей. Доплата: " + (additionalNights * room.getPricePerNight()));
    }

    public void getRoomInfo(String roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("Комната " + roomNumber + " не найдена");
            return;
        }

        System.out.println("\n=== Информация о комнате " + roomNumber + " ===");
        System.out.println("Тип: " + room.getType());
        System.out.println("Цена за ночь: " + room.getPricePerNight());
        System.out.println("Статус: " + (room.isOccupied() ? "ЗАНЯТА" : "СВОБОДНА"));
        
        if (room.isOccupied()) {
            Guest guest = findGuestById(room.getGuestId());
            if (guest != null) {
                System.out.println("Гость: " + guest.getFullName());
                System.out.println("Паспорт: " + guest.getPassport());
            }
        }
        System.out.println();
    }


    public void getBookingHistoryByRoom(String roomNumber) {
        System.out.println("\n=== История операций для комнаты " + roomNumber + " ===");
        boolean found = false;
        for (Booking booking : bookingHistory) {
            if (booking.getRoomNumber().equals(roomNumber)) {
                System.out.println("[" + booking.getId() + "] " + booking.getType() + 
                                   " - " + booking.getNights() + " ночей - " + booking.getNotes());
                found = true;
            }
        }
        if (!found) {
            System.out.println("История операций для комнаты " + roomNumber + " не найдена");
        }
        System.out.println();
    }

    public void getTotalRevenue() {
        double total = 0.0;
        for (Booking booking : bookingHistory) {
            if (booking.getType().equals("CHECK_IN")) {
                Room room = rooms.get(booking.getRoomNumber());
                if (room != null) {
                    total += booking.getNights() * room.getPricePerNight();
                }
            } else if (booking.getType().equals("EXTEND")) {
                Room room = rooms.get(booking.getRoomNumber());
                if (room != null) {
                    total += booking.getNights() * room.getPricePerNight();
                }
            }
        }
        System.out.printf("Суммарный доход от всех заселений: %.2f\n", total);
    }

    public void getAvailableRoomsByType(String type) {
        String upperType = type.toUpperCase();
        if (!upperType.equals("SINGLE") && !upperType.equals("DOUBLE") && !upperType.equals("SUITE")) {
            System.out.println("Ошибка: тип комнаты должен быть SINGLE, DOUBLE или SUITE");
            return;
        }

        System.out.println("\n=== Свободные номера типа " + upperType + " ===");
        boolean found = false;
        for (Room room : rooms.values()) {
            if (!room.isOccupied() && room.getType().equals(upperType)) {
                System.out.println("Номер " + room.getRoomNumber() + " - цена: " + room.getPricePerNight());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Свободных номеров типа " + upperType + " не найдено");
        }
        System.out.println();
    }

    public void getGuestWithMostNights() {
        Map<Integer, Integer> guestNights = new HashMap<>();
        
        for (Booking booking : bookingHistory) {
            if (booking.getType().equals("CHECK_IN") || booking.getType().equals("EXTEND")) {
                Room room = rooms.get(booking.getRoomNumber());
                if (room != null && room.getGuestId() != -1) {
                    int guestId = room.getGuestId();
                    guestNights.put(guestId, guestNights.getOrDefault(guestId, 0) + booking.getNights());
                }
            }
        }

        if (guestNights.isEmpty()) {
            System.out.println("Нет данных о проживании");
            return;
        }

        int maxGuestId = -1;
        int maxNights = 0;
        for (Map.Entry<Integer, Integer> entry : guestNights.entrySet()) {
            if (entry.getValue() > maxNights) {
                maxNights = entry.getValue();
                maxGuestId = entry.getKey();
            }
        }

        Guest guest = findGuestById(maxGuestId);
        if (guest != null) {
            System.out.println("Гость с наибольшим количеством ночей: " + guest.getFullName() + 
                               " (ID: GST-" + guest.getId() + ") - " + maxNights + " ночей");
        }
    }


    private Guest findGuestById(int id) {
        for (Guest guest : guests) {
            if (guest.getId() == id) {
                return guest;
            }
        }
        return null;
    }

    private double calculateStayCost(String roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) return 0;
        
        int totalNights = 0;
        for (Booking booking : bookingHistory) {
            if (booking.getRoomNumber().equals(roomNumber)) {
                if (booking.getType().equals("CHECK_IN") || booking.getType().equals("EXTEND")) {
                    totalNights += booking.getNights();
                }
            }
        }
        return totalNights * room.getPricePerNight();
    }


    public List<Guest> getGuests() {
        return guests;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public List<Booking> getBookingHistory() {
        return bookingHistory;
    }
}