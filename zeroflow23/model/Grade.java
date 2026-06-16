package model;

public class Grade {
    private int id;
    private String subjectCode;
    private String type;
    private double score;
    private String comment;

    public Grade(int id, String subjectCode, String type, double score, String comment) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.type = type;
        this.score = score;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", subjectCode='" + subjectCode + '\'' +
                ", type='" + type + '\'' +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                '}';
    }
}
