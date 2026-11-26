/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class FileTableModel
extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = new String[]{"Name", "Type", "Size", "Date Modified"};
    private List<File> fileList = new ArrayList<File>();
    private int currentSortColumn = -1;
    private boolean isAscending = true;

    public void setFiles(List<File> list) {
        this.fileList = list != null ? new ArrayList<File>(list) : new ArrayList<File>();
        if (this.currentSortColumn != -1) {
            this.sort(this.currentSortColumn, this.isAscending);
        } else {
            this.fireTableDataChanged();
        }
    }

    public File getFileAt(int n) {
        if (n >= 0 && n < this.fileList.size()) {
            return this.fileList.get(n);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return this.fileList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int n) {
        if (n >= 0 && n < COLUMN_NAMES.length) {
            return COLUMN_NAMES[n];
        }
        return super.getColumnName(n);
    }

    @Override
    public Class<?> getColumnClass(int n) {
        if (n == 0) {
            return File.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int n, int n2) {
        if (n < 0 || n >= this.fileList.size()) {
            return null;
        }
        File file = this.fileList.get(n);
        switch (n2) {
            case 0: {
                return file;
            }
            case 1: {
                return FileUtils.getFileType(file);
            }
            case 2: {
                return FileUtils.formatSize(file);
            }
            case 3: {
                return FileUtils.formatDate(file);
            }
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public void sort(final int n, final boolean bl) {
        this.currentSortColumn = n;
        this.isAscending = bl;
        Comparator<File> comparator = new Comparator<File>(){

            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && !file2.isDirectory()) {
                    return -1;
                }
                if (!file.isDirectory() && file2.isDirectory()) {
                    return 1;
                }
                int n2 = 0;
                switch (n) {
                    case 0: {
                        n2 = file.getName().compareToIgnoreCase(file2.getName());
                        break;
                    }
                    case 1: {
                        String string = FileUtils.getFileType(file);
                        String string2 = FileUtils.getFileType(file2);
                        n2 = string.compareToIgnoreCase(string2);
                        if (n2 != 0) break;
                        n2 = file.getName().compareToIgnoreCase(file2.getName());
                        break;
                    }
                    case 2: {
                        long l = file.isDirectory() ? 0L : file.length();
                        long l2 = file2.isDirectory() ? 0L : file2.length();
                        n2 = Long.compare(l, l2);
                        if (n2 != 0) break;
                        n2 = file.getName().compareToIgnoreCase(file2.getName());
                        break;
                    }
                    case 3: {
                        n2 = Long.compare(file.lastModified(), file2.lastModified());
                        break;
                    }
                    default: {
                        n2 = file.getName().compareToIgnoreCase(file2.getName());
                    }
                }
                return bl ? n2 : -n2;
            }
        };
        Collections.sort(this.fileList, comparator);
        this.fireTableDataChanged();
    }

    public int getCurrentSortColumn() {
        return this.currentSortColumn;
    }

    public boolean isAscending() {
        return this.isAscending;
    }
}

