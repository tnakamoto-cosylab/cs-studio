/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.archive.common.engine.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.csstudio.archive.common.service.channel.ArchiveChannelId;
import org.csstudio.domain.desy.system.IAlarmSystemVariable;

/** An ArchiveChannel that stores each incoming value.
 *  @author Kay Kasemir
 *  @param <V> the base type of the value
 *  @param <T> the css alarm value type with time info
 */
public class MonitoredArchiveChannel<V,
                                     T extends IAlarmSystemVariable<V>> extends AbstractArchiveChannel<V, T> {


    public MonitoredArchiveChannel(@Nonnull final String name,
                                   @Nonnull final ArchiveChannelId channelId,
                                   @Nullable final Class<V> typeClazz) throws EngineModelException {
        super(name, channelId, typeClazz);
    }


    @Override
    public String getMechanism() {
        //return "on change [" + PeriodFormat.formatSeconds(period_estimate) + "]";
        return "MONITOR (on change)";// + PeriodFormat.formatSeconds(period_estimate) + "]";
    }
}