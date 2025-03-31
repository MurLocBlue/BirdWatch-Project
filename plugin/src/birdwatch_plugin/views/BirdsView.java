package birdwatch_plugin.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import birdwatch_plugin.api.ApiClient;
import java.time.format.DateTimeFormatter;
import birdwatch_plugin.model.Bird;

public class BirdsView {
    private TableViewer viewer;
    private Table table;
    private ApiClient apiClient;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BirdsView() {
        this.apiClient = new ApiClient();
    }

    public Composite createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());

        viewer = new TableViewer(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
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
} 