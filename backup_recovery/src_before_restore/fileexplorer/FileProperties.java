package fileexplorer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.io.File;

public class FileProperties {

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

    // Constructor
    public FileProperties(File targetFile) {

        this.file = targetFile;

        if (targetFile != null && targetFile.exists()) {

            this.name = targetFile.getName().isEmpty()
                    ? targetFile.getAbsolutePath()
                    : targetFile.getName();

            File parent = targetFile.getParentFile();

            this.location = parent != null
                    ? parent.getAbsolutePath()
                    : targetFile.getAbsolutePath();

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

            this.formattedSize = FileUtils.formatSize(sizeBytes)
                    + " (" + sizeBytes + " bytes)";

            this.dateModified = FileUtils.formatDate(targetFile.lastModified());
            this.readable = targetFile.canRead();
            this.writable = targetFile.canWrite();
            this.hidden = targetFile.isHidden();

        } else {

            this.name = "Unknown";
            this.location = "-";
            this.type = "Unknown";
            this.sizeBytes = 0;
            this.formattedSize = "0 B";
            this.dateModified = "-";
            this.directory = false;
            this.readable = false;
            this.writable = false;
            this.hidden = false;
        }
    }

    // Getters
    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getFormattedSize() {
        return formattedSize;
    }

    public String getDateModified() {
        return dateModified;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getFolderCount() {
        return folderCount;
    }

    public int getFileCount() {
        return fileCount;
    }

    // Display Properties Dialog
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

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15));

        Icon icon = FileSystemView.getFileSystemView()
                .getSystemIcon(targetFile);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(36, 36));

        JLabel titleLabel = new JLabel(props.getName());
        titleLabel.setFont(
                titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());

        // Body
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;

        bodyPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;

        // Properties
        addPropertyRow(bodyPanel, gbc, row++, "Type:", props.getType());
        addPropertyRow(bodyPanel, gbc, row++, "Location:", props.getLocation());
        addPropertyRow(bodyPanel, gbc, row++, "Size:", props.getFormattedSize());

        // Folder information
        if (props.isDirectory()) {

            String contains = props.getFileCount() + " Files, "
                    + props.getFolderCount() + " Folders";

            addPropertyRow(bodyPanel, gbc, row++, "Contains:", contains);
        }

        // Separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;

        bodyPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;

        // Modified date
        addPropertyRow(bodyPanel, gbc, row++, "Modified:",
                props.getDateModified());

        // Attributes
        String attributes = "";

        if (props.isReadable()) {
            attributes += "Read ";
        }

        if (props.isWritable()) {
            attributes += "Write ";
        }

        if (props.isHidden()) {
            attributes += "[Hidden]";
        }

        if (attributes.trim().isEmpty()) {
            attributes = "None";
        }

        addPropertyRow(bodyPanel, gbc, row++, "Attributes:",
                attributes.trim());

        // Button
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(85, 28));

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Add panels to dialog
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(bodyPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Add one property row
    private static void addPropertyRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            String value) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(
                labelComponent.getFont().deriveFont(Font.BOLD));

        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;

        JLabel valueComponent = new JLabel(
                value != null ? value : "-");

        panel.add(valueComponent, gbc);
    }
}