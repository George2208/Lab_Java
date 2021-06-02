package graphics;

import database.Database;
import database.Row;
import database.Table;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class GraphicInterface extends Database {
    class Model implements TableModel {
        Table table;
        Class<?> cls;
        Model(Class<?> cls) {
            this.cls = cls;
            table = tables.get(cls);
        }
        @Override public int getRowCount() { return table.rows.size(); }
        @Override public int getColumnCount() { return table.getters.size()+1; }
        @Override public String getColumnName(int columnIndex) {
            if(columnIndex==0)
                return "ID";
            return String.valueOf(table.getters.keySet().toArray()[columnIndex-1]);
        }
        @Override public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex==0)
                return Integer.class;
            return table.getters.get(getColumnName(columnIndex)).getReturnType();
        }
        @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex != 0; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == 0)
                return ((Row) table.rows.values().toArray()[rowIndex]).id;
            try {
                return table.getters.get(getColumnName(columnIndex)).invoke(table.rows.values().toArray()[rowIndex]);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("<Get value at> exception");
            }
        }
        @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(columnIndex==0)
                throw new RuntimeException("ID column cannot be modified");
            Integer id = (Integer) table.rows.keySet().toArray()[rowIndex];
            String column = String.valueOf(table.getters.keySet().toArray()[columnIndex-1]);
            update(cls, id, column, String.valueOf(aValue));
        }
        @Override public void addTableModelListener(TableModelListener l) {}
        @Override public void removeTableModelListener(TableModelListener l) {}
    }

    String windowTitle;
    public GraphicInterface(String url, String windowTitle) {
        super(url);
        this.windowTitle = windowTitle;
    }
    public void run() {
        new JFrame(windowTitle){{
            setSize(400,500);
            setLayout(new BorderLayout());
            add(new JPanel(){{
                setLayout(new GridLayout(0, 1));
                for(Class<?> i: tables.keySet())
                    add(new JButton(tables.get(i).name){{ addActionListener(e -> focusTable(i, tables.get(i).name)); }});
            }});
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }};
    }
    public void focusTable(Class<?> cls, String windowTitle) {
        JFrame f;
        f = new JFrame(windowTitle){{
            setSize(400,500);
            setLayout(null);
            setVisible(true);
        }};
        JTable table = new JTable(new Model(cls)) {{
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i=0; i<getColumnModel().getColumnCount(); i++)
                getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }};
        f.add(new JScrollPane(table) {{
            setBounds(0,0,400, 500);
        }});
    }
}
