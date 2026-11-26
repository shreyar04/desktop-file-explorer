/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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

    public FileProperties(File file) {
        this.file = file;
        if (file != null && file.exists()) {
            this.name = file.getName().isEmpty() ? file.getAbsolutePath() : file.getName();
            File file2 = file.getParentFile();
            this.location = file2 != null ? file2.getAbsolutePath() : file.getAbsolutePath();
            this.type = FileUtils.getFileType(file);
            this.directory = file.isDirectory();
            if (this.directory) {
                int[] nArray = FileUtils.countItems(file);
                this.folderCount = nArray[0];
                this.fileCount = nArray[1];
                this.sizeBytes = FileUtils.calculateTotalSize(file);
            } else {
                this.sizeBytes = file.length();
            }
            this.formattedSize = FileUtils.formatSize(this.sizeBytes) + " (" + this.sizeBytes + " bytes)";
            this.dateModified = FileUtils.formatDate(file.lastModified());
            this.readable = file.canRead();
            this.writable = file.canWrite();
            this.hidden = file.isHidden();
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

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public String getType() {
        return this.type;
    }

    public long getSizeBytes() {
        return this.sizeBytes;
    }

    public String getFormattedSize() {
        return this.formattedSize;
    }

    public String getDateModified() {
        return this.dateModified;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public boolean isReadable() {
        return this.readable;
    }

    public boolean isWritable() {
        return this.writable;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public int getFolderCount() {
        return this.folderCount;
    }

    public int getFileCount() {
        return this.fileCount;
    }

    public static void showDialog(Component component, File file) {
        Object object;
        if (file == null || !file.exists()) {
            return;
        }
        FileProperties fileProperties = new FileProperties(file);
        Frame frame = (Frame)SwingUtilities.getWindowAncestor(component);
        final JDialog jDialog = new JDialog(frame, fileProperties.getName() + " Properties", true);
        jDialog.setLayout(new BorderLayout());
        jDialog.setSize(420, 480);
        jDialog.setResizable(false);
        jDialog.setLocationRelativeTo(component);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 0));
        jPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        JLabel jLabel = new JLabel(icon);
        jLabel.setPreferredSize(new Dimension(36, 36));
        JLabel jLabel2 = new JLabel(fileProperties.getName());
        jLabel2.setFont(jLabel2.getFont().deriveFont(1, 14.0f));
        jLabel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel.add(jLabel);
        jPanel.add(jLabel2);
        jPanel.add(Box.createHorizontalGlue());
        JPanel jPanel2 = new JPanel(new GridBagLayout());
        jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel jPanel3 = jPanel2;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints.fill = 2;
        int n = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = n++;
        gridBagConstraints.gridwidth = 2;
        jPanel2.add((Component)new JSeparator(), gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Type:", fileProperties.getType());
        FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Location:", fileProperties.getLocation());
        FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Size:", fileProperties.getFormattedSize());
        if (fileProperties.isDirectory()) {
            object = fileProperties.getFileCount() + " Files, " + fileProperties.getFolderCount() + " Folders";
            FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Contains:", (String)object);
        }
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = n++;
        gridBagConstraints.gridwidth = 2;
        jPanel2.add((Component)new JSeparator(), gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Modified:", fileProperties.getDateModified());
        object = (fileProperties.isReadable() ? "Read " : "") + (fileProperties.isWritable() ? "Write " : "") + (fileProperties.isHidden() ? "[Hidden]" : "");
        if (((String)object).trim().isEmpty()) {
            object = "None";
        }
        FileProperties.addPropertyRow(jPanel2, gridBagConstraints, n++, "Attributes:", ((String)object).trim());
        JPanel jPanel4 = new JPanel();
        JButton jButton = new JButton("OK");
        jButton.setPreferredSize(new Dimension(85, 28));
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jDialog.dispose();
            }
        });
        jPanel4.add(jButton);
        jPanel4.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        jDialog.add((Component)jPanel, "North");
        jDialog.add((Component)jPanel2, "Center");
        jDialog.add((Component)jPanel4, "South");
        jDialog.setVisible(true);
    }

    private static void addPropertyRow(JPanel jPanel, GridBagConstraints gridBagConstraints, int n, String string, String string2) {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = n;
        gridBagConstraints.weightx = 0.25;
        JLabel jLabel = new JLabel(string);
        jLabel.setFont(jLabel.getFont().deriveFont(1));
        jPanel.add((Component)jLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 0.75;
        JLabel jLabel2 = new JLabel(string2 != null ? string2 : "-");
        jPanel.add((Component)jLabel2, gridBagConstraints);
    }
}

