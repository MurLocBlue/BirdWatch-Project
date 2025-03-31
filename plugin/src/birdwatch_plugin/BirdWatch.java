package birdwatch_plugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.IWorkbenchConfigurer;

/**
 * This class controls all aspects of the application's execution
 */
public class BirdWatch implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new WorkbenchAdvisor() {
				@Override
				public void initialize(IWorkbenchConfigurer configurer) {
					super.initialize(configurer);
					configurer.setSaveAndRestore(true);
				}

				@Override
				public void preStartup() {
					super.preStartup();
				}

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
