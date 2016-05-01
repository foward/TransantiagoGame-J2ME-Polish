//#condition polish.usePolishGui && polish.midp2
/*
 * Created on 07.09.2005 at 09:58:53.
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
package de.enough.polish.ui.screenanimations;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.AccessibleCanvas;
import de.enough.polish.ui.ScreenChangeAnimation;
import de.enough.polish.ui.Style;

public class DisplayScreenChangeAnimation extends ScreenChangeAnimation {
	private Image image;
	private boolean stillRun = true;
	//row = wo sich das neue image befindet am display
	private int row = 0;
	private int[] rgbData ;
	private int[] rgbbuffer ;
	private int[] lstrgbbuffer ;
	private int[] scaler;
	private int lstScale = 0;
	private int width, height;
	private boolean first = true;
	private boolean letsGo = false;
	public DisplayScreenChangeAnimation() {
		super();
	}
	
	protected void show(Style style, Display dsplay, int width, int height,
			Image lstScreenImage, Image nxtScreenImage, AccessibleCanvas nxtCanvas, Displayable nxtDisplayable, boolean isForward  ) 
	{
			int size = nxtScreenImage.getWidth() * nxtScreenImage.getHeight();
			this.height = height;
			this.width = width;
			this.lstScale = width;
			this.scaler = new int [width];
			for(int i = 0;i < this.scaler.length;i++){
				this.scaler[i] = height;
			}
			this.rgbbuffer = new int[size];
			this.lstrgbbuffer = new int [size];
			this.rgbData = new int [size];
			nxtScreenImage.getRGB(this.rgbbuffer, 0, width, 0, 0, width, height );
			lstScreenImage.getRGB(this.lstrgbbuffer, 0, width, 0, 0, width, height );
			super.show(style, dsplay, width, height, lstScreenImage, nxtScreenImage, nxtCanvas, nxtDisplayable, isForward );
	}


	protected boolean animate() {
		// TODO Auto-generated method stub
		int row = 0,column = 5;
		for(int i = 0; i < this.rgbData.length;i++){
			if(row < this.row ){		
					if( this.scaler[row] < column || (this.height - this.scaler[row]) > column){
						this.rgbData[i] = 0x000000;
					}else{
						this.rgbData[i] = this.rgbbuffer[i];
					}
			}
			else{
				if(this.scaler[row] < column || (this.height - this.scaler[row]) > column){
					this.rgbData[i] = 0x000000;
				}else{
					this.rgbData[i] = this.lstrgbbuffer[i];
				}
			}
			row = (row + 1) % this.width;
			if(row == 0)column++;	
		}
		this.lstScale--;
		this.cubeEffect();
		return this.stillRun;
	}
	
	
	private void cubeEffect(){
		//scaling for the lstImage
		for(int i = this.width-1; i > this.lstScale;i--){	
			if(this.scaler[i] > this.height-60){
				this.scaler[i]--;
			}else{
				this.scaler[i]=0;
			}
			
		}
	}
	
	
	public void paintAnimation(Graphics g) {
			g.drawRGB(this.rgbData,0,this.width,0,0,this.width,this.height,false);
	}

}
