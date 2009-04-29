package org.csstudio.startuphelper.extensions;

import java.util.Map;

import org.eclipse.equinox.app.IApplicationContext;

/**
 * 
 * <code>LocaleSettingsExtPoint</code> defines how and which locale settings
 * (if any) will be applied to certain application parts.
 *
 * @author <a href="mailto:jaka.bobnar@cosylab.com">Jaka Bobnar</a>
 *
 */
public interface LocaleSettingsExtPoint extends CSSStartupExtensionPoint {

	/** The name of this extension point */
	public static final String NAME = "org.csstudio.startup.locale";
	
	/**
	 * Applies the locale settings. The locale settings can be gathered from any
	 * location specified by the implementation and can be set to whatever part
	 * of the application.
	 * 
	 * @param context the application context which triggered this call and for
	 * 			which the settings are being applied
	 * @param parameters contains additional parameters, which can define
	 * 			some special behaviour during the execution of this method (the keys
	 * 			are parameters names and the values are parameters values)
	 * 
	 * @return the exit code if something happened which requires to exit or restart 
	 * 			application or null if everything is alright
	 * 
	 * @throws Exception if an error occurred during the operation
	 */
	public Object applyLocaleSettings(IApplicationContext context, Map<String, Object> parameters) throws Exception;
}
