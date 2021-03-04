package com.company;

class Person {
    private String name, surname, type;
    private int age;
    private long identity_number;

    public Person(String _name, String _surname, int _age, long _identity_number, String _type) {
        name = _name;
        surname = _surname;
        age = _age;
        identity_number = _identity_number;
        type = _type;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getAge() { return age; }
    public long getIdentity_number() { return identity_number; }
    public String getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAge(int age) { this.age = age; }
    public void setIdentity_number(long identity_number) { this.identity_number = identity_number; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", type='" + type + '\'' +
                ", age=" + age +
                ", identity_number=" + identity_number +
                '}';
    }
}

public class Ex1 {
    public static void main(String[] args) {
        Person x = new Person("nume1", "prenume2", 20, 123456789L, "male");
        Person y = new Person("nume1", "prenume2", 30, 987654321L, "female");
        System.out.println(x);
        System.out.println(y);
    }
}
