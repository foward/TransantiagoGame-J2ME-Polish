//#condition polish.usePolishGui

/*
 * Created on Jan 31, 2007 at 5:12:42 AM.
 * 
 * Copyright (c) 2006 Robert Virkus / Enough Software
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
package de.enough.polish.ui.backgrounds;

import javax.microedition.lcdui.Graphics;

import de.enough.polish.ui.Background;
import de.enough.polish.util.DrawUtil;

/**
 * <p>Paints stripes with two colors that can change their color.</p>
 *
 * <p>Copyright Enough Software 2006 - 2008</p>
 * <pre>
 * history
 *        Jan 31, 2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class HorizontalStripesBackground extends Background {
	
	private int firstTopColor;
	private int firstBottomColor;
	private int secondTopColor;
	private int secondBottomColor;
	
	private int[] firstGradient;
	private int[] secondGradient;
	
	
	

	/**
	 * Creates a new background.
	 * 
	 * @param firstTopColor the first color at the start of the background
	 * @param firstBottomColor the first color at the end of the background
	 * @param secondTopColor the second color at the start of the background
	 * @param secondBottomColor the second color at the end of the background
	 */
	public HorizontalStripesBackground(int firstTopColor, int firstBottomColor, int secondTopColor, int secondBottomColor) {
		super();
		this.firstTopColor = firstTopColor;
		this.firstBottomColor = firstBottomColor;
		this.secondTopColor = secondTopColor;
		this.secondBottomColor = secondBottomColor;
	}



	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#paint(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void paint(int x, int y, int width, int height, Graphics g) {
		if (this.firstGradient == null || height>>1 != this.firstGradient.length) {
			this.firstGradient = DrawUtil.getGradient( this.firstTopColor, this.firstBottomColor, height>>1);
			this.secondGradient = DrawUtil.getGradient( this.secondTopColor, this.secondBottomColor, height>>1);
		}
		
		int[] first = this.firstGradient;
		int[] second = this.secondGradient;
		
		int rightX = x + width;
		for (int i = 0; i < (height>>1); i++) {
			int color = first[i];
			//int color = DrawUtil.getGradientColor(this.firstTopColor, this.firstBottomColor, i, height>>1 );
			g.setColor( color );
			g.drawLine( x, y, rightX, y );
			y++;
			color = second[i];
			//color = DrawUtil.getGradientColor(this.secondTopColor, this.secondBottomColor, i, height>>1 );
			g.setColor( color );
			g.drawLine( x, y, rightX, y );
			y++;			
		}
	}

}
