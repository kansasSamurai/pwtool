package org.kssamurai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import javax.swing.*;

/**
 * Main application class for a simple Swing application that demonstrates drag and drop functionality.
 */
public final class App extends JFrame implements DragGestureListener {

    private final JTextArea textArea;

    private final DragSource ds;

    /**
     * Constructor for the App class.
     * Initializes the GUI components and sets up drag and drop functionality.
     */
    public App() {
        super("Text Drag and Drop");

        JPanel contents = new JPanel();
        contents.setLayout(new BorderLayout());

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        contents.add(scrollPane, BorderLayout.CENTER);

        final JLabel label = new JLabel("APP [ENV]");
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 25));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setTransferHandler(new TransferHandler("text"));
        contents.add(label, BorderLayout.NORTH);
        // Install necessary code for the label to have drag and drop functionality
        new LabelListener(label, "hiddenpassword");

        // Initialize drag and drop functionality
        ds = DragSource.getDefaultDragSource();
        ds.createDefaultDragGestureRecognizer(textArea, DnDConstants.ACTION_COPY_OR_MOVE, this);

        this.add(contents);
        setVisible(true);
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {

        String text = textArea.getSelectedText();
        if (text != null && !text.isEmpty()) {
            StringSelection ss = new StringSelection(text);
            ds.startDrag(dge, DragSource.DefaultCopyDrop, ss, new DragSourceAdapter() {

                @Override
                @SuppressWarnings(value = { "EmptyBlock" })
                public void dragDropEnd(DragSourceDropEvent e) {
                    if (e.getDropSuccess() && e.getDropAction() == DnDConstants.ACTION_MOVE) {
                        // I do not want to remove the text from the source
                        // textArea.replaceSelection("");
                    }
                }

            });
        }
    }

    /**
     * Run the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting application...");
        // Use the Event Dispatch Thread to create and show the GUI
        SwingUtilities.invokeLater(App::new);
    }

}
