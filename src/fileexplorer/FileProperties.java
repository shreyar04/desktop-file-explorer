package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

/**
 * FileProperties: Encapsulates file metadata attributes and renders a modal
 * Windows-style File Properties dialog.
 *
 * Demonstrates Syllabus Concepts:
 * - Classes, Objects and Constructors
 * - Encapsulation (private fields, public getters)
 * - Layout Managers (GridBagLayout, BorderLayout, BoxLayout)
 * - GUI Components (JDialog, JLabel, JButton, JPanel, JSeparator)
 * - Event Handling (ActionListener via Anonymous Class)
 * - String Formatting & Exception Handling
 */
public class FileProperties {

    // Encapsulated property fields
    private final File file;
    private final String name;
    private final String location;
    private final String type;
    private final long sizeBytes;
    private final String formattedSize;
    private final String dateModified;
    private final boolean directory;
    private final boolean readable;
    private final boolean writable;
    private final boolean hidden;
    private int folderCount = 0;
    private int fileCount = 0;

    /**
     * Constructor initializes and populates all properties for the given file.
     *
     * @param targetFile File to extract metadata from
     */
    public FileProperties(File targetFile) {
        this.file = targetFile;
        if (targetFile != null && targetFile.exists()) {
            this.name = targetFile.getName().isEmpty() ? targetFile.getAbsolutePath() : targetFile.getName();
            File parent = targetFile.getParentFile();
            this.location = parent != null ? parent.getAbsolutePath() : targetFile.getAbsolutePath();
            this.type = FileUtils.getFileType(targetFile);
            this.directory = targetFile.isDirectory();

            if (directory) {
                int[] counts = FileUtils.countItems(targetFile);
                this.folderCount = counts[0];
                this.fileCount = counts[1];
                this.sizeBytes = FileUtils.calculateTotalSize(targetFile);
            } else {
                this.sizeBytes = targetFile.length();
            }

            this.formattedSize = FileUtils.formatSize(this.sizeBytes) + String.format(" (%,d bytes)", this.sizeBytes);
            this.dateModified = FileUtils.formatDate(targetFile.lastModified());
            this.readable = targetFile.canRead();
            this.writable = targetFile.canWrite();
            this.hidden = targetFile.isHidden();
        } else {
            this.name = "Unknown";
            this.location = "-";
            this.type = "Unknown";
            this.sizeBytes = 0L;
            this.formattedSize = "0 B";
            this.dateModified = "-";
            this.directory = false;
            this.readable = false;
            this.writable = false;
            this.hidden = false;
        }
    }

    // Getters demonstrating Encapsulation
    public File getFile() { return file; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public long getSizeBytes() { return sizeBytes; }
    public String getFormattedSize() { return formattedSize; }
    public String getDateModified() { return dateModified; }
    public boolean isDirectory() { return directory; }
    public boolean isReadable() { return readable; }
    public boolean isWritable() { return writable; }
    public boolean isHidden() { return hidden; }
    public int getFolderCount() { return folderCount; }
    public int getFileCount() { return fileCount; }

    /**
     * Displays a clean, modal Properties dialog displaying detailed file information.
     *
     * @param parentComponent Parent GUI component for centering
     * @param targetFile Target file or directory
     */
    public static void showDialog(Component parentComponent, File targetFile) {
        if (targetFile == null || !targetFile.exists()) {
            return;
        }

        FileProperties props = new FileProperties(targetFile);
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(parentComponent);
        JDialog dialog = new JDialog(owner, props.getName() + " Properties", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 480);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

        // Header Panel: Icon and File Name
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(targetFile);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(36, 36));

        JLabel titleLabel = new JLabel(props.getName());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());

        // Body Panel: Form grid layout with properties
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Separator
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        bodyPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Type
        addPropertyRow(bodyPanel, gbc, row++, "Type:", props.getType());

        // Location
        addPropertyRow(bodyPanel, gbc, row++, "Location:", props.getLocation());

        // Size
        addPropertyRow(bodyPanel, gbc, row++, "Size:", props.getFormattedSize());

        // Contains (if directory)
        if (props.isDirectory()) {
            String containsStr = props.getFileCount() + " Files, " + props.getFolderCount() + " Folders";
            addPropertyRow(bodyPanel, gbc, row++, "Contains:", containsStr);
        }

        // Separator
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        bodyPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Modified Date
        addPropertyRow(bodyPanel, gbc, row++, "Modified:", props.getDateModified());

        // Permissions / Attributes
        String attributes = (props.isReadable() ? "Read " : "") +
                            (props.isWritable() ? "Write " : "") +
                            (props.isHidden() ? "[Hidden]" : "");
        if (attributes.trim().isEmpty()) {
            attributes = "None";
        }
        addPropertyRow(bodyPanel, gbc, row++, "Attributes:", attributes.trim());

        // Bottom Button Panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(85, 28));

        // Event Handling: ActionListener using Anonymous Class
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Assemble main dialog
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(bodyPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Helper method to add a labeled property row to GridBagLayout.
     */
    private static void addPropertyRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        JLabel valLbl = new JLabel(value != null ? value : "-");
        panel.add(valLbl, gbc);
    }
}
