//#condition polish.usePolishGui
/*
 * Created on 16-Nov-2005 at 12:22:20.
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
package de.enough.polish.ui.texteffects;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.TextEffect;
import de.enough.polish.util.ArrayList;

/**
 * <p>Renders textual smileys with images.</p>
 * <p>Activate the smiley text effect by specifying <code>text-effect: smiley;</code> in your polish.css file.
 * <!--
 *    You can finetune the effect with following attributes:
 *    -->
 * </p>
 * <!--
 * <ul>
 * 	 <li><b>text-shadow-color</b>: the color of the shadow, defaults to black.</li>
 * 	 <li><b>text-shadow-orientation</b>: the orientation of the shadow, either bottom-right, bottom-left, top-right, top-left, bottom, top, right or left. Defaults to bottom-right.</li>
 * 	 <li><b>text-shadow-x</b>: use this for finetuning the shadow's horizontal position. Negative values move the shadow to the left.</li>
 * 	 <li><b>text-shadow-y</b>: use this for finetuning the shadow's vertical position. Negative values move the shadow to the top.</li>
 * </ul>
 *    -->
 *
 * <p>Copyright (c) 2007 Enough Software</p>
 * <pre>
 * history
 *        16-July-2007 - Andre creation
 * </pre>
 * @author Andre Schmidt, j2mepolish@enough.de
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class SmileyTextEffect extends TextEffect {
	
	public class Smiley
	{		
		public String[] smileys;
		public Image image;
		public String description;
		
		public Smiley(String[] smileys, String imageUrl) {
			this( smileys, imageUrl, null);
		}
		public Smiley(String[] smileys, String imageUrl, String description)
		{
			this.smileys = smileys;
			
			try
			{
				this.image = Image.createImage(imageUrl);
			}
			catch(IOException e)
			{
				//#debug error
				//# System.out.println("unable to load smiley image " + e);
			}
			
			this.description = description;
		}
	}
	
	public static Smiley[] smileyList = null;		
	
	protected Hashtable smileyMap;
	protected String currentSmiley;
	private int smileyWidth;
	private int smileyHeight;
	private boolean isInitialized;
	
	/**
	 * Creates a text with smileys
	 */
	public SmileyTextEffect() {
		super();
		
		//#if smileys:defined
			//#= smileyList = new Smiley[]${smileys};
		//#else
			smileyList = new Smiley[0];		
		//#endif
	}
	
	protected void init() {

		this.smileyMap 		= new Hashtable();
		
		this.smileyWidth	= smileyList[0].image.getWidth();
		this.smileyHeight	= smileyList[0].image.getHeight();
		
		for(int i=0; i<smileyList.length; i++)
		{
			Smiley smiley = smileyList[i];
			
			for(int j=0; j< smiley.smileys.length; j++)
			{
				this.smileyMap.put(smiley.smileys[j],smiley.image);
			}
		}
		
		this.isInitialized = true;
	}

	public int stringWidth(String str) {
		if (!this.isInitialized) {
			init();
		}
		int position 	= 0;
		int stringWidth = 0;
		
		while(str.length() > 0)
		{
			position = getSmiley(str);
			
			String linePart = str.substring(0, position);
			
			stringWidth += super.stringWidth(linePart);
			
			if(this.currentSmiley != null)
			{
				stringWidth += this.smileyWidth;
				
				position += this.currentSmiley.length();
			}
			
			str = str.substring(position,str.length());	
		}
		
		return stringWidth;
	}
	
	public int getFontHeight() {
		if (!this.isInitialized) {
			init();
		}
		
		int fontHeight = super.getFontHeight();
		
		if(fontHeight > this.smileyHeight)
		{
			return fontHeight;
		}
		else
		{
			return this.smileyHeight;
		}
	}
	
	


	public String[] wrap(String text, Font font, int firstLineWidth, int lineWidth) {
		if (!this.isInitialized) {
			init();
		}
		if (firstLineWidth <= 0 || lineWidth <= 0) {
			//#debug error
			//# System.out.println("INVALID LINE WIDTH FOR SPLITTING " + firstLineWidth + " / " + lineWidth + " ( for string " + text + ")");
			//#if polish.debug.error
				//# new RuntimeException().printStackTrace();
			//#endif
			return new String[]{ text };
		}
		boolean hasLineBreaks = (text.indexOf('\n') != -1);
		int completeWidth = stringWidth(text);
		if ( (completeWidth <= firstLineWidth && !hasLineBreaks) ) { // || (value.length() <= 1) ) {
			// the given string fits on the first line:
			//if (hasLineBreaks) {
			//	return split( "complete/linebreaks:" + completeWidth + "> " + value, '\n');
			//} else {
				return new String[]{  text };
			//}
		}
		// the given string does not fit on the first line:
		ArrayList lines = new ArrayList();
		if (!hasLineBreaks) {
			wrap( text, font, completeWidth, firstLineWidth, lineWidth, lines );
		} else {
			// now the string will be splitted at the line-breaks and
			// then each line is processed:
			char[] valueChars = text.toCharArray();
			int lastIndex = 0;
			char c =' ';
			for (int i = 0; i < valueChars.length; i++) {
				c = valueChars[i];
				if (c == '\n' || i == valueChars.length -1 ) {
					String line = null;
					if (i == valueChars.length -1) {
						line = new String( valueChars, lastIndex, (i + 1) - lastIndex );
						//System.out.println("wrap: adding last line " + line );
					} else {
						line = new String( valueChars, lastIndex, i - lastIndex );
						//System.out.println("wrap: adding " + line );
					}
					completeWidth = stringWidth(line);
					if (completeWidth <= firstLineWidth ) {
						lines.add( line );						
					} else {
						wrap(line, font, completeWidth, firstLineWidth, lineWidth, lines);
					}
					lastIndex = i + 1;
					// after the first line all line widths are the same:
					firstLineWidth = lineWidth;
				} // for each line
			} // for all chars
			// special case for lines that end with \n: add a further line
			if (c == '\n') {
				lines.add("");
			}
		}
		//#debug
		//# System.out.println("Wrapped [" + text + "] into " + lines.size() + " rows.");
		return (String[]) lines.toArray( new String[ lines.size() ]);
	}
	
	public void wrap( String value, Font font, 
			int completeWidth, int firstLineWidth, int lineWidth, 
			ArrayList list ) 
	{
		if (!this.isInitialized) {
			init();
		}
		char[] valueChars = value.toCharArray();
		int startPos = 0;
		int lastSpacePos = -1;
		int lastSpacePosLength = 0;
		int currentLineWidth = 0;
		for (int i = 0; i < valueChars.length; i++) {
			char c = valueChars[i];
			String smiley = isSmileyNext(valueChars,i);
			
			if(smiley == null)
			{
				currentLineWidth += font.charWidth( c );
			}
			else
			{
				Image img = (Image)this.smileyMap.get(smiley);
				currentLineWidth += img.getWidth();
				
				i += smiley.length() - 1;
			}
			
			if (c == '\n') {
				list.add( new String( valueChars, startPos, i - startPos ) );
				lastSpacePos = -1;
				startPos = i+1;
				currentLineWidth = 0;
				firstLineWidth = lineWidth; 
				i = startPos;
			} else if (currentLineWidth >= firstLineWidth && i > 0) {
				if ( lastSpacePos == -1 ) {
					i--;
					//System.out.println("value=" + value + ", i=" + i + ", startPos=" + startPos);
					list.add( new String( valueChars, startPos, i - startPos ) );
					startPos =  i;
					currentLineWidth = 0;
				} else {
					currentLineWidth -= lastSpacePosLength;
					list.add( new String( valueChars, startPos, lastSpacePos - startPos ) );
					startPos =  lastSpacePos + 1;
					lastSpacePos = -1;
				}
				firstLineWidth = lineWidth; 
			} else if (c == ' ' || c == '\t') {
				lastSpacePos = i;
				lastSpacePosLength = currentLineWidth;
			}
			
		} 
		// add tail:
		list.add( new String( valueChars, startPos, valueChars.length - startPos ) );
	}
	
	private String isSmileyNext(char[] chars, int offset)
	{
		Enumeration smileys = this.smileyMap.keys();
		String smiley;
		
		while(smileys.hasMoreElements())
		{
			smiley = (String)smileys.nextElement(); 
		}
		
		while(smileys.hasMoreElements())
		{
			smiley = (String)smileys.nextElement();
			 
			 if(offset + smiley.length() > chars.length)
			 {
				 continue;
			 }
			 else
			 {
				 boolean isSmiley = true;
				 for(int j=0; j<smiley.length(); j++)
				 {
					 if(chars[offset + j] != smiley.charAt(j))
					 {
						 isSmiley = false;
						 break;
					 }
				 }
				 
				 if(isSmiley)
				 {
					 return smiley;
				 }
			 }
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.TextEffect#drawString(java.lang.String, int, int, int, int, javax.microedition.lcdui.Graphics)
	 */
	public void drawString(String text, int textColor, int x, int y, int orientation,
			Graphics g) 
	{
		if (!this.isInitialized) {
			init();
		}
		int position 	= 0;
		int offset 		= 0;
		
		while(text.length() > 0)
		{
			position = getSmiley(text);
			
			String linePart = text.substring(0, position);
			
			g.drawString(linePart, x + offset, y, orientation);
			
			if(this.currentSmiley != null)
			{
				offset += super.stringWidth(linePart);
				
				Image img = (Image)this.smileyMap.get(this.currentSmiley);
			
				g.drawImage(img, x + offset, y, orientation);
				
				offset += this.smileyWidth;
				
				position += this.currentSmiley.length();
			}
			
			text = text.substring(position,text.length());	
		}
	}
	
	protected int getSmiley(String line)
	{
		Enumeration smileys = this.smileyMap.keys();
		
		int first = line.length();
		this.currentSmiley = null;
		
		while(smileys.hasMoreElements())
		{
			String smiley = (String)smileys.nextElement();
			
			int position = 0;
			if((position = line.indexOf(smiley)) > -1)
			{
				if(first > position)
				{
					first = position;
					this.currentSmiley = smiley;
				}
			}
		}
		
		return first;
	}
}
