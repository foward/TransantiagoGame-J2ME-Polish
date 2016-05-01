//#condition polish.usePolishGui
/*
 * Created on 23-Jan-2005 at 19:04:14.
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
package de.enough.polish.ui;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


/**
 * <p>Manages and paints the tabs of a tabbed form (or another Screen).</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2008</p>
 * <pre>
 * history
 *        23-Jan-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class TabBar extends Item {

	private final ImageItem[] tabs;
	private Style activeStyle;
	private Style activeFocusedStyle;
	private Style inactiveStyle;
	private Style activeStyleUsed;
	
	private int activeTabIndex;
	//#ifdef polish.hasPointerEvents
		//# protected boolean handlePointerReleaseEvent;
	//#endif
	private int xOffset;
	private int scrollArrowHeight = 10;
	private int scrollArrowPadding = 2;
	private int scrollArrowColor = 0xFFFFFF;
	//#ifdef polish.css.tabbar-left-arrow
		//# private Image leftArrow;
	//#endif
	//#ifdef polish.css.tabbar-right-arrow
		//# private Image rightArrow;
	//#endif
	private int arrowYOffset;
	private int arrowXOffset;
	private boolean allowRoundtrip;
	private int nextTabIndex;
	

	/**
	 * Creates a new tab bar.
	 * 
	 * @param tabNames the names of the tabs
	 * @param tabImages the images of the tabs, can be null
	 */
	public TabBar(String[] tabNames, Image[] tabImages) {
		this( tabNames, tabImages, null );
	}

	/**
	 * Creates a new tab bar.
	 * 
	 * @param tabNames the names of the tabs
	 * @param tabImages the images of the tabs, can be null
	 * @param style the style of the bar
	 */
	public TabBar(String[] tabNames, Image[] tabImages, Style style) {
		super( null, 0, Item.BUTTON, style);
		if (tabImages == null) {
			tabImages = new Image[ tabNames.length ];
		} else if (tabNames == null) {
			tabNames = new String[ tabImages.length ];
		}
		// getting styles:
		//#if !polish.LibraryBuild
			//#style activetab, tab, default
			this.activeStyle = (de.enough.polish.ui.StyleSheet.defaultStyle );
			//#style activefocusedtab, activetab, tab, default
			this.activeFocusedStyle = (de.enough.polish.ui.StyleSheet.defaultStyle );
			//#style inactivetab, tab, default
			this.inactiveStyle = (de.enough.polish.ui.StyleSheet.defaultStyle );
		//#endif
		
		this.tabs = new ImageItem[ tabNames.length ];
		for (int i = 0; i < tabImages.length; i++) {
			String name = tabNames[i];
			Image image = tabImages[i];
			ImageItem tab = new ImageItem( null, image, 0, name, this.inactiveStyle );
			tab.parent = this;
			this.tabs[i] = tab;
		}

		this.activeStyleUsed = this.activeStyle;
		this.tabs[0].style = this.activeStyleUsed;
	}
	
	/**
	 * Changes the active/selected tab.
	 * 
	 * @param index the index of the active tab, the first tab has the index 0.
	 */
	public void setActiveTab( int index ) {
		// deactivating the old tab:
		this.tabs[ this.activeTabIndex ].setStyle(this.inactiveStyle);
		// activating the new tab:
		this.activeTabIndex = index;
		this.tabs[ index ].setStyle(this.activeStyleUsed);
		this.isInitialized = false;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#focus(de.enough.polish.ui.Style, int)
	 */
	protected Style focus(Style newStyle, int direction)
	{
		this.activeStyleUsed = this.activeFocusedStyle;
		this.tabs[ this.activeTabIndex ].setStyle(this.activeFocusedStyle);
		return super.focus(newStyle, direction);
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#defocus(de.enough.polish.ui.Style)
	 */
	protected void defocus(Style originalStyle)
	{
		this.activeStyleUsed = this.activeStyle;
		this.tabs[ this.activeTabIndex ].setStyle(this.activeStyle);
		super.defocus(originalStyle);
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#initContent(int, int)
	 */
	protected void initContent(int firstLineWidth, int lineWidth) {
		int scrollerWidth = this.scrollArrowHeight + 2 * this.scrollArrowPadding;
		//#debug
		//# System.out.println("init of TabBar lineWidth=" + lineWidth + ", scrollerWidth=" + scrollerWidth + ", number of tabs=" + this.tabs.length );
		int maxHeight = scrollerWidth;
		int completeWidth = 0;
		int rightBorder = lineWidth - scrollerWidth;
		//#if polish.css.tabbar-roundtrip
			//# if (this.allowRoundtrip || this.activeTabIndex == 0 || this.activeTabIndex == this.tabs.length -1) {
		//#else
			if (this.activeTabIndex == 0 || this.activeTabIndex == this.tabs.length -1) {
		//#endif
			// only one scroll indicator needs to be painted
			//#if polish.css.tabbar-roundtrip
				//# if (this.activeTabIndex != 0 && !this.allowRoundtrip) {
			//#else
				if (this.activeTabIndex != 0) {
			//#endif
				rightBorder = lineWidth;
			}
			lineWidth -= maxHeight;
			completeWidth = maxHeight;
		} else {
			lineWidth -= 2 * maxHeight;
			completeWidth = 2 * maxHeight;
		}
		
		int activeTabXPos = 0;
		int activeTabWidth = 0;
		for (int i = 0; i < this.tabs.length; i++) {
			ImageItem tab = this.tabs[i];
			int tabHeight = tab.getItemHeight(firstLineWidth, lineWidth);
			if (tabHeight > maxHeight ) { 
				maxHeight = tabHeight;
			}
			if (i == this.activeTabIndex) {
				activeTabXPos = completeWidth;
				activeTabWidth = tab.itemWidth;
			}
			// I can use the itemWidth field, since I have called getItemHeight(..) above,
			// which initialises the tab.
			tab.relativeX = completeWidth;
			completeWidth += tab.itemWidth;
		}
		this.contentHeight = maxHeight;
		this.contentWidth = completeWidth;
		if (this.activeTabIndex == 0) {
			this.xOffset = 0;
		} else if ( this.xOffset + activeTabXPos < scrollerWidth ) {
			// tab is too much left:
			this.xOffset = scrollerWidth - activeTabXPos;
			//System.out.println("this.xOffset + activeTabXPos < scrollerWidth ");
		} else if ( this.xOffset + activeTabXPos + activeTabWidth > rightBorder ) {
			// tab is too much right:
			//#if polish.css.tabbar-roundtrip
				//# if (this.allowRoundtrip) {
					//# this.xOffset = (rightBorder - activeTabWidth - activeTabXPos);
				//# } else {
			//#endif
					this.xOffset = (rightBorder - activeTabWidth) - (activeTabXPos - scrollerWidth);
			//#if polish.css.tabbar-roundtrip
				//# }
			//#endif
			//System.out.println("this.xOffset + activeTabXPos + activeTabWidth > rightBorder");
			//System.out.println("xOffset=" + this.xOffset + ", activeTabXPos=" + activeTabXPos + ", activeTabWidth=" + activeTabWidth + ", rightBorder=" + rightBorder);
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#paintContent(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	protected void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) 
	{
		// draw scrolling indicators:
		g.setColor( this.scrollArrowColor );
		int cHeight = this.contentHeight;
		y += (cHeight - this.scrollArrowHeight) / 2;
		int originalX = x;
		//#if polish.css.tabbar-roundtrip
			//# if ( this.allowRoundtrip || this.activeTabIndex > 0 ) {
		//#else
			if ( this.activeTabIndex > 0 ) {
		//#endif
			// draw left scrolling indicator:
			x += this.scrollArrowPadding;
			//#ifdef polish.css.tabbar-left-arrow
				//# if (this.leftArrow != null) {
					//# g.drawImage(this.leftArrow, x + this.arrowXOffset, y + this.arrowYOffset, Graphics.LEFT |  Graphics.TOP );
				//# } else {
			//#endif
					int halfWidth = this.scrollArrowHeight >> 1;
					//#ifdef polish.midp2
						g.fillTriangle(x, y + halfWidth-1, x + this.scrollArrowHeight, y, x + this.scrollArrowHeight, y + this.scrollArrowHeight );
					//#else
						//# g.drawLine( x, y + halfWidth-1, x + this.scrollArrowHeight, y );
						//# g.drawLine( x + this.scrollArrowHeight, y, x + this.scrollArrowHeight, y + this.scrollArrowHeight);
						//# g.drawLine( x, y + halfWidth-1, x + this.scrollArrowHeight, y  + this.scrollArrowHeight );
					//#endif
			//#ifdef polish.css.tabbar-left-arrow
				//# }
			//#endif
			x += this.scrollArrowHeight + this.scrollArrowPadding;
			//#if polish.css.tabbar-roundtrip
				//# if (this.allowRoundtrip) {
					//# originalX = x;
				//# }
			//#endif
		}
			
		//#if polish.css.tabbar-roundtrip
			//# if ( this.allowRoundtrip || this.activeTabIndex < this.tabs.length - 1 ) {
		//#else
			if ( this.activeTabIndex < this.tabs.length - 1 ) {
		//#endif		
			// draw right scrolling indicator:
			rightBorder -=  (this.scrollArrowHeight + this.scrollArrowPadding);
			//#ifdef polish.css.tabbar-right-arrow
				//# if (this.rightArrow != null) {
					//# g.drawImage(this.rightArrow, rightBorder + this.arrowXOffset, y + this.arrowYOffset, Graphics.LEFT |  Graphics.TOP );
				//# } else {
			//#endif
					int halfWidth = this.scrollArrowHeight / 2;
					//#ifdef polish.midp2
						g.fillTriangle(rightBorder, y, rightBorder, y  + this.scrollArrowHeight, rightBorder + this.scrollArrowHeight, y + halfWidth - 1 );
					//#else
						//# g.drawLine( rightBorder, y, rightBorder, y  + this.scrollArrowHeight );
						//# g.drawLine( rightBorder, y, rightBorder + this.scrollArrowHeight, y + halfWidth - 1);
						//# g.drawLine( rightBorder, y  + this.scrollArrowHeight, rightBorder + this.scrollArrowHeight, y + halfWidth - 1);
					//#endif
			//#ifdef polish.css.tabbar-right-arrow
				//# }
			//#endif
			rightBorder -=  this.scrollArrowPadding;
		}

		// draw the tabs:
		y -= (cHeight - this.scrollArrowHeight) / 2;
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();
		g.setClip( x, y, rightBorder - x, clipHeight);
		x = originalX + this.xOffset;
		for (int i = 0; i < this.tabs.length; i++) {
			ImageItem tab = this.tabs[i];
			int tabHeight = tab.itemHeight;
			tab.paint( x, y + (cHeight - tabHeight), leftBorder, rightBorder, g );
			x += tab.itemWidth;
		}
		g.setClip( clipX, clipY, clipWidth, clipHeight);		
	}

	//#ifdef polish.useDynamicStyles	
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Item#createCssSelector()
	 //# */
	//# protected String createCssSelector() {
		//# return "tabbar";
	//# }
	//#endif
	

	public void setStyle(Style style) {
		super.setStyle(style);
		//#ifdef polish.css.tabbar-scrolling-indicator-color
			//# Integer scrollColorInt = style.getIntProperty(47);
			//# if (scrollColorInt != null) {
				//# this.scrollArrowColor = scrollColorInt.intValue();
			//# }
		//#endif
		//#ifdef polish.css.tabbar-left-arrow
			//# String leftArrowUrl = style.getProperty(155);
			//# if (leftArrowUrl != null) {
				//# try {
					//# this.leftArrow = StyleSheet.getImage(leftArrowUrl, this, false);
					//# this.scrollArrowHeight = this.leftArrow.getHeight();
				//# } catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to load tabbar-left-arrow " + leftArrowUrl);
				//# }
			//# }
		//#endif
		//#ifdef polish.css.tabbar-right-arrow
			//# String rightArrowUrl = style.getProperty(156);
			//# if (rightArrowUrl != null) {
				//# try {
					//# this.rightArrow = StyleSheet.getImage(rightArrowUrl, this, false);
					//# this.scrollArrowHeight = this.rightArrow.getHeight();
				//# } catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to load tabbar-right-arrow " + rightArrowUrl);
				//# }
			//# }
		//#endif
		//#ifdef polish.css.tabbar-arrow-y-offset
			//# Integer arrowYOffsetInt = style.getIntProperty(157);
			//# if (arrowYOffsetInt != null) {
				//# this.arrowYOffset = arrowYOffsetInt.intValue();
			//# }
		//#endif
		//#ifdef polish.css.tabbar-arrow-x-offset
			//# Integer arrowXOffsetInt = style.getIntProperty(157);
			//# if (arrowXOffsetInt != null) {
				//# this.arrowXOffset = arrowXOffsetInt.intValue();
			//# }
		//#endif
		//#if polish.css.tabbar-roundtrip
			//# Boolean allowRoundtripBool = style.getBooleanProperty(158);
			//# if (allowRoundtripBool != null) {
				//# this.allowRoundtrip = allowRoundtripBool.booleanValue();
			//# }
		//#endif
		//#if polish.css.tabbar-activetab-style
			//# Style actStyle = (Style) style.getObjectProperty(330);
			//# if (actStyle != null) {
				//# this.activeStyle = actStyle; 
				//# this.activeStyleUsed = actStyle;
			//# }
		//#endif
		//#if polish.css.tabbar-inactivetab-style
			//# Style inactStyle = (Style) style.getObjectProperty(331);
			//# if (inactStyle != null) {
				//# this.inactiveStyle = inactStyle; 
			//# }
		//#endif
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#handleKeyPressed(int, int)
	 */
	protected boolean handleKeyPressed(int keyCode, int gameAction) {
		//#if polish.css.tabbar-roundtrip
			//# if (this.allowRoundtrip) {
				//# if (gameAction == Canvas.RIGHT) {
					//# this.nextTabIndex = this.activeTabIndex + 1;
					//# if (this.nextTabIndex >= this.tabs.length) {
						//# this.nextTabIndex = 0;
					//# }
					//# return true;
				//# } else if (gameAction == Canvas.LEFT) {
					//# this.nextTabIndex = this.activeTabIndex - 1;
					//# if (this.nextTabIndex < 0) {
						//# this.nextTabIndex = this.tabs.length - 1;
					//# }		
					//# return true;
				//# }
			//# }
		//#endif
		if (gameAction == Canvas.RIGHT && this.activeTabIndex < this.tabs.length -1 ) {
			this.nextTabIndex = this.activeTabIndex + 1;
			return true;
		} else if (gameAction == Canvas.LEFT && this.activeTabIndex > 0) {
			this.nextTabIndex = this.activeTabIndex - 1;
			return true;
		}
		return super.handleKeyPressed(keyCode, gameAction);
	}

	//#ifdef polish.hasPointerEvents
	//# protected boolean handlePointerPressed(int x, int y) {
		//# this.handlePointerReleaseEvent = false;
		//# if (!isInItemArea(x, y)) {
			//# return false;
		//# }
		//# //System.out.println( "pointer-pressed: " + x + ", " + y);
		//# int scrollerWidth = this.scrollArrowHeight + 2 * this.scrollArrowPadding;
		//# int newActiveTabIndex = this.activeTabIndex;
		//# if ( (this.activeTabIndex > 0 || this.allowRoundtrip) && x <= scrollerWidth ) {
			//# //System.out.println("left: x <= " + scrollerWidth );
			//# int index = this.activeTabIndex - 1;
			//# if (index < 0) {
				//# index = this.tabs.length - 1;
			//# }
			//# newActiveTabIndex = index;
		//# } else if ( (this.activeTabIndex < this.tabs.length -1 || this.allowRoundtrip) && x >= this.itemWidth - scrollerWidth) {
			//# //System.out.println("right: x >= " + (this.xRightPos - scrollerWidth) );
			//# newActiveTabIndex = (this.activeTabIndex + 1) % this.tabs.length;
		//# } else {
			//# if (this.activeTabIndex > 0 || this.allowRoundtrip) {
//# //				x -= scrollerWidth;
			//# }
			//# for (int i = 0; i < this.tabs.length; i++) {
				//# ImageItem tab = this.tabs[i];
//# //				System.out.println( "x=" + x + ", tab.relativeX=" + tab.relativeX + ", xOffset=" + this.xOffset +  ", tab=" + tab);
				//# int tabX = tab.relativeX + this.xOffset;
				//# if (tabX <= x && x <= tabX + tab.itemWidth) {
					//# newActiveTabIndex = i;
					//# break;
				//# }
			//# }
		//# }
		//# int activeTab = this.activeTabIndex;
		//# if (newActiveTabIndex != activeTab) {
			//# this.tabs[newActiveTabIndex].notifyItemPressedStart();
			//# this.nextTabIndex = newActiveTabIndex;
			//# this.handlePointerReleaseEvent = true;
		//# }
//# 		
		//# return this.handlePointerReleaseEvent;
	//# }
	//#endif
	
	
	//#ifdef polish.hasPointerEvents
	//# protected boolean handlePointerReleased(int x, int y) {
		//# if (this.handlePointerReleaseEvent) {
			//# this.handlePointerReleaseEvent = false;
			//# if (this.screen instanceof TabbedForm) {
				//# this.tabs[this.nextTabIndex].notifyItemPressedEnd();
				//# ((TabbedForm)this.screen).setActiveTab(this.nextTabIndex);
			//# }
			//# return true;
		//# }
		//# return false;
	//# }
	//#endif

	/**
	 * Sets the image for the specified tab.
	 * 
	 * @param tabIndex the index of the tab 
	 * @param image the image
	 */
	public void setImage(int tabIndex, Image image) {
		this.tabs[tabIndex].setImage(image);		
	}
	
	/**
	 * Sets the text for the specified tab.
	 * 
	 * @param tabIndex the index of the tab 
	 * @param text the text
	 */
	public void setText(int tabIndex, String text ) {
		this.tabs[tabIndex].setAltText(text);		
	}

	/**
	 * Retrieves the index of the currently selected tab.
	 * 
	 * @return the index of the currently selected tab, 0 is the index of the first tab.
	 */
	public int getNextTab() {
		return this.nextTabIndex;
	}

}
