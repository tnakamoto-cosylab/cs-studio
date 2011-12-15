/*******************************************************************************
 * Copyright (c) 2011 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.scan.ui.scantree;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/** Scan Tree editor perspective
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class Perspective implements IPerspectiveFactory
{
    /** Perspective ID defined in plugin.xml */
    final public static String ID = "org.csstudio.scan.ui.scantree.perspective"; //$NON-NLS-1$
    
    /** Try to switch to the DataBrowser perspective
     *  @throws WorkbenchException on error
     */
    public static void showPerspective() throws WorkbenchException
    {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        workbench.showPerspective(ID, window);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void createInitialLayout(final IPageLayout layout)
    {
        // left | editor      | right
        //      |             |
        //      |             |
        //      +-------------+
        //      | bottom      |
        final String editor = layout.getEditorArea();
        final IFolderLayout left = layout.createFolder("left",
                        IPageLayout.LEFT, 0.20f, editor);
        final IFolderLayout right = layout.createFolder("right",
                        IPageLayout.RIGHT, 0.70f, editor);
        final IFolderLayout bottom = layout.createFolder("bottom",
                        IPageLayout.BOTTOM, 0.75f, editor);

        final IFolderLayout right_top = layout.createFolder("right_top",
                IPageLayout.TOP, 0.33f, "right");
        
        // Stuff for 'left'
        left.addView(IPageLayout.ID_RES_NAV);

        // Stuff for 'bottom'
        bottom.addView("org.csstudio.scan.ui.scanmonitor");
        bottom.addPlaceholder(IPageLayout.ID_PROGRESS_VIEW);

        // Stuff for 'right_top'
        right_top.addView(CommandListView.ID);
        
        // Stuff for 'right'
        right.addView(IPageLayout.ID_PROP_SHEET);

        
        // Populate the "Window/Views..." menu with suggested views
        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        layout.addShowViewShortcut(CommandListView.ID);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IPageLayout.ID_PROGRESS_VIEW);
    }
}
