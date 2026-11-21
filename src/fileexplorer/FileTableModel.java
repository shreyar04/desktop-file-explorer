package fileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * FileTableModel: Custom Table Model for rendering file listings in a JTable.
 *
 * Demonstrates Syllabus Concepts:
 * - Inheritance (extends AbstractTableModel)
 * - Collections Framework (List, ArrayList, Collections.sort)
 * - Interfaces (Comparator)
 * - Anonymous / Inner Classes
 * - Method Overriding (getRowCount, getColumnCount, getValueAt, etc.)
 * - Encapsulation and Access Modifiers
 */
public class FileTableModel extends AbstractTableModel {

    // Column definitions for the JTable
    private static final String[] COLUMN_NAMES = {
        "Name", "Type", "Size", "Date Modified"
    };

    // Internal list holding the File items for the current directory/search
    private List<File> fileList;

    // Current sorting state tracking
    private int currentSortColumn = -1;
    private boolean isAscending = true;

    /**
     * Default constructor initializing an empty file model.
     */
    public FileTableModel() {
        this.fileList = new ArrayList<>();
    }

    /**
     * Parameterized constructor initializing the model with an existing file list.
     * Demonstrates Constructor Overloading.
     *
     * @param files List of files to display
     */
    public FileTableModel(List<File> files) {
        if (files != null) {
            this.fileList = new ArrayList<>(files);
        } else {
            this.fileList = new ArrayList<>();
        }
    }

    /**
     * Updates the data source of the table model and refreshes the view.
     *
     * @param files New list of files
     */
    public void setFiles(List<File> files) {
        if (files != null) {
            this.fileList = new ArrayList<>(files);
        } else {
            this.fileList = new ArrayList<>();
        }
        // If sorting was previously applied, preserve it
        if (currentSortColumn != -1) {
            sort(currentSortColumn, isAscending);
        } else {
            fireTableDataChanged();
        }
    }

    /**
     * Returns the underlying list of files.
     *
     * @return List of File objects
     */
    public List<File> getFiles() {
        return Collections.unmodifiableList(this.fileList);
    }

    /**
     * Retrieves the specific File object at the given table row index.
     *
     * @param rowIndex Row index in table
     * @return File object corresponding to row, or null if out of bounds
     */
    public File getFileAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < fileList.size()) {
            return fileList.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return fileList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
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
        // Column 0 is File object to allow custom renderer to extract system icon & name
        if (columnIndex == 0) {
            return File.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= fileList.size()) {
            return null;
        }

        File file = fileList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                // Return File object directly so renderer can show both icon and file name
                return file;
            case 1:
                return FileUtils.getFileType(file);
            case 2:
                return FileUtils.formatSize(file);
            case 3:
                return FileUtils.formatDate(file);
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Table cells are read-only; operations are performed via toolbar/menu
        return false;
    }

    /**
     * Sorts the table by the specified column index and direction.
     * Directories are always kept above regular files (Windows Explorer style).
     *
     * Demonstrates Collections Framework (Collections.sort), Interfaces (Comparator),
     * and Anonymous Inner Classes.
     *
     * @param columnIndex Column to sort by (0: Name, 1: Type, 2: Size, 3: Date)
     * @param ascending True for ascending, false for descending
     */
    public void sort(int columnIndex, boolean ascending) {
        this.currentSortColumn = columnIndex;
        this.isAscending = ascending;

        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                // Rule 1: Always group directories first
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                }
                if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                }

                int result = 0;
                switch (columnIndex) {
                    case 0: // Name
                        result = f1.getName().compareToIgnoreCase(f2.getName());
                        break;
                    case 1: // Type
                        String type1 = FileUtils.getFileType(f1);
                        String type2 = FileUtils.getFileType(f2);
                        result = type1.compareToIgnoreCase(type2);
                        if (result == 0) {
                            result = f1.getName().compareToIgnoreCase(f2.getName());
                        }
                        break;
                    case 2: // Size (Long wrapper compare)
                        long size1 = f1.isDirectory() ? 0L : f1.length();
                        long size2 = f2.isDirectory() ? 0L : f2.length();
                        result = Long.compare(size1, size2);
                        if (result == 0) {
                            result = f1.getName().compareToIgnoreCase(f2.getName());
                        }
                        break;
                    case 3: // Date Modified (Long wrapper compare)
                        result = Long.compare(f1.lastModified(), f2.lastModified());
                        break;
                    default:
                        result = f1.getName().compareToIgnoreCase(f2.getName());
                        break;
                }

                return ascending ? result : -result;
            }
        };

        Collections.sort(this.fileList, comparator);
        fireTableDataChanged();
    }

    public int getCurrentSortColumn() {
        return currentSortColumn;
    }

    public boolean isAscending() {
        return isAscending;
    }
}
