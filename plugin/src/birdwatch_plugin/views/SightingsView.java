package birdwatch_plugin.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;

import birdwatch_plugin_datastore.api.ApiClient;
import birdwatch_plugin_datastore.model.Bird;
import birdwatch_plugin_datastore.model.Sighting;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * View class for managing bird sightings in the BirdWatch system.
 * Provides functionality to view, add, delete, and search bird sightings.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class SightingsView {
    private TableViewer viewer;
    private Table table;
    private ApiClient apiClient;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private Text birdNameSearchText;
    private Text locationSearchText;
    private DateTime startDateSearchDate;
    private DateTime startDateSearchTime;
    private DateTime endDateSearchDate;
    private DateTime endDateSearchTime;

    /**
     * Constructs a new SightingsView.
     * Initializes the API client for sighting operations.
     */
    public SightingsView() {
        this.apiClient = new ApiClient();
    }

    /**
     * Creates the control for this view.
     * Sets up the search fields, buttons, and table for sighting management.
     *
     * @param parent The parent composite
     * @return The created composite containing all controls
     */
    public Composite createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        // Create search container
        Composite searchContainer = new Composite(container, SWT.NONE);
        searchContainer.setLayout(new GridLayout(6, false));
        searchContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        // Bird name search label
        Label birdNameSearchLabel = new Label(searchContainer, SWT.NONE);
        birdNameSearchLabel.setText("Bird Name:");
        birdNameSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        // Bird name search text field
        birdNameSearchText = new Text(searchContainer, SWT.BORDER);
        birdNameSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Location search label
        Label locationSearchLabel = new Label(searchContainer, SWT.NONE);
        locationSearchLabel.setText("Location:");
        locationSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        // Location search text field
        locationSearchText = new Text(searchContainer, SWT.BORDER);
        locationSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // Perform search button
        Button searchButton = new Button(searchContainer, SWT.NONE);
        searchButton.setText("      Search      ");
        searchButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        searchButton.addListener(SWT.Selection, event -> performSearch());
        
        // Clear Filters button
        Button clearButton = new Button(searchContainer, SWT.NONE);
        clearButton.setText("Clear");
        clearButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        clearButton.addListener(SWT.Selection, event -> clearSearch());
        
        // Create second line of search container
        Composite searchContainerDates = new Composite(container, SWT.NONE);
        searchContainerDates.setLayout(new GridLayout(4, false));
        searchContainerDates.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        
        // Date start search label
        Label startDateSearchLabel = new Label(searchContainerDates, SWT.NONE);
        startDateSearchLabel.setText("Start date:");
        startDateSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        // Date start search date field
        startDateSearchDate = new DateTime(searchContainerDates, SWT.DATE | SWT.BORDER);
        startDateSearchDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // Date end search label
        Label endDateaSearchLabel = new Label(searchContainerDates, SWT.NONE);
        endDateaSearchLabel.setText("End date:");
        endDateaSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        // Date end search date field
        endDateSearchDate = new DateTime(searchContainerDates, SWT.DATE | SWT.BORDER);
        endDateSearchDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // Create third line of search container
        Composite searchContainerTimes = new Composite(container, SWT.NONE);
        searchContainerTimes.setLayout(new GridLayout(4, false));
        searchContainerTimes.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        
        // Date start time search label
        Label startDateSearchTimeLabel = new Label(searchContainerTimes, SWT.NONE);
        startDateSearchTimeLabel.setText("Start time:");
        startDateSearchTimeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        // Date start time search date field
        startDateSearchTime = new DateTime(searchContainerTimes, SWT.TIME | SWT.BORDER);
        startDateSearchTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // Date start time search label
        Label endDateSearchTimeLabel = new Label(searchContainerTimes, SWT.NONE);
        endDateSearchTimeLabel.setText("End time:");
        endDateSearchTimeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        // Date start time search date field
        endDateSearchTime = new DateTime(searchContainerTimes, SWT.TIME | SWT.BORDER);
        endDateSearchTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // Add search listeners
//		birdNameSearchText.addListener(SWT.Modify, event -> performSearch());
//		locationSearchText.addListener(SWT.Modify, event -> performSearch());
//		startDateSearchDate.addListener(SWT.Modify, event -> performSearch());
//		startDateSearchTime.addListener(SWT.Modify, event -> performSearch());
//		endDateSearchDate.addListener(SWT.Modify, event -> performSearch());
//		endDateSearchTime.addListener(SWT.Modify, event -> performSearch());

        // Create button container
        Composite buttonContainer = new Composite(container, SWT.NONE);
        buttonContainer.setLayout(new GridLayout(2, false));
        buttonContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        // Add Sighting button
        Button addButton = new Button(buttonContainer, SWT.PUSH);
        addButton.setText("Add New Sighting");
        addButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        addButton.addListener(SWT.Selection, event -> {
            AddSightingDialog dialog = new AddSightingDialog(Display.getCurrent().getActiveShell());
            dialog.open();
            if (dialog.getSighting() != null) {
                addButton.setEnabled(false); // Disable button while processing
                apiClient.createSighting(dialog.getSighting())
                    .thenAccept(sighting -> {
                        Display.getDefault().asyncExec(() -> {
                            try {
                                loadData(); // Refresh the table
                                MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
                                messageBox.setMessage("Sighting added successfully!");
                                messageBox.open();
                            } catch (Exception e) {
                                MessageBox errorBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
                                errorBox.setMessage("Error refreshing table: " + e.getMessage());
                                errorBox.open();
                            } finally {
                                addButton.setEnabled(true); // Re-enable button
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        Display.getDefault().asyncExec(() -> {
                            MessageBox errorBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
                            errorBox.setMessage("Error adding sighting: " + throwable.getMessage());
                            errorBox.open();
                            addButton.setEnabled(true); // Re-enable button
                        });
                        return null;
                    });
            }
        });

        // Delete Sighting button
        Button deleteButton = new Button(buttonContainer, SWT.PUSH);
        deleteButton.setText("Delete Selected Sighting");
        deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        deleteButton.addListener(SWT.Selection, event -> {
            IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
            Sighting selectedSighting = (Sighting) selection.getFirstElement();
            
            if (selectedSighting == null) {
                MessageBox errorBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
                errorBox.setMessage("Please select a sighting to delete");
                errorBox.open();
                return;
            }

            MessageBox confirmBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            confirmBox.setMessage("Are you sure you want to delete this sighting?");
            if (confirmBox.open() == SWT.YES) {
                deleteButton.setEnabled(false); // Disable button while processing
                apiClient.deleteSighting(selectedSighting.getId())
                    .thenAccept(v -> {
                        Display.getDefault().asyncExec(() -> {
                            try {
                                loadData(); // Refresh the table
                                MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
                                messageBox.setMessage("Sighting deleted successfully!");
                                messageBox.open();
                            } catch (Exception e) {
                                MessageBox errorBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
                                errorBox.setMessage("Error refreshing table: " + e.getMessage());
                                errorBox.open();
                            } finally {
                                deleteButton.setEnabled(true); // Re-enable button
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        Display.getDefault().asyncExec(() -> {
                            MessageBox errorBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
                            errorBox.setMessage("Error deleting sighting: " + throwable.getMessage());
                            errorBox.open();
                            deleteButton.setEnabled(true); // Re-enable button
                        });
                        return null;
                    });
            }
        });

        // Create table container
        Composite tableContainer = new Composite(container, SWT.NONE);
        tableContainer.setLayout(new FillLayout());
        tableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        viewer = new TableViewer(tableContainer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Create columns
        createColumns();

        // Set content provider
        viewer.setContentProvider(new ArrayContentProvider());

        // Load data
        loadData();
        
        // Reset filter fields
        clearSearch();

        return container;
    }

    /**
     * Dialog class for adding new sightings.
     * Provides input fields for sighting properties and handles validation.
     */
    private class AddSightingDialog extends Dialog {
        private Sighting sighting;
        private Text locationText;
        private DateTime dateTimeDate;
        private DateTime dateTimeTime;
        private ComboViewer birdCombo;
        private List<Bird> birds;

        /**
         * Constructs a new AddSightingDialog.
         *
         * @param parent The parent shell
         */
        public AddSightingDialog(Shell parent) {
            super(parent);
        }

        /**
         * Gets the created sighting from the dialog.
         *
         * @return The created sighting, or null if dialog was cancelled
         */
        public Sighting getSighting() {
            return sighting;
        }

        /**
         * Opens the dialog and handles user input.
         * Creates a new sighting if the user confirms the input.
         */
        public void open() {
            Shell parent = getParent();
            Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            shell.setText("Add New Sighting");
            shell.setLayout(new GridLayout(2, false));

            // Bird selection
            new Label(shell, SWT.NONE).setText("Bird:");
            Combo combo = new Combo(shell, SWT.READ_ONLY | SWT.DROP_DOWN);
            combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            birdCombo = new ComboViewer(combo);
            birdCombo.setContentProvider(ArrayContentProvider.getInstance());
            birdCombo.setLabelProvider(new ColumnLabelProvider() {
                @Override
                public String getText(Object element) {
                    return element instanceof Bird ? ((Bird) element).getName() : "";
                }
            });

            // Load birds for combo box
            apiClient.getBirds().thenAccept(loadedBirds -> {
                Display.getDefault().asyncExec(() -> {
                    birds = loadedBirds;
                    birdCombo.setInput(birds);
                    if (!birds.isEmpty()) {
                        combo.select(0);
                    }
                });
            });

            // Location field
            new Label(shell, SWT.NONE).setText("Location:");
            locationText = new Text(shell, SWT.BORDER);
            locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Date field
            new Label(shell, SWT.NONE).setText("Sighting Date:");
            dateTimeDate = new DateTime(shell, SWT.DATE | SWT.BORDER);
            dateTimeDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            
            // Time field
            new Label(shell, SWT.NONE).setText("Sighting Time:");
            dateTimeTime = new DateTime(shell, SWT.TIME | SWT.BORDER);
            dateTimeTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Buttons
            Composite buttonComposite = new Composite(shell, SWT.NONE);
            buttonComposite.setLayout(new GridLayout(2, true));
            buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

            Button okButton = new Button(buttonComposite, SWT.PUSH);
            okButton.setText("OK");
            okButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            okButton.addListener(SWT.Selection, event -> {
                try {
                    IStructuredSelection selection = (IStructuredSelection) birdCombo.getSelection();
                    Bird selectedBird = (Bird) selection.getFirstElement();
                    
                    if (selectedBird == null) {
                        MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        errorBox.setMessage("Please select a bird");
                        errorBox.open();
                        return;
                    }

                    if (locationText.getText().trim().isEmpty()) {
                        MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        errorBox.setMessage("Please enter a location");
                        errorBox.open();
                        return;
                    }

                    LocalDateTime sightingDateTime = LocalDateTime.of(
                        LocalDate.of(dateTimeDate.getYear(), dateTimeDate.getMonth() + 1, dateTimeDate.getDay()),
                        LocalTime.of(dateTimeTime.getHours(), dateTimeTime.getMinutes())
                    );

                    sighting = new Sighting();
                    // Create a new Bird with only the ID to avoid null ID issues
                    Bird birdRef = new Bird();
                    birdRef.setId(selectedBird.getId());
                    sighting.setBird(birdRef);
                    sighting.setLocation(locationText.getText().trim());
                    sighting.setSightingDate(sightingDateTime);
                    sighting.setCreatedAt(LocalDateTime.now());
                    shell.dispose();
                } catch (Exception e) {
                    MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    errorBox.setMessage("Error creating sighting: " + e.getMessage());
                    errorBox.open();
                }
            });

            Button cancelButton = new Button(buttonComposite, SWT.PUSH);
            cancelButton.setText("Cancel");
            cancelButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            cancelButton.addListener(SWT.Selection, event -> shell.dispose());

            shell.pack();
            shell.open();

            Display display = parent.getDisplay();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        }
    }

    /**
     * Creates and configures the table columns for displaying sighting information.
     * Sets up columns for ID, Bird Name, Location, Sighting Date, and Creation Date.
     */
    private void createColumns() {
        // ID column
        TableViewerColumn idColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn idCol = idColumn.getColumn();
        idCol.setText("ID");
        idCol.setWidth(100);
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sighting) {
                    return ((Sighting) element).getId().toString();
                }
                return "";
            }
        });

        // Bird Name column
        TableViewerColumn birdNameColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn birdNameCol = birdNameColumn.getColumn();
        birdNameCol.setText("Bird");
        birdNameCol.setWidth(150);
        birdNameColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sighting) {
                    Bird bird = ((Sighting) element).getBird();
                    return bird != null ? bird.getName() : "";
                }
                return "";
            }
        });

        // Location column
        TableViewerColumn locationColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn locationCol = locationColumn.getColumn();
        locationCol.setText("Location");
        locationCol.setWidth(150);
        locationColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sighting) {
                    return ((Sighting) element).getLocation();
                }
                return "";
            }
        });

        // Sighting Date column
        TableViewerColumn sightingDateColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn sightingDateCol = sightingDateColumn.getColumn();
        sightingDateCol.setText("Sighting Date");
        sightingDateCol.setWidth(150);
        sightingDateColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sighting) {
                    return ((Sighting) element).getSightingDate().format(DATE_FORMATTER);
                }
                return "";
            }
        });

        // Created At column
        TableViewerColumn createdAtColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn createdAtCol = createdAtColumn.getColumn();
        createdAtCol.setText("Created At");
        createdAtCol.setWidth(150);
        createdAtColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sighting) {
                    return ((Sighting) element).getCreatedAt().format(DATE_FORMATTER);
                }
                return "";
            }
        });
    }

    /**
     * Loads sighting data from the API and displays it in the table.
     * Refreshes the table viewer with the latest sighting information.
     */
    protected void loadData() {
        apiClient.getSightings().thenAccept(sightings -> {
            viewer.getTable().getDisplay().asyncExec(() -> {
                try {
                    viewer.setInput(sightings);
                    System.out.println("Successfully loaded " + sightings.size() + " sightings");
                } catch (Exception e) {
                    System.err.println("Error setting sightings data: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }).exceptionally(throwable -> {
            System.err.println("Error loading sightings: " + throwable.getMessage());
            throwable.printStackTrace();
            return null;
        });
    }

    /**
     * Performs a search based on the current bird name and location filter values.
     * Updates the table with filtered results.
     */
    private void performSearch() {
        String birdName = birdNameSearchText.getText().trim();
        String location = locationSearchText.getText().trim();
        String startDate = null;
        String endDate = null;
        
        if (startDateSearchDate != null && startDateSearchTime != null) {
        	LocalDateTime startDateTime = LocalDateTime.of(
                    LocalDate.of(startDateSearchDate.getYear(), startDateSearchDate.getMonth() + 1, startDateSearchDate.getDay()),
                    LocalTime.of(startDateSearchTime.getHours(), startDateSearchTime.getMinutes())
                    );
        	startDate = startDateTime.format(ISO_DATE_FORMATTER);
        }
        
        if (endDateSearchDate != null && endDateSearchTime != null) {
            LocalDateTime endDateTime = LocalDateTime.of(
                    LocalDate.of(endDateSearchDate.getYear(), endDateSearchDate.getMonth() + 1, endDateSearchDate.getDay()),
                    LocalTime.of(endDateSearchTime.getHours(), endDateSearchTime.getMinutes())
                    );

            endDate = endDateTime.format(ISO_DATE_FORMATTER);
        }
        
        if (!birdName.isEmpty() || !location.isEmpty() || !startDate.isEmpty() || !endDate.isEmpty()) {
            apiClient.searchSightings(birdName, location, startDate, endDate).thenAccept(sightings -> {
                viewer.getTable().getDisplay().asyncExec(() -> {
                    try {
                        viewer.setInput(sightings);
                        System.out.println("Successfully loaded " + sightings.size() + " sightings from search");
                    } catch (Exception e) {
                        System.err.println("Error setting sightings search data: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }).exceptionally(throwable -> {
                System.err.println("Error searching sightings: " + throwable.getMessage());
                throwable.printStackTrace();
                return null;
            });
        } else {
            loadData(); // Reload all sightings when both search fields are cleared
        }
    }
    
    /**
     * Clears the search filters for sighting view.
     * Updates the table with un-filtered results.
     */
    private void clearSearch() {
    	birdNameSearchText.setText("");
    	locationSearchText.setText("");
    	startDateSearchDate.setDate(1960, 0, 1);
    	startDateSearchTime.setTime(0, 0, 0);
    	
    	LocalDateTime today = LocalDateTime.now();
    	endDateSearchDate.setDate(today.getYear(), today.getMonthValue()-1, today.getDayOfMonth());
    	endDateSearchTime.setTime(today.getHour(), today.getMinute(), today.getSecond());
    	performSearch();
    }

    /**
     * Disposes of the view's resources.
     * Cleans up any allocated resources.
     */
    public void dispose() {
        if (apiClient != null) {
            apiClient.shutdown();
        }
    }
} 