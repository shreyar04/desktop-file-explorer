package fileexplorer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main: Application entry point for Desktop File Explorer.
 *
 * Demonstrates Syllabus Concepts:
 * - Application lifecycle and main method
 * - Exception Handling (try-catch for LookAndFeel)
 * - Java Swing Event Dispatch Thread (SwingUtilities.invokeLater)
 * - Anonymous Class / Lambda execution for thread safety
 */
public class Main {

    public static void main(String[] args) {
        // Output confirmation required by project specification
        System.out.println("Desktop File Explorer Started Successfully!");

        // Set native Windows Look and Feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Notice: Could not set System Look and Feel. Using default Swing theme.");
        }

        // Launch GUI safely on the Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FileExplorerFrame frame = new FileExplorerFrame();
                frame.setVisible(true);
            }
        });
    }
}
