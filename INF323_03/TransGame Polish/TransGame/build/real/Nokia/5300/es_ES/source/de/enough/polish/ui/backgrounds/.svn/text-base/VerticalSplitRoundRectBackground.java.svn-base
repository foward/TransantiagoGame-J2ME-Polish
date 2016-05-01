//#condition polish.usePolishGui
/*
 * Created on Apr 23, 2008 at 11:52:42 PM.
 * 
 * Copyright (c) 2007 Robert Virkus / Enough Software
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

/**
 * <p>Creates a rectangular background with two colors and rounded corners.</p>
 *
 * <p>Copyright Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class VerticalSplitRoundRectBackground extends Background
{

	private final int topColor;
	private final int bottomColor;
	private final int splitPos;
	private final boolean isPercent;
	private final int arcWidth;
	private final int arcHeight;

	/**
	 * Creates a new background
	 * @param topColor the top color
	 * @param bottomColor the bottom color
	 * @param splitPos the split position either in percent (0 - 100) or in pixels, negative values are interpreted as percent
	 * @param arcWidth the horizontal diameter of the arc at the four corners
	 * @param arcHeight the vertical diameter of the arc at the four corners
	 */
	public VerticalSplitRoundRectBackground( int topColor, int bottomColor, int splitPos, int arcWidth, int arcHeight ) 
	{
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.splitPos = splitPos < 0 ? -splitPos : splitPos;
		this.isPercent = splitPos < 0;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#paint(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void paint(int x, int y, int width, int height, Graphics g)
	{
		int split = this.splitPos;
		if (this.isPercent) {
			split = (height * split) / 100;
		}
		g.setColor( this.topColor );
		g.fillRoundRect(x, y, width, split + 1, this.arcWidth, this.arcHeight );
		g.fillRect( x, y + split - this.arcHeight, width, this.arcHeight );
		g.setColor( this.bottomColor );
		g.fillRoundRect( x, y + split, width, height - split, this.arcWidth, this.arcHeight );
		g.fillRect( x, y + split, width, this.arcHeight );
	}

}
