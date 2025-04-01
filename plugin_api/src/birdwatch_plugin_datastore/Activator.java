package birdwatch_plugin_datastore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * OSGI Bundle Activator for the BirdWatch Plugin Datastore.
 * Manages the lifecycle of the plugin bundle and provides access to the bundle context.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Gets the bundle context for the plugin.
	 *
	 * @return The BundleContext instance
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Called when the bundle is started.
	 * Initializes the bundle context.
	 *
	 * @param bundleContext The bundle context
	 * @throws Exception If an error occurs during startup
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/**
	 * Called when the bundle is stopped.
	 * Cleans up the bundle context.
	 *
	 * @param bundleContext The bundle context
	 * @throws Exception If an error occurs during shutdown
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
