//#condition polish.usePolishGui
/*
 * Created on Oct 27, 2004 at 7:03:40 PM.
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
package de.enough.polish.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import de.enough.polish.util.ArrayList;
import de.enough.polish.util.TextUtil;

/**
 * <p>Is responsible for visual representation and interpretation of user-input.</p>
 * <p>Copyright Enough Software 2004 - 2007 - 2008</p>
 * <pre>
 * history
 *        Oct 27, 2004 - rob creation
 * </pre>
 * @author Robert Virkus, robert@enough.de
 */
public class ContainerView 
extends ItemView
{
	//#if polish.css.columns || polish.useTable
		//#define tmp.useTable
		//#ifdef polish.css.columns-width.star
			//# private int starIndex;
		//#endif
			
	//#endif
	
	protected static final int NO_COLUMNS = 0;
	protected static final int EQUAL_WIDTH_COLUMNS = 1;
	protected static final int NORMAL_WIDTH_COLUMNS = 2;
	protected static final int STATIC_WIDTH_COLUMNS = 3;

	protected int yOffset;
	protected int focusedIndex = -1;
	/** this field is set automatically, so that subclasses can use it for referencing the parent-container */
	protected transient Container parentContainer;
	/** determines whether any animation of this view should be (re) started at the next possibility. this is set to "true" in each showNotify() method. */
	protected boolean restartAnimation;
	protected boolean focusFirstElement;
	protected int appearanceMode;
	protected transient Item focusedItem;
	
	// table support:
	protected int columnsSetting = NO_COLUMNS;
	protected int numberOfColumns;
	protected int[] columnsWidths;
	protected int[] rowsHeights;
	protected int numberOfRows;
//	/** 
//	 * the number of items - this information is used for rebuilding the table only when it is necessary 
//	 * (number is changed or the columns-width is not static). 
//	 */
//	protected int numberOfItems;
//	/** All items ordered within a table. Some cells can be null when colspan or rowspan CSS attributes are used. */
//	protected Item[][] itemsTable;
	
	protected boolean allowCycling = true;
	
	//#if polish.css.view-type-left-x-offset
		//# protected int leftXOffset;
	//#endif
	//#if polish.css.view-type-right-x-offset
		//# protected int rightXOffset;
	//#endif
	//#if polish.css.view-type-top-y-offset
		//# protected int topYOffset;
	//#endif
	//#if polish.css.view-type-sequential-traversal
		//# protected boolean isSequentialTraversal;
	//#endif
	//#if polish.css.expand-items
		//# protected boolean isExpandItems;
	//#endif
	/** indicates whether the parent Container is allowed to change the currently focused item 
	 *  when the user traverses around a form and enters the container from different sides 
	 */
	protected boolean allowsAutoTraversal = true;
	protected boolean isHorizontal = true;
	protected boolean isVertical = true;
	/** indicates whether elements in this container view can be selected directly by pointer events */
	protected boolean allowsDirectSelectionByPointerEvent = true;


	/**
	 * Creates a new view
	 */
	protected ContainerView() {
		super();
	}
	
	/**
	 * Initialises this item. 
	 * The implementation needs to calculate and set the contentWidth and 
	 * contentHeight fields. 
	 * The style of the focused item has already been set.
	 *  
	 * @param parentContainerItem the Container which uses this view, use parent.getItems() for retrieving all items. 
	 * @param firstLineWidth the maximum width of the first line 
	 * @param lineWidth the maximum width of any following lines
	 * @see #contentWidth
	 * @see #contentHeight
	 */
	protected void initContent( Item parentContainerItem, int firstLineWidth, int lineWidth ) {
		Container parent = (Container) parentContainerItem;		
		//#debug
		//# System.out.println("ContainerView: intialising content for " + this + " with vertical-padding " + this.paddingVertical + ", focusedIndex=" + this.focusedIndex + ", parent.focusedIndex=" + parent.getFocusedIndex() );
		this.focusedIndex = parent.getFocusedIndex();
		this.focusedItem = parent.getFocusedItem();
		//#if polish.Container.allowCycling != false
			this.allowCycling = parent.allowCycling;
			Item ancestor = parent.parent;
			while (this.allowCycling && ancestor != null) {
				if ( (ancestor instanceof Container)  && ((Container)ancestor).getNumberOfInteractiveItems()>1 ) {
					this.allowCycling = false;
					break;
				}
				ancestor = ancestor.parent;
			}
		//#endif
		//#if polish.css.view-type-left-x-offset
			//# lineWidth -= this.leftXOffset;
		//#endif
		//#if polish.css.view-type-right-x-offset
			//# lineWidth -= this.rightXOffset;
		//#endif

		
		this.parentContainer = parent;
		Item[] myItems = parent.getItems();

		//#ifdef tmp.useTable
			if (this.columnsSetting == NO_COLUMNS || myItems.length <= 1) {
		//#endif
			this.isHorizontal = false;
			this.isVertical = true;
			// look at the layout of the parentContainer, since the SHRINK layout can be set outside of the setStyle method as well:
			boolean isLayoutShrink = (this.parentContainer.layout & Item.LAYOUT_SHRINK) == Item.LAYOUT_SHRINK;
			int myContentWidth = 0;
			int myContentHeight = 0;
			boolean hasFocusableItem = false;
			for (int i = 0; i < myItems.length; i++) {
				Item item = myItems[i];
				//System.out.println("initalising " + item.getClass().getName() + ":" + i);
				int width = item.getItemWidth( firstLineWidth, lineWidth );
				int height = item.getItemHeight( firstLineWidth, lineWidth );
				if (item.appearanceMode != Item.PLAIN) {
					hasFocusableItem = true;
				}
				if (isLayoutShrink && i == this.focusedIndex) {
					width = 0;
				}
				if (width > myContentWidth) {
					myContentWidth = width; 
				}
				item.relativeY = myContentHeight;
				item.relativeX = 0;
				myContentHeight += height + this.paddingVertical;
			}
			if (hasFocusableItem) {
				this.appearanceMode = Item.INTERACTIVE;
				if (isLayoutShrink && this.focusedItem != null) {
					Item item = this.focusedItem;
					//System.out.println("container has shrinking layout and contains focuse item " + item + ", minWidth=" + parent.minimumWidth);
					item.isInitialized = false;
					boolean doExpand = item.isLayoutExpand;
					int width;
					if (doExpand) {
						item.isLayoutExpand = false;
						width = item.getItemWidth( lineWidth, lineWidth );
						item.isInitialized = false;
						item.isLayoutExpand = true;
					} else {
						width = item.itemWidth;
					}
					if (width > myContentWidth) {
						myContentWidth = width;
					}
					if ( parent.minimumWidth != 0 && myContentWidth < parent.minimumWidth ) {
						myContentWidth = parent.minimumWidth;
					}
				}
			} else {
				this.appearanceMode = Item.PLAIN;
			}
			
			//#if polish.css.view-type-top-y-offset
				//# this.contentHeight = myContentHeight + this.topYOffset;
			//#else
				this.contentHeight = myContentHeight;
			//#endif
			this.contentWidth = myContentWidth;
			//#if polish.css.expand-items
				//# if (this.isExpandItems) {
					//# if (parent.minimumWidth > myContentWidth) {
						//# myContentWidth = parent.minimumWidth - ((parent.borderWidth << 1) + parent.marginLeft + parent.paddingLeft + parent.marginRight + parent.paddingRight);
					//# }
					//# for (int i = 0; i < myItems.length; i++)
					//# {
						//# Item item = myItems[i];
						//# if (!item.isLayoutExpand && item.itemWidth < myContentWidth) {
							//# item.isLayoutExpand = true;
							//# item.init(myContentWidth, myContentWidth);
							//# item.isLayoutExpand = false;
						//# }
					//# }
				//# }
			//#endif
		//#ifdef tmp.useTable
				return;
			}
		//#endif
		
		//#ifdef tmp.useTable
			this.isHorizontal = true;
			// columns are used
			boolean isNormalWidthColumns = (this.columnsSetting == NORMAL_WIDTH_COLUMNS);
			//#ifdef polish.css.columns-width.star
				//# if (this.columnsSetting == STATIC_WIDTH_COLUMNS) {
					//# if (this.starIndex != -1) {
						//# int combinedWidth = 0;
						//# for (int i = 0; i < this.numberOfColumns; i++) {
							//# combinedWidth += this.columnsWidths[i];
						//# }
						//# this.columnsWidths[this.starIndex] = lineWidth - combinedWidth;
						//# this.starIndex = -1;
						//#debug
						//# System.out.println("initContent: width of star column=" + (lineWidth - combinedWidth) );
					//# }
				//# } else {
			//#else
				if (this.columnsSetting != STATIC_WIDTH_COLUMNS) {
			//#endif
				int availableColumnWidth;
				if (isNormalWidthColumns) {
					// this.columnsSetting == NORMAL_WIDTH_COLUMNS
					// each column should use as much space as it can use
					// without ousting the other columns
					// (the calculation will be finished below)
					availableColumnWidth = lineWidth - ((this.numberOfColumns -1) * this.paddingHorizontal);
				} else {
					// each column should take an equal share
					availableColumnWidth = 
						(lineWidth - ((this.numberOfColumns -1) * this.paddingHorizontal))
						/ this.numberOfColumns;
				}
				//System.out.println("available column width: " + availableColumnWidth );
				this.columnsWidths = new int[ this.numberOfColumns ];
				for (int i = 0; i < this.numberOfColumns; i++) {
					this.columnsWidths[i] = availableColumnWidth;
				}
			}
				
			//#if polish.css.colspan
				//# ArrayList rowHeightsList = new ArrayList( (myItems.length / this.numberOfColumns) + 1 );
			//#else
				this.numberOfRows = (myItems.length / this.numberOfColumns);
				if (myItems.length % this.numberOfColumns != 0) {
					this.numberOfRows += 1;
				}
				this.rowsHeights = new int[ this.numberOfRows ];
			//#endif
			int maxRowHeight = 0;
			int columnIndex = 0;
			int rowIndex = 0;
			int[] maxColumnWidths = null;
			if (isNormalWidthColumns) {
				maxColumnWidths = new int[ this.numberOfColumns ];
			}
			int maxWidth = 0; // important for "equal" columns-width
			int myContentHeight = 0;
			boolean hasFocusableItem = false;
			int columnX = 0; // the horizontal position of the current column relative to the content's left corner (starting a 0)
			//System.out.println("starting init of " + myItems.length + " container items.");
			for (int i=0; i< myItems.length; i++) {
				Item item = myItems[i];
				int availableWidth = this.columnsWidths[columnIndex];
				//#if polish.css.colspan
					//# int itemColSpan = item.colSpan;
					//# if (!item.isInitialized && item.style != null) {
						//# Integer colSpanInt = item.style.getIntProperty(106);
						//# if ( colSpanInt != null ) {
							//# itemColSpan = colSpanInt.intValue();
							//# //System.out.println("colspan of item " + i + "/" + item + ", column " + columnIndex + ": " + itemColSpan);
						//# }
					//# }
					//# //System.out.println("ContainerView.init(): colspan of item " + i + "=" + itemColSpan);
					//# if (itemColSpan > 1) {
						//# // okay, this item stretched beyond one column,
						//# // so let's calculate the correct available cell width
						//# // and switch to the right column index:
						//# int maxColSpan = this.numberOfColumns - columnIndex;
						//# if (itemColSpan > maxColSpan) {
							//#debug error
							//# System.err.println("Warning: colspan " + itemColSpan + " is invalid at column " + columnIndex + " of a table with " + this.numberOfColumns + " columns, now using maximum possible value " + maxColSpan + ".");
							//# itemColSpan = maxColSpan;
						//# }
//# 						
						//# // adjust the available width only when
						//# // each column has an equal size or when
						//# // the column widths are static,
						//# // otherwise the complete row width
						//# // is available anyhow:
						//# if (!isNormalWidthColumns) {
							//# if (itemColSpan == maxColSpan) {
								//# availableWidth = 0;
								//# for (int j = 0; j < columnIndex; j++) {
									//# availableWidth += this.paddingHorizontal + this.columnsWidths[j];
								//# }
								//# availableWidth = lineWidth - availableWidth;
							//# } else {
								//# for (int j = columnIndex + 1; j < columnIndex + itemColSpan; j++) {
									//# availableWidth += this.paddingHorizontal + this.columnsWidths[j];
								//# }								
							//# }
							//# //System.out.println("ContainerView.init(): adjusted availableWidth for item " + i + ": " + availableWidth);
						//# }
					//# }
				//#endif
//				System.out.println( i + ": available with: " + availableWidth + ", lineWidth=" + lineWidth );
//				System.out.println("itemWidth=" + item.getItemWidth( availableWidth, availableWidth ));
				int width = item.getItemWidth( availableWidth, availableWidth );
				//System.out.println("got item width");
				int height = item.getItemHeight( availableWidth, availableWidth );
				if (item.appearanceMode != Item.PLAIN) {
					hasFocusableItem = true;
				}
								
				if (height > maxRowHeight) {
					maxRowHeight = height;
				}
				//#if polish.css.colspan
					//# if (itemColSpan == 1) {
				//#endif
						if (isNormalWidthColumns && width > maxColumnWidths[columnIndex ]) {
							maxColumnWidths[ columnIndex ] = width;
						}
						if (width > maxWidth ) {
							maxWidth = width;
						}
				//#if polish.css.colspan
					//# }
				//#endif
				//#if polish.css.colspan
					//# if (item.colSpan != itemColSpan) {
						//#debug
						//# System.out.println("initializing new colspan of item " + i + "/" + item + ", column " + columnIndex + ": " + itemColSpan);
						//# item.colSpan = itemColSpan;
						//# item.isInitialized = false;
					//# }
					//# columnIndex += itemColSpan;
				//#else
					columnIndex++;
				//#endif
				item.relativeX = columnX; // when equal or normal column widths are used, this is below changed again, since the widhs are just calculated right now.
				item.relativeY = myContentHeight;
				columnX += availableWidth;
				if (columnIndex == this.numberOfColumns) {
					//System.out.println("starting new row: rowIndex=" + rowIndex + "  numberOfRows: " + numberOfRows);
					columnIndex = 0;
					columnX = 0;
					//#if polish.css.colspan
						//# //System.out.println("ContainerView.init(): adding new row " + rowIndex + " with height " + maxRowHeight + ", contentHeight=" + myContentHeight + ", item " + i);
						//# rowHeightsList.add( new Integer( maxRowHeight ) );
					//#else
						this.rowsHeights[rowIndex] = maxRowHeight;						
					//#endif
					myContentHeight += maxRowHeight + this.paddingVertical;
					maxRowHeight = 0;
					rowIndex++;
				}
			} // for each item
			if (hasFocusableItem) {
				this.appearanceMode = Item.INTERACTIVE;
			} else {
				this.appearanceMode = Item.PLAIN;
			}
			if (columnIndex != 0) {
				// last row is not completely filled.
				//#if polish.css.colspan
					//# rowHeightsList.add( new Integer( maxRowHeight ) );
				//#endif
				myContentHeight += maxRowHeight;
			}
			//#if polish.css.colspan
				//# this.numberOfRows = rowHeightsList.size();
				//# //System.out.println("ContainerView.init(): numberOfRows=" + this.numberOfRows + ", rowIndex=" + rowIndex);
				//# this.rowsHeights = new int[ this.numberOfRows ];
//# 				
				//# for (int i = 0; i < this.numberOfRows; i++) {
					//# this.rowsHeights[i] = ((Integer) rowHeightsList.get(i)).intValue();
				//# }
			//#endif
			// now save the worked out dimensions:
			columnX = 0;
			if (isNormalWidthColumns) {
				// Each column should use up as much space as 
				// needed in the "normal" columns-width mode.
				// Each column which takes less than available 
				// the available-row-width / number-of-columns
				// can keep, but the others might need to be adjusted,
				// in case the complete width of the table is wider
				// than the allowed width.
				
				int availableRowWidth = lineWidth - ((this.numberOfColumns -1) * this.paddingHorizontal);
				int availableColumnWidth = availableRowWidth / this.numberOfColumns;
				int usedUpWidth = 0;
				int leftColumns = this.numberOfColumns;
				int completeWidth = 0;
				for (int i = 0; i < maxColumnWidths.length; i++) {
					int maxColumnWidth = maxColumnWidths[i];
					if (maxColumnWidth <= availableColumnWidth) {
						usedUpWidth += maxColumnWidth;
						leftColumns--;
					}
					completeWidth += maxColumnWidth;
				}
				if (completeWidth <= availableRowWidth) {
					// workaround for cases in which there are only items with a colspan of the complete row,
					// or when a single column does not occupy any items with colspan=1.
					//#if polish.css.colspan
						//# int numberOfZeroWidthColumns = 0;
						//# for (int i = 0; i < maxColumnWidths.length; i++)
						//# {
							//# if (maxColumnWidths[i] == 0) {
								//# numberOfZeroWidthColumns++;
							//# }
						//# }
						//# if (numberOfZeroWidthColumns > 0) {
							//# int remainingWidth = availableRowWidth - completeWidth;
							//# for (int i = 0; i < maxColumnWidths.length; i++)
							//# {
								//# if (maxColumnWidths[i] == 0) {
									//# int colWidth;
									//# if (numberOfZeroWidthColumns == 1) {
										//# // last column receives the remaining space:
										//# colWidth = remainingWidth;
									//# } else {
										//# colWidth = remainingWidth / numberOfZeroWidthColumns;
									//# }
									//# maxColumnWidths[i] = colWidth;
									//# numberOfZeroWidthColumns--;
									//# remainingWidth -= colWidth;
								//# }
							//# }
						//# }
					//#endif
					// okay, the table is now fine just how it is
					this.columnsWidths = maxColumnWidths;
				} else {
					//System.out.println("container-view: too wide");
					// okay, some columns need to be adjusted:
					// re-initialise the table:
					int leftAvailableColumnWidth = (availableRowWidth - usedUpWidth) / leftColumns;
					int[] newMaxColumnWidths = new int[ this.numberOfColumns ];
					myContentHeight = 0;
					columnIndex = 0;
					rowIndex = 0;
					maxRowHeight = 0;
					maxWidth = 0;
					//System.out.println("starting init of " + myItems.length + " container items.");
					for (int i = 0; i < myItems.length; i++) {
						Item item = myItems[i];
						int width = item.itemWidth;
						int height = item.itemHeight;
						int maxColumnWidth = maxColumnWidths[ columnIndex ];
						//#if polish.css.colspan
							//# if (item.colSpan == 1) {
						//#endif
								if ( maxColumnWidth <= availableColumnWidth) {
									newMaxColumnWidths[ columnIndex ] = maxColumnWidths[ columnIndex ];
								} else {
									// re-initialise this item,
									// if it is wider than the left-available-column-width
									if ( width > leftAvailableColumnWidth ) {
										width = item.getItemWidth( leftAvailableColumnWidth, leftAvailableColumnWidth );
										height = item.getItemHeight( leftAvailableColumnWidth, leftAvailableColumnWidth );
									}
									if (width > newMaxColumnWidths[ columnIndex ]) {
										newMaxColumnWidths[ columnIndex ] = width;
									}
								}
						//#if polish.css.colspan
							//# }
							//# columnIndex += item.colSpan;
						//#else
							columnIndex++;
						//#endif
						if (height > maxRowHeight) {
							maxRowHeight = height;
						}
						item.relativeY = myContentHeight;
						//System.out.println( i + ": yTopPos=" + item.yTopPos );
						if (columnIndex == this.numberOfColumns) {
							//System.out.println("starting new row: rowIndex=" + rowIndex + "  numberOfRows: " + numberOfRows);
							columnIndex = 0;
							this.rowsHeights[rowIndex] = maxRowHeight;
							myContentHeight += maxRowHeight + this.paddingVertical;
							maxRowHeight = 0;
							rowIndex++;
						}
					} // for each item		
					this.columnsWidths = newMaxColumnWidths;
				}
				// this seems to be not required, 2008-05-15:
				//if (columnIndex != 0) {
					// last row is not completely filled.
					//myContentHeight += maxRowHeight;
				//}
			} else if (this.columnsSetting == EQUAL_WIDTH_COLUMNS) {
				// Use the maximum used column-width for each column,
				// unless this table should be expanded, in which
				// case the above set widths  will be used instead.
				if (!isLayoutExpand()) {
					for (int i = 0; i < this.columnsWidths.length; i++) {
						this.columnsWidths[i] = maxWidth;
					}
				}
			} // otherwise the column widths are defined statically.
			// set content height & width:
			int myContentWidth = 0;
			columnIndex = 0;			
			for (int i = 0; i < myItems.length; i++) {
				Item item = myItems[i];
				item.relativeX = myContentWidth;
				myContentWidth += this.columnsWidths[columnIndex] + this.paddingHorizontal;
				//#if polish.css.colspan
					//# columnIndex += item.colSpan;
				//#else
					columnIndex++;
				//#endif
				if (columnIndex == this.numberOfColumns) {
					columnIndex = 0;
					myContentWidth = 0;
				}
			}
			myContentWidth = 0;
			for (int i = 0; i < this.columnsWidths.length; i++) {
				myContentWidth += this.columnsWidths[i] + this.paddingHorizontal;
			}
			this.isVertical = this.numberOfRows > 1;
			this.contentWidth = myContentWidth;
			this.contentHeight = myContentHeight;
			
//			System.out.println("ContainerView.initContent(): contentWidth=" + this.contentWidth + ", focusedIndex=" + this.focusedIndex);

			
		//#endif
	}
		
	/**
	 * Determines whether this view should be expanded horizontally
	 * 
	 * @return true when this view should be expanded horizontally
	 * @see #layout
	 * @see Item#LAYOUT_EXPAND
	 */
	protected boolean isLayoutExpand() {
		return ((this.layout & Item.LAYOUT_EXPAND) == Item.LAYOUT_EXPAND);
	}

	/**
	 * Paints the content of this container view.
	 * This method adjusts the x and y offsets and forwards the call to paintContent(Container, Item[], int, int, int, int, int, int, int, int, Graphics)
	 * 
	 * @param parent the parent item
	 * @param x the left start position
	 * @param y the upper start position
	 * @param leftBorder the left border, nothing must be painted left of this position
	 * @param rightBorder the right border, nothing must be painted right of this position
	 * @param g the Graphics on which this item should be painted.
	 * @see #paintContent(Container, Item[], int, int, int, int, int, int, int, int, Graphics)
	 */
	protected void paintContent( Item parent, int x, int y, int leftBorder, int rightBorder, Graphics g ) {
		//System.out.println("ContainerView: painting content for " + this + " with vertical-padding " + this.paddingVertical  + ", screen=" + this.parentContainer.getScreen());
		
		//#if polish.css.view-type-top-y-offset
			//# y += this.topYOffset;
		//#endif
		//#if polish.css.view-type-left-x-offset
			//# x += this.leftXOffset;
			//# leftBorder += this.leftXOffset;
		//#endif
		//#if polish.css.view-type-right-x-offset
			//# rightBorder -= this.rightXOffset;
		//#endif
		//System.out.println("ContainerView.paint(): width=" + (rightBorder - leftBorder ) + ", firstLineWidth=" + (rightBorder - x) + ", contentWidth=" + this.contentWidth + ", parentContentWidth=" + this.parentContainer.contentWidth );
		
		Item[] myItems = this.parentContainer.getItems();
		
		paintContent( this.parentContainer, myItems, x, y, leftBorder, rightBorder, g.getClipX(), g.getClipY(), g.getClipWidth(), g.getClipHeight(), g);
		
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
		int focusedLeftBorder = leftBorder;
		int focusedRightBorder = rightBorder;
		//#ifdef tmp.useTable
			if (this.columnsSetting == NO_COLUMNS || myItems.length == 1) {
		//#endif
				for (int i = 0; i < myItems.length; i++) {
					if (i != this.focusedIndex) {
						Item item = myItems[i];
						//System.out.println("item " + i + " at " + item.relativeX + "/" + item.relativeY);
						int itemX = x + item.relativeX;
						int itemY = y + item.relativeY;
						paintItem(item, i, itemX, itemY, leftBorder, rightBorder, clipX, clipY, clipWidth, clipHeight, g);
					}
				}
		//#ifdef tmp.useTable
			} else {
				int columnIndex = 0;
				//int rowIndex = 0;
				for (int i = 0; i < myItems.length; i++) {
					Item item = myItems[i];
					int itemX = x + item.relativeX;
					int itemY = y + item.relativeY;
					int columnWidth = this.columnsWidths[ columnIndex ];
					//#if polish.css.colspan
						//# if (item.colSpan > 1) {						
							//# for (int j = columnIndex + 1; j < columnIndex + item.colSpan; j++) {
								//# columnWidth += this.columnsWidths[ j ] + this.paddingHorizontal;
							//# }
							//# //System.out.println("Painting item " + i + "/" + item + " with width=" + item.itemWidth + " and with increased colwidth of " + columnWidth + " (prev=" + this.columnsWidths[ columnIndex ] + ")" );
						//# }
					//#endif
					leftBorder = itemX;
					rightBorder = leftBorder + columnWidth;
					if (i == this.focusedIndex) {
						focusedLeftBorder = leftBorder;
						focusedRightBorder = rightBorder;
					} else {
						//System.out.println("item " + i + " at " + item.relativeX + "/" + item.relativeY);
						paintItem(item, i, itemX, itemY, leftBorder, rightBorder, clipX, clipY, clipWidth, clipHeight, g);
					} 
					//System.out.println("painting item " + item + " at x=" + itemX + ", leftBorder=" + leftBorder + ", rightBorder=" + rightBorder + ", relativeX=" + item.relativeX );
					//#if polish.css.colspan
						//# columnIndex += item.colSpan;
					//#else
						columnIndex++;
					//#endif
		
					if (columnIndex == this.numberOfColumns) {
						columnIndex = 0;
					}
				}				
			}
		//#endif
		// paint focused item last:
		if (this.focusedItem != null) {
			paintItem(this.focusedItem, this.focusedIndex, x + this.focusedItem.relativeX, y + this.focusedItem.relativeY, focusedLeftBorder, focusedRightBorder, clipX, clipY, clipWidth, clipHeight, g);
		}
		
//		int focusedX = x;
//		int focusedY = 0;
//		int focusedRightBorder = rightBorder;
//		//#ifdef tmp.useTable
//			if (this.columnsSetting == NO_COLUMNS || myItems.length == 1) {
//		//#endif
//				if (!(this.isLayoutCenter || this.isLayoutRight)) {
//					// adjust the right border:
//					//rightBorder = leftBorder + this.contentWidth;
//				}
//				for (int i = 0; i < myItems.length; i++) {
//					Item item = myItems[i];
//					// currently the NEWLINE_AFTER and NEWLINE_BEFORE layouts will be ignored,
//					// since after every item a line break will be done.
//					if (i == this.focusedIndex) {
//						focusedY = y;
//						item.getItemHeight( rightBorder - x, rightBorder - leftBorder );
//					} else {
//						// the currently focused item is painted last
//						paintItem(item, i, x, y, leftBorder, rightBorder, clipX, clipY, clipWidth, clipHeight, g);
//						//item.paint(x, y, leftBorder, rightBorder, g);
//					}
//					y += item.itemHeight + this.paddingVertical;
//				}
//		//#ifdef tmp.useTable
//			} else {
//				x = leftBorder;
//				int columnIndex = 0;
//				int rowIndex = 0;
//				for (int i = 0; i < myItems.length; i++) {
//					Item item = myItems[i];
//					int columnWidth = this.columnsWidths[ columnIndex ];
//					//#if polish.css.colspan
//						if (item.colSpan > 1) {						
//							for (int j = columnIndex + 1; j < columnIndex + item.colSpan; j++) {
//								columnWidth += this.columnsWidths[ j ] + this.paddingHorizontal;
//							}
//							//System.out.println("Painting item " + i + "/" + item + " with width=" + item.itemWidth + " and with increased colwidth of " + columnWidth + " (prev=" + this.columnsWidths[ columnIndex ] + ")" );
//							
//						}
//					//#endif
//					//TODO this is a quick hack so that pointer events work - normally the item.relativeX position should be set in initContent! 
//					item.relativeX = x - leftBorder;
//					if (i == this.focusedIndex) {
//						focusedY = y;
//						focusedX = x;
//						focusedRightBorder = x + columnWidth;
//						//System.out.println("focusedItem in table: x=" + focusedX + ", focusedRightBorder=" + focusedRightBorder + ", rightBorder=" + rightBorder +  ", isInitialized=" + this.focusedItem.isInitialised +  ", itemWidth=" + item.getItemWidth( columnWidth, columnWidth ));
//						// item.getItemHeight( columnWidth, columnWidth );
//					} else {
//						paintItem( item, i, x, y, x, x + columnWidth, clipX, clipY, clipWidth, clipHeight,  g );
//						// item.paint(x, y, x, x + columnWidth, g);
//					}
//					x += columnWidth + this.paddingHorizontal;
//					//#if polish.css.colspan
//						columnIndex += item.colSpan;
//					//#else
//						columnIndex++;
//					//#endif
//			
//					if (columnIndex == this.numberOfColumns) {
//						columnIndex = 0;
//						y += this.rowsHeights[ rowIndex ] + this.paddingVertical;
//						x = leftBorder;
//						rowIndex++;
//					}
//				}
//			}
//		//#endif
//		
//		// paint the currently focused item:
//		if (this.focusedItem != null) {
//			//System.out.println("Painting focusedItem " + this.focusedItem + " with width=" + this.focusedItem.itemWidth + " and with increased colwidth of " + (focusedRightBorder - focusedX)  );
//			paintItem( this.focusedItem, this.focusedIndex, focusedX, focusedY, focusedX, focusedRightBorder, clipX, clipY, clipWidth, clipHeight, g);
//			//this.focusedItem.paint(focusedX, focusedY, focusedX, focusedRightBorder, g);
//		}
	}

	/**
	 * Paints this item at the specified position.
	 * Subclasses can override this method for taking advantage of the table support of the basic ContainerView class. 
	 * When the item is outside of the given clipping area, it will not be painted.
	 *  
	 * @param item the item that needs to be painted
	 * @param index the index of the item
	 * @param x the horizontal position of the item
	 * @param y the vertical position of the item
	 * @param leftBorder the left border
	 * @param rightBorder the right border
	 * @param clipX absolute horizontal clipping start
	 * @param clipY absolute verical clipping start
	 * @param clipWidth clipping width
	 * @param clipHeight clipping height
	 * @param g the graphics context
	 */
	protected void paintItem( Item item, int index,  int x, int y, int leftBorder, int rightBorder, int clipX, int clipY, int clipWidth, int clipHeight, Graphics g ) {
		//#debug
		//# System.out.println("ContainerView: painting item at (" +  x + ", " + y + ") " + item );
		if ( ( (y < clipY + clipHeight && y + item.itemHeight > clipY)
			|| (item.internalX != Item.NO_POSITION_SET && y + item.internalY < clipY + clipHeight && y + item.internalY + item.internalHeight > clipY ) )
			&& (x < clipX + clipWidth && x + item.itemWidth > clipX) ) 
		{
			item.paint(x, y, leftBorder, rightBorder, g);
//		} else {
//			System.out.println("clipY=" + clipY + ", clipHeight=" + clipHeight + ", y=" + y + ", item.internalY=" + item.internalY + ", item.internalHeight=" + item.internalHeight + ", item.internalX=" + item.internalX);
		}
	} 

	/**
	 * Interpretes the given user-input and retrieves the nexte item which should be focused.
	 * Please not that the focusItem()-method is not called as well. The
	 * view is responsible for updating its internal configuration here as well.
	 * 
	 * @param keyCode the code of the keyPressed-events
	 * @param gameAction the associated game-action to the given keyCode
	 * @return the next item which will be focused, null when there is
	 * 			no such element.
	 */
	protected Item getNextItem( int keyCode, int gameAction ) 
	{
//		System.out.println("getNextItem for "+ getScreen().getKeyName( keyCode ));
		Item[] myItems = this.parentContainer.getItems();
		if ( 
				//#if polish.blackberry && !polish.hasTrackballEvents
					//# (gameAction == Canvas.RIGHT  && keyCode != Canvas.KEY_NUM6) ||
				//#else
					( this.isHorizontal && gameAction == Canvas.RIGHT  && keyCode != Canvas.KEY_NUM6) || 
				//#endif
				( this.isVertical   && gameAction == Canvas.DOWN   && keyCode != Canvas.KEY_NUM8)) 
		{
			//#if polish.css.view-type-sequential-traversal
				//# if (!this.isSequentialTraversal) {
			//#endif
					if (gameAction == Canvas.DOWN && this.columnsSetting != NO_COLUMNS) {
						return shiftFocus( true, this.numberOfColumns - 1, myItems );
					}
			//#if polish.css.view-type-sequential-traversal
				//# }
			//#endif
			return shiftFocus( true, 0, myItems );
			
		} else if ( 
				//#if polish.blackberry && !polish.hasTrackballEvents
					//# (gameAction == Canvas.LEFT  && keyCode != Canvas.KEY_NUM4) ||
				//#else
					(this.isHorizontal && gameAction == Canvas.LEFT  && keyCode != Canvas.KEY_NUM4) || 
				//#endif
				(this.isVertical && gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM2) ) 
		{
			//#if polish.css.view-type-sequential-traversal
				//# if (!this.isSequentialTraversal) {
			//#endif
					if (gameAction == Canvas.UP && this.columnsSetting != NO_COLUMNS) {
						return shiftFocus( false,  -(this.numberOfColumns -1 ), myItems);
					}
			//#if polish.css.view-type-sequential-traversal
				//# }
			//#endif
			return shiftFocus( false, 0, myItems );
		}
		
//		System.out.println("getNextItem: returning null for " + getScreen().getKeyName( keyCode )	);
		
		return null;
		
	}
	
	/**
	 * Shifts the focus to the next or the previous item.
	 * 
	 * @param forwardFocus true when the next item should be focused, false when
	 * 		  the previous item should be focused.
	 * @param steps how many steps forward or backward the search for the next focusable item should be started,
	 *        0 for the current item, negative values go backwards.
	 * @param items the items of this view
	 * @return the item that has been focused or null, when no item has been focused.
	 */
	protected Item shiftFocus(boolean forwardFocus, int steps, Item[] items) {
		//#debug
		//# System.out.println("ContainerView.shiftFocus( forward=" + forwardFocus + ", steps=" + steps + ", focusedIndex=" + this.focusedIndex + ")" );
//		System.out.println("parent.focusedIndex=" + this.parentContainer.getFocusedIndex() );
		boolean allowCycle = this.allowCycling;
		if (!allowCycle && forwardFocus && steps != 0 && isInBottomRow(this.focusedIndex) ) {
			return null;
		}
		int i;
		//#if polish.css.colspan
			//# i = this.focusedIndex;
			//# if ( i != -1 && steps != 0) {
				//# //System.out.println("ShiftFocus: steps=" + steps + ", forward=" + forwardFocus);
				//# int doneSteps = 0;
				//# steps = Math.abs( steps ) + 1;
				//# Item item = items[i]; //(Item) this.parentContainer.itemsList.get(i);
				//# while( doneSteps <= steps ) {
					//# doneSteps += item.colSpan;
					//# if (doneSteps >= steps) {
//# //						System.out.println("bailing out at too many steps: focusedIndex=" + this.focusedIndex + ", startIndex=" + i + ", steps=" + steps + ", doneSteps=" + doneSteps);
						//# break;
					//# }
					//# if (forwardFocus) {
						//# i++;
						//# if (i == items.length - 1 ) {
							//# if (!allowCycle) {
								//# return null;
							//# }
//# //								System.out.println("reached items.length -1, breaking at -2");
							//# i = items.length - 2;
							//# break;
						//# } else if (i >= items.length) {
							//# if (!allowCycle) {
								//# return null;
							//# }
//# //								System.out.println("reached items.length, breaking at -1");
							//# i = items.length - 1;
							//# break;
						//# }
					//# } else {
						//# i--; 
						//# if (i < 0) {
							//# i = 1;
							//# break;
						//# }
					//# }
					//# item = items[i];
//# //					System.out.println("focusedIndex=" + this.focusedIndex + ", startIndex=" + i + ", steps=" + steps + ", doneSteps=" + doneSteps);
				//# }
//# //				System.out.println("item is now " + i + ": " + item);
				//# if (doneSteps >= steps && item.colSpan != 1 && i != this.focusedIndex)  {
					//# if (forwardFocus) {
						//# i--;
						//# if (i < 0) {
							//# i = items.length + i;
						//# }
//# //						System.out.println("forward: Adjusting startIndex to " + i );
					//# } else {
						//# i = (i + 1) % items.length;
//# //						System.out.println("backward: Adjusting startIndex to " + i );
					//# }
				//# }
			//# }
		//#else
			i = this.focusedIndex + steps;
			if (steps != 0) {
				if (!forwardFocus) {
					if (i < 0) {
						if (!allowCycle) {
							return null;
						}
						i = items.length + i;
					}
					//System.out.println("forward: Adjusting startIndex to " + i );
				} else {
					i = i % items.length;
					if ( i >= items.length) {
						if (!allowCycle) {
							return null;
						}
						i -= items.length;
					}
					//System.out.println("backward: Adjusting startIndex to " + i );
				}
			}
		//#endif
		//TODO check how to integrate scrolling-cycling-coordination in containerviews
	//	//#if polish.Container.allowCycling != false
	//		boolean allowCycle = this.enableScrolling && this.allowCycling;
	//		if (allowCycle) {
	//			//#if polish.css.scroll-mode
	//				if (!this.scrollSmooth) {
	//					if (forwardFocus) {
	//						// when you scroll to the bottom and
	//						// there is still space, do
	//						// scroll first before cycling to the
	//						// first item:
	//						allowCycle = (this.yOffset + this.itemHeight <= this.yBottom);
	//					} else {
	//						// when you scroll to the top and
	//						// there is still space, do
	//						// scroll first before cycling to the
	//						// last item:
	//						allowCycle = (this.yOffset == 0);
	//					}						
	//				} else {
	//			//#endif
	//				if (forwardFocus) {
	//					// when you scroll to the bottom and
	//					// there is still space, do
	//					// scroll first before cycling to the
	//					// first item:
	//					allowCycle = (this.targetYOffset + this.itemHeight <= this.yBottom);
	//				} else {
	//					// when you scroll to the top and
	//					// there is still space, do
	//					// scroll first before cycling to the
	//					// last item:
	//					allowCycle = (this.targetYOffset == 0);
	//				}
	//			//#if polish.css.scroll-mode
	//				}
	//			//#endif
	//			//#debug
	//			System.out.println("shiftFocus: allowCycl=" + allowCycle + ", isFoward=" + forwardFocus + ", targetYOffset=" + this.targetYOffset + ", yOffset=" + this.yOffset );	
	//		}
	//	//#endif
		Item nextItem = null;
		//System.out.println("shifting focus - allowCycle=" + allowCycle + ", this.allowCycling=" + this.allowCycling + ", parent.allowCycling=" + this.parentContainer.allowCycling);
		//System.out.println("starting at i=" + i + ", focusedIndex=" + this.focusedIndex + ", steps=" + steps + ", allowCycle=" + allowCycle) ;
		while (true) {
			if (forwardFocus) {
				i++;
				if (i >= items.length) {
					//#if polish.Container.allowCycling != false
						if (allowCycle) {
							allowCycle = false;
							i = 0;
						} else {
							break;
						}
					//#else
						//# break;
					//#endif
				}
			} else {
				i--;
				if (i < 0) {
					//#if polish.Container.allowCycling != false
						if (allowCycle) {
							allowCycle = false;
							i = items.length - 1;
						} else {
							break;
						}
					//#else
						//# break;
					//#endif
				}
			}
			//System.out.println("now at i=" + i + ", focusedIndex=" + this.focusedIndex);
			nextItem = items[i];
			if (nextItem.appearanceMode != Item.PLAIN) {
				break;
			}
		}
		if (nextItem == null || nextItem.appearanceMode == Item.PLAIN || nextItem == this.focusedItem) {
			//System.out.println("returning null: allowCycling=" + this.parentContainer.allowCycling + ", local-cycle=" + allowCycle);
			return null;
		}
//		int direction = Canvas.UP;
//		if (forwardFocus) {
//			direction = Canvas.DOWN;
//		}
		Screen screen = getScreen();
		Item focItem = this.focusedItem;
		if (screen != null && focItem != null 
				&& i > this.focusedIndex 
				&& (nextItem.relativeY - focItem.relativeY + (focItem.relativeY + this.parentContainer.getRelativeScrollYOffset()) > screen.contentHeight )
		) {
			// scroll before shifting focus
			return null;
		}
				
		focusItem(i, nextItem );
		return nextItem;
	}

	/**
	 * Detects if the specified item index is within the last row of this view.
	 * @param index the item's index
	 * @return true when the item is in the last row of this view. When no columns are used, this will be only true for the last item.
	 */
	protected boolean isInBottomRow(int index)
	{
		//#ifdef tmp.useTable
			if (this.columnsSetting == NO_COLUMNS || this.parentContainer.size() <= 1) {
		//#endif
				return (index == this.parentContainer.size() -1 ); 
		//#ifdef tmp.useTable
			} else {
				//#if polish.css.colspan
					//# int adjustedIndex = 0;
//# 					
					//# for (int i=0; i < index; i++) {
						//# Item item = this.parentContainer.get(i);
						//# adjustedIndex += item.colSpan;
					//# }
					//# index = adjustedIndex;
				//#endif
				int row = index / this.numberOfColumns;
				//System.out.println("index=" + index + ", row=" + row + ", numberOfRows=" + this.numberOfRows);
				return (row == this.numberOfRows - 1);
				
			}
		//#endif
	}

	/**
	 * Focuses the item with the given index.
	 * The container will then set the style of the 
	 * retrieved item. The default implementation just
	 * sets the internal focusedIndex field along with focusedItem. 
	 * When this method is overwritten, please do call super.focusItem first
	 * or set the fields "focusedIndex" and "focusedItem" yourself.
	 * This method figures out the direction and calls focusItem( index, item, direction )
	 * 
	 * @param index the index of the item
	 * @param item the item which should be focused
	 * @see #focusItem(int, Item, int)
	 */
	protected void focusItem( int index, Item item  ) {
		int direction = 0;
		if (this.focusedIndex < index ) {
			direction = Canvas.DOWN;
		} else if (this.focusedIndex == index ) {
			direction = 0;
		} else {
			direction = Canvas.UP;
		}
		focusItem( index, item, direction );
	}
	
	/**
	 * Focuses the item with the given index.
	 * The container will then set the style of the 
	 * retrieved item. The default implementation just
	 * sets the internal focusedIndex field along with focusedItem. 
	 * When this method is overwritten, please do call super.focusItem first
	 * or set the fields "focusedIndex" and "focusedItem" yourself.
	 * 
	 * @param index the index of the item
	 * @param item the item which should be focused
	 * @param direction the direction, either Canvas.DOWN, Canvas.RIGHT, Canvas.UP, Canvas.LEFT or 0.
	 */
	protected void focusItem( int index, Item item, int direction  ) {
		this.parentContainer.focus(index, item, direction );
	}

//	/**
//	 * Sets the focus to this container view.
//	 * The default implementation sets the style and the field "isFocused" to true.
//	 * 
//	 * @param focusstyle the appropriate style.
//	 * @param direction the direction from the which the focus is gained, 
//	 *        either Canvas.UP, Canvas.DOWN, Canvas.LEFT, Canvas.RIGHT or 0.
//	 *        When 0 is given, the direction is unknown.1
//	 * 
//	 */
//	public void focus(Style focusstyle, int direction) {
//		this.isFocused = true;
//		setStyle( focusstyle );
//	}
//
//	
//	/**
//	 * Notifies this view that the parent container is not focused anymore.
//	 * Please call super.defocus() when overriding this method.
//	 * The default implementation calls setStyle( originalStyle )
//	 * and sets the field "isFocused" to false.
//	 * 
//	 * @param originalStyle the previous used style, may be null
//	 */
//	protected void defocus( Style originalStyle ) {
//		this.isFocused = false;
//		if (originalStyle != null) {
//			setStyle( originalStyle );
//		}
//	}
	
	/**
	 * Sets the style for this view.
	 * The style can include additional parameters for the view.
	 * Subclasses should call super.setStyle(style) first.
	 * 
	 * @param style the style
	 */
	protected void setStyle( Style style ) {
		//#debug
		//# System.out.println("Setting style for " + this + " with vertical padding=" + style.paddingVertical  );
		super.setStyle( style );
		//this.columnsSetting = NO_COLUMNS;
		//#ifdef polish.css.columns
			Integer columns = style.getIntProperty(4);
			if (columns != null) {
				this.numberOfColumns = columns.intValue();
				//System.out.println("ContainerView: supporting " + this.numberOfColumns + " cols");
				this.columnsSetting = NORMAL_WIDTH_COLUMNS;
				//#ifdef polish.css.columns-width
					String width = style.getProperty(5);
					if (width != null) {
						if ("equal".equals(width)) {
							this.columnsSetting = EQUAL_WIDTH_COLUMNS;
						} else if ("normal".equals(width)) {
							//this.columnsSetting = NORMAL_WIDTH_COLUMNS;
							// this is the default value set above...
						} else {
							// these are pixel settings.
							String[] widths = TextUtil.split( width, ',');
							if (widths.length != this.numberOfColumns) {
								// this is an invalid setting! 
								this.columnsSetting = NORMAL_WIDTH_COLUMNS;
								//#debug warn
								//# System.out.println("Container: Invalid [columns-width] setting: [" + width + "], the number of widths needs to be the same as with [columns] specified.");
							} else {
								this.columnsSetting = STATIC_WIDTH_COLUMNS;
								this.columnsWidths = new int[ this.numberOfColumns ];
								//#ifdef polish.css.columns-width.star
									//# this.starIndex = -1;
								//#endif
								for (int i = 0; i < widths.length; i++) {
									//#ifdef polish.css.columns-width.star
										//# String widthStr = widths[i];
										//# if ("*".equals( widthStr )) {
											//# if (this.starIndex != -1) {												
												//#debug error
												//# System.out.println("Container: Invalid [columns-width] setting: [" + width + "], only one * can be used!");
												//# this.columnsWidths[i] = 30;
											//# } else {
												//# this.starIndex = i;
												//# this.columnsWidths[i] = 0;
											//# }
										//# } else {
											//# int w = Integer.parseInt( widthStr );
											//# this.columnsWidths[i] = w;
										//# }
									//#else
										this.columnsWidths[i] = Integer.parseInt( widths[i] );
									//#endif
								}
								this.columnsSetting = STATIC_WIDTH_COLUMNS;
							}					
						}
					}
				//#endif
				//TODO rob allow definition of the "fill-policy"
			}
		//#endif
		//#if polish.css.view-type-left-x-offset
			//# Integer leftXOffsetInt = style.getIntProperty(108);
			//# if (leftXOffsetInt != null) {
				//# this.leftXOffset = leftXOffsetInt.intValue();
			//# }
		//#endif
		//#if polish.css.view-type-right-x-offset
			//# Integer rightXOffsetInt = style.getIntProperty(109);
			//# if (rightXOffsetInt != null) {
				//# this.rightXOffset = rightXOffsetInt.intValue();
			//# }
		//#endif
		//#if polish.css.view-type-top-y-offset
			//# Integer topYOffsetInt = style.getIntProperty(110);
			//# if (topYOffsetInt != null) {
				//# this.topYOffset = topYOffsetInt.intValue();
			//# }
		//#endif
		//#if polish.css.view-type-sequential-traversal
			//# Boolean sequentialTraversalBool = style.getBooleanProperty(308);
			//# if (sequentialTraversalBool != null) {
				//# this.isSequentialTraversal = sequentialTraversalBool.booleanValue();
			//# }
		//#endif
		//#if polish.css.expand-items
			//# Boolean expandItemsBool = style.getBooleanProperty(309);
			//# if (expandItemsBool != null) {
				//# this.isExpandItems = expandItemsBool.booleanValue();
			//# }
		//#endif
	}
	

	
	/**
	 * Retrieves the next focusable item.
	 * This helper method can be called by view-implementations.
	 * The index of the currently focused item can be retrieved with the focusedIndex-field.
	 * 
	 * @param items the available items
	 * @param forward true when a following item should be looked for,
	 *        false if a previous item should be looked for.
	 * @param steps the number of steps which should be used (e.g. 2 in a table with two columns)
	 * @param allowCircle true when either the first focusable or the last focusable element
	 *        should be returned when there is no focusable item in the given direction.
	 * @return either the next focusable item or null when there is no such element
	 * @see #focusItem(int, Item)
	 */
	protected Item getNextFocusableItem( final Item[] items, final boolean forward, int steps, boolean allowCircle ) {
		int i = this.focusedIndex;
		boolean isInLoop;
		while  ( true ) {
			if (forward) {
				i += steps;
				isInLoop = i < items.length;
				if (!isInLoop) {
					if (steps > 1) {
						i = items.length - 1;
						isInLoop = true;
					} else if (allowCircle) {
						steps = 1;
						allowCircle = false;
						i = 0;
						isInLoop = true;
					}
				}
			} else {
				i -= steps;
				isInLoop = i >= 0;
				if (!isInLoop) {
					if (steps > 1) {
						i = 0;
						isInLoop = true;
					} else if (allowCircle) {
						steps = 1;
						allowCircle = false;
						i = items.length - 1;
						isInLoop = true;
					}
				}
			}
			if (isInLoop) {
				Item item = items[i];
				if (item.appearanceMode != Item.PLAIN) {
					this.focusedIndex = i;
					return item;
				}
			} else {
				break;
			}
		}
		return null;
	}
	

	/**
	 * Notifies this view that it is about to be shown (again).
	 * The default implementation just sets the restartAnimation-field to true.
	 */
	public void showNotify() {
		this.restartAnimation = true;
		super.showNotify();
	}
	
	
	/**
	 * Retrieves the screen to which this view belongs to.
	 * This is necessary since the getScreen()-method of item has only protected
	 * access. The screen can be useful for setting the title for example. 
	 * 
	 * @return the screen in which this view is embedded.
	 */
	protected Screen getScreen() {
		return this.parentContainer.getScreen();
	}
	
	/**
	 * Handles the given keyPressed event when the currently focused item was not able to handle it.
	 * The default implementation just calls getNextItem() and focuses the returned item.
	 * 
	 * @param keyCode the key code
	 * @param gameAction the game action like Canvas.UP etc
	 * @return true when the key was handled.
	 */
	public boolean handleKeyPressed( int keyCode, int gameAction) {
		//#debug
		//# System.out.println("ContainerView.handleKeyPressed() of container " + this);
		Item item = getNextItem( keyCode, gameAction );
		if (item != null) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Adjusts the yOffset or the targetYOffset so that the given relative values are inside of the visible area.
	 * The call is ignored when scrolling is not enabled for this item.
	 * 
	 * @param direction the direction, is used for adjusting the scrolling when the internal area is to large. Either 0 or Canvas.UP, Canvas.DOWN, Canvas.LEFT or Canvas.RIGHT
	 * @param x the horizontal position of the area relative to this content's left edge, is ignored in the current version
	 * @param y the vertical position of the area relative to this content's top edge
	 * @param width the width of the area
	 * @param height the height of the area
	 */
	protected void scroll( int direction, int x, int y, int width, int height ){
		Container container = this.parentContainer;
		while (!container.enableScrolling) {
			Item item = container.parent;
			if (item instanceof Container) {
				x += container.relativeX;
				y += container.relativeY;
				container = (Container) item;
			} else {
				break;
			}
		} 
		if (container.enableScrolling) {
			container.scroll( direction, x, y, width, height );
		}
	}
	
	protected int getParentRelativeY(){
		return this.parentContainer.relativeY;
	}
	
	protected int getItemRelativeY( Item item ){
		return item.relativeY;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ItemView#isValid(de.enough.polish.ui.Item, de.enough.polish.ui.Style)
	 */
	protected boolean isValid(Item parent, Style style) {
		return (parent instanceof Container);
	}

	/**
	 * Focuses the given item and retrieves the previous style of that item.
	 * The default implementation sets the focusedIndex and focusedItem fields
	 * and returns the result of item.focus( focusStyle, direction ).
	 * This is a method that is usually called from within the parent Container (in contrast to the other focusItem() methods which forward the call to the parentContainer).
	 * 
	 * @param index the index of the item
	 * @param item the item which should be focused
	 * @param direction the direction, either Canvas.DOWN, Canvas.RIGHT, Canvas.UP, Canvas.LEFT or 0.
	 * @param focusedStyle the new style for the focused item
	 * @return the previous style of the focussed item
	 */
	public Style focusItem(int index, Item item, int direction, Style focusedStyle) {
//		System.out.println("focusItem: index=" + index);
		this.focusedIndex = index;
		this.focusedItem = item;
		return item.focus(focusedStyle, direction);
	}


}