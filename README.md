# Desktop File Explorer

A modern, simplified Windows File Explorer desktop application developed in Java Swing and AWT as a B.Tech CSE College Internal Project.

---

## 1. Project Introduction & Objectives

The **Desktop File Explorer** is a functional desktop application designed to emulate the essential features of Microsoft Windows File Explorer. Developed specifically for college laboratory evaluation and viva examination, it emphasizes clean architectural separation, standard Java development practices, robustness against file system errors, and thorough coverage of the university Java Programming syllabus.

### Primary Objectives:
- Provide an intuitive graphical interface for browsing storage drives, quick access locations, and folders.
- Enable core file system operations: Open, Create Folder, Rename, Copy, Move, Delete, Search, Sort, and View Properties.
- Naturally demonstrate fundamental and intermediate Java concepts (OOP, Collections, Exception Handling, Streams/IO, Swing GUI).
- Maintain beginner-friendly, well-commented source code without relying on third-party frameworks or external build tools.

---

## 2. Technologies Used

- **Programming Language:** Java (Standard Edition - JDK 22.0.1 compatible)
- **GUI Framework:** Java Swing (`javax.swing.*`) and Java AWT (`java.awt.*`)
- **System Integration:** Java File API (`java.io.File`), Java Streams (`FileInputStream`, `FileOutputStream`), and Windows Desktop API (`java.awt.Desktop`)
- **Data Structures:** Java Collections Framework (`List`, `ArrayList`, `Stack`, `Map`, `LinkedHashMap`, `Collections`, `Comparator`, `Iterator`)
- **No External Dependencies:** Developed entirely using standard Java SE libraries (zero Maven/Gradle bloat, runs directly via `javac` and `java`).

---

## 3. Implemented Features (Mandatory 10 Features)

| # | Feature | Implementation Details |
|---|---|---|
| 1 | **Browse Drives & Folders** | Detects all system drives (`File.listRoots()`) and user locations (Home, Desktop, Documents, Downloads). Dynamic directory traversal with back/forward navigation. |
| 2 | **Open Files & Directories** | Double-clicking a folder navigates inside; double-clicking a file launches it in Windows default application (`Desktop.getDesktop().open(file)`). |
| 3 | **Create Folder** | Interactive prompt to create new directories. Validates against reserved Windows characters (`\ / : * ? " < > |`) and duplicate names. |
| 4 | **Rename Files & Folders** | Renames files and directories safely using `File.renameTo()`, preventing accidental overwriting of existing items. |
| 5 | **Copy Files & Folders** | Staged copy to an internal clipboard. Supports recursive deep copy for folder trees using buffered `FileInputStream` and `FileOutputStream`. Duplicate copies automatically generate " - Copy" suffixes. |
| 6 | **Move / Cut Files & Folders** | Cut-and-paste file migration. Performs direct atomic rename when on the same partition or deep recursive copy-and-delete across partitions. Prevents moving directories into themselves. |
| 7 | **Delete with Confirmation** | Removes files and non-empty directory trees recursively. Displays a confirmation dialog (`JOptionPane.showConfirmDialog`) to avoid accidental deletion. |
| 8 | **Search by Name** | Instant case-insensitive search in current directory or recursive subfolders using Collections traversal (`Iterator<File>`). |
| 9 | **Sort by Columns** | Sorts by Name (A-Z / Z-A), Type, Size, or Date Modified. Folders always remain cleanly grouped at the top. Accessible via column headers or toolbar combo box. |
| 10 | **File Properties** | Detailed modal dialog showing file name, type, absolute location, formatted size (Bytes / KB / MB), item count for folders, last modified date, and permissions (Read, Write, Hidden). |

---

## 4. Project Directory Structure

```
DesktopFileExplorer/
│
├── src/
│   └── fileexplorer/
│       ├── Main.java               # Application entry point & Swing thread initialization
│       ├── FileExplorerFrame.java  # Main Swing GUI window, layouts, menus, and event handlers
│       ├── FileManager.java        # Core business logic, navigation history & clipboard operations
│       ├── FileTableModel.java     # Custom AbstractTableModel managing JTable rows and sorting
│       ├── FileProperties.java     # Metadata extraction and modal Properties dialog
│       └── FileUtils.java          # Static utility methods: formatting, recursion, and validation
│
├── out/                            # Compiled Java bytecode (.class files)
├── README.md                       # Comprehensive project documentation
├── VIVA_PREPARATION.md             # 30 Curated viva questions and answers
└── .gitignore                      # Git exclusion rules
```

---

## 5. Explanation of Java Classes

### `Main.java`
- Serves as the application entry point containing `public static void main(String[] args)`.
- Sets the native Windows Look and Feel (`UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())`) to achieve a modern Windows Explorer aesthetic.
- Spawns the main window on the Swing **Event Dispatch Thread (EDT)** using `SwingUtilities.invokeLater()`, guaranteeing thread safety.

### `FileExplorerFrame.java`
- Inherits from `javax.swing.JFrame` and implements `ActionListener`.
- Assembles the GUI layout:
  - **Top Navigation Bar:** Back, Forward, Up, Refresh, Location Address Bar, and Search Bar.
  - **Toolbar:** New Folder, Rename, Copy, Move/Cut, Paste, Delete, Properties, and Sort dropdown.
  - **Center Split Pane:** Left sidebar (`JList` for Quick Access and Drives) and right main view (`JTable` with custom icon renderers).
  - **Bottom Status Bar:** Live item counters, clipboard status, and selected item details.
  - **Context Menus:** Right-click context menus for items (Open, Cut, Copy, Paste into folder, Rename, Delete, Properties) and empty table background (Refresh, New Folder, Paste, Sort by, Properties).
  - **Keyboard Shortcuts:** Global key bindings (`Ctrl+C`, `Ctrl+X`, `Ctrl+V`, `Ctrl+Shift+N`, `F2`, `F5`, `Delete`, `Alt+Enter`, `Alt+Left/Right/Up`, `Ctrl+F`).
- Coordinates event handling (`MouseAdapter`, `KeyAdapter`, `ActionListener`, `ActionMap`).

### `FileManager.java`
- Manages business state: current directory, history stacks (`backHistory`, `forwardHistory`), and clipboard (`clipboardFile`, `clipboardMode`).
- Implements navigation methods (`navigateTo`, `goBack`, `goForward`, `goUp`).
- Houses core file operations (`createFolder`, `rename`, `copy`, `cut`, `paste`, `delete`, `open`, `search`).
- Contains overloaded `search()` methods demonstrating Collection iteration and recursive folder exploration.

### `FileTableModel.java`
- Extends `javax.swing.table.AbstractTableModel`.
- Manages an internal `List<File>` and defines 4 columns: Name, Type, Size, Date Modified.
- Implements custom multi-criteria sorting using `Collections.sort()` and `Comparator<File>`, prioritizing folders before files.

### `FileProperties.java`
- Encapsulates file metadata attributes (name, path, size, formatted size, folder contents, date modified, read/write permissions).
- Renders a clean, modal `JDialog` using `GridBagLayout` and `BorderLayout` modeled after the Windows File Properties dialog.

### `FileUtils.java`
- Static utility class with private constructor.
- Provides overloaded methods `formatSize(long)` / `formatSize(File)` and `formatDate(long)` / `formatDate(File)`.
- Implements recursive folder copying with buffered Java I/O streams and recursive deletion for non-empty directories.
- Provides name validation against illegal Windows characters and handles collision-free file naming (`" - Copy"`).

---

## 6. Java Syllabus Concepts Demonstrated

| Syllabus Section | Concept | Where & How It Is Used |
|---|---|---|
| **Section A** | Classes, Objects & Constructors | Encapsulated models (`FileProperties`, `SidebarItem`, `FileManager`). |
| | Method Overloading | `FileUtils.formatSize()`, `FileUtils.formatDate()`, `FileManager.search()`. |
| | String Handling | `String.substring()`, `lastIndexOf()`, `toLowerCase()`, `String.format()`. |
| | Wrapper Classes | `Long.compare()` in `FileTableModel`, `Integer` parsing, `Boolean`. |
| | Inheritance | `FileExplorerFrame extends JFrame`, `FileTableModel extends AbstractTableModel`. |
| | Interfaces | `ActionListener`, `Comparator<File>`, `ListSelectionListener`. |
| | Inner & Anonymous Classes | Table cell renderers, mouse listeners, runnable threads. |
| **Section B** | Exception Handling | `try-catch-finally`, throwing `IOException`, handling `SecurityException`. |
| | Streams and I/O | `FileInputStream`, `FileOutputStream`, buffered file copying. |
| | Collections Framework | `List<File>`, `ArrayList`, `Stack<File>` (history), `LinkedHashMap` (sidebar). |
| | Iterators | Traversal using `Iterator<File>` in `FileManager.search()`. |
| | Utility Classes | `Collections.sort()`, `Arrays.asList()`, `SimpleDateFormat`. |
| **Section C** | Swing & AWT Components | `JFrame`, `JTable`, `JSplitPane`, `JToolBar`, `JPopupMenu`, `JDialog`. |
| | Layout Managers | `BorderLayout`, `FlowLayout`, `BoxLayout`, `GridBagLayout`. |
| | Event Handling | Action events, mouse click events, keyboard events (Enter / F5). |
| | Adapter Classes | `MouseAdapter` for double-clicks, `KeyAdapter` for address/search bars. |

---

## 7. How to Compile and Run

### Prerequisites
- JDK 8 or higher (Recommended: JDK 22.0.1 or JDK 17 LTS).
- Verify installation in terminal:
  ```powershell
  javac -version
  java -version
  ```

### Step-by-Step Compilation & Execution

1. Open PowerShell or Command Prompt.
2. Navigate to the project root directory:
   ```powershell
   cd E:\Java_project\DesktopFileExplorer
   ```
3. Compile all source files into the `out` directory:
   ```powershell
   javac -d out src/fileexplorer/*.java
   ```
4. Run the application:
   ```powershell
   java -cp out fileexplorer.Main
   ```

---

## 8. College Viva Demonstration Tips

When presenting to the examiner:
1. **Launch the Application:** Point out the native Windows theme and initial loading of the user home directory.
2. **Demonstrate Navigation:** Click between Drives and Quick Access in the left sidebar, double-click into folders, use Back (`←`) and Up (`↑`).
3. **Perform File Operations:**
   - Click **New Folder**, name it `CollegeDemo`.
   - Double-click into `CollegeDemo`.
   - Go back, select `CollegeDemo`, click **Rename** to `CollegeDemoRenamed`.
   - Select an existing file, click **Copy**, go into the new folder, click **Paste**.
   - Select the file, click **Properties** to show the dialog.
   - Click **Delete** to show the warning confirmation dialog and recursive deletion.
4. **Demonstrate Search & Sort:** Type a query into the Search box, view results, clear search, and sort table columns by clicking headers.
