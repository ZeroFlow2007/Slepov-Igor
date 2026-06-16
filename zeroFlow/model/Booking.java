package model;

public class Booking {
    private int id;
    private String roomNumber;
    private String type;
    private int nights;
    private String notes;

    public Booking(int id, String roomNumber, String type, int nights, String notes) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.nights = nights;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", nights=" + nights +
                ", notes='" + notes + '\'' +
                '}';
    }
}

