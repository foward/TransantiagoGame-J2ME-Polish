//#condition polish.usePolishGui

/*
 * Created on 2008-03-02 at 4:37:12.
 * 
 * Copyright (c) 2008 Robert Virkus / Enough Software
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
 * Generates a radial gradient from the inner-color to the outer-color.
 * @author Robert Virkus 
 */
public class GradientRadialBackground  extends Background {
	private final int innerColor;
	private final int outerColor;
	private final int start;
	private final int end;
	private final int centerX;
	private final int centerY;


	/**
	 * Creates a new radial gradient background
	 * 
	 * @param innerColor the color at the top of the gradient
	 * @param outerColor the color at the bottom of the gradient
	 * @param start the line counted from the top at which the gradient starts, either in pixels or in percent
	 * @param end the line counted from the top at which the gradient ends, either in pixels or in percent
	 */
	public GradientRadialBackground( int innerColor, int outerColor, int start, int end ) {
		this( innerColor, outerColor, start, end, 0, 0 );
	}
	
	/**
	 * Creates a new radial gradient background
	 * 
	 * @param innerColor the color at the top of the gradient
	 * @param outerColor the color at the bottom of the gradient
	 * @param start the line counted from the top at which the gradient starts, either in pixels or in percent
	 * @param end the line counted from the top at which the gradient ends, either in pixels or in percent
	 * @param centerX the horizontal center in percent. 0 is the center, -100 is the very left, +100 the very right
	 * @param centerY the vertical center in percent. 0 is the center, -100 is the very top, +100 the very bottom
	 */
	public GradientRadialBackground( int innerColor, int outerColor, int start, int end, int centerX, int centerY ) {
		this.innerColor = innerColor;
		this.outerColor = outerColor;
		this.start = start;
		this.end = end;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	/*
	 * Paints the screen 
	 */
	public void paint(int x, int y, int width, int height, Graphics g) {
		int steps = (Math.max( width, height) >> 1) - 1;
		int startOffset = this.start;
		int endOffset = this.end;
		if (startOffset != endOffset) {
			startOffset = (startOffset * steps) / 100;
			endOffset = (endOffset * steps) / 100;
		} else {
			endOffset = steps;
		}
		int targetX = (width >> 1) + (((width>>1)*this.centerX) / 100) - startOffset;
		int targetY = (height >> 1) + (((width>>1)*this.centerY) / 100) - startOffset;
		int originalX = x;
		int originalY = y;
		g.setColor( this.innerColor );
		for (int i = 0; i < steps; i++) {
			if (i >= startOffset  && i < endOffset ) {
				int color = DrawUtil.getGradientColor(this.outerColor, this.innerColor, i - startOffset, steps );
				g.setColor( color );
				
			}
			g.fillArc( x, y, width, height, 0, 360 );
			x = originalX + (targetX * i)/steps;
			y = originalY + (targetY * i)/steps;
			if (width > startOffset) {
				width -= 2;
			}
			if (height > startOffset) {
				height -= 2;
			}
		}
	}
}
