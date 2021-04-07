package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Album implements Comparable<Album> {
    private final String name, artist;
    private final int rating, year;

    public Album(String name, String artist, int rating, int year) {
        this.name = name;
        this.artist = artist;
        this.rating = rating;
        this.year = year;
    }

    public String getName() { return name; }
    public String getArtist() { return artist; }
    public int getRating() { return rating; }
    public int getYear() { return year; }

    @Override
    public int compareTo(Album o) {
        return rating - o.rating;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                '}';
    }
}

class NameComparator implements Comparator<Album> {
    @Override
    public int compare(Album o1, Album o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

class RatingComparator implements Comparator<Album> {
    @Override
    public int compare(Album o1, Album o2) {
        return o1.getRating() - o2.getRating();
    }
}

class YearComparator implements Comparator<Album> {
    @Override
    public int compare(Album o1, Album o2) {
        return o1.getYear() - o2.getYear();
    }
}

public class Ex2 {
    public static void main(String[] args) {
        List<Album> list = new ArrayList<>();
        list.add(new Album("name4", "artist4", 4, 2004));
        list.add(new Album("name1", "artist1", 1, 2001));
        list.add(new Album("name3", "artist3", 3, 2003));
        list.add(new Album("name2", "artist2", 2, 2002));
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);
        list.sort(new NameComparator());
        System.out.println(list);
        list.sort(new RatingComparator());
        System.out.println(list);
        list.sort(new YearComparator());
        System.out.println(list);

    }
}
