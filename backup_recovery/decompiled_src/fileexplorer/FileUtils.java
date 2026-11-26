/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileSystemView;

public class FileUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private FileUtils() {
    }

    public static String formatSize(long l) {
        if (l < 0L) {
            return "0 B";
        }
        if (l < 1024L) {
            return l + " B";
        }
        if (l < 0x100000L) {
            double d = (double)l / 1024.0;
            return String.format("%.2f KB", d);
        }
        if (l < 0x40000000L) {
            double d = (double)l / 1048576.0;
            return String.format("%.2f MB", d);
        }
        double d = (double)l / 1.073741824E9;
        return String.format("%.2f GB", d);
    }

    public static String formatSize(File file) {
        if (file == null || !file.exists()) {
            return "-";
        }
        if (file.isDirectory()) {
            return "<DIR>";
        }
        return FileUtils.formatSize(file.length());
    }

    public static String formatDate(long l) {
        if (l <= 0L) {
            return "-";
        }
        return DATE_FORMAT.format(new Date(l));
    }

    public static String formatDate(File file) {
        if (file == null || !file.exists()) {
            return "-";
        }
        return FileUtils.formatDate(file.lastModified());
    }

    public static String getFileExtension(File file) {
        if (file == null || file.isDirectory()) {
            return "";
        }
        String string = file.getName();
        int n = string.lastIndexOf(46);
        if (n > 0 && n < string.length() - 1) {
            return string.substring(n + 1).toLowerCase();
        }
        return "";
    }

    public static String getFileType(File file) {
        String string;
        if (file == null || !file.exists()) {
            return "Unknown";
        }
        if (file.isDirectory()) {
            return "File Folder";
        }
        try {
            string = FileSystemView.getFileSystemView().getSystemTypeDescription(file);
            if (string != null && !string.trim().isEmpty()) {
                return string;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        switch (string = FileUtils.getFileExtension(file)) {
            case "txt": {
                return "Text Document";
            }
            case "java": {
                return "Java Source File";
            }
            case "class": {
                return "Java Bytecode File";
            }
            case "pdf": {
                return "PDF Document";
            }
            case "doc": 
            case "docx": {
                return "Microsoft Word Document";
            }
            case "xls": 
            case "xlsx": {
                return "Microsoft Excel Worksheet";
            }
            case "ppt": 
            case "pptx": {
                return "PowerPoint Presentation";
            }
            case "png": {
                return "PNG Image";
            }
            case "jpg": 
            case "jpeg": {
                return "JPEG Image";
            }
            case "gif": {
                return "GIF Image";
            }
            case "zip": 
            case "rar": 
            case "7z": {
                return "Compressed Archive";
            }
            case "exe": {
                return "Application";
            }
            case "html": 
            case "htm": {
                return "HTML Document";
            }
            case "json": {
                return "JSON File";
            }
            case "xml": {
                return "XML Document";
            }
            case "mp3": 
            case "wav": {
                return "Audio File";
            }
            case "mp4": 
            case "mkv": {
                return "Video File";
            }
        }
        return string.isEmpty() ? "File" : string.toUpperCase() + " File";
    }

    public static boolean isValidFileName(String string) {
        String[] stringArray;
        if (string == null || string.trim().isEmpty()) {
            return false;
        }
        String string2 = string.trim();
        String string3 = "\\/:*?\"<>|";
        for (char n : string3.toCharArray()) {
            if (string2.indexOf(n) == -1) continue;
            return false;
        }
        Object object = string2.toUpperCase();
        for (String string4 : stringArray = new String[]{"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "LPT1", "LPT2", "LPT3"}) {
            if (!((String)object).equals(string4) && !((String)object).startsWith(string4 + ".")) continue;
            return false;
        }
        return true;
    }

    public static File getUniqueDestinationFile(File file, String string) {
        File file2 = new File(file, string);
        if (!file2.exists()) {
            return file2;
        }
        String string2 = string;
        String string3 = "";
        int n = string.lastIndexOf(46);
        if (n > 0 && !file2.isDirectory()) {
            string2 = string.substring(0, n);
            string3 = string.substring(n);
        }
        int n2 = 1;
        while (file2.exists()) {
            Object object = n2 == 1 ? " - Copy" : " - Copy (" + n2 + ")";
            file2 = new File(file, string2 + (String)object + string3);
            ++n2;
        }
        return file2;
    }

    public static void copyRecursively(File file, File file2) throws IOException {
        if (!file.exists()) {
            throw new IOException("Source file does not exist: " + file.getAbsolutePath());
        }
        if (file.isDirectory()) {
            if (!file2.exists() && !file2.mkdirs()) {
                throw new IOException("Failed to create destination directory: " + file2.getAbsolutePath());
            }
            File[] fileArray = file.listFiles();
            if (fileArray != null) {
                for (File file3 : fileArray) {
                    File file4 = new File(file2, file3.getName());
                    FileUtils.copyRecursively(file3, file4);
                }
            }
        } else {
            File file5 = file2.getParentFile();
            if (file5 != null && !file5.exists()) {
                file5.mkdirs();
            }
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 FileOutputStream fileOutputStream = new FileOutputStream(file2);){
                int n;
                byte[] byArray = new byte[8192];
                while ((n = fileInputStream.read(byArray)) > 0) {
                    fileOutputStream.write(byArray, 0, n);
                }
            }
            file2.setLastModified(file.lastModified());
        }
    }

    public static boolean deleteRecursively(File file) {
        File[] fileArray;
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory() && (fileArray = file.listFiles()) != null) {
            for (File file2 : fileArray) {
                boolean bl = FileUtils.deleteRecursively(file2);
                if (bl) continue;
                return false;
            }
        }
        file.setWritable(true);
        return file.delete();
    }

    public static int[] countItems(File file) {
        File[] fileArray;
        int[] nArray = new int[]{0, 0};
        if (file != null && file.isDirectory() && (fileArray = file.listFiles()) != null) {
            for (File file2 : fileArray) {
                if (file2.isDirectory()) {
                    nArray[0] = nArray[0] + 1;
                    continue;
                }
                nArray[1] = nArray[1] + 1;
            }
        }
        return nArray;
    }

    public static long calculateTotalSize(File file) {
        if (file == null || !file.exists()) {
            return 0L;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        long l = 0L;
        File[] fileArray = file.listFiles();
        if (fileArray != null) {
            for (File file2 : fileArray) {
                l += FileUtils.calculateTotalSize(file2);
            }
        }
        return l;
    }
}

