package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
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
import javax.imageio.ImageIO;
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
import javax.swing.JMenu;
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
import javax.swing.KeyStroke;
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
 * FileExplorerFrame: Modern, full-featured Windows-style File Explorer GUI.
 *
 * Demonstrates Syllabus Concepts:
 * - Inheritance (extends JFrame)
 * - Interfaces (implements ActionListener)
 * - Event Handling and Adapter Classes (MouseAdapter, KeyAdapter)
 * - Layout Managers (BorderLayout, FlowLayout, BoxLayout)
 * - Swing Components (JFrame, JTable, JSplitPane, JToolBar, JPopupMenu, etc.)
 * - Keyboard Shortcuts via KeyAdapter (Adapter Class pattern)
 * - Robust Exception Handling with User Dialogs
 * - Inner Classes (SidebarItem static inner class)
 * - Anonymous Classes (ActionListener, ListSelectionListener implementations)
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
    private JLabel lblCurrentFolderIcon;
    private JTextField txtAddressBar;
    private JButton btnGo;

    // Search Components
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;

    // Toolbar Buttons
    private JButton btnNewFolder;
    private JButton btnCut;
    private JButton btnCopy;
    private JButton btnPaste;
    private JButton btnRename;
    private JButton btnDelete;
    private JButton btnProperties;
    private JComboBox<String> comboSort;

    // Main Content Components
    private JTable fileTable;
    private JScrollPane tableScrollPane;
    private JList<SidebarItem> listQuickAccess;
    private JList<SidebarItem> listDrives;

    // Guard flag to prevent recursive listener loops in sidebar
    private boolean isUpdatingSidebarSelection = false;

    // Context Menus
    private JPopupMenu itemContextMenu;
    private JPopupMenu backgroundContextMenu;
    private JMenuItem itemMenuOpen;
    private JMenuItem itemMenuCut;
    private JMenuItem itemMenuCopy;
    private JMenuItem itemMenuPaste;
    private JMenuItem itemMenuRename;
    private JMenuItem itemMenuDelete;
    private JMenuItem itemMenuProperties;

    private JMenuItem bgMenuRefresh;
    private JMenuItem bgMenuNewFolder;
    private JMenuItem bgMenuPaste;
    private JMenuItem bgMenuProperties;

    // Status Bar Labels
    private JLabel lblStatusCount;
    private JLabel lblStatusClipboard;
    private JLabel lblStatusSelection;

    // Fonts & Colors
    private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    private static final Color COLOR_ACCENT_BLUE = new Color(0, 102, 204);
    private static final Color COLOR_BORDER = new Color(218, 222, 229);
    private static final Color COLOR_SIDEBAR_BG = new Color(248, 249, 250);

    /**
     * Constructor initializes managers, layout, components, and event bindings.
     * Also loads the application icon from resources or the filesystem.
     */
    public FileExplorerFrame() {
        super("Desktop File Explorer");
        this.fileManager = new FileManager();
        this.tableModel = new FileTableModel();

        initializeUI();
        setupKeyboardShortcuts();
        loadApplicationIcon();

        // Initial navigation
        navigateTo(fileManager.getCurrentDirectory());
    }

    /**
     * Loads the application icon for the window title bar and taskbar.
     * Looks for icon.png in the resources/ folder next to the project root.
     * Demonstrates: Java I/O (ImageIO.read), Exception Handling (try-catch),
     * and the java.io.File class.
     */
    private void loadApplicationIcon() {
        // Try a few common locations where the icon may be stored
        String[] iconPaths = {
            "resources/icon.png",
            "src/fileexplorer/icon.png",
            "icon.png"
        };

        for (String path : iconPaths) {
            File iconFile = new File(path);
            if (iconFile.exists()) {
                try {
                    // ImageIO.read() reads an image file and returns a BufferedImage
                    setIconImage(ImageIO.read(iconFile));
                    return; // Successfully loaded; stop looking
                } catch (IOException ex) {
                    // Could not read this file; try the next path
                }
            }
        }
        // If no icon found, the application uses the default Java Duke icon - that's fine
    }

    /**
     * Constructs the primary GUI layout and window settings.
     */
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1140, 720);
        setMinimumSize(new Dimension(880, 520));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Panel: Navigation Bar & Toolbar
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        JPanel navBar = createNavigationBar();
        JToolBar toolBar = createToolBar();

        topContainer.add(navBar);
        topContainer.add(toolBar);
        add(topContainer, BorderLayout.NORTH);

        // 2. Center Split Pane: Sidebar & File Table
        JPanel sidebarPanel = createSidebarPanel();
        JPanel tablePanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, tablePanel);
        splitPane.setDividerLocation(230);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        // 3. Bottom Status Bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        // 4. Context Menus
        createContextMenus();
    }

    /**
     * Constructs the Top Navigation Bar containing navigation buttons,
     * the Location Address Bar, and the Search Bar.
     */
    private JPanel createNavigationBar() {
        JPanel navPanel = new JPanel(new BorderLayout(6, 4));
        navPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 4, 10));

        // Left Navigation Controls Panel
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));

        btnBack = createIconButton("\u25C4", "Back (Alt + Left)");
        btnBack.setEnabled(false);
        btnBack.addActionListener(this);

        btnForward = createIconButton("\u25BA", "Forward (Alt + Right)");
        btnForward.setEnabled(false);
        btnForward.addActionListener(this);

        btnUp = createIconButton("\u25B2 Up", "Up to parent folder (Alt + Up)");
        btnUp.setPreferredSize(new Dimension(64, 28));
        btnUp.addActionListener(this);

        btnRefresh = createIconButton("\u27F3", "Refresh (F5)");
        btnRefresh.addActionListener(this);

        leftControls.add(btnBack);
        leftControls.add(btnForward);
        leftControls.add(btnUp);
        leftControls.add(btnRefresh);

        // Center Address Bar Panel
        JPanel addressPanel = new JPanel(new BorderLayout(4, 0));
        addressPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(2, 6, 2, 2)
        ));
        addressPanel.setBackground(Color.WHITE);

        lblCurrentFolderIcon = new JLabel();
        lblCurrentFolderIcon.setPreferredSize(new Dimension(20, 20));

        txtAddressBar = new JTextField();
        txtAddressBar.setFont(FONT_REGULAR);
        txtAddressBar.setBorder(null);
        txtAddressBar.setBackground(Color.WHITE);
        txtAddressBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    navigateToAddress(txtAddressBar.getText());
                }
            }
        });

        btnGo = new JButton("\u27A4"); // Arrow symbol
        btnGo.setToolTipText("Go to entered path (Enter)");
        btnGo.setPreferredSize(new Dimension(36, 24));
        btnGo.setFocusPainted(false);
        btnGo.setFont(FONT_REGULAR);
        btnGo.addActionListener(this);

        addressPanel.add(lblCurrentFolderIcon, BorderLayout.WEST);
        addressPanel.add(txtAddressBar, BorderLayout.CENTER);
        addressPanel.add(btnGo, BorderLayout.EAST);

        // Right Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        JLabel lblFind = new JLabel("Search: ");
        lblFind.setFont(FONT_REGULAR);

        txtSearch = new JTextField(12);
        txtSearch.setFont(FONT_REGULAR);
        txtSearch.setPreferredSize(new Dimension(140, 28));
        txtSearch.setToolTipText("Search files by name (Ctrl + F)");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    executeSearch();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    clearSearch();
                }
            }
        });

        btnSearch = new JButton("Find");
        btnSearch.setFont(FONT_REGULAR);
        btnSearch.setPreferredSize(new Dimension(58, 28));
        btnSearch.addActionListener(this);

        btnClearSearch = new JButton("\u2715");
        btnClearSearch.setToolTipText("Clear Search (Esc)");
        btnClearSearch.setPreferredSize(new Dimension(34, 28));
        btnClearSearch.setFont(FONT_REGULAR);
        btnClearSearch.addActionListener(this);

        searchPanel.add(lblFind);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClearSearch);

        navPanel.add(leftControls, BorderLayout.WEST);
        navPanel.add(addressPanel, BorderLayout.CENTER);
        navPanel.add(searchPanel, BorderLayout.EAST);

        return navPanel;
    }

    /**
     * Constructs the Toolbar containing all primary file operations:
     * New Folder, Cut, Copy, Paste, Rename, Delete, Properties, and Sort dropdown.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        btnNewFolder = createToolBarButton("\uD83D\uDCC1+ New Folder", "Create new folder (Ctrl+Shift+N)");
        btnCut = createToolBarButton("\u2702 Cut", "Cut selected item to move (Ctrl+X)");
        btnCopy = createToolBarButton("\uD83D\uDCCB Copy", "Copy selected item to clipboard (Ctrl+C)");
        btnPaste = createToolBarButton("\uD83D\uDCE5 Paste", "Paste item from clipboard (Ctrl+V)");
        btnPaste.setEnabled(false);
        btnRename = createToolBarButton("\u270F Rename", "Rename selected item (F2)");
        btnDelete = createToolBarButton("\uD83D\uDDD1 Delete", "Delete selected item (Delete)");
        btnProperties = createToolBarButton("\u2139 Properties", "View item properties (Alt+Enter)");

        toolBar.add(btnNewFolder);
        toolBar.add(Box.createHorizontalStrut(6));
        toolBar.add(btnCut);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnCopy);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnPaste);
        toolBar.add(Box.createHorizontalStrut(6));
        toolBar.add(btnRename);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnDelete);
        toolBar.add(Box.createHorizontalStrut(6));
        toolBar.add(btnProperties);

        // Right-aligned Sort dropdown
        toolBar.add(Box.createHorizontalGlue());
        JLabel lblSort = new JLabel("Sort by: ");
        lblSort.setFont(FONT_REGULAR);

        String[] sortOptions = {
            "Name (A-Z)", "Name (Z-A)", "Type", "Size", "Date Modified"
        };
        comboSort = new JComboBox<>(sortOptions);
        comboSort.setFont(FONT_REGULAR);
        comboSort.setPreferredSize(new Dimension(135, 26));
        comboSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySortingFromCombo();
            }
        });

        toolBar.add(lblSort);
        toolBar.add(comboSort);

        return toolBar;
    }

    /**
     * Helper to create consistently styled navigation buttons.
     */
    private JButton createIconButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(38, 28));
        btn.setFont(FONT_REGULAR);
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Helper to create consistently styled toolbar buttons.
     */
    private JButton createToolBarButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setToolTipText(tooltip);
        btn.setFont(FONT_REGULAR);
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(3, 8, 3, 8));
        btn.addActionListener(this);
        return btn;
    }

    /**
     * Internal model class representing a sidebar location item.
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
        sidebar.setBackground(COLOR_SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Custom Cell Renderer showing native icons with modern padding
        DefaultListCellRenderer sidebarRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SidebarItem) {
                    SidebarItem item = (SidebarItem) value;
                    label.setText("  " + item.displayName);
                    label.setIcon(item.icon);
                    label.setFont(FONT_REGULAR);
                    label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
                }
                return label;
            }
        };

        // Section 1: Quick Access
        JLabel lblQuickAccess = new JLabel("\u2B50 Quick Access");
        lblQuickAccess.setFont(FONT_HEADER);
        lblQuickAccess.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblQuickAccess.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));

        DefaultListModel<SidebarItem> quickModel = new DefaultListModel<>();
        Map<String, File> quickLocations = fileManager.getQuickAccessLocations();
        for (Map.Entry<String, File> entry : quickLocations.entrySet()) {
            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(entry.getValue());
            quickModel.addElement(new SidebarItem(entry.getKey(), entry.getValue(), icon));
        }

        listQuickAccess = new JList<>(quickModel);
        listQuickAccess.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listQuickAccess.setCellRenderer(sidebarRenderer);
        listQuickAccess.setBackground(COLOR_SIDEBAR_BG);
        listQuickAccess.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sidebar selection listener: Quick Access
        listQuickAccess.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !isUpdatingSidebarSelection) {
                    SidebarItem selected = listQuickAccess.getSelectedValue();
                    if (selected != null) {
                        isUpdatingSidebarSelection = true;
                        listDrives.clearSelection();
                        isUpdatingSidebarSelection = false;
                        navigateTo(selected.targetFolder);
                    }
                }
            }
        });

        // Mouse click listener allows clicking already-selected item to re-navigate/refresh
        listQuickAccess.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SidebarItem selected = listQuickAccess.getSelectedValue();
                if (selected != null) {
                    navigateTo(selected.targetFolder);
                }
            }
        });

        // Section 2: This PC / Drives
        JLabel lblDrives = new JLabel("\uD83D\uDCBB This PC (Drives)");
        lblDrives.setFont(FONT_HEADER);
        lblDrives.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDrives.setBorder(BorderFactory.createEmptyBorder(12, 4, 4, 4));

        DefaultListModel<SidebarItem> drivesModel = new DefaultListModel<>();
        File[] drives = fileManager.getSystemDrives();
        for (File drive : drives) {
            String name = drive.getAbsolutePath();
            try {
                String desc = FileSystemView.getFileSystemView().getSystemDisplayName(drive);
                if (desc != null && !desc.trim().isEmpty()) {
                    name = desc;
                }
            } catch (Exception ignored) {}
            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(drive);
            drivesModel.addElement(new SidebarItem(name, drive, icon));
        }

        listDrives = new JList<>(drivesModel);
        listDrives.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDrives.setCellRenderer(sidebarRenderer);
        listDrives.setBackground(COLOR_SIDEBAR_BG);
        listDrives.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sidebar selection listener: Drives (Fixes Issue 1: Drive Navigation Bug!)
        listDrives.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !isUpdatingSidebarSelection) {
                    SidebarItem selected = listDrives.getSelectedValue();
                    if (selected != null) {
                        isUpdatingSidebarSelection = true;
                        listQuickAccess.clearSelection();
                        isUpdatingSidebarSelection = false;
                        navigateTo(selected.targetFolder);
                    }
                }
            }
        });

        // Mouse click listener guarantees clicking drives always refreshes and navigates
        listDrives.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SidebarItem selected = listDrives.getSelectedValue();
                if (selected != null) {
                    navigateTo(selected.targetFolder);
                }
            }
        });

        JScrollPane scrollQuick = new JScrollPane(listQuickAccess);
        scrollQuick.setBorder(null);
        scrollQuick.setBackground(COLOR_SIDEBAR_BG);
        scrollQuick.getViewport().setBackground(COLOR_SIDEBAR_BG);

        JScrollPane scrollDrives = new JScrollPane(listDrives);
        scrollDrives.setBorder(null);
        scrollDrives.setBackground(COLOR_SIDEBAR_BG);
        scrollDrives.getViewport().setBackground(COLOR_SIDEBAR_BG);

        sidebar.add(lblQuickAccess);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(scrollQuick);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(lblDrives);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(scrollDrives);

        return sidebar;
    }

    /**
     * Constructs the Center File Table Panel using JTable backed by FileTableModel.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Custom JTable displaying an empty folder placeholder when 0 rows
        fileTable = new JTable(tableModel) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {
                    g.setColor(Color.GRAY);
                    g.setFont(FONT_REGULAR);
                    String emptyMsg = "This folder is empty.";
                    int strWidth = g.getFontMetrics().stringWidth(emptyMsg);
                    int x = Math.max(10, (getWidth() - strWidth) / 2);
                    int y = Math.max(40, getHeight() / 2);
                    g.drawString(emptyMsg, x, y);
                }
            }
        };

        fileTable.setFont(FONT_REGULAR);
        fileTable.setRowHeight(28);
        fileTable.setShowGrid(false);
        fileTable.setIntercellSpacing(new Dimension(0, 2));
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        fileTable.setFillsViewportHeight(true);
        fileTable.setBackground(Color.WHITE);

        // Adjust column widths
        TableColumn colName = fileTable.getColumnModel().getColumn(0);
        colName.setPreferredWidth(360);

        TableColumn colType = fileTable.getColumnModel().getColumn(1);
        colType.setPreferredWidth(140);

        TableColumn colSize = fileTable.getColumnModel().getColumn(2);
        colSize.setPreferredWidth(95);

        TableColumn colDate = fileTable.getColumnModel().getColumn(3);
        colDate.setPreferredWidth(155);

        // Custom Cell Renderer for Column 0 (Name): shows system file/folder icon
        colName.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus,
                                                          int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof File) {
                    File file = (File) value;
                    label.setText(" " + (file.getName().isEmpty() ? file.getAbsolutePath() : file.getName()));
                    try {
                        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                        label.setIcon(icon);
                    } catch (Exception e) {
                        label.setIcon(null);
                    }
                    label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                }
                return label;
            }
        });

        // Align Size column to the right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        colSize.setCellRenderer(rightRenderer);

        // Double-click to open file/folder
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

        // Table selection listener updates status bar details
        fileTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateSelectedStatus();
                }
            }
        });

        // Table Header Click Listener for Sorting
        JTableHeader header = fileTable.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 28));
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
                    String arrow = newAscending ? " \u25B2" : " \u25BC";
                    lblStatusSelection.setText("Sorted by " + tableModel.getColumnName(col) + arrow);
                }
            }
        });

        tableScrollPane = new JScrollPane(fileTable);
        tableScrollPane.setBorder(null);
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        // Right click listener on the scrollpane background (empty space)
        tableScrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleBackgroundContextMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleBackgroundContextMenu(e);
            }
        });

        panel.add(tableScrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Constructs the Bottom Status Bar showing item counts, clipboard state, and selection.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout(15, 0));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));

        lblStatusCount = new JLabel("Loading items...");
        lblStatusCount.setFont(FONT_REGULAR);

        lblStatusClipboard = new JLabel("");
        lblStatusClipboard.setFont(FONT_REGULAR);
        lblStatusClipboard.setForeground(new Color(150, 80, 0)); // Warm amber for clipboard notice

        lblStatusSelection = new JLabel("Ready");
        lblStatusSelection.setFont(FONT_REGULAR);
        lblStatusSelection.setForeground(COLOR_ACCENT_BLUE);

        JPanel rightStatusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightStatusPanel.setOpaque(false);
        rightStatusPanel.add(lblStatusClipboard);
        rightStatusPanel.add(lblStatusSelection);

        statusBar.add(lblStatusCount, BorderLayout.WEST);
        statusBar.add(rightStatusPanel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Constructs both Right-Click Context Menus:
     * 1. Item Context Menu (when clicking a file or folder row)
     * 2. Background Context Menu (when clicking empty space)
     */
    private void createContextMenus() {
        // --- 1. Item Context Menu ---
        itemContextMenu = new JPopupMenu();

        itemMenuOpen = new JMenuItem("Open");
        itemMenuOpen.setFont(FONT_BOLD);
        itemMenuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selected = getSelectedFile();
                if (selected != null) {
                    handleOpen(selected);
                }
            }
        });

        itemMenuCut = new JMenuItem("Cut / Move");
        itemMenuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itemMenuCut.addActionListener(this);

        itemMenuCopy = new JMenuItem("Copy");
        itemMenuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itemMenuCopy.addActionListener(this);

        itemMenuPaste = new JMenuItem("Paste");
        itemMenuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        itemMenuPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selected = getSelectedFile();
                // If a folder row was right-clicked, paste inside that folder!
                if (selected != null && selected.isDirectory()) {
                    handlePaste(selected);
                } else {
                    handlePaste(fileManager.getCurrentDirectory());
                }
            }
        });

        itemMenuRename = new JMenuItem("Rename");
        itemMenuRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        itemMenuRename.addActionListener(this);

        itemMenuDelete = new JMenuItem("Delete");
        itemMenuDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        itemMenuDelete.addActionListener(this);

        itemMenuProperties = new JMenuItem("Properties");
        itemMenuProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.ALT_MASK));
        itemMenuProperties.addActionListener(this);

        itemContextMenu.add(itemMenuOpen);
        itemContextMenu.add(new JSeparator());
        itemContextMenu.add(itemMenuCut);
        itemContextMenu.add(itemMenuCopy);
        itemContextMenu.add(itemMenuPaste);
        itemContextMenu.add(new JSeparator());
        itemContextMenu.add(itemMenuRename);
        itemContextMenu.add(itemMenuDelete);
        itemContextMenu.add(new JSeparator());
        itemContextMenu.add(itemMenuProperties);

        // --- 2. Background Context Menu ---
        backgroundContextMenu = new JPopupMenu();

        bgMenuRefresh = new JMenuItem("Refresh");
        bgMenuRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        bgMenuRefresh.addActionListener(this);

        bgMenuNewFolder = new JMenuItem("New Folder");
        bgMenuNewFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        bgMenuNewFolder.addActionListener(this);

        bgMenuPaste = new JMenuItem("Paste");
        bgMenuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        bgMenuPaste.addActionListener(this);

        // Sort By submenu
        JMenu menuSortBy = new JMenu("Sort by");
        String[] sorts = {"Name (A-Z)", "Name (Z-A)", "Type", "Size", "Date Modified"};
        for (int i = 0; i < sorts.length; i++) {
            final int sortIndex = i;
            JMenuItem sortItem = new JMenuItem(sorts[i]);
            sortItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    comboSort.setSelectedIndex(sortIndex);
                }
            });
            menuSortBy.add(sortItem);
        }

        bgMenuProperties = new JMenuItem("Properties");
        bgMenuProperties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show properties of current directory
                FileProperties.showDialog(FileExplorerFrame.this, fileManager.getCurrentDirectory());
            }
        });

        backgroundContextMenu.add(bgMenuRefresh);
        backgroundContextMenu.add(new JSeparator());
        backgroundContextMenu.add(bgMenuNewFolder);
        backgroundContextMenu.add(bgMenuPaste);
        backgroundContextMenu.add(new JSeparator());
        backgroundContextMenu.add(menuSortBy);
        backgroundContextMenu.add(new JSeparator());
        backgroundContextMenu.add(bgMenuProperties);
    }

    /**
     * Handles right-click popup trigger on the table.
     */
    private void handleContextMenuTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int row = fileTable.rowAtPoint(e.getPoint());
            if (row != -1) {
                if (!fileTable.isRowSelected(row)) {
                    fileTable.setRowSelectionInterval(row, row);
                }
                File selected = getSelectedFile();
                boolean isFolder = (selected != null && selected.isDirectory());

                itemMenuOpen.setEnabled(selected != null);
                itemMenuCut.setEnabled(selected != null);
                itemMenuCopy.setEnabled(selected != null);
                itemMenuRename.setEnabled(selected != null);
                itemMenuDelete.setEnabled(selected != null);
                itemMenuProperties.setEnabled(selected != null);

                // If folder selected, paste can paste into folder; otherwise paste into current dir
                itemMenuPaste.setEnabled(fileManager.canPaste());
                if (isFolder && fileManager.canPaste()) {
                    itemMenuPaste.setText("Paste into '" + selected.getName() + "'");
                } else {
                    itemMenuPaste.setText("Paste");
                }

                itemContextMenu.show(e.getComponent(), e.getX(), e.getY());
            } else {
                // Clicked on empty space inside table
                handleBackgroundContextMenu(e);
            }
        }
    }

    /**
     * Handles right-click popup trigger on empty space/background.
     */
    private void handleBackgroundContextMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            fileTable.clearSelection();
            bgMenuPaste.setEnabled(fileManager.canPaste());
            backgroundContextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Registers keyboard shortcuts using a simple KeyAdapter on the file table.
     * This demonstrates Adapter Classes from the college syllabus.
     *
     * Shortcuts:
     *   Enter       - Open selected file or folder
     *   F2          - Rename selected item
     *   F5          - Refresh current view
     *   Delete      - Delete selected item
     *   Ctrl + C    - Copy selected item
     *   Ctrl + X    - Cut / Move selected item
     *   Ctrl + V    - Paste item from clipboard
     *   Ctrl+Shift+N - Create new folder
     *   Ctrl + F    - Focus search box
     *   Alt + Enter - Show properties
     *   Alt + Left  - Go back in navigation history
     *   Alt + Right - Go forward in navigation history
     *   Alt + Up    - Go to parent directory
     */
    private void setupKeyboardShortcuts() {
        // KeyAdapter is a Swing Adapter Class (college syllabus: Section C - Adapter Classes)
        fileTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                boolean ctrl  = e.isControlDown();
                boolean alt   = e.isAltDown();
                boolean shift = e.isShiftDown();

                if (ctrl && shift && key == KeyEvent.VK_N) {
                    // Ctrl + Shift + N: New Folder
                    handleNewFolder();
                } else if (ctrl && key == KeyEvent.VK_C && !shift) {
                    // Ctrl + C: Copy
                    handleCopy();
                } else if (ctrl && key == KeyEvent.VK_X) {
                    // Ctrl + X: Cut
                    handleCut();
                } else if (ctrl && key == KeyEvent.VK_V) {
                    // Ctrl + V: Paste
                    handlePaste(fileManager.getCurrentDirectory());
                } else if (ctrl && key == KeyEvent.VK_F) {
                    // Ctrl + F: Focus search
                    txtSearch.requestFocusInWindow();
                    txtSearch.selectAll();
                } else if (alt && key == KeyEvent.VK_ENTER) {
                    // Alt + Enter: Properties
                    handleProperties();
                } else if (alt && key == KeyEvent.VK_LEFT) {
                    // Alt + Left: Go Back
                    if (fileManager.goBack()) {
                        refreshCurrentView();
                    }
                } else if (alt && key == KeyEvent.VK_RIGHT) {
                    // Alt + Right: Go Forward
                    if (fileManager.goForward()) {
                        refreshCurrentView();
                    }
                } else if (alt && key == KeyEvent.VK_UP) {
                    // Alt + Up: Go to parent directory
                    if (fileManager.goUp()) {
                        refreshCurrentView();
                    }
                } else if (key == KeyEvent.VK_ENTER && !ctrl && !alt) {
                    // Enter: Open selected item
                    File selected = getSelectedFile();
                    if (selected != null) {
                        handleOpen(selected);
                    }
                } else if (key == KeyEvent.VK_F2) {
                    // F2: Rename
                    handleRename();
                } else if (key == KeyEvent.VK_F5) {
                    // F5: Refresh
                    refreshCurrentView();
                } else if (key == KeyEvent.VK_DELETE && !ctrl && !alt) {
                    // Delete: Delete
                    handleDelete();
                }
            }
        });
    }

    /**
     * Primary Navigation Method:
     * Validates target directory, updates FileManager state, and refreshes the view.
     * Fixes Bug 1 (Drive and Folder Navigation).
     *
     * @param directory Target Directory to navigate into
     */
    public void navigateTo(File directory) {
        if (directory == null) {
            return;
        }

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

    /**
     * Refreshes the table, address bar, buttons, and status bar from the current directory.
     */
    public void refreshCurrentView() {
        File current = fileManager.getCurrentDirectory();
        if (current == null || !current.exists()) {
            return;
        }

        // Update Address Bar and Folder Icon
        txtAddressBar.setText(current.getAbsolutePath());
        try {
            lblCurrentFolderIcon.setIcon(FileSystemView.getFileSystemView().getSystemIcon(current));
        } catch (Exception ignored) {}

        // Load files into Table Model
        List<File> files = fileManager.getFiles();
        tableModel.setFiles(files);

        // Update Navigation Buttons
        btnBack.setEnabled(fileManager.canGoBack());
        btnForward.setEnabled(fileManager.canGoForward());
        btnUp.setEnabled(current.getParentFile() != null);
        btnPaste.setEnabled(fileManager.canPaste());

        // Update Status Counts
        int[] counts = FileUtils.countItems(current);
        lblStatusCount.setText(String.format(" %d items  (%d folders, %d files)",
            counts[0] + counts[1], counts[0], counts[1]));

        updateClipboardStatus();
        updateSelectedStatus();
    }

    /**
     * Navigates to a path string entered into the address bar.
     */
    private void navigateToAddress(String path) {
        if (path == null || path.trim().isEmpty()) {
            return;
        }
        File target = new File(path.trim());
        if (target.exists() && target.isDirectory()) {
            navigateTo(target);
        } else {
            JOptionPane.showMessageDialog(this,
                "The specified path does not exist or is not a directory:\n" + path,
                "Invalid Path", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Returns the currently selected File from the table.
     */
    private File getSelectedFile() {
        int selectedRow = fileTable.getSelectedRow();
        if (selectedRow != -1) {
            return tableModel.getFileAt(selectedRow);
        }
        return null;
    }

    /**
     * Handles opening a file or folder.
     */
    private void handleOpen(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            if (file.isDirectory()) {
                navigateTo(file);
            } else {
                lblStatusSelection.setText("Opening file: " + file.getName());
                fileManager.open(file);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening item: " + ex.getMessage(),
                "Open Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates status bar when table selection changes.
     */
    private void updateSelectedStatus() {
        File selected = getSelectedFile();
        if (selected != null) {
            String sizeStr = selected.isDirectory() ? "Folder" : FileUtils.formatSize(selected.length());
            lblStatusSelection.setText("Selected: " + selected.getName() + " (" + sizeStr + ")");
        } else {
            lblStatusSelection.setText("Ready");
        }
    }

    /**
     * Updates the status bar clipboard indicator.
     */
    private void updateClipboardStatus() {
        if (fileManager.canPaste()) {
            lblStatusClipboard.setText("\uD83D\uDCCB Clipboard: " + fileManager.getClipboardDescription());
            btnPaste.setEnabled(true);
        } else {
            lblStatusClipboard.setText("");
            btnPaste.setEnabled(false);
        }
    }

    /**
     * Applies sorting from the toolbar combo box.
     */
    private void applySortingFromCombo() {
        int index = comboSort.getSelectedIndex();
        switch (index) {
            case 0: tableModel.sort(0, true); break;  // Name A-Z
            case 1: tableModel.sort(0, false); break; // Name Z-A
            case 2: tableModel.sort(1, true); break;  // Type
            case 3: tableModel.sort(2, true); break;  // Size
            case 4: tableModel.sort(3, false); break; // Date Modified
        }
    }

    /**
     * Executes file search.
     */
    private void executeSearch() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            refreshCurrentView();
            return;
        }

        lblStatusSelection.setText("Searching for '" + query + "'...");
        List<File> results = fileManager.search(query);
        tableModel.setFiles(results);
        lblStatusCount.setText(String.format(" Search Results: %d item(s) found", results.size()));
    }

    /**
     * Clears active search.
     */
    private void clearSearch() {
        txtSearch.setText("");
        refreshCurrentView();
    }

    // ==========================================
    // File Operations Handlers
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
            return;
        }

        try {
            File created = fileManager.createFolder(folderName);
            refreshCurrentView();
            lblStatusSelection.setText("Created folder: " + created.getName());
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
            return;
        }

        try {
            File renamed = fileManager.rename(selected, newName);
            refreshCurrentView();
            lblStatusSelection.setText("Renamed to: " + renamed.getName());
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
        updateClipboardStatus();
        lblStatusSelection.setText("Copied '" + selected.getName() + "' to clipboard.");
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
        updateClipboardStatus();
        lblStatusSelection.setText("Cut '" + selected.getName() + "' (ready to paste).");
    }

    /**
     * Paste into specified target directory (supports cross-folder and cross-drive).
     */
    private void handlePaste(File targetDir) {
        if (!fileManager.canPaste()) {
            JOptionPane.showMessageDialog(this,
                "Clipboard is empty or file no longer exists.",
                "Paste", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (targetDir == null || !targetDir.isDirectory()) {
            targetDir = fileManager.getCurrentDirectory();
        }

        try {
            File pasted = fileManager.paste(targetDir);
            refreshCurrentView();
            updateClipboardStatus();
            lblStatusSelection.setText("Pasted item: " + pasted.getName());
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
                refreshCurrentView();
                lblStatusSelection.setText("Deleted: " + selected.getName());
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
                refreshCurrentView();
            }
        } else if (source == btnForward) {
            if (fileManager.goForward()) {
                refreshCurrentView();
            }
        } else if (source == btnUp) {
            if (fileManager.goUp()) {
                refreshCurrentView();
            }
        } else if (source == btnRefresh || source == bgMenuRefresh) {
            refreshCurrentView();
            lblStatusSelection.setText("Refreshed.");
        } else if (source == btnGo) {
            navigateToAddress(txtAddressBar.getText());
        } else if (source == btnSearch) {
            executeSearch();
        } else if (source == btnClearSearch) {
            clearSearch();
        } else if (source == btnNewFolder || source == bgMenuNewFolder) {
            handleNewFolder();
        } else if (source == btnRename || source == itemMenuRename) {
            handleRename();
        } else if (source == btnCopy || source == itemMenuCopy) {
            handleCopy();
        } else if (source == btnCut || source == itemMenuCut) {
            handleCut();
        } else if (source == btnPaste || source == bgMenuPaste) {
            handlePaste(fileManager.getCurrentDirectory());
        } else if (source == btnDelete || source == itemMenuDelete) {
            handleDelete();
        } else if (source == btnProperties || source == itemMenuProperties) {
            handleProperties();
        }
    }
}
