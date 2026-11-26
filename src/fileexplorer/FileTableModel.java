package fileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * FileTableModel: Custom Table Model for the JTable that shows files and folders.
 *
 * In Swing, JTable needs a "model" to get its data from.
 * We extend AbstractTableModel and override its methods to supply our file list.
 *
 * Demonstrates Syllabus Concepts:
 * - Inheritance (extends AbstractTableModel)
 * - Method Overriding (@Override on getRowCount, getColumnCount, getValueAt, etc.)
 * - Collections Framework (List, ArrayList, Collections.sort)
 * - Interface (Comparator<File>) via Anonymous Inner Class
 * - Encapsulation (private fileList, public methods to access it)
 * - Arrays (COLUMN_NAMES String array)
 */
public class FileTableModel extends AbstractTableModel {

    // Array of column header names displayed at the top of the JTable
    private static final String[] COLUMN_NAMES = {
        "Name", "Type", "Size", "Date Modified"
    };

    // The list of File objects currently displayed in the table
    private List<File> fileList;

    // Tracks the last sort so we can re-sort after refreshing the list
    private int currentSortColumn = -1;  // -1 means no sort applied yet
    private boolean isAscending = true;

    /**
     * Constructor: starts with an empty file list.
     */
    public FileTableModel() {
        this.fileList = new ArrayList<File>();
    }

    /**
     * Replaces the file list and refreshes the table display.
     * Called every time we navigate to a new folder or search.
     *
     * @param files The new list of files to display
     */
    public void setFiles(List<File> files) {
        if (files != null) {
            this.fileList = new ArrayList<File>(files);
        } else {
            this.fileList = new ArrayList<File>();
        }
        // Re-apply the previous sort order if one was selected
        if (currentSortColumn != -1) {
            sort(currentSortColumn, isAscending);
        } else {
            fireTableDataChanged(); // Tells JTable to repaint all rows
        }
    }

    /**
     * Returns the File object at a specific row in the table.
     *
     * @param rowIndex The row number (0-based)
     * @return File at that row, or null if row is out of range
     */
    public File getFileAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < fileList.size()) {
            return fileList.get(rowIndex);
        }
        return null;
    }

    // ---- AbstractTableModel methods that JTable calls to build itself ----

    @Override
    public int getRowCount() {
        return fileList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length; // Always 4: Name, Type, Size, Date Modified
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < COLUMN_NAMES.length) {
            return COLUMN_NAMES[columnIndex];
        }
        return super.getColumnName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Column 0 returns a File object so our custom renderer can show its icon
        if (columnIndex == 0) {
            return File.class;
        }
        return String.class; // Other columns return plain text
    }

    /**
     * Returns the display value for a specific cell in the table.
     * JTable calls this for every visible cell.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= fileList.size()) {
            return null;
        }

        File file = fileList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return file;                        // Name column: returns File (renderer shows icon + name)
            case 1:
                return FileUtils.getFileType(file); // Type column: "File Folder", "Text Document", etc.
            case 2:
                return FileUtils.formatSize(file);  // Size column: "1.23 MB" or "<DIR>"
            case 3:
                return FileUtils.formatDate(file);  // Date column: "2026-11-17 10:30:00"
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // We don't allow editing cells directly in the table
        return false;
    }

    /**
     * Sorts the file list by the given column.
     * Folders always appear before files, then we sort by the chosen column.
     *
     * Demonstrates:
     * - Collections.sort()       — sorts the ArrayList in place
     * - Comparator<File>         — an interface that defines comparison logic
     * - Anonymous Inner Class    — we implement Comparator inline here
     * - Long.compare()           — Wrapper class method for comparing long values
     *
     * @param columnIndex Which column to sort by (0=Name, 1=Type, 2=Size, 3=Date)
     * @param ascending   true = A-Z or smallest first; false = Z-A or largest first
     */
    public void sort(int columnIndex, boolean ascending) {
        this.currentSortColumn = columnIndex;
        this.isAscending = ascending;

        // Create a Comparator using an anonymous inner class
        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                // Always put folders before files (like Windows Explorer)
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1; // f1 (folder) comes first
                }
                if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;  // f2 (folder) comes first
                }

                // Both are same type — compare by the chosen column
                int result = 0;
                switch (columnIndex) {
                    case 0: // Name: compare alphabetically
                        result = f1.getName().compareToIgnoreCase(f2.getName());
                        break;
                    case 1: // Type: compare by file type description
                        String type1 = FileUtils.getFileType(f1);
                        String type2 = FileUtils.getFileType(f2);
                        result = type1.compareToIgnoreCase(type2);
                        if (result == 0) {
                            result = f1.getName().compareToIgnoreCase(f2.getName());
                        }
                        break;
                    case 2: // Size: compare by file size in bytes
                        long size1 = f1.isDirectory() ? 0L : f1.length();
                        long size2 = f2.isDirectory() ? 0L : f2.length();
                        result = Long.compare(size1, size2); // Long is a Wrapper Class
                        if (result == 0) {
                            result = f1.getName().compareToIgnoreCase(f2.getName());
                        }
                        break;
                    case 3: // Date: compare by last modified timestamp
                        result = Long.compare(f1.lastModified(), f2.lastModified());
                        break;
                    default:
                        result = f1.getName().compareToIgnoreCase(f2.getName());
                        break;
                }

                // If descending order requested, reverse the result
                return ascending ? result : -result;
            }
        };

        Collections.sort(this.fileList, comparator);
        fireTableDataChanged(); // Tell JTable the data changed so it repaints
    }

    public int getCurrentSortColumn() {
        return currentSortColumn;
    }

    public boolean isAscending() {
        return isAscending;
    }
}

