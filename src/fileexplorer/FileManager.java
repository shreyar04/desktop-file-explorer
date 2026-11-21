package fileexplorer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * FileManager: Core business logic controller for the Desktop File Explorer.
 * Manages file system navigation, navigation history stacks, internal clipboard,
 * and executes file operations (create, rename, copy, move, delete, open, search).
 *
 * Demonstrates Syllabus Concepts:
 * - Collections Framework (List, ArrayList, Stack, Map, LinkedHashMap)
 * - Iterators (Iterator<File>)
 * - Method Overloading (search methods)
 * - Exception Handling (IOException, SecurityException)
 * - File and Desktop APIs (java.io.File, java.awt.Desktop)
 * - Enums and State Encapsulation
 */
public class FileManager {

    /**
     * Enumeration representing the clipboard operation type.
     */
    public enum ClipboardMode {
        COPY,
        CUT
    }

    // Current working directory
    private File currentDirectory;

    // Navigation history stacks (Demonstrates Collections Framework - Stack)
    private final Stack<File> backHistory;
    private final Stack<File> forwardHistory;

    // Internal clipboard state for Copy / Cut / Paste operations
    private File clipboardFile;
    private ClipboardMode clipboardMode;

    /**
     * Constructor initializes the FileManager with a default directory.
     * Starts at the user's home directory.
     */
    public FileManager() {
        this.backHistory = new Stack<>();
        this.forwardHistory = new Stack<>();
        this.clipboardFile = null;
        this.clipboardMode = null;

        // Default start directory: User Home directory
        String userHome = System.getProperty("user.home");
        File initialDir = new File(userHome);
        if (!initialDir.exists() || !initialDir.isDirectory()) {
            File[] roots = File.listRoots();
            initialDir = (roots != null && roots.length > 0) ? roots[0] : new File(".");
        }
        this.currentDirectory = initialDir;
    }

    /**
     * Navigates to a target directory and updates navigation history.
     *
     * @param targetDirectory Directory to navigate into
     * @return true if navigation succeeded; false if directory is invalid
     */
    public boolean navigateTo(File targetDirectory) {
        if (targetDirectory == null || !targetDirectory.exists() || !targetDirectory.isDirectory()) {
            return false;
        }

        // Avoid adding to back stack if navigating to the same directory
        if (currentDirectory != null && !currentDirectory.equals(targetDirectory)) {
            backHistory.push(currentDirectory);
            forwardHistory.clear(); // Clear forward history on new navigation path
        }

        this.currentDirectory = targetDirectory;
        return true;
    }

    /**
     * Checks if back navigation is possible.
     */
    public boolean canGoBack() {
        return !backHistory.isEmpty();
    }

    /**
     * Checks if forward navigation is possible.
     */
    public boolean canGoForward() {
        return !forwardHistory.isEmpty();
    }

    /**
     * Navigates back in history.
     *
     * @return true if successful
     */
    public boolean goBack() {
        if (canGoBack()) {
            forwardHistory.push(currentDirectory);
            this.currentDirectory = backHistory.pop();
            return true;
        }
        return false;
    }

    /**
     * Navigates forward in history.
     *
     * @return true if successful
     */
    public boolean goForward() {
        if (canGoForward()) {
            backHistory.push(currentDirectory);
            this.currentDirectory = forwardHistory.pop();
            return true;
        }
        return false;
    }

    /**
     * Navigates up to the parent directory.
     *
     * @return true if navigated up; false if already at drive root
     */
    public boolean goUp() {
        if (currentDirectory != null) {
            File parent = currentDirectory.getParentFile();
            if (parent != null && parent.exists()) {
                return navigateTo(parent);
            }
        }
        return false;
    }

    /**
     * Gets the current working directory.
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Retrieves all files and folders in the current directory.
     * Handles restricted directories gracefully.
     *
     * @return List of File objects
     */
    public List<File> getFiles() {
        List<File> result = new ArrayList<>();
        if (currentDirectory != null && currentDirectory.exists()) {
            File[] list = currentDirectory.listFiles();
            if (list != null) {
                result.addAll(Arrays.asList(list));
            }
        }
        return result;
    }

    /**
     * Retrieves system drives (e.g., C:\, D:\, E:\).
     *
     * @return Array of drive root File objects
     */
    public File[] getSystemDrives() {
        File[] roots = File.listRoots();
        return roots != null ? roots : new File[0];
    }

    /**
     * Retrieves standard Quick Access locations (Home, Desktop, Documents, Downloads).
     * Demonstrates Collections Framework (Map, LinkedHashMap).
     *
     * @return Ordered Map of location names and corresponding File objects
     */
    public Map<String, File> getQuickAccessLocations() {
        Map<String, File> locations = new LinkedHashMap<>();
        String userHome = System.getProperty("user.home");
        File homeFile = new File(userHome);
        locations.put("Home", homeFile);

        File desktop = new File(homeFile, "Desktop");
        if (desktop.exists()) locations.put("Desktop", desktop);

        File documents = new File(homeFile, "Documents");
        if (documents.exists()) locations.put("Documents", documents);

        File downloads = new File(homeFile, "Downloads");
        if (downloads.exists()) locations.put("Downloads", downloads);

        File pictures = new File(homeFile, "Pictures");
        if (pictures.exists()) locations.put("Pictures", pictures);

        return locations;
    }

    /**
     * Creates a new folder in the current directory.
     *
     * @param folderName Name of the new folder
     * @return Newly created File object
     * @throws IOException If name is invalid, already exists, or creation fails
     */
    public File createFolder(String folderName) throws IOException {
        if (!FileUtils.isValidFileName(folderName)) {
            throw new IOException("Invalid folder name. Names cannot contain \\ / : * ? \" < > |");
        }

        File newFolder = new File(currentDirectory, folderName.trim());
        if (newFolder.exists()) {
            throw new IOException("A file or folder with name '" + folderName + "' already exists.");
        }

        boolean created = newFolder.mkdir();
        if (!created) {
            throw new IOException("Failed to create folder. Please check directory write permissions.");
        }
        return newFolder;
    }

    /**
     * Renames an existing file or directory.
     *
     * @param targetFile File to rename
     * @param newName New name for the file
     * @return Renamed File object
     * @throws IOException If invalid name or rename fails
     */
    public File rename(File targetFile, String newName) throws IOException {
        if (targetFile == null || !targetFile.exists()) {
            throw new IOException("Selected file does not exist.");
        }
        if (!FileUtils.isValidFileName(newName)) {
            throw new IOException("Invalid name. Names cannot contain \\ / : * ? \" < > |");
        }

        File destination = new File(targetFile.getParentFile(), newName.trim());
        if (destination.exists()) {
            throw new IOException("A file or folder named '" + newName + "' already exists in this folder.");
        }

        boolean renamed = targetFile.renameTo(destination);
        if (!renamed) {
            throw new IOException("Rename failed. The file may be in use by another application.");
        }
        return destination;
    }

    /**
     * Stages a file/folder to clipboard for Copy operation.
     */
    public void copy(File file) {
        if (file != null && file.exists()) {
            this.clipboardFile = file;
            this.clipboardMode = ClipboardMode.COPY;
        }
    }

    /**
     * Stages a file/folder to clipboard for Move/Cut operation.
     */
    public void cut(File file) {
        if (file != null && file.exists()) {
            this.clipboardFile = file;
            this.clipboardMode = ClipboardMode.CUT;
        }
    }

    /**
     * Checks if there is a file in the clipboard ready to paste.
     */
    public boolean canPaste() {
        return clipboardFile != null && clipboardFile.exists();
    }

    public File getClipboardFile() {
        return clipboardFile;
    }

    public ClipboardMode getClipboardMode() {
        return clipboardMode;
    }

    /**
     * Executes the paste operation into the target directory.
     * Automatically handles duplicate names and recursive folder copies.
     *
     * @param targetDirectory Destination directory
     * @return Destination File created
     * @throws IOException If paste fails
     */
    public File paste(File targetDirectory) throws IOException {
        if (!canPaste()) {
            throw new IOException("Clipboard is empty or source file no longer exists.");
        }
        if (targetDirectory == null || !targetDirectory.isDirectory()) {
            throw new IOException("Target is not a valid directory.");
        }

        // Prevent pasting a directory inside itself or its subdirectories
        if (clipboardFile.isDirectory()) {
            String sourcePath = clipboardFile.getAbsolutePath().toLowerCase();
            String targetPath = targetDirectory.getAbsolutePath().toLowerCase();
            if (targetPath.startsWith(sourcePath)) {
                throw new IOException("Cannot copy/move a directory into itself or its subfolder.");
            }
        }

        File destination = FileUtils.getUniqueDestinationFile(targetDirectory, clipboardFile.getName());

        if (clipboardMode == ClipboardMode.COPY) {
            FileUtils.copyRecursively(clipboardFile, destination);
        } else if (clipboardMode == ClipboardMode.CUT) {
            // Attempt fast rename first (works within same drive)
            boolean moved = clipboardFile.renameTo(destination);
            if (!moved) {
                // Across drives or locked: copy recursively then delete source
                FileUtils.copyRecursively(clipboardFile, destination);
                FileUtils.deleteRecursively(clipboardFile);
            }
            // Clear clipboard after cut/paste completes
            this.clipboardFile = null;
            this.clipboardMode = null;
        }

        return destination;
    }

    /**
     * Deletes a file or directory recursively.
     *
     * @param file File or directory to delete
     * @return true if deleted
     * @throws IOException If deletion fails
     */
    public boolean delete(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File to delete does not exist.");
        }

        boolean success = FileUtils.deleteRecursively(file);
        if (!success) {
            throw new IOException("Could not delete '" + file.getName() + "'. It may be read-only or currently open.");
        }
        return true;
    }

    /**
     * Opens a file using the default Windows application or navigates if it's a directory.
     *
     * @param file File to open
     * @throws IOException If opening fails
     */
    public void open(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
        }

        if (file.isDirectory()) {
            navigateTo(file);
            return;
        }

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(file);
        } else {
            throw new IOException("System desktop open action is not supported on this platform.");
        }
    }

    /**
     * Method Overloading - Variant 1:
     * Searches for files and folders matching query inside the current directory.
     * Demonstrates Collections and Iterators (Iterator<File>).
     *
     * @param query Search query string
     * @return Matching list of File objects
     */
    public List<File> search(String query) {
        return search(this.currentDirectory, query, false);
    }

    /**
     * Method Overloading - Variant 2:
     * Searches for files and folders matching query with optional recursive subfolder search.
     * Demonstrates:
     * - Method Overloading
     * - Collections Framework (List, ArrayList)
     * - Explicit Iterator traversal
     *
     * @param startDirectory Directory to search within
     * @param query Search term (case-insensitive)
     * @param recursive True to search subdirectories recursively
     * @return Matching list of File objects
     */
    public List<File> search(File startDirectory, String query, boolean recursive) {
        List<File> matches = new ArrayList<>();
        if (startDirectory == null || !startDirectory.exists() || query == null) {
            return matches;
        }

        String lowerQuery = query.trim().toLowerCase();
        List<File> allItems = new ArrayList<>();

        if (recursive) {
            collectAllFiles(startDirectory, allItems, 0, 3); // Max depth 3 for responsive UI
        } else {
            File[] files = startDirectory.listFiles();
            if (files != null) {
                allItems.addAll(Arrays.asList(files));
            }
        }

        // Traverse using Iterator (College Syllabus: Section B - Iterators)
        Iterator<File> iterator = allItems.iterator();
        while (iterator.hasNext()) {
            File item = iterator.next();
            if (item.getName().toLowerCase().contains(lowerQuery)) {
                matches.add(item);
            }
        }

        return matches;
    }

    /**
     * Helper recursive method to gather files up to max depth.
     */
    private void collectAllFiles(File dir, List<File> collector, int currentDepth, int maxDepth) {
        if (dir == null || currentDepth > maxDepth) {
            return;
        }
        File[] items = dir.listFiles();
        if (items == null) {
            return;
        }
        for (File item : items) {
            collector.add(item);
            if (item.isDirectory()) {
                collectAllFiles(item, collector, currentDepth + 1, maxDepth);
            }
        }
    }
}
