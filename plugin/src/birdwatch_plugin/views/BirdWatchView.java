package birdwatch_plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

/**
 * Main view class for the BirdWatch plugin.
 * Provides a tabbed interface containing Birds and Sightings views.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class BirdWatchView extends ViewPart {
    /** The unique identifier for this view */
    public static final String ID = "birdwatch_plugin.views.BirdWatchView";
    private CTabFolder tabFolder;
    private BirdsView birdsView;
    private SightingsView sightingsView;

    /**
     * Creates the view's controls and initializes the tabbed interface.
     * Sets up the Birds and Sightings tabs with their respective views.
     *
     * @param parent The parent composite
     */
    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        
        tabFolder = new CTabFolder(parent, SWT.NONE);
        
        // Create Birds tab
        CTabItem birdsTab = new CTabItem(tabFolder, SWT.NONE);
        birdsTab.setText("Birds");
        birdsView = new BirdsView();
        birdsTab.setControl(birdsView.createControl(tabFolder));
        
        // Create Sightings tab
        CTabItem sightingsTab = new CTabItem(tabFolder, SWT.NONE);
        sightingsTab.setText("Sightings");
        sightingsView = new SightingsView();
        sightingsTab.setControl(sightingsView.createControl(tabFolder));

        // Add tab selection listener to refresh data
        tabFolder.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                int selectedIndex = tabFolder.getSelectionIndex();
                if (selectedIndex == 0) { // Birds tab
                    birdsView.loadData();
                } else if (selectedIndex == 1) { // Sightings tab
                    sightingsView.loadData();
                }
            }
        });
    }

    /**
     * Sets focus to the tab folder when the view is activated.
     */
    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }

    /**
     * Disposes of the view's resources.
     * Cleans up the Birds and Sightings views.
     */
    @Override
    public void dispose() {
        if (birdsView != null) {
            birdsView.dispose();
        }
        if (sightingsView != null) {
            sightingsView.dispose();
        }
        super.dispose();
    }
} 