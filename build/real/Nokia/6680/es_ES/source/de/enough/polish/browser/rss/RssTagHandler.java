//#condition polish.usePolishGui

/*
 * Created on 24-Apr-2007 at 19:20:28.
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
package de.enough.polish.browser.rss;

import javax.microedition.lcdui.Command;

import de.enough.polish.browser.Browser;
import de.enough.polish.browser.TagHandler;
import de.enough.polish.browser.html.HtmlTagHandler;
import de.enough.polish.io.StringReader;
import de.enough.polish.ui.Container;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.ItemCommandListener;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.Style;
import de.enough.polish.util.HashMap;
import de.enough.polish.util.Locale;
import de.enough.polish.util.TextUtil;
import de.enough.polish.xml.SimplePullParser;

public class RssTagHandler
	extends TagHandler
{
	private static final String TAG_CHANNEL = "channel";
	private static final String TAG_LINK = "link";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_COPYRIGHT = "copyright";
	private static final String TAG_PUBDATE = "pubDate";
	private static final String TAG_GUID = "guid";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_URL = "url";
	private static final String TAG_ITEM = "item";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_AUTHOR = "author";

	private static final String TAG_DIGG_DIGGCOUNT = "digg:diggCount";
	private static final String TAG_DIGG_USERNAME = "digg:username";
	private static final String TAG_DIGG_USERIMAGE = "digg:userimage";
	private static final String TAG_DIGG_CATEGORY = "digg:category";
	private static final String TAG_DIGG_COMMENTCOUNT = "digg:commentCount";

	/** item attribute for storing the RSS item */
	public static final String ATTR_RSS_ITEM = "RSS_ITEM";

	  /** default select command */
	//#ifdef polish.i18n.useDynamicTranslations
		//# public static Command CMD_RSS_ITEM_SELECT = new Command( "Show", Command.OK, 1 );
	//#elifdef polish.rss.command.select:defined
public static final Command CMD_RSS_ITEM_SELECT = new Command("Show", Command.OK, 1 );
	//#else
		//# public static final Command CMD_RSS_ITEM_SELECT = new Command("Show", Command.OK, 1);
	//#endif
	/** default go to article command */
	//#ifdef polish.i18n.useDynamicTranslations
		//# public static Command CMD_GO_TO_ARTICLE = new Command( "Go", Command.SCREEN, 2 );
	//#elifdef polish.rss.command.followlink:defined
public static final Command CMD_GO_TO_ARTICLE = new Command("Go", Command.SCREEN, 2 );
	//#else
		//# public static final Command CMD_GO_TO_ARTICLE = new Command("Go", Command.SCREEN, 2);
	//#endif
	
	

	private Browser browser;
	private boolean inChannelTag;
	private boolean inItemTag;
	private String title;
	private String description;
	private String url;
	private Command linkCommand;
	private ItemCommandListener itemListener;
	private boolean includeDescriptions;

	//#if polish.midp2
	public RssTagHandler(Command linkCommand, javax.microedition.lcdui.ItemCommandListener listener)
	{
		//#debug error
		//# System.out.println("This constructor should not be used after preprocessing.");
		this.linkCommand = linkCommand;
	}
	//#endif

	public RssTagHandler(Command linkCommand, ItemCommandListener listener)
	{
		this.linkCommand = linkCommand;
		this.itemListener = listener;
		//#ifdef polish.i18n.useDynamicTranslations
			//# if ( "Show" != CMD_RSS_ITEM_SELECT.getLabel()) {
				//# CMD_RSS_ITEM_SELECT = new Command( "Show", Command.OK, 1 );
				//# CMD_GO_TO_ARTICLE = new Command( "Go", Command.SCREEN, 2 );
			//# }
		//#endif
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.browser.TagHandler#register(de.enough.polish.browser.Browser)
	 */
	public void register(Browser browser)
	{
		this.browser = browser;

		browser.addTagHandler(TAG_CHANNEL, this);
		browser.addTagHandler(TAG_LINK, this);
		browser.addTagHandler(TAG_LANGUAGE, this);
		browser.addTagHandler(TAG_COPYRIGHT, this);
		browser.addTagHandler(TAG_PUBDATE, this);
		browser.addTagHandler(TAG_GUID, this);
		browser.addTagHandler(TAG_IMAGE, this);
		browser.addTagHandler(TAG_URL, this);
		browser.addTagHandler(TAG_ITEM, this);
		browser.addTagHandler(TAG_TITLE, this);
		browser.addTagHandler(TAG_DESCRIPTION, this);
		browser.addTagHandler(TAG_AUTHOR, this);

		browser.addTagHandler(TAG_DIGG_DIGGCOUNT, this);
		browser.addTagHandler(TAG_DIGG_USERNAME, this);
		browser.addTagHandler(TAG_DIGG_USERIMAGE, this);
		browser.addTagHandler(TAG_DIGG_CATEGORY, this);
		browser.addTagHandler(TAG_DIGG_COMMENTCOUNT, this);
	}
	
	private static String decodeHtml(String encodedHtml) {
		String tmp = TextUtil.replace(encodedHtml, "&lt;", "<");
		tmp = TextUtil.replace(tmp, "&gt;", ">");
		return TextUtil.replace(tmp, "&quot;", "\"");
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.browser.TagHandler#handleTag(de.enough.polish.ui.Container, de.enough.polish.xml.SimplePullParser, java.lang.String, boolean, de.enough.polish.util.HashMap, de.enough.polish.ui.Style)
	 */
	public boolean handleTag(Container parentItem, SimplePullParser parser, String tagName, boolean opening, HashMap attributeMap, Style style)
	{
		if (TAG_CHANNEL.equals(tagName)) {
			this.inChannelTag = opening;
			return true;
		}

		if (this.inChannelTag) {
			if (TAG_ITEM.equals(tagName)) {
				this.inItemTag = opening;

				if (!opening && this.title != null && this.description != null) {
					addRssItem(this.title, this.description, this.url);
					this.title = null;
					this.description = null;
					this.url = null;
				}

				return true;
			}

			if (this.inItemTag) {
				if (TAG_TITLE.equals(tagName)) {
					if (opening) {
						parser.next();
						this.title = parser.getText();
					}

					return true;
				}

				if (TAG_LINK.equals(tagName)) {
					if (opening) {
						parser.next();
						this.url = parser.getText();
					}

					return true;
				}

				if (TAG_DESCRIPTION.equals(tagName)) {
					if (opening) {
						parser.next();
						this.description = parser.getText();
						// Description can be encoded HTML. Decode it.
						this.description = decodeHtml(this.description);
						if (this.includeDescriptions) {
							try {
								this.browser.loadPartialPage(new StringReader(this.description));
							} catch (Exception e) {
								//#debug error
								//# System.out.println("Unable to load description " + this.description + e );
							}
						}
					}

					return true;
				}
			}
		}

		// Ignore content of some tags.
		if (TAG_TITLE.equals(tagName)
			|| TAG_LINK.equals(tagName)
			|| TAG_DESCRIPTION.equals(tagName)
			|| TAG_LANGUAGE.equals(tagName)
			|| TAG_PUBDATE.equals(tagName)
			|| TAG_GUID.equals(tagName)
			|| TAG_URL.equals(tagName)
			|| TAG_DIGG_DIGGCOUNT.equals(tagName)
			|| TAG_DIGG_USERNAME.equals(tagName)
			|| TAG_DIGG_USERIMAGE.equals(tagName)
			|| TAG_DIGG_CATEGORY.equals(tagName)
			|| TAG_DIGG_COMMENTCOUNT.equals(tagName)) {
			if (opening) {
				parser.next();
			}
			return true;
		}

		return false;
	}

	protected void addRssItem(String title, String description, String url)
	{
		//#style browserLink
		StringItem item = new StringItem(null, title, de.enough.polish.ui.StyleSheet.browserlinkStyle );
		item.setAppearanceMode(Item.HYPERLINK);
		item.setDefaultCommand(CMD_RSS_ITEM_SELECT);
		item.setItemCommandListener(this.itemListener);
		item.setAttribute(ATTR_RSS_ITEM, new RssItem(title, description, this.url));
		item.addCommand(this.linkCommand);

		if (this.url != null) {
			item.setAttribute(HtmlTagHandler.ATTR_HREF, url);
		}

		this.browser.add(item);
	}

	/**
	 * Determines whether RSS descriptions should be included directly on the overview page
	 * 
	 * @return true when descriptions should be included
	 */
	public boolean isIncludeDescriptions() {
		return this.includeDescriptions;
	}
	

	/**
	 * Specifies whether RSS descriptions should be included directly on the overview page
	 * @param includeDescriptions true when descriptions should be included
	 */
	public void setIncludeDescriptions(boolean includeDescriptions)
	{
		this.includeDescriptions = includeDescriptions;
	}
}
