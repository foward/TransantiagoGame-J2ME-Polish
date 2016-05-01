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

import java.util.Enumeration;
import java.util.Hashtable;

import de.enough.polish.browser.Browser;
import de.enough.polish.browser.TagHandler;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.Container;
import de.enough.polish.ui.ImageItem;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.ItemCommandListener;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.TableItem;
import de.enough.polish.ui.TextField;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.HashMap;
import de.enough.polish.util.Locale;
import de.enough.polish.util.TextUtil;
import de.enough.polish.xml.SimplePullParser;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

//#if polish.cldc1.0
	//# import de.enough.polish.util.TextUtil;
//#endif

/**
 * Handles HTML tags.
 */
public class HtmlTagHandler
  extends TagHandler
  implements ItemCommandListener
{
	/** title tag */
  public static final String TAG_TITLE = "title";
	/** style tag */
  public static final String TAG_STYLE = "style";
	/** br tag */
  public static final String TAG_BR = "br";
	/** p tag */
  public static final String TAG_P = "p";
	/** img tag */
  public static final String TAG_IMG = "img";
	/** div tag */
  public static final String TAG_DIV = "div";
	/** span tag */
  public static final String TAG_SPAN = "span";
	/** a tag */
  public static final String TAG_A = "a";
	/** b tag */
  public static final String TAG_B = "b";
	/** strong tag */
  public static final String TAG_STRONG = "strong";
	/** i tag */
  public static final String TAG_I = "i";
	/** em tag */
  public static final String TAG_EM = "em";
	/** form tag */
  public static final String TAG_FORM = "form";
	/** input tag */
  public static final String TAG_INPUT = "input";
	/** button tag */
  public static final String TAG_BUTTON = "button";
	/** text area tag */
  public static final String TAG_TEXT_AREA = "textarea";
	/** select tag */
  public static final String TAG_SELECT = "select";
	/** option tag */
  public static final String TAG_OPTION = "option";
	/** script tag */
  public static final String TAG_SCRIPT = "script";
	/** table tag */
  public static final String TAG_TABLE = "table";
	/** table row tag */
  public static final String TAG_TR = "tr";
	/** table header tag */
  public static final String TAG_TH = "th";
	/** table data tag */
  public static final String TAG_TD = "td";
  
	/** type attribute */
  public static final String INPUT_TYPE = "type";
	/** name attribute */
  public static final String INPUT_NAME = "name";
	/** value attribute */
  public static final String INPUT_VALUE = "value";

	/** text type-value */
  public static final String INPUTTYPE_TEXT = "text";
	/** hidden type-value */
  public static final String INPUTTYPE_HIDDEN = "hidden";
	/** submit type-value */
  public static final String INPUTTYPE_SUBMIT = "submit";
  
	/** href attribute */
  public static final String ATTR_HREF = "href";
	/** form attribute */
  public static final String ATTR_FORM = "polish_form";
	/** type attribute */
  public static final String ATTR_TYPE = "type";
	/** value attribute */
  public static final String ATTR_VALUE = "value";
	/** name attribute */
  public static final String ATTR_NAME = "name";
  	/** size attribute */
  	public static final String ATTR_SIZE = "size";
  	/** multiple attribute */
  	public static final String ATTR_MULTIPLE = "multiple";

  /** default link command */
	//#ifdef polish.i18n.useDynamicTranslations
		//# public static Command CMD_LINK = new Command( "Go", Command.OK, 2 );
	//#elifdef polish.command.followlink:defined
public static final Command CMD_LINK = new Command("Go", Command.OK, 1 );
	//#else
		//# public static final Command CMD_LINK = new Command("Go", Command.OK, 1);
	//#endif
	/** default submit command */
	//#ifdef polish.i18n.useDynamicTranslations
		//# public static Command CMD_SUBMIT = new Command( "Submit", Command.ITEM, 2 );
	//#elifdef polish.command.submit:defined
public static final Command CMD_SUBMIT = new Command("Submit", Command.ITEM, 2 );
	//#else
		//# public static final Command CMD_SUBMIT = new Command("Submit", Command.ITEM, 2);
	//#endif
	/** default back command */
	//#ifdef polish.i18n.useDynamicTranslations
		//# public static Command CMD_BACK = new Command( "Back", Command.BACK, 10 );
	//#elifdef polish.command.back:defined
public static final Command CMD_BACK = new Command("Back", Command.BACK, 10 );
	//#else
		//# public static final Command CMD_BACK = new Command("Back", Command.BACK, 10);
	//#endif
  
  private HtmlForm currentForm;
  private HtmlSelect currentSelect;
  private TableItem currentTable;
  
  protected HtmlBrowser browser;

  /** next text should be added in bold font style */
  public boolean textBold;
  /** next text should be added in italic font style */
  public boolean textItalic;
  /** style for the forthcoming text */
  public Style textStyle;
private FormListener formListener;
  
  /**
   * Creates a new html tag handler
   */
  public HtmlTagHandler() {
	  //#ifdef polish.i18n.useDynamicTranslations
	  	//# if ("Go" != CMD_LINK.getLabel()) {
	  		//# CMD_LINK = new Command( "Go", Command.OK, 2 );
	  		//# CMD_SUBMIT = new Command( "Submit", Command.ITEM, 2 );
	  		//# CMD_BACK = new Command( "Back", Command.BACK, 10 );
	  	//# }
	  //#endif
  }
  
  public void register(Browser parent)
  {
    this.browser = (HtmlBrowser) parent;
    this.textBold = false;
    this.textItalic = false;
    
    parent.addTagHandler(TAG_TITLE, this);
    parent.addTagHandler(TAG_STYLE, this);
    
    parent.addTagHandler(TAG_BR, this);
    parent.addTagHandler(TAG_P, this);
    parent.addTagHandler(TAG_IMG, this);
    parent.addTagHandler(TAG_DIV, this);
    parent.addTagHandler(TAG_SPAN, this);
    parent.addTagHandler(TAG_A, this);
    parent.addTagHandler(TAG_B, this);
    parent.addTagHandler(TAG_STRONG, this);
    parent.addTagHandler(TAG_I, this);
    parent.addTagHandler(TAG_EM, this);
    parent.addTagHandler(TAG_FORM, this);
    parent.addTagHandler(TAG_INPUT, this);
    parent.addTagHandler(TAG_BUTTON, this);
    parent.addTagHandler(TAG_SELECT, this);
    parent.addTagHandler(TAG_OPTION, this);
    parent.addTagHandler(TAG_SCRIPT, this);
    parent.addTagHandler(TAG_TEXT_AREA, this);
    parent.addTagHandler(TAG_TABLE, this);
    parent.addTagHandler(TAG_TR, this);
    parent.addTagHandler(TAG_TH, this);
    parent.addTagHandler(TAG_TD, this);
  }

  /* (non-Javadoc)
   * @see de.enough.polish.browser.TagHandler#handleTag(de.enough.polish.ui.Container, de.enough.polish.xml.PullParser, java.lang.String, boolean, de.enough.polish.util.HashMap, de.enough.polish.ui.Style)
   */
  public boolean handleTag(Container parentItem, SimplePullParser parser, String tagName, boolean opening, HashMap attributeMap, Style style)
  {
	  //#debug
	  //# System.out.println("checking tag " + tagName );
	  tagName = tagName.toLowerCase();
	  if (TAG_DIV.equals(tagName) || TAG_SPAN.equals(tagName)) {
		  if (opening) {
			  this.browser.openContainer( style );
 		  } else {
 			  Container container = this.browser.closeContainer();
 			  if (UiAccess.cast(container) instanceof TableItem) {
 				  this.currentTable = (TableItem)UiAccess.cast(container);
 			  }
		  }
	  } else if (TAG_SELECT.equals(tagName)) {
    	  if (opening) {
    		  if (this.currentSelect != null) {
    			  //#debug error
    			  //# System.out.println("Error in HTML-Code: You cannot open a <select>-tag inside another <select>-tag.");

    			  ChoiceGroup choiceGroup = this.currentSelect.getChoiceGroup();
    			  add(choiceGroup);
    			  if (this.currentForm == null) {
    				  //#debug error
    				  //# System.out.println("Error in HTML-Code: no <form> for <select> element found!");
    			  } else {
    				  this.currentForm.addItem(choiceGroup);
    			  }
    			  this.currentSelect = null;
    		  }

    		  String name = parser.getAttributeValue(ATTR_NAME);
    		  String sizeStr = parser.getAttributeValue(ATTR_SIZE);
    		  int size;

    		  try {
    			  size = Integer.parseInt(sizeStr);
    		  }
    		  catch (NumberFormatException e) {
    			  size = -1;
    		  }

    		  boolean isMultiple = parser.getAttributeValue(ATTR_MULTIPLE) != null;
    		  this.currentSelect = new HtmlSelect(name, size, isMultiple, style);
    	  } else { // tag is closed
    		  if (this.currentSelect != null) {
    			  ChoiceGroup choiceGroup = this.currentSelect.getChoiceGroup();
    			  add(choiceGroup);
    			  if (this.currentForm == null) {
    				  //#debug error
    				  //# System.out.println("Error in HTML-Code: no <form> for <select> element found!");
    			  } else {
    				  this.currentForm.addItem(choiceGroup);
    			  }
    			  this.currentSelect = null;
    		  }
    		  //#mdebug error
    		  //# else {
    			  //#debug error
    			  //# System.out.println("Error in HTML-Code. You cannot close a <select>-tag without opening one.");
    		  //# }
    		  //#enddebug
    	  }
    	  return true;
      }
      else if (TAG_OPTION.equals(tagName)) {
    	  if (this.currentSelect != null && opening) {
    		  // TODO: handle "selected" attribute.
    		  String value = parser.getAttributeValue(ATTR_VALUE);
    		  String selected = parser.getAttributeValue("selected");
    		  parser.next();
    		  String name = parser.getText();

    		  if (value == null) {
    			  value = name;
    		  }

    		  this.currentSelect.addOption(name, value, selected != null, style);
    	  }
    	  return true;
      }

    if (opening)
    {    
      if (TAG_TITLE.equals(tagName))
      {
        // Hack to read title.
        parser.next();
        String name = parser.getText();
        Screen myScreen = this.browser.getScreen();
        if (name != null && myScreen != null) {
        	myScreen.setTitle( name );
        }
        return true; 
      }
      else if (TAG_STYLE.equals(tagName))
      {
        // Hack to read style content.
        parser.next();
        return true;
      }
      else if (TAG_A.equals(tagName))
      {
        String href = (String) attributeMap.get(ATTR_HREF);
        parser.next();
        Item linkItem;
        if (href != null)
        {
        	String anchorText = parser.getText();
        	// hack for image links:
        	if ("".equals(anchorText) && TAG_IMG.equals(parser.getName())) {
        		// this is an image link:
        		attributeMap.clear();
        		for (int i = 0; i < parser.getAttributeCount(); i++)
        	    {
        	      String attributeName = parser.getAttributeName(i);
        	      String attributeValue = parser.getAttributeValue(i);
        	      attributeMap.put(attributeName, attributeValue);
        	    }
                String src = (String) attributeMap.get("src");
                String url = this.browser.makeAbsoluteURL(src);
                Image image = this.browser.loadImage(url);
                //#style browserLink
                linkItem = new ImageItem(null, image, 0, (String) attributeMap.get("alt") , de.enough.polish.ui.StyleSheet.browserlinkStyle );
        		//this.browser.loadImageLater( url, (ImageItem) linkItem );
        		
        	} else {
        		//#style browserLink
        		linkItem = new StringItem(null, anchorText, de.enough.polish.ui.StyleSheet.browserlinkStyle );
        	}
    	    linkItem.setDefaultCommand(CMD_LINK);
    	    linkItem.setItemCommandListener( this );
    	    linkItem.setAttribute(ATTR_HREF, href );
    	    addCommands(TAG_A, linkItem);
        }
        else
        {
        	//#style browserText
        	linkItem = new StringItem(null, parser.getText(), de.enough.polish.ui.StyleSheet.browsertextStyle );
        }
		if (style != null) {
			linkItem.setStyle(style);
		}
        add(linkItem);
        return true;
      }
      else if (TAG_BR.equals(tagName))
      {
        // TODO: Can we do this without adding a dummy StringItem?
        StringItem stringItem = new StringItem(null, null);
        stringItem.setLayout(Item.LAYOUT_NEWLINE_AFTER);
        add(stringItem);
        return true;
      }
      else if (TAG_P.equals(tagName))
      {
        StringItem stringItem = new StringItem(null, null);
        stringItem.setLayout(Item.LAYOUT_NEWLINE_AFTER);
        add(stringItem);
        if (opening) {
        	this.textStyle = style;
        }
        return true;
      }
      else if (TAG_IMG.equals(tagName))
      {
        String src = (String) attributeMap.get("src");
        String url = this.browser.makeAbsoluteURL(src);
        Image image = this.browser.loadImage(url);
        if (image != null) {
        	ImageItem item = new ImageItem(null, image, Item.LAYOUT_DEFAULT, "");
    		if (style != null) {
    			item.setStyle(style);
    		}

        	add(item);
        }
        return true;
      }
      else if (TAG_TEXT_AREA.equals(tagName)) 
      {
    	  parser.next();
    	  String value = parser.getText();
    	  int maxCharNumber = 500;
    	  String cols = (String) attributeMap.get("cols");
    	  String rows = (String) attributeMap.get("rows");
    	  if (cols != null && rows != null) {
    		  try {
    			  maxCharNumber = Integer.parseInt(cols) * Integer.parseInt(rows);
    		  } catch (Exception e) {
    			  //#debug error
    			  //# System.out.println("Unable to parse textarea cols or rows attribute: cols=" + cols + ", rows=" + rows);
    		  }
    	  }
          //#style browserInput
          TextField textField = new TextField(null, value, maxCharNumber, TextField.ANY, de.enough.polish.ui.StyleSheet.browserinputStyle );
          if (style != null) {
          	textField.setStyle(style);
          }
          add(textField);
          if (this.currentForm != null) {
        	  this.currentForm.addItem(textField);
        	  textField.setAttribute(ATTR_FORM, this.currentForm);
              String name = (String) attributeMap.get(INPUT_NAME);
              if (value == null) {
              	value = name;
              }
              if (name != null) {
              	textField.setAttribute(ATTR_NAME, name);
            	textField.setAttribute(ATTR_VALUE, value);            	  
              }
          }
          return true;
      }
      else if (TAG_BUTTON.equals(tagName) && this.currentForm != null) {
          String name = (String) attributeMap.get(INPUT_NAME);
          String value = (String) attributeMap.get(INPUT_VALUE);

          if (value == null) {
          	value = name;
          }

          //#style browserLink
          StringItem buttonItem = new StringItem(null, value, de.enough.polish.ui.StyleSheet.browserlinkStyle );
          if (style != null) {
          	buttonItem.setStyle(style);
          }
          buttonItem.setDefaultCommand(CMD_SUBMIT);
          buttonItem.setItemCommandListener(this);
          addCommands(TAG_INPUT, INPUT_TYPE, INPUTTYPE_SUBMIT, buttonItem);
          add(buttonItem);
          
          this.currentForm.addItem(buttonItem);
          buttonItem.setAttribute(ATTR_FORM, this.currentForm);
          buttonItem.setAttribute(ATTR_TYPE, "submit");

          if (name != null) {
          	buttonItem.setAttribute(ATTR_NAME, name);
          	buttonItem.setAttribute(ATTR_VALUE, value);
          }    	  
      }
      else if (TAG_INPUT.equals(tagName))
      {
        if (this.currentForm != null)
        {
          String type = (String) attributeMap.get(INPUT_TYPE);
          String name = (String) attributeMap.get(INPUT_NAME);
          String value = (String) attributeMap.get(INPUT_VALUE);
          
          if (this.formListener != null && name != null) {
        	  value = this.formListener.verifyInitialFormValue(this.currentForm.getAction(),  name, value);
          }
          
          if (INPUTTYPE_TEXT.equals(type))
          {
            //#style browserInput
            TextField textField = new TextField(null, value, 100, TextField.ANY, de.enough.polish.ui.StyleSheet.browserinputStyle );
            if (style != null) {
            	textField.setStyle(style);
            }
            add(textField);
            
            this.currentForm.addItem(textField);
            textField.setAttribute(ATTR_FORM, this.currentForm);

            if (name != null) {
            	textField.setAttribute(ATTR_NAME, name);
            	if (value == null) {
            		value = "";
            	}
            	textField.setAttribute(ATTR_VALUE, value);
            }
          }
          else if (INPUTTYPE_SUBMIT.equals(type))
          {

            if (value == null) {
            	value = name;
            }

            //#style browserLink
            StringItem buttonItem = new StringItem(null, value, de.enough.polish.ui.StyleSheet.browserlinkStyle );
            if (style != null) {
            	buttonItem.setStyle(style);
            }
            buttonItem.setDefaultCommand(CMD_SUBMIT);
            buttonItem.setItemCommandListener(this);
            addCommands(TAG_INPUT, INPUT_TYPE, INPUTTYPE_SUBMIT, buttonItem);
            add(buttonItem);
            
            this.currentForm.addItem(buttonItem);
            buttonItem.setAttribute(ATTR_FORM, this.currentForm);
            buttonItem.setAttribute(ATTR_TYPE, "submit");

            if (name != null) {
            	buttonItem.setAttribute(ATTR_NAME, name);
            	buttonItem.setAttribute(ATTR_VALUE, value);
            }
          }
          else if (INPUTTYPE_HIDDEN.equals(type)) {
        	  this.currentForm.addHiddenElement( name, value );
          }
          //#if polish.debug.debug
          //# else
          //# {
            //#debug
            //# System.out.println("unhandled html form input type: " + type);
          //# }
          //#endif
        }

        return true;
      }
      else if (TAG_SCRIPT.equals(tagName)) {
    	  // Consume javascript code.
    	  parser.next();
    	  return true;
      }
      else if (TAG_TABLE.equals(tagName)) {
    	  //#style browserTable?
    	  TableItem table  = new TableItem();
    	  table.setSelectionMode( TableItem.SELECTION_MODE_CELL | TableItem.SELECTION_MODE_INTERACTIVE );
    	  table.setCellContainerStyle( this.browser.getStyle() );
    	  if (style != null) {
    		  table.setStyle(style);
    	  }
    	  
    	  this.currentTable = table;
    	  this.browser.openContainer(table);
    	  return true;
      }
      else if (this.currentTable != null && TAG_TR.equals(tagName)) {
    	  this.currentTable.moveToNextRow();
    	  return true;
      }
      else if (this.currentTable != null && TAG_TH.equals(tagName)) {
    	  //TODO differentiate between th and td
    	  this.currentTable.moveToNextColumn();
    	  return true;
      }
      else if (this.currentTable != null && TAG_TD.equals(tagName)) {
    	  //TODO differentiate between th and td
    	  this.currentTable.moveToNextColumn();
    	  return true;
      }
    }
    else 
    {
    	// the tag is being closed:
    	if (TAG_TABLE.equals(tagName)) {
    		Container container = this.browser.closeContainer();
    		if (UiAccess.cast(container) instanceof TableItem) {
    			this.currentTable = (TableItem)UiAccess.cast(container);
    		} else {
    			this.currentTable = null;
    		}
    		return true;
    	}
    }
   
    
    
    if (TAG_B.equals(tagName)
      || TAG_STRONG.equals(tagName))
    {
      this.textBold = opening;
      return true;
    }
    else if (TAG_I.equals(tagName)
      || TAG_EM.equals(tagName))
    {
      this.textItalic = opening;
      return true;
    }
    else if (TAG_FORM.equals(tagName))
    {
      if (opening)
      {
	    String name = (String) attributeMap.get("name");	
        String action = (String) attributeMap.get("action");
        String method = (String) attributeMap.get("method");
        
        if (method == null)
        {
          method = "GET";
        }
        this.currentForm = new HtmlForm(name, action, method.toUpperCase());
      }
      else
      {
        this.currentForm = null;
      }
      
      return true;
    }

    return false;
  }

   /**
     * Adds an item either to the browser or to the current table.
	 * @param item the item
	 */
	private void add(Item item)
	{
		//System.out.println("adding " + item + ", currentTable=" + this.currentTable);
		this.browser.add(item);
	}

/* (non-Javadoc)
   * @see de.enough.polish.browser.TagHandler#handleCommand(javax.microedition.lcdui.Command)
   */
  public boolean handleCommand(Command command)
  {
    if (command == CMD_LINK)
    {
      handleLinkCommand();
      return true;
    }
    else if (command == CMD_SUBMIT)
    {
      handleSubmitCommand();
      return true;
    }
    else if (command == CMD_BACK)
    {
      handleBackCommand();
      return true;
    }
    
    return false;
  }

  protected void handleBackCommand()
  {
    this.browser.goBack();
  }
  
  /**
   * Creates a Form GET method URL for the specified browser.
   * 
   * @return the GET URL or null when the browser's current item is not a Submit button
   */
  public String createGetSubmitCall()
  {
	    Item submitItem = this.browser.getFocusedItem();
	    HtmlForm form = (HtmlForm) submitItem.getAttribute(ATTR_FORM);
  		while (form == null && (submitItem instanceof Container)) {
  			submitItem = ((Container)submitItem).getFocusedItem();
  			form = (HtmlForm) submitItem.getAttribute(ATTR_FORM);
  		}
  		return createGetSubmitCall(submitItem, form);
  }
  /**
   * Creates a Form GET method URL for the specified browser.
   * 
 * @param submitItem the item that triggered the action
 * @param form the form that contains necessary data
   * @return the GET URL or null when the browser's current item is not a Submit button
   */
  public String createGetSubmitCall(Item submitItem, HtmlForm form)
  {

	  if (form == null)
	  {
	  	return null;
	  }

    StringBuffer sb = new StringBuffer();
    sb.append( this.browser.makeAbsoluteURL(form.getAction()) );
    Hashtable elements = form.getFormElements(this.formListener, submitItem);
    Enumeration enumeration = elements.keys();
    char separatorChar = '?';
	while (enumeration.hasMoreElements()) {
		String name = (String) enumeration.nextElement();
		String value = (String) elements.get(name);
		value = TextUtil.encodeUrl(value);
		sb.append(separatorChar);
		sb.append(name).append('=').append( value );
		separatorChar = '&';
	}
    return sb.toString();
  }

  	/**
  	 * Does a Form POST method call.
  	 * @param submitItem the item triggering the call
  	 * @param form the form containing the data
  	 */
  	public void doPostSubmitCall(Item submitItem, HtmlForm form )
  	{
	
		if (form == null) {
		  	return;
		}
	
	    StringBuffer sb = new StringBuffer();
	    
	    Hashtable elements = form.getFormElements(this.formListener, submitItem);
	    Enumeration enumeration = elements.keys();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = (String) elements.get(name);
			value = TextUtil.encodeUrl(value);
			sb.append(name).append('=').append( value );
			if (enumeration.hasMoreElements()) {
				sb.append('&');
			}
		}

	    this.browser.go( this.browser.makeAbsoluteURL( form.getAction() ), sb.toString());
  	}

  	protected void handleSubmitCommand()
  	{
  		Item submitItem = this.browser.getFocusedItem();
  		HtmlForm form = (HtmlForm) submitItem.getAttribute(ATTR_FORM);
  		while (form == null && (submitItem instanceof Container)) {
  			submitItem = ((Container)submitItem).getFocusedItem();
  			form = (HtmlForm) submitItem.getAttribute(ATTR_FORM);
  		}
  		if (form == null) {
  			return;
  		}

  		if (form.isPost()) {
  			doPostSubmitCall(submitItem, form );
  		}
  		else {
  			String url = createGetSubmitCall(submitItem, form);
  			this.browser.go(url);
  		}
  	}

  protected void handleLinkCommand()
  {
	  
    Item linkItem = getFocusedItemWithAttribute( ATTR_HREF, this.browser );
    String href = (String) linkItem.getAttribute(ATTR_HREF);
    if (href != null) {
    	this.browser.go(this.browser.makeAbsoluteURL(href));
    }
    //#if polish.debug.error
    //# else {
    	//#debug error
    	//# System.out.println("Unable to handle link command for item " + linkItem + ": no " + ATTR_HREF + " attribute found.");
    //# }
    //#endif
  }
  
  	//#if polish.LibraryBuild
	//# private Item getFocusedItemWithAttribute(String attribute, de.enough.polish.ui.FakeContainerCustomItem container )
	//# {
		//# return null;
	//# }
	//#endif
  
	/**
	 * Retrieves the currently focused item that has specified the attribute
	 * @param attribute the attribute
	 * @param container the container that should have focused the item
	 * @return the item that contains the attribute or the focused item which is not a Container itself
	 */
	protected Item getFocusedItemWithAttribute(String attribute, Container container )
	{
		Item item = container.getFocusedItem();
		if (item.getAttribute(attribute) == null && item instanceof Container) {
			return getFocusedItemWithAttribute(attribute, (Container) item );
		}
		return item;
	}

/**
   * Handles item commands (implements ItemCommandListener).
   * 
   * @param command the command
   * @param item the item from which the command originates
   */
	public void commandAction(Command command, Item item)
	{
		handleCommand(command);
	}

	  /**
	   * Sets the form listener that is notified about form creation and submission events
	   * 
	   * @param listener the listener, use null for de-registering a previous listener
	   */
	public void setFormListener(FormListener listener)
	{
		this.formListener = listener;
	}
}
