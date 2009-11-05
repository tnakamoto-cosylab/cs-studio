package org.csstudio.alarm.table.ui;

import java.util.ArrayList;

import org.csstudio.alarm.dbaccess.archivedb.Filter;
import org.csstudio.alarm.dbaccess.archivedb.FilterItem;
import org.csstudio.apputil.ui.time.StartEndDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class FilterSettingDialog extends StartEndDialog {

	private final int FILTER_SETTING_SIZE = 8;
	private final Filter _filter;
	private ArrayList<String> _settingHistory;
	private final String[][] _messageProperties;
	private Combo[] _patternCombo;
	private Combo[] _propertyCombo;
	private Combo[] _conjunctionCombo;

	public FilterSettingDialog(Shell parentShell, Filter filter,
			ArrayList<String> settingHistory, String[][] messageProperties,
			String timeFrom, String timeTo) {
		super(parentShell, timeFrom, timeTo);
		_filter = filter;
		_settingHistory = settingHistory;
		_messageProperties = messageProperties;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite area = (Composite) super.createDialogArea(parent);

		final Group box = new Group(area, 0);
		box.setText("Filter Conditions");
		box.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		box.setLayout(layout);
		GridData gd;
		Label explanation = new Label(box, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 4;
		explanation.setLayoutData(gridData);
		explanation
				.setText("Die Suche ignoriert Gross-/Kleinschreibung. Als Wildcard" +
						" koennen '*' fuer beliebig viele Zeichen\n und '?' f�r genau " +
						"ein Zeichen verwendet werden.");
		// Property: ____property____ Value: ___value___
		Label l;
		_patternCombo = new Combo[FILTER_SETTING_SIZE];
		_propertyCombo = new Combo[FILTER_SETTING_SIZE];
		_conjunctionCombo = new Combo[FILTER_SETTING_SIZE];
		for (int i = 0; i < _propertyCombo.length; ++i) {
			if (i > 0) { // new row
				_conjunctionCombo[i] = new Combo(box, SWT.DROP_DOWN
						| SWT.READ_ONLY);
				_conjunctionCombo[i].add("AND");
				_conjunctionCombo[i].add("OR");
				_conjunctionCombo[i].select(0);
				gd = new GridData();
				gd.horizontalSpan = 2;
				gd.grabExcessHorizontalSpace = true;
				gd.horizontalAlignment = SWT.RIGHT;
				_conjunctionCombo[i].setLayoutData(gd);
				gd = new GridData();
				Label dummy = new Label(box, SWT.NONE);
				dummy.setLayoutData(gd);
				dummy = new Label(box, SWT.NONE);
				dummy.setLayoutData(gd);
			}
			l = new Label(box, 0);
			l.setText("Property");
			l.setLayoutData(new GridData());

			_propertyCombo[i] = new Combo(box, SWT.DROP_DOWN | SWT.READ_ONLY);
			_propertyCombo[i].setToolTipText("List of message properties");
			_propertyCombo[i].setVisibleItemCount(25);
			for (String[] prop : _messageProperties)
				_propertyCombo[i].add(prop[1]);
			_propertyCombo[i].setLayoutData(new GridData(SWT.FILL, 0, true,
					false));
			_propertyCombo[i].select(0);

			l = new Label(box, 0);
			l.setText("Pattern");
			l.setLayoutData(new GridData());

			_patternCombo[i] = new Combo(box, SWT.DROP_DOWN);
			_patternCombo[i].add("");
			for (String pattern : _settingHistory)
				_patternCombo[i].add(pattern);
			_patternCombo[i].setVisibleItemCount(20);
			_patternCombo[i].setToolTipText("Pattern to search for");
			_patternCombo[i].setLayoutData(new GridData(SWT.FILL, 0, true,
					false));
		}

		setCurrentSettings();

		return parent;

	}

	private void setCurrentSettings() {
		if (_filter == null) {
			return;
		} else if (_filter.getFilterItems() == null) {
			return;
		}
		ArrayList<FilterItem> filterItems = _filter.getFilterItems();
		String[] propertyItems = _propertyCombo[0].getItems();
		String[] patternItems = _patternCombo[0].getItems();
		int j = 0;
		for (FilterItem filterItem : filterItems) {
			String property = filterItem.getProperty();
			for (int i = 0; i < propertyItems.length; i++) {
				if (_propertyCombo[j].getItem(i).equalsIgnoreCase(property)) {
					_propertyCombo[j].select(i);
					break;
				}
			}
			String pattern = filterItem.getValue();
			boolean patternInHistory = false;
			for (int i = 0; i < patternItems.length; i++) {
				if (_patternCombo[j].getItem(i).equalsIgnoreCase(pattern)) {
					_patternCombo[j].select(i);
					patternInHistory = true;
					break;
				}
			}
			if (!patternInHistory) {
				_patternCombo[j].add(pattern);
				_patternCombo[j]
						.select((_patternCombo[j].getItems().length - 1));
			}
			if (j < (FILTER_SETTING_SIZE - 1)) {
				if (_conjunctionCombo[j + 1].getItem(1).equalsIgnoreCase(
						filterItem.getRelation())) {
					_conjunctionCombo[j + 1].select(1);
				} else {
					_conjunctionCombo[j + 1].select(0);
				}
			}
			j++;
		}
	}

	/** Memorize entered/selected data */
	@Override
	protected void okPressed() {
		if (_filter != null) {
			_filter.clearFilter();
			for (int i = 0; i < FILTER_SETTING_SIZE; i++) {
				if (_patternCombo[i].getText().length() > 0) {
					String relation;
					if (i < (FILTER_SETTING_SIZE - 1)) {
						relation = _conjunctionCombo[i + 1]
								.getItem((_conjunctionCombo[i + 1]
										.getSelectionIndex()));
					} else {
						relation = "END";
					}
					FilterItem filterItem = new FilterItem(_propertyCombo[i]
							.getItem((_propertyCombo[i].getSelectionIndex())),
							_patternCombo[i].getText(), relation);
					_filter.setFilterItem(filterItem);
				}
			}
		}
		if (_settingHistory != null) {
			for (Combo element : _patternCombo) {
				String comboText = element.getText();
				if (comboText.length() > 0) {
					boolean inHistory = false;
					for (String historyElement : _settingHistory) {
						if (historyElement.equals(comboText)) {
							inHistory = true;
							break;
						}
					}
					if (!inHistory) {
						_settingHistory.add(0, comboText);
					}
				}
			}
		}
		if (_settingHistory.size() > 20) {
			_settingHistory = (ArrayList<String>) _settingHistory
					.subList(0, 19);
		}
		super.okPressed();
	}
}
