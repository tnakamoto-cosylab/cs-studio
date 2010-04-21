/*
		* Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
		* Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
		*
		* THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
		* WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT
		NOT LIMITED
		* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE
		AND
		* NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
		BE LIABLE
		* FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
		CONTRACT,
		* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
		SOFTWARE OR
		* THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE
		DEFECTIVE
		* IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING,
		REPAIR OR
		* CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART
		OF THIS LICENSE.
		* NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS
		DISCLAIMER.
		* DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
		ENHANCEMENTS,
		* OR MODIFICATIONS.
		* THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION,
		MODIFICATION,
		* USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE
		DISTRIBUTION OF THIS
		* PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU
		MAY FIND A COPY
		* AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
		*/
package org.csstudio.sds.behavior.desy;

import org.csstudio.sds.components.model.TankModel;
import org.epics.css.dal.simple.AnyData;
import org.epics.css.dal.simple.MetaData;

/**
 * TODO (hrickens) :
 *
 * @author hrickens
 * @author $Author$
 * @version $Revision$
 * @since 21.04.2010
 */
public class TankConnectionBehavior extends AbstractDesyConnectionBehavior<TankModel> {

    /**
     * Constructor.
     */
    public TankConnectionBehavior() {
        addInvisiblePropertyId(TankModel.PROP_MIN);
        addInvisiblePropertyId(TankModel.PROP_MAX);
        addInvisiblePropertyId(TankModel.PROP_HIHI_LEVEL);
        addInvisiblePropertyId(TankModel.PROP_HI_LEVEL);
        addInvisiblePropertyId(TankModel.PROP_LOLO_LEVEL);
        addInvisiblePropertyId(TankModel.PROP_LO_LEVEL);
        addInvisiblePropertyId(TankModel.PROP_FILL_COLOR);
        addInvisiblePropertyId(TankModel.PROP_FILLBACKGROUND_COLOR);
        addInvisiblePropertyId(TankModel.PROP_TRANSPARENT);
        addInvisiblePropertyId(TankModel.PROP_ACTIONDATA);
        addInvisiblePropertyId(TankModel.PROP_BORDER_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doProcessValueChange(final TankModel model, final AnyData anyData) {
        super.doProcessValueChange(model, anyData);

    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doProcessMetaDataChange(final TankModel widget, final MetaData metaData) {
        // TODO Auto-generated method stub

    }

}
