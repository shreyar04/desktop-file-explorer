/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileExplorerFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] stringArray) {
        System.out.println("Desktop File Explorer Started Successfully!");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception) {
            System.err.println("Notice: Could not set System Look and Feel. Using default Swing theme.");
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                FileExplorerFrame fileExplorerFrame = new FileExplorerFrame();
                fileExplorerFrame.setVisible(true);
            }
        });
    }
}

