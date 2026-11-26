/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FileManager {
    private File currentDirectory;
    private final Stack<File> backHistory = new Stack();
    private final Stack<File> forwardHistory = new Stack();
    private File clipboardFile = null;
    private ClipboardMode clipboardMode = null;

    public FileManager() {
        String string = System.getProperty("user.home");
        File file = new File(string);
        if (!file.exists() || !file.isDirectory()) {
            File[] fileArray = File.listRoots();
            file = fileArray != null && fileArray.length > 0 ? fileArray[0] : new File(".");
        }
        this.currentDirectory = file;
    }

    public boolean navigateTo(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }
        if (this.currentDirectory != null && !this.currentDirectory.equals(file)) {
            this.backHistory.push(this.currentDirectory);
            this.forwardHistory.clear();
        }
        this.currentDirectory = file;
        return true;
    }

    public boolean canGoBack() {
        return !this.backHistory.isEmpty();
    }

    public boolean canGoForward() {
        return !this.forwardHistory.isEmpty();
    }

    public boolean goBack() {
        if (this.canGoBack()) {
            this.forwardHistory.push(this.currentDirectory);
            this.currentDirectory = this.backHistory.pop();
            return true;
        }
        return false;
    }

    public boolean goForward() {
        if (this.canGoForward()) {
            this.backHistory.push(this.currentDirectory);
            this.currentDirectory = this.forwardHistory.pop();
            return true;
        }
        return false;
    }

    public boolean goUp() {
        File file;
        if (this.currentDirectory != null && (file = this.currentDirectory.getParentFile()) != null && file.exists() && file.isDirectory()) {
            return this.navigateTo(file);
        }
        return false;
    }

    public File getCurrentDirectory() {
        return this.currentDirectory;
    }

    public List<File> getFiles() {
        ArrayList<File> arrayList = new ArrayList<File>();
        if (this.currentDirectory != null && this.currentDirectory.exists()) {
            try {
                File[] fileArray = this.currentDirectory.listFiles();
                if (fileArray != null) {
                    arrayList.addAll(Arrays.asList(fileArray));
                }
            }
            catch (SecurityException securityException) {
                System.err.println("Security warning: Permission denied to list contents of " + this.currentDirectory.getAbsolutePath());
            }
        }
        return arrayList;
    }

    public File[] getSystemDrives() {
        File[] fileArray = File.listRoots();
        return fileArray != null ? fileArray : new File[]{};
    }

    public Map<String, File> getQuickAccessLocations() {
        File file;
        File file2;
        File file3;
        File file4;
        File file5;
        LinkedHashMap<String, File> linkedHashMap = new LinkedHashMap<String, File>();
        String string = System.getProperty("user.home");
        File file6 = new File(string);
        linkedHashMap.put("Home", file6);
        File file7 = new File(file6, "Desktop");
        if (file7.exists()) {
            linkedHashMap.put("Desktop", file7);
        }
        if ((file5 = new File(file6, "Downloads")).exists()) {
            linkedHashMap.put("Downloads", file5);
        }
        if ((file4 = new File(file6, "Documents")).exists()) {
            linkedHashMap.put("Documents", file4);
        }
        if ((file3 = new File(file6, "Pictures")).exists()) {
            linkedHashMap.put("Pictures", file3);
        }
        if ((file2 = new File(file6, "Music")).exists()) {
            linkedHashMap.put("Music", file2);
        }
        if ((file = new File(file6, "Videos")).exists()) {
            linkedHashMap.put("Videos", file);
        }
        return linkedHashMap;
    }

    public File createFolder(String string) throws IOException {
        if (!FileUtils.isValidFileName(string)) {
            throw new IOException("Invalid folder name. Names cannot contain \\ / : * ? \" < > |");
        }
        File file = new File(this.currentDirectory, string.trim());
        if (file.exists()) {
            throw new IOException("A file or folder with name '" + string + "' already exists.");
        }
        boolean bl = file.mkdir();
        if (!bl) {
            throw new IOException("Failed to create folder. Please check directory write permissions.");
        }
        return file;
    }

    public File rename(File file, String string) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("Selected file does not exist.");
        }
        if (!FileUtils.isValidFileName(string)) {
            throw new IOException("Invalid name. Names cannot contain \\ / : * ? \" < > |");
        }
        File file2 = new File(file.getParentFile(), string.trim());
        if (file2.exists()) {
            throw new IOException("A file or folder named '" + string + "' already exists in this folder.");
        }
        boolean bl = file.renameTo(file2);
        if (!bl) {
            throw new IOException("Rename failed. The file may be in use by another application.");
        }
        return file2;
    }

    public void copy(File file) {
        if (file != null && file.exists()) {
            this.clipboardFile = file;
            this.clipboardMode = ClipboardMode.COPY;
        }
    }

    public void cut(File file) {
        if (file != null && file.exists()) {
            this.clipboardFile = file;
            this.clipboardMode = ClipboardMode.CUT;
        }
    }

    public boolean canPaste() {
        return this.clipboardFile != null && this.clipboardFile.exists();
    }

    public File getClipboardFile() {
        return this.clipboardFile;
    }

    public ClipboardMode getClipboardMode() {
        return this.clipboardMode;
    }

    public void clearClipboard() {
        this.clipboardFile = null;
        this.clipboardMode = null;
    }

    public String getClipboardDescription() {
        if (!this.canPaste()) {
            return "";
        }
        String string = this.clipboardMode == ClipboardMode.COPY ? "COPY" : "CUT";
        return "[" + string + "] " + this.clipboardFile.getName();
    }

    public File paste(File file) throws IOException {
        Object object;
        if (!this.canPaste()) {
            throw new IOException("Clipboard is empty or the source file no longer exists.");
        }
        if (file == null || !file.isDirectory()) {
            file = this.currentDirectory;
        }
        if (file == null || !file.isDirectory()) {
            throw new IOException("Target is not a valid directory.");
        }
        if (this.clipboardFile.isDirectory()) {
            object = this.clipboardFile.getAbsolutePath().toLowerCase();
            String string = file.getAbsolutePath().toLowerCase();
            if (string.equals(object) || string.startsWith((String)object + File.separator)) {
                throw new IOException("Cannot copy or move a folder into itself or its subfolder.");
            }
        }
        if (this.clipboardMode == ClipboardMode.CUT && file.equals(this.clipboardFile.getParentFile())) {
            object = this.clipboardFile;
            this.clearClipboard();
            return object;
        }
        object = FileUtils.getUniqueDestinationFile(file, this.clipboardFile.getName());
        if (this.clipboardMode == ClipboardMode.COPY) {
            FileUtils.copyRecursively(this.clipboardFile, (File)object);
        } else if (this.clipboardMode == ClipboardMode.CUT) {
            boolean bl = this.clipboardFile.renameTo((File)object);
            if (!bl) {
                FileUtils.copyRecursively(this.clipboardFile, (File)object);
                boolean bl2 = FileUtils.deleteRecursively(this.clipboardFile);
                if (!bl2) {
                    System.err.println("Notice: Could not delete all source files during cut/move.");
                }
            }
            this.clearClipboard();
        }
        return object;
    }

    public boolean delete(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File to delete does not exist.");
        }
        boolean bl = FileUtils.deleteRecursively(file);
        if (!bl) {
            throw new IOException("Could not delete '" + file.getName() + "'. It may be read-only or currently open in another program.");
        }
        return true;
    }

    public void open(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
        }
        if (file.isDirectory()) {
            this.navigateTo(file);
            return;
        }
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new IOException("System desktop open action is not supported on this platform.");
        }
        Desktop.getDesktop().open(file);
    }

    public List<File> search(String string) {
        return this.search(this.currentDirectory, string, false);
    }

    public List<File> search(File file, String string, boolean bl) {
        ArrayList<File> arrayList = new ArrayList<File>();
        if (file == null || !file.exists() || string == null) {
            return arrayList;
        }
        String string2 = string.trim().toLowerCase();
        ArrayList<File> arrayList2 = new ArrayList<File>();
        if (bl) {
            this.collectAllFiles(file, arrayList2, 0, 3);
        } else {
            File[] fileArray = file.listFiles();
            if (fileArray != null) {
                arrayList2.addAll(Arrays.asList(fileArray));
            }
        }
        for (File file2 : arrayList2) {
            if (!file2.getName().toLowerCase().contains(string2)) continue;
            arrayList.add(file2);
        }
        return arrayList;
    }

    private void collectAllFiles(File file, List<File> list, int n, int n2) {
        if (file == null || n > n2) {
            return;
        }
        try {
            File[] fileArray = file.listFiles();
            if (fileArray == null) {
                return;
            }
            for (File file2 : fileArray) {
                list.add(file2);
                if (!file2.isDirectory()) continue;
                this.collectAllFiles(file2, list, n + 1, n2);
            }
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
    }

    public static enum ClipboardMode {
        COPY,
        CUT;

    }
}

