/**
 * 
 */
package org.csstudio.utility.pvmanager.jms.beast;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.epics.pvmanager.jms.beast.BeastDataSource;


import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;



/**
 * Client to be regestered to the extension point.
 * 
 * @author berryma4
 * 
 */
@SuppressWarnings("deprecation")
public class BeastDataSourceFromPreferences {

	private static Logger log = Logger
			.getLogger(BeastDataSourceFromPreferences.class.getName());
	private volatile BeastDataSource client;

	/**
	 * 
	 */
	public BeastDataSourceFromPreferences() {
		final IPreferencesService prefs = Platform.getPreferencesService();
		
		String topic_name = prefs.getString(Activator.PLUGIN_ID,
				PreferenceConstants.topic_name,
				"ALARM_900W_SERVER", null);
		
		String server = prefs.getString(Activator.PLUGIN_ID,
				PreferenceConstants.server,
				"tcp://alarm.hlc.nscl.msu.edu:61616", null);
		
		try {
			this.client = new BeastDataSource(topic_name, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}