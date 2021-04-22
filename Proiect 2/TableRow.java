package com.company;

import java.util.List;
import java.util.Map;

public interface TableRow {
    List<String> getData();
    <T extends TableRow> void addData(Map<Integer, T> map, String[] columns) throws IndexOutOfBoundsException;
}
