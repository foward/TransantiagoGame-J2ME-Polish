//#condition polish.midp || polish.usePolishGui

/*
 * Created on 03-Jun-2005 at 21:33:55.
 * 
 * Copyright (c) 2005 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */
package de.enough.polish.ui;

import javax.microedition.lcdui.Graphics;

/**
 * <p>Makes the canvas methods available to the J2ME Polish GUI.</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2008</p>
 * <pre>
 * history
 *        03-Jun-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public interface AccessibleCanvas {
	
	public void keyPressed( int keyCode );
	
	public void keyRepeated( int keyCode );

	public void keyReleased( int keyCode );

	//#if polish.hasPointerEvents
	//# public void pointerPressed( int x, int y );
	//#endif
	
	//#if polish.hasPointerEvents
	//# /* (non-Javadoc)
	 //# * @see javax.microedition.lcdui.Canvas#pointerPressed(int,int)
	 //# */
	//# public void pointerReleased(int x, int y);
	//#endif

	//#if polish.hasPointerEvents
	//# /* (non-Javadoc)
	 //# * @see javax.microedition.lcdui.Canvas#pointerDragged(int,int)
	 //# */
	//# public void pointerDragged(int x, int y);
	//#endif
	
	public void showNotify();
	
	public void hideNotify();
	
	//#if polish.midp2 && !polish.Bugs.needsNokiaUiForSystemAlerts 
	public void sizeChanged( int width, int height );
	//#endif
	
	public void paint( Graphics g );

}
