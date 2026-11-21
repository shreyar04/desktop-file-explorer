package fileexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileSystemView;

/**
 * FileUtils: Utility class containing reusable helper methods for file operations,
 * string formatting, calculations, and data conversions.
 *
 * Demonstrates Syllabus Concepts:
 * - Method Overloading (formatSize, formatDate)
 * - String Handling (substring, lastIndexOf, trim, toLowerCase)
 * - Static Methods and Constants
 * - Java I/O Streams and NIO (FileInputStream, FileOutputStream, Files)
 * - Exception Handling (try-catch, IOException)
 * - Wrapper Classes (Long, Integer)
 */
public class FileUtils {

    // Date format for displaying file timestamps consistently
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Private constructor to prevent instantiation of utility class
    private FileUtils() {}

    /**
     * Method Overloading - Variant 1:
     * Formats raw byte count into human-readable units (Bytes, KB, MB, GB).
     *
     * @param bytes Number of bytes as a long primitive
     * @return Formatted string (e.g., "1.45 MB", "512 KB")
     */
    public static String formatSize(long bytes) {
        if (bytes < 0) {
            return "0 B";
        }
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            double kb = bytes / 1024.0;
            return String.format("%.2f KB", kb);
        } else if (bytes < 1024 * 1024 * 1024) {
            double mb = bytes / (1024.0 * 1024.0);
            return String.format("%.2f MB", mb);
        } else {
            double gb = bytes / (1024.0 * 1024.0 * 1024.0);
            return String.format("%.2f GB", gb);
        }
    }

    /**
     * Method Overloading - Variant 2:
     * Formats file size based on a File object.
     * Returns empty or item count indicator for directories, or byte size for files.
     *
     * @param file The File object
     * @return Formatted string representation
     */
    public static String formatSize(File file) {
        if (file == null || !file.exists()) {
            return "-";
        }
        if (file.isDirectory()) {
            return "<DIR>";
        }
        return formatSize(file.length());
    }

    /**
     * Method Overloading - Variant 1:
     * Formats epoch millisecond timestamp into readable date string.
     *
     * @param millis Time in milliseconds
     * @return Formatted date string (e.g., "2026-11-17 10:30:00")
     */
    public static String formatDate(long millis) {
        if (millis <= 0) {
            return "-";
        }
        return DATE_FORMAT.format(new Date(millis));
    }

    /**
     * Method Overloading - Variant 2:
     * Formats the last modified date of a File object.
     *
     * @param file The File object
     * @return Formatted date string
     */
    public static String formatDate(File file) {
        if (file == null || !file.exists()) {
            return "-";
        }
        return formatDate(file.lastModified());
    }

    /**
     * Extracts the extension from a file name.
     * Demonstrates String manipulation methods: lastIndexOf, substring, toLowerCase.
     *
     * @param file The file to inspect
     * @return Extension in lowercase without dot, or empty string if none
     */
    public static String getFileExtension(File file) {
        if (file == null || file.isDirectory()) {
            return "";
        }
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Determines a user-friendly description of the file type.
     * Uses Windows FileSystemView where possible, with fallback logic.
     *
     * @param file The file to check
     * @return A readable description like "Folder", "Java Source File", "Text Document"
     */
    public static String getFileType(File file) {
        if (file == null || !file.exists()) {
            return "Unknown";
        }
        if (file.isDirectory()) {
            return "File Folder";
        }

        try {
            // Use native Windows file description if available
            String sysType = FileSystemView.getFileSystemView().getSystemTypeDescription(file);
            if (sysType != null && !sysType.trim().isEmpty()) {
                return sysType;
            }
        } catch (Exception e) {
            // Fallback to manual extension lookup
        }

        String ext = getFileExtension(file);
        switch (ext) {
            case "txt": return "Text Document";
            case "java": return "Java Source File";
            case "class": return "Java Bytecode File";
            case "pdf": return "PDF Document";
            case "doc":
            case "docx": return "Microsoft Word Document";
            case "xls":
            case "xlsx": return "Microsoft Excel Worksheet";
            case "ppt":
            case "pptx": return "PowerPoint Presentation";
            case "png": return "PNG Image";
            case "jpg":
            case "jpeg": return "JPEG Image";
            case "gif": return "GIF Image";
            case "zip":
            case "rar":
            case "7z": return "Compressed Archive";
            case "exe": return "Application";
            case "html":
            case "htm": return "HTML Document";
            case "json": return "JSON File";
            case "xml": return "XML Document";
            case "mp3":
            case "wav": return "Audio File";
            case "mp4":
            case "mkv": return "Video File";
            default:
                return ext.isEmpty() ? "File" : ext.toUpperCase() + " File";
        }
    }

    /**
     * Validates whether a file or folder name contains illegal Windows characters.
     * Illegal characters: \ / : * ? " < > |
     *
     * @param name Proposed name
     * @return true if name is valid; false otherwise
     */
    public static boolean isValidFileName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        // Check for Windows reserved characters
        String illegalChars = "\\/:*?\"<>|";
        for (char c : illegalChars.toCharArray()) {
            if (trimmed.indexOf(c) != -1) {
                return false;
            }
        }
        // Check for reserved Windows device names
        String upper = trimmed.toUpperCase();
        String[] reservedNames = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "LPT1", "LPT2", "LPT3"};
        for (String res : reservedNames) {
            if (upper.equals(res) || upper.startsWith(res + ".")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a safe, non-colliding destination file in target directory.
     * If file already exists, appends " - Copy" or " - Copy (2)".
     *
     * @param targetDirectory Directory where file will be placed
     * @param baseName Desired file or folder name
     * @return A unique File that does not currently exist
     */
    public static File getUniqueDestinationFile(File targetDirectory, String baseName) {
        File candidate = new File(targetDirectory, baseName);
        if (!candidate.exists()) {
            return candidate;
        }

        String nameWithoutExt = baseName;
        String extWithDot = "";
        int dotIndex = baseName.lastIndexOf('.');
        if (dotIndex > 0 && !candidate.isDirectory()) {
            nameWithoutExt = baseName.substring(0, dotIndex);
            extWithDot = baseName.substring(dotIndex);
        }

        int count = 1;
        while (candidate.exists()) {
            String suffix = (count == 1) ? " - Copy" : " - Copy (" + count + ")";
            candidate = new File(targetDirectory, nameWithoutExt + suffix + extWithDot);
            count++;
        }
        return candidate;
    }

    /**
     * Recursively copies a file or directory to a target location.
     * Demonstrates:
     * - File I/O Streams (FileInputStream, FileOutputStream)
     * - Recursion for directory hierarchy
     * - Exception handling
     *
     * @param source The source File or Directory
     * @param destination The target File or Directory
     * @throws IOException If an I/O error occurs
     */
    public static void copyRecursively(File source, File destination) throws IOException {
        if (!source.exists()) {
            throw new IOException("Source file does not exist: " + source.getAbsolutePath());
        }

        if (source.isDirectory()) {
            if (!destination.exists() && !destination.mkdirs()) {
                throw new IOException("Failed to create destination directory: " + destination.getAbsolutePath());
            }
            File[] children = source.listFiles();
            if (children != null) {
                for (File child : children) {
                    File destChild = new File(destination, child.getName());
                    copyRecursively(child, destChild);
                }
            }
        } else {
            // Ensure parent directory exists
            File parent = destination.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            // Copy file using Java I/O Streams with buffer (College Syllabus: Streams & I/O)
            try (FileInputStream inStream = new FileInputStream(source);
                 FileOutputStream outStream = new FileOutputStream(destination)) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }
            }
        }
    }

    /**
     * Recursively deletes a file or directory.
     * Essential for directories containing files, as standard File.delete() fails on non-empty dirs.
     *
     * @param fileOrDir File or directory to delete
     * @return true if successfully deleted; false otherwise
     */
    public static boolean deleteRecursively(File fileOrDir) {
        if (fileOrDir == null || !fileOrDir.exists()) {
            return false;
        }

        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean success = deleteRecursively(child);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return fileOrDir.delete();
    }

    /**
     * Counts the number of folders and files inside a directory.
     *
     * @param directory The directory to inspect
     * @return An array where index 0 = folder count, index 1 = file count
     */
    public static int[] countItems(File directory) {
        int[] counts = new int[]{0, 0};
        if (directory != null && directory.isDirectory()) {
            File[] items = directory.listFiles();
            if (items != null) {
                for (File item : items) {
                    if (item.isDirectory()) {
                        counts[0]++;
                    } else {
                        counts[1]++;
                    }
                }
            }
        }
        return counts;
    }

    /**
     * Recursively calculates the total size of a file or directory.
     *
     * @param fileOrDir The target file or directory
     * @return Total size in bytes
     */
    public static long calculateTotalSize(File fileOrDir) {
        if (fileOrDir == null || !fileOrDir.exists()) {
            return 0L;
        }
        if (!fileOrDir.isDirectory()) {
            return fileOrDir.length();
        }

        long totalSize = 0L;
        File[] children = fileOrDir.listFiles();
        if (children != null) {
            for (File child : children) {
                totalSize += calculateTotalSize(child);
            }
        }
        return totalSize;
    }
}
