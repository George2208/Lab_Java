package com.company;

class Subject {
    private Room room;
    private int nr_students;
    private Person teacher;

    public Subject(Room _room, int _nr_students, Person _teacher) {
        room = _room;
        nr_students = _nr_students;
        teacher = _teacher;
    }

    public Room getRoom() { return room; }
    public int getNr_students() { return nr_students; }
    public Person getTeacher() { return teacher; }

    public void setRoom(Room room) { this.room = room; }
    public void setNr_students(int nr_students) { this.nr_students = nr_students; }
    public void setTeacher(Person teacher) { this.teacher = teacher; }

    @Override
    public String toString() {
        return "Subject{" +
                "room=" + room +
                ", nr_students=" + nr_students +
                ", teacher=" + teacher +
                '}';
    }
}

public class Ex3 {
    public static void main(String[] args) {
        Person p1 = new Person("nume1", "prenume2", 20, 123456789L, "male");
        Person p2 = new Person("nume1", "prenume2", 30, 987654321L, "female");
        Room r1 = new Room(1, 1, "Apartament");
        Room r2 = new Room(2, 2, "Garsoniera");
        Subject s1 = new Subject(r1, 10, p1);
        Subject s2 = new Subject(r2, 5, p2);
        System.out.println(s1);
        System.out.println(s2);
    }
}
