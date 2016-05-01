//#condition polish.usePolishGui
/*
 * Created on 14-Mar-2004 at 21:31:51.
 *
 * Copyright (c) 2004-2005 Robert Virkus / Enough Software
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

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.Background;
import de.enough.polish.ui.ImageConsumer;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.StyleSheet;

/**
 * <p>Paints a tiled image as a background.</p>
 * <p>Following CSS parameters are supported:
 * <ul>
 * 		<li><b>image</b>: the image url, e.g. url( bg.png ) or none</li>
 * 		<li><b>color</b>: the background color, should the image
 * 							be smaller than the actual background-area. Or "transparent".</li>
 * 		<li><b>repeat</b>: defines whether the image should be repeated. Either "repeat", "repeat-x"/"repeat-horizontal" or "repeat-y"/"repeat-vertical".</li>
 * 		<li><b>padding</b>: the gap between tiles, can be negative for overlapping the tiles.</li>
 * 		<li><b>padding-horizontal</b>: the horizontal gap between tiles, can be negative for overlapping the tiles.</li>
 * 		<li><b>padding-vertical</b>: the vertical gap between tiles, can be negative for overlapping the tiles.</li>
 * 		<li><b>anchor</b>: The anchor of the image, either  "left", "right", 
 * 			"center" (="horizontal-center"), "vertical-center", "top" or "bottom" 
 * 			or any combinationof these values. Defaults to "horizontal-center | vertical-center".
 * 		<li><b>overlap</b>: defines whether the tiles should overlap over the actual background-area when they don't
 *        fit exactly into the actual background-area. Either "true"/"yes" or "no"/"false". Defaults to false.</li>
 * 		</li>
 * 		<li><b>x-offset</b>: The number of pixels to move the image horizontally, negative values move it to the left.</li>
 * 		<li><b>y-offset</b>: The number of pixels to move the image vertically, negative values move it to the top.</li>
 * 		<li><b></b>: </li>
 * </ul>
 * </p>
 *
 * <p>Copyright Enough Software 2004 - 2008</p>

 * <pre>
 * history
 *        14-Mar-2004 - rob creation
 * </pre>
 * @author Robert Virkus, robert@enough.de
 */
public class TiledImageBackground 
extends Background
//#ifdef polish.images.backgroundLoad
//# implements ImageConsumer
//#endif
{
	public static final int REPEAT = 1;
	public static final int REPEAT_X = 2;
	public static final int REPEAT_Y = 3;
	
	private Image image;
	private final int color;
	private final int repeatMode;
	private boolean isLoaded;
	private final String imageUrl;
	private final int anchor;
	private final int paddingHorizontal;
	private final int paddingVertical;
	private final boolean overlap;
	private final int xOffset;
	private final int yOffset;
	
	/**
	 * Creates a new image background.
	 * 
	 * @param color the background color or Item.TRANSPARENT
	 * @param imageUrl the url of the image, e.g. "/bg.png", must not be null!
	 * @param repeatMode indicates whether the background image should
	 *        be repeated, either ImageBackground.REPEAT, REPEAT_X or REPEAT_Y
	 * @param anchor the anchor of the image, either  "left", "right", 
	 * 			"center" (="horizontal-center"), "vertical-center", "top" or "bottom" 
	 * 			or any combinationof these values. Defaults to "horizontal-center | vertical-center"
	 * @param paddingHorizontal the horizontal gap between tiles, can be negative for overlapping the tiles
	 * @param paddingVertical the horizontal gap between tiles, can be negative for overlapping the tiles
	 * @param overlap defines whether the tiles should overlap over the actual background-area when they don't
	 *        fit exactly into the actual background-area.
	 */
	public TiledImageBackground( int color, String imageUrl, int repeatMode, int anchor, int paddingHorizontal, int paddingVertical, boolean overlap ) {
		this(color, imageUrl, repeatMode, anchor, paddingHorizontal, paddingVertical, overlap, 0, 0 );
	}
	/**
	 * Creates a new image background.
	 * 
	 * @param color the background color or Item.TRANSPARENT
	 * @param imageUrl the url of the image, e.g. "/bg.png", must not be null!
	 * @param repeatMode indicates whether the background image should
	 *        be repeated, either ImageBackground.REPEAT, REPEAT_X or REPEAT_Y
	 * @param anchor the anchor of the image, either  "left", "right", 
	 * 			"center" (="horizontal-center"), "vertical-center", "top" or "bottom" 
	 * 			or any combinationof these values. Defaults to "horizontal-center | vertical-center"
	 * @param paddingHorizontal the horizontal gap between tiles, can be negative for overlapping the tiles
	 * @param paddingVertical the horizontal gap between tiles, can be negative for overlapping the tiles
	 * @param overlap defines whether the tiles should overlap over the actual background-area when they don't
	 *        fit exactly into the actual background-area.
	 * @param xOffset The number of pixels to move the image horizontally, negative values move it to the left.
	 * @param yOffset The number of pixels to move the image vertically, negative values move it to the top.
	 */
	public TiledImageBackground( int color, String imageUrl, int repeatMode, int anchor, int paddingHorizontal, int paddingVertical, boolean overlap, int xOffset, int yOffset ) {
		this.paddingHorizontal = paddingHorizontal;
		this.paddingVertical = paddingVertical;
		this.color = color;
		this.repeatMode = repeatMode;
		this.imageUrl = imageUrl;
		this.anchor = anchor;
		this.overlap = overlap;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	//#ifdef polish.images.backgroundLoad
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.ImageConsumer#setImage(java.lang.String, javax.microedition.lcdui.Image)
	 //# */
	//# public void setImage(String name, Image image) {
		//# this.image = image;
	//# }
	//#endif
	
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#paint(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void paint(int x, int y, int width, int height, Graphics g) {
		if (!this.isLoaded) {
			try {
				this.image = StyleSheet.getImage(this.imageUrl, this, false);
			} catch (IOException e) {
				//#debug error
				//# System.out.println( "unable to load image [" + this.imageUrl + "]" +  e );
			}
			this.isLoaded = true;
		}
		if (this.color != Item.TRANSPARENT) {
			g.setColor( this.color );
			g.fillRect( x, y, width + 1, height + 1 );
		}
		
		x += this.xOffset;
		y += this.yOffset;
		if (this.image != null) {
			int imgWidth = this.image.getWidth() + this.paddingHorizontal;
			int imgHeight = this.image.getHeight() + this.paddingVertical;
			int numberOfXRepeats = ((width + this.paddingHorizontal) / imgWidth) - 1;
			if (numberOfXRepeats < 0) {
				numberOfXRepeats = 0;
			}
			int totalWidth = (imgWidth - this.paddingHorizontal) + numberOfXRepeats  * imgWidth;
			if (totalWidth < width && this.overlap ) {
				numberOfXRepeats++;
				totalWidth += imgWidth;
			}
			int numberOfYRepeats = ((height + this.paddingVertical) / imgHeight) - 1;
			if (numberOfYRepeats < 0) {
				numberOfYRepeats = 0;
			}
			int totalHeight = ( imgHeight - this.paddingVertical) + numberOfYRepeats * imgHeight;
			if (totalHeight < height && this.overlap ) {
				numberOfYRepeats++;
				totalHeight += imgHeight;
			}
			
			if (this.repeatMode == REPEAT ) {					
				if ( (this.anchor & Graphics.VCENTER) == Graphics.VCENTER) {
					y += (height - totalHeight) / 2;
				} else if ( (this.anchor & Graphics.BOTTOM) == Graphics.BOTTOM) {
					y += (height - totalHeight);
				}
				if ( (this.anchor & Graphics.HCENTER) == Graphics.HCENTER) {
					x += (width - totalWidth) / 2;					
				} else if ( (this.anchor & Graphics.RIGHT) == Graphics.RIGHT) {
					x += (width - totalWidth);
				}
				int imgX = x;
				int imgY = y;
				for (int i = 0; i <= numberOfYRepeats; i++) {
					for (int j = 0; j <= numberOfXRepeats; j++) {
						g.drawImage(this.image, imgX, imgY, Graphics.LEFT | Graphics.TOP );
						imgX += imgWidth;
					}
					imgY += imgHeight;
					imgX = x;
				}
			} else if (this.repeatMode == REPEAT_X) {
				if ( (this.anchor & Graphics.HCENTER) == Graphics.HCENTER) {
					x += ((width - totalWidth) / 2) + ((imgWidth - this.paddingHorizontal) / 2);					
				} else if ( (this.anchor & Graphics.RIGHT) == Graphics.RIGHT) {
					x += (width - totalWidth) + (imgWidth - this.paddingHorizontal);
				}
				if ( (this.anchor & Graphics.VCENTER) == Graphics.VCENTER) {
					y += (height / 2);
				} else if ( (this.anchor & Graphics.BOTTOM) == Graphics.BOTTOM) {
					y += height;
				}

				for (int j = 0; j <= numberOfXRepeats; j++) {
					g.drawImage(this.image, x, y, this.anchor );
					x += imgWidth;
				}
			} else {
				// repeatMode == REPEAT_Y
				if ( (this.anchor & Graphics.HCENTER) == Graphics.HCENTER) {
					x += (width / 2);
				} else if ( (this.anchor & Graphics.RIGHT) == Graphics.RIGHT) {
					x += width;
				}
				if ( (this.anchor & Graphics.VCENTER) == Graphics.VCENTER) {
					y += ((height - totalHeight) / 2) + ((imgHeight - this.paddingVertical) / 2);
				} else if ( (this.anchor & Graphics.BOTTOM) == Graphics.BOTTOM) {
					y += (height - totalHeight) + (imgHeight - this.paddingVertical);
				}
				for (int i = 0; i <= numberOfYRepeats; i++) {
					g.drawImage(this.image, x, y, this.anchor );
					y += imgHeight;
				}
			}
		}
	}
	
	/**
	 * Releases all (memory intensive) resources such as images or RGB arrays of this background.
	 */
	public void releaseResources() {
		this.isLoaded = false;
		this.image = null;
	}
	
}
