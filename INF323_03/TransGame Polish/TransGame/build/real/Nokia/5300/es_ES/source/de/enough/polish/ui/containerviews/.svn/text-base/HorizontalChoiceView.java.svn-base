//#condition polish.usePolishGui
/*
 * Created on 08-Apr-2005 at 11:17:51.
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

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.Background;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.Container;
import de.enough.polish.ui.ContainerView;
import de.enough.polish.ui.Form;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.StyleSheet;

/**
 * <p>Shows  the available items of an ChoiceGroup or a horizontal list.</p>
 * <p>Apply this view by specifying "view-type: horizontal-choice;" in your polish.css file.</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2008</p>
 * <pre>
 * history
 *        02-March-2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class HorizontalChoiceView extends ContainerView {
	
	
	private final static int POSITION_BOTH_SIDES = 0; 
	private final static int POSITION_RIGHT = 1; 
	private final static int POSITION_LEFT = 2; 
	private final static int POSITION_NONE = 3;
	private int arrowColor;
	//#ifdef polish.css.horizontalview-left-arrow
		//# private Image leftArrow;
		//# private int leftYOffset;
	//#endif
	//#ifdef polish.css.horizontalview-right-arrow
		//# private Image rightArrow;
		//# private int rightYOffset;
	//#endif
	//#ifdef polish.css.horizontalview-arrows-image
		//# private Image arrowsImage;
		//# private int yOffset;
	//#endif 
	//#ifdef polish.css.horizontalview-arrow-position
		//# private int arrowPosition;
		//#ifdef polish.css.horizontalview-arrow-padding
			//# private int arrowPadding;
		//#endif
	//#endif
	//#ifdef polish.css.horizontalview-roundtrip
		//# private boolean allowRoundTrip;
	//#endif
	//#ifdef polish.css.horizontalview-expand-background
		//# private Background expandBackground;
		//# private boolean isExpandBackground = true;
	//#endif
	private int arrowWidth = 10;
	private int currentItemIndex;
	private int leftArrowStartX;
	private int leftArrowEndX;
	private int rightArrowStartX;
	//private int rightArrowEndX;
	//private boolean isInitialized;
	private int xOffset;
	private int completeWidthOfItems;

	/**
	 * Creates a new view
	 */
	public HorizontalChoiceView() {
		super();
		this.allowsAutoTraversal = false;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#initContent(de.enough.polish.ui.Container, int, int)
	 */
	protected void initContent(Item parentItm, int firstLineWidth,
			int lineWidth) 
	{
		//#debug
		//# System.out.println("Initalizing HorizontalChoiceView with a lineWidth of " + lineWidth );
		Container parent = (Container) parentItm;
		//TODO allow no selection for MULTIPLE
		int selectedItemIndex = ((ChoiceGroup) parent).getSelectedIndex();
		if (selectedItemIndex == -1) {
			selectedItemIndex = 0;
		}
		if ( selectedItemIndex < parent.size() ) {
			parent.focus (selectedItemIndex, parent.get( selectedItemIndex ), 0);
		}
		//parent.focusedIndex = selectedItemIndex;
		int height = 0;
		//#if polish.css.horizontalview-left-arrow || polish.css.horizontalview-right-arrow
			//# int width = 0;
			//#ifdef polish.css.horizontalview-left-arrow
				//# if (this.leftArrow != null) {
					//# width = this.leftArrow.getWidth();
					//# height = this.leftArrow.getHeight();
				//# }
			//#endif
			//#ifdef polish.css.horizontalview-right-arrow
				//# if (this.rightArrow != null) {
					//# if ( this.rightArrow.getWidth() > width) {
						//# width = this.rightArrow.getWidth();
						//# if (this.leftArrow.getHeight() > height) {
							//# height = this.leftArrow.getHeight();
						//# }
					//# }
				//# }
			//#endif
			//#if polish.css.horizontalview-left-arrow && polish.css.horizontalview-right-arrow
				//# if (this.rightArrow != null && this.leftArrow != null) {
					//# this.arrowWidth = width;
				//# } else {
			//#endif
					//# if (width > this.arrowWidth) {
						//# this.arrowWidth = width;
					//# }
			//#if polish.css.horizontalview-left-arrow && polish.css.horizontalview-right-arrow
				//# }
			//#endif
		//#endif
		int completeArrowWidth;
		//#if polish.css.horizontalview-arrows-image
			//# if (this.arrowsImage != null) {
				//# height = this.arrowsImage.getHeight();
				//# completeArrowWidth = this.arrowsImage.getWidth();
				//# this.arrowWidth = completeArrowWidth / 2;
			//# } else {
		//#endif
				//#if polish.css.horizontalview-arrow-padding
					//# completeArrowWidth = ( this.arrowWidth * 2 ) + this.paddingHorizontal + this.arrowPadding;
				//#else
					completeArrowWidth = ( this.arrowWidth + this.paddingHorizontal ) << 1;
				//#endif
		//#if polish.css.horizontalview-arrows-image
			//# }
		//#endif
		//#ifdef polish.css.horizontalview-arrow-position
			//# if (this.arrowPosition == POSITION_BOTH_SIDES) {
		//#endif
				this.leftArrowStartX = 0;
				this.leftArrowEndX = this.arrowWidth;
				this.rightArrowStartX = lineWidth - this.arrowWidth;
				//this.rightArrowEndX = lineWidth;
		//#ifdef polish.css.horizontalview-arrow-position
			//# } else if (this.arrowPosition == POSITION_RIGHT ){
				//# this.leftArrowStartX = lineWidth - completeArrowWidth + this.paddingHorizontal;
				//# this.leftArrowEndX = this.leftArrowStartX + this.arrowWidth;
				//# this.rightArrowStartX = lineWidth - this.arrowWidth;
				//# //this.rightArrowEndX = lineWidth;
			//# } else if (this.arrowPosition == POSITION_LEFT ) {
				//# this.leftArrowStartX = 0;
				//# this.leftArrowEndX = this.arrowWidth;
				//# this.rightArrowStartX = this.arrowWidth + this.paddingHorizontal;
				//# //this.rightArrowEndX = this.rightArrowStartX + this.arrowWidth;
			//# } else {
				//# completeArrowWidth = 0;
			//# }
		//#endif
		lineWidth -= completeArrowWidth;
		int completeWidth = 0;
		Item[] items = parent.getItems();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			//TODO allow drawing of boxes as well
			((ChoiceItem) item).drawBox = false;
			int itemHeight = item.getItemHeight(lineWidth, lineWidth);
			int itemWidth = item.itemWidth;
			if (itemHeight > height ) {
				height = itemHeight;
			}
			int startX = completeWidth;
			completeWidth += itemWidth + this.paddingHorizontal;
			if ( i == selectedItemIndex) {
				if ( startX + this.xOffset < 0 ) {
					this.xOffset = -startX; 
				} else if ( completeWidth + this.xOffset > lineWidth ) {
					this.xOffset = lineWidth - completeWidth;
				}
			}
		}
		this.completeWidthOfItems = completeWidth;
	
		this.contentHeight = height;
		this.contentWidth = lineWidth + completeArrowWidth;
		
		if (items.length > 0) {
			this.appearanceMode = Item.INTERACTIVE;
		} else {
			this.appearanceMode = Item.PLAIN;
		}
		if (selectedItemIndex < items.length ) {
			this.focusedItem = items[ selectedItemIndex ];
			this.currentItemIndex = selectedItemIndex;
		}
		//if ( selectedItem.isFocused ) {
			//System.out.println("Exclusive Single Line View: contentHeight=" + this.contentHeight);
		//}
		//this.isInitialized = true;
		
		//#if polish.css.horizontalview-arrows-image
			//# if (this.arrowsImage != null) {
				//# int offset = (this.contentHeight - this.arrowsImage.getHeight()) / 2; // always center vertically
				//# this.yOffset = offset;
			//# }
		//#endif
		//#if polish.css.horizontalview-left-arrow			
			//# if (this.leftArrow != null) {
				//# this.leftYOffset = (this.contentHeight - this.leftArrow.getHeight()) / 2; // always center vertically
			//# }
		//#endif
		//#if polish.css.horizontalview-right-arrow
			//# if (this.rightArrow != null) {
				//# this.rightYOffset = (this.contentHeight - this.rightArrow.getHeight()) / 2; // always center vertically
			//# }
		//#endif

//		System.out.println("leftX=" + this.leftArrowStartX);
//		System.out.println("rightX=" + this.rightArrowStartX);
//		System.out.println("arrowColor=" + Integer.toHexString(this.arrowColor));
	}
	
	

	protected void setStyle(Style style) {
		//#ifdef polish.css.horizontalview-expand-background
			//# Boolean expandBackgroundBool = style.getBooleanProperty(137);
			//# if (expandBackgroundBool != null) {
				//# this.isExpandBackground = expandBackgroundBool.booleanValue(); 
			//# }
			//# if (!this.isExpandBackground) {
				//# this.expandBackground = style.background;				
			//# } else {
				//# this.expandBackground = null;
			//# }
		//#endif
		super.setStyle(style);
		//#ifdef polish.css.horizontalview-arrows-image
			//# String arrowImageUrl = style.getProperty(159);
			//# if (arrowImageUrl != null) {
				//# try {
					//# this.arrowsImage = StyleSheet.getImage( arrowImageUrl, this, true );
				//# } catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to load left arrow image [" + arrowImageUrl + "]" + e );
				//# }
			//# }
		//#endif
		//#ifdef polish.css.horizontalview-left-arrow
			//# String leftArrowUrl = style.getProperty(135);
			//# if (leftArrowUrl != null) {
				//# try {
					//# this.leftArrow = StyleSheet.getImage( leftArrowUrl, this, true );
				//# } catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to load left arrow image [" + leftArrowUrl + "]" + e );
				//# }
			//# }
		//#endif
		//#ifdef polish.css.horizontalview-right-arrow
			//# String rightArrowUrl = style.getProperty(134);
			//# if (rightArrowUrl != null) {
				//# try {
					//# this.rightArrow = StyleSheet.getImage( rightArrowUrl, this, true );
				//# } catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to load right arrow image [" + rightArrowUrl + "]" + e );
				//# }
			//# }
		//#endif
		//#ifdef polish.css.horizontalview-arrow-color
			//# Integer colorInt = style.getIntProperty(133);
			//# if ( colorInt != null ) {
				//# this.arrowColor = colorInt.intValue();
			//# }
		//#endif
		//#ifdef polish.css.horizontalview-arrow-position
			//# Integer positionInt = style.getIntProperty(136);
			//# if ( positionInt != null ) {
				//# this.arrowPosition = positionInt.intValue();
			//# }
			//#ifdef polish.css.horizontalview-arrow-padding
				//# Integer arrowPaddingInt = style.getIntProperty(139);
				//# if (arrowPaddingInt != null) {
					//# this.arrowPadding = arrowPaddingInt.intValue();
//# //				} else {
//# //					this.arrowPadding = style.paddingHorizontal;
				//# }
			//#endif
		//#endif
		//#ifdef polish.css.horizontalview-roundtrip
			//# Boolean allowRoundTripBool = style.getBooleanProperty(138);
			//# if (allowRoundTripBool != null) {
				//# this.allowRoundTrip = allowRoundTripBool.booleanValue();
			//# }
		//#endif
	}
	
	
	/**
	 * Paints the content of this container view.
	 * This method calls 
	 * 
	 * @param container the parent container
	 * @param myItems the items that should be painted
	 * @param x the left start position
	 * @param y the upper start position
	 * @param leftBorder the left border, nothing must be painted left of this position
	 * @param rightBorder the right border, nothing must be painted right of this position
	 * @param clipX absolute horizontal clipping start
	 * @param clipY absolute verical clipping start
	 * @param clipWidth clipping width
	 * @param clipHeight clipping height
	 * @param g the Graphics on which this item should be painted.
	 */
	protected void paintContent(Container container, Item[] myItems, int x, int y, int leftBorder, int rightBorder, int clipX, int clipY, int clipWidth, int clipHeight, Graphics g) {
		//#debug
		//# System.out.println("HorizontalView.start: x=" + x + ", y=" + y + ", leftBorder=" + leftBorder + ", rightBorder=" + rightBorder );
		int modifiedX = x;

		//#ifdef polish.css.horizontalview-arrow-position
			//# if (this.arrowPosition == POSITION_BOTH_SIDES ) {
		//#endif
				modifiedX += this.arrowWidth + this.paddingHorizontal;
				leftBorder += this.arrowWidth + this.paddingHorizontal;
				rightBorder -= this.arrowWidth + this.paddingHorizontal;
		//#ifdef polish.css.horizontalview-arrow-position
			//# } else if (this.arrowPosition == POSITION_LEFT ) {
				//# modifiedX += (this.arrowWidth + this.paddingHorizontal) << 1;
				//# leftBorder += (this.arrowWidth + this.paddingHorizontal) << 1;
			//# } else {
				//# rightBorder -= (this.arrowWidth + this.paddingHorizontal) << 1;				
			//# }
		//#endif	
				
		//#ifdef polish.css.horizontalview-expand-background
			//# if (!this.isExpandBackground && this.expandBackground != null) {
				//# this.expandBackground.paint(modifiedX, y, rightBorder-leftBorder, this.contentHeight, g);
			//# }
		//#endif


		
		//#debug
		//# System.out.println("HorizontalChoiceView.item: x=" + modifiedX + ", y=" + y + ", leftBorder=" + leftBorder + ", rightBorder=" + rightBorder + ", availableWidth=" + (rightBorder - leftBorder) + ", itemWidth=" + this.focusedItem.itemWidth  );
//		g.setColor(0xff0000);
//		g.drawLine( rightBorder, y, rightBorder, y + this.contentHeight);
//		g.drawLine( leftBorder, y, leftBorder, y + this.contentHeight);
		boolean setClip = this.completeWidthOfItems > rightBorder - leftBorder;
		if (setClip) {
			g.clipRect(modifiedX, clipY, rightBorder - modifiedX, clipHeight );
		}
		
		int itemX = modifiedX + this.xOffset;
		int focusedX = 0;
		int cHeight = this.contentHeight;
		int vOffset = 0;
		for (int i = 0; i < myItems.length; i++) {
			Item item = myItems[i];
			if ( item == this.focusedItem ) {
				focusedX = itemX;				
			} else {
				//TODO allow explicit setting of top, vcenter and bottom for items (=layout)
				vOffset = (cHeight - item.itemHeight) / 2;
				item.paint(itemX, y + vOffset, itemX, itemX + item.itemWidth, g);
			}
			itemX += item.itemWidth + this.paddingHorizontal;
		}
		if (this.focusedItem != null) {
			vOffset = (cHeight - this.focusedItem.itemHeight) / 2;
			this.focusedItem.paint(focusedX, y + vOffset, leftBorder, rightBorder, g);			
		}
		if (setClip) {
			g.setClip( clipX, clipY, clipWidth, clipHeight );
		}

		//#ifdef polish.css.horizontalview-arrow-position
			//# if (this.arrowPosition != POSITION_NONE ) {
		//#endif

		g.setColor( this.arrowColor );
		//draw left arrow:
		//#ifdef polish.css.horizontalview-roundtrip
			//# if (this.allowRoundTrip || this.currentItemIndex > 0) {
		//#else
			if (this.currentItemIndex > 0) {
		//#endif
			// draw left arrow
			int startX = x + this.leftArrowStartX;
			Image image = null;
			//#if polish.css.horizontalview-arrows-image
				//# image = this.arrowsImage;
				//# vOffset = this.yOffset;
			//#endif			
			//#ifdef polish.css.horizontalview-left-arrow
				//# if (image == null) {
					//# image = this.leftArrow;
					//# vOffset = this.leftYOffset;
				//# }
			//#endif
			if (image != null) {
				//System.out.println("Drawing left IMAGE arrow at " + startX );
				g.drawImage( image, startX, y + vOffset, Graphics.LEFT | Graphics.TOP );
			} else {
				//#if polish.midp2
					//System.out.println("Drawing left triangle arrow at " + startX );
					g.fillTriangle( 
							startX, y + this.contentHeight/2, 
							startX + this.arrowWidth, y,
							startX + this.arrowWidth, y + this.contentHeight );
				//#else
					//# int y1 = y + this.contentHeight / 2;
					//# int x2 = startX + this.arrowWidth;
					//# int y3 = y + this.contentHeight;
					//# g.drawLine( startX, y1, x2, y );
					//# g.drawLine( startX, y1, x2, y3 );
					//# g.drawLine( x2, y, x2, y3 );
				//#endif
			}
		}
		
		// draw right arrow:
		//#if polish.css.horizontalview-arrows-image
			//# if (this.arrowsImage != null) {
				//# return;
			//# }
		//#endif
		if (
		//#ifdef polish.css.horizontalview-roundtrip
			//# this.allowRoundTrip ||  
		//#endif
			(this.currentItemIndex < this.parentContainer.size() - 1) ) 
		{
			// draw right arrow
			int startX = x + this.rightArrowStartX;	
			//#ifdef polish.css.horizontalview-right-arrow
				//# if (this.rightArrow != null) {
					//# g.drawImage( this.rightArrow, startX, y + this.rightYOffset, Graphics.LEFT | Graphics.TOP );
				//# } else {
			//#endif
				//#if polish.midp2
					g.fillTriangle( 
							startX + this.arrowWidth, y + this.contentHeight/2, 
							startX, y,
							startX, y + this.contentHeight );
				//#else
					//# int y1 = y + this.contentHeight / 2;
					//# int x2 = startX + this.arrowWidth;
					//# int y3 = y + this.contentHeight;
					//# g.drawLine( x2, y1, startX, y );
					//# g.drawLine( x2, y1, startX, y3 );
					//# g.drawLine( startX, y, startX, y3 );
				//#endif
			//#ifdef polish.css.horizontalview-right-arrow
				//# }
			//#endif
		}
			
		//#ifdef polish.css.horizontalview-arrow-position
			//# } // if (this.arrowPosition != POSITION_NONE ) {
		//#endif

	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#getNextItem(int, int)
	 */
	protected Item getNextItem(int keyCode, int gameAction) {
		//#debug
		//# System.out.println("ExclusiveSingleLineView: getNextItem()");
		ChoiceGroup choiceGroup = (ChoiceGroup) this.parentContainer;
		Item[] items = this.parentContainer.getItems();
		ChoiceItem currentItem = (ChoiceItem) this.focusedItem;
		if (currentItem == null) {
			//#debug warn
			//# System.out.println("HorizontalChoiceView: getNextItem(): no current item defined, it seems the initContent() has been skipped.");
			this.currentItemIndex = choiceGroup.getSelectedIndex();
			currentItem = (ChoiceItem) items[ this.currentItemIndex ];
			this.focusedItem = currentItem;
		}
		
		ChoiceItem nextItem = null;
	
		//#ifdef polish.css.horizontalview-roundtrip
			//# if ( gameAction == Canvas.LEFT && (this.allowRoundTrip || this.currentItemIndex > 0 )) {
		//#else
			if ( gameAction == Canvas.LEFT && this.currentItemIndex > 0 ) {
		//#endif
			currentItem.select(false);
			this.currentItemIndex--;
			//#ifdef polish.css.horizontalview-roundtrip
				//# if (this.currentItemIndex < 0) {
					//# this.currentItemIndex = items.length - 1;
				//# }
			//#endif
			nextItem = (ChoiceItem) items[ this.currentItemIndex ];
			//nextItem.adjustProperties( lastItem );
			//this.currentItem.select( true );
			choiceGroup.setSelectedIndex( this.currentItemIndex, true );
			if (getScreen() instanceof Form) {
				choiceGroup.notifyStateChanged();
			}
		//#ifdef polish.css.horizontalview-roundtrip
			//# } else if ( gameAction == Canvas.RIGHT && (this.allowRoundTrip || this.currentItemIndex < items.length - 1  )) {
		//#else
			} else if ( gameAction == Canvas.RIGHT && this.currentItemIndex < items.length - 1 ) {
		//#endif
			currentItem.select(false);
			this.currentItemIndex++;
			//#ifdef polish.css.horizontalview-roundtrip
				//# if (this.currentItemIndex >= items.length) {
					//# this.currentItemIndex = 0;
				//# }
			//#endif
			nextItem = (ChoiceItem) items[ this.currentItemIndex ];
			//nextItemItem.adjustProperties( lastItem );
			choiceGroup.setSelectedIndex( this.currentItemIndex, true );
			if (getScreen() instanceof Form) {
				choiceGroup.notifyStateChanged();
			}
			//this.currentItem.select( true );			
		}
		// in all other cases there is no next item:
		return nextItem;
	}

	//#ifdef polish.hasPointerEvents
	//# /**
	 //# * Handles pointer pressed events.
	 //# * This is an optional feature that doesn't need to be implemented by subclasses.
	 //# * 
	 //# * @param x the x position of the event
	 //# * @param y the y position of the event
	 //# * @return true when the event has been handled. When false is returned the parent container
	 //# *         will forward the event to the affected item.
	 //# */
	//# public boolean handlePointerPressed(int x, int y) {
		//# if (y < 0 || y > this.contentHeight ) {
			//# return false;
		//# }
		//# Item[] items = this.parentContainer.getItems();
		//# if (this.currentItemIndex > 0 && x >= this.leftArrowStartX  && x <= this.leftArrowEndX ) {
			//# this.currentItemIndex--;
		//# } else  {
			//# this.currentItemIndex++;
			//# if (this.currentItemIndex >= items.length ) {
				//# this.currentItemIndex = 0;
			//# }
		//# }
//# 		
		//# ((ChoiceItem)this.focusedItem).select( false );
		//# this.focusedItem = items[ this.currentItemIndex ];
		//# //this.currentItem.select( true );
		//# ((ChoiceGroup) this.parentContainer).setSelectedIndex( this.currentItemIndex, true );
		//# this.parentContainer.focus (this.currentItemIndex, this.focusedItem, 0);
		//# return true;
	//# }
	//#endif

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#defocus(de.enough.polish.ui.Style)
	 */
	protected void defocus(Style originalStyle) {
		//#ifdef polish.css.horizontalview-expand-background
			//# if (this.expandBackground != null) {
				//# this.parentContainer.background = this.expandBackground;
				//# this.expandBackground = null;
			//# }
		//#endif
//		if (this.parentBackground != null ) {
//			this.parentContainer.background = this.parentBackground;
//			this.parentBackground = null;
//		}
		super.defocus(originalStyle);
		//System.out.println("EXCLUSIVE:   DEFOCUS!");
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ContainerView#focus(de.enough.polish.ui.Style, int)
	 */
	public void focus(Style focusstyle, int direction) {
		//#ifdef polish.css.horizontalview-expand-background
			//# if (!this.isExpandBackground) {
				//# Background bg = this.parentContainer.background;
				//# if (bg != null) {
					//# this.expandBackground = bg; 
					//# this.parentContainer.background = null;
				//# }
			//# }
		//#endif
		//System.out.println("EXCLUSIVE:   FOCUS, parentBackround != null: " + (this.parentBackground != null));
		super.focus(focusstyle, direction);
	}
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ItemView#isValid(de.enough.polish.ui.Item, de.enough.polish.ui.Style)
	 */
	protected boolean isValid(Item parent, Style style) {
		return (parent instanceof ChoiceGroup);
	}

}
