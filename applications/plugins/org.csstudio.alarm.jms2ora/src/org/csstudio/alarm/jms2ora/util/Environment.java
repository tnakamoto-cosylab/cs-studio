
/*
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 */

package org.csstudio.alarm.jms2ora.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * @author Markus Moeller
 *
 */
public final class Environment {

    private static Environment INSTANCE;

    private String hostName;
    private String userName;

    private Environment() {

        final Map <String, String> env = System.getenv();

        if(env.containsKey("COMPUTERNAME")) {
            hostName = env.get("COMPUTERNAME");
        } else {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch(final UnknownHostException uhe) {
                hostName = "unknown_host";
            }
        }

        if(env.containsKey("USERNAME")) {
            userName = env.get("USERNAME");
        } else {
            userName = System.getProperty("user.name");
        }
    }

    @Nonnull
    public static synchronized Environment getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Environment();
        }
        return INSTANCE;
    }

    @Nonnull
    public String getHostName() {
        return hostName;
    }

    @Nonnull
    public String getUserName() {
        return userName;
    }
}
