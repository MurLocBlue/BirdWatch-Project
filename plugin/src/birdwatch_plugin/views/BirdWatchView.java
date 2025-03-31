package birdwatch_plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class BirdWatchView extends ViewPart {
    public static final String ID = "birdwatch_plugin.views.BirdWatchView";
    private CTabFolder tabFolder;
    private BirdsView birdsView;
    private SightingsView sightingsView;

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
    }

    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }

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