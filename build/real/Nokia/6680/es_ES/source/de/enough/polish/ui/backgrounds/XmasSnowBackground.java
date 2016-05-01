//#condition polish.usePolishGui
/*
 * Created on 06.12.2005 at 11:38:02.
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

import java.util.Random;

import javax.microedition.lcdui.Graphics;

import de.enough.polish.ui.Background;
import de.enough.polish.ui.Item;

public class XmasSnowBackground extends Background {
	private boolean isRunning = true;
	private int[] x,y,z;
	private int numberOfFlakes,color,maxFlakeSize;
	private transient final Random rand = new Random();
	private int flakeColor = 0xffffff;
	
	public XmasSnowBackground() {
		// just have rand completed
	}
	
	public XmasSnowBackground(int color, int snowFlakesColor, int maxSnowFlakeSize, int numbers) {
		this( color, null, 100, 100, maxSnowFlakeSize, numbers );
		this.flakeColor = snowFlakesColor;
	}
	public XmasSnowBackground(int color, String url, int width, int height, int maxSnowFlakeSize, int numberOfFlakes) {
		super();
		this.color = color;
		this.maxFlakeSize = maxSnowFlakeSize;
		this.numberOfFlakes = numberOfFlakes;
		this.x = new int[numberOfFlakes];
		this.y = new int[numberOfFlakes];
		this.z = new int[numberOfFlakes];
		int i = 0;
		while(i < numberOfFlakes){
			this.x[i] = Math.abs( this.rand.nextInt() % width );
			this.y[i] = Math.abs( this.rand.nextInt() % height );
			this.z[i] = Math.abs( this.rand.nextInt() % maxSnowFlakeSize );
			i++;
		}
	}
	
	public boolean animate() {
		return this.isRunning;
	}
	
	public void paint(int x, int y, int width, int height, Graphics g) {
		if (this.color != Item.TRANSPARENT) {
			g.setColor(this.color);
			g.fillRect(x,y,width,height);
		}
		g.setColor(this.flakeColor );
		int i = 0;
		while(i < this.numberOfFlakes){
//			this.x[i]++;
			if(this.y[i] < height && this.x[i] < width){
				int z = this.z[i];
				this.y[i]+= z;	
				this.x[i]+=1;
			}
			else{
				this.y[i] = 0;
				this.x[i] = Math.abs( this.rand.nextInt() % width );
				this.z[i] = Math.abs( this.rand.nextInt() % this.maxFlakeSize );
			}
//			System.out.print("X:"+this.x[i]+";Y:"+this.y[i]+";Z:"+this.z[i]+";width"+width+";height"+height+"\n");
			int size = this.z[i];
			g.fillRoundRect( x + this.x[i], y + this.y[i],size,size,size,size);
			i++;
		}
	}
}
