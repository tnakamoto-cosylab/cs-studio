package org.csstudio.opibuilder.widgets.editparts;

import org.csstudio.opibuilder.editparts.AbstractPVWidgetEditPart;
import org.csstudio.opibuilder.editparts.ExecutionMode;
import org.csstudio.opibuilder.model.AbstractPVWidgetModel;
import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.opibuilder.properties.IWidgetPropertyChangeHandler;
import org.csstudio.opibuilder.util.OPIFont;
import org.csstudio.opibuilder.widgets.figures.CheckBoxFigure;
import org.csstudio.opibuilder.widgets.figures.AbstractBoolControlFigure.IBoolControlListener;
import org.csstudio.opibuilder.widgets.model.CheckBoxModel;
import org.csstudio.platform.data.IValue;
import org.csstudio.platform.data.ValueUtil;
import org.csstudio.platform.ui.util.CustomMediaFactory;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class CheckBoxEditPart extends AbstractPVWidgetEditPart {

	@Override
	protected IFigure doCreateFigure() {
		CheckBoxFigure figure = new CheckBoxFigure();
		figure.setBit(getWidgetModel().getBit());
		figure.setFont(CustomMediaFactory.getInstance().getFont(
				getWidgetModel().getFont().getFontData()));
		figure.setText(getWidgetModel().getLabel());
		figure.addBoolControlListener(new IBoolControlListener() {
			public void valueChanged(final double newValue) {
				if (getExecutionMode() == ExecutionMode.RUN_MODE)
					setPVValue(AbstractPVWidgetModel.PROP_PVNAME, newValue);
			}
		});		
		markAsControlPV(AbstractPVWidgetModel.PROP_PVNAME);
		figure.setRunMode(getExecutionMode().equals(
				ExecutionMode.RUN_MODE));
		
		return figure;
	}
	
	@Override
	public CheckBoxModel getWidgetModel() {
		return  (CheckBoxModel)getModel();
	}

	@Override
	protected void registerPropertyChangeHandlers() {

		// value
		IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				if(newValue == null)
					return false;
				CheckBoxFigure figure = (CheckBoxFigure) refreshableFigure;
				figure.setValue(ValueUtil.getDouble((IValue)newValue));
				return true;
			}
		};
		setPropertyChangeHandler(AbstractPVWidgetModel.PROP_PVVALUE, handler);
		
		// bit
		handler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				CheckBoxFigure figure = (CheckBoxFigure) refreshableFigure;
				figure.setBit((Integer) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(CheckBoxModel.PROP_BIT, handler);
		
		//label
		handler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				CheckBoxFigure figure = (CheckBoxFigure) refreshableFigure;
				figure.setText((String) newValue);
				Display.getCurrent().timerExec(10, new Runnable() {					
					public void run() {
						if(getWidgetModel().isAutoSize())
							performAutoSize(refreshableFigure);
					}
				});
				return true;
			}
		};
		setPropertyChangeHandler(CheckBoxModel.PROP_LABEL, handler);
		
		// font
		IWidgetPropertyChangeHandler fontHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue, final IFigure refreshableFigure) {
				CheckBoxFigure figure = (CheckBoxFigure) refreshableFigure;
				FontData fontData = ((OPIFont) newValue).getFontData();
				figure.setFont(CustomMediaFactory.getInstance().getFont(fontData));
				return true;
			}
		};
		setPropertyChangeHandler(CheckBoxModel.PROP_FONT, fontHandler);
		
		handler = new IWidgetPropertyChangeHandler(){
			public boolean handleChange(Object oldValue, Object newValue,
					IFigure figure) {				
				if((Boolean)newValue){
					performAutoSize(figure);
					figure.revalidate();
				}
				return true;
			}
		};
		setPropertyChangeHandler(CheckBoxModel.PROP_AUTOSIZE, handler);
		
		handler = new IWidgetPropertyChangeHandler(){
			public boolean handleChange(Object oldValue, Object newValue,
					final IFigure figure) {
				Display.getCurrent().timerExec(10, new Runnable() {					
					public void run() {
						if(getWidgetModel().isAutoSize()){
							performAutoSize(figure);
							figure.revalidate();
						}
					}
				});
				
				return true;
			}
		};
		setPropertyChangeHandler(CheckBoxModel.PROP_FONT, handler);		
		setPropertyChangeHandler(AbstractWidgetModel.PROP_BORDER_STYLE, handler);
		setPropertyChangeHandler(AbstractWidgetModel.PROP_BORDER_WIDTH, handler);
	}
	
	/**
	 * @param figure
	 */
	private void performAutoSize(IFigure figure) {
		getWidgetModel().setSize(((CheckBoxFigure)figure).getPreferredSize());
	}

}
