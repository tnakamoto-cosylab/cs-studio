package org.csstudio.startuphelper.extensions;

import java.util.Map;

/**
 * 
 * <code>ShutDownExtPoint</code> is used to provide the code that needs to be
 * executed just before the workbench closes. Such as for instance saving 
 * some data, closing connections or anything else which is not done automatically.
 *
 * @author <a href="mailto:jaka.bobnar@cosylab.com">Jaka Bobnar</a>
 *
 */
public interface ShutDownExtPoint extends CSSStartupExtensionPoint {

	/** The name of this extension point */
	public static final String NAME = "org.csstudio.startup.shutdown";
	
	/**
	 * Is called just before the workbench is closed. The implementation should 
	 * handle all the things that need to be done before the application exits.
	 * 
	 * @param parameters contains additional parameters, which can define
	 * 			some special behaviour during the execution of this method (the keys
	 * 			are parameters names and the values are parameters values)
	 * 
	 */
	public void beforeShutDown(Map<String, Object> parameters);
}
