//#condition polish.usePolishGui
/*
 * Created on 15-Aug-2007 at 00:41:51.
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
package de.enough.polish.ui.containerviews;

import javax.microedition.lcdui.Graphics;

import de.enough.polish.ui.Container;
import de.enough.polish.ui.ContainerView;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.Style;


/**
 * <p>Shows  the available items of a Container in a horizontal list.</p>
 * <p>Apply this view by specifying "view-type: horizontal;" in your polish.css file.</p>
 *
 * <p>Copyright Enough Software 2007 - 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class HorizontalContainerView extends ContainerView {
	
	
	private int xOffset;
	private boolean allowRoundTrip;
	private boolean isExpandRightLayout;

	/**
	 * Creates a new view
	 */
	public HorizontalContainerView() {
		super();
		this.allowsAutoTraversal = false;
		this.isHorizontal = true;
		this.isVertical = false;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#initContent(de.enough.polish.ui.Container, int, int)
	 */
	protected void initContent(Item parentItm, int firstLineWidth,
			int lineWidth) 
	{
		//#debug
		//# System.out.println("Initalizing HorizontalContainerView");
		Container parent = (Container) parentItm;
		int selectedItemIndex = parent.getFocusedIndex();
		int height = 0;

		int completeWidth = 0;
		Item[] items = parent.getItems();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			int itemHeight = item.getItemHeight(lineWidth, lineWidth);
			int itemWidth = item.itemWidth;
			if (itemHeight > height ) {
				height = itemHeight;
			}
			int startX = completeWidth;
			item.relativeX = completeWidth;
			item.relativeY = 0;
			completeWidth += itemWidth + this.paddingHorizontal;
			if ( i == selectedItemIndex) {
				if ( startX + this.xOffset < 0 ) {
					this.xOffset = -startX; 
				} else if ( completeWidth + this.xOffset > lineWidth ) {
					this.xOffset = lineWidth - completeWidth;
				}
				this.focusedItem = item;
			}
			if (item.appearanceMode != Item.PLAIN) {
				this.appearanceMode = Item.INTERACTIVE;
			}
		}
		this.contentHeight = height;
		if (lineWidth < completeWidth) {
			this.contentWidth = lineWidth;
		} else {
			this.contentWidth = completeWidth;
		}
		
    	if (
    			((parent.getLayout() & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT)
    		&&	((parent.getLayout() & Item.LAYOUT_EXPAND) == Item.LAYOUT_EXPAND)
    		) 
    	{
    		this.isExpandRightLayout = true;
    	} else {
    		this.isExpandRightLayout = false;
    	}

	}
	
	

	protected void setStyle(Style style) {
		super.setStyle(style);

		//#ifdef polish.css.horizontalview-roundtrip
			//# Boolean allowRoundTripBool = style.getBooleanProperty(138);
			//# if (allowRoundTripBool != null) {
				//# this.allowRoundTrip = allowRoundTripBool.booleanValue();
			//# }
		//#endif

	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#paintContent(de.enough.polish.ui.Container, de.enough.polish.ui.Item[], int, int, int, int, int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	protected void paintContent(Container container, Item[] myItems, int x, int y, int leftBorder, int rightBorder, int clipX, int clipY, int clipWidth, int clipHeight, Graphics g) {
    	if (this.isExpandRightLayout) {
    		x = rightBorder - this.contentWidth;
    	}
		x += this.xOffset;
		super.paintContent(container, myItems, x, y, leftBorder, rightBorder, clipX,
				clipY, clipWidth, clipHeight, g);
	}
	
	
	
	
}
