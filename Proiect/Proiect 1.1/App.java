package com.company;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class App{
    private final Database db;

    void menu() {
        JFrame frame = new JFrame("Catalog");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);

        JButton studentsButton = new JButton("Students");
        studentsButton.addActionListener(e -> students());
        studentsButton.setBounds(0, 0, 100, 50);
        frame.add(studentsButton);

        JButton teachersButton = new JButton("Teachers");
        teachersButton.addActionListener(e -> teachers());
        teachersButton.setBounds(0, 100, 100, 50);
        frame.add(teachersButton);
    }

    void students() {
        JFrame frame = new JFrame("Students");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.getContentPane().removeAll();
        JTable table = new JTable(db.getStudentsData(), new String[] {"ID", "First name", "Last name", "Delete"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return column != 0; }
        };
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0,0,500,400);
        frame.add(sp);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                System.out.print(row + " " + col);
                if(table.getColumnName(col).equals("Delete")) {
                    db.deleteStudent(Integer.parseInt((String) table.getValueAt(row, 0)));
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    students();
//                    JOptionPane.showMessageDialog(null, "Student deleted");
                }
            }
        });

        JButton b = new JButton("Add student");
        b.setBounds(150, 410, 200, 40);
        b.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            JFrame form = new JFrame();
            form.setSize(300, 400);
            form.setVisible(true);
            form.setLayout(null);

            JTextField fname = new JTextField("First name", 50);
            fname.setBounds(80, 100, 140, 20);
            form.add(fname);

            JTextField lname = new JTextField("Last name", 50);
            lname.setBounds(80, 130, 140, 20);
            form.add(lname);

            JButton addStudent = new JButton("Add student");
            addStudent.setBounds(100, 160, 100, 20);
            addStudent.addActionListener(ee -> {
                db.add(new Student(fname.getText(), lname.getText()));
                form.dispatchEvent(new WindowEvent(form, WindowEvent.WINDOW_CLOSING));
                students();
            });
            form.add(addStudent);
        });
        frame.add(b);
    }

    void teachers() {
        JFrame frame = new JFrame("Professors");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(null);

        JTable table = new JTable(db.getTeachersData(), new String[] {"ID", "First name", "Last name", "Delete"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return column != 0; }
        };
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0,0,500,400);
        frame.add(sp);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if(table.getColumnName(col).equals("Delete")) {
                    db.deleteTeacher(Integer.parseInt((String) table.getValueAt(row, 0)));
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    teachers();
//                    JOptionPane.showMessageDialog(null, "Teacher deleted");
                }
            }
        });

        JButton b = new JButton("Add teacher");
        b.setBounds(150, 410, 200, 40);
        b.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            JFrame form = new JFrame();
            form.setSize(300, 400);
            form.setVisible(true);
            form.setLayout(null);

            JTextField fname = new JTextField("First name", 50);
            fname.setBounds(80, 100, 140, 20);
            form.add(fname);

            JTextField lname = new JTextField("Last name", 50);
            lname.setBounds(80, 130, 140, 20);
            form.add(lname);

            JButton addTeacher = new JButton("Add teacher");
            addTeacher.setBounds(100, 160, 100, 20);
            addTeacher.addActionListener(ee -> {
                db.add(new Teacher(fname.getText(), lname.getText()));
                form.dispatchEvent(new WindowEvent(form, WindowEvent.WINDOW_CLOSING));
                teachers();
            });
            form.add(addTeacher);
        });
        frame.add(b);
    }

    App(Database db) {
        this.db = db;
        SwingUtilities.invokeLater(this::menu);
    }
}
