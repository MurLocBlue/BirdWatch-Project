package birdwatch_plugin.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import birdwatch_plugin.api.ApiClient;
import java.time.format.DateTimeFormatter;
import birdwatch_plugin.model.Bird;
import java.time.LocalDateTime;

public class BirdsView {
    private TableViewer viewer;
    private Table table;
    private ApiClient apiClient;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Text nameSearchText;
    private Text colorSearchText;

    public BirdsView() {
        this.apiClient = new ApiClient();
    }

    public Composite createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        // Create search container
        Composite searchContainer = new Composite(container, SWT.NONE);
        searchContainer.setLayout(new GridLayout(4, false));
        searchContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        // Name search label
        Label nameSearchLabel = new Label(searchContainer, SWT.NONE);
        nameSearchLabel.setText("Name:");
        nameSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        // Name search text field
        nameSearchText = new Text(searchContainer, SWT.BORDER);
        nameSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Color search label
        Label colorSearchLabel = new Label(searchContainer, SWT.NONE);
        colorSearchLabel.setText("Color:");
        colorSearchLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        // Color search text field
        colorSearchText = new Text(searchContainer, SWT.BORDER);
        colorSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Add search listeners
        nameSearchText.addListener(SWT.Modify, event -> performSearch());
        colorSearchText.addListener(SWT.Modify, event -> performSearch());

        // Create button container
        Composite buttonContainer = new Composite(container, SWT.NONE);
        buttonContainer.setLayout(new GridLayout(1, false));
        buttonContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        // Add Bird button
        Button addButton = new Button(buttonContainer, SWT.PUSH);
        addButton.setText("Add New Bird");
        addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        addButton.addListener(SWT.Selection, event -> {
            AddBirdDialog dialog = new AddBirdDialog(Display.getCurrent().getActiveShell());
            dialog.open();
            if (dialog.getBird() != null) {
                addButton.setEnabled(false); // Disable button while processing
                apiClient.createBird(dialog.getBird())
                    .thenAccept(bird -> {
                        Display.getDefault().asyncExec(() -> {
                            try {
                                loadData(); // Refresh the table
                                MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
                                messageBox.setMessage("Bird added successfully!");
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
                            errorBox.setMessage("Error adding bird: " + throwable.getMessage());
                            errorBox.open();
                            addButton.setEnabled(true); // Re-enable button
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

        return container;
    }

    private void createColumns() {
        // ID column
        TableViewerColumn idColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn idCol = idColumn.getColumn();
        idCol.setText("ID");
        idCol.setWidth(100);
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Bird) {
                    return ((Bird) element).getId().toString();
                }
                return "";
            }
        });

        // Name column
        TableViewerColumn nameColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn nameCol = nameColumn.getColumn();
        nameCol.setText("Name");
        nameCol.setWidth(150);
        nameColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Bird) {
                    return ((Bird) element).getName();
                }
                return "";
            }
        });

        // Color column
        TableViewerColumn colorColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn colorCol = colorColumn.getColumn();
        colorCol.setText("Color");
        colorCol.setWidth(100);
        colorColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Bird) {
                    return ((Bird) element).getColor();
                }
                return "";
            }
        });

        // Weight column
        TableViewerColumn weightColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn weightCol = weightColumn.getColumn();
        weightCol.setText("Weight (kg)");
        weightCol.setWidth(100);
        weightColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Bird) {
                    return String.format("%.2f", ((Bird) element).getWeight());
                }
                return "";
            }
        });

        // Height column
        TableViewerColumn heightColumn = new TableViewerColumn(viewer, SWT.NONE);
        TableColumn heightCol = heightColumn.getColumn();
        heightCol.setText("Height (cm)");
        heightCol.setWidth(100);
        heightColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Bird) {
                    return String.format("%.2f", ((Bird) element).getHeight());
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
                if (element instanceof Bird) {
                    return ((Bird) element).getCreatedAt().format(DATE_FORMATTER);
                }
                return "";
            }
        });
    }

    private void loadData() {
        apiClient.getBirds().thenAccept(birds -> {
            viewer.getTable().getDisplay().asyncExec(() -> {
                try {
                    viewer.setInput(birds);
                    System.out.println("Successfully loaded " + birds.size() + " birds");
                } catch (Exception e) {
                    System.err.println("Error setting birds data: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }).exceptionally(throwable -> {
            System.err.println("Error loading birds: " + throwable.getMessage());
            throwable.printStackTrace();
            return null;
        });
    }

    public void dispose() {
        if (apiClient != null) {
            apiClient.shutdown();
        }
    }

    private void performSearch() {
        String name = nameSearchText.getText().trim();
        String color = colorSearchText.getText().trim();
        
        if (!name.isEmpty() || !color.isEmpty()) {
            apiClient.searchBirds(name, color).thenAccept(birds -> {
                viewer.getTable().getDisplay().asyncExec(() -> {
                    try {
                        viewer.setInput(birds);
                        System.out.println("Successfully loaded " + birds.size() + " birds from search");
                    } catch (Exception e) {
                        System.err.println("Error setting birds search data: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }).exceptionally(throwable -> {
                System.err.println("Error searching birds: " + throwable.getMessage());
                throwable.printStackTrace();
                return null;
            });
        } else {
            loadData(); // Reload all birds when both search fields are cleared
        }
    }

    private class AddBirdDialog extends Dialog {
        private Bird bird;
        private Text nameText;
        private Text colorText;
        private Text weightText;
        private Text heightText;

        public AddBirdDialog(Shell parent) {
            super(parent);
        }

        public Bird getBird() {
            return bird;
        }

        public void open() {
            Shell parent = getParent();
            Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            shell.setText("Add New Bird");
            shell.setLayout(new GridLayout(2, false));

            // Name field
            new Label(shell, SWT.NONE).setText("Name:");
            nameText = new Text(shell, SWT.BORDER);
            nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Color field
            new Label(shell, SWT.NONE).setText("Color:");
            colorText = new Text(shell, SWT.BORDER);
            colorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Weight field
            new Label(shell, SWT.NONE).setText("Weight (kg):");
            weightText = new Text(shell, SWT.BORDER);
            weightText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Height field
            new Label(shell, SWT.NONE).setText("Height (cm):");
            heightText = new Text(shell, SWT.BORDER);
            heightText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // Buttons
            Composite buttonComposite = new Composite(shell, SWT.NONE);
            buttonComposite.setLayout(new GridLayout(2, true));
            buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

            Button okButton = new Button(buttonComposite, SWT.PUSH);
            okButton.setText("OK");
            okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
            okButton.addListener(SWT.Selection, event -> {
                try {
                    bird = new Bird();
                    bird.setName(nameText.getText());
                    bird.setColor(colorText.getText());
                    bird.setWeight(Double.parseDouble(weightText.getText()));
                    bird.setHeight(Double.parseDouble(heightText.getText()));
                    bird.setCreatedAt(LocalDateTime.now());
                    shell.dispose();
                } catch (NumberFormatException e) {
                    // Handle invalid number input
                }
            });

            Button cancelButton = new Button(buttonComposite, SWT.PUSH);
            cancelButton.setText("Cancel");
            cancelButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
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
} 