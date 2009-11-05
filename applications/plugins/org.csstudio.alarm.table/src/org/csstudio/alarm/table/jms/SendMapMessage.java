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
 */
package org.csstudio.alarm.table.jms;

import java.util.Hashtable;
import java.util.Timer;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.platform.utility.jms.sharedconnection.ISharedConnectionHandle;
import org.csstudio.platform.utility.jms.sharedconnection.SharedJmsConnections;

public class SendMapMessage implements ISendMapMessage {

    Hashtable<String, String> properties = null;
    Session session = null;
    MessageProducer sender = null;
    Destination destination = null;
    MapMessage message = null;
    private CloseJMSConnectionTimerTask _timerTask;
    private Timer _timer = new Timer();
    private ISharedConnectionHandle _senderConnection;

    public void startSender(String topic) throws Exception {
        _senderConnection = SharedJmsConnections.sharedSenderConnection();
        session = _senderConnection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        destination = (Destination) session.createTopic(topic);
        sender = session.createProducer(destination);
        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void stopSender() throws Exception {
        if (sender != null) {
            sender.close();
        }
        if (session != null) {
            session.close();
        }
        if (_senderConnection != null) {
            _senderConnection.release();
        }
        session = null;
        sender = null;
        destination = null;
        message = null;
    }

    public MapMessage getSessionMessageObject(String topic) throws Exception {
        if (session == null) {
            CentralLogger.getInstance().debug(this,
                    "Start Sender, start timer task");
            startSender(topic);
            _timerTask = new CloseJMSConnectionTimerTask(this);
            _timer.schedule(_timerTask, 1000, 1000);
        }
        _timerTask.set_lastDBAcccessInMillisec(System.currentTimeMillis());
        CentralLogger.getInstance().debug(this, "Create mapMessage");
        if (message != null) {
            CentralLogger.getInstance().debug(this,
                    "clear body from previous jms message for reuse");
            message.clearBody();
        } else {
            CentralLogger.getInstance().debug(this,
                    "Jms message is null, create new map message from session");
            message = session.createMapMessage();
        }
        return message;
    }

    public void sendMessage(String topic) throws Exception {
        if (session == null) {
            CentralLogger.getInstance().debug(this,
                    "Start Sender, start timer task");
            startSender(topic);
            _timerTask = new CloseJMSConnectionTimerTask(this);
            _timer.schedule(_timerTask, 1000, 1000);
        }
        _timerTask.set_lastDBAcccessInMillisec(System.currentTimeMillis());
        CentralLogger.getInstance().debug(this, "Send the JMS message");
        sender.send(message);
    }
}
