/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileManager;
import fileexplorer.FileProperties;
import fileexplorer.FileTableModel;
import fileexplorer.FileUtils;
import fileexplorer.IconUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
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
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class FileExplorerFrame
extends JFrame
implements ActionListener {
    public static final Color COLOR_BG_WINDOW = new Color(243, 243, 245);
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(229, 229, 234);
    public static final Color COLOR_BORDER_INPUT = new Color(215, 215, 222);
    public static final Color COLOR_ACCENT_BLUE = new Color(0, 103, 192);
    public static final Color COLOR_SELECTION_BG = new Color(229, 241, 251);
    public static final Color COLOR_HOVER_BG = new Color(238, 239, 242);
    public static final Color COLOR_TEXT_PRIMARY = new Color(32, 32, 32);
    public static final Color COLOR_TEXT_MUTED = new Color(112, 112, 112);
    public static final Font FONT_REGULAR = new Font("Segoe UI", 0, 12);
    public static final Font FONT_BOLD = new Font("Segoe UI", 1, 12);
    public static final Font FONT_SEMIBOLD = new Font("Segoe UI", 1, 11);
    public static final Font FONT_SMALL = new Font("Segoe UI", 0, 11);
    public static final Font FONT_SECTION = new Font("Segoe UI", 1, 11);
    private final FileManager fileManager;
    private final FileTableModel tableModel;
    private final DefaultListModel<File> gridListModel;
    private JPanel centerCardContainer;
    private CardLayout cardLayout;
    private boolean isGridView = true;
    private JList<File> gridList;
    private JScrollPane gridScrollPane;
    private JTable fileTable;
    private JScrollPane tableScrollPane;
    private JPanel tabBarPanel;
    private final List<TabInfo> tabsList = new ArrayList<TabInfo>();
    private int activeTabIndex = 0;
    private JButton btnBack;
    private JButton btnForward;
    private JButton btnUp;
    private JButton btnRefresh;
    private JLabel lblAddressIcon;
    private JTextField txtAddressBar;
    private JButton btnGo;
    private JTextField txtSearch;
    private JButton btnClearSearch;
    private JButton btnNew;
    private JButton btnCut;
    private JButton btnCopy;
    private JButton btnPaste;
    private JButton btnRename;
    private JButton btnDelete;
    private JButton btnProperties;
    private JButton btnSort;
    private JButton btnViewToggle;
    private JList<SidebarItem> listSidebar;
    private DefaultListModel<SidebarItem> sidebarModel;
    private boolean isUpdatingSidebarSelection = false;
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
    private JLabel lblStatusCount;
    private JLabel lblStatusClipboard;
    private JLabel lblStatusSelection;

    public FileExplorerFrame() {
        super("Desktop File Explorer");
        this.fileManager = new FileManager();
        this.tableModel = new FileTableModel();
        this.gridListModel = new DefaultListModel();
        this.initializeUI();
        this.setupKeyboardShortcuts();
        this.loadApplicationIcon();
        File file = this.fileManager.getCurrentDirectory();
        this.addTab(file.getName().isEmpty() ? "Home" : file.getName(), file);
        this.navigateTo(file);
    }

    private void loadApplicationIcon() {
        String[] stringArray;
        for (String string : stringArray = new String[]{"resources/icon.png", "src/fileexplorer/icon.png", "icon.png"}) {
            File file = new File(string);
            if (!file.exists()) continue;
            try {
                this.setIconImage(ImageIO.read(file));
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void initializeUI() {
        this.setDefaultCloseOperation(3);
        this.setSize(1180, 740);
        this.setMinimumSize(new Dimension(920, 560));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLOR_BG_WINDOW);
        this.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        jPanel.setBackground(COLOR_BG_WINDOW);
        this.tabBarPanel = this.createTabBar();
        JPanel jPanel2 = this.createNavigationAddressBar();
        JToolBar jToolBar = this.createCommandToolBar();
        jPanel.add(this.tabBarPanel);
        jPanel.add(jPanel2);
        jPanel.add(jToolBar);
        this.add((Component)jPanel, "North");
        JPanel jPanel3 = this.createSidebarPanel();
        JPanel jPanel4 = this.createMainContentCard();
        JSplitPane jSplitPane = new JSplitPane(1, jPanel3, jPanel4);
        jSplitPane.setDividerLocation(230);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setDividerSize(4);
        jSplitPane.setBorder(null);
        jSplitPane.setBackground(COLOR_BG_WINDOW);
        this.add((Component)jSplitPane, "Center");
        JPanel jPanel5 = this.createStatusBar();
        this.add((Component)jPanel5, "South");
        this.createContextMenus();
    }

    private JPanel createTabBar() {
        JPanel jPanel = new JPanel(new FlowLayout(0, 4, 3));
        jPanel.setBackground(COLOR_BG_WINDOW);
        jPanel.setBorder(BorderFactory.createEmptyBorder(3, 10, 0, 10));
        JButton jButton = new JButton("+");
        jButton.setFont(new Font("Segoe UI", 0, 15));
        jButton.setPreferredSize(new Dimension(28, 28));
        jButton.setFocusPainted(false);
        jButton.setContentAreaFilled(false);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        jButton.setCursor(new Cursor(12));
        jButton.setToolTipText("Open new tab");
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File file = FileExplorerFrame.this.fileManager.getCurrentDirectory();
                FileExplorerFrame.this.addTab(file.getName().isEmpty() ? "Home" : file.getName(), file);
            }
        });
        jPanel.add(jButton);
        return jPanel;
    }

    private void addTab(String string, File file) {
        final TabInfo tabInfo = new TabInfo(string, file);
        this.tabsList.add(tabInfo);
        int n = this.tabsList.size() - 1;
        JPanel jPanel = new JPanel(this, new FlowLayout(0, 6, 4)){
            final /* synthetic */ FileExplorerFrame this$0;
            {
                this.this$0 = fileExplorerFrame;
                super(layoutManager);
            }

            @Override
            protected void paintComponent(Graphics graphics) {
                boolean bl;
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean bl2 = bl = this.this$0.activeTabIndex == this.this$0.tabsList.indexOf(tabInfo);
                if (bl) {
                    graphics2D.setColor(COLOR_CARD_BG);
                    graphics2D.fillRoundRect(0, 0, this.getWidth(), this.getHeight() + 6, 8, 8);
                    graphics2D.setColor(COLOR_BORDER);
                    graphics2D.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() + 6, 8, 8);
                } else {
                    graphics2D.setColor(new Color(235, 236, 239));
                    graphics2D.fillRoundRect(0, 1, this.getWidth(), this.getHeight() + 4, 6, 6);
                }
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        jPanel.setOpaque(false);
        jPanel.setCursor(new Cursor(12));
        jPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 6));
        JLabel jLabel = new JLabel(file.isDirectory() ? IconUtils.getFolderIcon(16) : IconUtils.getHomeIcon(16));
        JLabel jLabel2 = new JLabel(string);
        jLabel2.setFont(FONT_REGULAR);
        jLabel2.setForeground(COLOR_TEXT_PRIMARY);
        JButton jButton = new JButton(IconUtils.getCloseIcon(10));
        jButton.setPreferredSize(new Dimension(18, 18));
        jButton.setContentAreaFilled(false);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        jButton.setFocusPainted(false);
        jButton.setCursor(new Cursor(12));
        jButton.addActionListener(new ActionListener(){
            final /* synthetic */ FileExplorerFrame this$0;
            {
                this.this$0 = fileExplorerFrame;
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                this.this$0.closeTab(tabInfo);
            }
        });
        jPanel.add(jLabel);
        jPanel.add(jLabel2);
        jPanel.add(jButton);
        tabInfo.tabComponent = jPanel;
        tabInfo.lblTitle = jLabel2;
        tabInfo.lblIcon = jLabel;
        jPanel.addMouseListener(new MouseAdapter(this){
            final /* synthetic */ FileExplorerFrame this$0;
            {
                this.this$0 = fileExplorerFrame;
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int n = this.this$0.tabsList.indexOf(tabInfo);
                if (n != -1) {
                    this.this$0.switchToTab(n);
                }
            }
        });
        this.tabBarPanel.add((Component)jPanel, this.tabBarPanel.getComponentCount() - 1);
        this.tabBarPanel.revalidate();
        this.tabBarPanel.repaint();
        this.switchToTab(n);
    }

    private void switchToTab(int n) {
        if (n >= 0 && n < this.tabsList.size()) {
            this.activeTabIndex = n;
            TabInfo tabInfo = this.tabsList.get(n);
            this.navigateTo(tabInfo.directory);
            this.tabBarPanel.repaint();
        }
    }

    private void closeTab(TabInfo tabInfo) {
        if (this.tabsList.size() <= 1) {
            return;
        }
        int n = this.tabsList.indexOf(tabInfo);
        if (n != -1) {
            this.tabBarPanel.remove(tabInfo.tabComponent);
            this.tabsList.remove(n);
            if (this.activeTabIndex >= this.tabsList.size()) {
                this.activeTabIndex = this.tabsList.size() - 1;
            }
            this.tabBarPanel.revalidate();
            this.tabBarPanel.repaint();
            this.switchToTab(this.activeTabIndex);
        }
    }

    private void updateActiveTabInfo(File file) {
        if (this.activeTabIndex >= 0 && this.activeTabIndex < this.tabsList.size()) {
            String string;
            TabInfo tabInfo = this.tabsList.get(this.activeTabIndex);
            tabInfo.directory = file;
            tabInfo.title = string = file.getName().isEmpty() ? file.getAbsolutePath() : file.getName();
            tabInfo.lblTitle.setText(string);
            this.tabBarPanel.repaint();
        }
    }

    private JPanel createNavigationAddressBar() {
        JPanel jPanel = new JPanel(new BorderLayout(8, 0));
        jPanel.setBackground(COLOR_BG_WINDOW);
        jPanel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        JPanel jPanel2 = new JPanel(new FlowLayout(0, 4, 0));
        jPanel2.setOpaque(false);
        this.btnBack = this.createFlatIconButton(IconUtils.getBackIcon(16, IconUtils.COLOR_ICON_DARK), "Back (Alt + Left)");
        this.btnBack.setEnabled(false);
        this.btnBack.addActionListener(this);
        this.btnForward = this.createFlatIconButton(IconUtils.getForwardIcon(16, IconUtils.COLOR_ICON_DARK), "Forward (Alt + Right)");
        this.btnForward.setEnabled(false);
        this.btnForward.addActionListener(this);
        this.btnUp = this.createFlatIconButton(IconUtils.getUpIcon(16, IconUtils.COLOR_ICON_DARK), "Up to parent folder (Alt + Up)");
        this.btnUp.addActionListener(this);
        this.btnRefresh = this.createFlatIconButton(IconUtils.getRefreshIcon(16, IconUtils.COLOR_ICON_DARK), "Refresh (F5)");
        this.btnRefresh.addActionListener(this);
        jPanel2.add(this.btnBack);
        jPanel2.add(this.btnForward);
        jPanel2.add(this.btnUp);
        jPanel2.add(this.btnRefresh);
        JPanel jPanel3 = new JPanel(new BorderLayout(6, 0)){

            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(COLOR_CARD_BG);
                graphics2D.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);
                graphics2D.setColor(COLOR_BORDER_INPUT);
                graphics2D.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new Dimension(460, 32));
        jPanel3.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 6));
        this.lblAddressIcon = new JLabel(IconUtils.getFolderIcon(16));
        this.txtAddressBar = new JTextField();
        this.txtAddressBar.setFont(FONT_REGULAR);
        this.txtAddressBar.setBorder(null);
        this.txtAddressBar.setOpaque(false);
        this.txtAddressBar.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 10) {
                    FileExplorerFrame.this.navigateToAddress(FileExplorerFrame.this.txtAddressBar.getText());
                }
            }
        });
        this.btnGo = new JButton("\u27a4");
        this.btnGo.setFont(FONT_REGULAR);
        this.btnGo.setPreferredSize(new Dimension(28, 24));
        this.btnGo.setContentAreaFilled(false);
        this.btnGo.setBorder(BorderFactory.createEmptyBorder());
        this.btnGo.setFocusPainted(false);
        this.btnGo.setToolTipText("Go to path (Enter)");
        this.btnGo.setCursor(new Cursor(12));
        this.btnGo.addActionListener(this);
        jPanel3.add((Component)this.lblAddressIcon, "West");
        jPanel3.add((Component)this.txtAddressBar, "Center");
        jPanel3.add((Component)this.btnGo, "East");
        JPanel jPanel4 = new JPanel(new BorderLayout(6, 0)){

            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(COLOR_CARD_BG);
                graphics2D.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);
                graphics2D.setColor(COLOR_BORDER_INPUT);
                graphics2D.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new Dimension(220, 32));
        jPanel4.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 6));
        JLabel jLabel = new JLabel(IconUtils.getSearchIcon(14));
        this.txtSearch = new JTextField();
        this.txtSearch.setFont(FONT_REGULAR);
        this.txtSearch.setBorder(null);
        this.txtSearch.setOpaque(false);
        this.txtSearch.setToolTipText("Search files in current folder (Ctrl + F)");
        this.txtSearch.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 10) {
                    FileExplorerFrame.this.executeSearch();
                } else if (keyEvent.getKeyCode() == 27) {
                    FileExplorerFrame.this.clearSearch();
                }
            }
        });
        this.btnClearSearch = new JButton(IconUtils.getCloseIcon(11));
        this.btnClearSearch.setPreferredSize(new Dimension(22, 22));
        this.btnClearSearch.setContentAreaFilled(false);
        this.btnClearSearch.setBorder(BorderFactory.createEmptyBorder());
        this.btnClearSearch.setFocusPainted(false);
        this.btnClearSearch.setToolTipText("Clear Search (Esc)");
        this.btnClearSearch.setCursor(new Cursor(12));
        this.btnClearSearch.addActionListener(this);
        jPanel4.add((Component)jLabel, "West");
        jPanel4.add((Component)this.txtSearch, "Center");
        jPanel4.add((Component)this.btnClearSearch, "East");
        jPanel.add((Component)jPanel2, "West");
        jPanel.add((Component)jPanel3, "Center");
        jPanel.add((Component)jPanel4, "East");
        return jPanel;
    }

    private JToolBar createCommandToolBar() {
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        jToolBar.setBackground(COLOR_BG_WINDOW);
        jToolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER), BorderFactory.createEmptyBorder(3, 12, 4, 12)));
        this.btnNew = new JButton("  New \u2304", IconUtils.getNewIcon(16));
        this.styleCommandButton(this.btnNew, "Create new folder (Ctrl+Shift+N)");
        this.btnCut = this.createCommandIconButton(IconUtils.getCutIcon(16), "Cut / Move (Ctrl+X)");
        this.btnCopy = this.createCommandIconButton(IconUtils.getCopyIcon(16), "Copy (Ctrl+C)");
        this.btnPaste = this.createCommandIconButton(IconUtils.getPasteIcon(16), "Paste (Ctrl+V)");
        this.btnPaste.setEnabled(false);
        this.btnRename = this.createCommandIconButton(IconUtils.getRenameIcon(16), "Rename (F2)");
        this.btnDelete = this.createCommandIconButton(IconUtils.getDeleteIcon(16), "Delete (Delete)");
        this.btnProperties = this.createCommandIconButton(IconUtils.getPropertiesIcon(16), "Properties (Alt+Enter)");
        this.btnSort = new JButton(" Sort \u2304", IconUtils.getSortIcon(16));
        this.styleCommandButton(this.btnSort, "Sort items");
        this.btnSort.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileExplorerFrame.this.showSortPopupMenu(FileExplorerFrame.this.btnSort);
            }
        });
        this.btnViewToggle = new JButton(" View \u2304", IconUtils.getViewGridIcon(16));
        this.styleCommandButton(this.btnViewToggle, "Change view (Large Icons / Details)");
        this.btnViewToggle.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileExplorerFrame.this.showViewPopupMenu(FileExplorerFrame.this.btnViewToggle);
            }
        });
        jToolBar.add(this.btnNew);
        jToolBar.add(Box.createHorizontalStrut(6));
        jToolBar.add(this.createToolbarSeparator());
        jToolBar.add(Box.createHorizontalStrut(6));
        jToolBar.add(this.btnCut);
        jToolBar.add(Box.createHorizontalStrut(2));
        jToolBar.add(this.btnCopy);
        jToolBar.add(Box.createHorizontalStrut(2));
        jToolBar.add(this.btnPaste);
        jToolBar.add(Box.createHorizontalStrut(2));
        jToolBar.add(this.btnRename);
        jToolBar.add(Box.createHorizontalStrut(2));
        jToolBar.add(this.btnDelete);
        jToolBar.add(Box.createHorizontalStrut(2));
        jToolBar.add(this.btnProperties);
        jToolBar.add(Box.createHorizontalGlue());
        jToolBar.add(this.btnSort);
        jToolBar.add(Box.createHorizontalStrut(4));
        jToolBar.add(this.btnViewToggle);
        return jToolBar;
    }

    private void styleCommandButton(final JButton jButton, String string) {
        jButton.setFont(FONT_REGULAR);
        jButton.setForeground(COLOR_TEXT_PRIMARY);
        jButton.setFocusPainted(false);
        jButton.setContentAreaFilled(false);
        jButton.setToolTipText(string);
        jButton.setCursor(new Cursor(12));
        jButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8), BorderFactory.createEmptyBorder()));
        jButton.addMouseListener(new MouseAdapter(this){

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                jButton.setContentAreaFilled(true);
                jButton.setBackground(COLOR_HOVER_BG);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                jButton.setContentAreaFilled(false);
            }
        });
        jButton.addActionListener(this);
    }

    private JButton createCommandIconButton(Icon icon, String string) {
        final JButton jButton = new JButton(icon);
        jButton.setPreferredSize(new Dimension(34, 30));
        jButton.setFocusPainted(false);
        jButton.setContentAreaFilled(false);
        jButton.setToolTipText(string);
        jButton.setCursor(new Cursor(12));
        jButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jButton.addMouseListener(new MouseAdapter(this){

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if (jButton.isEnabled()) {
                    jButton.setContentAreaFilled(true);
                    jButton.setBackground(COLOR_HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                jButton.setContentAreaFilled(false);
            }
        });
        jButton.addActionListener(this);
        return jButton;
    }

    private JButton createFlatIconButton(Icon icon, String string) {
        final JButton jButton = new JButton(icon);
        jButton.setPreferredSize(new Dimension(30, 30));
        jButton.setFocusPainted(false);
        jButton.setContentAreaFilled(false);
        jButton.setToolTipText(string);
        jButton.setCursor(new Cursor(12));
        jButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jButton.addMouseListener(new MouseAdapter(this){

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if (jButton.isEnabled()) {
                    jButton.setContentAreaFilled(true);
                    jButton.setBackground(COLOR_HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                jButton.setContentAreaFilled(false);
            }
        });
        return jButton;
    }

    private Component createToolbarSeparator() {
        JSeparator jSeparator = new JSeparator(1);
        jSeparator.setMaximumSize(new Dimension(1, 22));
        jSeparator.setForeground(COLOR_BORDER);
        return jSeparator;
    }

    private JPanel createSidebarPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(COLOR_BG_WINDOW);
        jPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 4));
        this.sidebarModel = new DefaultListModel();
        File file = new File(System.getProperty("user.home"));
        this.sidebarModel.addElement(new SidebarItem("Home", file, IconUtils.getHomeIcon(16), false));
        this.sidebarModel.addElement(new SidebarItem("Pinned", null, IconUtils.getPinIcon(12), true));
        Map<String, File> map = this.fileManager.getQuickAccessLocations();
        File[] fileArray = map.entrySet().iterator();
        while (fileArray.hasNext()) {
            Map.Entry<String, File> object2 = fileArray.next();
            String string = object2.getKey();
            Icon icon = "Desktop".equalsIgnoreCase(string) ? IconUtils.getDesktopIcon(16) : ("Downloads".equalsIgnoreCase(string) ? IconUtils.getDownloadsIcon(16) : ("Documents".equalsIgnoreCase(string) ? IconUtils.getDocumentsIcon(16) : ("Pictures".equalsIgnoreCase(string) ? IconUtils.getPicturesIcon(16) : ("Music".equalsIgnoreCase(string) ? IconUtils.getMusicIcon(16) : IconUtils.getFolderIcon(16)))));
            this.sidebarModel.addElement(new SidebarItem(string, object2.getValue(), icon, false));
        }
        this.sidebarModel.addElement(new SidebarItem("Recycle Bin", file, IconUtils.getRecycleBinIcon(16), false));
        this.sidebarModel.addElement(new SidebarItem("Drives", null, null, true));
        for (File file2 : fileArray = this.fileManager.getSystemDrives()) {
            String string = file2.getAbsolutePath();
            try {
                String string2 = FileSystemView.getFileSystemView().getSystemDisplayName(file2);
                if (string2 != null && !string2.trim().isEmpty()) {
                    string = string2;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.sidebarModel.addElement(new SidebarItem(string, file2, IconUtils.getDriveIcon(16), false));
        }
        this.listSidebar = new JList<SidebarItem>(this.sidebarModel);
        this.listSidebar.setSelectionMode(0);
        this.listSidebar.setBackground(COLOR_BG_WINDOW);
        this.listSidebar.setCellRenderer(new SidebarCellRenderer());
        this.listSidebar.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                SidebarItem sidebarItem;
                if (!(listSelectionEvent.getValueIsAdjusting() || FileExplorerFrame.this.isUpdatingSidebarSelection || (sidebarItem = FileExplorerFrame.this.listSidebar.getSelectedValue()) == null || sidebarItem.isHeader || sidebarItem.targetFolder == null)) {
                    FileExplorerFrame.this.navigateTo(sidebarItem.targetFolder);
                }
            }
        });
        this.listSidebar.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                SidebarItem sidebarItem = FileExplorerFrame.this.listSidebar.getSelectedValue();
                if (sidebarItem != null && !sidebarItem.isHeader && sidebarItem.targetFolder != null) {
                    FileExplorerFrame.this.navigateTo(sidebarItem.targetFolder);
                }
            }
        });
        JScrollPane jScrollPane = new JScrollPane(this.listSidebar);
        jScrollPane.setBorder(null);
        jScrollPane.getViewport().setBackground(COLOR_BG_WINDOW);
        jScrollPane.setHorizontalScrollBarPolicy(31);
        jPanel.add((Component)jScrollPane, "Center");
        return jPanel;
    }

    private JPanel createMainContentCard() {
        this.cardLayout = new CardLayout();
        this.centerCardContainer = new JPanel(this.cardLayout);
        this.centerCardContainer.setBackground(COLOR_CARD_BG);
        this.centerCardContainer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 8, 12), BorderFactory.createLineBorder(COLOR_BORDER, 1)));
        JPanel jPanel = this.createGridViewPanel();
        JPanel jPanel2 = this.createTableViewPanel();
        this.centerCardContainer.add((Component)jPanel, "GRID");
        this.centerCardContainer.add((Component)jPanel2, "TABLE");
        this.cardLayout.show(this.centerCardContainer, "GRID");
        return this.centerCardContainer;
    }

    private JPanel createGridViewPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(COLOR_CARD_BG);
        this.gridList = new JList<File>(this.gridListModel);
        this.gridList.setLayoutOrientation(2);
        this.gridList.setVisibleRowCount(-1);
        this.gridList.setSelectionMode(0);
        this.gridList.setBackground(COLOR_CARD_BG);
        this.gridList.setCellRenderer(new GridCardCellRenderer());
        this.gridList.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                File file;
                if (mouseEvent.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(mouseEvent) && (file = FileExplorerFrame.this.gridList.getSelectedValue()) != null) {
                    FileExplorerFrame.this.handleOpen(file);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleGridContextMenuTrigger(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleGridContextMenuTrigger(mouseEvent);
            }
        });
        this.gridList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    FileExplorerFrame.this.updateSelectedStatus();
                }
            }
        });
        this.gridScrollPane = new JScrollPane(this.gridList);
        this.gridScrollPane.setBorder(null);
        this.gridScrollPane.getViewport().setBackground(COLOR_CARD_BG);
        this.gridScrollPane.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleBackgroundContextMenu(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleBackgroundContextMenu(mouseEvent);
            }
        });
        jPanel.add((Component)this.gridScrollPane, "Center");
        return jPanel;
    }

    private JPanel createTableViewPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(COLOR_CARD_BG);
        this.fileTable = new JTable(this.tableModel){

            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (this.getRowCount() == 0) {
                    graphics.setColor(COLOR_TEXT_MUTED);
                    graphics.setFont(FONT_REGULAR);
                    String string = "This folder is empty.";
                    int n = graphics.getFontMetrics().stringWidth(string);
                    int n2 = Math.max(10, (this.getWidth() - n) / 2);
                    int n3 = Math.max(40, this.getHeight() / 2);
                    graphics.drawString(string, n2, n3);
                }
            }
        };
        this.fileTable.setFont(FONT_REGULAR);
        this.fileTable.setRowHeight(30);
        this.fileTable.setShowGrid(false);
        this.fileTable.setIntercellSpacing(new Dimension(0, 2));
        this.fileTable.setSelectionMode(0);
        this.fileTable.setAutoResizeMode(4);
        this.fileTable.setFillsViewportHeight(true);
        this.fileTable.setBackground(COLOR_CARD_BG);
        TableColumn tableColumn = this.fileTable.getColumnModel().getColumn(0);
        tableColumn.setPreferredWidth(380);
        TableColumn tableColumn2 = this.fileTable.getColumnModel().getColumn(1);
        tableColumn2.setPreferredWidth(140);
        TableColumn tableColumn3 = this.fileTable.getColumnModel().getColumn(2);
        tableColumn3.setPreferredWidth(95);
        TableColumn tableColumn4 = this.fileTable.getColumnModel().getColumn(3);
        tableColumn4.setPreferredWidth(160);
        tableColumn.setCellRenderer(new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
                JLabel jLabel = (JLabel)super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
                if (object instanceof File) {
                    File file = (File)object;
                    jLabel.setText(" " + (file.getName().isEmpty() ? file.getAbsolutePath() : file.getName()));
                    jLabel.setIcon(IconUtils.getFileIcon(file, 18));
                    jLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                }
                return jLabel;
            }
        });
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(4);
        tableColumn3.setCellRenderer(defaultTableCellRenderer);
        this.fileTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int n;
                if (mouseEvent.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(mouseEvent) && (n = FileExplorerFrame.this.fileTable.getSelectedRow()) != -1) {
                    File file = FileExplorerFrame.this.tableModel.getFileAt(n);
                    FileExplorerFrame.this.handleOpen(file);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleTableContextMenuTrigger(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleTableContextMenuTrigger(mouseEvent);
            }
        });
        this.fileTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    FileExplorerFrame.this.updateSelectedStatus();
                }
            }
        });
        final JTableHeader jTableHeader = this.fileTable.getTableHeader();
        jTableHeader.setFont(FONT_SEMIBOLD);
        jTableHeader.setPreferredSize(new Dimension(0, 30));
        jTableHeader.setBackground(COLOR_CARD_BG);
        jTableHeader.addMouseListener(new MouseAdapter(this){
            final /* synthetic */ FileExplorerFrame this$0;
            {
                this.this$0 = fileExplorerFrame;
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int n = jTableHeader.columnAtPoint(mouseEvent.getPoint());
                if (n != -1) {
                    boolean bl = this.this$0.tableModel.getCurrentSortColumn() == n ? !this.this$0.tableModel.isAscending() : true;
                    this.this$0.tableModel.sort(n, bl);
                    this.this$0.syncGridWithTableModel();
                }
            }
        });
        this.tableScrollPane = new JScrollPane(this.fileTable);
        this.tableScrollPane.setBorder(null);
        this.tableScrollPane.getViewport().setBackground(COLOR_CARD_BG);
        this.tableScrollPane.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleBackgroundContextMenu(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                FileExplorerFrame.this.handleBackgroundContextMenu(mouseEvent);
            }
        });
        jPanel.add((Component)this.tableScrollPane, "Center");
        return jPanel;
    }

    private JPanel createStatusBar() {
        JPanel jPanel = new JPanel(new BorderLayout(15, 0));
        jPanel.setBackground(COLOR_BG_WINDOW);
        jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER), BorderFactory.createEmptyBorder(4, 16, 4, 16)));
        this.lblStatusCount = new JLabel("Loading items...");
        this.lblStatusCount.setFont(FONT_REGULAR);
        this.lblStatusCount.setForeground(COLOR_TEXT_MUTED);
        this.lblStatusClipboard = new JLabel("");
        this.lblStatusClipboard.setFont(FONT_REGULAR);
        this.lblStatusClipboard.setForeground(new Color(175, 90, 0));
        this.lblStatusSelection = new JLabel("Ready");
        this.lblStatusSelection.setFont(FONT_REGULAR);
        this.lblStatusSelection.setForeground(COLOR_ACCENT_BLUE);
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 15, 0));
        jPanel2.setOpaque(false);
        jPanel2.add(this.lblStatusClipboard);
        jPanel2.add(this.lblStatusSelection);
        jPanel.add((Component)this.lblStatusCount, "West");
        jPanel.add((Component)jPanel2, "East");
        return jPanel;
    }

    private void createContextMenus() {
        this.itemContextMenu = new JPopupMenu();
        this.itemMenuOpen = new JMenuItem("Open");
        this.itemMenuOpen.setFont(FONT_BOLD);
        this.itemMenuOpen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File file = FileExplorerFrame.this.getSelectedFile();
                if (file != null) {
                    FileExplorerFrame.this.handleOpen(file);
                }
            }
        });
        this.itemMenuCut = new JMenuItem("Cut / Move");
        this.itemMenuCut.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        this.itemMenuCut.addActionListener(this);
        this.itemMenuCopy = new JMenuItem("Copy");
        this.itemMenuCopy.setAccelerator(KeyStroke.getKeyStroke(67, 2));
        this.itemMenuCopy.addActionListener(this);
        this.itemMenuPaste = new JMenuItem("Paste");
        this.itemMenuPaste.setAccelerator(KeyStroke.getKeyStroke(86, 2));
        this.itemMenuPaste.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File file = FileExplorerFrame.this.getSelectedFile();
                if (file != null && file.isDirectory()) {
                    FileExplorerFrame.this.handlePaste(file);
                } else {
                    FileExplorerFrame.this.handlePaste(FileExplorerFrame.this.fileManager.getCurrentDirectory());
                }
            }
        });
        this.itemMenuRename = new JMenuItem("Rename");
        this.itemMenuRename.setAccelerator(KeyStroke.getKeyStroke(113, 0));
        this.itemMenuRename.addActionListener(this);
        this.itemMenuDelete = new JMenuItem("Delete");
        this.itemMenuDelete.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        this.itemMenuDelete.addActionListener(this);
        this.itemMenuProperties = new JMenuItem("Properties");
        this.itemMenuProperties.setAccelerator(KeyStroke.getKeyStroke(10, 8));
        this.itemMenuProperties.addActionListener(this);
        this.itemContextMenu.add(this.itemMenuOpen);
        this.itemContextMenu.add(new JSeparator());
        this.itemContextMenu.add(this.itemMenuCut);
        this.itemContextMenu.add(this.itemMenuCopy);
        this.itemContextMenu.add(this.itemMenuPaste);
        this.itemContextMenu.add(new JSeparator());
        this.itemContextMenu.add(this.itemMenuRename);
        this.itemContextMenu.add(this.itemMenuDelete);
        this.itemContextMenu.add(new JSeparator());
        this.itemContextMenu.add(this.itemMenuProperties);
        this.backgroundContextMenu = new JPopupMenu();
        this.bgMenuRefresh = new JMenuItem("Refresh");
        this.bgMenuRefresh.setAccelerator(KeyStroke.getKeyStroke(116, 0));
        this.bgMenuRefresh.addActionListener(this);
        this.bgMenuNewFolder = new JMenuItem("New Folder");
        this.bgMenuNewFolder.setAccelerator(KeyStroke.getKeyStroke(78, 3));
        this.bgMenuNewFolder.addActionListener(this);
        this.bgMenuPaste = new JMenuItem("Paste");
        this.bgMenuPaste.setAccelerator(KeyStroke.getKeyStroke(86, 2));
        this.bgMenuPaste.addActionListener(this);
        this.bgMenuProperties = new JMenuItem("Properties");
        this.bgMenuProperties.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileProperties.showDialog(FileExplorerFrame.this, FileExplorerFrame.this.fileManager.getCurrentDirectory());
            }
        });
        this.backgroundContextMenu.add(this.bgMenuRefresh);
        this.backgroundContextMenu.add(new JSeparator());
        this.backgroundContextMenu.add(this.bgMenuNewFolder);
        this.backgroundContextMenu.add(this.bgMenuPaste);
        this.backgroundContextMenu.add(new JSeparator());
        this.backgroundContextMenu.add(this.bgMenuProperties);
    }

    private void showSortPopupMenu(Component component) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        String[] stringArray = new String[]{"Name (A-Z)", "Name (Z-A)", "Type", "Size", "Date Modified"};
        for (int i = 0; i < stringArray.length; ++i) {
            final int n = i;
            JMenuItem jMenuItem = new JMenuItem(stringArray[i]);
            jMenuItem.addActionListener(new ActionListener(){
                final /* synthetic */ FileExplorerFrame this$0;
                {
                    this.this$0 = fileExplorerFrame;
                }

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    this.this$0.applySortIndex(n);
                }
            });
            jPopupMenu.add(jMenuItem);
        }
        jPopupMenu.show(component, 0, component.getHeight());
    }

    private void showViewPopupMenu(Component component) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem("Large Icons (Grid)");
        jMenuItem.setIcon(IconUtils.getViewGridIcon(16));
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileExplorerFrame.this.setViewMode(true);
            }
        });
        JMenuItem jMenuItem2 = new JMenuItem("Details (Table)");
        jMenuItem2.setIcon(IconUtils.getViewDetailsIcon(16));
        jMenuItem2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileExplorerFrame.this.setViewMode(false);
            }
        });
        jPopupMenu.add(jMenuItem);
        jPopupMenu.add(jMenuItem2);
        jPopupMenu.show(component, 0, component.getHeight());
    }

    private void setViewMode(boolean bl) {
        this.isGridView = bl;
        this.cardLayout.show(this.centerCardContainer, bl ? "GRID" : "TABLE");
        this.btnViewToggle.setIcon(bl ? IconUtils.getViewGridIcon(16) : IconUtils.getViewDetailsIcon(16));
        this.updateSelectedStatus();
    }

    private void handleGridContextMenuTrigger(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            int n = this.gridList.locationToIndex(mouseEvent.getPoint());
            if (n != -1 && this.gridList.getCellBounds(n, n).contains(mouseEvent.getPoint())) {
                this.gridList.setSelectedIndex(n);
                this.showItemContextMenu(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            } else {
                this.handleBackgroundContextMenu(mouseEvent);
            }
        }
    }

    private void handleTableContextMenuTrigger(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            int n = this.fileTable.rowAtPoint(mouseEvent.getPoint());
            if (n != -1) {
                this.fileTable.setRowSelectionInterval(n, n);
                this.showItemContextMenu(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            } else {
                this.handleBackgroundContextMenu(mouseEvent);
            }
        }
    }

    private void showItemContextMenu(Component component, int n, int n2) {
        File file = this.getSelectedFile();
        boolean bl = file != null;
        boolean bl2 = bl && file.isDirectory();
        this.itemMenuOpen.setEnabled(bl);
        this.itemMenuCut.setEnabled(bl);
        this.itemMenuCopy.setEnabled(bl);
        this.itemMenuRename.setEnabled(bl);
        this.itemMenuDelete.setEnabled(bl);
        this.itemMenuProperties.setEnabled(bl);
        this.itemMenuPaste.setEnabled(this.fileManager.canPaste());
        if (bl2 && this.fileManager.canPaste()) {
            this.itemMenuPaste.setText("Paste into '" + file.getName() + "'");
        } else {
            this.itemMenuPaste.setText("Paste");
        }
        this.itemContextMenu.show(component, n, n2);
    }

    private void handleBackgroundContextMenu(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            if (this.isGridView) {
                this.gridList.clearSelection();
            } else {
                this.fileTable.clearSelection();
            }
            this.bgMenuPaste.setEnabled(this.fileManager.canPaste());
            this.backgroundContextMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        }
    }

    private void setupKeyboardShortcuts() {
        KeyAdapter keyAdapter = new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int n = keyEvent.getKeyCode();
                boolean bl = keyEvent.isControlDown();
                boolean bl2 = keyEvent.isAltDown();
                boolean bl3 = keyEvent.isShiftDown();
                if (bl && bl3 && n == 78) {
                    FileExplorerFrame.this.handleNewFolder();
                } else if (bl && n == 67 && !bl3) {
                    FileExplorerFrame.this.handleCopy();
                } else if (bl && n == 88) {
                    FileExplorerFrame.this.handleCut();
                } else if (bl && n == 86) {
                    FileExplorerFrame.this.handlePaste(FileExplorerFrame.this.fileManager.getCurrentDirectory());
                } else if (bl && n == 70) {
                    FileExplorerFrame.this.txtSearch.requestFocusInWindow();
                    FileExplorerFrame.this.txtSearch.selectAll();
                } else if (bl2 && n == 10) {
                    FileExplorerFrame.this.handleProperties();
                } else if (bl2 && n == 37) {
                    if (FileExplorerFrame.this.fileManager.goBack()) {
                        FileExplorerFrame.this.refreshCurrentView();
                    }
                } else if (bl2 && n == 39) {
                    if (FileExplorerFrame.this.fileManager.goForward()) {
                        FileExplorerFrame.this.refreshCurrentView();
                    }
                } else if (bl2 && n == 38) {
                    if (FileExplorerFrame.this.fileManager.goUp()) {
                        FileExplorerFrame.this.refreshCurrentView();
                    }
                } else if (n == 10 && !bl && !bl2) {
                    File file = FileExplorerFrame.this.getSelectedFile();
                    if (file != null) {
                        FileExplorerFrame.this.handleOpen(file);
                    }
                } else if (n == 113) {
                    FileExplorerFrame.this.handleRename();
                } else if (n == 116) {
                    FileExplorerFrame.this.refreshCurrentView();
                } else if (n == 127 && !bl && !bl2) {
                    FileExplorerFrame.this.handleDelete();
                }
            }
        };
        this.gridList.addKeyListener(keyAdapter);
        this.fileTable.addKeyListener(keyAdapter);
    }

    public void navigateTo(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists() || !file.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Cannot access directory: " + file.getAbsolutePath() + "\nAccess is denied or the location does not exist.", "Access Error", 0);
            return;
        }
        boolean bl = this.fileManager.navigateTo(file);
        if (bl) {
            this.refreshCurrentView();
            this.updateActiveTabInfo(file);
        }
    }

    public void refreshCurrentView() {
        File file = this.fileManager.getCurrentDirectory();
        if (file == null || !file.exists()) {
            return;
        }
        this.txtAddressBar.setText(file.getAbsolutePath());
        this.lblAddressIcon.setIcon(IconUtils.getFolderIcon(16));
        List<File> list = this.fileManager.getFiles();
        this.tableModel.setFiles(list);
        this.gridListModel.clear();
        for (File file2 : list) {
            this.gridListModel.addElement(file2);
        }
        this.btnBack.setEnabled(this.fileManager.canGoBack());
        this.btnForward.setEnabled(this.fileManager.canGoForward());
        this.btnUp.setEnabled(file.getParentFile() != null);
        this.btnPaste.setEnabled(this.fileManager.canPaste());
        int n = list.size();
        this.lblStatusCount.setText(n + " items");
        this.updateClipboardStatus();
        this.updateSelectedStatus();
    }

    private void syncGridWithTableModel() {
        this.gridListModel.clear();
        for (int i = 0; i < this.tableModel.getRowCount(); ++i) {
            this.gridListModel.addElement(this.tableModel.getFileAt(i));
        }
    }

    private void navigateToAddress(String string) {
        if (string == null || string.trim().isEmpty()) {
            return;
        }
        File file = new File(string.trim());
        if (file.exists() && file.isDirectory()) {
            this.navigateTo(file);
        } else {
            JOptionPane.showMessageDialog(this, "The specified path does not exist or is not a directory:\n" + string, "Invalid Path", 2);
        }
    }

    private File getSelectedFile() {
        if (this.isGridView) {
            return this.gridList.getSelectedValue();
        }
        int n = this.fileTable.getSelectedRow();
        if (n != -1 && n < this.tableModel.getRowCount()) {
            return this.tableModel.getFileAt(n);
        }
        return null;
    }

    private void handleOpen(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            if (file.isDirectory()) {
                this.navigateTo(file);
            } else {
                this.lblStatusSelection.setText("Opening file: " + file.getName());
                this.fileManager.open(file);
            }
        }
        catch (IOException iOException) {
            JOptionPane.showMessageDialog(this, "Error opening item: " + iOException.getMessage(), "Open Error", 0);
        }
    }

    private void updateSelectedStatus() {
        int n;
        File file = this.getSelectedFile();
        int n2 = n = this.isGridView ? this.gridListModel.size() : this.tableModel.getRowCount();
        if (file != null) {
            this.lblStatusCount.setText(n + " items   |   1 item selected");
            String string = file.isDirectory() ? "Folder" : FileUtils.formatSize(file.length());
            this.lblStatusSelection.setText(file.getName() + " (" + string + ")");
        } else {
            this.lblStatusCount.setText(n + " items");
            this.lblStatusSelection.setText("Ready");
        }
    }

    private void updateClipboardStatus() {
        if (this.fileManager.canPaste()) {
            this.lblStatusClipboard.setText("Clipboard: " + this.fileManager.getClipboardDescription());
            this.btnPaste.setEnabled(true);
        } else {
            this.lblStatusClipboard.setText("");
            this.btnPaste.setEnabled(false);
        }
    }

    private void applySortIndex(int n) {
        switch (n) {
            case 0: {
                this.tableModel.sort(0, true);
                break;
            }
            case 1: {
                this.tableModel.sort(0, false);
                break;
            }
            case 2: {
                this.tableModel.sort(1, true);
                break;
            }
            case 3: {
                this.tableModel.sort(2, true);
                break;
            }
            case 4: {
                this.tableModel.sort(3, false);
            }
        }
        this.syncGridWithTableModel();
        this.lblStatusSelection.setText("Sorted items");
    }

    private void executeSearch() {
        String string = this.txtSearch.getText().trim();
        if (string.isEmpty()) {
            this.refreshCurrentView();
            return;
        }
        this.lblStatusSelection.setText("Searching for '" + string + "'...");
        List<File> list = this.fileManager.search(string);
        this.tableModel.setFiles(list);
        this.gridListModel.clear();
        for (File file : list) {
            this.gridListModel.addElement(file);
        }
        this.lblStatusCount.setText(list.size() + " items found");
    }

    private void clearSearch() {
        this.txtSearch.setText("");
        this.refreshCurrentView();
    }

    private void handleNewFolder() {
        String string = JOptionPane.showInputDialog(this, "Enter name for the new folder:", "Create New Folder", -1);
        if (string == null || string.trim().isEmpty()) {
            return;
        }
        try {
            File file = this.fileManager.createFolder(string);
            this.refreshCurrentView();
            this.lblStatusSelection.setText("Created folder: " + file.getName());
        }
        catch (IOException iOException) {
            JOptionPane.showMessageDialog(this, "Error creating folder: " + iOException.getMessage(), "New Folder Error", 0);
        }
    }

    private void handleRename() {
        File file = this.getSelectedFile();
        if (file == null) {
            JOptionPane.showMessageDialog(this, "Please select a file or folder to rename.", "Rename", 1);
            return;
        }
        String string = file.getName();
        String string2 = JOptionPane.showInputDialog(this, "Enter new name:", string);
        if (string2 == null || string2.trim().isEmpty() || string2.trim().equals(string)) {
            return;
        }
        try {
            File file2 = this.fileManager.rename(file, string2);
            this.refreshCurrentView();
            this.lblStatusSelection.setText("Renamed to: " + file2.getName());
        }
        catch (IOException iOException) {
            JOptionPane.showMessageDialog(this, "Error renaming: " + iOException.getMessage(), "Rename Error", 0);
        }
    }

    private void handleCopy() {
        File file = this.getSelectedFile();
        if (file == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to copy.", "Copy", 1);
            return;
        }
        this.fileManager.copy(file);
        this.updateClipboardStatus();
        this.lblStatusSelection.setText("Copied '" + file.getName() + "' to clipboard.");
    }

    private void handleCut() {
        File file = this.getSelectedFile();
        if (file == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to move.", "Move / Cut", 1);
            return;
        }
        this.fileManager.cut(file);
        this.updateClipboardStatus();
        this.lblStatusSelection.setText("Cut '" + file.getName() + "' (ready to paste).");
    }

    private void handlePaste(File file) {
        if (!this.fileManager.canPaste()) {
            JOptionPane.showMessageDialog(this, "Clipboard is empty or file no longer exists.", "Paste", 1);
            return;
        }
        if (file == null || !file.isDirectory()) {
            file = this.fileManager.getCurrentDirectory();
        }
        try {
            File file2 = this.fileManager.paste(file);
            this.refreshCurrentView();
            this.updateClipboardStatus();
            this.lblStatusSelection.setText("Pasted item: " + file2.getName());
        }
        catch (IOException iOException) {
            JOptionPane.showMessageDialog(this, "Error pasting item: " + iOException.getMessage(), "Paste Error", 0);
        }
    }

    private void handleDelete() {
        File file = this.getSelectedFile();
        if (file == null) {
            JOptionPane.showMessageDialog(this, "Please select a file or folder to delete.", "Delete", 1);
            return;
        }
        String string = file.isDirectory() ? "folder and all its contents" : "file";
        int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete this " + string + "?\n\n" + file.getName(), "Confirm Delete", 0, 2);
        if (n == 0) {
            try {
                this.fileManager.delete(file);
                this.refreshCurrentView();
                this.lblStatusSelection.setText("Deleted: " + file.getName());
            }
            catch (IOException iOException) {
                JOptionPane.showMessageDialog(this, "Error deleting item: " + iOException.getMessage(), "Delete Error", 0);
            }
        }
    }

    private void handleProperties() {
        File file = this.getSelectedFile();
        if (file == null) {
            file = this.fileManager.getCurrentDirectory();
        }
        FileProperties.showDialog(this, file);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.btnBack) {
            if (this.fileManager.goBack()) {
                this.refreshCurrentView();
            }
        } else if (object == this.btnForward) {
            if (this.fileManager.goForward()) {
                this.refreshCurrentView();
            }
        } else if (object == this.btnUp) {
            if (this.fileManager.goUp()) {
                this.refreshCurrentView();
            }
        } else if (object == this.btnRefresh || object == this.bgMenuRefresh) {
            this.refreshCurrentView();
            this.lblStatusSelection.setText("Refreshed.");
        } else if (object == this.btnGo) {
            this.navigateToAddress(this.txtAddressBar.getText());
        } else if (object == this.btnClearSearch) {
            this.clearSearch();
        } else if (object == this.btnNew || object == this.bgMenuNewFolder) {
            this.handleNewFolder();
        } else if (object == this.btnRename || object == this.itemMenuRename) {
            this.handleRename();
        } else if (object == this.btnCopy || object == this.itemMenuCopy) {
            this.handleCopy();
        } else if (object == this.btnCut || object == this.itemMenuCut) {
            this.handleCut();
        } else if (object == this.btnPaste || object == this.bgMenuPaste) {
            this.handlePaste(this.fileManager.getCurrentDirectory());
        } else if (object == this.btnDelete || object == this.itemMenuDelete) {
            this.handleDelete();
        } else if (object == this.btnProperties || object == this.itemMenuProperties) {
            this.handleProperties();
        }
    }

    private static class TabInfo {
        String title;
        File directory;
        JPanel tabComponent;
        JLabel lblTitle;
        JLabel lblIcon;

        TabInfo(String string, File file) {
            this.title = string;
            this.directory = file;
        }
    }

    private static class SidebarItem {
        final String displayName;
        final File targetFolder;
        final Icon icon;
        final boolean isHeader;

        SidebarItem(String string, File file, Icon icon, boolean bl) {
            this.displayName = string;
            this.targetFolder = file;
            this.icon = icon;
            this.isHeader = bl;
        }

        public String toString() {
            return this.displayName;
        }
    }

    private class SidebarCellRenderer
    extends JPanel
    implements ListCellRenderer<SidebarItem> {
        private final JLabel lblIcon = new JLabel();
        private final JLabel lblText = new JLabel();
        private boolean isSelected = false;
        private boolean isHeader = false;

        SidebarCellRenderer() {
            this.setLayout(new FlowLayout(0, 10, 6));
            this.setOpaque(false);
            this.add(this.lblIcon);
            this.add(this.lblText);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends SidebarItem> jList, SidebarItem sidebarItem, int n, boolean bl, boolean bl2) {
            this.isSelected = bl && !sidebarItem.isHeader;
            this.isHeader = sidebarItem.isHeader;
            this.lblText.setText(sidebarItem.displayName);
            if (sidebarItem.isHeader) {
                this.lblIcon.setIcon(sidebarItem.icon);
                this.lblText.setFont(FONT_SECTION);
                this.lblText.setForeground(COLOR_TEXT_MUTED);
                this.setBorder(BorderFactory.createEmptyBorder(10, 4, 2, 4));
            } else {
                this.lblIcon.setIcon(sidebarItem.icon);
                this.lblText.setFont(this.isSelected ? FONT_BOLD : FONT_REGULAR);
                this.lblText.setForeground(this.isSelected ? COLOR_TEXT_PRIMARY : COLOR_TEXT_PRIMARY);
                this.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
            }
            return this;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            if (this.isSelected && !this.isHeader) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(COLOR_HOVER_BG);
                graphics2D.fillRoundRect(2, 2, this.getWidth() - 4, this.getHeight() - 4, 6, 6);
                graphics2D.setColor(COLOR_ACCENT_BLUE);
                graphics2D.fillRoundRect(3, 5, 3, this.getHeight() - 10, 2, 2);
                graphics2D.dispose();
            }
            super.paintComponent(graphics);
        }
    }

    private class GridCardCellRenderer
    extends JPanel
    implements ListCellRenderer<File> {
        private final JLabel lblIcon = new JLabel();
        private final JLabel lblName = new JLabel();
        private boolean isSelected = false;

        GridCardCellRenderer() {
            this.setLayout(new BoxLayout(this, 1));
            this.setPreferredSize(new Dimension(112, 116));
            this.setOpaque(false);
            this.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            this.lblIcon.setAlignmentX(0.5f);
            this.lblName.setAlignmentX(0.5f);
            this.lblName.setFont(FONT_SMALL);
            this.lblName.setHorizontalAlignment(0);
            this.add(this.lblIcon);
            this.add(Box.createVerticalStrut(6));
            this.add(this.lblName);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends File> jList, File file, int n, boolean bl, boolean bl2) {
            this.isSelected = bl;
            if (file != null) {
                String string = file.getName().isEmpty() ? file.getAbsolutePath() : file.getName();
                this.lblName.setText(string);
                this.lblName.setToolTipText(string);
                Icon icon = IconUtils.getFileIcon(file, 56);
                this.lblIcon.setIcon(icon);
            }
            return this;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            if (this.isSelected) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(COLOR_SELECTION_BG);
                graphics2D.fillRoundRect(2, 2, this.getWidth() - 4, this.getHeight() - 4, 6, 6);
                graphics2D.setColor(COLOR_ACCENT_BLUE);
                graphics2D.drawRoundRect(2, 2, this.getWidth() - 5, this.getHeight() - 5, 6, 6);
                graphics2D.dispose();
            }
            super.paintComponent(graphics);
        }
    }
}

