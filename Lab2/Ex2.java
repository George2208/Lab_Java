package com.company;

class Room {
    private int room_number, room_floor;
    private String room_type;

    public Room(int _room_number, int _room_floor, String _room_type) {
        room_number = _room_number;
        room_floor = _room_floor;
        room_type = _room_type;
    }

    public int getRoom_number() { return room_number; }
    public int getRoom_floor() { return room_floor; }
    public String getRoom_type() { return room_type; }

    public void setRoom_number(int room_number) { this.room_number = room_number; }
    public void setRoom_floor(int room_floor) { this.room_floor = room_floor; }
    public void setRoom_type(String room_type) { this.room_type = room_type; }

    @Override
    public String toString() {
        return "Room{" +
                "room_number=" + room_number +
                ", room_floor=" + room_floor +
                ", room_type='" + room_type + '\'' +
                '}';
    }
}

public class Ex2 {
    public static void main(String[] args) {
        Room x = new Room(1, 1, "Apartament");
        Room y = new Room(2, 2, "Garsoniera");
        System.out.println(x);
        System.out.println(y);
    }
}
