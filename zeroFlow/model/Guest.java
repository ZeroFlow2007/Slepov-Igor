package model;

public class Guest {
    private int id;
    private String fullName;
    private String passport;

    public Guest(int id, String fullName, String passport) {
        this.id = id;
        this.fullName = fullName;
        this.passport = passport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", passport='" + passport + '\'' +
                '}';
    }
}