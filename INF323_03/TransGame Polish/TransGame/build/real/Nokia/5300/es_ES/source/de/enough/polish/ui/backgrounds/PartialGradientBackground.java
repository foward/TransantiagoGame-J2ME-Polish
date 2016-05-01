//#condition polish.usePolishGui

/*
 * Created on Dec 28, 2007 at 3:19:28 PM.
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
import de.enough.polish.util.DrawUtil;

/**
 * <p>Provides a partial gradient - this background will be usually used within a <code>combined</code> background.</p>
 * <pre>
 * backgrounds {
 * 	partialTop {
 * 		type: partial-gradient;
 * 		start: 10%;
 * 		end: 20%;
 * 		top-color: white;
 * 		bottom-color: blue;
 *  }
 * 	partialBottom {
 * 		type: partial-gradient;
 * 		start: 20%;
 * 		end: 40%;
 * 		top-color: blue;
 * 		bottom-color: white;
 *  }
 *  formFg {
 *  	type: combined;
 *  	foreground: partialTop;
 *  	background: partialBottom;
 *  }
 *  formBg {
 *  	color: white;
 *  }
 * }
 * .myForm {
 * 	background {
 *    type: combined;
 *    foreground: formFg;
 *    background: formBg;
 *  }
 * }
 * </pre>
 *
 * <p>Copyright Enough Software 2007 - 2008</p>
 * <pre>
 * history
 *        Dec 28, 2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class PartialGradientBackground extends Background
{

	private int topColor;
	private int bottomColor;
	private int stroke;
	private int start;
	private int end;
	private int startLine;
	private int endLine;
	private int[] gradient;
	private int lastHeight;
	/**
	 * Creates a new gradient background
	 * 
	 * @param topColor the color at the top of the gradient
	 * @param bottomColor the color at the bottom of the gradient
	 * @param stroke the line stroke style
	 * @param start the line counted from the top at which the gradient starts in percent
	 * @param end the line counted from the top at which the gradient ends in percent
	 */
	public PartialGradientBackground( int topColor, int bottomColor, int stroke, int start, int end ) {
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.stroke = stroke;
		this.start = start;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#paint(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void paint(int x, int y, int width, int height, Graphics g)
	{
		g.setStrokeStyle(this.stroke);
		int startOffset = this.startLine;
		int endOffset = this.endLine;
		if (this.gradient == null || this.lastHeight != height ) {
			int steps = height;
			if (this.start != this.end) {
				this.startLine = (this.start * height) / 100;
				this.endLine = (this.end * height) / 100;
				steps = this.endLine - this.startLine;
				startOffset = this.startLine;
				endOffset = this.endLine;				
			} else {
				this.endLine = height;
				endOffset = height;
			}
			this.gradient = DrawUtil.getGradient( this.topColor, this.bottomColor, steps );
			this.lastHeight = height;
		}
		g.setColor( this.topColor );
		for (int i = startOffset; i < endOffset; i++) {
			int color = this.gradient[i - startOffset ];
			g.setColor( color );
			g.drawLine( x, y + i, x + width, y + i);
		}
		g.drawLine( x, y + endOffset, x + width, y + endOffset);
		g.setStrokeStyle( Graphics.SOLID );	
	}

}
