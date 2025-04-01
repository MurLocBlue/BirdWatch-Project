package birdwatch_plugin_datastore;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * Main application class for the BirdWatch Plugin Datastore.
 * Controls all aspects of the application's execution and lifecycle.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class Application implements IApplication {

	/**
	 * Starts the application.
	 * Initializes the BirdWatch plugin and performs any necessary setup.
	 *
	 * @param context The application context
	 * @return The exit code (EXIT_OK for successful execution)
	 * @throws Exception If an error occurs during startup
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Hello Birds!");
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// do nothing
	}
}
