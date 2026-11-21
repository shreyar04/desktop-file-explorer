package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * FileExplorerFrame: Main Graphical User Interface for Desktop File Explorer.
 *
 * Demonstrates Syllabus Concepts:
 * - Inheritance (extends JFrame)
 * - Interfaces (ActionListener)
 * - Event Handling and Adapter Classes (MouseAdapter, KeyAdapter)
 * - Layout Managers (BorderLayout, FlowLayout, BoxLayout)
 * - Swing Components (JFrame, JTable, JSplitPane, JToolBar, JPopupMenu, etc.)
 * - Inner Classes and Anonymous Classes
 * - Exception Handling with User Dialogs
 */
public class FileExplorerFrame extends JFrame implements ActionListener {

    // Core Business Logic Manager and Table Model
    private final FileManager fileManager;
    private final FileTableModel tableModel;

    // GUI Navigation Components
    private JButton btnBack;
    private JButton btnForward;
    private JButton btnUp;
    private JButton btnRefresh;
    private JTextField txtAddressBar;
    private JButton btnGo;

    // Search Components
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;

    // Toolbar Buttons
    private JButton btnNewFolder;
    private JButton btnRename;
    private JButton btnCopy;
    private JButton btnCut;
    private JButton btnPaste;
    private JButton btnDelete;
    private JButton btnProperties;
    private JComboBox<String> comboSort;

    // Main Content Components
    private JTable fileTable;
    private JList<SidebarItem> listQuickAccess;
    private JList<SidebarItem> listDrives;

    // Context Menu
    private JPopupMenu contextMenu;
    private JMenuItem menuOpen;
    private JMenuItem menuNewFolder;
    private JMenuItem menuRename;
    private JMenuItem menuCopy;
    private JMenuItem menuCut;
    private JMenuItem menuPaste;
    private JMenuItem menuDelete;
    private JMenuItem menuProperties;

    // Status Bar Labels
    private JLabel lblStatusCount;
    private JLabel lblStatusMessage;

    /**
     * Constructor initializes the frame, managers, layouts, and components.
     */
    public FileExplorerFrame() {
        super("Desktop File Explorer");
        this.fileManager = new FileManager();
        this.tableModel = new FileTableModel();

        initializeUI();
        loadDirectory(fileManager.getCurrentDirectory());
    }

    /**
     * Sets up the complete GUI structure, layouts, and event bindings.
     */
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null); // Center window on screen
        setLayout(new BorderLayout());

        // 1. Create Top Container (Navigation Bar + Toolbar)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        JPanel navBar = createNavigationBar();
        JToolBar toolBar = createToolBar();

        topContainer.add(navBar);
        topContainer.add(toolBar);
        add(topContainer, BorderLayout.NORTH);

        // 2. Create Center Split Pane (Sidebar + Main File Table)
        JPanel sidebarPanel = createSidebarPanel();
        JPanel tablePanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, tablePanel);
        splitPane.setDividerLocation(220);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(6);
        add(splitPane, BorderLayout.CENTER);

        // 3. Create Bottom Status Bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        // 4. Create Right-Click Context Menu
        createContextMenu();
    }

    /**
     * Constructs the Top Navigation Bar containing Back, Forward, Up, Refresh,
     * Address Bar, Go button, and Search Bar.
     */
    private JPanel createNavigationBar() {
        JPanel navPanel = new JPanel(new BorderLayout(5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(6, 8, 4, 8));

        // Left Navigation Controls Panel (FlowLayout)
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));

        btnBack = new JButton("\u2190"); // Left arrow
        btnBack.setToolTipText("Back (Alt + Left)");
        btnBack.setPreferredSize(new Dimension(38, 28));
        btnBack.setEnabled(false);
        btnBack.addActionListener(this);

        btnForward = new JButton("\u2192"); // Right arrow
        btnForward.setToolTipText("Forward (Alt + Right)");
        btnForward.setPreferredSize(new Dimension(38, 28));
        btnForward.setEnabled(false);
        btnForward.addActionListener(this);

        btnUp = new JButton("\u2191 Up"); // Up arrow
        btnUp.setToolTipText("Up to parent folder");
        btnUp.setPreferredSize(new Dimension(60, 28));
        btnUp.addActionListener(this);

        btnRefresh = new JButton("\u21BB"); // Refresh symbol
        btnRefresh.setToolTipText("Refresh (F5)");
        btnRefresh.setPreferredSize(new Dimension(38, 28));
        btnRefresh.addActionListener(this);

        leftControls.add(btnBack);
        leftControls.add(btnForward);
        leftControls.add(btnUp);
        leftControls.add(btnRefresh);

        // Center Address Bar Panel
        JPanel addressPanel = new JPanel(new BorderLayout(4, 0));
        JLabel lblAddress = new JLabel(" Location: ");
        lblAddress.setFont(lblAddress.getFont().deriveFont(Font.BOLD));

        txtAddressBar = new JTextField();
        txtAddressBar.setPreferredSize(new Dimension(300, 28));
        txtAddressBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    navigateToAddress(txtAddressBar.getText());
                }
            }
        });

        btnGo = new JButton("Go");
        btnGo.setPreferredSize(new Dimension(50, 28));
        btnGo.addActionListener(this);

        addressPanel.add(lblAddress, BorderLayout.WEST);
        addressPanel.add(txtAddressBar, BorderLayout.CENTER);
        addressPanel.add(btnGo, BorderLayout.EAST);

        // Right Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        txtSearch = new JTextField(12);
        txtSearch.setPreferredSize(new Dimension(130, 28));
        txtSearch.setToolTipText("Search files in current folder");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    executeSearch();
                }
            }
        });

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(72, 28));
        btnSearch.addActionListener(this);

        btnClearSearch = new JButton("\u2715"); // Cross symbol
        btnClearSearch.setToolTipText("Clear Search");
        btnClearSearch.setPreferredSize(new Dimension(36, 28));
        btnClearSearch.addActionListener(this);

        searchPanel.add(new JLabel("Find:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClearSearch);

        navPanel.add(leftControls, BorderLayout.WEST);
        navPanel.add(addressPanel, BorderLayout.CENTER);
        navPanel.add(searchPanel, BorderLayout.EAST);

        return navPanel;
    }

    /**
     * Constructs the Toolbar containing file operations:
     * New Folder, Rename, Copy, Cut, Paste, Delete, Properties, and Sort.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));

        btnNewFolder = new JButton("New Folder");
        btnNewFolder.setToolTipText("Create a new folder (Ctrl+Shift+N)");
        btnNewFolder.addActionListener(this);

        btnRename = new JButton("Rename");
        btnRename.setToolTipText("Rename selected item");
        btnRename.addActionListener(this);

        btnCopy = new JButton("Copy");
        btnCopy.setToolTipText("Copy selected item to clipboard");
        btnCopy.addActionListener(this);

        btnCut = new JButton("Move / Cut");
        btnCut.setToolTipText("Cut selected item to move");
        btnCut.addActionListener(this);

        btnPaste = new JButton("Paste");
        btnPaste.setToolTipText("Paste item from clipboard");
        btnPaste.setEnabled(false);
        btnPaste.addActionListener(this);

        btnDelete = new JButton("Delete");
        btnDelete.setToolTipText("Delete selected item");
        btnDelete.addActionListener(this);

        btnProperties = new JButton("Properties");
        btnProperties.setToolTipText("View properties of selected item");
        btnProperties.addActionListener(this);

        // Sort Selector
        JLabel lblSort = new JLabel("  Sort by: ");
        lblSort.setFont(lblSort.getFont().deriveFont(Font.PLAIN, 12f));

        String[] sortOptions = {"Name (A-Z)", "Name (Z-A)", "Type", "Size", "Date Modified"};
        comboSort = new JComboBox<>(sortOptions);
        comboSort.setPreferredSize(new Dimension(130, 26));
        comboSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySortingFromCombo();
            }
        });

        toolBar.add(btnNewFolder);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnRename);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnCopy);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnCut);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnPaste);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnDelete);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnProperties);

        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(lblSort);
        toolBar.add(comboSort);

        return toolBar;
    }

    /**
     * Helper model class for the sidebar items (Quick Access and Drives).
     */
    private static class SidebarItem {
        final String displayName;
        final File targetFolder;
        final Icon icon;

        SidebarItem(String displayName, File targetFolder, Icon icon) {
            this.displayName = displayName;
            this.targetFolder = targetFolder;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Constructs the Left Sidebar containing Quick Access folders and Available Drives.
     */
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Custom Cell Renderer for Sidebar Items showing native icons
        DefaultListCellRenderer sidebarRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SidebarItem) {
                    SidebarItem item = (SidebarItem) value;
                    label.setText(item.displayName);
                    if (item.icon != null) {
                        label.setIcon(item.icon);
                    }
                    label.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
                }
                return label;
            }
        };

        // Section 1: Quick Access
        JLabel lblQuickAccess = new JLabel(" Quick Access");
        lblQuickAccess.setFont(lblQuickAccess.getFont().deriveFont(Font.BOLD, 12f));
        lblQuickAccess.setAlignmentX(Component.LEFT_ALIGNMENT);

        DefaultListModel<SidebarItem> quickModel = new DefaultListModel<>();
        Map<String, File> quickLocations = fileManager.getQuickAccessLocations();
        for (Map.Entry<String, File> entry : quickLocations.entrySet()) {
            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(entry.getValue());
            quickModel.addElement(new SidebarItem(entry.getKey(), entry.getValue(), icon));
        }

        listQuickAccess = new JList<>(quickModel);
        listQuickAccess.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listQuickAccess.setCellRenderer(sidebarRenderer);
        listQuickAccess.setAlignmentX(Component.LEFT_ALIGNMENT);

        listQuickAccess.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && listQuickAccess.getSelectedValue() != null) {
                    listDrives.clearSelection();
                    SidebarItem selected = listQuickAccess.getSelectedValue();
                    loadDirectory(selected.targetFolder);
                }
            }
        });

        // Section 2: This PC / Drives
        JLabel lblDrives = new JLabel(" This PC (Drives)");
        lblDrives.setFont(lblDrives.getFont().deriveFont(Font.BOLD, 12f));
        lblDrives.setAlignmentX(Component.LEFT_ALIGNMENT);

        DefaultListModel<SidebarItem> drivesModel = new DefaultListModel<>();
        File[] drives = fileManager.getSystemDrives();
        for (File drive : drives) {
            String name = drive.getAbsolutePath();
            try {
                String desc = FileSystemView.getFileSystemView().getSystemDisplayName(drive);
                if (desc != null && !desc.isEmpty()) {
                    name = desc;
                }
            } catch (Exception ignored) {}
            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(drive);
            drivesModel.addElement(new SidebarItem(name, drive, icon));
        }

        listDrives = new JList<>(drivesModel);
        listDrives.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDrives.setCellRenderer(sidebarRenderer);
        listDrives.setAlignmentX(Component.LEFT_ALIGNMENT);

        listDrives.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && listDrives.getSelectedValue() != null) {
                    listQuickAccess.clearSelection();
                    SidebarItem selected = listDrives.getSelectedValue();
                    loadDirectory(selected.targetFolder);
                }
            }
        });

        // Assemble sidebar components
        sidebar.add(lblQuickAccess);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(new JScrollPane(listQuickAccess));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(lblDrives);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(new JScrollPane(listDrives));

        return sidebar;
    }

    /**
     * Constructs the Center File Table Panel using JTable backed by FileTableModel.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        fileTable = new JTable(tableModel);
        fileTable.setRowHeight(26);
        fileTable.setShowGrid(false);
        fileTable.setIntercellSpacing(new Dimension(0, 2));
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Adjust column widths
        TableColumn colName = fileTable.getColumnModel().getColumn(0);
        colName.setPreferredWidth(340);

        TableColumn colType = fileTable.getColumnModel().getColumn(1);
        colType.setPreferredWidth(140);

        TableColumn colSize = fileTable.getColumnModel().getColumn(2);
        colSize.setPreferredWidth(90);

        TableColumn colDate = fileTable.getColumnModel().getColumn(3);
        colDate.setPreferredWidth(150);

        // Custom Cell Renderer for Column 0 (Name): shows system file/folder icon
        colName.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus,
                                                          int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof File) {
                    File file = (File) value;
                    label.setText(file.getName().isEmpty() ? file.getAbsolutePath() : file.getName());
                    try {
                        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                        label.setIcon(icon);
                    } catch (Exception e) {
                        label.setIcon(null);
                    }
                    label.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                }
                return label;
            }
        });

        // Align Size column to the right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        colSize.setCellRenderer(rightRenderer);

        // Event Handling: Double click to open folder or launch file
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int selectedRow = fileTable.getSelectedRow();
                    if (selectedRow != -1) {
                        File clickedFile = tableModel.getFileAt(selectedRow);
                        handleOpen(clickedFile);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleContextMenuTrigger(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleContextMenuTrigger(e);
            }
        });

        // Selection Listener to update status bar details
        fileTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateStatusForSelection();
                }
            }
        });

        // Table Header Click Listener for Sorting
        JTableHeader header = fileTable.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = header.columnAtPoint(e.getPoint());
                if (col != -1) {
                    boolean newAscending = true;
                    if (tableModel.getCurrentSortColumn() == col) {
                        newAscending = !tableModel.isAscending();
                    }
                    tableModel.sort(col, newAscending);
                    lblStatusMessage.setText("Sorted by " + tableModel.getColumnName(col) +
                                            (newAscending ? " (Ascending)" : " (Descending)"));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(fileTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Constructs the Bottom Status Bar showing item counts and operation messages.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout(10, 0));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        lblStatusCount = new JLabel("Loading items...");
        lblStatusCount.setFont(lblStatusCount.getFont().deriveFont(Font.PLAIN, 12f));

        lblStatusMessage = new JLabel("Ready");
        lblStatusMessage.setFont(lblStatusMessage.getFont().deriveFont(Font.PLAIN, 12f));
        lblStatusMessage.setForeground(new Color(0, 102, 204));

        statusBar.add(lblStatusCount, BorderLayout.WEST);
        statusBar.add(lblStatusMessage, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Constructs the Right-Click Context Menu for the file table.
     */
    private void createContextMenu() {
        contextMenu = new JPopupMenu();

        menuOpen = new JMenuItem("Open");
        menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selected = getSelectedFile();
                if (selected != null) {
                    handleOpen(selected);
                }
            }
        });

        menuNewFolder = new JMenuItem("New Folder");
        menuNewFolder.addActionListener(this);

        menuRename = new JMenuItem("Rename");
        menuRename.addActionListener(this);

        menuCopy = new JMenuItem("Copy");
        menuCopy.addActionListener(this);

        menuCut = new JMenuItem("Cut / Move");
        menuCut.addActionListener(this);

        menuPaste = new JMenuItem("Paste");
        menuPaste.addActionListener(this);

        menuDelete = new JMenuItem("Delete");
        menuDelete.addActionListener(this);

        menuProperties = new JMenuItem("Properties");
        menuProperties.addActionListener(this);

        contextMenu.add(menuOpen);
        contextMenu.add(new JSeparator());
        contextMenu.add(menuNewFolder);
        contextMenu.add(menuRename);
        contextMenu.add(new JSeparator());
        contextMenu.add(menuCopy);
        contextMenu.add(menuCut);
        contextMenu.add(menuPaste);
        contextMenu.add(new JSeparator());
        contextMenu.add(menuDelete);
        contextMenu.add(new JSeparator());
        contextMenu.add(menuProperties);
    }

    /**
     * Triggers context menu on right click and selects clicked row if unselected.
     */
    private void handleContextMenuTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int row = fileTable.rowAtPoint(e.getPoint());
            if (row != -1 && !fileTable.isRowSelected(row)) {
                fileTable.setRowSelectionInterval(row, row);
            }

            boolean hasSelection = fileTable.getSelectedRow() != -1;
            menuOpen.setEnabled(hasSelection);
            menuRename.setEnabled(hasSelection);
            menuCopy.setEnabled(hasSelection);
            menuCut.setEnabled(hasSelection);
            menuDelete.setEnabled(hasSelection);
            menuProperties.setEnabled(hasSelection);
            menuPaste.setEnabled(fileManager.canPaste());

            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Loads and displays the files for a specified directory.
     *
     * @param directory Target directory
     */
    public void loadDirectory(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(this,
                "Cannot access directory: " + (directory != null ? directory.getAbsolutePath() : "null"),
                "Access Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update address bar
        txtAddressBar.setText(directory.getAbsolutePath());

        // Fetch files from FileManager
        List<File> files = fileManager.getFiles();
        tableModel.setFiles(files);

        // Update navigation button states
        btnBack.setEnabled(fileManager.canGoBack());
        btnForward.setEnabled(fileManager.canGoForward());
        btnUp.setEnabled(directory.getParentFile() != null);
        btnPaste.setEnabled(fileManager.canPaste());

        // Update status counts
        int[] counts = FileUtils.countItems(directory);
        lblStatusCount.setText(String.format("Total: %d items  (%d folders, %d files)",
            counts[0] + counts[1], counts[0], counts[1]));
        lblStatusMessage.setText("Ready");
    }

    /**
     * Navigates to a path typed into the address bar.
     */
    private void navigateToAddress(String path) {
        if (path == null || path.trim().isEmpty()) {
            return;
        }
        File target = new File(path.trim());
        if (target.exists() && target.isDirectory()) {
            fileManager.navigateTo(target);
            loadDirectory(target);
        } else {
            JOptionPane.showMessageDialog(this,
                "The specified path does not exist or is not a directory:\n" + path,
                "Invalid Path", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Retrieves the currently selected File from the JTable.
     *
     * @return Selected File or null if no selection
     */
    private File getSelectedFile() {
        int selectedRow = fileTable.getSelectedRow();
        if (selectedRow != -1) {
            return tableModel.getFileAt(selectedRow);
        }
        return null;
    }

    /**
     * Handles opening a file or navigating into a directory.
     */
    private void handleOpen(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            if (file.isDirectory()) {
                fileManager.navigateTo(file);
                loadDirectory(file);
            } else {
                lblStatusMessage.setText("Opening file: " + file.getName());
                fileManager.open(file);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening item: " + ex.getMessage(),
                "Open Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates status bar when table row selection changes.
     */
    private void updateStatusForSelection() {
        File selected = getSelectedFile();
        if (selected != null) {
            String sizeStr = selected.isDirectory() ? "Folder" : FileUtils.formatSize(selected.length());
            lblStatusMessage.setText("Selected: " + selected.getName() + " (" + sizeStr + ")");
        } else {
            lblStatusMessage.setText("Ready");
        }
    }

    /**
     * Applies sorting based on the toolbar combo box selection.
     */
    private void applySortingFromCombo() {
        int index = comboSort.getSelectedIndex();
        switch (index) {
            case 0: // Name (A-Z)
                tableModel.sort(0, true);
                break;
            case 1: // Name (Z-A)
                tableModel.sort(0, false);
                break;
            case 2: // Type
                tableModel.sort(1, true);
                break;
            case 3: // Size
                tableModel.sort(2, true);
                break;
            case 4: // Date Modified
                tableModel.sort(3, false);
                break;
        }
    }

    /**
     * Executes file search based on query entered in search box.
     */
    private void executeSearch() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            loadDirectory(fileManager.getCurrentDirectory());
            return;
        }

        lblStatusMessage.setText("Searching for '" + query + "'...");
        List<File> results = fileManager.search(query);
        tableModel.setFiles(results);
        lblStatusCount.setText(String.format("Search Results: %d item(s) found", results.size()));
    }

    /**
     * Clears current search and restores the folder view.
     */
    private void clearSearch() {
        txtSearch.setText("");
        loadDirectory(fileManager.getCurrentDirectory());
    }

    // ==========================================
    // Core Feature Implementations (Mandatory 10)
    // ==========================================

    /**
     * Feature 3: Create Folder
     */
    private void handleNewFolder() {
        String folderName = JOptionPane.showInputDialog(this,
            "Enter name for the new folder:",
            "Create New Folder",
            JOptionPane.PLAIN_MESSAGE);

        if (folderName == null || folderName.trim().isEmpty()) {
            return; // User cancelled
        }

        try {
            File created = fileManager.createFolder(folderName);
            loadDirectory(fileManager.getCurrentDirectory());
            lblStatusMessage.setText("Created folder: " + created.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error creating folder: " + ex.getMessage(),
                "New Folder Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Feature 4: Rename File or Folder
     */
    private void handleRename() {
        File selected = getSelectedFile();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a file or folder to rename.",
                "Rename", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String currentName = selected.getName();
        String newName = JOptionPane.showInputDialog(this,
            "Enter new name:",
            currentName);

        if (newName == null || newName.trim().isEmpty() || newName.trim().equals(currentName)) {
            return; // Cancelled or unchanged
        }

        try {
            File renamed = fileManager.rename(selected, newName);
            loadDirectory(fileManager.getCurrentDirectory());
            lblStatusMessage.setText("Renamed to: " + renamed.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error renaming: " + ex.getMessage(),
                "Rename Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Feature 5: Copy File or Folder
     */
    private void handleCopy() {
        File selected = getSelectedFile();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to copy.",
                "Copy", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        fileManager.copy(selected);
        btnPaste.setEnabled(true);
        lblStatusMessage.setText("Copied '" + selected.getName() + "' to clipboard.");
    }

    /**
     * Feature 6: Move / Cut File or Folder
     */
    private void handleCut() {
        File selected = getSelectedFile();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to move.",
                "Move / Cut", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        fileManager.cut(selected);
        btnPaste.setEnabled(true);
        lblStatusMessage.setText("Cut '" + selected.getName() + "' to clipboard (ready to move).");
    }

    /**
     * Paste helper for Copy and Move operations
     */
    private void handlePaste() {
        if (!fileManager.canPaste()) {
            return;
        }

        try {
            File pasted = fileManager.paste(fileManager.getCurrentDirectory());
            loadDirectory(fileManager.getCurrentDirectory());
            btnPaste.setEnabled(fileManager.canPaste());
            lblStatusMessage.setText("Pasted item: " + pasted.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error pasting item: " + ex.getMessage(),
                "Paste Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Feature 7: Delete File or Folder with confirmation
     */
    private void handleDelete() {
        File selected = getSelectedFile();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a file or folder to delete.",
                "Delete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String itemType = selected.isDirectory() ? "folder and all its contents" : "file";
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to permanently delete this " + itemType + "?\n\n" + selected.getName(),
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fileManager.delete(selected);
                loadDirectory(fileManager.getCurrentDirectory());
                lblStatusMessage.setText("Deleted: " + selected.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting item: " + ex.getMessage(),
                    "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Feature 10: File Properties
     */
    private void handleProperties() {
        File selected = getSelectedFile();
        if (selected == null) {
            // If no item selected, show properties of current directory
            selected = fileManager.getCurrentDirectory();
        }

        FileProperties.showDialog(this, selected);
    }

    // ==========================================
    // Event Dispatching (ActionListener Implementation)
    // ==========================================

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnBack) {
            if (fileManager.goBack()) {
                loadDirectory(fileManager.getCurrentDirectory());
            }
        } else if (source == btnForward) {
            if (fileManager.goForward()) {
                loadDirectory(fileManager.getCurrentDirectory());
            }
        } else if (source == btnUp) {
            if (fileManager.goUp()) {
                loadDirectory(fileManager.getCurrentDirectory());
            }
        } else if (source == btnRefresh) {
            loadDirectory(fileManager.getCurrentDirectory());
            lblStatusMessage.setText("Refreshed.");
        } else if (source == btnGo) {
            navigateToAddress(txtAddressBar.getText());
        } else if (source == btnSearch) {
            executeSearch();
        } else if (source == btnClearSearch) {
            clearSearch();
        } else if (source == btnNewFolder || source == menuNewFolder) {
            handleNewFolder();
        } else if (source == btnRename || source == menuRename) {
            handleRename();
        } else if (source == btnCopy || source == menuCopy) {
            handleCopy();
        } else if (source == btnCut || source == menuCut) {
            handleCut();
        } else if (source == btnPaste || source == menuPaste) {
            handlePaste();
        } else if (source == btnDelete || source == menuDelete) {
            handleDelete();
        } else if (source == btnProperties || source == menuProperties) {
            handleProperties();
        }
    }
}
