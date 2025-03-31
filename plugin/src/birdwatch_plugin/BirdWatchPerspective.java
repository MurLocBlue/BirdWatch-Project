package birdwatch_plugin;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class BirdWatchPerspective implements IPerspectiveFactory {

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