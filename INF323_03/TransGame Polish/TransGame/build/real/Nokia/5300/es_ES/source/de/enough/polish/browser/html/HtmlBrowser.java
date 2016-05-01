//#condition polish.usePolishGui

/*
 * Created on 11-Jan-2006 at 19:20:28.
 * 
 * Copyright (c) 2007 - 2008 Michael Koch / Enough Software
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
package de.enough.polish.browser.html;

import de.enough.polish.browser.Browser;
import de.enough.polish.browser.ProtocolHandler;
import de.enough.polish.browser.protocols.HttpProtocolHandler;
import de.enough.polish.browser.protocols.ResourceProtocolHandler;
import de.enough.polish.io.RedirectHttpConnection;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.Style;
import de.enough.polish.util.StringTokenizer;
import de.enough.polish.util.TextUtil;

/**
 * TODO: Write good docs.
 * 
 * polish.Browser.UserAgent
 * polish.Browser.MaxRedirects
 * polish.Browser.Gzip
 * polish.Browser.POISupport
 * polish.Browser.PaintDownloadIndicator
 * 
 * @see HttpProtocolHandler
 * @see ResourceProtocolHandler
 * @see RedirectHttpConnection
 */
public class HtmlBrowser
extends Browser
{

	private HtmlTagHandler htmlTagHandler;

  	/**
  	 * Creates a new browser using the default ".browser" style and default tag- and protocol handlers.
  	 */
  public HtmlBrowser()
  {
	  //#if polish.css.style.browser && !polish.LibraryBuild
	  	//#style browser
	  	this(de.enough.polish.ui.StyleSheet.browserStyle );	
	  //#else
	  	//# this( (Style) null );
	  //#endif
  }
  
  /**
   * Creates a new browser with the given style, the default tag handler and default protocol handlers (http, https, resource)
   * 
   * @param style the style
   * @see #getDefaultProtocolHandlers()
   * @see HtmlTagHandler
   */
  public HtmlBrowser( Style style )
  {
	  this( new HtmlTagHandler(), getDefaultProtocolHandlers(), style );
  }
  
  /**
   * Creates a new browser with the specified html tag handler
   * 
   * @param tagHandler the HtmlTagHandler used for this browser
   * @param protocolHandlers the protocol handlers
   * 
   * @throws NullPointerException when the tagHandler is null
   */
  public HtmlBrowser( HtmlTagHandler tagHandler, ProtocolHandler[] protocolHandlers )
  {
	  //#if polish.css.style.browser && !polish.LibraryBuild
	  	//#style browser
	  	this( tagHandler, protocolHandlers , de.enough.polish.ui.StyleSheet.browserStyle );	
	  //#else
	  	//# this( tagHandler, protocolHandlers, (Style) null );
	  //#endif	  
  }
  
  /**
   * Creates a new browser with the specified html tag handler
   * 
   * @param tagHandler the HtmlTagHandler used for this browser
   * @param protocolHandlers the protocol handlers
   * @param style the style of this browser
   * 
   * @throws NullPointerException when the tagHandler is null
   */
  public HtmlBrowser( HtmlTagHandler tagHandler, ProtocolHandler[] protocolHandlers,  Style style )
  {
	  super( protocolHandlers, style );
	  tagHandler.register(this);
	  this.htmlTagHandler = tagHandler;
  }
  
  /**
   * Sets the form listener that is notified about form creation and submission events
   * 
   * @param listener the listener, use null for de-registering a previous listener
   */
  public void setFormListener( FormListener listener ) {
	  this.htmlTagHandler.setFormListener( listener );
  }
  
  
  protected void handleText(String text)
  {
    if (text.length() > 0)
    {
      StringTokenizer st = new StringTokenizer(text, " \n\t");
      
      while (st.hasMoreTokens())
      {
        String str = st.nextToken();
        str = TextUtil.replace(str, "&nbsp;", " ");
        StringItem stringItem = null;
        if (this.htmlTagHandler.textStyle != null) {
        	stringItem = new StringItem(null, str, this.htmlTagHandler.textStyle);
        } 
        else 
        if (this.htmlTagHandler.textBold && this.htmlTagHandler.textItalic)
        {
          //#style browserTextBoldItalic
          stringItem = new StringItem(null, str, de.enough.polish.ui.StyleSheet.browsertextbolditalicStyle );
        }
        else if (this.htmlTagHandler.textBold)
        {
          //#style browserTextBold
          stringItem = new StringItem(null, str, de.enough.polish.ui.StyleSheet.browsertextboldStyle );
        }
        else if (this.htmlTagHandler.textItalic)
        {
          //#style browserTextItalic
          stringItem = new StringItem(null, str, de.enough.polish.ui.StyleSheet.browsertextitalicStyle );
        }
        else
        {
          //#style browserText
          stringItem = new StringItem(null, str, de.enough.polish.ui.StyleSheet.browsertextStyle );
        }
        add(stringItem);
      }
    }
  }
  
  

}
