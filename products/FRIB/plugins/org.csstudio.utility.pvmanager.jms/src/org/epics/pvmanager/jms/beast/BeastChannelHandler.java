/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager.jms.beast;

import org.epics.pvmanager.jms.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.transport.TransportListener;
import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.MultiplexedChannelHandler;
import org.epics.pvmanager.ValueCache;

/**
 *
 * @author msekoranja
 */
public class BeastChannelHandler extends MultiplexedChannelHandler<BeastChannelHandler, Message> implements MessageListener {

    private final String topicName;
    private final String pvName;
    private MessageConsumer consumer;
    private MessageProducer producer;
    private volatile Session session;
    private BeastDataSource beastDataSource;

    /**
     *
     * @param chanelName
     */
    public BeastChannelHandler(String topicName, String pvName, BeastDataSource beastDataSource) {
        super(topicName);
        this.beastDataSource = beastDataSource;
        this.topicName = topicName;
        this.pvName = pvName;
    }

    @Override
    public void connect() {
        try {
            final ActiveMQConnection amq_connection = (ActiveMQConnection) beastDataSource.getConnection();
            amq_connection.addTransportListener(transportListener);
            amq_connection.setExceptionListener(exceptionListener);
            session = beastDataSource.getConnection().createSession(/* transacted */false, Session.AUTO_ACKNOWLEDGE);
            this.producer = createProducer(topicName);
            this.consumer = createConsumer(topicName);
            this.consumer.setMessageListener(this);
            processConnection(this);

        } catch (JMSException ex) {
            Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Override
    public boolean isConnected(BeastChannelHandler beastChannelHandler) {
        final ActiveMQConnection amq_connection = ((ActiveMQSession) beastChannelHandler.session).getConnection();
        return amq_connection.getTransport().isConnected();

    }

    @Override
    public void disconnect() {
        try {
            session.close();
        } catch (JMSException ex) {
            Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected BeastTypeAdapter findTypeAdapter(ValueCache<?> cache, BeastChannelHandler beastChannelHandler) {
        return beastDataSource.getTypeSupport().find(cache, beastChannelHandler);
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        try {

            if (newValue instanceof String) {
                MapMessage message = createBeastMapMessage((String) newValue);
                producer.send(message);
            } else {
                throw new RuntimeException("Unsupported type for JMS: " + newValue.getClass());
            }
        } catch (JMSException ex) {
            Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message msg) {
        if (msg instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) msg;
            try {
                if (mapMessage.getString(JMSLogMessage.NAME).equals(pvName) || pvName.isEmpty()) {
                    processMessage(msg);
                }
            } catch (JMSException ex) {
                Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new RuntimeException("Unsupported type for Beast: " + msg.getClass());
        }
    }
    private final TransportListener transportListener = new TransportListener() {
        @Override
        public void onCommand(Object o) {
        }

        @Override
        public void onException(IOException ioe) {
            Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, ioe);
        }

        @Override
        public void transportInterupted() {
            processConnection(BeastChannelHandler.this);
        }

        @Override
        public void transportResumed() {
            processConnection(BeastChannelHandler.this);
        }
    };
    ;

    private final ExceptionListener exceptionListener = new ExceptionListener() {
        @Override
        public void onException(JMSException jmse) {
            Logger.getLogger(BeastChannelHandler.class.getName()).log(Level.SEVERE, null, jmse);
        }
    };

    ;
    /**
     * Create a producer. Derived class can use this to create one or more
     * producers, sending MapMessages to them in the communicator thread.
     *
     * @param topic_name Name of topic for the new producer
     * @return MessageProducer
     * @throws JMSException on error
     */
    protected MessageProducer createProducer(final String topic_name) throws JMSException {
        final Topic topic = session.createTopic(topic_name);
        final MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return producer;
    }

    /**
     * Create a consumer.
     *
     * @param topic_name Name of topic for the new consumer
     * @return MessageProducer
     * @throws JMSException on error
     */
    protected MessageConsumer createConsumer(final String topic_name) throws JMSException {
        final Topic topic = session.createTopic(topic_name);
        final MessageConsumer consumer = session.createConsumer(topic);
        return consumer;
    }

    public String channelName() {
        return topicName;
    }

    private MapMessage createBeastMapMessage(final String text) throws JMSException {
        MapMessage map = session.createMapMessage();
        map.setString(JMSLogMessage.TYPE, JMSAlarmMessage.TYPE_ALARM);
        map.setString(JMSAlarmMessage.CONFIG, this.topicName);
        map.setString(JMSLogMessage.TEXT, text);
        map.setString(JMSLogMessage.APPLICATION_ID, "CSS");
        map.setString(JMSLogMessage.HOST, "host");
        map.setString(JMSLogMessage.USER, "user");
        map.setString(JMSLogMessage.NAME, this.pvName);
        return map;
    }
}
