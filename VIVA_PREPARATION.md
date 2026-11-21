# Comprehensive College Viva & Exam Preparation Guide
## Desktop File Explorer — Java Swing College Internal Project

---

## Table of Contents
1. [Quick Revision Cheat Sheet](#1-quick-revision-cheat-sheet)
2. [Elevator Pitches (2-Minute and 5-Minute Presentations)](#2-elevator-pitches)
3. [Project Architecture & Internal Workflow](#3-project-architecture--internal-workflow)
4. [Class-by-Class Deep Dive & Code Explanations](#4-class-by-class-deep-dive--code-explanations)
5. [Java Syllabus Concepts Mapped to Source Code](#5-java-syllabus-concepts-mapped-to-source-code)
6. [Internal Working of All 10 Features & GUI Systems](#6-internal-working-of-all-10-features--gui-systems)
7. [75 High-Yield Viva Questions and Detailed Answers](#7-75-high-yield-viva-questions-and-detailed-answers)
8. [Project Limitations and Future Scope](#8-project-limitations-and-future-scope)

---

## 1. Quick Revision Cheat Sheet

| Category | Details / Implementation in Project |
|---|---|
| **Project Name** | Desktop File Explorer |
| **Technology Stack** | Java Standard Edition (JDK 22.0.1 compatible), Java Swing, Java AWT, Java I/O & NIO |
| **External Libraries** | **Zero** (no Maven, Gradle, Spring, or third-party JARs; runs entirely on vanilla Java SE) |
| **Design Pattern** | **Model-View-Controller (MVC)** architectural separation |
| **Package Name** | `fileexplorer` |
| **Total Source Classes** | 6 classes: `Main`, `FileExplorerFrame`, `FileManager`, `FileTableModel`, `FileProperties`, `FileUtils` |
| **Data Structures Used** | • `ArrayList<File>` (file table list & search results)<br>• `Stack<File>` (back & forward browser history)<br>• `LinkedHashMap<String, File>` (predictable Quick Access sidebar ordering)<br>• Native arrays `File[]` and `byte[]` |
| **Core GUI Components** | `JFrame`, `JTable`, `JSplitPane`, `JScrollPane`, `JToolBar`, `JPopupMenu`, `JDialog`, `JTextField`, `JButton`, `JLabel`, `JList`, `JComboBox` |
| **Layout Managers** | `BorderLayout`, `FlowLayout`, `BoxLayout`, `GridBagLayout` |
| **Event Handling** | • Delegation Event Model<br>• `ActionListener` (buttons & menus)<br>• `MouseAdapter` (double-click table rows & right-click popups)<br>• `KeyAdapter` (Enter in address bar & Esc in search)<br>• `InputMap` & `ActionMap` (global keyboard shortcuts) |
| **I/O & Streams** | `FileInputStream`, `FileOutputStream`, 8 KB byte buffer (`byte[8192]`), `File.renameTo()`, `File.delete()`, `File.listFiles()` |
| **Recursion Used In** | 1. `FileUtils.copyRecursively()` (deep folder copy)<br>2. `FileUtils.deleteRecursively()` (non-empty folder deletion)<br>3. `FileUtils.calculateTotalSize()` (folder size calculation)<br>4. `FileManager.collectAllFiles()` (recursive search with depth limit) |
| **Global Shortcuts** | `Ctrl+C` (Copy), `Ctrl+X` (Cut), `Ctrl+V` (Paste), `Ctrl+Shift+N` (New Folder), `F2` (Rename), `Delete` (Delete), `F5` (Refresh), `Alt+Enter` (Properties), `Alt+Left` / `Backspace` (Back), `Alt+Right` (Forward), `Alt+Up` (Up), `Ctrl+F` (Search) |

---

## 2. Elevator Pitches

### 2.1 The 2-Minute Presentation (Quick Viva Answer)
> *"Good morning, sir/ma'am. My project is **Desktop File Explorer**, a lightweight Windows File Explorer clone built in pure Java using Swing and AWT. It operates completely offline with zero external dependencies.*
> 
> *The system follows a clean Model-View-Controller architecture: `FileExplorerFrame` handles the presentation and layout; `FileTableModel` is our custom table model bridging the file data with Swing’s `JTable`; `FileManager` manages navigation history, the clipboard, and business logic; `FileProperties` displays metadata; and `FileUtils` provides static helper methods for byte-stream copying and recursive deletion.*
> 
> *The application implements all 10 mandatory features: browsing storage drives and quick access folders, opening files in default Windows applications via `Desktop.getDesktop().open()`, creating folders, renaming, deep copying, moving across drives, recursive deleting with confirmation prompts, searching by name, sorting by any column with folders grouped at the top, and displaying file properties.*
> 
> *Technically, the project demonstrates core university syllabus topics: OOP encapsulation, method overloading, inheritance, abstract classes, interfaces, the Collections framework (`List`, `Stack`, `LinkedHashMap`), byte-oriented I/O streams with buffers, recursion, `try-catch` exception handling, and GUI event handling with adapter classes and keyboard bindings."*

---

### 2.2 The 5-Minute Presentation (Comprehensive Demonstration)
> *"Good morning, sir/ma'am. I have developed a desktop application titled **Desktop File Explorer**. The goal of this project is to implement a robust, user-friendly file manager that replicates the essential capabilities of the Windows File Explorer while directly applying core Java syllabus principles.*
> 
> ***Architectural Organization:***  
> *The application is structured into six modular classes within the `fileexplorer` package:*
> 1. *`Main.java` is the application entry point. It sets the native Windows Look and Feel so the controls match the operating system, then safely schedules our frame onto the Swing Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater()`.*
> 2. *`FileExplorerFrame.java` is our primary view, extending `JFrame`. It builds a modern layout using `BorderLayout`, `JSplitPane`, and `BoxLayout`. It includes a top navigation bar (Back, Forward, Up, Refresh, Location, Search), a toolbar with button glyphs, a sidebar showing Quick Access and Available Drives, a central `JTable` rendering native file icons, and a 3-segment status bar showing item counts, clipboard state, and selection details.*
> 3. *`FileManager.java` is the controller. It encapsulates directory navigation using two `Stack<File>` instances to implement browser-style Back and Forward histories. It also manages an internal clipboard supporting both `COPY` and `CUT` modes.*
> 4. *`FileTableModel.java` extends `AbstractTableModel`. It manages an `ArrayList<File>` and overrides table queries while implementing custom sorting via `Collections.sort()` and `Comparator<File>`.*
> 5. *`FileProperties.java` extracts file metadata and displays a modal `JDialog` formatted with `GridBagLayout`.*
> 6. *`FileUtils.java` is a static utility class handling method overloading for size and date formatting, Windows filename validation, recursive folder copying using buffered `FileInputStream` and `FileOutputStream`, and recursive deletion.*
> 
> ***Feature Highlights & Problem Solving:***  
> *In addition to standard browsing, renaming, and folder creation, our application handles complex edge cases:*
> - *Cross-Drive Operations: When moving files across partitions (such as from C: to E:), atomic `File.renameTo()` fails in Windows. Our `FileManager.paste()` detects this and falls back to a recursive copy followed by recursive source deletion.*
> - *Accidental Recursion Guard: If a user attempts to cut or copy a directory into itself or its own subfolder, our code checks path prefixes and blocks the operation with an error dialog.*
> - *Context Menus & Shortcuts: We implemented two distinct right-click popup menus—one for table items and one for empty space—along with global window shortcuts like `Ctrl+C`, `Ctrl+V`, `F2`, and `Delete` via Swing's `InputMap` and `ActionMap`.*
> 
> *The project was developed strictly using standard Java SE, proving that clean architecture and native APIs can deliver a responsive, production-style desktop app."*

---

## 3. Project Architecture & Internal Workflow

### 3.1 Architectural Diagram (MVC Pattern)

```
                    +------------------------------------+
                    |             Main.java              |
                    |  (Sets Look & Feel, starts on EDT) |
                    +-----------------+------------------+
                                      |
                                      v
       +--------------------------------------------------------------+
       |                   FileExplorerFrame (VIEW)                   |
       |  - Top Navigation Bar (Back, Forward, Up, Refresh, Address)  |
       |  - Action Toolbar (New Folder, Cut, Copy, Paste, Delete...)  |
       |  - Left Sidebar (Quick Access JList, Drives JList)           |
       |  - Center Table (JTable with native icons & cell renderers)  |
       |  - Bottom Status Bar (Counts, Clipboard, Selection)          |
       |  - Context Menus (Item popup & Background popup)             |
       +--------------+-------------------------------+---------------+
                      |                               |
       User Events    |                               | Data Binding
       (Clicks, Keys) |                               v
                      v                     +-------------------+
       +----------------------------+       |  FileTableModel   |
       |     FileManager (CTRL)     |       |      (MODEL)      |
       | - currentDirectory: File   |<------+ - fileList: List  |
       | - backHistory: Stack       |       | - sort(): Comp.   |
       | - forwardHistory: Stack    |       +-------------------+
       | - clipboard: File & Mode   |
       +--------------+-------------+
                      |
        File Ops Call |
                      v
       +----------------------------+       +-------------------+
       |      FileUtils (UTIL)      |       |  FileProperties   |
       | - copyRecursively()        |       |    (VIEW/MODEL)   |
       | - deleteRecursively()      |       | - Metadata model  |
       | - formatSize() / Date()    |       | - Modal JDialog   |
       +----------------------------+       +-------------------+
```

---

### 3.2 Step-by-Step Lifecycle & Workflow

```
1. Main.main() starts
   |--> System.out.println("Desktop File Explorer Started Successfully!")
   |--> UIManager.setLookAndFeel(Windows System L&F)
   |--> SwingUtilities.invokeLater() queues GUI creation on EDT
        |
2. FileExplorerFrame constructor executes
   |--> Instantiates FileManager (defaults to user.home)
   |--> Instantiates FileTableModel
   |--> initializeUI() builds JFrame, SplitPane, Toolbar, Lists, Table
   |--> setupKeyboardShortcuts() registers InputMap & ActionMap
   |--> navigateTo(initialDir) executes
        |
3. navigateTo(File dir) pipeline
   |--> Verifies directory exists and isDirectory() == true
   |--> fileManager.navigateTo(dir): pushes old dir to backHistory Stack
   |--> refreshCurrentView():
        |--> txtAddressBar.setText(dir.getAbsolutePath())
        |--> tableModel.setFiles(fileManager.getFiles())
        |--> Updates button states (Back, Forward, Up, Paste)
        |--> FileUtils.countItems() updates Status Bar count
        |--> Displays items in JTable with native icons
        |
4. User Interaction Loop (Event Delegation)
   |--> Double-click folder  --> navigateTo(clickedFolder)
   |--> Double-click file    --> Desktop.getDesktop().open(file)
   |--> Click Drive in list  --> navigateTo(driveRoot)
   |--> Click Copy (Ctrl+C)  --> fileManager.copy(selectedFile)
   |--> Click Paste (Ctrl+V) --> fileManager.paste(targetDir) --> refreshCurrentView()
   |--> Press F5 / Refresh   --> refreshCurrentView()
   |--> Close Window         --> EXIT_ON_CLOSE terminates JVM
```

---

## 4. Class-by-Class Deep Dive & Code Explanations

### 4.1 `Main.java`
- **Location:** `src/fileexplorer/Main.java`
- **Purpose:** Entry point of the entire desktop application.
- **Key Responsibilities:**
  1. Displays the startup console banner.
  2. Sets the Windows Look and Feel so controls render with native system borders, buttons, and colors.
  3. Uses `SwingUtilities.invokeLater()` to launch the GUI on the Event Dispatch Thread (EDT).

#### Critical Code Snippet & Line-by-Line Explanation:
```java
public static void main(String[] args) {
    System.out.println("Desktop File Explorer Started Successfully!");

    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        System.err.println("Notice: Using default Swing theme.");
    }

    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            FileExplorerFrame frame = new FileExplorerFrame();
            frame.setVisible(true);
        }
    });
}
```
- **Line 2:** Prints the verification message required by the project specification.
- **Line 5:** `UIManager.getSystemLookAndFeelClassName()` asks the operating system (Windows) for its native GUI look-and-feel class string (e.g., `com.sun.java.swing.plaf.windows.WindowsLookAndFeel`).
- **Line 9:** `SwingUtilities.invokeLater()` places the GUI creation task on the event queue of the Event Dispatch Thread (EDT). This prevents race conditions and ensures thread safety.
- **Line 10:** An anonymous inner class implementing `Runnable` creates `FileExplorerFrame` and makes it visible.

---

### 4.2 `FileExplorerFrame.java`
- **Location:** `src/fileexplorer/FileExplorerFrame.java`
- **Purpose:** The primary presentation layer (GUI view and event coordinator).
- **Inheritance:** `extends JFrame implements ActionListener`
- **Key UI Fields:**
  - `fileTable`: Central `JTable` rendering files.
  - `txtAddressBar`: `JTextField` showing current path.
  - `txtSearch`: `JTextField` for name search.
  - `listQuickAccess`, `listDrives`: Two `JList` instances inside the left sidebar.
  - `btnBack`, `btnForward`, `btnUp`, `btnRefresh`: Navigation controls.
  - `btnNewFolder`, `btnCut`, `btnCopy`, `btnPaste`, `btnRename`, `btnDelete`, `btnProperties`: Toolbar buttons.
  - `itemContextMenu`, `backgroundContextMenu`: Two `JPopupMenu` instances.
  - `lblStatusCount`, `lblStatusClipboard`, `lblStatusSelection`: Bottom status labels.

#### Critical Code Snippet 1: Navigation Method (Fixing the Drive Navigation Bug)
```java
public void navigateTo(File directory) {
    if (directory == null) return;
    if (!directory.exists() || !directory.isDirectory()) {
        JOptionPane.showMessageDialog(this,
            "Cannot access directory: " + directory.getAbsolutePath() +
            "\nAccess is denied or the location does not exist.",
            "Access Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    boolean success = fileManager.navigateTo(directory);
    if (success) {
        refreshCurrentView();
    }
}
```
- **Explanation:** In the previous buggy version, clicking a sidebar drive loaded files directly without updating `fileManager.currentDirectory`. This centralized `navigateTo()` method now guarantees that `fileManager.navigateTo()` is called first. If valid, `refreshCurrentView()` updates the table, address bar, buttons, and status labels in a synchronized manner.

#### Critical Code Snippet 2: Global Keybinding via InputMap and ActionMap
```java
InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
ActionMap actionMap = getRootPane().getActionMap();

inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK), "copyAction");
actionMap.put("copyAction", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        handleCopy();
    }
});
```
- **Explanation:** `WHEN_IN_FOCUSED_WINDOW` binds the keystroke globally to the window, regardless of whether a button, list, or table currently holds input focus. This ensures shortcuts like `Ctrl+C` and `Ctrl+V` work reliably across the entire UI.

---

### 4.3 `FileManager.java`
- **Location:** `src/fileexplorer/FileManager.java`
- **Purpose:** Controller and business logic engine. Manages state, navigation history, clipboard, and file operations.
- **Key State Variables:**
  - `currentDirectory: File`
  - `backHistory: Stack<File>`
  - `forwardHistory: Stack<File>`
  - `clipboardFile: File`
  - `clipboardMode: ClipboardMode` (Enum: `COPY` or `CUT`)

#### Critical Code Snippet: Cross-Drive Move & Self-Copy Prevention (`paste`)
```java
public File paste(File targetDirectory) throws IOException {
    if (!canPaste()) {
        throw new IOException("Clipboard is empty or the source file no longer exists.");
    }
    if (targetDirectory == null || !targetDirectory.isDirectory()) {
        targetDirectory = currentDirectory;
    }

    // Safety Rule 1: Prevent pasting folder into itself or its subfolder
    if (clipboardFile.isDirectory()) {
        String sourcePath = clipboardFile.getAbsolutePath().toLowerCase();
        String targetPath = targetDirectory.getAbsolutePath().toLowerCase();
        if (targetPath.equals(sourcePath) || targetPath.startsWith(sourcePath + File.separator)) {
            throw new IOException("Cannot copy or move a folder into itself or its subfolder.");
        }
    }

    // Safety Rule 2: Harmless cut-and-paste in the exact same folder
    if (clipboardMode == ClipboardMode.CUT && targetDirectory.equals(clipboardFile.getParentFile())) {
        File original = clipboardFile;
        clearClipboard();
        return original;
    }

    File destination = FileUtils.getUniqueDestinationFile(targetDirectory, clipboardFile.getName());

    if (clipboardMode == ClipboardMode.COPY) {
        FileUtils.copyRecursively(clipboardFile, destination);
    } else if (clipboardMode == ClipboardMode.CUT) {
        // Attempt fast atomic rename first (works within same drive partition)
        boolean moved = clipboardFile.renameTo(destination);
        if (!moved) {
            // Cross-drive partition move: copy recursively then delete source
            FileUtils.copyRecursively(clipboardFile, destination);
            FileUtils.deleteRecursively(clipboardFile);
        }
        clearClipboard();
    }
    return destination;
}
```
- **Explanation:** Demonstrates defensive file programming. Checks for recursion loops (`startsWith`), prevents unnecessary file collisions, attempts fast atomic moves on the same drive partition, and falls back to stream copying plus recursive deletion across separate partitions (e.g., C: to E:).

---

### 4.4 `FileTableModel.java`
- **Location:** `src/fileexplorer/FileTableModel.java`
- **Purpose:** Custom table model connecting the `List<File>` dataset with Swing’s `JTable`.
- **Inheritance:** `extends AbstractTableModel`
- **Columns (4):** `Name` (Index 0), `Type` (Index 1), `Size` (Index 2), `Date Modified` (Index 3).

#### Critical Code Snippet: Custom Multi-Factor Comparator for Sorting
```java
public void sort(int columnIndex, boolean ascending) {
    this.currentSortColumn = columnIndex;
    this.isAscending = ascending;

    Comparator<File> comparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            // Primary Rule: Folders always appear before files
            if (f1.isDirectory() && !f2.isDirectory()) return -1;
            if (!f1.isDirectory() && f2.isDirectory()) return 1;

            int result = 0;
            switch (columnIndex) {
                case 0: // Name
                    result = f1.getName().compareToIgnoreCase(f2.getName());
                    break;
                case 1: // Type
                    result = FileUtils.getFileType(f1).compareToIgnoreCase(FileUtils.getFileType(f2));
                    break;
                case 2: // Size (Long wrapper compare)
                    long s1 = f1.isDirectory() ? 0L : f1.length();
                    long s2 = f2.isDirectory() ? 0L : f2.length();
                    result = Long.compare(s1, s2);
                    break;
                case 3: // Date Modified
                    result = Long.compare(f1.lastModified(), f2.lastModified());
                    break;
            }
            return ascending ? result : -result;
        }
    };

    Collections.sort(this.fileList, comparator);
    fireTableDataChanged();
}
```
- **Explanation:** Overrides default sorting behavior. Folders are always grouped at the top. It uses the `Comparator` interface and wrapper class comparisons (`Long.compare`), then notifies the table view to repaint via `fireTableDataChanged()`.

---

### 4.5 `FileProperties.java`
- **Location:** `src/fileexplorer/FileProperties.java`
- **Purpose:** Encapsulates file metadata extraction and renders a modal properties window.
- **Key Fields:** `name`, `location`, `type`, `sizeBytes`, `formattedSize`, `dateModified`, `directory`, `readable`, `writable`, `hidden`, `folderCount`, `fileCount`.

#### Critical Code Snippet: Layout Manager Setup (`GridBagLayout`)
```java
JPanel bodyPanel = new JPanel(new GridBagLayout());
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(4, 4, 4, 4);
gbc.fill = GridBagConstraints.HORIZONTAL;

addPropertyRow(bodyPanel, gbc, 0, "Type:", props.getType());
addPropertyRow(bodyPanel, gbc, 1, "Location:", props.getLocation());
addPropertyRow(bodyPanel, gbc, 2, "Size:", props.getFormattedSize());
if (props.isDirectory()) {
    addPropertyRow(bodyPanel, gbc, 3, "Contains:", props.getFileCount() + " Files, " + props.getFolderCount() + " Folders");
}
```
- **Explanation:** Demonstrates `GridBagLayout`, the most versatile AWT layout manager. It arranges label-value pairs cleanly into columns using grid coordinates and insets (padding).

---

### 4.6 `FileUtils.java`
- **Location:** `src/fileexplorer/FileUtils.java`
- **Purpose:** Static utility class providing reusable algorithms for file manipulation, recursion, stream copying, and validation.
- **Constructor:** `private FileUtils() {}` (prevents unwanted instantiation).

#### Critical Code Snippet: Buffered Stream Copying & Windows Read-Only Deletion
```java
// Buffered Stream Copying
try (FileInputStream inStream = new FileInputStream(source);
     FileOutputStream outStream = new FileOutputStream(destination)) {
    byte[] buffer = new byte[8192]; // 8 KB buffer
    int length;
    while ((length = inStream.read(buffer)) > 0) {
        outStream.write(buffer, 0, length);
    }
}
destination.setLastModified(source.lastModified());

// Recursive Deletion
public static boolean deleteRecursively(File fileOrDir) {
    if (fileOrDir == null || !fileOrDir.exists()) return false;
    if (fileOrDir.isDirectory()) {
        File[] children = fileOrDir.listFiles();
        if (children != null) {
            for (File child : children) {
                if (!deleteRecursively(child)) return false;
            }
        }
    }
    fileOrDir.setWritable(true); // Windows fix: allow deleting read-only files
    return fileOrDir.delete();
}
```
- **Explanation:** 
  1. Uses Java 7+ **try-with-resources** to guarantee streams are closed automatically.
  2. Uses an 8 KB byte buffer for efficient I/O transfer instead of reading byte-by-byte.
  3. Uses post-order recursion to delete directory contents from the bottom up.
  4. Calls `setWritable(true)` so Windows allows deleting files marked as read-only.

---

## 5. Java Syllabus Concepts Mapped to Source Code

### Section A: Core Java & OOP Concepts
1. **Classes and Objects:**
   - Every file defines a blueprint. Instantiated via `new FileExplorerFrame()`, `new FileManager()`, and `new FileProperties()`.
2. **Constructors & Constructor Overloading:**
   - `FileTableModel()` (default empty constructor) and `FileTableModel(List<File> files)` (parameterized constructor).
3. **Encapsulation:**
   - All state fields in `FileManager` (`currentDirectory`, `clipboardFile`, `backHistory`) and `FileProperties` are marked `private` and accessed via public getters (`getCurrentDirectory()`, `canPaste()`).
4. **Method Overloading (Compile-Time Polymorphism):**
   - `FileUtils.formatSize(long bytes)` vs `FileUtils.formatSize(File file)`.
   - `FileUtils.formatDate(long millis)` vs `FileUtils.formatDate(File file)`.
   - `FileManager.search(String query)` vs `FileManager.search(File startDir, String query, boolean recursive)`.
5. **Inheritance:**
   - `FileExplorerFrame extends JFrame`: inherits window features.
   - `FileTableModel extends AbstractTableModel`: inherits table event listener dispatchers.
6. **Interfaces:**
   - `ActionListener`: implemented by `FileExplorerFrame`.
   - `Comparator<File>`: implemented anonymously in `FileTableModel.sort()`.
   - `ListSelectionListener`: used for sidebar and table row selection.
7. **Abstract Classes:**
   - `AbstractTableModel`: provides default event listener management while requiring us to implement `getRowCount()`, `getColumnCount()`, and `getValueAt()`.
8. **Inner & Anonymous Classes:**
   - `SidebarItem`: static nested helper class inside `FileExplorerFrame`.
   - Anonymous classes used for `new Runnable()`, `new MouseAdapter()`, and button action listeners.
9. **String Handling:**
   - `substring()`, `lastIndexOf()`, `toLowerCase()`, `indexOf()`, `trim()`, and `String.format()`.
10. **Wrapper Classes:**
    - `Long.compare()`, `Integer`, and `Boolean` conversions.

---

### Section B: Collections, Streams & Error Handling
1. **Collections Framework:**
   - `List` / `ArrayList`: dynamically resized storage for table rows and search results.
   - `Stack`: LIFO (Last-In-First-Out) data structure used for `backHistory` and `forwardHistory`.
   - `Map` / `LinkedHashMap`: key-value mapping for Quick Access paths with insertion-order preservation.
2. **Iterators:**
   - Explicit `Iterator<File> iterator = allItems.iterator()` in `FileManager.search()`.
3. **Utility Classes:**
   - `Collections.sort()` for sorting lists; `Arrays.asList()` for array conversion.
4. **Exception Handling:**
   - `try-catch-finally` and try-with-resources.
   - Throws descriptive `java.io.IOException` instances on illegal names or failed operations.
   - Catches `SecurityException` when attempting to list restricted system folders.
5. **Byte Streams & I/O:**
   - `FileInputStream` and `FileOutputStream` with `byte[] buffer = new byte[8192]`.

---

### Section C: GUI, Layouts & Event Handling
1. **Swing vs AWT:**
   - Heavyweight AWT components rely on OS peers; lightweight Swing components are written in pure Java and support pluggable themes.
2. **Layout Managers:**
   - `BorderLayout`: Main frame division (North, Center, South).
   - `BoxLayout`: Vertical arrangement of navigation bars and sidebar sections.
   - `FlowLayout`: Horizontal grouping of navigation buttons and search controls.
   - `GridBagLayout`: Flexible grid of label-value pairs in the Properties dialog.
3. **Delegation Event Model:**
   - Event Source (e.g., `JButton`) $\rightarrow$ Event Object (`ActionEvent`) $\rightarrow$ Event Listener (`ActionListener`).
4. **Adapter Classes:**
   - `MouseAdapter` and `KeyAdapter` allow overriding only the required methods (`mouseClicked`, `keyPressed`) without cluttering code with unused interface methods.

---

## 6. Internal Working of All 10 Features & GUI Systems

### Feature 1: Browse Drives and Folders
- `FileManager.getSystemDrives()` invokes `File.listRoots()` to identify all attached drive letters (C:, D:, E:).
- Quick Access scans standard directories via `System.getProperty("user.home")`.
- When an item is clicked, `navigateTo(targetDirectory)` updates `currentDirectory`, pushes the prior directory onto `backHistory`, clears `forwardHistory`, and repaints the table.

### Feature 2: Open Files and Directories
- Double-clicking an item triggers `MouseAdapter.mouseClicked()`.
- If the target is a directory, it calls `navigateTo(folder)`.
- If it is a file, it calls `Desktop.getDesktop().open(file)`, delegating file execution to Windows default software (e.g., Notepad, Word, or Media Player).

### Feature 3: Create Folder
- `JOptionPane.showInputDialog` requests the new folder name.
- `FileUtils.isValidFileName()` validates that the name contains no illegal characters (`\/:*?"<>|`) or reserved Windows device names (`CON`, `PRN`, `AUX`, `NUL`).
- `File.mkdir()` creates the directory, and `refreshCurrentView()` updates the display.

### Feature 4: Rename File or Folder
- Prompts user for a new name pre-populated with the current name.
- Verifies that no item in the parent directory already uses that name.
- Invokes `File.renameTo()`, followed by `refreshCurrentView()`.

### Feature 5: Copy Files & Folders
- `FileManager.copy(file)` stores the target in `clipboardFile` and sets `clipboardMode = ClipboardMode.COPY`.
- On paste, `FileUtils.copyRecursively()` reads and writes file bytes using buffered `FileInputStream` / `FileOutputStream`.
- If the destination already contains a file with that name, `FileUtils.getUniqueDestinationFile()` appends `" - Copy"`.

### Feature 6: Move / Cut Files & Folders
- `FileManager.cut(file)` sets `clipboardMode = ClipboardMode.CUT`.
- On paste, it first attempts an atomic move via `File.renameTo(destination)`.
- If moving across different physical drives, `renameTo()` returns `false`. The system falls back to copying the tree recursively to the destination and deleting the original source.

### Feature 7: Delete with Confirmation
- Displays `JOptionPane.showConfirmDialog()` with a warning icon to prevent accidental deletion.
- `FileUtils.deleteRecursively()` performs a post-order traversal, removing child files and subfolders before deleting the target directory itself.
- Marks files writable (`setWritable(true)`) to handle Windows read-only locks.

### Feature 8: Search Files & Folders
- Retrieves search query from `txtSearch`.
- `FileManager.search()` filters matching files in the current folder using an `Iterator<File>` and case-insensitive string matching (`contains()`).
- Recursively inspects subdirectories up to a depth limit of 3 to keep the interface fast and responsive.

### Feature 9: Sort by Name, Type, Size, and Date
- Triggered by column header clicks or the toolbar combo box.
- `FileTableModel.sort()` uses a custom `Comparator<File>` that keeps folders grouped at the top, then sorts by column values.
- Calls `fireTableDataChanged()` to refresh the table rows.

### Feature 10: File Properties Dialog
- Extracts metadata via `FileProperties`: size in bytes, formatted size (KB/MB), last modified timestamp, and permissions.
- Recursively counts child files and folders if the item is a directory.
- Renders a modal `JDialog` formatted with `GridBagLayout`.

### Context Menus & Keyboard Shortcuts
- Two `JPopupMenu` instances are registered: one on the table rows and one on empty background space.
- Global key bindings registered via `InputMap` and `ActionMap` on `getRootPane()` ensure shortcuts work regardless of which component currently holds focus.

---

## 7. 75 High-Yield Viva Questions and Detailed Answers

### Category 1: Project Overview & Architecture
#### Q1. What is the title and primary objective of this project?
**Answer:** The project is titled **Desktop File Explorer**. Its primary objective is to develop a lightweight, standalone desktop file manager in Java that replicates the core capabilities of Windows File Explorer while demonstrating core concepts from our university Java syllabus.

#### Q2. Why did you use Java Swing instead of JavaFX or web frameworks?
**Answer:** Java Swing and AWT are built directly into standard Java (JDK), requiring zero external dependencies, build tools, or runtime web containers. This keeps the project simple, fully offline, and directly aligned with our college coursework.

#### Q3. What architectural design pattern does your project follow?
**Answer:** It follows the **Model-View-Controller (MVC)** design pattern:
- **Model:** `FileTableModel` and `FileProperties` manage file data and properties.
- **View:** `FileExplorerFrame` handles Swing components, layouts, and menus.
- **Controller:** `FileManager` manages navigation history, clipboard state, and user actions.

#### Q4. Which package contains your application code?
**Answer:** All source files reside in the custom package `fileexplorer`.

#### Q5. Name the six Java source files in your project.
**Answer:** `Main.java`, `FileExplorerFrame.java`, `FileManager.java`, `FileTableModel.java`, `FileProperties.java`, and `FileUtils.java`.

#### Q6. How does your application achieve a native Windows look and feel?
**Answer:** In `Main.java`, before creating any GUI components, we invoke:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```
This applies the Windows OS visual theme to all buttons, scrollbars, dialogs, and table headers.

#### Q7. What are the minimum system requirements to run this project?
**Answer:** Any computer with Java Runtime Environment (JRE) or Java Development Kit (JDK) version 8 or higher (we used JDK 22.0.1) running Windows.

#### Q8. Does your application use any database? How is data stored?
**Answer:** No external database is used. The application reads and writes directly to the local Windows NTFS/FAT32 file system using Java's standard File and I/O APIs.

---

### Category 2: Java OOP & Language Fundamentals
#### Q9. Where are Classes and Objects demonstrated in your project?
**Answer:** Each `.java` file defines a class. When the application starts, instances such as `new FileExplorerFrame()`, `new FileManager()`, and `new FileProperties(file)` are allocated as concrete objects on the Java heap.

#### Q10. What is Constructor Overloading and where did you use it?
**Answer:** Constructor overloading means defining multiple constructors with different parameter signatures in the same class. In `FileTableModel.java`, we provide both a no-argument constructor `FileTableModel()` and a parameterized constructor `FileTableModel(List<File> files)`.

#### Q11. How is Encapsulation achieved in `FileManager`?
**Answer:** Internal state variables (like `currentDirectory`, `clipboardFile`, and `backHistory`) are declared `private`. External classes cannot alter them directly; they must call public methods like `navigateTo()`, `copy()`, and `getCurrentDirectory()`.

#### Q12. What is Method Overloading? Give examples from your project.
**Answer:** Method overloading is defining methods with the same name but different parameters within the same class. Examples:
- `FileUtils.formatSize(long bytes)` vs `FileUtils.formatSize(File file)`
- `FileUtils.formatDate(long millis)` vs `FileUtils.formatDate(File file)`
- `FileManager.search(String query)` vs `FileManager.search(File startDir, String query, boolean recursive)`

#### Q13. Why are methods in `FileUtils` declared `static`?
**Answer:** Because they are stateless utility functions that operate purely on the arguments passed into them (e.g., formatting bytes or validating a name). Making them `static` allows them to be called directly as `FileUtils.formatSize(...)` without creating an object.

#### Q14. Why is the constructor of `FileUtils` declared `private`?
**Answer:** To prevent accidental instantiation using `new FileUtils()`. Utility classes containing only static methods should never be instantiated.

#### Q15. Where did you use String manipulation methods?
**Answer:** In `FileUtils` and `FileManager`:
- `lastIndexOf('.')` and `substring()` to extract file extensions.
- `toLowerCase()` and `contains()` for case-insensitive searching.
- `trim()` to remove leading and trailing whitespace from user input.
- `String.format()` for formatted size and item count strings.

#### Q16. What is a Wrapper Class and where did you use one?
**Answer:** A wrapper class wraps primitive data types into objects (e.g., `Long`, `Integer`, `Boolean`). In `FileTableModel.sort()`, we use `Long.compare(f1.length(), f2.length())` to sort files by size and last-modified dates.

#### Q17. What are Access Specifiers and how did you choose them?
**Answer:** 
- `private`: Encapsulates internal variables and helper methods.
- `public`: Exposes external methods (e.g., `navigateTo()`, `paste()`).
- Package-private (default): Used for helper classes like `SidebarItem` accessed only within `fileexplorer`.

#### Q18. What is the role of the `final` keyword in your code?
**Answer:**
- On constants: `private static final SimpleDateFormat DATE_FORMAT` ensures the reference cannot be reassigned.
- On fields: `private final FileManager fileManager` ensures the reference remains immutable throughout the frame's lifecycle.

#### Q19. What is an Enumeration (`enum`) and where is it used?
**Answer:** An enum defines a fixed set of named constants. In `FileManager.java`, we define:
```java
public enum ClipboardMode { COPY, CUT }
```
This clearly distinguishes between copy and move operations without relying on arbitrary numbers or strings.

#### Q20. What is an Anonymous Inner Class? Give an example from your project.
**Answer:** An inner class declared without a class name that simultaneously defines and instantiates an object. Example:
```java
SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() { new FileExplorerFrame().setVisible(true); }
});
```

---

### Category 3: Inheritance, Interfaces & Abstract Classes
#### Q21. Where is Inheritance used in your project?
**Answer:**
- `FileExplorerFrame extends JFrame`: inherits top-level window capabilities, menu bar support, and window event listeners.
- `FileTableModel extends AbstractTableModel`: inherits table event dispatchers and column management logic.

#### Q22. What is an Abstract Class? How does `AbstractTableModel` work?
**Answer:** An abstract class cannot be instantiated directly and often provides partial implementation. `AbstractTableModel` implements core listener management and firing mechanisms, while requiring our subclass `FileTableModel` to implement three required methods: `getRowCount()`, `getColumnCount()`, and `getValueAt()`.

#### Q23. What is an Interface? Which interfaces are implemented in your project?
**Answer:** An interface is a contract containing abstract method declarations. Our project implements:
- `ActionListener`: handles button clicks and menu actions via `actionPerformed()`.
- `Comparator<File>`: defines custom comparison logic in `FileTableModel.sort()`.
- `ListSelectionListener`: listens for row selections in tables and lists.

#### Q24. What is the difference between an Abstract Class and an Interface?
**Answer:** An abstract class can have instance variables and concrete methods with state. An interface primarily defines method signatures and constants. A class can extend only one abstract class (single inheritance), but can implement multiple interfaces.

#### Q25. Why does `FileExplorerFrame` implement `ActionListener`?
**Answer:** By implementing `ActionListener`, the frame itself serves as the centralized event handler for buttons and menu items via its `actionPerformed(ActionEvent e)` method.

#### Q26. Where did you use the `super` keyword?
**Answer:** In `FileExplorerFrame` constructor: `super("Desktop File Explorer");` calls the constructor of `JFrame` to set the window title bar.

#### Q27. What is method overriding? Give an example from your code.
**Answer:** Method overriding occurs when a subclass provides its own specific implementation of a method defined in its parent class. In `FileTableModel`, we override `getRowCount()`, `getColumnCount()`, `getValueAt()`, and `getColumnName()`.

#### Q28. What does `@Override` do?
**Answer:** It is a compiler annotation that ensures the annotated method matches a signature in the parent class or interface. If the name or parameters are mismatched, the compiler generates an error.

---

### Category 4: Collections Framework & Data Structures
#### Q29. Which Collection interfaces and classes did you use?
**Answer:**
- `List` implemented by `ArrayList`: dynamic arrays for file listings and search results.
- `Stack`: LIFO collection for Back and Forward navigation histories.
- `Map` implemented by `LinkedHashMap`: key-value pairs for Quick Access paths preserving insertion order.

#### Q30. Why did you use `Stack<File>` for navigation history?
**Answer:** A `Stack` follows Last-In-First-Out (LIFO) semantics, matching how browser history works:
- Visiting folder B pushes current folder A onto `backHistory`.
- Clicking Back pops folder A off the stack and pushes B onto `forwardHistory`.

#### Q31. Why did you use `LinkedHashMap` instead of `HashMap` for Quick Access?
**Answer:** A standard `HashMap` does not guarantee any iteration order. `LinkedHashMap` maintains a doubly linked list through its entries, preserving the exact order in which Quick Access folders (Home, Desktop, Downloads, Documents) are added.

#### Q32. How does `Collections.sort()` work in your project?
**Answer:** In `FileTableModel.sort()`, we pass our `fileList` and a custom `Comparator<File>` to `Collections.sort()`. The algorithm sorts items by comparing pairs of files based on whether they are directories, and then by their name, type, size, or timestamp.

#### Q33. Why did you use `Comparator` instead of `Comparable`?
**Answer:** `Comparable` allows only one natural sorting order inside the target class (`File`). Since `java.io.File` cannot be modified, and our application needs multiple sorting criteria (by Name, by Size, by Date, ascending, descending), we used `Comparator` to define external sorting strategies dynamically.

#### Q34. Where did you use an `Iterator`? Write the snippet.
**Answer:** In `FileManager.search()`:
```java
Iterator<File> iterator = allItems.iterator();
while (iterator.hasNext()) {
    File item = iterator.next();
    if (item.getName().toLowerCase().contains(lowerQuery)) {
        matches.add(item);
    }
}
```

#### Q35. What is the advantage of using an `Iterator` over a standard for-loop?
**Answer:** An `Iterator` provides a uniform, collection-agnostic way to traverse elements. It abstracts away the underlying data structure and safely supports element removal during iteration without index-shifting bugs.

#### Q36. What is the time complexity of searching files in a directory?
**Answer:** $O(N)$, where $N$ is the number of files and folders in that directory, because every item's name is inspected once.

#### Q37. What is `Collections.unmodifiableList()` and why is it used?
**Answer:** In `FileTableModel.getFiles()`, returning `Collections.unmodifiableList(this.fileList)` prevents external classes from modifying the internal collection without going through the model's update methods.

#### Q38. How does `ArrayList` differ from a regular Java array (`File[]`)?
**Answer:** A standard array has a fixed size defined at creation. An `ArrayList` is dynamically resizable; it automatically expands its internal capacity as new files are added.

---

### Category 5: Exception Handling & Robustness
#### Q39. What is an Exception in Java?
**Answer:** An abnormal condition or event that disrupts the normal execution flow of a program at runtime.

#### Q40. What is the difference between Checked and Unchecked Exceptions?
**Answer:**
- **Checked Exceptions:** Inherit from `java.lang.Exception` (excluding runtime exceptions). Checked at compile time; must be caught or declared (e.g., `IOException`).
- **Unchecked Exceptions:** Inherit from `java.lang.RuntimeException`. Not checked at compile time (e.g., `NullPointerException`, `IllegalArgumentException`).

#### Q41. Where is `IOException` handled in your project?
**Answer:** In all file operations: `createFolder()`, `rename()`, `copyRecursively()`, `paste()`, and `delete()`. If an operation fails, the exception is caught in `FileExplorerFrame`, and a user-friendly error dialog is displayed via `JOptionPane.showMessageDialog()`.

#### Q42. What is Try-With-Resources and where is it used?
**Answer:** Introduced in Java 7, it automatically closes resources implementing `AutoCloseable` at the end of the block. In `FileUtils.copyRecursively()`:
```java
try (FileInputStream in = new FileInputStream(source);
     FileOutputStream out = new FileOutputStream(destination)) {
    // Read and write operations
} // in and out are closed automatically here, even if an exception occurs
```

#### Q43. How do you handle Permission Denied errors on Windows?
**Answer:** When accessing system folders like `C:\System Volume Information`, `File.listFiles()` returns `null` or throws a `SecurityException`. In `FileManager.getFiles()`, we catch `SecurityException` and check `if (list != null)` before processing, preventing `NullPointerException` crashes.

#### Q44. How do you prevent accidental deletion of important files?
**Answer:** Before invoking `fileManager.delete()`, `FileExplorerFrame` presents a modal confirmation dialog:
```java
int confirm = JOptionPane.showConfirmDialog(this,
    "Are you sure you want to permanently delete this...?",
    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
```
The deletion only proceeds if the user explicitly clicks `YES`.

#### Q45. What happens if a user inputs an illegal folder name like `Test:Folder*`?
**Answer:** `FileUtils.isValidFileName()` checks the string against Windows forbidden characters (`\/:*?"<>|`) and device names (`CON`, `PRN`, `AUX`, `NUL`). If an invalid character is found, it throws an `IOException` explaining why the name was rejected.

#### Q46. What happens if you try to delete a read-only file on Windows?
**Answer:** In Windows, calling `File.delete()` on a read-only file returns `false`. In `FileUtils.deleteRecursively()`, we explicitly execute `fileOrDir.setWritable(true)` before `delete()`, clearing the read-only attribute so the file can be removed.

---

### Category 6: File Handling, Streams & I/O Operations
#### Q47. What does the `java.io.File` class represent?
**Answer:** It represents an abstract pathname for files and directories on disk. It provides metadata inspection (`exists()`, `length()`, `isDirectory()`, `canRead()`, `lastModified()`) and filesystem actions (`mkdir()`, `renameTo()`, `delete()`), but does not directly read or write file contents.

#### Q48. How does `FileInputStream` and `FileOutputStream` work in your copy feature?
**Answer:** `FileInputStream` opens a readable byte channel from the source file. `FileOutputStream` opens a writable byte channel to the destination file. Data is transferred through a shared 8 KB buffer (`byte[8192]`) in a loop until `inStream.read()` returns -1 (end of file).

#### Q49. Why did you use an 8 KB buffer (`byte[8192]`) instead of reading byte-by-byte?
**Answer:** Reading byte-by-byte causes a hardware system call for every single byte, which is very slow. A buffer reads and writes chunks of 8,192 bytes at once, dramatically reducing I/O system calls and speeding up transfers.

#### Q50. How does recursive copy work for directory trees?
**Answer:** In `FileUtils.copyRecursively(File src, File dest)`:
1. If `src` is a file, it copies the bytes via streams.
2. If `src` is a directory, it creates the destination folder via `dest.mkdirs()`.
3. It lists all children via `src.listFiles()`.
4. It iterates over each child and calls `copyRecursively(child, new File(dest, child.getName()))`.

#### Q51. Why is recursion required to delete a folder in Java?
**Answer:** Standard Java `File.delete()` only deletes a directory if it is completely empty. If it contains even one file or subfolder, `delete()` fails. Recursion is required to traverse down to the deepest leaf files, delete them first, and then delete the parent directory on the way back up (post-order traversal).

#### Q52. What is an atomic rename and why does it fail across drives?
**Answer:** `File.renameTo()` asks the operating system filesystem driver to update the file's path entry in the master file table. This is instantaneous (atomic) within the same disk partition. However, across different physical drives or partitions (e.g., C: to E:), an atomic rename is impossible because bytes must actually be transferred across physical storage sectors.

#### Q53. How does your Cut/Move feature handle cross-drive moves?
**Answer:** In `FileManager.paste()`:
1. It first attempts `clipboardFile.renameTo(destination)`.
2. If `renameTo()` returns `false` (which happens across drives), it falls back to calling `FileUtils.copyRecursively()` followed by `FileUtils.deleteRecursively()` on the original file.

#### Q54. How do you prevent copying or moving a folder into its own subdirectory?
**Answer:** We compare path strings:
```java
String sourcePath = clipboardFile.getAbsolutePath().toLowerCase();
String targetPath = targetDirectory.getAbsolutePath().toLowerCase();
if (targetPath.equals(sourcePath) || targetPath.startsWith(sourcePath + File.separator)) {
    throw new IOException("Cannot copy or move a folder into itself or its subfolder.");
}
```

#### Q55. How do you launch a file in its default Windows program?
**Answer:** Using the Java AWT Desktop integration:
```java
Desktop.getDesktop().open(file);
```
Windows inspects the file extension (e.g., `.pdf`, `.mp4`, `.txt`) and launches the associated default program.

#### Q56. How do you obtain native Windows file and folder icons?
**Answer:** Using Swing's `FileSystemView`:
```java
Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
```
This fetches the authentic Windows shell icon used in the operating system.

---

### Category 7: Swing GUI, AWT, Layout Managers & Event Handling
#### Q57. What is the difference between AWT and Swing?
**Answer:**
- **AWT (Abstract Window Toolkit):** Heavyweight components backed by native OS window peers. Platform-dependent look and limited component set.
- **Swing:** Lightweight components written entirely in Java. Pluggable Look and Feel, richer controls (`JTable`, `JSplitPane`), and double-buffered rendering.

#### Q58. What is the Event Dispatch Thread (EDT)? Why is it important?
**Answer:** Swing components are not thread-safe. To prevent race conditions and screen flickering, all GUI updates, painting, and event dispatching must happen on a single dedicated background thread called the Event Dispatch Thread (EDT).

#### Q59. What Layout Managers did you use and where?
**Answer:**
1. `BorderLayout`: Main window frame (North for navigation/toolbar, Center for split pane, South for status bar).
2. `BoxLayout`: Top container and sidebar vertical stacks.
3. `FlowLayout`: Left and right navigation button groups.
4. `GridBagLayout`: Detailed label-value alignment in `FileProperties` dialog.

#### Q60. How does `JSplitPane` work in your UI?
**Answer:** `JSplitPane` divides the window into two resizable compartments. We set `JSplitPane.HORIZONTAL_SPLIT` with the sidebar on the left and the file table on the right, allowing the user to drag the divider to adjust widths.

#### Q61. How does `JTable` render custom file icons?
**Answer:** Column 0 has a custom `TableCellRenderer` (`DefaultTableCellRenderer`). In `getTableCellRendererComponent()`, it takes the `File` object, retrieves its system icon via `FileSystemView.getSystemIcon(file)`, sets `label.setIcon(icon)`, and formats the name.

#### Q62. What is the Delegation Event Model?
**Answer:** A standard Java pattern where the event source (e.g., `JButton`) does not handle the event itself. Instead, it delegates the work to a registered listener object (e.g., `ActionListener`) when an action occurs.

#### Q63. What is an Event Adapter Class? Give an example.
**Answer:** A convenience class that implements a listener interface with default empty methods. Instead of implementing all 5 methods of `MouseListener`, we extend `MouseAdapter` and override only `mouseClicked()` and `mousePressed()`.

#### Q64. How did you implement global keyboard shortcuts?
**Answer:** Using Swing's `InputMap` and `ActionMap` on `getRootPane()`:
```java
InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refreshAction");
actionMap.put("refreshAction", new AbstractAction() { ... });
```
This guarantees shortcuts like `F5`, `Ctrl+C`, and `Delete` trigger regardless of which component has focus.

#### Q65. Why did you use `InputMap`/`ActionMap` instead of `KeyListener`?
**Answer:** `KeyListener` only fires when the specific component it is attached to currently has keyboard focus. If focus shifts to a button or list, `KeyListener` stops responding. `InputMap` with `WHEN_IN_FOCUSED_WINDOW` listens globally across the entire window.

#### Q66. How does right-click popup menu triggering work across operating systems?
**Answer:** Different operating systems trigger popups on different mouse events (Windows on `mouseReleased`, Unix on `mousePressed`). To be cross-platform, we check `e.isPopupTrigger()` inside both `mousePressed()` and `mouseReleased()` methods of `MouseAdapter`.

#### Q67. How does the empty folder message ("This folder is empty.") appear?
**Answer:** In `FileExplorerFrame`, we override `paintComponent(Graphics g)` on the `JTable`. If `getRowCount() == 0`, it draws the centered text `"This folder is empty."` directly on the empty table surface.

#### Q68. What does `tableModel.fireTableDataChanged()` do?
**Answer:** It sends a `TableModelEvent` to all registered listeners (primarily the `JTable`), notifying them that the row data has changed and instructing the table to repaint its rows.

---

### Category 8: Advanced Scenarios, Edge Cases & Bug Fixes
#### Q69. What was the Drive Navigation bug and how did you resolve it?
**Answer:** Previously, selecting a drive in the sidebar updated the address bar text but failed to invoke `fileManager.navigateTo(drive)`. As a result, `fileManager.currentDirectory` stayed unchanged, and files from the old directory kept displaying. We resolved this by creating a centralized `navigateTo(File)` method that updates `FileManager` state before calling `refreshCurrentView()`, and attached mouse listeners to ensure re-clicking a drive always refreshes the view.

#### Q70. How does your app prevent name collisions when copying a file into the same directory?
**Answer:** `FileUtils.getUniqueDestinationFile()` checks `if (candidate.exists())`. If so, it splits the file name and extension and checks sequential names: `"Filename - Copy.ext"`, `"Filename - Copy (2).ext"`, etc., until an unused name is found.

#### Q71. How do you distinguish between clicking a file and clicking a folder in the table?
**Answer:** We call `selectedFile.isDirectory()`. If true, it is a directory; otherwise, it is a file.

#### Q72. How did you prevent recursive event loops between the two sidebar lists?
**Answer:** When selecting an item in `listDrives`, we clear selection in `listQuickAccess`. To prevent that clearing action from re-triggering `listQuickAccess`'s selection listener, we use a boolean guard flag `isUpdatingSidebarSelection = true` during the transition.

#### Q73. What is the difference between `File.mkdir()` and `File.mkdirs()`?
**Answer:**
- `mkdir()` creates only the single named directory; fails if parent directories do not exist.
- `mkdirs()` creates the named directory along with all necessary but nonexistent parent directories in the path.

#### Q74. Can your application run on Linux or macOS?
**Answer:** Yes. Pure Java is cross-platform. While we optimized the visual Look and Feel and drive listing for Windows, all methods use `File.separator` and standard Java SE APIs, allowing the code to compile and run on Linux or macOS as well.

#### Q75. If you had an extra week, what feature would you add next?
**Answer:** Asynchronous background file copying with a visual `JProgressBar` using Swing's `SwingWorker`, so copying huge multi-gigabyte files doesn't temporarily block the main window thread.

---

## 8. Project Limitations and Future Scope

### Current Limitations:
1. **Single-Threaded File Operations:** File copies and recursive deletions run synchronously on the Event Dispatch Thread (EDT). For extremely large files (e.g., 10 GB), the UI briefly pauses until the loop completes.
2. **Permanent Deletion:** Files are deleted permanently via `File.delete()` rather than moved to the native Windows Recycle Bin.
3. **No File Content Editing:** The explorer opens files with external programs rather than providing an embedded hex/text editor.
4. **Single-Item Selection:** Operations currently target one selected item at a time rather than multi-item selection.

### Future Scope & Enhancements:
1. **Multithreaded Transfers (`SwingWorker`):** Offload long-running stream copy operations to background worker threads with interactive progress bars and cancel buttons.
2. **Windows Recycle Bin Integration:** Use Java 9+ `Desktop.moveToTrash(File)` to safely send deleted files to the Recycle Bin.
3. **Multi-Selection Operations:** Enable `ListSelectionModel.MULTIPLE_INTERVAL_SELECTION` to allow batch copy, cut, and deletion.
4. **Dual-Pane Mode:** Add side-by-side directory panels for faster file organization.
5. **File Bookmarking & Tagging:** Allow users to tag or pin favorite directories beyond standard quick access.
