//#condition polish.usePolishGui
/*
 * Created on Apr 10, 2008 at 2:10:53 AM.
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
package de.enough.polish.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import de.enough.polish.util.TableData;

/**
 * <p>Allows to manage items in a dynamic tabular layout</p>
 *
 * <p>Copyright Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class TableItem 
//#if polish.LibraryBuild
	//# extends FakeContainerCustomItem
//#else
	extends Container
//#endif

{
	/**
	 * Selection mode for a table in which no cells can be selected.
	 */
	public static final int SELECTION_MODE_NONE = 0;
	/**
	 * Selection mode for a table in which any cell can be selected, even empty ones.
	 */
	public static final int SELECTION_MODE_CELL = 2;
	/**
	 * Selection mode for a table in which cells/colummns/rows with content can be selected.
	 */
	public static final int SELECTION_MODE_NONEMPTY = 4;
	/**
	 * Selection mode for a table in which cells/columns/rows with interactive items (like TextField) can be selected.
	 */
	public static final int SELECTION_MODE_INTERACTIVE = 8;
	/**
	 * Selection mode for a table in which rows can be selected.
	 */
	public static final int SELECTION_MODE_ROW = 16;
	/**
	 * Selection mode for a table in which columns can be selected.
	 */
	public static final int SELECTION_MODE_COLUMN = 32;
	
	/** default style for lines between cells */
	public static int LINE_STYLE_SOLID = 0;
	/** dotted style for lines between cells */
	public static int LINE_STYLE_DOTTED = 2;
	/** no lines between cells */
	public static int LINE_STYLE_INVISIBLE = 2;
	
	protected TableData tableData;
	protected Font font;
	protected int fontColor;
	protected int[] rowHeights;
	protected int[] columnWidths;
	
	protected int lineStroke;
	protected int lineColor = -1;
	protected int completeWidth;
	
	protected int xOffset;
	protected int targetXOffset;
	
	protected int selectionMode = SELECTION_MODE_NONE;
	protected Background selectedBackground;
	protected Background selectedRowBackground;
	protected Background selectedColumnBackground;
	private int selectedRowIndex = -1;
	private int selectedColumnIndex = -1;
	private Style selectedItemStyle;
	private int currentColumnIndex = -1;
	private int currentRowIndex = -1;
	private Style cellContainerStyle;
	
	private final Object paintLock = new Object();
	

	/**
	 * Creates a new TableItem with an empty table data.
	 */
	public TableItem()
	{
		this(null, null);
	}

	/**
	 * Creates a new TableItem with an empty table data.
	 * @param style style of this item
	 */
	public TableItem(Style style)
	{
		this( null, null );
	}
	
	/**
	 * Creates a new TableItem with the specified dimensions
	 * 
	 * @param columns the number of columns 
	 * @param rows  the number of rows
	 */
	public TableItem(int columns, int rows)
	{
		this( columns, rows, null );
	}

	
	/**
	 * Creates a new TableItem with the specified dimensions
	 * 
	 * @param columns the number of columns 
	 * @param rows  the number of rows
	 * @param style the style of this item
	 */
	public TableItem(int columns, int rows, Style style)
	{
		this( new TableData( columns, rows), style );
	}

	
	/**
	 * Creates a new TableItem with an empty table data.
	 * @param tableData the data to be displayed in this table
	 */
	public TableItem(TableData tableData )
	{
		this(tableData, null);
	}
	
	/**
	 * Creates a new TableItem with an empty table data.
	 * @param tableData the data to be displayed in this table
	 * @param style style of this item
	 */
	public TableItem(TableData tableData, Style style )
	{
		super( false, style );
		this.tableData = tableData;
	}
	

	/**
	 * Sets the table data for this item
	 * @param tableData the new table data
	 */
	public void setTableData( TableData tableData ) {
		this.tableData = tableData;
		requestInit();
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#initContent(int, int)
	 */
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#setStyle(de.enough.polish.ui.Style)
	 */
	public void setStyle(Style style)
	{
		super.setStyle(style);
		this.font = style.font;
		this.fontColor = style.getFontColor();
		if (this.lineColor == -1) {
			this.lineColor = this.fontColor;
		}
		//#if polish.css.table-line-color
			//# Color lineColorObj = style.getColorProperty(322);
			//# if (lineColorObj != null) {
				//# this.lineColor = lineColorObj.getColor();
			//# }
		//#endif
		//#if polish.css.table-line-stroke
			//# Integer strokeObj = style.getIntProperty(323);
			//# if (strokeObj != null) {
				//# this.lineStroke = strokeObj.intValue();
			//# }
		//#endif
		//#if polish.css.table-selected-background
			//# Background sBackground = (Background) style.getObjectProperty(332);
			//# if (sBackground != null) {
				//# setSelectedBackground(sBackground);
			//# }
		//#endif
		//#if polish.css.table-selected-row-background
			//# Background srBackground = (Background) style.getObjectProperty(333);
			//# if (srBackground != null) {
				//# this.selectedRowBackground = srBackground;
			//# }
		//#endif
		//#if polish.css.table-selected-column-background
			//# Background scBackground = (Background) style.getObjectProperty(334);
			//# if (scBackground != null) {
				//# this.selectedColumnBackground = scBackground;
			//# }
		//#endif
		
	}
	
	/**
	 * Sets the stroke style and color of the lines between cells.
	 * @param lineStroke the line stroke style
	 * @param lineColor the color for lines
	 * @see #LINE_STYLE_SOLID
	 * @see #LINE_STYLE_DOTTED
	 * @see #LINE_STYLE_INVISIBLE
	 */
	public void setLineStyle( int lineStroke, int lineColor) {
		this.lineStroke = lineStroke;
		this.lineColor = lineColor;
	}

	protected void initContent(int firstLineWidth, int lineWidth)
	{
		if (lineWidth == 0) {
			return;
		}
		if (this.tableData == null) {
			this.contentWidth = 0;
			this.contentHeight = 0;
			return;
		}
		if (this.font == null) {
			this.font = Font.getDefaultFont();
		}
		int numberOfColumns = this.tableData.getNumberOfColumns();
		int[] widths = new int[ numberOfColumns ];
		int numberOfRows = this.tableData.getNumberOfRows();
		int[] heights = new int[ numberOfRows ];
		int textHeight = this.font.getHeight();
		int width;
		int height;
		for (int col = 0; col < numberOfColumns; col++ ) {
			for (int row = 0; row < numberOfRows; row++ ) {
				Object data = this.tableData.get(col, row);
				if (data == null) {
					continue;
				}
				if (data instanceof Item) {
					Item item = (Item) data;
					width = item.getItemWidth(lineWidth, lineWidth);
					height = item.getItemHeight(lineWidth, lineWidth);
				} else {
					width = this.font.stringWidth( data.toString() );
					height = textHeight;
				}
				if (this.lineStroke != LINE_STYLE_INVISIBLE) {
					width += (this.paddingHorizontal << 1) + 1;
					height += (this.paddingVertical << 1) + 1;
				} else {
					width += (this.paddingHorizontal << 1);
					height += (this.paddingVertical << 1);
				}
				if (width > widths[col]) {
					widths[col] = width;
				}
				if (height > heights[row]) {
					heights[row] = height;
				}
			}
		}
//		if (numberOfRows > 0) {
//			heights[ numberOfRows - 1] -= this.paddingVertical;
//		}
		height = 0;
		width = 0;
		for (int col = 0; col < numberOfColumns; col++ ) {
			height = 0;
			for (int row = 0; row < numberOfRows; row++ ) {
				Object data = this.tableData.get(col, row);
				if (data instanceof Item) {
					Item item = (Item) data;
					item.relativeX = width;
					item.relativeY = height;
				}
				height += heights[row];
			}
			width += widths[col];
		}
		this.completeWidth = width;
		this.contentWidth = width;
		this.contentHeight = height - this.paddingVertical;
		this.columnWidths = widths;
		this.rowHeights = heights;
		int appearance = PLAIN;
		if (this.completeWidth > lineWidth) {
			appearance = INTERACTIVE;
			this.contentWidth = lineWidth;
		}
		if (this.selectionMode != SELECTION_MODE_NONE) {
			if (this.internalX == NO_POSITION_SET) {
				selectCell();
			} else if (
					(this.selectedColumnIndex != -1 && widths[this.selectedColumnIndex] != this.internalWidth)
					|| (this.selectedRowIndex != -1 && (heights[this.selectedRowIndex] - (this.paddingVertical / 2)) != this.internalHeight)
			) {
				setSelectedCell( this.selectedColumnIndex, this.selectedRowIndex);
			}
			if (this.selectedColumnIndex != -1 || this.selectedRowIndex != -1) {
				appearance = INTERACTIVE;
			} else if (this.completeWidth <= this.contentWidth){
				appearance = PLAIN;
			}
		}
		this.appearanceMode = appearance;
		//System.out.println("contentHeight=" + this.contentHeight + ", width=" + this.contentWidth );
		
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#paintContent(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	protected void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g)
	{
		synchronized (this.paintLock) {
			if (this.tableData == null) {
				return;
			}
			int clipX = 0;
			int clipY = 0;
			int clipWidth = 0;
			int clipHeight = 0;
			if (this.completeWidth > rightBorder - leftBorder){
				clipX = g.getClipX();
				clipY = g.getClipY();
				clipWidth = g.getClipWidth();
				clipHeight = g.getClipHeight();
				g.clipRect( x, clipY, rightBorder - leftBorder, clipHeight);
			}
			int numberOfColumns = this.tableData.getNumberOfColumns();
			int numberOfRows = this.tableData.getNumberOfRows();
			int[] widths = this.columnWidths;
			int[] heights = this.rowHeights;
			if (numberOfColumns != widths.length || numberOfRows != heights.length) {
				return;
			}
			int height = 0;
			int width = 0;
			x += this.xOffset;
			boolean paintSelection = this.isFocused;
			if (!(paintSelection && this.selectedBackground != null && ( (this.selectionMode & SELECTION_MODE_CELL) == SELECTION_MODE_CELL)) ) {
				paintSelection = false;
			}
			//System.out.println("clipping: y=" + clipY + ", bottom=" + (clipY + clipHeight));
			boolean horizontalLinesPainted = false;
			for (int col = 0; col < numberOfColumns; col++ ) {
				height = 0;
				for (int row = 0; row < numberOfRows; row++ ) {
					int nextHeight = height + heights[row];
					if (!horizontalLinesPainted) {
						if (this.lineStroke != LINE_STYLE_INVISIBLE && row != numberOfRows -1) {
							g.setColor( this.lineColor );
							if (this.lineStroke == LINE_STYLE_DOTTED) {
								g.setStrokeStyle( Graphics.DOTTED );
								g.drawLine( x, y + nextHeight - this.paddingVertical, x + this.completeWidth, y + nextHeight - this.paddingVertical );
								g.setStrokeStyle( Graphics.SOLID );
							} else {
								g.drawLine( x, y + nextHeight - this.paddingVertical, x + this.completeWidth, y + nextHeight - this.paddingVertical );
							}
						}
					}
					
					Object data = this.tableData.get(col, row);
					if (paintSelection && col == this.selectedColumnIndex && row == this.selectedRowIndex) {
						this.selectedBackground.paint(x + this.internalX, y + this.internalY, this.internalWidth, this.internalHeight, g);
					}
					//System.out.println("painting cell " + col + ", " + row + " / " + (x + width) + ", " + (y + height) + " : " + data);
					if (data instanceof Item) {
						Item item = (Item) data;
						item.paint(x + width, y + height, x + width, x + width + widths[col] - (this.paddingHorizontal << 1), g);
	//					g.setColor( 0x00ff00);
	//					g.drawRect(x + width, y + height, widths[col] - (this.paddingHorizontal << 1), heights[row] - (this.paddingVertical << 1 ) );
					} else if (data != null) {
						g.setColor( this.fontColor );
						g.setFont( this.font );
						g.drawString( data.toString(), x + width, y + height, Graphics.LEFT | Graphics.TOP );
					}
					height = nextHeight;
				}
				horizontalLinesPainted = true;
				width += widths[col];
				if (this.lineStroke != LINE_STYLE_INVISIBLE && col != numberOfColumns -1) {
					g.setColor( this.lineColor );
					if (this.lineStroke == LINE_STYLE_DOTTED) {
						g.setStrokeStyle( Graphics.DOTTED );
						g.drawLine( x + width - this.paddingHorizontal, y, x + width - this.paddingHorizontal, y + this.contentHeight );
						g.setStrokeStyle( Graphics.SOLID );
					} else {
						g.drawLine( x + width - this.paddingHorizontal, y, x + width - this.paddingHorizontal, y + this.contentHeight );
					}
				}
			}
			if (this.completeWidth > rightBorder - leftBorder){
				g.setClip(clipX, clipY, clipWidth, clipHeight );
			}
		}
	}
	

	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#paintBackground(int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	protected void paintBackground(int x, int y, int width, int height, Graphics g)
	{
		super.paintBackground(x, y, width, height, g);
		if (this.isFocused) {
			if ( ((this.selectionMode & SELECTION_MODE_ROW) == SELECTION_MODE_ROW)  && this.selectedRowIndex != -1 && this.selectedRowBackground != null) {
				this.selectedRowBackground.paint(x, y + this.internalY + this.paddingTop, width, this.internalHeight, g);
			}
			if (( (this.selectionMode & SELECTION_MODE_COLUMN) == SELECTION_MODE_COLUMN)  && this.selectedColumnIndex != -1 && this.selectedColumnBackground != null) {
				int bgX  = x + this.xOffset + this.internalX;
				int bgW = this.internalWidth;
				if (bgX < x) {
					bgW -= (x - bgX);
					bgX = x;
				}
				if (bgX + bgW > x + width) {
					bgW = (x + width) - bgX;
				}
				if (bgW > 0) {
					this.selectedRowBackground.paint(bgX, y + this.paddingTop, bgW, this.contentHeight - (this.paddingTop + this.paddingBottom), g);
				}
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#animate(long, de.enough.polish.ui.ClippingRegion)
	 */
	public void animate(long currentTime, ClippingRegion repaintRegion)
	{
		super.animate(currentTime, repaintRegion);
		//TODO animate currently selected item
		int current = this.xOffset;
		int target = this.targetXOffset;
		if (target != current) {
			int speed = (target - current) / 3;
			speed += target > current ? 1 : -1;
			current += speed;
			if ( ( speed > 0 && current > target) || (speed < 0 && current < target ) ) {
				current = target;
			}
			this.xOffset = current;
			addRelativeToContentRegion(repaintRegion, -1, -1, this.contentWidth + 2, this.contentHeight + 2 );
		}
		synchronized (this.paintLock) {
			int sCol = this.selectedColumnIndex;
			int sRow = this.selectedRowIndex;
			if (sCol != -1 && sRow != -1 && this.tableData != null) {
				Object data = this.tableData.get(sCol, sRow);
				if (data instanceof Item) {
					((Item)data).animate(currentTime, repaintRegion);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeCustomItem#handleKeyPressed(int, int)
	 */
	protected boolean handleKeyPressed(int keyCode, int gameAction)
	{
		if (this.appearanceMode != PLAIN) {
			if (this.selectionMode == SELECTION_MODE_NONE) {
				if (gameAction == Canvas.RIGHT 
						&& this.xOffset + this.completeWidth > this.contentWidth) 
				{
					int offset = this.targetXOffset -  (this.contentWidth >> 1);
					if (offset + this.completeWidth < this.contentWidth) {
						offset = this.contentWidth - this.completeWidth;
					}
					this.targetXOffset = offset;
					return true;
				} else if (gameAction == Canvas.LEFT 
						&& this.xOffset < 0)
				{
					int offset = this.targetXOffset + (this.contentWidth >> 1);
					if (offset > 0) {
						offset = 0;
					}
					this.targetXOffset = offset;
					return true;
				}
			} else {
				boolean selectInteractive = ((this.selectionMode & SELECTION_MODE_INTERACTIVE) == SELECTION_MODE_INTERACTIVE);
				boolean selectNotempty = ((this.selectionMode & SELECTION_MODE_NONEMPTY) == SELECTION_MODE_NONEMPTY);
				boolean selectCell = ((this.selectionMode & SELECTION_MODE_CELL) == SELECTION_MODE_CELL);
				boolean selectRow = ((this.selectionMode & SELECTION_MODE_ROW) == SELECTION_MODE_ROW);
				boolean selectColumn = ((this.selectionMode & SELECTION_MODE_COLUMN) == SELECTION_MODE_COLUMN);
				if ( ((gameAction == Canvas.RIGHT && keyCode != Canvas.KEY_NUM6)
						| (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM4))
						&& (selectCell || selectColumn)
				) {
					int startCol;
					int endCol;
					int addCol;
					if (gameAction == Canvas.RIGHT) {
						startCol = this.selectedColumnIndex + 1;
						endCol = getNumberOfColumns();
						addCol = 1;
					} else {
						startCol = this.selectedColumnIndex - 1;
						endCol = -1;
						addCol = -1;
						
					}
					for (int col=startCol; col != endCol; col += addCol) {
						if (selectCell) {
							Object cell = get( col, this.selectedRowIndex );
							if (selectInteractive) {
								if (cell instanceof Item && ((Item)cell).appearanceMode != PLAIN) {
									setSelectedCell( col, this.selectedRowIndex, gameAction );
									return true;
								}
							} else if (selectNotempty) {
								if (cell !=  null) {
									setSelectedCell( col, this.selectedRowIndex, gameAction );
									return true;										
								}
							} else {
								setSelectedCell( col, this.selectedRowIndex, gameAction );
								return true;																													
							}
						} else {
							// select next selectable column:
							for (int row=0; row < getNumberOfRows(); row++) {
								Object cell = get( col, row );
								if (selectInteractive) {
									if (cell instanceof Item && ((Item)cell).appearanceMode != PLAIN) {
										setSelectedCell( col, this.selectedRowIndex, gameAction );
										return true;
									}
								} else if (selectNotempty) {
									if (cell !=  null) {
										setSelectedCell( col, this.selectedRowIndex, gameAction );
										return true;										
									}
								} else {
									setSelectedCell( col, this.selectedRowIndex, gameAction );
									return true;																													
								}
							}
						}
					}
				}
				if ( ((gameAction == Canvas.DOWN && keyCode != Canvas.KEY_NUM8)
						|| (gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM2)
						)
						&& (selectCell || selectRow)
				){
					int startRow;
					int endRow;
					int addRow;
					if (gameAction == Canvas.DOWN) {
						startRow = this.selectedRowIndex + 1;
						endRow = getNumberOfRows();
						addRow = 1;
					} else {
						startRow = this.selectedRowIndex - 1;
						endRow = -1;
						addRow = -1;
					}
					for (int row=startRow; row != endRow; row += addRow) {
						if (selectCell) {
							Object cell = get( this.selectedColumnIndex, row );
							if (selectInteractive) {
								if (cell instanceof Item && ((Item)cell).appearanceMode != PLAIN) {
									setSelectedCell( this.selectedColumnIndex, row, gameAction );
									return true;
								}
							} else if (selectNotempty) {
								if (cell !=  null) {
									setSelectedCell( this.selectedColumnIndex, row, gameAction );
									return true;										
								}
							} else {
								setSelectedCell( this.selectedColumnIndex, row, gameAction );
								return true;																													
							}
						} else {
							// select next selectable column:
							for (int col=0; col < getNumberOfColumns(); col++) {
								Object cell = get( col, row );
								if (selectInteractive) {
									if (cell instanceof Item && ((Item)cell).appearanceMode != PLAIN) {
										setSelectedCell( this.selectedColumnIndex, row, gameAction );
										return true;
									}
								} else if (selectNotempty) {
									if (cell !=  null) {
										setSelectedCell( this.selectedColumnIndex, row, gameAction );
										return true;										
									}
								} else {
									setSelectedCell( this.selectedColumnIndex, row, gameAction );
									return true;																													
								}
							}
						}
					}

				}
			}
		}
		return super.handleKeyPressed(keyCode, gameAction);
	}
	
	
	/**
	 * @param col the column
	 * @param row the row
	 */
	public void setSelectedCell(int col, int row)
	{
		setSelectedCell(col, row, 0);
	}
	/**
	 * @param col the column
	 * @param row the row
	 * @param direction the direction
	 */
	public void setSelectedCell(int col, int row, int direction)
	{
		if (this.selectionMode == SELECTION_MODE_CELL) {
			if (this.selectedItemStyle != null) {
				Object data = getSelectedCell();
				if (data instanceof Item) {
					Item item = (Item) data;
					item.defocus(this.selectedItemStyle);
				}
			}
			Object data = get(col, row);
			if (data instanceof Item) {
				Item item = (Item) data;
				item.focus( null, direction );
			}
		}
		this.selectedColumnIndex = col;
		this.selectedRowIndex = row;
		if (this.rowHeights != null) {
			if (col != -1) {
				this.internalX = getRelativeColumnX(col);
				this.internalWidth = this.columnWidths[col];
			} else {
				this.internalX = 0;
				this.internalWidth = this.completeWidth;				
			}
			if (row != -1) {
				this.internalY = getRelativeRowY(row);
				this.internalHeight = this.rowHeights[row] - (this.paddingVertical / 1);
			} else {
				this.internalY = 0;
				this.internalHeight = this.contentHeight;
			}
			// scroll horizontally:
			if (this.targetXOffset + this.internalX <  0) {
				this.targetXOffset = -this.internalX;
			} else if (this.targetXOffset + this.internalX + this.internalWidth > this.contentWidth) {
				this.targetXOffset = this.contentWidth - (this.internalX + this.internalWidth );
			}
		}
	}

	/**
	 * @param row
	 * @return
	 */
	private int getRelativeRowY(int row)
	{
		int y = 0;
		for (int i=0; i<row; i++) {
			y += this.rowHeights[i]; 
		}
		if (y > 0) {
			 y -= (this.paddingVertical / 2);
		}
		return y;
	}

	/**
	 * @param col
	 * @return
	 */
	private int getRelativeColumnX(int col)
	{
		int x = 0;
		for (int i=0; i<col; i++) {
			x += this.columnWidths[i];
		}
		return x;
	}

	//**************************** Table Management  ******************************************************************//
	

	/**
	 * Specifies a new dimension for this table
	 * 
	 * @param columns the number of columns 
	 * @param rows  the number of rows
	 */
	public void setDimension(int columns, int rows) {
		synchronized (this.paintLock) {
			if (this.tableData == null) 
			{
				this.tableData = new TableData(columns, rows );
			} else {
				this.tableData.setDimension(columns, rows);
			}
		}
		requestInit();
	}

	/**
	 * Retrieves the number of columns
	 * @return the number of columns, -1 when no table data is used
	 */
	public int getNumberOfColumns() {
		if (this.tableData == null) {
			return -1;
		}
		return this.tableData.getNumberOfColumns();
	}

	/**
	 * Retrieves the number of rows
	 * @return the number of rows, -1 when no table data is used
	 */
	public int getNumberOfRows() {
		if (this.tableData == null) {
			return -1;
		}
		return this.tableData.getNumberOfRows();
	}

	
	/**
	 * Adds a new column to this table.
	 * @return the index of the created column, the first/most left one has the index 0.
	 */
	public int addColumn() {
		int col = 0;
		synchronized (this.paintLock) {
			if (this.tableData == null) {
				this.tableData = new TableData( 1, 0);
				requestInit();
			} else {
				requestInit();
				col = this.tableData.addColumn();
			}
		}
		this.currentColumnIndex = col;
		return col;
	}
	
	/**
	 * Moves the internal column index to the next column.
	 * If necessary a new column will be created.
	 * The internal column index is used for add()
	 * @see #add(Item)
	 */
	public void moveToNextColumn() {
		if (this.currentColumnIndex + 1 >= getNumberOfColumns()) {
			addColumn();
		} else {
			this.currentColumnIndex++;
		}
	}
	
	/**
	 * Moves the internal row index to the next row.
	 * If necessary a new row will be created.
	 * The internal row index is used for add()
	 * @see #add(Item)
	 */
	public void moveToNextRow() {
		if (this.currentRowIndex + 1 >= getNumberOfRows()) {
			addRow();
		} else {
			this.currentRowIndex++;
		}
	}
	
	/**
	 * Inserts a column before the specified index.
	 * 
	 * @param index the index, use 0 for adding a column in the front, use getNumberOfColumns() for appending the column at the end
	 * @throws ArrayIndexOutOfBoundsException when the index is invalid
	 */
	public void insertColumn( int index ) {
		synchronized (this.paintLock) {
			this.tableData.insertColumn(index);
			requestInit();
		}
	}
	
	/**
	 * Removes the specified column
	 * 
	 * @param index the index. 0 is the first column
	 * @throws ArrayIndexOutOfBoundsException when the index is invalid
	 */
	public void removeColumn( int index ) {
		synchronized (this.paintLock) {
			this.tableData.removeColumn(index);
			requestInit();
		}
	}
	
	
	/**
	 * Adds a new row to this table.
	 * @return the index of the created row, the first/top one has the index 0.
	 */
	public int addRow() {
		int row = 0;
		synchronized (this.paintLock) {
			if (this.tableData == null) {
				this.tableData = new TableData( 0, 1);
				requestInit();
			} else {
				requestInit();
				row = this.tableData.addRow();
			}
		}
		this.currentRowIndex = row;
		return row;
	}
	
	/**
	 * Inserts a row before the specified index.
	 * 
	 * @param index the index, use 0 for adding a row in the top, use getNumberOfRows() for appending the column at the bottom
	 * @throws ArrayIndexOutOfBoundsException when the index is invalid
	 */
	public void insertRow( int index ) {
		synchronized (this.paintLock) {
			this.tableData.insertRow(index);
			requestInit();
		}
	}
	
	/**
	 * Removes the specified row
	 * 
	 * @param index the index. 0 is the first row
	 * @throws ArrayIndexOutOfBoundsException when the index is invalid
	 */
	public void removeRow( int index ) {
		synchronized (this.paintLock) {
			this.tableData.removeRow(index);
			requestInit();
		}
	}

	/**
	 * Sets the value of the given table position.
	 * @param column the horizontal position
	 * @param row the vertical position
	 * @param value the value, use null to delete a previous set value
	 * @throws ArrayIndexOutOfBoundsException when the column or row is invalid
	 * @see #get(int, int)
	 */
	public void set( int column, int row, Object value ) {
		set( column, row, value, null );
	}
	
	/**
	 * Sets the value of the given table position.
	 * @param column the horizontal position
	 * @param row the vertical position
	 * @param value the value, use null to delete a previous set value
	 * @param itemStyle the style of the added value item, if the value is not an Item it will be converted to a StringItem
	 * @throws ArrayIndexOutOfBoundsException when the column or row is invalid
	 * @see #get(int, int)
	 */
	public void set( int column, int row, Object value, Style itemStyle ) {
		Item item = null;
		if (value instanceof Item) {
			item = (Item) value;
			item.parent = UiAccess.cast(this);
		}
		if (itemStyle != null) {
			if (item != null) {
				item.setStyle(itemStyle);
			} else if (value != null) {
				item = new StringItem( null, value.toString(), itemStyle );
				item.parent = UiAccess.cast(this);
				value = item;
			}
		}
		synchronized (this.paintLock) {
			this.tableData.set(column, row, value);
		}
		repaint();
	}

	/**
	 * Sets the value of the given table position.
	 * @param column the horizontal position
	 * @param row the vertical position
	 * @return the value previously stored at the given position
	 * @throws ArrayIndexOutOfBoundsException when the column or row is invalid
	 * @see #set(int, int, Object)
	 */
	public Object get( int column, int row ) {
		return this.tableData.get(column, row);
	}
	
	/**
	 * Retrieves the data within the currently selected cell.
	 * @return the data of the selected cell, null if none is selected or this table has not a  cell or column_and_row selection mode
	 * @see #setSelectionMode(int)
	 */
	public Object getSelectedCell() {
		if (this.selectedColumnIndex == -1 || this.selectedRowIndex == -1) {
			return null;
		}
		return this.tableData.get( this.getSelectedColumn(), getSelectedRow() );
	}
	
	/**
	 * Retrieves the index of the currently selected row
	 * 
	 * @return the index of the currently selected row, 0 is the first row
	 */
	public int getSelectedRow() {
		return this.selectedRowIndex;
	}
	
	/**
	 * Retrieves the index of the currently selected column
	 * 
	 * @return the index of the currently selected column, 0 is the first column
	 */
	public int getSelectedColumn() {
		return this.selectedColumnIndex;
	}


	//#ifdef polish.useDynamicStyles	
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Item#createCssSelector()
	 //# */
	//# protected String createCssSelector()
	//# {
		//# return "table";
	//# }
	//#endif

	/**
	 * Retrieves the selection mode 
	 * @return the current selection mode
	 * @see #SELECTION_MODE_NONE
	 * @see #SELECTION_MODE_CELL
	 * @see #SELECTION_MODE_NONEMPTY
	 * @see #SELECTION_MODE_INTERACTIVE
	 * @see #SELECTION_MODE_ROW
	 * @see #SELECTION_MODE_COLUMN
	 */
	public int getSelectionMode() {
		return this.selectionMode;
	}
	

	/**
	 * Sets the selection mode.
	 * You can combine several settings with the OR operator. If you
	 * want to select a column and a row at the same time but only want the user
	 * to select interactive cells (cells that have interactive items) 
	 * call setSelectionMode( TableItem.SELECTION_MODE_ROW | TableItem.SELECTION_MODE_COLUMN | TableItem.SELECTION_MODE_INTERACTIVE ).
	 * If you want that individual cells are focused using either the 'focused-style' or the 'stylename:hover' CSS styles,
	 * you need to use the SELECTION_MODE_CELL as well:
	 * setSelectionMode( Table.SELECTION_MODE_CELL | TableItem.SELECTION_MODE_ROW | TableItem.SELECTION_MODE_COLUMN | TableItem.SELECTION_MODE_INTERACTIVE ).
	 *  
	 * @param selectionMode the desired selection mode
	 * @see #SELECTION_MODE_NONE
	 * @see #SELECTION_MODE_CELL
	 * @see #SELECTION_MODE_NONEMPTY
	 * @see #SELECTION_MODE_INTERACTIVE
	 * @see #SELECTION_MODE_ROW
	 * @see #SELECTION_MODE_COLUMN
	 */
	public void setSelectionMode(int selectionMode)
	{
		this.selectionMode = selectionMode;
		if (this.selectionMode == SELECTION_MODE_NONE) {
			this.selectedColumnIndex = -1;
			this.selectedRowIndex = -1;
			if (this.completeWidth > this.contentWidth) {
				this.appearanceMode = INTERACTIVE;
			} else {
				this.appearanceMode = PLAIN;
			}
		} else {
			selectCell();
			if (this.selectedColumnIndex != -1 || this.selectedRowIndex != -1) {
				this.appearanceMode = INTERACTIVE;
			} else if (this.completeWidth <= this.contentWidth){
				this.appearanceMode = PLAIN;
			}
		}
	}

	/**
	 * @return the selectedBackground
	 */
	public Background getSelectedBackground() {
	return this.selectedBackground;}
	

	/**
	 * @param selectedBackground the selectedBackground to set
	 */
	public void setSelectedBackground(Background selectedBackground)
	{
		this.selectedBackground = selectedBackground;
		if (this.selectedRowBackground == null) {
			this.selectedRowBackground = selectedBackground;
		}
		if (this.selectedColumnBackground == null) {
			this.selectedColumnBackground = selectedBackground;
		}
	}

	/**
	 * @return the selectedBackgroundHorizontal
	 */
	public Background getSelectedRowBackground() {
	return this.selectedRowBackground;}
	

	/**
	 * @param selectedBackgroundHorizontal the selectedBackgroundHorizontal to set
	 */
	public void setSelectedRowBackground(
			Background selectedBackgroundHorizontal)
	{
		this.selectedRowBackground = selectedBackgroundHorizontal;
	}

	/**
	 * @return the selectedBackgroundVertical
	 */
	public Background getSelectedColumnBackground() {
	return this.selectedColumnBackground;}
	

	/**
	 * @param selectedBackgroundVertical the selectedBackgroundVertical to set
	 */
	public void setSelectedColumnBackground(Background selectedBackgroundVertical)
	{
		this.selectedColumnBackground = selectedBackgroundVertical;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeCustomItem#defocus(de.enough.polish.ui.Style)
	 */
	public void defocus(Style originalStyle)
	{
		super.defocus(originalStyle);
		if ( !(this.selectionMode == SELECTION_MODE_NONE || this.appearanceMode == PLAIN)) {			
			Object obj = getSelectedCell();
			if (obj instanceof Item) {
				Item item = (Item) obj;
				item.defocus(this.selectedItemStyle);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeCustomItem#focus(de.enough.polish.ui.Style, int)
	 */
	protected Style focus(Style newStyle, int direction)
	{
		Style oldStyle = super.focus(newStyle, direction);
		if ( this.selectionMode == SELECTION_MODE_CELL) {			
			Object obj = getSelectedCell();
			if (obj == null) {
				selectCell();
			}
			if (obj instanceof Item) {
				Item item = (Item) obj;
				this.selectedItemStyle = item.focus( null, direction );
			}
		}
		return oldStyle;
	}

	/**
	 *  Selects the first selectable cell/column/row
	 */
	private void selectCell()
	{
		boolean selectInteractive = ((this.selectionMode & SELECTION_MODE_INTERACTIVE) == SELECTION_MODE_INTERACTIVE);
		boolean selectNotempty = ((this.selectionMode & SELECTION_MODE_NONEMPTY) == SELECTION_MODE_NONEMPTY);
		boolean selectCell = ((this.selectionMode & SELECTION_MODE_CELL) == SELECTION_MODE_CELL);
		boolean selectRow = ((this.selectionMode & SELECTION_MODE_ROW) == SELECTION_MODE_ROW);
		boolean selectColumn = ((this.selectionMode & SELECTION_MODE_COLUMN) == SELECTION_MODE_COLUMN);
		for (int row=0; row<getNumberOfRows(); row++) {
			for (int col=0; col<getNumberOfColumns(); col++ ) {
				Object data = get(col, row);
				if (selectInteractive) {
					if (data instanceof Item &&  ((Item)data).appearanceMode != PLAIN) {
						if ( !(selectCell || selectColumn)) {
							col = -1;
						}
						if ( !(selectCell || selectRow)) {
							row = -1;
						}
						setSelectedCell(col, row);
						return;
					}
				} else if (selectNotempty) {
					if (data != null) {
						if ( !(selectCell || selectColumn)) {
							col = -1;
						}
						if ( !(selectCell || selectRow)) {
							row = -1;
						}
						setSelectedCell(col, row);
						return;
					}
				} else {
					if ( !(selectCell || selectColumn)) {
						col = -1;
					}
					if ( !(selectCell || selectRow)) {
						row = -1;
					}
					setSelectedCell(col, row);
					return;
				}
			}
		}
	}
	
	/**
	 * Sets the style for cases when several items are added to a cell.
	 * @param containerStyle the style for containers that are automatically generated when several items are added into a cell
	 */
	public void setCellContainerStyle(Style containerStyle)
	{
		this.cellContainerStyle = containerStyle;
	}
	


	/**
	 * Adds the given item to the specified table element.
	 * The table element itself will be changed into a container, if necessary
	 * @param col the column index
	 * @param row the row index
	 * @param item the item
	 */
	public void add(int col, int row, Item item)
	{
		synchronized (this.paintLock) {
			if (this.tableData == null) {
				setDimension(col + 1, row + 1);
			}
			Object existing = this.tableData.get(col, row);
			if (existing == null) {
				set( this.currentColumnIndex, this.currentRowIndex, item );
			} else {
				if (existing instanceof Container) {
					((Container)existing).add(item);
				} else {
					Container container = new Container(false, this.cellContainerStyle);
					if (existing instanceof Item) {
						container.add( (Item)existing );
					} else {
						container.add( existing.toString() );
					}
					container.add(item);
					set( this.currentColumnIndex, this.currentRowIndex, container );
				}
			}
		}
	}

	
	/****** Container methods ********************************************************************************/

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#add(de.enough.polish.ui.Item)
	 */
	public void add(Item item)
	{
		add( this.currentColumnIndex, this.currentRowIndex, item );
	}

	

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#size()
	 */
	public int size()
	{
		return getNumberOfColumns() * getNumberOfRows();
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#get(int)
	 */
	public Item get(int index)
	{
		int row = index / getNumberOfColumns();
		int col = index % getNumberOfColumns();
		return (Item) this.tableData.get(col, row);
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#indexOf(de.enough.polish.ui.Item)
	 */
	public int indexOf(Item item)
	{
		for (int row=0; row<getNumberOfRows();row++) {
			for (int col=0;col<getNumberOfColumns();col++) {
				Object obj = this.tableData.get(col, row);
				if (obj == item) {
					return (row * getNumberOfColumns()) + col;
				}
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#remove(int)
	 */
	public Item remove(int index)
	{
		int row = index / getNumberOfColumns();
		int col = index % getNumberOfColumns();
		Item previous = (Item) this.tableData.get(col, row);
		this.tableData.set(col, row, null);
		return previous;
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.FakeContainerCustomItem#set(int, de.enough.polish.ui.Item, de.enough.polish.ui.Style)
	 */
	public Item set(int index, Item item, Style itemStyle)
	{
		int row = index / getNumberOfColumns();
		int col = index % getNumberOfColumns();
		if (itemStyle != null) {
			item.setStyle( itemStyle );
		}
		Item prev = (Item) this.tableData.get(col, row);
		set(col, row, item);
		return prev;
	}

	
	
}
