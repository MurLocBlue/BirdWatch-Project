package birdwatch_plugin;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Defines the initial layout for the BirdWatch perspective.
 * Configures the arrangement of views and editors in the workbench window.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class BirdWatchPerspective implements IPerspectiveFactory {

    /**
     * Creates the initial layout for the BirdWatch perspective.
     * Places the BirdWatch view on the left side of the editor area.
     *
     * @param layout The page layout to configure
     */
    @Override
    public void createInitialLayout(IPageLayout layout) {
        // Get the editor area
        String editorArea = layout.getEditorArea();
        
        // Add the BirdWatch view to the left side
        layout.addView("birdwatch_plugin.views.BirdWatchView", IPageLayout.LEFT, 0.7f, editorArea);
        
        // Make the editor area visible
        layout.setEditorAreaVisible(true);
    }
} 