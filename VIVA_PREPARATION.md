# College Viva Preparation Guide: Desktop File Explorer

This document contains 30 likely viva questions with clear, simple, high-scoring answers directly based on the actual source code of the **Desktop File Explorer** project.

---

### Category 1: Project Architecture & Overview

#### Q1. What is the objective and architecture of this project?
**Answer:**
The Desktop File Explorer is a simplified desktop clone of Windows File Explorer built in Java Swing. It follows a clean separation of concerns:
- **Presentation Layer (`FileExplorerFrame`, `FileProperties`):** Swing GUI components, menus, and user dialogs.
- **Model Layer (`FileTableModel`):** Bridges file collections with the `JTable`.
- **Controller/Business Logic Layer (`FileManager`):** Manages navigation state, history stacks, clipboard, and file operations.
- **Utility Layer (`FileUtils`):** Static helpers for formatting, recursion, and stream I/O.

#### Q2. Why did you choose Java Swing instead of JavaFX or web technologies?
**Answer:**
Java Swing and AWT are part of the core Java Foundation Classes (JFC) included directly in the Java Standard Edition (SE) JDK. They do not require any external dependencies, frameworks, or web browsers, making them ideal for demonstrating core Java concepts, GUI layout managers, and event handling aligned with our college syllabus.

#### Q3. How does the application look like native Windows?
**Answer:**
In `Main.java`, we set the system Look and Feel before creating the GUI:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```
This instructs Swing to adopt the native Windows theme, controls, and colors. We also retrieve native file system icons using `FileSystemView.getFileSystemView().getSystemIcon(file)`.

---

### Category 2: Classes, Objects & OOP Concepts

#### Q4. Where are Classes, Objects, and Constructors demonstrated in your project?
**Answer:**
- Every file in our `fileexplorer` package is a class.
- When the user launches the app, `new FileExplorerFrame()`, `new FileManager()`, and `new FileTableModel()` instantiate real objects on the heap.
- `FileProperties` demonstrates constructor encapsulation where passing a `File` object calculates its name, path, formatted size, and item counts, storing them in private fields accessible via getters.
- Constructor overloading is demonstrated in `FileTableModel` with both a default constructor `FileTableModel()` and a parameterized constructor `FileTableModel(List<File> files)`.

#### Q5. Where did you use Method Overloading and why?
**Answer:**
Method overloading occurs when methods in the same class share the same name with different parameter lists. In our project:
1. `FileUtils.formatSize(long bytes)` converts raw byte numbers, while `FileUtils.formatSize(File file)` checks if the file is a directory (`<DIR>`) before calling the byte formatter.
2. `FileUtils.formatDate(long millis)` and `FileUtils.formatDate(File file)`.
3. `FileManager.search(String query)` searches the current folder, while `FileManager.search(File startDir, String query, boolean recursive)` allows specifying a custom root directory and recursive depth.

#### Q6. What access specifiers did you use and why?
**Answer:**
- `private`: Used for internal state fields (e.g., `backHistory`, `fileList`, `currentDirectory`, UI buttons) to preserve encapsulation and prevent unauthorized external modification.
- `public`: Used for constructors and methods intended for external access (e.g., `navigateTo()`, `delete()`, `formatSize()`).
- Package-private / default: Used for helper classes like `SidebarItem` inside the package.

#### Q7. Why did you make `FileUtils` a utility class with a private constructor?
**Answer:**
`FileUtils` contains only `public static` methods that do not hold instance state. Making the constructor `private` prevents unnecessary instantiation via `new FileUtils()`, which is a standard Java best practice.

---

### Category 3: Inheritance & Interfaces

#### Q8. Where is Inheritance used in your source code?
**Answer:**
- `FileExplorerFrame extends JFrame`: Inherits window management, menu support, and title bar capabilities from Swing's `JFrame`.
- `FileTableModel extends AbstractTableModel`: Inherits default table model event notification mechanisms (`fireTableDataChanged()`) while overriding custom data retrieval methods like `getRowCount()`, `getColumnCount()`, and `getValueAt()`.

#### Q9. What Interfaces did you implement and how do they work?
**Answer:**
- `ActionListener`: Implemented by `FileExplorerFrame` to respond to button clicks and menu item selections via `actionPerformed(ActionEvent e)`.
- `Comparator<File>`: Used in `FileTableModel.sort()` to compare two `File` objects based on name, type, size, or date.
- `ListSelectionListener`: Used to listen for row selection changes in the table and sidebar to update the status bar.

#### Q10. What is the difference between an Abstract Class and an Interface in your project?
**Answer:**
- `AbstractTableModel` is an abstract class: It provides default code for event listener registration and column management, while requiring us to implement data-specific methods (`getRowCount`, `getValueAt`).
- `ActionListener` and `Comparator` are interfaces: They only define method contracts (`actionPerformed` and `compare`) with no stored state.

#### Q11. Where are Inner Classes and Anonymous Classes used?
**Answer:**
- **Static Nested Class:** `SidebarItem` inside `FileExplorerFrame` encapsulates display name, file reference, and icon for sidebar entries.
- **Anonymous Classes:**
  - `new Runnable() { public void run() { ... } }` in `Main.java` passed to `SwingUtilities.invokeLater`.
  - `new Comparator<File>() { ... }` in `FileTableModel.sort()`.
  - `new MouseAdapter() { ... }` on table rows for double-clicks and right-click context menu.

---

### Category 4: Exception Handling

#### Q12. How is Exception Handling implemented in your file operations?
**Answer:**
File operations are prone to run-time failures (e.g., file locked, disk full, invalid characters, permission denied).
- We use `try-catch-finally` blocks throughout.
- Methods like `createFolder`, `rename`, `paste`, and `delete` in `FileManager` validate inputs and throw descriptive `java.io.IOException` instances when an error occurs.
- The GUI catches these exceptions in `FileExplorerFrame` and displays user-friendly error dialogs using `JOptionPane.showMessageDialog(..., JOptionPane.ERROR_MESSAGE)`.

#### Q13. How did you handle restricted directories or Permission Denied errors?
**Answer:**
In Windows, system folders like `System Volume Information` return `null` when `File.listFiles()` is called. In `FileManager.getFiles()`, we explicitly check `if (list != null)` before adding items to our collection, preventing `NullPointerException`.

---

### Category 5: Java File Handling & Streams (I/O)

#### Q14. What is the difference between `File` and Java I/O Streams?
**Answer:**
- `java.io.File` represents metadata about paths, files, and directories (exists, isDirectory, length, lastModified, renameTo, delete). It does not read or write file byte contents.
- `FileInputStream` and `FileOutputStream` are byte streams used to read and write the actual binary data inside files.

#### Q15. How does your Copy feature work?
**Answer:**
In `FileUtils.copyRecursively()`:
1. If the source is a file, we open a `FileInputStream` on the source and a `FileOutputStream` on the destination, reading and writing bytes through an 8 KB buffer (`byte[] buffer = new byte[8192]`).
2. If the source is a directory, it creates the destination folder (`mkdirs()`), lists all child files and subdirectories (`listFiles()`), and recursively calls `copyRecursively()` on each child.
3. We also check for duplicate names and append `" - Copy"` using `FileUtils.getUniqueDestinationFile()`.

#### Q16. How does your Move (Cut/Paste) feature work?
**Answer:**
In `FileManager.paste()`:
1. If the clipboard mode is `CUT`, it first attempts an atomic rename using `file.renameTo(destination)`. This is instantaneous if source and destination are on the same disk drive.
2. If `renameTo()` fails (e.g., moving across different drive partitions), it executes a deep recursive copy to the target followed by a recursive deletion of the source.
3. It clears the clipboard after the cut operation finishes.

#### Q17. How does recursive Delete work and why is recursion necessary?
**Answer:**
The standard Java `File.delete()` method only succeeds if a directory is completely empty. If a folder contains files or subfolders, `delete()` returns `false`.
In `FileUtils.deleteRecursively()`:
We list all children, recursively delete each subfolder and file first (post-order traversal), and finally call `delete()` on the parent directory.

#### Q18. How do you open a file with its default Windows application?
**Answer:**
We check if desktop integration is supported via `Desktop.isDesktopSupported()`, retrieve the system desktop instance with `Desktop.getDesktop()`, and call:
```java
Desktop.getDesktop().open(file);
```
Windows automatically inspects the file extension and launches the associated program (e.g., Notepad for `.txt`, Acrobat for `.pdf`).

---

### Category 6: Collections Framework

#### Q19. Which Collection classes did you use and why?
**Answer:**
1. `List<File>` and `ArrayList<File>`: Dynamic arrays storing file items in `FileTableModel` and search results.
2. `Stack<File>`: Implements the Back and Forward browser-style navigation history (`backHistory.push()`, `backHistory.pop()`).
3. `LinkedHashMap<String, File>`: Stores quick access locations (Home, Desktop, Downloads) in predictable insertion order.

#### Q20. Where did you use an `Iterator` in your code?
**Answer:**
In `FileManager.search()`:
```java
Iterator<File> iterator = allItems.iterator();
while (iterator.hasNext()) {
    File item = iterator.next();
    if (item.getName().toLowerCase().contains(lowerQuery)) {
        matches.add(item);
    }
}
```
This explicitly demonstrates iterator traversal from the Collections framework (Syllabus Section B).

#### Q21. How does the sorting feature work using `Collections.sort()`?
**Answer:**
In `FileTableModel.sort()`, we pass our `fileList` and a custom `Comparator<File>` to `Collections.sort()`.
The comparator enforces two rules:
1. **Primary rule:** Directories are always placed before files.
2. **Secondary rule:** Compares based on the chosen column:
   - Name: `f1.getName().compareToIgnoreCase(f2.getName())`
   - Type: `FileUtils.getFileType(f1).compareToIgnoreCase(...)`
   - Size: `Long.compare(f1.length(), f2.length())`
   - Date: `Long.compare(f1.lastModified(), f2.lastModified())`

---

### Category 7: GUI, Swing vs AWT, and Layout Managers

#### Q22. What is the difference between AWT and Swing?
**Answer:**
- **AWT (Abstract Window Toolkit):** Heavyweight components that rely on underlying operating system native peers (e.g., `java.awt.Button`). Platform-dependent look and feel.
- **Swing:** Lightweight components written entirely in pure Java (e.g., `javax.swing.JButton`). Pluggable Look and Feel, richer feature set (tables, trees, tabbed panes), and double buffering.
- In our project, Swing is used for UI components (`JFrame`, `JTable`, `JButton`), while AWT provides foundational layouts, colors, fonts, and events (`BorderLayout`, `Color`, `ActionEvent`).

#### Q23. What Layout Managers did you use and how do they arrange components?
**Answer:**
- `BorderLayout`: Used for `FileExplorerFrame`. Divides space into North (navigation bar), Center (split pane), and South (status bar).
- `BoxLayout`: Arranges components in a single row or column (used in top navigation container and sidebar).
- `FlowLayout`: Arranges components left-to-right (used in navigation button groups).
- `GridBagLayout`: Used in `FileProperties` dialog for aligned label-value pairs with customizable constraints (`GridBagConstraints`).

#### Q24. What is `JSplitPane` and why was it used?
**Answer:**
`JSplitPane` divides a container into two resizable panes. We used a horizontal split (`JSplitPane.HORIZONTAL_SPLIT`) to place the navigation sidebar on the left and the main file table on the right, allowing the user to drag the divider to adjust panel widths.

#### Q25. What is the role of `JTable` and `AbstractTableModel`?
**Answer:**
`JTable` is Swing's component for displaying tabular data. Following the Model-View-Controller (MVC) pattern, `JTable` acts as the View, while our `FileTableModel` (which extends `AbstractTableModel`) acts as the Model. When data changes, calling `fireTableDataChanged()` tells the table to repaint its rows.

---

### Category 8: Event Handling & Adapter Classes

#### Q26. How does Java Event Handling work in your project?
**Answer:**
Java uses the **Delegation Event Model**:
1. **Event Source:** UI components where user interactions happen (e.g., `btnRefresh`, `btnDelete`).
2. **Event Object:** Encapsulates what happened (e.g., `ActionEvent`, `MouseEvent`).
3. **Event Listener:** Interface containing callback methods (e.g., `ActionListener.actionPerformed()`).
When the user clicks a button, the source generates an `ActionEvent` and delivers it to the registered listener.

#### Q27. What is an Adapter Class and where did you use one?
**Answer:**
Listener interfaces with multiple methods (like `MouseListener` which has 5 methods) require implementing all methods, even empty ones.
An **Adapter Class** (like `MouseAdapter` or `KeyAdapter`) provides default empty implementations. We extended `MouseAdapter` for table clicks to override only `mouseClicked()` for double-clicks, saving boilerplate code.

#### Q28. What is the Swing Event Dispatch Thread (EDT) and why does `SwingUtilities.invokeLater()` matter?
**Answer:**
Swing components are not thread-safe. All GUI creation, painting, and updates must occur on a single background thread called the **Event Dispatch Thread (EDT)**.
In `Main.java`, `SwingUtilities.invokeLater()` places our GUI construction code onto the EDT queue, preventing race conditions and UI deadlocks.

---

### Category 9: Edge Cases & Safety

#### Q29. How do you prevent a user from moving a folder into its own subfolder?
**Answer:**
In `FileManager.paste()`, before moving or copying a directory, we compare path prefixes:
```java
String sourcePath = clipboardFile.getAbsolutePath().toLowerCase();
String targetPath = targetDirectory.getAbsolutePath().toLowerCase();
if (targetPath.startsWith(sourcePath)) {
    throw new IOException("Cannot copy/move a directory into itself or its subfolder.");
}
```
This prevents infinite recursive loops that would crash the file system.

#### Q30. How do you prevent accidental deletion?
**Answer:**
Whenever the user triggers Delete (via toolbar, menu, or key), `FileExplorerFrame.handleDelete()` opens a modal warning dialog:
```java
int confirm = JOptionPane.showConfirmDialog(this,
    "Are you sure you want to permanently delete this...?",
    "Confirm Delete",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE);
```
The deletion only proceeds if the user explicitly clicks `YES`.
