//#condition polish.usePolishGui && polish.midp2
/*
 * Created on Nov 21, 2007 at 4:42:11 PM.
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
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.Background;
import de.enough.polish.util.DrawUtil;

/**
 * <p></p>
 *
 * <p>Copyright Enough Software 2007 - 2008</p>
 * <pre>
 * history
 *        Nov 21, 2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class MaskBackground extends Background
{
	
	private final Background mask;
	private final Background background;
	
	private int[] buffer;
	private int[] maskBuffer;
	private int[] backgroundBuffer;
	private int lastWidth;
	private int lastHeight;
	private final int maskColor;
	private final int opacity;
	private boolean refreshMask;
	private boolean refreshBackground;


	/**
	 * Creates a new combiend background.
	 * 
	 * @param mask the background used for masking the actual background
	 * @param maskColor the color of the mask
	 * @param background  the background painted in the background
	 * @param opacity the overall maximum opacity between 0 (invisible) to 255 (fully opaque) 
	 * 
	 */
	public MaskBackground( Background mask, int maskColor, Background background, int opacity )
	{
		this.mask = mask;
		this.maskColor = maskColor;
		this.background = background;
		this.opacity = opacity;
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#paint(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void paint(int x, int y, int width, int height, Graphics g)
	{
		//#if polish.midp2
			boolean refreshAll = this.buffer == null || width != this.lastWidth || height != this.lastHeight; 
			if (refreshAll || this.refreshBackground || this.refreshMask ) {
				int area = width * height;
				Image image = Image.createImage( width, height );
				Graphics imageG = image.getGraphics();
				if (refreshAll || this.refreshMask ) {
					this.mask.paint(0,0, width, height, imageG);
					int[] maskData = this.maskBuffer; 
					if (maskData == null || maskData.length != area) {
						maskData = new int[ area ];
						this.maskBuffer = maskData;
					}
					image.getRGB(maskData, 0, width, 0, 0, width, height );
					this.refreshMask = false;
					if (refreshAll || this.refreshBackground) {
						imageG.setColor( 0xffffff );
						imageG.fillRect(0, 0, width + 1, height + 1);
					}
				}
				
				if (refreshAll || this.refreshBackground) {
					this.background.paint(0,0, width, height, imageG);
					int[] backgroundData = this.backgroundBuffer; 
					if (backgroundData == null || backgroundData.length != area) {
						backgroundData = new int[ area ];
						this.backgroundBuffer = backgroundData;
					}
					image.getRGB(backgroundData, 0, width, 0, 0, width, height );
					this.refreshBackground = false;
				}
				
				int[] rgbData = this.buffer;
				if (rgbData == null || rgbData.length != area) {
					rgbData = new int[ area ];
					this.buffer = rgbData;
				}
				
				
				int targetColor = imageG.getDisplayColor( this.maskColor ) | 0xff000000;
				for (int i = 0; i < area; i++) {
					int maskCol = this.maskBuffer[i];
					int maskAlpha = maskCol >>> 24;
					maskCol |= 0xff000000;
					if (maskCol == targetColor) {
						int pixel = this.backgroundBuffer[i];
						int pixelAlpha = pixel >>> 24;
						int maxAlpha = Math.min( maskAlpha, this.opacity );
						if (pixelAlpha > maxAlpha) {
							pixel = (pixel | 0xff000000)  & ((maxAlpha << 24) | 0x00ffffff);
						}
						rgbData[i] = pixel;
						
					} else {
						// each non-masked pixel has to be fully transparent:
						rgbData[i] = 0x00000000;
					}
				}
				this.buffer = rgbData;
				this.lastWidth = width;
				this.lastHeight = height;
			}
			//#ifdef polish.Bugs.drawRgbOrigin
				//# x += g.getTranslateX();
				//# y += g.getTranslateY();
			//#endif
			DrawUtil.drawRgb( this.buffer, x, y, width, height, true, g );			
		//#else
			//# this.background.paint(x, y, width, height, g);
		//#endif
	}
	

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#animate()
	 */
	public boolean animate()
	{
		boolean animated = false;
		if ( this.mask.animate()) {
			this.refreshMask = true;
			this.lastWidth = 0;
			animated =  true;
		}
		if ( this.background.animate() ) {
			this.refreshBackground = true;
			this.lastWidth = 0;
			animated =  true;
		}
		return animated;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#hideNotify()
	 */
	public void hideNotify()
	{
		this.mask.hideNotify();
		this.background.hideNotify();
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#releaseResources()
	 */
	public void releaseResources()
	{
		this.lastWidth = 0;
		this.mask.releaseResources();
		this.background.releaseResources();
		this.buffer = null;
		this.maskBuffer = null;
		this.backgroundBuffer = null;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Background#showNotify()
	 */
	public void showNotify()
	{
		this.mask.showNotify();
		this.background.showNotify();
	}

}
