package org.kssamurai;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

/**
 * LabelListener is a class that implements drag and drop functionality for a JLabel.
 * It allows the label to be dragged and dropped, transferring its text or an alternative value.
 */
public class LabelListener implements DragGestureListener {

    private final String altValue;

    private final JLabel sourceLabel;

    private final DragSource dragSource;

    /**
     * Constructor for LabelListener.
     *
     * @param label The JLabel to which this listener will be attached.
     * @param alt   An alternative value to be used during drag and drop, if provided.
     */
    public LabelListener(JLabel label, String alt) {
        this.altValue = alt;
        this.sourceLabel = label;

        this.sourceLabel.setTransferHandler(new LabelTransferHandler());

        // Create a mouselistener for a JLabel that can be dragged
        // https://docs.oracle.com/javase/8/docs/technotes/guides/swing/1.4/dnd.html#AddingSupport
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler th = c.getTransferHandler();
                th.exportAsDrag(c, e, TransferHandler.COPY);
            }
        };
        this.sourceLabel.addMouseListener(ml);

        // Set the drag source for the label
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(sourceLabel, DnDConstants.ACTION_COPY, this);

    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        // Handle the drag gesture here
        System.out.println("Drag gesture recognized");

        // You can initiate the drag operation here
        // For example, you can start a drag operation with a specific data flavor
        StringSelection ss = new StringSelection(sourceLabel.getText());
        dragSource.startDrag(dge, DragSource.DefaultCopyDrop, ss,
                new DragSourceListener() {
                    @Override
                    public void dragDropEnd(DragSourceDropEvent dsde) {
                        // Handle the end of the drag operation
                        System.out.println("Drag operation ended");
                    }

                    @Override
                    public void dragEnter(DragSourceDragEvent dsde) {
                        // Handle the drag enter event
                    }

                    @Override
                    public void dragExit(DragSourceEvent dse) {
                        // Handle the drag exit event
                    }

                    @Override
                    public void dragOver(DragSourceDragEvent dsde) {
                        // Handle the drag over event
                    }

                    @Override
                    public void dropActionChanged(DragSourceDragEvent dsde) {
                        // Handle the drop action changed event
                    }
                });
    }

    private class LabelTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JLabel label = (JLabel) c;
            if (altValue != null && !altValue.isEmpty()) {
                return new StringSelection(altValue);
            } else {
                return new StringSelection(label.getText());
            }
        }

        @Override
        @SuppressWarnings(value = { "EmptyBlock" })
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == DnDConstants.ACTION_MOVE) {
                // If the action is MOVE, you can clear the label text or perform any other action
                // For example, you can clear the label text
                // ((JLabel) source).setText("");
            }
        }
    }

}
