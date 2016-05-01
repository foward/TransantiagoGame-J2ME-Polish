//#condition polish.usePolishGui

/*
 * Created on 09.06.2006 at 15:41:12.
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
package de.enough.polish.ui.backgrounds;

import javax.microedition.lcdui.Graphics;

import de.enough.polish.ui.Background;
import de.enough.polish.util.DrawUtil;
/**
 * Generates a gradient from the left-color to the right-color.
 * Copyright 2008 Enough Software
 * @author Robert Virkus 
 */
public class GradientHorizontalBackground  extends Background {
	private final int leftColor;
	private final int rightColor;
	private final int stroke;
	private final int start;
	private final int end;
	private final boolean isPercent;

	private int[] gradient;
	private int lastWidth;
	private int startLine;
	private int endLine;

	/**
	 * Creates a new gradient background
	 * 
	 * @param leftColor the color at the left edge of the gradient
	 * @param rightColor the color at the right edge of the gradient
	 * @param stroke the line stroke style
	 */
	public GradientHorizontalBackground(int leftColor, int rightColor,int stroke) {
		this( leftColor, rightColor, stroke, 0, 0, false );
		
	}
	
	/**
	 * Creates a new gradient background
	 * 
	 * @param leftColor the color at the top of the gradient
	 * @param rightColor the color at the bottom of the gradient
	 * @param stroke the line stroke style
	 * @param start the line counted from the top at which the gradient starts, either in pixels or in percent
	 * @param end the line counted from the top at which the gradient ends, either in pixels or in percent
	 * @param isPercent true when the start and end settings should be counted in percent
	 */
	public GradientHorizontalBackground( int leftColor, int rightColor, int stroke, int start, int end, boolean isPercent ) {
		this.leftColor = leftColor;
		this.rightColor = rightColor;
		this.stroke = stroke;
		this.start = start;
		this.end = end;
		this.isPercent = isPercent;
	}
	
	/*
	 * Paints the screen 
	 */
	public void paint(int x, int y, int width, int height, Graphics g) {
		g.setStrokeStyle(this.stroke);
		int startOffset = this.startLine;
		int endOffset = this.endLine;
		if (this.gradient == null || this.lastWidth != width ) {
			int steps = width;
			if (this.start != this.end) {
				steps = this.end - this.start;
				if (this.isPercent) {
					this.startLine = (this.start * width) / 100;
					this.endLine = (this.end * width) / 100;
					steps = this.endLine - this.startLine;
				} else {
					this.startLine = this.start;
					this.endLine = this.end;
				}
				startOffset = this.startLine;
				endOffset = this.endLine;				
			} else {
				this.endLine = width;
				endOffset = width;
			}
			this.gradient = DrawUtil.getGradient( this.leftColor, this.rightColor, steps );
			this.lastWidth = width;
		}
		g.setColor( this.leftColor );
		for (int i = 0; i < width; i++) {
			if (i >= startOffset  && i < endOffset ) {
				int color = this.gradient[i - startOffset ];
				g.setColor( color );
				
			}
			g.drawLine( x, y, x, y + height);
			x++;
		}
		g.setStrokeStyle( Graphics.SOLID );
	}
}
