/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
class XmlTimeStampAdapter extends XmlAdapter<BigDecimal, Timestamp> {

    @Override
    public Timestamp unmarshal(BigDecimal v) throws Exception {
        return Timestamp.of(v.longValue(), v.remainder(new BigDecimal(1)).scaleByPowerOfTen(9).intValue());
    }

    @Override
    public BigDecimal marshal(Timestamp v) throws Exception {
        return new BigDecimal(v.getNanoSec()).scaleByPowerOfTen(-9).add(new BigDecimal(v.getSec()));
    }

}
