//#condition polish.midp || polish.usePolishGui
/*
 * Created on 08-Nov-2004 at 23:59:52.
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
/*
 * Modified on 14-Feb-2006 by Radu Zah, raduzah@yahoo.com.
 * Added the removeInstance method that will remove the font from cache. 
 * Usefull if you have large fonts and you want to free up some memory.
 */
package de.enough.polish.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.lcdui.Image;

/**
 * <p>Can be used to use any kind of bitmap fonts.</p>
 *
 * <p>Copyright Enough Software 2004 - 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public final class BitMapFont {
	private static Hashtable fontsByUrl = new Hashtable();
	
	private String fontUrl;
	private Image fontImage;
	private boolean hasMixedCase;
	private byte[] characterWidths;
	private short[] xPositions;
	private String characterMap;
	private int fontHeight;
	private int spaceIndex;

	/**
	 * Creates a new bitmap font.
	 * 
	 * @param fontUrl the url of the *.bmf file containing the font-specification.
	 */
	private BitMapFont( String fontUrl ) {
		super();
		this.fontUrl = fontUrl;
		//#debug
		//# System.out.println("Creating bitmap font " + fontUrl );
	}
	
	private void initFont() {
		// try to load the *.bmf file:
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream(this.fontUrl);
			if (in == null) {
				return;
			}
			DataInputStream dataIn = new DataInputStream( in );
			this.hasMixedCase = dataIn.readBoolean();
			String map = dataIn.readUTF();
			this.characterMap = map;
			this.spaceIndex = map.indexOf(' ');
			int length = map.length();
			this.characterWidths = new byte[ length ];
			this.xPositions = new short[ length ];
			short xPos = 0;
			for (int i = 0; i < length; i++ ) {
				byte width = dataIn.readByte();
				this.characterWidths[i] = width;
				this.xPositions[i] = xPos;
				xPos += width;
			}
			//#ifdef polish.midp2
				this.fontImage = Image.createImage( in );
			//#else
				//# ByteArrayOutputStream out = new ByteArrayOutputStream();
				//# byte[] pngBuffer = new byte[ 3 * 1024 ];
				//# int read;
				//# while ( (read = in.read(pngBuffer, 0, pngBuffer.length)) != -1) {
					//# out.write(pngBuffer, 0, read );
				//# }
				//# pngBuffer = out.toByteArray();
				//# out = null;
				//# this.fontImage = Image.createImage(pngBuffer, 0, pngBuffer.length);
				//# /*
				 //# * in.available() always returns 0 on Nokia and Motorola devices
				//# int pngLength = dataIn.available();
				//#debug
				//# System.out.println("BitMapFont: There are " + pngLength + " bytes available in the stream.");
				//# byte[] pngBuffer = new byte[ pngLength ];
				//# // defensive loading of the buffer, since
				//# // not all implementations do read the full
				//# // buffer in in.read( byte[] ):
				//# dataIn.readFully(pngBuffer);
				//# this.fontImage = Image.createImage(pngBuffer, 0, pngBuffer.length);
				//# */
			//#endif
			this.fontHeight = this.fontImage.getHeight();
			this.fontUrl = null;
		} catch (IOException e) {
			//#debug error
			//# System.out.println("Unable to load bitmap-font [" + this.fontUrl + "]" + e);
		//#ifndef polish.Bugs.ImageIOStreamAutoClose
		} finally {
			if (in != null) {
				try { 
					in.close();
				} catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to close bitmap-font stream" + e);
				}
			}
		//#endif
		}
	}
	
	/**
	 * Creates a viewer object for the given string.
	 * 
	 * @param input the input which should be shown.
	 * @return a viewer object which shows the font in a performant manner
	 */
	public BitMapFontViewer getViewer( String input ) {
		if (this.fontImage == null) {
			initFont();
			if (this.fontImage == null) {
				return null;
			}
		}
		//int imageWidth = this.fontImage.getWidth();
		// get the x/y-position and width for each character:
		if (!this.hasMixedCase) {
			input = input.toLowerCase();
		}
		int length = input.length();
		//short[] yPositions = new short[ length ];
		int[] indeces = new int[ length ];
		for (int i = length - 1; i >= 0; i-- ) {
			char inputCharacter = input.charAt(i);
			if (inputCharacter == '\n') {
				indeces[i] = BitMapFontViewer.ABSOLUTE_LINE_BREAK;
			} else {
				indeces[i] = this.characterMap.indexOf( inputCharacter );
			}
		}
		return new BitMapFontViewer( this.fontImage, indeces, this.xPositions, this.characterWidths, this.fontHeight, this.spaceIndex, 1 );
	}

	/**
	 * Gets the instance of the specified font.
	 * 
	 * @param url the url of the font
	 * @return the corresponding bitmap font.
	 */
	public static BitMapFont getInstance(String url) {
		BitMapFont font = (BitMapFont) fontsByUrl.get( url );
		if (font == null ) {
			font = new BitMapFont( url );
			fontsByUrl.put( url, font );
		}
		return font;
	}

	/**
	 * Removes the instance of the specified font from the internal cache.
	 * 
	 * @param url the url of the font
	 */
	public static void removeInstance(String url) {
		fontsByUrl.remove( url );
	}

	/**
	 * Retrieves the width of the given character
	 * @param c the character 
	 * @return -1 if unknown otherwise the width for the given character
	 */
	public int charWidth(char c) {
		if (this.fontImage == null) {
			initFont();
			if (this.fontImage == null) {
				return -1;
			}
		}
		for (int i=0; i<this.characterMap.length(); i++ ) {
			char cm = this.characterMap.charAt(i);
			if (cm == c) {
				return this.characterWidths[i]; 
			}
		}
		return -1;
	}

	/**
	 * Retrieves the width of the given text
	 * @param str the text 
	 * @return -1 if unknown otherwise the width for the given text
	 */
	public int stringWidth(String str) {
		if (this.fontImage == null) {
			initFont();
			if (this.fontImage == null) {
				return -1;
			}
		}
		int width = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			for (int j=0; j<this.characterMap.length(); j++ ) {
				char cm = this.characterMap.charAt(j);
				if (cm == c) {
					width += this.characterWidths[j];
					break;
				}
			}
			
		}
		return width;
	}

	/**
	 * Retrieves the height of this bitmap font
	 * @return the height of the font
	 */
	public int getFontHeight() {
		if (this.fontImage == null) {
			initFont();
			if (this.fontImage == null) {
				return -1;
			}
		}
		return this.fontHeight;
	}
}
