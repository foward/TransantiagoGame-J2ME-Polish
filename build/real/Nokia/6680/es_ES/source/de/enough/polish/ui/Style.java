//#condition polish.usePolishGui
/*
 * Created on 04-Jan-2004 at 19:43:08.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.io.Externalizable;
import de.enough.polish.io.Serializable;
import de.enough.polish.io.Serializer;


/**
 * <p>Style defines the design of any widget.</p>
 * <p>This class is used by the widgets. If you only use the predefined
 *       widgets you do not need to work with this class.
 * </p>
 * @author Robert Virkus, robert@enough.de
 * <pre>
 * history
 *        04-Jan-2004 - rob creation
 * </pre>
 */
public class Style implements Externalizable
{
	//#if polish.cldc1.1
		public final static Boolean TRUE = Boolean.TRUE;
		public final static Boolean FALSE = Boolean.FALSE;
	//#else
		//# public final static Boolean TRUE = new Boolean( true );
		//# public final static Boolean FALSE = new Boolean( false );
	//#endif

	/**
	 * The name of this style.
	 */
	public transient String name;
	public transient String dynamicName;

	public Background background;
	public Border border;
	public Font font;
	/**
	 * @deprecated Please use getFontColor() instead
	 * @see #getFontColor()
	 */
	public int fontColor;
	public Color fontColorObj;
	public int paddingLeft;
	public int paddingTop;
	public int paddingRight;
	public int paddingBottom;
	public int paddingVertical;
	public int paddingHorizontal;
	public int marginLeft;
	public int marginTop;
	public int marginRight;
	public int marginBottom;

	public int layout;
	
	private short[] attributeKeys;
	private Object[] attributeValues;
	
	/**
	 * Creates a new style with default settings
	 */
	public Style() {
		this( 0, 0, 0, 0, // margin
				1, 1, 1, 1, 1, 1, // padding
				Graphics.TOP | Graphics.LEFT, // layout
				0, null, null, // font
				null, null // background, border
				, null // attributes
				, null
		);
	}


	/**
	 * Creates a new Style.
	 * 
	 * @param marginLeft the margin in pixels to the next element on the left
	 * @param marginRight the margin in pixels to the next element on the right
	 * @param marginTop the margin in pixels to the next element on the top
	 * @param marginBottom the margin in pixels to the next element on the bottom
	 * @param paddingLeft the padding between the left border and content in pixels
	 * @param paddingRight the padding between the right border and content in pixels
	 * @param paddingTop the padding between the top border and content in pixels
	 * @param paddingBottom the padding between the bottom border and content in pixels
	 * @param paddingVertical the vertical padding between internal elements of an item 
	 * @param paddingHorizontal the horizontal padding between internal elements of an item
	 * @param layout the layout for this style, e.g. Item.LAYOUT_CENTER
	 * @param fontColor the color of the font
	 * @param font the content-font for this style
	 * @param background the background for this style
	 * @param border the border for this style
	 * @param attributeKeys the integer-IDs of any additional attributes. Can be null.
	 * @param attributeValues the values of any additional attributes. Can be null.
	 */
	public Style( int marginLeft, int marginRight, int marginTop, int marginBottom,
			int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, int paddingVertical, int paddingHorizontal,
			int layout,
			int fontColor, Font font,  
			Background background, Border border 
			, short[] attributeKeys
			, Object[] attributeValues
			) 
	{
		this( marginLeft, marginRight, marginTop, marginBottom,
				paddingLeft, paddingRight, paddingTop, paddingBottom, paddingVertical, paddingHorizontal,
				layout,
				fontColor, null, font,
				background, border
				, attributeKeys
				, attributeValues
		);
	}
	
	/**
	 * Creates a new Style.
	 * 
	 * @param marginLeft the margin in pixels to the next element on the left
	 * @param marginRight the margin in pixels to the next element on the right
	 * @param marginTop the margin in pixels to the next element on the top
	 * @param marginBottom the margin in pixels to the next element on the bottom
	 * @param paddingLeft the padding between the left border and content in pixels
	 * @param paddingRight the padding between the right border and content in pixels
	 * @param paddingTop the padding between the top border and content in pixels
	 * @param paddingBottom the padding between the bottom border and content in pixels
	 * @param paddingVertical the vertical padding between internal elements of an item 
	 * @param paddingHorizontal the horizontal padding between internal elements of an item
	 * @param layout the layout for this style, e.g. Item.LAYOUT_CENTER
	 * @param fontColor the color of the font
	 * @param fontColorObj the color of the font, might be a dynamic color like COLOR_FOREGROUND
	 * @param font the content-font for this style
	 * @param background the background for this style
	 * @param border the border for this style
	 * @param attributeKeys the integer-IDs of any additional attributes. Can be null.
	 * @param attributeValues the values of any additional attributes. Can be null.
	 */
	public Style( int marginLeft, int marginRight, int marginTop, int marginBottom,
			int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, int paddingVertical, int paddingHorizontal,
			int layout,
			int fontColor, Color fontColorObj, Font font,  
			Background background, Border border 
			, short[] attributeKeys
			, Object[] attributeValues
			) 
	{
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
		this.paddingVertical = paddingVertical;
		this.paddingHorizontal = paddingHorizontal;
		this.layout = layout;
		this.fontColor = fontColor;
		this.fontColorObj = fontColorObj;
		this.font = font;
		this.background = background;
		this.border = border;

		this.attributeValues = attributeValues;
		this.attributeKeys = attributeKeys;
		//#ifdef false
			//# // this is only used, so that the IDE does not complain about unused code:
			//# getProperty( -1 );
			//# getIntProperty( -1 );
			//# getBooleanProperty( -1 );
			//# getObjectProperty( -1 );
			//# getColorProperty( -1 );
		//#endif
	}
	
	//#ifdef polish.LibraryBuild
	//# /**
	 //# * Retrieves a non-standard property of this style.
	 //# * 
	 //# * @param propName the name of the property
	 //# * @return the value of this property as a String. If none has been defined, null will be returned.
	 //# */
	//# public String getProperty( String propName ) {
		//# return null;
	//# }
	//#endif
	
	//#ifdef polish.LibraryBuild
	//# /**
	 //# * Retrieves a non-standard property of this style.
	 //# * 
	 //# * @param propName the name of the property
	 //# * @return the value of this property. If none has been defined, null will be returned.
	 //# */
	//# public Object getObjectProperty( String propName ) {
		//# return null;
	//# }
	//#endif


	//#ifdef polish.LibraryBuild
	//# /**
	 //# * Retrieves a non-standard integer property of this style.
	 //# * 
	 //# * @param propName the name of the property
	 //# * @return the value of this property as an Integer object. If none has been defined, null will be returned.
	 //# */
	//# public Integer getIntProperty( String propName ) {
		//# return new Integer( 0 );
	//# }
	//#endif

	//#ifdef polish.LibraryBuild
	//# /**
	 //# * Retrieves a non-standard color property of this style.
	 //# * 
	 //# * @param propName the name of the property
	 //# * @return the value of this property as an Color object. If none has been defined, null will be returned.
	 //# */
	//# public Color getColorProperty( String propName ) {
		//# return new Color( 0 );
	//# }
	//#endif
	
	//#ifdef polish.LibraryBuild
	//# /**
	 //# * Retrieves a non-standard boolean property of this style.
	 //# * 
	 //# * @param propName the name of the property
	 //# * @return the value of this property as an Boolean object. If none has been defined, null will be returned.
	 //# */
	//# public Boolean getBooleanProperty( String propName ) {
		//# return new Boolean( false );
	//# }
	//#endif

	//#ifdef polish.LibraryBuild
		//# private String getProperty( int key ) {
	//#else
		public String getProperty( int key ) {
	//#endif
		if (this.attributeKeys == null) {
			return null;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				Object value = this.attributeValues[i];
				if (value != null) {
					return value.toString();
				} else {
					return null;
				}
			}
		}
		return null;
	}
		
	//#ifdef polish.LibraryBuild
		//# private Object getObjectProperty( int key ) {
	//#else
		public Object getObjectProperty( int key ) {
	//#endif
		if (this.attributeKeys == null) {
			return null;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				return this.attributeValues[i];
			}
		}
		return null;
	}


	//#ifdef polish.LibraryBuild
		//# private Integer getIntProperty( int key ) {
	//#else
		public Integer getIntProperty( int key ) {
	//#endif
		if (this.attributeKeys == null) {
			return null;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				Object value = this.attributeValues[i];
				if (value instanceof Color) {
					return ((Color)value).getInteger();
				}
				return (Integer) value;
			}
		}
		return null;
	}
		
	//#ifdef polish.LibraryBuild
		//# private Color getColorProperty( int key ) {
	//#else
		public Color getColorProperty( int key ) {
	//#endif
		if (this.attributeKeys == null) {
			return null;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				Object value = this.attributeValues[i];
				return (Color) value;
			}
		}
		return null;
	}
	
	//#ifdef polish.LibraryBuild
		//# private Boolean getBooleanProperty( int key ) {
	//#else
		public Boolean getBooleanProperty( int key ) {
	//#endif
		if (this.attributeKeys == null) {
			return null;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				return (Boolean) this.attributeValues[i];
			}
		}
		return null;
	}
	
	
	/**
	 * Releases all (memory intensive) resources such as images or RGB arrays of this style.
	 */
	public void releaseResources() {
		if (this.background != null) {
			this.background.releaseResources();
		}
		//TODO how to handle before/after images?
	}
	
	/**
	 * Retrieves the font color that should be used.
	 * The color can be dynamic like Display.COLOR_FOREGROUND and should always be retrieved
	 * using this method instead of using the public field fontColor.
	 *  
	 * @return the color for the font.
	 */
	public int getFontColor() {
		if ( this.fontColorObj != null ) {
			return this.fontColorObj.getColor();
		} else {
			return this.fontColor;
		}
	}


	/**
	 * @param key the integer key of the attribute
	 */
	public void removeAttribute(int key) {
		if (this.attributeKeys == null) {
			return;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				//TODO decrease array
				this.attributeValues[i] = null;
				return;
			}
		}
	}
	
	/**
	 * @param key the integer key of the attribute
	 * @param value the value of the attribute
	 */
	public void addAttribute(int key, Object value) {
		if (this.attributeKeys == null) {
			this.attributeKeys = new short[]{ (short)key };
			this.attributeValues = new Object[]{ value };
			return;
		}
		for (int i = 0; i < this.attributeKeys.length; i++ ) {
			if (this.attributeKeys[i] == key) {
				this.attributeValues[i] = value;
				return;
			}
		}
		// need to increas the attributes tables:
		short[] keys = new short[ this.attributeKeys.length + 1 ];
		System.arraycopy(this.attributeKeys,0, keys, 0, this.attributeKeys.length );
		keys[ this.attributeKeys.length ] = (short) key;
		Object[] values = new Object[ this.attributeKeys.length + 1 ];
		System.arraycopy(this.attributeValues,0, values, 0, this.attributeKeys.length );
		values[ this.attributeKeys.length ] = value;
		this.attributeKeys = keys;
		this.attributeValues = values;
	}


	public void read(DataInputStream in) throws IOException {
		this.background = (Background)Serializer.deserialize(in);
		this.border 	= (Border)Serializer.deserialize(in);
		this.font 		= (Font)Serializer.deserialize(in);
		
		this.fontColor = in.readInt();
		this.fontColorObj = (Color)Serializer.deserialize(in);
		this.paddingLeft = in.readInt();
		this.paddingRight = in.readInt();
		this.paddingTop = in.readInt();
		this.paddingBottom = in.readInt();
		this.paddingVertical = in.readInt();
		this.paddingHorizontal = in.readInt();
		this.marginLeft = in.readInt();
		this.marginRight = in.readInt();
		this.marginTop = in.readInt();
		this.marginBottom = in.readInt();
		this.layout = in.readInt();
		
		this.attributeKeys 	 = (short[])Serializer.deserialize(in);
		this.attributeValues = (Object[])Serializer.deserialize(in);
	}


	public void write(DataOutputStream out) throws IOException {
		Serializer.serialize(this.background, out);
		Serializer.serialize(this.border, out);
		Serializer.serialize(this.font, out);
		
		out.writeInt(this.fontColor);
		Serializer.serialize(fontColorObj, out);
		out.writeInt(this.paddingLeft);
		out.writeInt(this.paddingRight);
		out.writeInt(this.paddingTop);
		out.writeInt(this.paddingBottom);
		out.writeInt(this.paddingVertical);
		out.writeInt(this.paddingHorizontal);
		out.writeInt(this.marginLeft);
		out.writeInt(this.marginRight);
		out.writeInt(this.marginTop);
		out.writeInt(this.marginBottom);
		out.writeInt(this.layout);
		
		Serializer.serialize(this.attributeKeys, out);
		Serializer.serialize(this.attributeValues, out);
	}
	
//#ifdef polish.Style.additionalMethods:defined
	//#include ${polish.Style.additionalMethods}
//#endif
	

}
