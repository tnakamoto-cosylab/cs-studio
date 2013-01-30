/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager.jms.beast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.pvmanager.util.NumberFormats;
import org.epics.util.time.Timestamp;

public class AlarmTimeDisplayExtractor implements Alarm, Time, Display {

    protected final AlarmSeverity alarmSeverity;
    protected final String alarmStatus;
    protected final Timestamp timeStamp;
    protected final Integer timeUserTag;
    protected final boolean isTimeValid;
    protected final Double lowerDisplayLimit;
    protected final Double lowerCtrlLimit;
    protected final Double lowerAlarmLimit;
    protected final Double lowerWarningLimit;
    protected final String units;
    protected final NumberFormat format;
    protected final Double upperWarningLimit;
    protected final Double upperAlarmLimit;
    protected final Double upperCtrlLimit;
    protected final Double upperDisplayLimit;

    /**
     *
     * @param message
     * @param disconnected
     */
    public AlarmTimeDisplayExtractor(MapMessage message, boolean disconnected) throws JMSException, ParseException {

        // alarm_t
        if (!message.getString(JMSLogMessage.SEVERITY).isEmpty()) {
            this.alarmSeverity = alarmSeverityMap.get(message.getString(JMSLogMessage.SEVERITY));
        } else {
            this.alarmSeverity = null;
        }

        if (!message.getString(JMSAlarmMessage.STATUS).isEmpty()) {
            this.alarmStatus = message.getString(JMSAlarmMessage.STATUS);
        } else {
            this.alarmStatus = null;
        }
        // timeStamp_t 

        if (message.getString(JMSAlarmMessage.EVENTTIME) != null) {
            this.timeStamp = Timestamp.of(new SimpleDateFormat(JMSLogMessage.DATE_FORMAT, Locale.ENGLISH).parse(message.getString(JMSAlarmMessage.EVENTTIME)));
            this.isTimeValid = true;
        } else {
            this.timeStamp = null;
            this.isTimeValid = false;
        }
        this.timeUserTag = null;

        // display_t    
        this.lowerDisplayLimit = null;
        this.upperDisplayLimit = null;
        // control_t
        this.lowerCtrlLimit = null;
        this.upperCtrlLimit = null;
        // valueAlarm_t
        this.lowerAlarmLimit = null;
        this.upperAlarmLimit = null;
        this.lowerWarningLimit = null;
        this.upperWarningLimit = null;
        this.units = null;
        this.format = null;
    }
    protected static final Map<String, AlarmSeverity> alarmSeverityMap;

    static {
        Map<String, AlarmSeverity> map = new HashMap<String, AlarmSeverity>();
        map.put("NONE", AlarmSeverity.NONE);
        map.put("MINOR", AlarmSeverity.MINOR);
        map.put("MAJOR", AlarmSeverity.MAJOR);
        map.put("INVALID", AlarmSeverity.INVALID);
        map.put("UNDEFINED", AlarmSeverity.UNDEFINED);
        alarmSeverityMap = Collections.unmodifiableMap(map);
    }

    ;

    @Override
    public AlarmSeverity getAlarmSeverity() {
        return alarmSeverity;
    }

    @Override
    public String getAlarmName() {
        return alarmStatus.toString();
    }

    @Override
    public Timestamp getTimestamp() {
        return timeStamp;
    }

    @Override
    public Integer getTimeUserTag() {
        return timeUserTag;
    }

    @Override
    public boolean isTimeValid() {
        return isTimeValid;
    }

    @Override
    public Double getLowerDisplayLimit() {
        return lowerDisplayLimit;
    }

    @Override
    public Double getLowerCtrlLimit() {
        return lowerCtrlLimit;
    }

    @Override
    public Double getLowerAlarmLimit() {
        return lowerAlarmLimit;
    }

    @Override
    public Double getLowerWarningLimit() {
        return lowerWarningLimit;
    }

    @Override
    public String getUnits() {
        return units;
    }

    @Override
    public NumberFormat getFormat() {
        return format;
    }

    @Override
    public Double getUpperWarningLimit() {
        return upperWarningLimit;
    }

    @Override
    public Double getUpperAlarmLimit() {
        return upperAlarmLimit;
    }

    @Override
    public Double getUpperCtrlLimit() {
        return upperCtrlLimit;
    }

    @Override
    public Double getUpperDisplayLimit() {
        return upperDisplayLimit;
    }
}
