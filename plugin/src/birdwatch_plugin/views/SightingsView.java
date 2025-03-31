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
import birdwatch_plugin.model.Sighting;
import java.time.format.DateTimeFormatter;
import birdwatch_plugin.model.Bird;

public class SightingsView {
    private TableViewer viewer;
    private Table table;
    private ApiClient apiClient;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SightingsView() {
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

    private void loadData() {
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

    public void dispose() {
        if (apiClient != null) {
            apiClient.shutdown();
        }
    }
} 