package birdwatch_plugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.IWorkbenchConfigurer;

/**
 * Main application class for the BirdWatch Eclipse plugin.
 * Controls the application's lifecycle and initializes the workbench.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class BirdWatch implements IApplication {

	/**
	 * Starts the BirdWatch application.
	 * Initializes the workbench and sets up the initial perspective.
	 *
	 * @param context The application context
	 * @return The exit code (EXIT_OK for successful execution, EXIT_RESTART if restart is requested)
	 * @throws Exception If an error occurs during startup
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new WorkbenchAdvisor() {
				/**
				 * Initializes the workbench configuration.
				 * Enables save and restore functionality.
				 *
				 * @param configurer The workbench configurer
				 */
				@Override
				public void initialize(IWorkbenchConfigurer configurer) {
					super.initialize(configurer);
					configurer.setSaveAndRestore(true);
				}

				/**
				 * Performs pre-startup tasks.
				 */
				@Override
				public void preStartup() {
					super.preStartup();
				}

				/**
				 * Performs post-startup tasks.
				 * Shows the BirdWatch view in the active workbench window.
				 */
				@Override
				public void postStartup() {
					super.postStartup();
					// Show the BirdWatch view
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage()
							.showView("birdwatch_plugin.views.BirdWatchView");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				/**
				 * Gets the ID of the initial perspective to show.
				 *
				 * @return The ID of the BirdWatch perspective
				 */
				@Override
				public String getInitialWindowPerspectiveId() {
					return "birdwatch_plugin.perspective";
				}
			});
			return returnCode == PlatformUI.RETURN_RESTART ? IApplication.EXIT_RESTART : IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/**
	 * Stops the BirdWatch application.
	 * Closes the workbench if it is running.
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(() -> {
			if (!display.isDisposed()) {
				workbench.close();
			}
		});
	}
}
