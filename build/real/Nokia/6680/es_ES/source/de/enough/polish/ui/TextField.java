/*
 * TODOs
 * 
 * - bitmapfont needs testing
 * - text effects
 * - layout: center, right
 */

//#condition polish.usePolishGui
/*
 * Copyright (c) 2004-2007 Robert Virkus / Enough Software
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
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
//#if polish.TextField.useDirectInput && polish.TextField.usePredictiveInput && !polish.blackberry
	//# import de.enough.polish.predictive.TextBuilder;
	//# import de.enough.polish.predictive.trie.TrieProvider;
//#endif
import de.enough.polish.util.DrawUtil;
import de.enough.polish.util.Locale;

//#if polish.blackberry
	//# import de.enough.polish.blackberry.ui.PolishTextField;
	//# import de.enough.polish.blackberry.ui.PolishEditField;
	//# import de.enough.polish.blackberry.ui.PolishPasswordEditField;
	//# import net.rim.device.api.system.Application;
	//# import net.rim.device.api.ui.Field;
	//# import net.rim.device.api.ui.FieldChangeListener;
	//# import net.rim.device.api.ui.Manager;
	//# import net.rim.device.api.ui.UiApplication;
//# import net.rim.device.api.ui.component.BasicEditField;
//#endif

//#if polish.api.windows
	//# import de.enough.polish.windows.Keyboard;
//#endif

/**
 * A <code>TextField</code> is an editable text component that may be
 * placed into
 * a <A HREF="../../../javax/microedition/lcdui/Form.html"><CODE>Form</CODE></A>. It can be
 * given a piece of text that is used as the initial value.
 * 
 * <P>A <code>TextField</code> has a maximum size, which is the
 * maximum number of characters
 * that can be stored in the object at any time (its capacity). This limit is
 * enforced when the <code>TextField</code> instance is constructed,
 * when the user is editing text within the <code>TextField</code>, as well as
 * when the application program calls methods on the
 * <code>TextField</code> that modify its
 * contents. The maximum size is the maximum stored capacity and is unrelated
 * to the number of characters that may be displayed at any given time.
 * The number of characters displayed and their arrangement into rows and
 * columns are determined by the device. </p>
 * 
 * <p>The implementation may place a boundary on the maximum size, and the
 * maximum size actually assigned may be smaller than the application had
 * requested.  The value actually assigned will be reflected in the value
 * returned by <A HREF="../../../javax/microedition/lcdui/TextField.html#getMaxSize()"><CODE>getMaxSize()</CODE></A>.  A defensively-written
 * application should compare this value to the maximum size requested and be
 * prepared to handle cases where they differ.</p>
 * 
 * <a name="constraints"></a>
 * <h3>Input Constraints</h3>
 * 
 * <P>The <code>TextField</code> shares the concept of <em>input
 * constraints</em> with the <A HREF="../../../javax/microedition/lcdui/TextBox.html"><CODE>TextBox</CODE></A> class. The different
 * constraints allow the application to request that the user's input be
 * restricted in a variety of ways. The implementation is required to
 * restrict the user's input as requested by the application. For example, if
 * the application requests the <code>NUMERIC</code> constraint on a
 * <code>TextField</code>, the
 * implementation must allow only numeric characters to be entered. </p>
 * 
 * <p>The <em>actual contents</em> of the text object are set and modified by
 * and are
 * reported to the application through the <code>TextBox</code> and
 * <code>TextField</code> APIs.  The <em>displayed contents</em> may differ
 * from the actual contents if the implementation has chosen to provide
 * special formatting suitable for the text object's constraint setting.
 * For example, a <code>PHONENUMBER</code> field might be displayed with
 * digit separators and punctuation as
 * appropriate for the phone number conventions in use, grouping the digits
 * into country code, area code, prefix, etc. Any spaces or punctuation
 * provided are not considered part of the text object's actual contents. For
 * example, a text object with the <code>PHONENUMBER</code>
 * constraint might display as
 * follows:</p>
 * 
 * <pre><code>
 * (408) 555-1212    
 * </code></pre>
 * 
 * <p>but the actual contents of the object visible to the application
 * through the APIs would be the string
 * &quot;<code>4085551212</code>&quot;.
 * The <code>size</code> method reflects the number of characters in the
 * actual contents, not the number of characters that are displayed, so for
 * this example the <code>size</code> method would return <code>10</code>.</p>
 * 
 * <p>Some constraints, such as <code>DECIMAL</code>, require the
 * implementation to perform syntactic validation of the contents of the text
 * object.  The syntax checking is performed on the actual contents of the
 * text object, which may differ from the displayed contents as described
 * above.  Syntax checking is performed on the initial contents passed to the
 * constructors, and it is also enforced for all method calls that affect the
 * contents of the text object.  The methods and constructors throw
 * <code>IllegalArgumentException</code> if they would result in the contents
 * of the text object not conforming to the required syntax.</p>
 * 
 * <p>The value passed to the <A HREF="../../../javax/microedition/lcdui/TextField.html#setConstraints(int)"><CODE>setConstraints()</CODE></A> method
 * consists of a restrictive constraint setting described above, as well as a
 * variety of flag bits that modify the behavior of text entry and display.
 * The value of the restrictive constraint setting is in the low order
 * <code>16</code> bits
 * of the value, and it may be extracted by combining the constraint value
 * with the <code>CONSTRAINT_MASK</code> constant using the bit-wise
 * <code>AND</code> (<code>&amp;</code>) operator.
 * The restrictive constraint settings are as follows:
 * 
 * <blockquote><code>
 * ANY<br>
 * EMAILADDR<br>
 * NUMERIC<br>
 * PHONENUMBER<br>
 * URL<br>
 * DECIMAL<br>
 * </code></blockquote>
 * 
 * <p>The modifier flags reside in the high order <code>16</code> bits
 * of the constraint
 * value, that is, those in the complement of the
 * <code>CONSTRAINT_MASK</code> constant.
 * The modifier flags may be tested individually by combining the constraint
 * value with a modifier flag using the bit-wise <code>AND</code>
 * (<code>&amp;</code>) operator.  The
 * modifier flags are as follows:
 * 
 * <blockquote><code>
 * PASSWORD<br>
 * UNEDITABLE<br>
 * SENSITIVE<br>
 * NON_PREDICTIVE<br>
 * INITIAL_CAPS_WORD<br>
 * INITIAL_CAPS_SENTENCE<br>
 * </code></blockquote>
 * 
 * <a name="modes"></a>
 * <h3>Input Modes</h3>
 * 
 * <p>The <code>TextField</code> shares the concept of <em>input
 * modes</em> with the <A HREF="../../../javax/microedition/lcdui/TextBox.html"><CODE>TextBox</CODE></A> class.  The application can request that the
 * implementation use a particular input mode when the user initiates editing
 * of a <code>TextField</code> or <code>TextBox</code>.  The input
 * mode is a concept that exists within
 * the user interface for text entry on a particular device.  The application
 * does not request an input mode directly, since the user interface for text
 * entry is not standardized across devices.  Instead, the application can
 * request that the entry of certain characters be made convenient.  It can do
 * this by passing the name of a Unicode character subset to the <A HREF="../../../javax/microedition/lcdui/TextField.html#setInitialInputMode(java.lang.String)"><CODE>setInitialInputMode()</CODE></A> method.  Calling this method
 * requests that the implementation set the mode of the text entry user
 * interface so that it is convenient for the user to enter characters in this
 * subset.  The application can also request that the input mode have certain
 * behavioral characteristics by setting modifier flags in the constraints
 * value.
 * 
 * <p>The requested input mode should be used whenever the user initiates the
 * editing of a <code>TextBox</code> or <code>TextField</code> object.
 * If the user had changed input
 * modes in a previous editing session, the application's requested input mode
 * should take precedence over the previous input mode set by the user.
 * However, the input mode is not restrictive, and the user is allowed to
 * change the input mode at any time during editing.  If editing is already in
 * progress, calls to the <code>setInitialInputMode</code> method do not
 * affect the current input mode, but instead take effect at the next time the
 * user initiates editing of this text object.
 * 
 * <p>The initial input mode is a hint to the implementation.  If the
 * implementation cannot provide an input mode that satisfies the
 * application's request, it should use a default input mode.
 * 
 * <P>The input mode that results from the application's request is not a
 * restriction on the set of characters the user is allowed to enter.  The
 * user MUST be allowed to switch input modes to enter any character that is
 * allowed within the current constraint setting.  The constraint
 * setting takes precedence over an input mode request, and the implementation
 * may refuse to supply a particular input mode if it is inconsistent with the
 * current constraint setting.
 * 
 * <P>For example, if the current constraint is <code>ANY</code>, the call</P>
 * 
 * <TABLE BORDER="2">
 * <TR>
 * <TD ROWSPAN="1" COLSPAN="1">
 * <pre><code>
 * setInitialInputMode("MIDP_UPPERCASE_LATIN");    </code></pre>
 * </TD>
 * </TR>
 * </TABLE>
 * 
 * <p>should set the initial input mode to allow entry of uppercase Latin
 * characters.  This does not restrict input to these characters, and the user
 * will be able to enter other characters by switching the input mode to allow
 * entry of numerals or lowercase Latin letters.  However, if the current
 * constraint is <code>NUMERIC</code>, the implementation may ignore
 * the request to set an
 * initial input mode allowing <code>MIDP_UPPERCASE_LATIN</code>
 * characters because these
 * characters are not allowed in a <code>TextField</code> whose
 * constraint is <code>NUMERIC</code>.  In
 * this case, the implementation may instead use an input mode that allows
 * entry of numerals, since such an input mode is most appropriate for entry
 * of data under the <code>NUMERIC</code> constraint.
 * 
 * <P>A string is used to name the Unicode character subset passed as a
 * parameter to the
 * <A HREF="../../../javax/microedition/lcdui/TextField.html#setInitialInputMode(java.lang.String)"><CODE>setInitialInputMode()</CODE></A> method.
 * String comparison is case sensitive.
 * 
 * <P>Unicode character blocks can be named by adding the prefix
 * &quot;<code>UCB</code>_&quot; to the
 * the string names of fields representing Unicode character blocks as defined
 * in the J2SE class <code>java.lang.Character.UnicodeBlock</code>.  Any
 * Unicode character block may be named in this fashion.  For convenience, the
 * most common Unicode character blocks are listed below.
 * 
 * <blockquote><code>
 * UCB_BASIC_LATIN<br>
 * UCB_GREEK<br>
 * UCB_CYRILLIC<br>
 * UCB_ARMENIAN<br>
 * UCB_HEBREW<br>
 * UCB_ARABIC<br>
 * UCB_DEVANAGARI<br>
 * UCB_BENGALI<br>
 * UCB_THAI<br>
 * UCB_HIRAGANA<br>
 * UCB_KATAKANA<br>
 * UCB_HANGUL_SYLLABLES<br>
 * </code></blockquote>
 * 
 * <P>&quot;Input subsets&quot; as defined by the J2SE class
 * <code>java.awt.im.InputSubset</code> may be named by adding the prefix
 * &quot;<code>IS_</code>&quot; to the string names of fields
 * representing input subsets as defined
 * in that class.  Any defined input subset may be used.  For convenience, the
 * names of the currently defined input subsets are listed below.
 * 
 * <blockquote><code>
 * IS_FULLWIDTH_DIGITS<br>
 * IS_FULLWIDTH_LATIN<br>
 * IS_HALFWIDTH_KATAKANA<br>
 * IS_HANJA<br>
 * IS_KANJI<br>
 * IS_LATIN<br>
 * IS_LATIN_DIGITS<br>
 * IS_SIMPLIFIED_HANZI<br>
 * IS_TRADITIONAL_HANZI<br>
 * </code></blockquote>
 * 
 * <P>MIDP has also defined the following character subsets:
 * 
 * <blockquote>
 * <code>MIDP_UPPERCASE_LATIN</code> - the subset of
 * <code>IS_LATIN</code> that corresponds to
 * uppercase Latin letters
 * </blockquote>
 * <blockquote>
 * <code>MIDP_LOWERCASE_LATIN</code> - the subset of
 * <code>IS_LATIN</code> that corresponds to
 * lowercase Latin letters
 * </blockquote>
 * 
 * <p>
 * Finally, implementation-specific character subsets may be named with
 * strings that have a prefix of &quot;<code>X_</code>&quot;.  In
 * order to avoid namespace conflicts,
 * it is recommended that implementation-specific names include the name of
 * the defining company or organization after the initial
 * &quot;<code>X_</code>&quot; prefix.
 * 
 * <p> For example, a Japanese language application might have a particular
 * <code>TextField</code> that the application intends to be used
 * primarily for input of
 * words that are &quot;loaned&quot; from languages other than Japanese.  The
 * application might request an input mode facilitating Hiragana input by
 * issuing the following method call:</p>
 * 
 * <TABLE BORDER="2">
 * <TR>
 * <TD ROWSPAN="1" COLSPAN="1">
 * <pre><code>
 * textfield.setInitialInputMode("UCB_HIRAGANA");       </code></pre>
 * </TD>
 * </TR>
 * </TABLE>
 * <h3>Implementation Note</h3>
 * 
 * <p>Implementations need not compile in all the strings listed above.
 * Instead, they need only to compile in the strings that name Unicode
 * character subsets that they support.  If the subset name passed by the
 * application does not match a known subset name, the request should simply
 * be ignored without error, and a default input mode should be used.  This
 * lets implementations support this feature reasonably inexpensively.
 * However, it has the consequence that the application cannot tell whether
 * its request has been accepted, nor whether the Unicode character subset it
 * has requested is actually a valid subset.
 * <HR>
 * 
 * @author Robert Virkus, robert@enough.de
 * @author Andrew Barnes, andy@geni.com.au basic implementation of direct input
 * @since MIDP 1.0
 */
public class TextField extends StringItem
//#if polish.TextField.useDirectInput && !polish.blackberry
	//#define tmp.forceDirectInput
	//#define tmp.directInput
//#elif polish.css.textfield-direct-input && !polish.blackberry
	//#define tmp.directInput
	//#define tmp.allowDirectInput
//#endif
//#if tmp.directInput && polish.TextField.usePredictiveInput && !polish.blackberry
	//#define tmp.usePredictiveInput
//#endif
//#if polish.TextField.supportSymbolsEntry && tmp.directInput
	//#define tmp.supportsSymbolEntry
	//#if !polish.css.style.textFieldSymbolTable && !polish.css.style.textFieldSymbolList 
		//#abort You need to define the ".textFieldSymbolList" CSS style when enabling the polish.TextField.supportSymbolsEntry option. 
	//#endif
//#endif
//#if !(polish.blackberry || polish.doja) || tmp.supportsSymbolEntry
	//#defineorappend tmp.implements=CommandListener
	//#define tmp.implementsCommandListener
//#endif
//#if polish.TextField.suppressCommands
	//#define tmp.suppressCommands
	//#if polish.key.maybeSupportsAsciiKeyMap
		//#defineorappend tmp.implements=ItemCommandListener
		//#define tmp.implementsItemCommandListener
	//#endif
//#else
 	//#defineorappend tmp.implements=ItemCommandListener
	//#define tmp.implementsItemCommandListener
//#endif
//#if polish.blackberry
	//#defineorappend tmp.implements=FieldChangeListener
//#endif
//#if polish.LibraryBuild
	//#define tmp.implementsCommandListener
	//#define tmp.implementsItemCommandListener
	//# implements CommandListener, ItemCommandListener
	//#if polish.blackberry
	//# //	, FieldChangeListener
	//#endif
//#elif tmp.implements:defined
implements CommandListener, ItemCommandListener
//#endif
{
	/**
	 * The user is allowed to enter any text.
	 * <A HREF="Form.html#linebreak">Line breaks</A> may be entered.
	 * 
	 * <P>Constant <code>0</code> is assigned to <code>ANY</code>.</P>
	 */
	public static final int ANY = 0;

	/**
	 * The user is allowed to enter an e-mail address.
	 * 
	 * <P>Constant <code>1</code> is assigned to <code>EMAILADDR</code>.</P>
	 * 
	 */
	public static final int EMAILADDR = 1;

	/**
	 * The user is allowed to enter only an integer value. The implementation
	 * must restrict the contents either to be empty or to consist of an
	 * optional minus sign followed by a string of one or more decimal
	 * numerals.  Unless the value is empty, it will be successfully parsable
	 * using <A HREF="../../../java/lang/Integer.html#parseInt(java.lang.String)"><CODE>Integer.parseInt(String)</CODE></A>.
	 * 
	 * <P>The minus sign consumes space in the text object.  It is thus
	 * impossible to enter negative numbers into a text object whose maximum
	 * size is <code>1</code>.</P>
	 * 
	 * <P>Constant <code>2</code> is assigned to <code>NUMERIC</code>.</P>
	 * 
	 */
	public static final int NUMERIC = 2;

	/**
	 * The user is allowed to enter a phone number. The phone number is a
	 * special
	 * case, since a phone-based implementation may be linked to the
	 * native phone
	 * dialing application. The implementation may automatically start a phone
	 * dialer application that is initialized so that pressing a single key
	 * would be enough to make a call. The call must not made automatically
	 * without requiring user's confirmation.  Implementations may also
	 * provide a feature to look up the phone number in the device's phone or
	 * address database.
	 * 
	 * <P>The exact set of characters allowed is specific to the device and to
	 * the device's network and may include non-numeric characters, such as a
	 * &quot;+&quot; prefix character.</P>
	 * 
	 * <P>Some platforms may provide the capability to initiate voice calls
	 * using the <A HREF="../../../javax/microedition/midlet/MIDlet.html#platformRequest(java.lang.String)"><CODE>MIDlet.platformRequest</CODE></A> method.</P>
	 * 
	 * <P>Constant <code>3</code> is assigned to <code>PHONENUMBER</code>.</P>
	 * 
	 */
	public static final int PHONENUMBER = 3;

	/**
	 * The user is allowed to enter a URL.
	 * 
	 * <P>Constant <code>4</code> is assigned to <code>URL</code>.</P>
	 * 
	 */
	public static final int URL = 4;

	/**
	 * The user is allowed to enter numeric values with optional decimal
	 * fractions, for example &quot;-123&quot;, &quot;0.123&quot;, or
	 * &quot;.5&quot;.
	 * 
	 * <p>The implementation may display a period &quot;.&quot; or a
	 * comma &quot;,&quot; for the decimal fraction separator, depending on
	 * the conventions in use on the device.  Similarly, the implementation
	 * may display other device-specific characters as part of a decimal
	 * string, such as spaces or commas for digit separators.  However, the
	 * only characters allowed in the actual contents of the text object are
	 * period &quot;.&quot;, minus sign &quot;-&quot;, and the decimal
	 * digits.</p>
	 * 
	 * <p>The actual contents of a <code>DECIMAL</code> text object may be
	 * empty.  If the actual contents are not empty, they must conform to a
	 * subset of the syntax for a <code>FloatingPointLiteral</code> as defined
	 * by the <em>Java Language Specification</em>, section 3.10.2.  This
	 * subset syntax is defined as follows: the actual contents
	 * must consist of an optional minus sign
	 * &quot;-&quot;, followed by one or more whole-number decimal digits,
	 * followed by an optional fraction separator, followed by zero or more
	 * decimal fraction digits.  The whole-number decimal digits may be
	 * omitted if the fraction separator and one or more decimal fraction
	 * digits are present.</p>
	 * 
	 * <p>The syntax defined above is also enforced whenever the application
	 * attempts to set or modify the contents of the text object by calling
	 * a constructor or a method.</p>
	 * 
	 * <p>Parsing this string value into a numeric value suitable for
	 * computation is the responsibility of the application.  If the contents
	 * are not empty, the result can be parsed successfully by
	 * <code>Double.valueOf</code> and related methods if they are present
	 * in the runtime environment. </p>
	 * 
	 * <p>The sign and the fraction separator consume space in the text
	 * object.  Applications should account for this when assigning a maximum
	 * size for the text object.</p>
	 * 
	 * <P>Constant <code>5</code> is assigned to <code>DECIMAL</code>.</p>
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int DECIMAL = 5;

	/**
	 * Indicates that the text entered is confidential data that should be
	 * obscured whenever possible.  The contents may be visible while the
	 * user is entering data.  However, the contents must never be divulged
	 * to the user.  In particular, the existing contents must not be shown
	 * when the user edits the contents.  The means by which the contents
	 * are obscured is implementation-dependent.  For example, each
	 * character of the data might be masked with a
	 * &quot;<code>*</code>&quot; character.  The
	 * <code>PASSWORD</code> modifier is useful for entering
	 * confidential information
	 * such as passwords or personal identification numbers (PINs).
	 * 
	 * <p>Data entered into a <code>PASSWORD</code> field is treated
	 * similarly to <code>SENSITIVE</code>
	 * in that the implementation must never store the contents into a
	 * dictionary or table for use in predictive, auto-completing, or other
	 * accelerated input schemes.  If the <code>PASSWORD</code> bit is
	 * set in a constraint
	 * value, the <code>SENSITIVE</code> and
	 * <code>NON_PREDICTIVE</code> bits are also considered to be
	 * set, regardless of their actual values.  In addition, the
	 * <code>INITIAL_CAPS_WORD</code> and
	 * <code>INITIAL_CAPS_SENTENCE</code> flag bits should be ignored
	 * even if they are set.</p>
	 * 
	 * <p>The <code>PASSWORD</code> modifier can be combined with
	 * other input constraints
	 * by using the bit-wise <code>OR</code> operator (<code>|</code>).
	 * The <code>PASSWORD</code> modifier is not
	 * useful with some constraint values such as
	 * <code>EMAILADDR</code>, <code>PHONENUMBER</code>,
	 * and <code>URL</code>. These combinations are legal, however,
	 * and no exception is
	 * thrown if such a constraint is specified.</p>
	 * 
	 * <p>Constant <code>0x10000</code> is assigned to
	 * <code>PASSWORD</code>.</p>
	 * 
	 */
	public static final int PASSWORD = 0x10000;

	/**
	 * Indicates that editing is currently disallowed.  When this flag is set,
	 * the implementation must prevent the user from changing the text
	 * contents of this object.  The implementation should also provide a
	 * visual indication that the object's text cannot be edited.  The intent
	 * of this flag is that this text object has the potential to be edited,
	 * and that there are circumstances where the application will clear this
	 * flag and allow the user to edit the contents.
	 * 
	 * <p>The <code>UNEDITABLE</code> modifier can be combined with
	 * other input constraints
	 * by using the bit-wise <code>OR</code> operator (<code>|</code>).
	 * 
	 * <p>Constant <code>0x20000</code> is assigned to <code>UNEDITABLE</code>.</p>
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int UNEDITABLE = 0x20000;

	/**
	 * Indicates that the text entered is sensitive data that the
	 * implementation must never store into a dictionary or table for use in
	 * predictive, auto-completing, or other accelerated input schemes.  A
	 * credit card number is an example of sensitive data.
	 * 
	 * <p>The <code>SENSITIVE</code> modifier can be combined with other input
	 * constraints by using the bit-wise <code>OR</code> operator
	 * (<code>|</code>).</p>
	 * 
	 * <p>Constant <code>0x40000</code> is assigned to
	 * <code>SENSITIVE</code>.</p>
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int SENSITIVE = 0x40000;

	/**
	 * Indicates that the text entered does not consist of words that are
	 * likely to be found in dictionaries typically used by predictive input
	 * schemes.  If this bit is clear, the implementation is allowed to (but
	 * is not required to) use predictive input facilities.  If this bit is
	 * set, the implementation should not use any predictive input facilities,
	 * but it instead should allow character-by-character text entry.
	 * 
	 * <p>The <code>NON_PREDICTIVE</code> modifier can be combined
	 * with other input
	 * constraints by using the bit-wise <code>OR</code> operator
	 * (<code>|</code>).
	 * 
	 * <P>Constant <code>0x80000</code> is assigned to
	 * <code>NON_PREDICTIVE</code>.</P>
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int NON_PREDICTIVE = 0x80000;

	/**
	 * This flag is a hint to the implementation that during text editing, the
	 * initial letter of each word should be capitalized.  This hint should be
	 * honored only on devices for which automatic capitalization is
	 * appropriate and when the character set of the text being edited has the
	 * notion of upper case and lower case letters.  The definition of
	 * word boundaries is implementation-specific.
	 * 
	 * <p>If the application specifies both the
	 * <code>INITIAL_CAPS_WORD</code> and the
	 * <code>INITIAL_CAPS_SENTENCE</code> flags,
	 * <code>INITIAL_CAPS_WORD</code> behavior should be used.
	 * 
	 * <p>The <code>INITIAL_CAPS_WORD</code> modifier can be combined
	 * with other input
	 * constraints by using the bit-wise <code>OR</code> operator
	 * (<code>|</code>).
	 * 
	 * <p>Constant <code>0x100000</code> is assigned to
	 * <code>INITIAL_CAPS_WORD</code>.
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int INITIAL_CAPS_WORD = 0x100000;

	/**
	 * This flag is a hint to the implementation that during text editing, the
	 * initial letter of each sentence should be capitalized.  This hint
	 * should be honored only on devices for which automatic capitalization is
	 * appropriate and when the character set of the text being edited has the
	 * notion of upper case and lower case letters.  The definition of
	 * sentence boundaries is implementation-specific.
	 * 
	 * <p>If the application specifies both the
	 * <code>INITIAL_CAPS_WORD</code> and the
	 * <code>INITIAL_CAPS_SENTENCE</code> flags,
	 * <code>INITIAL_CAPS_WORD</code> behavior should be used.
	 * 
	 * <p>The <code>INITIAL_CAPS_SENTENCE</code> modifier can be
	 * combined with other input
	 * constraints by using the bit-wise <code>OR</code> operator
	 * (<code>|</code>).
	 * 
	 * <p>Constant <code>0x200000</code> is assigned to
	 * <code>INITIAL_CAPS_SENTENCE</code>.
	 * 
	 * 
	 * @since MIDP 2.0
	 */
	public static final int INITIAL_CAPS_SENTENCE = 0x200000;

	/**
	 * The mask value for determining the constraint mode. 
	 * The application should
	 * use the bit-wise <code>AND</code> operation with a value returned by
	 * <code>getConstraints()</code> and
	 * <code>CONSTRAINT_MASK</code> in order to retrieve the current
	 * constraint mode,
	 * in order to remove any modifier flags such as the
	 * <code>PASSWORD</code> flag.
	 * 
	 * <P>Constant <code>0xFFFF</code> is assigned to
	 * <code>CONSTRAINT_MASK</code>.</P>
	 */
	public static final int CONSTRAINT_MASK = 0xFFFF;

	// clear command is used in DateField, too:
	//#ifdef polish.command.clear.priority:defined
	//#= private final static int CLEAR_PRIORITY = ${polish.command.clear.priority};
	//#else
		private final static int CLEAR_PRIORITY = 8;
	//#endif
	//#ifdef polish.i18n.useDynamicTranslations
			//# public static Command CLEAR_CMD = new Command( "Clear", Command.CANCEL, CLEAR_PRIORITY );
	//#elifdef polish.command.clear:defined
public static final Command CLEAR_CMD = new Command( "Clear", Command.CANCEL, CLEAR_PRIORITY );
	//#else
		//# public static final Command CLEAR_CMD = new Command( "Clear", Command.CANCEL, CLEAR_PRIORITY ); 
	//#endif

		
	//#ifndef tmp.suppressCommands
	
		//#ifdef polish.command.delete.priority:defined
		//#= private final static int DELETE_PRIORITY = ${polish.command.delete.priority};
		//#else
			private final static int DELETE_PRIORITY = 1;
		//#endif
			
		//#if (polish.TextField.suppressDeleteCommand != true) && !polish.blackberry && !polish.TextField.keepDeleteCommand
			//#define tmp.updateDeleteCommand
		//#endif
			
		// the delete command is a Command.ITEM type when the extended menubar is used in conjunction with a defined return-key, 
		// because CANCEL will be mapped on special keys like the return key on Sony Ericsson devices.
		//#if polish.MenuBar.useExtendedMenuBar && polish.key.ReturnKey:defined
			//#ifdef polish.i18n.useDynamicTranslations
				//#	public static Command DELETE_CMD = new Command( "Delete", Command.ITEM, DELETE_PRIORITY );
			//#elifdef polish.command.delete:defined
				//#= public static final Command DELETE_CMD = new Command( "${polish.command.delete}", Command.ITEM, DELETE_PRIORITY );
			//#else
				//# public static final Command DELETE_CMD = new Command( "Delete", Command.ITEM, DELETE_PRIORITY ); 
			//#endif
		//#else
			//#ifdef polish.i18n.useDynamicTranslations
				//# public static Command DELETE_CMD = new Command( "Delete", Command.CANCEL, DELETE_PRIORITY );
			//#elifdef polish.command.delete:defined
public static final Command DELETE_CMD = new Command( "Delete", Command.CANCEL, DELETE_PRIORITY );
			//#else
				//# public static final Command DELETE_CMD = new Command( "Delete", Command.CANCEL, DELETE_PRIORITY ); 
			//#endif
	  	//#endif
	//#endif	
	
	//#if polish.key.maybeSupportsAsciiKeyMap
		//#ifdef polish.command.switch_keyboard.priority:defined
			//#= private final static int SWITCH_KEYBOARD_PRIORITY = ${polish.command.switch_keyboard.priority}; 
		//#else
			//# private final static int SWITCH_KEYBOARD_PRIORITY = 1; 
		//#endif
		//# private static Command SWITCH_KEYBOARD_CMD = new Command ("Switch keyboard", Command.ITEM, SWITCH_KEYBOARD_PRIORITY);
	//#endif

  	/** valid input characters for local parts of email addresses, apart from 0..9 and a..z. */
  	private static final String VALID_LOCAL_EMAIL_ADDRESS_CHARACTERS = ".-_@!#$%&'*+/=?^`{|}~";
  	/** valid input characters for domain names, apart from 0..9 and a..z. */
  	private static final String VALID_DOMAIN_CHARACTERS = "._-";
	private int maxSize;
	private int constraints;
	//#ifdef polish.css.textfield-caret-color
		//# private int caretColor = -1;
	//#endif
	private char editingCaretChar = '|';
	protected char caretChar = '|';
	protected boolean showCaret;
	private long lastCaretSwitch;
	protected String title;
	private String passwordText;
	private boolean isPassword;
	private boolean enableDirectInput;
	
	//#if (!tmp.suppressCommands && !tmp.supportsSymbolEntry) || tmp.supportsSymbolEntry
		private ItemCommandListener additionalItemCommandListener;
	//#endif

	// this is outside of the tmp.directInput block, so that it can be referenced from the UiAccess class
	protected int inputMode; // the current input mode		
	//#if tmp.directInput
		//#if tmp.supportsSymbolEntry
			//# protected static List symbolsList;
			//#if polish.TextField.Symbols:defined
				//#= private static String[] definedSymbols = ${ stringarray(polish.TextField.Symbols)}; 
			//#else
				//# protected static String[] definedSymbols = {"&","@","/","\\","<",">","(",")","{","}","[","]",".","+","-","*",":","_","\"","#","$","%",":)",":(",";)",":x",":D",":P"};
				//# //private static String definedSymbols = "@/\\<>(){}.,+-*:_\"#$%";
			//#endif
//# 				
			//#ifdef polish.command.entersymbol.priority:defined
			//#= private static int ENTER_SYMBOL_PRIORITY = ${polish.command.entersymbol.priority};
			//#else
				//# private static int ENTER_SYMBOL_PRIORITY = 9;
			//#endif
//# 				
			//#ifdef polish.i18n.useDynamicTranslations
				//# public static Command ENTER_SYMBOL_CMD = new Command( "Add Symbol", Command.ITEM, ENTER_SYMBOL_PRIORITY );
			//#elifdef polish.command.entersymbol:defined
				//#= public static final Command ENTER_SYMBOL_CMD = new Command( "${polish.command.entersymbol}", Command.ITEM, ENTER_SYMBOL_PRIORITY );
			//#else
				//# public static final Command ENTER_SYMBOL_CMD = new Command( "Add Symbol", Command.ITEM, ENTER_SYMBOL_PRIORITY ); 
			//#endif
		//#endif
		//# private boolean isKeyDown;
		//#ifdef polish.TextField.InputTimeout:defined
			//#= private static final int INPUT_TIMEOUT = ${polish.TextField.InputTimeout};  
		//#else
			//# private static final int INPUT_TIMEOUT = 1000;
		//#endif
		//# /** input mode for lowercase characters */
		//# public static final int MODE_LOWERCASE = 0;
		//# /** input mode for entering one uppercase followed by lowercase characters */
		//# public static final int MODE_FIRST_UPPERCASE = 1; // only the first character should be written in uppercase
		//# /** input mode for uppercase characters */
		//# public static final int MODE_UPPERCASE = 2;
		//# /** input mode for numeric characters */
		//# public static final int MODE_NUMBERS = 3;
		//# /** input mode for input using a separete native TextBox */
		//# public static final int MODE_NATIVE = 4;
		//#ifdef polish.key.ChangeInputModeKey:defined
			//#= protected static final int KEY_CHANGE_MODE = ${polish.key.ChangeInputModeKey};
		//#else
			//# public static final int KEY_CHANGE_MODE = Canvas.KEY_POUND;
		//#endif
		//#ifdef polish.key.ClearKey:defined
		   //#= public static final int KEY_DELETE = ${polish.key.ClearKey};
		//#else
		   //# public static final int KEY_DELETE = -8 ; //Canvas.KEY_STAR;
		//#endif
		//#if polish.key.shift:defined
		   //#= private static final int KEY_SHIFT = ${polish.key.shift};
		//#else
		   //# private static final int KEY_SHIFT = -50;
		//#endif
		//# private boolean nextCharUppercase; // is needed for the FIRST_UPPERCASE-mode
//# 		
		//# private String[] realTextLines; // the textLines with spaces and line breaks at the end
		//# private String originalRowText; // current line including spaces and line breaks at the end
		//# private int caretPosition; // the position of the caret in the text
		//# private int caretColumn; // the current column of the caret, 0 is the first column
		//# private int caretRow; // the current row of the caret, 0 is the first row
		//# private int caretRowWidth; // the width of the current row
		//# private int caretX;
		//# private int caretY;
		//# private int caretWidth;
//# 		
		//#ifdef polish.css.textfield-show-length
			//# private boolean showLength;
		//#endif
//# 
		//# private int lastKey; // the last key which has been pressed
		//# long lastInputTime; // the last time a key has been pressed
		//# private int characterIndex; // the index within the available characters of the current key
		//# // the characters for each key:
		//#ifdef polish.TextField.charactersKey1:defined
			//#= private static final String charactersKey1 = "${polish.TextField.charactersKey1}";
		//#else
		//# private static final String charactersKey1 = ".,!?\u00bf:/()@_-+1'\";";
		//#endif
		//#ifdef polish.TextField.charactersKey2:defined
			//#= private static final String charactersKey2 = "${polish.TextField.charactersKey2}";
		//#else
			//# private static final String charactersKey2 = "abc2\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7";
		//#endif
		//#ifdef polish.TextField.charactersKey3:defined
			//#= private static final String charactersKey3 = "${polish.TextField.charactersKey3}";
		//#else
			//# private static final String charactersKey3 = "def3\u00e8\u00e9\u00ea\u00eb";
		//#endif
		//#ifdef polish.TextField.charactersKey4:defined
			//#= private static final String charactersKey4 = "${polish.TextField.charactersKey4}";
		//#else
			//# private static final String charactersKey4 = "ghi4\u00ec\u00ed\u00ee\u00ef";
		//#endif
		//#ifdef polish.TextField.charactersKey5:defined
			//#= private static final String charactersKey5 = "${polish.TextField.charactersKey5}";
		//#else
			//# private static final String charactersKey5 = "jkl5";
		//#endif
		//#ifdef polish.TextField.charactersKey6:defined
			//#= private static final String charactersKey6 = "${polish.TextField.charactersKey6}";
		//#else
			//# private static final String charactersKey6 = "mno6\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6";
		//#endif
		//#ifdef polish.TextField.charactersKey7:defined
			//#= private static final String charactersKey7 = "${polish.TextField.charactersKey7}";
		//#else
			//# private static final String charactersKey7 = "pqrs7\u00df";
		//#endif
		//#ifdef polish.TextField.charactersKey8:defined
			//#= private static final String charactersKey8 = "${polish.TextField.charactersKey8}";
		//#else
			//# private static final String charactersKey8 = "tuv8\u00f9\u00fa\u00fb\u00fc";
		//#endif
		//#ifdef polish.TextField.charactersKey9:defined
			//#= private static final String charactersKey9 = "${polish.TextField.charactersKey9}";
		//#else
			//# private static final String charactersKey9 = "wxyz9\u00fd";
		//#endif
		//#ifdef polish.TextField.charactersKey0:defined
			//#= private static final String charactersKey0 = "${polish.TextField.charactersKey0}";
		//#else
			//# protected static final String charactersKey0 = " 0";
		//#endif
		//#ifdef polish.TextField.charactersKeyStar:defined
			//#= protected static final String charactersKeyStar = "${polish.TextField.charactersKeyStar}";
		//#else
			//# protected static final String charactersKeyStar = ".,!?\u00bf:/@_-+1'\";";
		//#endif
		//#ifdef polish.TextField.charactersKeyPound:defined
			//#= protected static final String charactersKeyPound = "${polish.TextField.charactersKeyPound}";
		//#else
			//# protected static final String charactersKeyPound = null;
		//#endif
		//# /** map of characters that can be triggered witht the 0..9 and #, * keys */
		//# public static final String[] CHARACTERS = new String[]{ charactersKey0, charactersKey1, charactersKey2, charactersKey3, charactersKey4, charactersKey5, charactersKey6, charactersKey7, charactersKey8, charactersKey9 };
		//# private static final String[] EMAIL_CHARACTERS = new String[]{ VALID_LOCAL_EMAIL_ADDRESS_CHARACTERS + "0", VALID_LOCAL_EMAIL_ADDRESS_CHARACTERS + "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9" };
		//# private String[] characters;
		//# private boolean isNumeric;
		//# private boolean isDecimal;
		//# private boolean isEmail;
		//# private boolean isUrl;
//# 
		//# private int rowHeight;
		//#if polish.TextField.includeInputInfo
			//#define tmp.includeInputInfo
			//#define tmp.useInputInfo
			//# protected StringItem infoItem;
		//#elif polish.TextField.showInputInfo != false
			//#define tmp.useInputInfo
		//#endif
		//# protected final Object lock;
		//# private long deleteKeyRepeatCount;
	//#endif
	protected char emailSeparatorChar = ';';
	//#if polish.blackberry
		//# private PolishTextField editField;
		//#if polish.Bugs.ItemStateListenerCalledTooEarly
			//# private long lastFieldChangedEvent;
		//#endif
	//#endif
	//#if polish.midp && !polish.blackberry
		//#define tmp.useNativeTextBox
		private javax.microedition.lcdui.TextBox midpTextBox;
	//#endif
	
	long lastTimePressed = -1;
	
	//#if tmp.usePredictiveInput
		//# boolean predictiveInput = false;
		//# private PredictiveAccess predictiveAccess;
//# 		
		//# boolean nextMode 	 = this.predictiveInput;
		//# public static final int SWITCH_DELAY 	 = 1000;
	//#endif		
		
	protected boolean flashCaret = true;
	protected boolean isUneditable;
	private boolean isShowInputInfo = true;
	
	private boolean suppressCommands = false;
	
	//#if polish.TextField.showHelpText
		//# private StringItem helpItem;
	//#endif
	
	//#if polish.key.maybeSupportsAsciiKeyMap
		//# private static boolean useAsciiKeyMap = false;
	//#endif
	//#if polish.key.supportsAsciiKeyMap || polish.key.maybeSupportsAsciiKeyMap
		//#define tmp.supportsAsciiKeyMap
	//#endif
	private boolean isKeyPressedHandled;

	/**
	 * Creates a new <code>TextField</code> object with the given label, initial
	 * contents, maximum size in characters, and constraints.
	 * If the text parameter is <code>null</code>, the
	 * <code>TextField</code> is created empty.
	 * The <code>maxSize</code> parameter must be greater than zero.
	 * An <code>IllegalArgumentException</code> is thrown if the
	 * length of the initial contents string exceeds <code>maxSize</code>.
	 * However,
	 * the implementation may assign a maximum size smaller than the
	 * application had requested.  If this occurs, and if the length of the
	 * contents exceeds the newly assigned maximum size, the contents are
	 * truncated from the end in order to fit, and no exception is thrown.
	 * 
	 * @param label item label
	 * @param text the initial contents, or null if the TextField is to be empty
	 * @param maxSize the maximum capacity in characters
	 * @param constraints see input constraints
	 * @throws IllegalArgumentException if maxSize is zero or less
	 * 					or if the value of the constraints parameter is invalid
	 * 					or if text is illegal for the specified constraints
	 * 					or if the length of the string exceeds the requested maximum capacity
	 */
	public TextField( String label, String text, int maxSize, int constraints)
	{
		this( label, text, maxSize, constraints, null );
	}
	
	/**
	 * Creates a new <code>TextField</code> object with the given label, initial
	 * contents, maximum size in characters, and constraints.
	 * If the text parameter is <code>null</code>, the
	 * <code>TextField</code> is created empty.
	 * The <code>maxSize</code> parameter must be greater than zero.
	 * An <code>IllegalArgumentException</code> is thrown if the
	 * length of the initial contents string exceeds <code>maxSize</code>.
	 * However,
	 * the implementation may assign a maximum size smaller than the
	 * application had requested.  If this occurs, and if the length of the
	 * contents exceeds the newly assigned maximum size, the contents are
	 * truncated from the end in order to fit, and no exception is thrown.
	 * 
	 * @param label item label
	 * @param text the initial contents, or null if the TextField is to be empty
	 * @param maxSize the maximum capacity in characters
	 * @param constraints see input constraints
	 * @param style the CSS style for this field
	 * @throws IllegalArgumentException if maxSize is zero or less
	 * 					or if the value of the constraints parameter is invalid
	 * 					or if text is illegal for the specified constraints
	 * 					or if the length of the string exceeds the requested maximum capacity
	 */
	public TextField( String label, String text, int maxSize, int constraints, Style style)
	{
		super( label, null, INTERACTIVE, style );
		//#if tmp.directInput
			//# this.lock = new Object();
		//#endif
		this.constraints = constraints;
		this.maxSize = maxSize;
		if (label != null) {
			this.title = label;
		} else {
			//#ifdef polish.title.input:defined
this.title = "Input";
			//#else
				//# this.title = "Input";
			//#endif
		}
		if ((constraints & PASSWORD) == PASSWORD) {
			this.isPassword = true;
		}
		//#ifndef polish.hasPointerEvents
			if ((constraints & NUMERIC) == NUMERIC && (constraints & DECIMAL) != DECIMAL) {
				this.enableDirectInput = true;
			}
		//#endif
			
		setConstraints(constraints);
				
		//#if tmp.usePredictiveInput
			//# this.predictiveAccess = new PredictiveAccess();
			//# this.predictiveAccess.init(this);
		//#endif
			
		//#if polish.TextField.showHelpText
//# 			
			//#style help?
			//# this.helpItem = new StringItem(null,null);
//# /*			if (style != null && this.helpItem.style != null && this.helpItem.style.font == null) {
				//# this.helpItem.style.font = style.font;
			//# }*/
			//# /*Font font = this.helpItem.getFont();
			//# Font newFont = Font.getFont(font.getFace(), font.getStyle(), this.getFont().getSize());
//# 			
			//# this.helpItem.setFont(newFont);*/
//# 			
			//#ifdef polish.TextField.help:defined
				//#= this.helpItem.setText("${polish.TextField.help}");
			//#else
				//# this.helpItem.setText("type text here");
			//#endif
		//#endif

		//#if polish.key.maybeSupportsAsciiKeyMap
			//#if polish.api.windows
				//# if (Keyboard.needsQwertzAndNumericSupport()) {
					//# addCommand(SWITCH_KEYBOARD_CMD);
				//# }
				//# useAsciiKeyMap = true;
			//#endif
		//#endif

		setString(text);
	}
	
	//#if tmp.useNativeTextBox
	/**
	 * Creates the TextBox used for the actual input mode.
	 */
	private void createTextBox() {
		String currentText = this.isPassword ? this.passwordText : this.text;
		this.midpTextBox = new javax.microedition.lcdui.TextBox( this.title, currentText, this.maxSize, this.constraints );
		this.midpTextBox.addCommand(StyleSheet.OK_CMD);
		if (!this.isUneditable) {
			this.midpTextBox.addCommand(StyleSheet.CANCEL_CMD);
		}
		this.midpTextBox.setCommandListener( this );	
	}
	//#endif
	
	/**
	 * Gets the contents of the <code>TextField</code> as a string value.
	 * 
	 * @return the current contents, an empty string when the current value is null. 
	 * @see #setString(java.lang.String)
	 */
	public String getString()
	{
		//#if polish.blackberry
			//# if ( this.editField != null ) {
				//# return this.editField.getText();
			//# }
		//#endif
		if ( this.isPassword ) {
			if (this.passwordText == null) {
				return "";
			}
			return this.passwordText;
		} else {
			if (this.text == null) {
				return "";
			}
			return this.text;
		}
	}
	
	/**
	 * Retrieves the decimal value entered with a dot as the decimal mark.
	 * <ul>
	 * <li>When the value has no decimal places it will be returned as it is: 12</li>
	 * <li>When the value is null, null will be returned: null</li>
	 * <li>When the value has decimal places, a dot will be used: 12.3</li>
	 * </ul>
	 * @return either the formatted value or null, when there was no input.
	 * @throws IllegalStateException when the TextField is not DECIMAL constrained
	 */
	public String getDotSeparatedDecimalString() {
		//#if tmp.directInput
			//#if tmp.allowDirectInput
				//# if (this.enableDirectInput) {
			//#endif
					//# if (!this.isDecimal) {
						//# throw new IllegalStateException();
					//# }
					//# String value = getString();
					//# if ( Locale.DECIMAL_SEPARATOR == '.' || value == null) {
						//# return value;
					//# } else {
						//# return value.replace( Locale.DECIMAL_SEPARATOR, '.');
					//# }
			//#if tmp.allowDirectInput
				//# }
			//#endif
		//#endif
		//#if !tmp.forceDirectInput
			if (( getConstraints() & DECIMAL)!= DECIMAL) {
				throw new IllegalStateException();
			}
			String value = getString();
			if (value == null) {
				return null;
			}
			return value.replace(',', '.');			
		//#endif
	}

	/**
	 * Sets the contents of the <code>TextField</code> as a string
	 * value, replacing the previous contents.
	 * 
	 * @param text the new value of the TextField, or null if the TextField is to be made empty
	 * @throws IllegalArgumentException if text is illegal for the current input constraints
	 * 									or  if the text would exceed the current maximum capacity
	 * @see #getString()
	 */
	public void setString( String text)
	{
		//#debug
		//# System.out.println("setString [" + text + "] for textfield [" + (this.label != null ? this.label.getText() : "no label") + "].");
		//#if !(tmp.forceDirectInput || polish.blackberry)
			if (this.midpTextBox != null) {
				this.midpTextBox.setString( text );
			}
		//#endif
		//#if polish.blackberry
			//# if (this.editField != null && !this.editField.getText().equals(text) ) {
				//# Object bbLock = UiApplication.getEventLock();
				//# if (this.screen == null) {
					//# this.screen = getScreen();
				//# }
				//# synchronized (bbLock) {
                    //# if (this.isFocused) {
                    	//# this.screen.setFocus( null );
                    //# }
                    //# if (text != null) {
                        //# this.editField.setText(text);
                    //# } else {
                        //# this.editField.setText(""); // setting null triggers an IllegalArgumentException
                    //# }
                    //# if (this.isFocused) {
                    	//# this.screen.setFocus( this );
                    //# }
				//# }
			//# }
		//#endif
		if (this.isPassword) {
			this.passwordText = text;
			if (text != null) {
				int length = text.length();
				StringBuffer buffer = new StringBuffer( length );
				for (int i = 0; i < length; i++) {
					buffer.append('*');
				}
				text = buffer.toString();
			}
		}
				
		//#ifdef tmp.directInput
			//# if (text == null) {
				//# this.caretPosition = 0;
				//# this.caretRow = 0;
				//# this.caretColumn = 0;
				//# this.caretX = 0;
				//# this.caretY = 0;
			//# } else if (
					//# (this.caretPosition == 0  && (this.text == null || this.text.length() == 0) )
					//# || ( this.caretPosition > text.length())
					//# || (this.text != null && text != null && text.length() > (this.text.length()+1))
					//# )
			//# {
				//# this.caretPosition = text.length();
			//# }
		//#endif
		//#if tmp.updateDeleteCommand
			if (this.isFocused) {
				updateDeleteCommand( text );
			}
		//#endif
		setText(text);
		//#ifdef tmp.directInput
			//# if ((text == null || text.length() == 0) && this.inputMode == MODE_FIRST_UPPERCASE) {
				//# this.nextCharUppercase = true;
			//# }
			//#if polish.css.textfield-show-length && tmp.useInputInfo
				//# if (this.isFocused && this.showLength) {
					//# updateInfo();
				//# }
		    //#endif
		//#endif
				
		//#if tmp.usePredictiveInput
			//# if(this.predictiveInput) {
				//# this.predictiveAccess.synchronize();
			//# }
		//#endif
	}
	 	
	protected void updateDeleteCommand(String newText) {
		//#if tmp.updateDeleteCommand
		// remove delete command when the caret is before the first character,
		// add it when it is after the first character:
		// #debug
		//System.out.println("updateDeleteCommand: newText=[" + newText + "]");
		if ( !this.isUneditable ) {
			if ( newText == null 
				//#ifdef tmp.directInput
					//# || (this.caretPosition == 0    &&    this.caretChar == this.editingCaretChar)
				//#else
					|| newText.length() == 0 
				//#endif
			) {
				removeCommand( DELETE_CMD );
			} else if ((this.text == null || this.text.length() == 0)
				//#ifdef tmp.directInput
					//# || this.caretPosition == 1
				//#endif						
			) {
				addCommand( DELETE_CMD );
			}
		}
		//#endif		
	}

	
	/**
	 * Copies the contents of the <code>TextField</code> into a character array starting at index zero. 
	 * Array elements beyond the characters copied are left
	 * unchanged.
	 * 
	 * @param data the character array to receive the value
	 * @return the number of characters copied
	 * @throws ArrayIndexOutOfBoundsException if the array is too short for the contents
	 * @throws NullPointerException if data is null
	 * @see #setChars(char[], int, int)
	 */
	public int getChars(char[] data)
	{
		if (this.text == null) {
			return 0;
		}
		String txt = this.text;
		if (this.isPassword) {
			txt = this.passwordText;
		}
		char[] textArray = txt.toCharArray();
		System.arraycopy(textArray, 0, data, 0, textArray.length );
		return textArray.length;
	}

	/** 
	 * Sets the contents of the <code>TextField</code> from a  character array, 
	 * replacing the previous contents. 
	 * Characters are copied from the region of the
	 * <code>data</code> array
	 * starting at array index <code>offset</code> and running for
	 * <code>length</code> characters.
	 * If the data array is <code>null</code>, the <code>TextField</code>
	 * is set to be empty and the other parameters are ignored.
	 * 
	 * <p>The <code>offset</code> and <code>length</code> parameters must
	 * specify a valid range of characters within
	 * the character array <code>data</code>.
	 * The <code>offset</code> parameter must be within the
	 * range <code>[0..(data.length)]</code>, inclusive.
	 * The <code>length</code> parameter
	 * must be a non-negative integer such that
	 * <code>(offset + length) &lt;= data.length</code>.</p>
	 * 
	 * @param data the source of the character data
	 * @param offset the beginning of the region of characters to copy
	 * @param length the number of characters to copy
	 * @throws ArrayIndexOutOfBoundsException - if offset and length do not specify a valid range within the data array
	 * @throws IllegalArgumentException - if data is illegal for the current input constraints
	 *												   or if the text would exceed the current maximum capacity
	 * @see #getChars(char[])
	 */
	public void setChars(char[] data, int offset, int length)
	{
		char[] copy = new char[ length ];
		System.arraycopy(data, offset, copy, 0, length );
		setString( new String( copy ));
	}

	/**
	 * Inserts a string into the contents of the
	 * <code>TextField</code>.  The string is
	 * inserted just prior to the character indicated by the
	 * <code>position</code> parameter, where zero specifies the first
	 * character of the contents of the <code>TextField</code>.  If
	 * <code>position</code> is
	 * less than or equal to zero, the insertion occurs at the beginning of
	 * the contents, thus effecting a prepend operation.  If
	 * <code>position</code> is greater than or equal to the current size of
	 * the contents, the insertion occurs immediately after the end of the
	 * contents, thus effecting an append operation.  For example,
	 * <code>text.insert(s, text.size())</code> always appends the string
	 * <code>s</code> to the current contents.
	 * 
	 * <p>The current size of the contents is increased by the number of
	 * inserted characters. The resulting string must fit within the current
	 * maximum capacity. </p>
	 * 
	 * <p>If the application needs to simulate typing of characters it can
	 * determining the location of the current insertion point
	 * (&quot;caret&quot;)
	 * using the with <CODE>getCaretPosition()</CODE> method.
	 * For example,
	 * <code>text.insert(s, text.getCaretPosition())</code> inserts the string
	 * <code>s</code> at the current caret position.</p>
	 * 
	 * @param src the String to be inserted
	 * @param position the position at which insertion is to occur
	 * @throws IllegalArgumentException if the resulting contents would be illegal for the current input constraints
	 *		   or if the insertion would exceed the current maximum capacity
	 * @throws NullPointerException if src is null
	 */
	public void insert( String src, int position)
	{
		String txt = this.text;
		if(txt == null)
			txt = "";
		
		if (this.isPassword) {
			txt = this.passwordText;
		}
		String start = txt.substring( 0, position );
		String end = txt.substring( position );
		setString( start + src + end );
		//#if tmp.directInput
			//# if (position == this.caretPosition) {
				//# this.caretPosition += src.length();
			//# }
		//#endif
		//#if tmp.usePredictiveInput
			//# if(this.predictiveInput)
				//# this.predictiveAccess.synchronize();
		//#endif
	}

	/**
	 * Inserts a subrange of an array of characters into the contents of
	 * the <code>TextField</code>.  The <code>offset</code> and
	 * <code>length</code> parameters indicate the subrange
	 * of the data array to be used for insertion. Behavior is otherwise
	 * identical to <A HREF="../../../javax/microedition/lcdui/TextField.html#insert(java.lang.String, int)"><CODE>insert(String, int)</CODE></A>.
	 * 
	 * <p>The <code>offset</code> and <code>length</code> parameters must
	 * specify a valid range of characters within
	 * the character array <code>data</code>.
	 * The <code>offset</code> parameter must be within the
	 * range <code>[0..(data.length)]</code>, inclusive.
	 * The <code>length</code> parameter
	 * must be a non-negative integer such that
	 * <code>(offset + length) &lt;= data.length</code>.</p>
	 * 
	 * @param data - the source of the character data
	 * @param offset - the beginning of the region of characters to copy
	 * @param length - the number of characters to copy
	 * @param position - the position at which insertion is to occur
	 * @throws ArrayIndexOutOfBoundsException - if offset and length do not specify a valid range within the data array
	 * @throws IllegalArgumentException - if the resulting contents would be illegal for the current input constraints
	 *												   or if the insertion would exceed the current maximum capacity
	 * @throws NullPointerException - if data is null
	 */
	public void insert(char[] data, int offset, int length, int position)
	{
		char[] copy = new char[ length ];
		System.arraycopy( data, offset, copy, 0, length);
		insert( new String( copy ), position );
	}

	/**
	 * Deletes characters from the <code>TextField</code>.
	 * 
	 * <p>The <code>offset</code> and <code>length</code> parameters must
	 * specify a valid range of characters within
	 * the contents of the <code>TextField</code>.
	 * The <code>offset</code> parameter must be within the
	 * range <code>[0..(size())]</code>, inclusive.
	 * The <code>length</code> parameter
	 * must be a non-negative integer such that
	 * <code>(offset + length) &lt;= size()</code>.</p>
	 * 
	 * @param offset the beginning of the region to be deleted
	 * @param length the number of characters to be deleted
	 * @throws IllegalArgumentException if the resulting contents would be illegal for the current input constraints
	 * @throws StringIndexOutOfBoundsException if offset and length do not specify a valid range within the contents of the TextField
	 */
	public void delete(int offset, int length)
	{
		String txt = this.text;
		if (this.isPassword) {
			txt = this.passwordText;
		}
		String start = txt.substring(0, offset );
		String end = txt.substring( offset + length );
		setString( start + end );
	}

	/**
	 * Returns the maximum size (number of characters) that can be
	 * stored in this <code>TextField</code>.
	 * 
	 * @return the maximum size in characters
	 * @see #setMaxSize(int)
	 */
	public int getMaxSize()
	{
		return this.maxSize;
	}

	/**
	 * Sets the maximum size (number of characters) that can be contained
	 * in this
	 * <code>TextField</code>. If the current contents of the
	 * <code>TextField</code> are larger than
	 * <code>maxSize</code>, the contents are truncated to fit.
	 * 
	 * @param maxSize the new maximum size
	 * @return assigned maximum capacity may be smaller than requested.
	 * @throws IllegalArgumentException if maxSize is zero or less.
	 *									or if the contents after truncation would be illegal for the current input constraints
	 * @see #getMaxSize()
	 */
	public int setMaxSize(int maxSize)
	{
		if ((this.text != null && maxSize < this.text.length()) || (maxSize < 1)) {
			throw new IllegalArgumentException();
		}
		//#if ! tmp.forceDirectInput && !polish.blackberry
			if (this.midpTextBox != null) {
				this.maxSize = this.midpTextBox.setMaxSize(maxSize);
				return this.maxSize;
			} else {
		//#endif
				this.maxSize = maxSize;
				return maxSize;
		//#if ! tmp.forceDirectInput  && !polish.blackberry
			}
		//#endif
	}

	/**
	 * Gets the number of characters that are currently stored in this
	 * <code>TextField</code>.
	 * 
	 * @return number of characters in the TextField
	 */
	public int size()
	{
		if (this.text == null) {
			return 0;
		} else {
			return this.text.length();
		}
	}

	/**
	 * Gets the current input position.  For some UIs this may block and ask
	 * the user for the intended caret position, and on other UIs this may
	 * simply return the current caret position.
	 * When the direct input mode is used, this method simply returns the current cursor position (= non blocking).
	 * 
	 * @return the current caret position, 0 if at the beginning
	 */
	public int getCaretPosition()
	{
		//#ifdef tmp.allowDirectInput
			//# if (this.enableDirectInput) {
				//# return this.caretPosition;
			//#if !tmp.forceDirectInput
			//# } else if (this.midpTextBox != null) {
				//# return this.midpTextBox.getCaretPosition();
			//#endif
			//# }
			//# return 0;
		//#elif polish.blackberry
			//# return this.editField.getInsertPositionOffset();
		//#elif tmp.forceDirectInput
			//# return this.caretPosition;
		//#else
			if (this.midpTextBox != null) {
				return this.midpTextBox.getCaretPosition();
			}
			return 0;
		//#endif
	}
	
	/**
	 * Sets the caret position.
	 * Please note that this operation requires the direct input mode to work.
	 * 
	 * @param position the new caret position,  0 puts the caret at the start of the line, getString().length moves the caret to the end of the input.
	 */
	public void setCaretPosition(int position) {
		//#if polish.blackberry
			//# this.editField.setCursorPosition(position);
		//#elif tmp.allowDirectInput || tmp.forceDirectInput
			//# this.caretPosition = position;
			//# if ( this.isInitialized  && this.realTextLines != null ){
				//# int row = 0;
				//# int col = 0;
				//# int passedCharacters = 0;
				//# String textLine = null;
				//# for (int i = 0; i < this.textLines.length; i++) {
					//# textLine = this.realTextLines[i]; //this.textLines[i];
					//# passedCharacters += textLine.length();
					//# //System.out.println("passedCharacters=" + passedCharacters + ", line=" + textLine );
					//# if (passedCharacters >= position ) {
						//# row = i;
						//# col = textLine.length() - (passedCharacters - position);
						//# break;
					//# }
				//# }
				//#debug
				//# System.out.println("setCaretPosition, position=" + position + ", row=" + row + ", col=" + col );
				//# this.caretRow = row;	
				//# this.caretColumn = col;
//# 				
				//# textLine = this.textLines[ row ];
				//# String firstPart;
				//# if (this.caretColumn < textLine.length()) {
					//# firstPart = textLine.substring(0, this.caretColumn);
				//# } else {
					//# firstPart = textLine;
				//# }
				//# if (this.isPassword) {
					//# this.caretX = stringWidth( "*" ) * firstPart.length();
				//# } else {
					//# this.caretX = stringWidth( firstPart );
				//# }
				//# this.internalY = this.caretRow * this.rowHeight;
				//# this.caretY = this.internalY;
				//# repaint();
			//# }
		//#endif
	}



	/**
	 * Sets the input constraints of the <code>TextField</code>. If
	 * the the current contents
	 * of the <code>TextField</code> do not match the new
	 * <code>constraints</code>, the contents are
	 * set to empty.
	 * 
	 * @param constraints see input constraints
	 * @throws IllegalArgumentException if constraints is not any of the ones specified in input constraints
	 * @see #getConstraints()
	 */
	public void setConstraints(int constraints)
	{
		this.constraints = constraints;
		int fieldType = constraints & 0xffff;
		this.isUneditable = (constraints & UNEDITABLE) == UNEDITABLE;
		//#if polish.css.text-wrap
			//# this.animateTextWrap = this.isUneditable;
		//#endif
		//#if polish.blackberry
//# 			
			//# long bbStyle = Field.FOCUSABLE;
			//# if (!this.isUneditable) {
				//# bbStyle |= Field.EDITABLE;
			//# }
//# 			
			//# if ( fieldType == DECIMAL) {
				//# bbStyle |= BasicEditField.FILTER_REAL_NUMERIC;
			//# } else if (fieldType == NUMERIC) {
				//# bbStyle |= BasicEditField.FILTER_INTEGER;
			//# } else if (fieldType == PHONENUMBER) {
				//# bbStyle |= BasicEditField.FILTER_PHONE;
			//# } else if (fieldType == EMAILADDR ) {
				//# bbStyle |= BasicEditField.FILTER_EMAIL;
			//# } else if ( fieldType == URL ) {
				//# bbStyle |= BasicEditField.FILTER_URL;
			//# }
			//# if (this.editField != null) {
				//# // remove the old edit field from the blackberry screen:
				//# this._bbFieldAdded = false;
				//# if (this.isFocused) {
					//# getScreen().setFocus( this );
				//# }
			//# }
			//# if ((constraints & PASSWORD) == PASSWORD) {
				//# this.isPassword = true;
				//# this.editField = new PolishPasswordEditField( null, getString(), this.maxSize, bbStyle );
			//# } else {
				//# this.isPassword = false;
				//# this.editField = new PolishEditField( null, getString(), this.maxSize, bbStyle );
			//# }
			//# this.editField.setChangeListener( this );
			//# this._bbField = (Field) this.editField;
		//#elif !tmp.forceDirectInput
			if (this.midpTextBox != null) {
				this.midpTextBox.setConstraints(constraints);
			}
		//#endif
		//#ifdef tmp.directInput
			//# this.characters = CHARACTERS;
			//# this.isEmail = false;
			//# this.isUrl = false;
			//# if ((constraints & PASSWORD) == PASSWORD) {
				//# this.isPassword = true;
			//# }
			//# if (fieldType == NUMERIC || fieldType == PHONENUMBER) {
				//# this.isNumeric = true;
				//# this.inputMode = MODE_NUMBERS;
				//#ifndef polish.hasPointerEvents
					//# this.enableDirectInput = true;
				//#endif
			//# } else {
				//# this.isNumeric = false;
			//# }
			//# if (fieldType == DECIMAL) {
				//# this.isNumeric = true;
				//# this.isDecimal = true;
				//# this.inputMode = MODE_NUMBERS;
			//# } else {
				//# this.isDecimal = false;
			//# }
			//# if (fieldType == EMAILADDR) {
				//# this.isEmail = true;
				//# this.characters = EMAIL_CHARACTERS;
			//# }
			//# if (fieldType == URL) {
				//# this.isUrl = true;
			//# }
			//# if ((constraints & INITIAL_CAPS_WORD) == INITIAL_CAPS_WORD) {
				//# this.inputMode = MODE_FIRST_UPPERCASE;
				//# this.nextCharUppercase = true;
			//# } 
			//#if tmp.useInputInfo
				//# updateInfo();
			//#endif
		//#endif
				
		//#if !tmp.suppressCommands
			//#if (polish.TextField.suppressDeleteCommand != true)
				removeCommand( DELETE_CMD );
			//#endif
		
			//#if polish.TextField.suppressClearCommand != true
				removeCommand( CLEAR_CMD );
			//#endif
		
			//#if tmp.directInput && tmp.supportsSymbolEntry && polish.TextField.suppressAddSymbolCommand != true
				//# removeCommand( ENTER_SYMBOL_CMD );
			//#endif
		//#endif
				
		// set item commands:
				
		//#if !tmp.suppressCommands		
		if(!this.suppressCommands)
		{
			if (this.isFocused) {
				getScreen().removeItemCommands( this );
			}
			
			// add default text field item-commands:
			//#if (polish.TextField.suppressDeleteCommand != true) && !polish.blackberry
				if (!this.isUneditable)  {
					//#ifdef polish.i18n.useDynamicTranslations
						//# String delLabel = "Delete";
						//# if ( delLabel != DELETE_CMD.getLabel()) {
							//# DELETE_CMD = new Command( delLabel, Command.CANCEL, DELETE_PRIORITY );
						//# }
					//#endif
					this.addCommand(DELETE_CMD);
				}
			//#endif
			//#if polish.TextField.suppressClearCommand != true
				if (!this.isUneditable) {
					//#ifdef polish.i18n.useDynamicTranslations
						//# String clearLabel = "Clear";
						//# if ( clearLabel != CLEAR_CMD.getLabel()) {
							//# CLEAR_CMD = new Command( clearLabel, Command.ITEM, CLEAR_PRIORITY );
						//# }
					//#endif
					this.addCommand(CLEAR_CMD);
				}
			//#endif
			//#if tmp.directInput && tmp.supportsSymbolEntry && polish.TextField.suppressAddSymbolCommand != true
				//# if (!this.isNumeric) {
					//# if (!this.isUneditable) {
						//#ifdef polish.i18n.useDynamicTranslations
							//# String enterSymbolLabel = "Add Symbol";
							//# if ( enterSymbolLabel != ENTER_SYMBOL_CMD.getLabel()) {
								//# ENTER_SYMBOL_CMD = new Command( enterSymbolLabel, Command.ITEM, ENTER_SYMBOL_PRIORITY );
							//# }
						//#endif
						//# this.addCommand(ENTER_SYMBOL_CMD);
					//# }
				//# }
			//#endif	
			}
		
			this.itemCommandListener = this;
			if (this.isFocused) {
				showCommands();
			}
			
		// end of if !tmp.suppressCommands:
		//#endif
			
			
//		if ( (constraints & UNEDITABLE) == UNEDITABLE) {
//			// deactivate this field:
//			super.setAppearanceMode( Item.PLAIN );
//			if (this.isInitialised && this.isFocused && this.parent instanceof Container) {
//				((Container)this.parent).requestDefocus( this );
//			}
//		} else {
//			super.setAppearanceMode( Item.INTERACTIVE );
//		}
	}
	
	//#if polish.blackberry
	//# /**
	 //# * Sets a blackberry field to selectable. Used to prevent
	 //# * fields to be selectable if they should not be shown.
	 //# * 
	 //# * @param editable true, if the textfield should be selectable, otherwise false
	 //# */
	//# public void activate(boolean editable)
	//# {
		//# Object bbLock = Application.getEventLock();
		//# synchronized (bbLock) 
		//# {
			//# if(editable)
			//# {
				//# if(this._bbField == null)
				//# {
					//# setConstraints(this.constraints);
				//# }
				//# if (this.isFocused) {
					//# getScreen().setFocus( this );
				//# }
			//# }
			//# else
			//# {	
				//# if(this._bbField != null)
				//# {
//# 					
						//# Manager manager = this._bbField.getManager();
						//# manager.delete(this._bbField);
//# 						
						//# this.editField = null;
						//# this._bbField = null;
						//# this._bbFieldAdded = false;
					//# }
				//# }
			//# }
		//# }
	//#endif
	
	//#if polish.blackberry
	//# public void setEditable(boolean editable)
	//# {
		//# this._bbField.setEditable(editable);
	//# }
	//#endif

	/**
	 * Gets the current input constraints of the <code>TextField</code>.
	 * 
	 * @return the current constraints value (see input constraints)
	 * @see #setConstraints(int)
	 */
	public int getConstraints()
	{
		return this.constraints;
	}
	
	/**
	 * Sets a hint to the implementation as to the input mode that should be
	 * used when the user initiates editing of this <code>TextField</code>.  The
	 * <code>characterSubset</code> parameter names a subset of Unicode
	 * characters that is used by the implementation to choose an initial
	 * input mode.  If <code>null</code> is passed, the implementation should
	 * choose a default input mode.
	 * 
	 * 
	 * <p>When the direct input mode is used, J2ME Polish will ignore this call completely.</p>
	 * 
	 * @param characterSubset a string naming a Unicode character subset, or null
	 * @since  MIDP 2.0
	 */
	public void setInitialInputMode( String characterSubset)
	{
		//#if !(tmp.forceDirectInput || polish.blackberry) && polish.midp2
			if (this.midpTextBox == null) {
				createTextBox();
			}
			this.midpTextBox.setInitialInputMode( characterSubset );
		//#endif
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#paint(int, int, javax.microedition.lcdui.Graphics)
	 */
	
	
	public void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) {
		//#if polish.blackberry
        	//# if (this.isFocused && !StyleSheet.currentScreen.isMenuOpened() ) {
				//# this.editField.setPaintPosition( x + g.getTranslateX(), y + g.getTranslateY() );
			//# } else {
				//# super.paintContent(x, y, leftBorder, rightBorder, g);
			//# }
		//#else
        
        //#if tmp.includeInputInfo
    		//# if (this.isUneditable || !this.isFocused) {
        //#endif
    			super.paintContent(x, y, leftBorder, rightBorder, g);
    	//#if tmp.includeInputInfo
    		//# }
		//#endif
    		
		if (this.isUneditable || !this.isFocused) {
			return;
		}
		//#ifdef tmp.directInput
			//#ifdef tmp.allowDirectInput
				//# if ( this.enableDirectInput ) {
			//#endif
				//# // adjust text-start for input info abc|Abc|ABC|123 if it should be shown on the same line:
				//#if tmp.includeInputInfo && !polish.TextField.useExternalInfo
					//# if (this.infoItem != null && this.isShowInputInfo) {
						//# int infoWidth = this.infoItem.getItemWidth( rightBorder-leftBorder, rightBorder-leftBorder);
						//# if (this.infoItem.isLayoutRight) {
							//# int newRightBorder = rightBorder - infoWidth;
							//# this.infoItem.paint( newRightBorder, y, newRightBorder, rightBorder, g);
							//# rightBorder = newRightBorder;								
						//# } else {
							//# // left aligned (center is not supported)
							//# this.infoItem.paint( x, y, leftBorder, rightBorder, g);
							//# x += infoWidth;
							//# leftBorder += infoWidth;
						//# }
					//# }
				//#endif
				//# super.paintContent(x, y, leftBorder, rightBorder, g);
//# 
				//#if polish.TextField.showHelpText
				//# if(this.text == null || this.text.length() == 0)
				//# {
					//# this.helpItem.paint(x, y, leftBorder, rightBorder, g);
				//# }
				//#endif
//# 				
//# 
		  		//#ifdef polish.css.text-wrap
		        	//# if (this.useSingleLine) {
		        		//# x += this.xOffset;
		        	//# }
		        //#endif
//# 					
//# 
				//# if (this.showCaret) {
					//#ifdef polish.css.textfield-caret-color
						//# g.setColor( this.caretColor );
					//#else
						//# g.setColor( this.textColor );
					//#endif
					//# int cX;
					//# if (!this.isLayoutCenter && !this.isLayoutRight) {
						//# cX = x + this.caretX;
					//# } else {
						//# if (this.isLayoutCenter) {
							//# cX = x + (((rightBorder - leftBorder) - this.caretRowWidth) >> 1 ) + this.caretX;
						//# } else  {
							//#if polish.i18n.rightToLeft
								//# cX = rightBorder -  this.caretX;
							//#else
								//# cX = rightBorder - this.caretRowWidth + this.caretX;
							//#endif
						//# }
					//# }
					//# int cY = y + this.caretY;
                    //# if (this.caretChar != this.editingCaretChar) {
                    	//# // draw background rectangle
                        //# int w = this.caretWidth;
                        //# if (this.isPassword) {
                        	//# int starWidth = stringWidth("*");
                        	//# if (starWidth > w) {
                        		//# w = starWidth;
                        	//# }
                        //# }
                        //# int h = getFontHeight();
                        //# g.fillRect( cX, cY, w, h);
                        //# //display highlighted text in white color
                        //# g.setColor( DrawUtil.getComplementaryColor(g.getColor()) );
                        //# //TODO use bitmap font and text-effect
            			//#if polish.Bugs.needsBottomOrientiationForStringDrawing
                        	//# cY += this.font.getHeight();
                        	//# g.drawChar( this.caretChar, cX, cY, Graphics.BOTTOM | Graphics.LEFT );
                        //#else
                        	//# g.drawChar( this.caretChar, cX, cY, Graphics.TOP | Graphics.LEFT );
                        //#endif
                    //# } else {
                        //# g.drawLine( cX, cY, cX, cY + getFontHeight() - 2);
                    //# }
				//# }
//# 
//# 				
				//#if tmp.usePredictiveInput
					//# this.predictiveAccess.paintChoices(x, y, this.caretX, this.caretY, leftBorder, rightBorder, g);
				//#endif
//# 					
				//# return;
			//#ifdef tmp.allowDirectInput
			//# }
			//#endif
		//#else
			// no direct input possible, but paint caret
			if (this.showCaret && this.isFocused ) {
				//#ifndef polish.css.textfield-caret-color
					g.setColor( this.textColor );
				//#else
					//# g.setColor( this.caretColor );
				//#endif	
				if (this.isLayoutCenter) {
					x = leftBorder 
						+ ((rightBorder - leftBorder) >> 1) 
						+ (this.contentWidth >> 1)
						+ 2;
				} else if (this.isLayoutRight){
					x = rightBorder; 
				} else {
					x += this.contentWidth + 2;
				}
				g.drawLine( x, y, x, y + getFontHeight() );
			}
		//#endif	
		// end of non-blackberry block
		//#endif
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#initItem()
	 */
	protected void initContent(int firstLineWidth, int lineWidth) {
		this.screen = getScreen();
		//#if tmp.includeInputInfo
			//# if (this.infoItem != null) {
				//# int infoWidth = this.infoItem.getItemWidth(firstLineWidth, lineWidth);
				//# firstLineWidth -= infoWidth;
				//# lineWidth -= infoWidth;
			//# }
		//#endif
		//#if polish.blackberry && polish.css.text-wrap
			//# if (this.isFocused) {
				//# this.useSingleLine = false;
			//# }
		//#endif
		super.initContent(firstLineWidth, lineWidth);
		if (this.font == null) {
			this.font = Font.getDefaultFont();
		}
		if (this.contentWidth < this.minimumWidth) {
			this.contentWidth = this.minimumWidth;
		} 
		if (this.contentHeight < this.minimumHeight) {
			this.contentHeight = this.minimumHeight;
		} else  if (this.contentHeight < getFontHeight()) {
			this.contentHeight = getFontHeight();
		}
		//#if polish.blackberry
			//# if (!this.isFocused) {
				//# return;
			//# }
			//# if(this.editField != null)
			//# {
				//# this.editField.setFont( this.font, this.textColor );
				//# // alowing native field to expand to the fully available width,
				//# // the content size does not need to be changed as the same font is being
				//# // used.
				//# this.editField.layout( lineWidth, this.contentHeight );
			//# }
		//#elif tmp.directInput
			//# this.rowHeight = getFontHeight() + this.paddingVertical;			
			//# if (this.textLines == null || this.text.length() == 0) {
				//# this.caretX = 0;
				//# this.caretY = 0;
				//# this.caretPosition = 0;
				//# this.caretColumn = 0;
				//# this.caretRow = 0;
				//# this.originalRowText = "";
				//# this.realTextLines = null;
				//#if polish.css.text-wrap
					//# if (this.useSingleLine) {
						//# this.xOffset = 0;
					//# }
				//#endif
			//# } else {
				//# // init the original text-lines with spaces and line-breaks:
 				//# //System.out.println("TextField.initContent(): text=[" + this.text + "], (this.realTextLines == null): " + (this.realTextLines == null) + ", this.caretPosition=" + this.caretPosition + ", caretColumn=" + this.caretColumn + ", doSetCaretPos=" + this.doSetCaretPosition + ", hasBeenSet=" + this.caretPositionHasBeenSet);
				//# int length = this.textLines.length;
				//# int endOfLinePos = 0;
				//# int textLength = this.text.length();
				//# String[] realLines = this.realTextLines;
				//# if (realLines == null || realLines.length != length) {
					//# realLines = new String[ length ];
				//# }
				//# boolean caretPositionHasBeenSet = false;
				//# int cp = this.caretPosition;
//# //				if (this.caretChar != this.editingCaretChar) {
//# //					cp++;
//# //				}
				//# for (int i = 0; i < length; i++) {
					//# String line = this.textLines[i];
					//# endOfLinePos += line.length();
					//# if (endOfLinePos < textLength) {
						//# char c = this.text.charAt( endOfLinePos );
						//# if (c == ' '  || c == '\t'  || c == '\n') {
							//# line += c;
							//# endOfLinePos++;
						//# }
					//# }
					//# realLines[i] = line;
					//# if (!caretPositionHasBeenSet && ((endOfLinePos > cp) || (endOfLinePos == cp && i == length -1 )) ) {
						//# //System.out.println("TextField: caretPos=" + this.caretPosition + ", line=" + line + ", endOfLinePos=" + endOfLinePos );
						//# this.caretRow = i;
						//# setCaretRow(line, line.length() - (endOfLinePos - cp) );
						//# this.caretY = this.caretRow * this.rowHeight;
						//# caretPositionHasBeenSet = true;
					//# }
				//# } // for each line
				//# this.realTextLines = realLines;
				//# if (!caretPositionHasBeenSet) {
					//# //System.out.println("caret position has not been set before");
					//#ifdef polish.css.font-bitmap
						//# //if (this.textLines != null) {
						//# if (this.bitMapFontViewer == null) {
					//#endif
							//# //this.caretPosition = this.text.length();
							//# this.caretRow = 0; //this.realTextLines.length - 1;
							//# String caretRowText = this.realTextLines[ this.caretRow ];
							//# int caretRowLength = caretRowText.length();
							//# if (caretRowLength > 0 && caretRowText.charAt( caretRowLength-1) == '\n' ) {
								//# caretRowText = caretRowText.substring(0, caretRowLength-1);
							//# }
							//# setCaretRow( caretRowText, caretRowLength );						
							//# this.caretPosition = this.caretColumn;
							//# this.caretY = 0; // this.rowHeight * (this.realTextLines.length - 1);
							//# //System.out.println(this + ".initContent()/font3: caretX=" + this.caretX);
							//# //this.textLines[ this.textLines.length -1 ] += " "; 
							//# this.textLines[ 0 ] += " ";
					//#ifdef polish.css.font-bitmap
						//# } else {
							//# // a bitmap-font is used:
							//# this.caretPosition = this.text.length();
							//# this.caretX = this.bitMapFontViewer.getWidth();
							//# //System.out.println(this + ".initContent(): caretX=bitMapFontViewer.getWidth()=" + this.caretX);
							//# this.caretY = this.bitMapFontViewer.getHeight() - this.bitMapFontViewer.getFontHeight();
						//# }
					//#endif
				//# }
			//# }
			//# // set the internal information so that big TextBoxes can still be scrolled
			//# // correctly:
			//# this.internalX = 0;
			//# this.internalY = this.caretY;
			//# this.internalWidth = this.contentWidth;
			//# this.internalHeight = this.rowHeight;
			//# this.screen = getScreen();
			//# if (this.isFocused && this.parent instanceof Container ) {
				//# // ensure that the visible area of this TextField is shown:
				//# ((Container)this.parent).scroll(0, this); // problem: itemHeight is not yet set
			//# }
		//#endif
	}
	
	//#if tmp.directInput
	//# /**
	 //# * Sets the caret row.
	 //# * The fields originalRowText, caretColumn, caretRowFirstPart, caretX, caretRowLastPart and caretRowLastPartWidth
	 //# * are being set. Note that the field caretRowLastPartWidth is only set whent the
	 //# * layout is either center or right.
	 //# *  
	 //# * @param line the new caret row text
	 //# * @param column the column position of the caret
	 //# */
	//# private void setCaretRow( String line, int column ) {
		//#debug
		//# System.out.println("setCaretRow( line=\"" + line + "\", column=" + column + ")");
		//# this.originalRowText = line;
		//# int length = line.length();
		//# if (column > length )  {
			//# column = length;
		//# }
		//# this.caretColumn = column;
		//# boolean endsInLineBreak = (length >= 1) && (line.charAt(length-1) == '\n'); 
		//# String caretRowFirstPart;
		//# boolean firstPartIsFullRow = false;
		//# if (column == length || ( endsInLineBreak && column == length -1 )) {
			//# if ( endsInLineBreak ) {
				//# caretRowFirstPart = line.substring( 0, length - 1);								
			//# } else {
				//# caretRowFirstPart = line;				
			//# }
			//# firstPartIsFullRow = true;
		//# } else {
			//# caretRowFirstPart = line.substring( 0, column );
			//# //this.caretRowLastPartWidth = this.font.stringWidth(this.caretRowLastPart);;
		//# }
		//# if (this.isPassword) {
			//# this.caretX = stringWidth( "*" ) * caretRowFirstPart.length();
		//# } else {
			//# this.caretX = stringWidth(caretRowFirstPart);
		//# }
		//# if (this.isLayoutCenter || this.isLayoutRight) {
			//# if (firstPartIsFullRow) {
				//# this.caretRowWidth = this.caretX;
			//# } else {
				//# this.caretRowWidth = stringWidth( line );
			//# }
		//# }
		//# //System.out.println("caretRowWidth=" + this.caretRowWidth + " for line=" + line);
		//#if polish.css.text-wrap
			//# if (this.useSingleLine) {
				//# if (this.caretX > this.contentWidth) {
					//# this.xOffset = this.contentWidth - this.caretX - this.caretWidth - 5;
				//# } else {
					//# this.xOffset = 0;
				//# }
			//# }
		//#endif
		//#debug
		//# System.out.println("setCaretRow() result: endsInLineBreak=" + endsInLineBreak + ", firstPart=[" + caretRowFirstPart + "].");
	//# }
	//#endif
	
	

	/*
	 * Checks the caret position - is only used during debugging.
	private void checkCaretPosition() {
		int cPos = 0;
		for (int i = 0; i < this.realTextLines.length; i++) {
			String line = this.realTextLines[i];
			if (i == this.caretRow) {
				cPos += this.caretColumn;
				break;
			}
			cPos += line.length();
		}
		if (cPos != this.caretPosition) {
			System.out.println("=============================");
			System.out.println("Warning: this.caretPosition = " + this.caretPosition);
			System.out.println("but it should be: " + cPos );
			System.out.println("=============================");
		}
	}
	 */

	//#ifdef tmp.directInput
	//# /*
	 //# * Calculates the caret x and y positions.
	//# private void calculateCaretPosition() {
		//# // calculate row, column, x and y position of the caret:
		//# if (this.text == null) {
			//# this.caretX = 0;
			//# this.caretY = 0;
			//# return;
		//# }
		//#ifdef polish.css.font-bitmap
			//# if (this.bitMapFontViewer != null) {
				//# // a bitmap-font is used
				//# //TODO calculate caret-position with bitmap-fonts
			//# }
		//#endif
		//# // no bitmap-font is used:
		//# int maxPos = this.text.length();
		//# if (this.caretPosition == maxPos) {
			//# int length = this.textLines.length - 1;
			//# this.caretY = length * (this.font.getHeight() + this.paddingVertical);
			//# this.caretX = this.font.stringWidth( this.textLines[ length ] );
			//# return;
		//# }
		//# int pos = 0;
		//# int row = 0;
		//# int y = 0;
		//# int rowHeight = this.font.getHeight() + this.paddingVertical;
		//# for (int i = 0; i < this.textLines.length; i++) {
			//# String line = this.textLines[i];
			//# int lineLength = line.length();
			//# int textIndex = this.text.indexOf(line, pos);
			//# int lastPos = textIndex + lineLength;
			//# if (lastPos < maxPos) {
				//# char lastChar = this.text.charAt( lastPos );
				//# if (lastChar == ' ') {
					//# pos++;
					//# line += " ";
				//# }
			//# }
//# 			
			//# if (this.caretPosition <= pos + lineLength) {
				//# this.caretColumn = this.caretPosition - pos;
				//# this.caretRow = row;
				//# //TODO respect the layout of the text:
				//# if ( this.caretColumn != 0 ) {
					//# this.caretX = this.font.stringWidth(line.substring(0, this.caretColumn));
				//# } else {
					//# this.caretX = 0;
				//# }
				//# this.caretY = y;
				//# break;
			//# }
			//# pos += lineLength;
			//# y += rowHeight;
			//# row++;
		//# }
	//# }
	 //# */
	//#endif
	
	//#if polish.blackberry
//# //	protected void hideNotify() {
//# //		this.editField.onUndisplay();
//# //	}
	//#endif

	//#ifdef polish.useDynamicStyles
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Item#getCssSelector()
	 //# */
	//# protected String createCssSelector() {
		//# return "textfield";
	//# }
	//#endif

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#setStyle(de.enough.polish.ui.Style)
	 */
	public void setStyle(Style style) {
		super.setStyle(style);
		if (this.font == null) {
			this.font = Font.getDefaultFont();
		}
		//#ifdef polish.css.textfield-width
			//# Integer width = style.getIntProperty(27);
			//# if (width != null) {
				//# this.minimumWidth = width.intValue();
			//# }
		//#endif
		//#ifdef polish.css.textfield-height
			//# Integer height = style.getIntProperty(28);
			//# if (height != null) {
				//# this.minimumHeight = height.intValue();
			//# }
		//#endif
		//#ifdef polish.css.textfield-direct-input
			//# Boolean useDirectInputBool = style.getBooleanProperty(29);
			//# if (useDirectInputBool != null) {
				//# this.enableDirectInput = useDirectInputBool.booleanValue();
			//# }
		//#endif
		//#ifdef polish.css.textfield-caret-color
			//# Color colorInt = style.getColorProperty(30);
			//# if (colorInt != null) {
				//# this.caretColor = colorInt.getColor();
			//# } else if (this.caretColor == -1) {
				//# this.caretColor = this.textColor;
			//# }
		//#endif
		//#if polish.css.textfield-show-length && tmp.directInput
			//# Boolean showBool = style.getBooleanProperty(32);
			//# if (showBool != null) {
				//# this.showLength = showBool.booleanValue();
			//# }
		//#endif
		//#ifdef polish.css.textfield-caret-flash
			//# Boolean flashCursorBool = style.getBooleanProperty( 80 );
			//# if ( flashCursorBool != null ) {
				//# this.flashCaret = flashCursorBool.booleanValue();
				//# if (!this.flashCaret) {
					//# this.showCaret = true;
				//# }
			//# }
		//#endif	
		
		//#if tmp.usePredictiveInput
			//#if polish.css.predictive-containerstyle
				//# Style containerstyle = (Style) style.getObjectProperty(256);
				//# if (containerstyle != null) {
					//# this.predictiveAccess.choicesContainer.setStyle( containerstyle );
				//# }
			//#endif
			//#if polish.css.predictive-choicestyle
				//# Style choicestyle = (Style) style.getObjectProperty(257);
				//# if (choicestyle != null) {
					//# this.predictiveAccess.choiceItemStyle = choicestyle;
				//# }
			//#endif
			//#if polish.css.predictive-choice-orientation
				//# Integer choiceorientation = style.getIntProperty(285);
				//# if (choiceorientation != null) {
					//# this.predictiveAccess.choiceOrientation = choiceorientation.intValue();
				//# }
			//#endif
		//#endif			
	}
	
	//#ifdef tmp.directInput
//# 	
	//# protected boolean isValidInput( char insertChar, int position, String myText ) {
		//# if (!this.isEmail) {
			//# return true;
		//# }
		//# // check valid input for email addresses:
		//# char lowerCaseInsertChar = Character.toLowerCase( insertChar );
		//# boolean isValidInput = (insertChar >= '0' && insertChar <= '9')  || ( lowerCaseInsertChar >= 'a' && lowerCaseInsertChar <= 'z' ) ;
		//# if (!isValidInput) {
			//# boolean isInLocalPart = true; // are we in the first/local part before the '@' in the address?
//# 			
			//# String emailAddressText = myText;
			//# int atPosition = -1;
			//# int relativeCaretPosition = this.caretPosition;
			//# if (emailAddressText != null) {
				//# // extract single email address part when there are several email addresses (this can 
				//# // only happen when a ChoiceTextField is used)
				//# int separatorPosition;
				//# while ( (separatorPosition = emailAddressText.indexOf(this.emailSeparatorChar)) != -1 ) {
					//# if (separatorPosition < this.caretPosition) {
						//# emailAddressText = emailAddressText.substring( separatorPosition + 1);
						//# relativeCaretPosition -= separatorPosition;
					//# } else {
						//# emailAddressText = emailAddressText.substring( 0, separatorPosition );
						//# break;
					//# }
				//# }
				//# // check for the '@' sign as the separator between local part and domain name:
				//# atPosition = emailAddressText.indexOf('@');
				//# isInLocalPart = ( atPosition == -1 )  ||  ( atPosition >= this.caretPosition );
			//# }
			//# if (isInLocalPart) {
				//# boolean isAtFirstChar = (emailAddressText == null || relativeCaretPosition == 0);
				//# isValidInput = ( VALID_LOCAL_EMAIL_ADDRESS_CHARACTERS.indexOf( insertChar ) != -1 ) 
							//# && !( (insertChar == '.') && isAtFirstChar) // the first char must not be a dot.
							//# && !(atPosition != -1 && insertChar == '@' && atPosition != position) // it's not allowed to enter two @ characters
							//# && !(insertChar == '@' &&  isAtFirstChar ); // the first character must not be the '@' sign
			//# } else {
				//# isValidInput = VALID_DOMAIN_CHARACTERS.indexOf( insertChar ) != -1;
			//# }
			//# if (!isValidInput) {
				//#debug
				//# System.out.println("email: invalid input!");
			//# }
		//# }
		//# return isValidInput;
	//# }
//# 	
	//# protected void commitCurrentCharacter() {
		//#debug
		//# System.out.println("comitting current character: " + this.caretChar);
		//# String myText;
		//# if (this.isPassword) {
			//# myText = this.passwordText;
		//# } else {
			//# myText = this.text;
		//# }
		//# char insertChar  = this.caretChar; 
		//# if (!isValidInput( insertChar, this.caretPosition, myText )) {
			//# return;
		//# }
		//# this.caretChar = this.editingCaretChar;
		//# // increase caret position after notifying itemstatelisteners in case they want to know the current caret position...
		//# this.caretPosition++;
		//# this.caretColumn++;
		//# if (this.isPassword) {
			//# this.caretX += stringWidth("*");
		//# } else {
			//# this.caretX += this.caretWidth;
		//# }
		//# boolean nextCharInputHasChanged = false;
		//#if polish.TextField.suppressAutoInputModeChange
			//# if ( this.inputMode == MODE_FIRST_UPPERCASE  
				//# && (insertChar == ' ' ||  ( insertChar == '.' && !(this.isEmail || this.isUrl)) )) 
			//# {
				//# this.nextCharUppercase = true;
			//# } else {
				//# this.nextCharUppercase = false;
			//# }
		//#else
			//# nextCharInputHasChanged = this.nextCharUppercase;
			//# if ( ( (this.inputMode == MODE_FIRST_UPPERCASE || this.nextCharUppercase) 
					//# && insertChar == ' ') 
				//# || ( insertChar == '.' && !(this.isEmail || this.isUrl))) 
			//# {
				//# this.nextCharUppercase = true;
			//# } else {
				//# this.nextCharUppercase = false;
			//# }
			//# nextCharInputHasChanged = (this.nextCharUppercase != nextCharInputHasChanged);
			//# if ( this.inputMode == MODE_FIRST_UPPERCASE ) {
				//# this.inputMode = MODE_LOWERCASE;
			//# }
		//#endif
			//#if polish.css.textfield-show-length  && tmp.useInputInfo
			//# if (this.showLength || nextCharInputHasChanged) {
				//# updateInfo();
			//# }
		//#elif tmp.useInputInfo
			//# if (nextCharInputHasChanged) {
				//# updateInfo();
			//# }
		//#endif
//# 
//# 		
		//# notifyStateChanged();
		//#ifdef polish.css.textfield-caret-flash
			//# if (!this.flashCaret) {
				//# repaint();
			//# }
		//#endif
	//# }
//# 
	//# protected void insertCharacter( char insertChar, boolean append, boolean commit ) {
		//# if (append && this.text != null && this.text.length() >= this.maxSize ) {
			//# return;
		//# }
		//#debug
		//# System.out.println( "inserting character " + insertChar + ", append=" + append + ", commit=" + commit +", caretPos=" + this.caretPosition );
		//# String myText;
		//# if (this.isPassword) {
			//# myText = this.passwordText;
		//# } else {
			//# myText = this.text;
		//# }
//# 		
		//# int cp = this.caretPosition;
		//# if (!isValidInput( insertChar, cp, myText )) {
			//# return;
		//# }
//# 	
		//# if (myText == null || myText.length() == 0) {
			//# myText = "" + insertChar;
		//# } else if (append) {
			//# StringBuffer buffer = new StringBuffer( myText.length() + 1 );
			//# buffer.append( myText.substring( 0, cp ) )
				//# .append( insertChar );
			//# if (cp < myText.length() ) {
				//# buffer.append( myText.substring( cp ) );
			//# }
			//# myText = buffer.toString();
		//# } else {
			//# // replace current caret char:
			//# StringBuffer buffer = new StringBuffer(myText.length());
			//# buffer.append(myText.substring( 0, cp ) ).append( insertChar );
			//# if (cp < myText.length() - 1) {
				//# buffer.append( myText.substring( this.caretPosition + 1 ) );
			//# }
			//# myText = buffer.toString();
		//# }
		//# //System.out.println("new text=[" + myText + "]" );
		//# boolean nextCharInputHasChanged = false;
		//# if (commit) {
			//# this.caretPosition++;
			//# this.caretColumn++;
			//#if polish.TextField.suppressAutoInputModeChange
				//# if ( this.inputMode == MODE_FIRST_UPPERCASE  
					//# && (insertChar == ' ' ||  ( insertChar == '.' && !(this.isEmail || this.isUrl)) )) 
				//# {
					//# this.nextCharUppercase = true;
				//# } else {
					//# this.nextCharUppercase = false;
				//# }
			//#else
				//# nextCharInputHasChanged = this.nextCharUppercase;
				//# if ( ( (this.inputMode == MODE_FIRST_UPPERCASE || this.nextCharUppercase) 
						//# && insertChar == ' ') 
					//# || ( insertChar == '.' && !(this.isEmail || this.isUrl))) 
				//# {
					//# this.nextCharUppercase = true;
				//# } else {
					//# this.nextCharUppercase = false;
				//# }
				//# nextCharInputHasChanged = (this.nextCharUppercase != nextCharInputHasChanged);
				//# if ( this.inputMode == MODE_FIRST_UPPERCASE ) {
					//# this.inputMode = MODE_LOWERCASE;
				//# }
			//#endif
			//# this.caretChar = this.editingCaretChar;
		//# }
		//# setString( myText );
		//# if (!commit) {
			//# if (myText.length() == 1) {
				//# this.caretPosition = 0;
			//# }
		//# } else {
			//# notifyStateChanged();			
			//#if polish.css.textfield-show-length  && tmp.useInputInfo
				//# if (this.showLength || nextCharInputHasChanged) {
					//# updateInfo();
				//# }
			//#elif tmp.useInputInfo
				//# if (nextCharInputHasChanged) {
					//# updateInfo();
				//# }
			//#endif
		//# }
	//# }
	//#endif

	//#if tmp.directInput && tmp.useInputInfo
	//# /**
	 //# * Updates the information text
	 //# */
	//# public void updateInfo() {
		//# if (this.isUneditable || !this.isShowInputInfo) {
			//# // don't show info when this field is not editable
			//# return;
		//# }
		//# // # debug
		//# // System.out.println("update info: " + this.text );
		//# String modeStr;
		//# switch (this.inputMode) {
			//# case MODE_LOWERCASE:
				//# if (this.nextCharUppercase) {
					//# modeStr = "Abc";
				//# } else {
					//# modeStr = "abc";
				//# }
				//# break;
			//# case MODE_FIRST_UPPERCASE:
				//# modeStr = "Abc";
				//# break;
			//# case MODE_UPPERCASE:
				//# modeStr = "ABC";
				//# break;
			//# case MODE_NATIVE:
				//# modeStr = "Nat.";
				//# break;
			//# default:
				//# modeStr = "123";
				//# break;
		//# }
//# 		
		//#if tmp.usePredictiveInput
		//# if(this.predictiveInput)
		//# {
			//# if(this.predictiveAccess.getInfo() != null)
			//# {
				//# modeStr = this.predictiveAccess.getInfo();
			//# }
//# 			
			//#if polish.TextField.includeInputInfo
				//# if(this.infoItem != null && this.infoItem.getStyle().layout == Graphics.RIGHT)
				//# {
					//# modeStr = PredictiveAccess.INDICATOR + modeStr;
				//# }
				//# else
				//# {
					//# modeStr = modeStr + PredictiveAccess.INDICATOR;
				//# }
			//#else
				//# modeStr = PredictiveAccess.INDICATOR + modeStr;
			//#endif
		//# }
		//#endif
//# 		
		//#ifdef polish.css.textfield-show-length
			//# if (this.showLength) {
				//# int length = (this.text == null) ? 0 : this.text.length();
				//# modeStr = length + " | " + modeStr;
			//# }
		//#endif
		//#if tmp.includeInputInfo
			//# if (this.infoItem == null) {
				//#if !polish.TextField.useExternalInfo
					//#style info, default
					//# this.infoItem = new StringItem( null, modeStr );
					//# this.infoItem.screen = getScreen();
					//# this.infoItem.parent = this;
					//# repaint();
				//#endif
			//# } else {
				//# this.infoItem.setText(modeStr);			
			//# }
			//# //System.out.println("setting info to [" + modeStr + "]");
		//#else
			//# if (this.screen == null) {
				//# this.screen = getScreen();
			//# }
			//# if (this.screen != null) {
				//# this.screen.setInfo( modeStr );
			//# }
		//#endif
	//# }
	//#endif
	
	

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.StringItem#animate(long, de.enough.polish.ui.ClippingRegion)
	 */
	public void animate(long currentTime, ClippingRegion repaintRegion)
	{
		//#if tmp.usePredictiveInput
			//# this.predictiveAccess.animateChoices(currentTime, repaintRegion );
		//#endif
		super.animate(currentTime, repaintRegion);
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#animate()
	 */
	public boolean animate() { 
		if (!this.isFocused) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		//#if polish.blackberry
			//#if polish.Bugs.ItemStateListenerCalledTooEarly
				//# if (this.lastFieldChangedEvent != 0 && currentTime - this.lastFieldChangedEvent > 500) {
					//# this.lastFieldChangedEvent = 0;
					//# setString( this.editField.getText() );
					//# notifyStateChanged();
					//# return true;
				//# }
			//#endif
			//# return false;
		//#else
			//#if tmp.directInput
				//# synchronized ( this.lock ) {
					//# if (this.caretChar != this.editingCaretChar) {
						//# if ( !this.isKeyDown && (currentTime - this.lastInputTime) >= INPUT_TIMEOUT ) {
							//# commitCurrentCharacter();
							//# //insertCharacter( this.caretChar, false, true);
						//# }
					//# } else if (this.isKeyDown && 
							//# this.deleteKeyRepeatCount != 0 && 
							//# (this.deleteKeyRepeatCount % 3) == 0 && 
							//# this.text != null && 
							//# this.caretPosition > 0
							//# ) 
					//# {
						//# if (this.deleteKeyRepeatCount >= 9) {
							//# String myText = this.text;
							//# if (myText != null && myText.length() > 0) {
								//# setString(null);
								//# notifyStateChanged();
							//# }
						//# } else if (this.caretPosition > 0){
							//# String myText = getString();
//# 							
							//# int nextStop = this.caretPosition - 1;
							//# while (myText.charAt(nextStop) != ' ' && nextStop > 0) {
								//# nextStop--;
							//# }
							//# //System.out.println("next stop=" + nextStop + ", caretPosition=" + this.caretPosition);
							//# setString(myText.substring( 0, nextStop) + myText.substring( this.caretPosition ) );
							//# notifyStateChanged();
						//# }
					//# }
				//# }
			//#endif
			if (!this.flashCaret || this.isUneditable) {
				//System.out.println("TextField.animate():  flashCaret==false");
				return false;
			}
			if ( currentTime - this.lastCaretSwitch > 500 ) {
				this.lastCaretSwitch = currentTime;
				this.showCaret = ! this.showCaret;
				return true;
			} else {
				return false;
			}
		//#endif
	}
	
	//#if !polish.blackberry
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Item#handleKeyPressed(int, int)
	 */
	protected boolean handleKeyPressed(int keyCode, int gameAction) {
		this.isKeyPressedHandled = false;
		//#ifndef tmp.directInput
			//#if polish.bugs.inversedGameActions
				//# if ((gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM8)
					//# || (gameAction == Canvas.DOWN && keyCode != Canvas.KEY_NUM2)
					//# || (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM6)
					//# || (gameAction == Canvas.RIGHT && keyCode != Canvas.KEY_NUM4)
					//# || (gameAction == Canvas.FIRE && keyCode != Canvas.KEY_NUM5)
					//# || (this.screen.isSoftKey(keyCode, gameAction))
				//# ) 
				//# {
					//# return false;
				//# }
			//#else
				if ((gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM2) 
						|| (gameAction == Canvas.DOWN && keyCode != Canvas.KEY_NUM8)
						|| (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM4)
						|| (gameAction == Canvas.RIGHT && keyCode != Canvas.KEY_NUM6)
						|| (gameAction == Canvas.FIRE && keyCode != Canvas.KEY_NUM5)
						|| (this.screen.isSoftKey(keyCode, gameAction))
					) 
				{
					return false;
				}
			//#endif
		//#elif !polish.blackberry
			//# if (this.inputMode == MODE_NATIVE) {
				//# if ((gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM2) 
						//# || (gameAction == Canvas.DOWN && keyCode != Canvas.KEY_NUM8)
						//# || (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM4)
						//# || (gameAction == Canvas.RIGHT && keyCode != Canvas.KEY_NUM6)
						//# || (gameAction == Canvas.FIRE && keyCode != Canvas.KEY_NUM5)
						//# || (this.screen.isSoftKey(keyCode, gameAction))
						//# ) 
				//# {
					//# return false;
				//# }
			//# }
			//# this.isKeyDown = true;
		//#endif
		
		//#if tmp.allowDirectInput
			//# if (this.enableDirectInput) {
		//#endif
				//#ifdef tmp.directInput
					//#if !polish.blackberry
						//#if polish.key.ChangeNumericalAlphaInputModeKey:defined
						//#= if (this.inputMode == MODE_NATIVE && keyCode != KEY_CHANGE_MODE && keyCode != ${polish.key.ChangeNumericalAlphaInputModeKey}  ) {
						//#else
						//# if (this.inputMode == MODE_NATIVE && keyCode != KEY_CHANGE_MODE) {
						//#endif
							//#if tmp.useNativeTextBox
								//# showTextBox();
								//# return true;
							//#endif
						//# }
					//#endif
//# 					
					//# synchronized ( this.lock ) {
//# 						
	//# //					if (this.text == null) { // in that case no mode change can be done with an empty textfield 
	//# //						return false;
	//# //					}
	//# //					else 
						//# if (this.isUneditable) {
							//# return false;
						//# }
//# 						
						//# boolean handled = false;
//# 						
						//#if tmp.usePredictiveInput && !polish.key.ChangeNumericalAlphaInputModeKey:defined
                        //# if ( keyCode == KEY_CHANGE_MODE && !this.isNumeric && !this.isUneditable)
                        //# {
                                //# if(this.lastTimePressed == -1)
                                //# {
                                        //# this.nextMode = !this.predictiveInput;
                                        //# this.lastTimePressed = System.currentTimeMillis();
                                //# }
                                //# else
                                //# {
                                        //# if((System.currentTimeMillis() - this.lastTimePressed) > SWITCH_DELAY && TrieProvider.isPredictiveInstalled())
                                        //# {
                                                //# if(this.nextMode != this.predictiveInput)
                                                //# {
                                                        //# this.predictiveInput = !this.predictiveInput;
//# 
                                                        //# this.nextMode = this.predictiveInput;
                                                        //# updateInfo();
//# 
                                                        //# this.predictiveInput = !this.predictiveInput;
                                                //# }
                                        //# }
                                //# }
//# 
                                //# handled = true;
                        //# }
                        //#endif
//# 
                        //#ifdef polish.key.ChangeNumericalAlphaInputModeKey:defined
                        //#= if(!handled &&
                        //#=     !(keyCode == KEY_CHANGE_MODE && !this.isNumeric &&
                        //#=       !(KEY_CHANGE_MODE == Canvas.KEY_NUM0 &&
                        //#=         this.inputMode == MODE_NUMBERS)) &&
                        //#=     !(keyCode == ${polish.key.ChangeNumericalAlphaInputModeKey} &&
                        //#=       !this.isNumeric))
                        //#else
                        //# if(!handled && keyCode != KEY_CHANGE_MODE)
                        //#endif
                        //# {
                                //# handled = handleKeyInsert(keyCode, gameAction);
                        //# }
//# 						
						//# // Backspace
						//#ifdef polish.key.ClearKey:defined
							//#= if (keyCode == ${polish.key.ClearKey}
						//#else
							//# if ( keyCode == -8 || keyCode == 8 
						//#endif
						//#if polish.key.backspace:defined
							//#= || keyCode == ${polish.key.backspace}
						//#endif
							//# && !handled) 
						//# {
							//# handled = handleKeyClear(keyCode, gameAction);
						//# }
//# 							
						//# // Navigate the caret
						//# if (   (gameAction == Canvas.UP 	|| 
								//# gameAction == Canvas.DOWN 	||
								//# gameAction == Canvas.LEFT 	||
								//# gameAction == Canvas.RIGHT  ||
								//# gameAction == Canvas.FIRE)  &&
								//# !handled) 
						//# {
							//# handled = handleKeyNavigation(keyCode, gameAction);
							//# if (!handled && getScreen().isGameActionFire(keyCode, gameAction) && this.defaultCommand != null) {
								//# notifyItemPressedStart();
								//# handled = true;
							//# }
						//# }
//# 						
						//# if(true)
						//# {
							//# this.isKeyPressedHandled = handled;
							//# return handled;
						//# }
					//# }
				//#endif
		//#if tmp.allowDirectInput
			//# }
		//#endif
		//#ifndef polish.hasPointerEvents
			String currentText = this.isPassword ? this.passwordText : this.text;
			if (this.enableDirectInput) {
				int currentLength = (this.text == null ? 0 : this.text.length());
				if ( 	keyCode >= Canvas.KEY_NUM0 && 
						keyCode <= Canvas.KEY_NUM9) 
				{	
					if (currentLength >= this.maxSize) {
						// in numeric mode ignore 2,4,6 and 8 keys, so that they are not processed
						// by a parent component:
						this.isKeyPressedHandled = true;
						return true;
					}
					String newText = (currentText == null ? "" : currentText ) + (keyCode - 48);
					setString( newText );
					notifyStateChanged();
					this.isKeyPressedHandled = true;
					return true;
				}
				if (currentLength > 0) {
					//#ifdef polish.key.ClearKey:defined
if ((keyCode == -8) || (gameAction == Canvas.LEFT)) {
					//#else
						//# if (keyCode == -8 || gameAction == Canvas.LEFT) {						
					//#endif
						setString( currentText.substring(0, currentLength - 1) );
						notifyStateChanged();
						this.isKeyPressedHandled = true;
						return true;
					}
				}				
				return false;
			}
		//#endif
		
		
		if ( keyCode >= 32 
			//#ifdef polish.key.ClearKey:defined
|| (keyCode == -8)
			//#else
				//# || (keyCode == -8 || keyCode == 8)
			//#endif
			//#if ${ isOS( Windows ) }
				//# || (gameAction != Canvas.DOWN && gameAction != Canvas.UP && gameAction != Canvas.LEFT && gameAction != Canvas.RIGHT)
			//#endif
			|| (getScreen().isGameActionFire(keyCode, gameAction) ) )
		{	
			//#if !(polish.blackberry || tmp.forceDirectInput)
				showTextBox();
			//#endif
			return true;
		} else {
			return false;
		}
		
	}
	//#endif
	
	/**
	 * Tries to interpret a key pressed event for inserting a character into this TextField.
	 * @param keyCode the key code of the event
	 * @param gameAction the associated game action
	 * @return true when the key could be interpreted as a character
	 */
	protected boolean handleKeyInsert(int keyCode, int gameAction)
	{		
		//#if tmp.directInput
			//#if tmp.usePredictiveInput
				//# if (this.predictiveInput) {
					//#if tmp.supportsAsciiKeyMap
						//# if (!this.screen.isKeyboardAccessible()) {
					//#endif
							//# return this.predictiveAccess.keyInsert(keyCode, gameAction);
					//#if tmp.supportsAsciiKeyMap
						//# }
					//#endif
				//# }
			//#endif
//# 			
			//# int currentLength = (this.text == null ? 0 : this.text.length());
			//# char insertChar = (char) (' ' + (keyCode - 32));
			//#if tmp.supportsAsciiKeyMap
				//# try {
					//# String name  = this.screen.getKeyName( keyCode );
					//# if (name != null && name.length() == 1) {
						//# insertChar = name.charAt(0);
					//# }
				//# } catch (IllegalArgumentException e) {
					//# // ignore
				//# }
				//#if polish.key.maybeSupportsAsciiKeyMap
					//# if (!useAsciiKeyMap) {
						//# if (keyCode >= 32
								//# && (keyCode < Canvas.KEY_NUM0 || keyCode > Canvas.KEY_NUM9)
								//# && (keyCode != Canvas.KEY_POUND && keyCode != Canvas.KEY_STAR)
								//# && (keyCode <= 126) // only allow ascii characters for the initial input...
								//# && ( !getScreen().isSoftKey(keyCode, gameAction) )
						//# ) {
							//# useAsciiKeyMap = true;
						//# }
					//# }
				//#endif
				//# if (keyCode >= 32 
						//# && this.screen.isKeyboardAccessible() 
						//#if polish.key.maybeSupportsAsciiKeyMap
							//# && useAsciiKeyMap
						//#endif
						//# && this.inputMode != MODE_NUMBERS 
						//# && !this.isNumeric
						//# && !( 	   (gameAction == Canvas.UP     &&  insertChar != '2' && keyCode == this.screen.getKeyCode(Canvas.UP) ) 
								//# || (gameAction == Canvas.DOWN   &&  insertChar != '8' && keyCode == this.screen.getKeyCode(Canvas.DOWN)  )
								//# || (gameAction == Canvas.LEFT   &&  insertChar != '4' && keyCode == this.screen.getKeyCode(Canvas.LEFT)	)
								//# || (gameAction == Canvas.RIGHT  &&  insertChar != '6' && keyCode == this.screen.getKeyCode(Canvas.RIGHT))
								//# //|| (gameAction == Canvas.FIRE   &&  keyCode == this.screen.getKeyCode(Canvas.FIRE) )  
							//# )
						//# ) 
				//# {
//# 					
					//# if (this.nextCharUppercase || this.inputMode == MODE_UPPERCASE) {
						//# insertChar = Character.toUpperCase(insertChar);
					//# }
					//# insertCharacter( insertChar, true, true );
					//# return true;
				//# }
				//#if polish.key.enter:defined
					//#= if ( keyCode == ${polish.key.enter} ) {
						//# //this.caretChar = '\n';
						//# insertCharacter('\n', true, true );
						//# return true;
					//# }
				//#endif
			//#endif
			//# if (this.inputMode == MODE_NUMBERS && !this.isUneditable) {
				//# if ( keyCode >= Canvas.KEY_NUM0 && keyCode <= Canvas.KEY_NUM9 )  
				//# {
					//# if (currentLength >= this.maxSize) {
						//# // ignore this key event - also don't forward it to the parent component:
						//# return true;
					//# }
					//# insertChar = Integer.toString( keyCode - Canvas.KEY_NUM0 ).charAt( 0 );
					//# insertCharacter(insertChar, true, true );
					//# return true;
				//# }
				//#if polish.TextField.numerickeys.1:defined
					//# String numericKeyStr = null;
					//# int foundNumber = -1;
					//#= numericKeyStr = "${polish.TextField.numerickeys.1}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 1;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.2}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 2;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.3}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 3;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.4}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 4;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.5}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 5;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.6}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 6;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.7}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 7;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.8}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 8;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.9}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 9;
					//# }
					//#= numericKeyStr = "${polish.TextField.numerickeys.0}";
					//# if (numericKeyStr.indexOf(insertChar) != -1) {
						//# foundNumber = 0;
					//# }
					//# if (foundNumber != -1) {
						//# if (currentLength >= this.maxSize) {
							//# // ignore this key event - also don't forward it to the parent component:
							//# return true;
						//# }
						//# insertChar = Integer.toString( foundNumber ).charAt( 0 );
						//# insertCharacter(insertChar, true, true );
						//# return true;						
					//# }
				//#endif
				//# if ( this.isDecimal ) {
					//# //System.out.println("handling key for DECIMAL TextField");
					//# if (currentLength < this.maxSize 
							//# && ( keyCode == Canvas.KEY_POUND || keyCode == Canvas.KEY_STAR )
							//# && (this.text.indexOf( Locale.DECIMAL_SEPARATOR) == -1)
							//# ) 
					//# {
//# 						
						//# insertChar = Locale.DECIMAL_SEPARATOR;
						//# insertCharacter(insertChar, true, true );
						//# return true;								
					//# }
				//# }
//# 				
			//# }
			//# if ( (!this.isNumeric) //this.inputMode != MODE_NUMBERS 
					//# && !this.isUneditable
					//# && ((currentLength < this.maxSize) || ( currentLength == this.maxSize && this.caretChar != this.editingCaretChar && keyCode == this.lastKey)) 
					//# && ( (keyCode >= Canvas.KEY_NUM0 && keyCode <= Canvas.KEY_NUM9)
					  //# || (keyCode == Canvas.KEY_POUND ) 
					  //# || (keyCode == Canvas.KEY_STAR )
					//#if tmp.supportsSymbolEntry && polish.key.AddSymbolKey:defined
						//#= || (keyCode == ${polish.key.AddSymbolKey} )
					//#endif
					//#if tmp.supportsSymbolEntry && polish.key.AddSymbolKey2:defined
						//#= || (keyCode == ${polish.key.AddSymbolKey2} )
					//#endif
					//# )) 
			//# {	
				//#if tmp.supportsSymbolEntry && (polish.key.AddSymbolKey:defined || polish.key.AddSymbolKey2:defined)
					//# boolean showSymbolList = false;
					//#if polish.key.AddSymbolKey:defined
						//#= showSymbolList = (keyCode == ${polish.key.AddSymbolKey});
					//#endif
					//#if polish.key.AddSymbolKey2:defined
						//#= showSymbolList = showSymbolList || (keyCode == ${polish.key.AddSymbolKey2});
					//#endif
					//# if ( showSymbolList ) {
						//# showSymbolsList();
						//# return true;
					//# }
				//#endif
				//# String alphabet;
				//# if (keyCode == Canvas.KEY_POUND) {
					//# alphabet = charactersKeyPound;
				//# } else if (keyCode == Canvas.KEY_STAR) {
					//# alphabet = charactersKeyStar;
				//# } else {
					//# alphabet = this.characters[ keyCode - Canvas.KEY_NUM0 ];
				//# }
				//# if (alphabet == null || (alphabet.length() == 0)) {
					//# return false;
				//# }
				//# this.lastInputTime = System.currentTimeMillis();
				//# char newCharacter;
				//# int alphabetLength = alphabet.length();
				//# boolean appendNewCharacter = false;
				//# if (keyCode == this.lastKey && (this.caretChar != this.editingCaretChar)) {
					//# this.characterIndex++;
					//# if (this.characterIndex >= alphabetLength) {
						//# this.characterIndex = 0;
					//# }
				//# } else {
					//# // insert the last character into the text:
					//# if (this.caretChar != this.editingCaretChar) {
						//# commitCurrentCharacter();
						//# if (currentLength + 1 > this.maxSize) {
							//# return true;
						//# }
					//# }
					//# appendNewCharacter = true;
					//# this.characterIndex = 0;
					//# this.lastKey = keyCode;
				//# }
				//# newCharacter = alphabet.charAt( this.characterIndex );
				//# //System.out.println("TextField.handleKeyPressed(): newCharacter=" + newCharacter + ", currentLength=" + currentLength + ", maxSize=" + this.maxSize + ", text.length()=" + this.text.length() );
				//# if ( this.inputMode == MODE_UPPERCASE 
						//# || this.nextCharUppercase ) 
				//# {
					//# newCharacter = Character.toUpperCase(newCharacter);
				//# }
				//#ifdef polish.css.font-bitmap
					//# if (this.bitMapFont != null) {
						//# this.caretWidth = this.bitMapFont.charWidth( newCharacter );
					//# } else {
				//#endif
						//# this.caretWidth = this.font.charWidth( newCharacter );
				//#ifdef polish.css.font-bitmap
					//# }
				//#endif
				//# this.caretChar = newCharacter;
				//# if (alphabetLength == 1) {
					//# insertCharacter( newCharacter, true, true );
				//# } else {
					//# insertCharacter( newCharacter, appendNewCharacter, false );
				//# }
				//# return true;
			//# }
		//#endif
		return false;
	}
	
	protected boolean handleKeyClear(int keyCode, int gameAction)
	{
		//#if tmp.directInput
			//# if (this.isUneditable) {
				//# return false;
			//# }
			//#if tmp.usePredictiveInput
				//# if (this.predictiveInput) {
					//# return this.predictiveAccess.keyClear(keyCode, gameAction);
				//# }
			//#endif			
			//# if ( this.text != null && this.text.length() > 0) {			
				//# return deleteCurrentChar();
			//# }
		//#endif
			
		return false;
	}
	
	protected boolean handleKeyMode(int keyCode, int gameAction)
	{
		//#if tmp.directInput
			//#if tmp.usePredictiveInput
				//# if (this.predictiveInput) {
					//# return this.predictiveAccess.keyMode(keyCode, gameAction);
				//# }
			//#endif
//# 			
			//#if polish.key.ChangeNumericalAlphaInputModeKey:defined
				//#= if (keyCode == ${polish.key.ChangeNumericalAlphaInputModeKey} && !this.isNumeric && !this.isUneditable) {
					//# if (this.inputMode == MODE_NUMBERS) {
						//# this.inputMode = MODE_LOWERCASE;
					//# } else {
						//# this.inputMode = MODE_NUMBERS;
					//# }
					//#if tmp.useInputInfo
						//# updateInfo();
					//#endif
					//# if (this.caretChar != this.editingCaretChar) {
						//# commitCurrentCharacter();
					//# }
					//# if (this.inputMode == MODE_FIRST_UPPERCASE) {
						//# this.nextCharUppercase = true;
					//# } else {
						//# this.nextCharUppercase = false;
					//# }
					//# return true;
				//# }
			//#endif
			//#if polish.key.supportsAsciiKeyMap.condition:defined && polish.key.shift:defined
				//# // there is a shift key responsible for switching the input mode which
				//# // is only used when the device is opened up - example includes the Nokia/E70.
				//# if (!this.screen.isKeyboardAccessible()) {
			//#endif
				//#if polish.key.ChangeNumericalAlphaInputModeKey:defined
					//#= if ( keyCode == KEY_CHANGE_MODE && !this.isNumeric && !this.isUneditable && (!(KEY_CHANGE_MODE == Canvas.KEY_NUM0 && this.inputMode == MODE_NUMBERS)) ) {
				//#else
				//# if ( keyCode == KEY_CHANGE_MODE && !this.isNumeric && !this.isUneditable) {
				//#endif
					//# if (this.nextCharUppercase && this.inputMode == MODE_LOWERCASE) {
						//# this.nextCharUppercase = false;
					//# } else {
						//# this.inputMode++;							
					//# }
					//#if polish.key.ChangeNumericalAlphaInputModeKey:defined
						//# if (this.inputMode > MODE_UPPERCASE) {
							//#if polish.TextField.allowNativeModeSwitch
								//# if (this.inputMode > MODE_NATIVE) {
									//# this.inputMode = MODE_LOWERCASE;
								//# } else {
									//# this.inputMode = MODE_NATIVE;
								//# }
							//#else
								//# this.inputMode = MODE_LOWERCASE;
							//#endif
						//# }
					//#elif polish.TextField.allowNativeModeSwitch
						//# if (this.inputMode > MODE_NATIVE) {
							//# this.inputMode = MODE_LOWERCASE;
						//# }
					//#else
						//# if (this.inputMode > MODE_NUMBERS) {
							//# this.inputMode = MODE_LOWERCASE;
						//# }
					//#endif
					//#if tmp.useInputInfo
						//# updateInfo();
					//#endif
					//# if (this.caretChar != this.editingCaretChar) {
						//# commitCurrentCharacter();
					//# }
					//# if (this.inputMode == MODE_FIRST_UPPERCASE) {
						//# this.nextCharUppercase = true;
					//# } else {
						//# this.nextCharUppercase = false;
					//# }
					//# return true;
				//# }
			//#if polish.key.supportsAsciiKeyMap.condition:defined && polish.key.shift:defined
				//# }
			//#endif
			//#if polish.key.shift:defined
				//# if ( keyCode == KEY_SHIFT && !this.isNumeric && !this.isUneditable) {
					//# if (this.nextCharUppercase && this.inputMode == MODE_LOWERCASE) {
						//# this.nextCharUppercase = false;
					//# } else {
						//# int mode = this.inputMode + 1;
						//# if (mode >= MODE_NUMBERS) {
							//# mode = MODE_LOWERCASE;
						//# }
						//# if (mode == MODE_FIRST_UPPERCASE) {
							//# this.nextCharUppercase = true;
						//# } else {
							//# this.nextCharUppercase = false;
						//# }
						//# this.inputMode = mode;
					//# }
					//#if tmp.useInputInfo
						//# updateInfo();
					//#endif
					//# return true;
				//# }
			//#endif
		//#endif
		return false;
	}
	

	protected boolean handleKeyNavigation(int keyCode, int gameAction)
	{
		//#if tmp.directInput
			//# if (this.realTextLines == null) {
				//# return false;
			//# }
//# 		
			//#if tmp.usePredictiveInput
				//# if(this.predictiveInput) {
					//# return this.predictiveAccess.keyNavigation(keyCode, gameAction);
				//# }
			//#endif
//# 			
			//# char character = this.caretChar;
			//# boolean characterInserted = character != this.editingCaretChar;
			//# if (characterInserted) {
				//# commitCurrentCharacter();
			//# }
//# 			
			//# if (getScreen().isGameActionFire(keyCode, gameAction)
					//# && this.defaultCommand != null 
					//# && this.itemCommandListener != null) 
			//# {
				//# this.itemCommandListener.commandAction(this.defaultCommand, this);
				//# return true;
			//# }
			//# else if (gameAction == Canvas.UP && keyCode != Canvas.KEY_NUM2) {
				//#ifdef polish.css.font-bitmap
					//# if (this.bitMapFontViewer != null) {
						//# // a bitmap-font is used
						//# return false;
					//# }
				//#endif
				//# // this TextField has a normal font:
				//# if (this.caretRow ==  0) {
					//# return false;
				//# } 
				//# // restore the text-line:
				//# this.caretRow--;
				//# this.caretY -= this.rowHeight;
				//# this.internalY = this.caretY;
				//# String fullLine = this.realTextLines[ this.caretRow ];
				//# int previousCaretRowFirstLength = this.caretColumn;
				//# setCaretRow(fullLine, this.caretColumn );
				//# this.caretPosition -= previousCaretRowFirstLength + (this.originalRowText.length() - this.caretColumn);
				//# this.internalX = 0;
				//# this.internalY = this.caretRow * this.rowHeight;
				//#if tmp.updateDeleteCommand
					//# updateDeleteCommand( this.text );
				//#endif
				//# return true;
			//# } else if (gameAction == Canvas.DOWN && keyCode != Canvas.KEY_NUM8) {
				//#ifdef polish.css.font-bitmap
					//# if (this.bitMapFontViewer != null) {
						//# // a bitmap-font is used
						//# return false;
					//# }
				//#endif
				//# if (this.textLines == null || this.caretRow >= this.textLines.length - 1) {
					//# return false;
				//# } 
//# 	
				//# String lastLine = this.originalRowText;
				//# int lastLineLength = lastLine.length();
				//# this.caretRow++;	
				//# this.caretY += this.rowHeight;
				//# this.internalY = this.caretY;
				//# int lastCaretRowLastPartLength = lastLineLength - this.caretColumn;
				//# String nextLine = this.realTextLines[ this.caretRow ];
				//# setCaretRow( nextLine, this.caretColumn );
				//# this.caretPosition += (lastCaretRowLastPartLength + this.caretColumn );
				//# this.internalY = this.caretRow * this.rowHeight;
				//#if tmp.updateDeleteCommand
					//# updateDeleteCommand( this.text );
				//#endif
				//# return true;
			//#if polish.i18n.rightToLeft
			//# } else if (gameAction == Canvas.RIGHT && keyCode != Canvas.KEY_NUM6) {
			//#else
			//# } else if (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM4) {
			//#endif
				//#ifdef polish.css.font-bitmap
				//# if (this.bitMapFontViewer != null) {
					//# // a bitmap-font is used
					//# // delete last character:
					//# return deleteCurrentChar();
				//# }
				//#endif
				//# int column = this.caretColumn;
				//# if (column > 0) {
					//# this.caretPosition--;
					//# column--;
					//# setCaretRow( this.originalRowText, column );
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
//# 						
					//# return true;
				//# } else if ( this.caretRow > 0) {
					//# // this is just a visual line-break:
					//# //this.caretPosition--;
					//# this.caretRow--;
					//# String prevLine = this.realTextLines[ this.caretRow ];
					//# int carColumn = prevLine.length();
					//# boolean isOnNewlineChar = prevLine.charAt( carColumn - 1 ) == '\n';
					//# if (isOnNewlineChar) {
						//# this.caretPosition--;
						//# carColumn--;
					//# }
					//# setCaretRow(prevLine, carColumn );
					//# //System.out.println(this + ".handleKeyPressed()/font4: caretX=" + this.caretX);
					//# this.caretY -= this.rowHeight;
					//# this.internalY = this.caretY;
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
					//# return true;
				//# }
			//#if polish.i18n.rightToLeft
			//# } else if (gameAction == Canvas.LEFT && keyCode != Canvas.KEY_NUM4) {
			//#else
			//# } else if ( gameAction == Canvas.RIGHT  && keyCode != Canvas.KEY_NUM6) {
			//#endif
				//#ifdef polish.debug.debug
				//# if (this.isPassword) {
					//#debug
					//# System.out.println("originalRowText=" + this.originalRowText );
				//# }
				//#endif
				//# if (characterInserted) {
					//# //System.out.println("right but character inserted");
					//# return true;
				//# }
				//#ifdef polish.css.font-bitmap
					//# if (this.bitMapFontViewer != null) {
						//# // a bitmap-font is used
						//# return false;
					//# }
				//#endif
				//# boolean isOnNewlineChar = this.caretColumn < this.originalRowText.length() 
										//# && this.originalRowText.charAt( this.caretColumn ) == '\n';
				//# if (this.caretColumn < this.originalRowText.length() && !isOnNewlineChar ) {
					//# //System.out.println("right not in last column");
					//# this.caretColumn++;
					//# this.caretPosition++;
					//# setCaretRow( this.originalRowText, this.caretColumn );
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
					//# return true;
				//# } else if (this.caretRow < this.realTextLines.length - 1) {
					//# //System.out.println("right in not the last row");
					//# this.caretRow++;								
					//# if (isOnNewlineChar) {
						//# this.caretPosition++;
					//# }
					//# this.originalRowText = this.realTextLines[ this.caretRow ];
					//# if (characterInserted) {
						//# if (this.isPassword) {
							//# this.caretX = stringWidth("*");
						//# } else {
							//# this.caretX = stringWidth( String.valueOf( character ) );
						//# }
						//# //System.out.println(this + ".handleKeyPressed()/font6: caretX=" + this.caretX);
						//# this.caretColumn = 1;
					//# } else {
						//# setCaretRow( this.originalRowText, 0 );
					//# }
					//# this.caretY += this.rowHeight;
					//# this.internalY = this.caretY;
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
					//# return true;
				//# } else if (characterInserted) {
					//# //System.out.println("right after character insertion");
					//# // a character has been inserted at the last column of the last row:
					//# if (this.isPassword) {
						//# this.caretX += stringWidth("*");
					//# } else {
						//# this.caretX += this.caretWidth;
					//# }
					//# this.caretColumn++;
					//# this.caretPosition++;
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
					//# return true;
				//# }
			//# }
		//#endif
		
		return false;
	}
	
	
			
	//#if !polish.blackberry && tmp.directInput
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Item#handleKeyRepeated(int, int)
	 //# */
	//# protected boolean handleKeyRepeated(int keyCode, int gameAction) {
		//#debug
		//# System.out.println("TextField.handleKeyRepeated( " + keyCode + ")");
		//# if (keyCode >= Canvas.KEY_NUM0 
				//# && keyCode <= Canvas.KEY_NUM9 ) 
		//# {
			//# // ignore repeat events when the current input mode is numbers:
			//# if ( this.isNumeric || this.inputMode == MODE_NUMBERS ) {
				//# if (keyCode == Canvas.KEY_NUM0 && this.inputMode == PHONENUMBER) {
					//# if (this.caretPosition == 0 || "0".equals(this.text)) {
						//# String str = getString();
						//# if (str.length() > 0 && str.charAt(0) == '0') {
							//# str = str.substring(1);
						//# }
						//# setString( "+" + str );
						//# return true;
					//# }
				//# }
				//# return false;
			//# }
			//# int currentLength = (this.text == null ? 0 : this.text.length());
			//# if ( !this.isUneditable && currentLength <= this.maxSize ) 
			//# {	
				//# // enter number character:
				//# this.lastInputTime = System.currentTimeMillis();
				//# char newCharacter = Integer.toString(keyCode - 48).charAt(0);
				//#ifdef polish.css.font-bitmap
					//# if (this.bitMapFont != null) {
						//# this.caretWidth = this.bitMapFont.charWidth( newCharacter );
					//# } else {
				//#endif
						//# this.caretWidth = this.font.charWidth( newCharacter );
				//#ifdef polish.css.font-bitmap
					//# }
				//#endif
				//# if (newCharacter != this.caretChar) {
//# 					
					//#if tmp.usePredictiveInput
						//# if (this.predictiveInput)
						//# {
							//# TextBuilder builder = this.predictiveAccess.getBuilder();
//# 							
							//# try
							//# {
								//# builder.keyClear();
								//# this.predictiveAccess.openChoices(false);
//# 								
								//# builder.addString("" + newCharacter);
							//# }catch(Exception e)
							//# {
								//# System.out.println("unable to clear " + e);
							//# }
//# 							
							//# setText(builder.getText().toString());
							//# setCaretPosition(builder.getCaretPosition());
						//# }
						//# else
					//#endif
					//# {
						//# this.caretChar = newCharacter;
						//# insertCharacter( newCharacter, false, false );
					//# }
				//# }
				//# return true;
			//# }
		//# }
		//#ifdef polish.key.ClearKey:defined
			//#= if (keyCode == ${polish.key.ClearKey}
		//#else
		//# if ( keyCode == -8 
		//#endif
		//#if polish.key.backspace:defined
			//#= || keyCode == ${polish.key.backspace}
		//#endif
				//# && this.caretPosition > 0
				//# && this.text != null
		//# ) 
		//# {
			//# this.deleteKeyRepeatCount++;
		//# }
//# 
		//# return super.handleKeyRepeated(keyCode, gameAction);
	//# }
	//#endif

	//#if !polish.blackberry && tmp.directInput
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Item#handleKeyReleased(int, int)
	 //# */
	//# protected boolean handleKeyReleased( int keyCode, int gameAction ) {
		//# //System.out.println("handleKeyReleased for textfield " + getString() );
		//# this.isKeyDown = false;
		//# this.deleteKeyRepeatCount = 0;
//# 		
		//#if tmp.usePredictiveInput
			//# if (this.predictiveAccess.handleKeyReleased( keyCode, gameAction )) {
				//# return true;
			//# }
		//#endif
//# 
		//# int clearKey =
			//#if polish.key.ClearKey:defined
				//#= ${polish.key.ClearKey};
			//#else
				//# -8;
			//#endif
		//# boolean clearKeyPressed = (keyCode == clearKey);
//# 		 
		//#if polish.key.ChangeNumericalAlphaInputModeKey:defined
			//#= if ((keyCode == KEY_CHANGE_MODE || keyCode == ${polish.key.ChangeNumericalAlphaInputModeKey}) && 
			//#= !this.isNumeric && !this.isUneditable && 
			//#= (!(KEY_CHANGE_MODE == Canvas.KEY_NUM0 && this.inputMode == MODE_NUMBERS) || keyCode == ${polish.key.ChangeNumericalAlphaInputModeKey}) )
		//#else
		//# if ( keyCode == KEY_CHANGE_MODE && !this.isNumeric && !this.isUneditable)
		//#endif
		//# {
			//#if tmp.usePredictiveInput && !polish.key.ChangeNumericalAlphaInputModeKey:defined
			//# if((System.currentTimeMillis() - this.lastTimePressed) > SWITCH_DELAY && TrieProvider.isPredictiveInstalled())
			//# {
				//# this.lastTimePressed = -1;
				//# if(this.predictiveInput)
				//# {
					//# getPredictiveAccess().disablePredictiveInput();
				//# }
				//# else
				//# {
					//# getPredictiveAccess().enablePredictiveInput();
				//# }
//# 				
				//# updateInfo();
				//# return true;
			//# }
			//#endif
			//# this.lastTimePressed = -1;
			//# return handleKeyMode(keyCode, gameAction) || clearKeyPressed;
		//# }
//# 		
//# 		
		//# return this.isKeyPressedHandled || clearKeyPressed || super.handleKeyReleased( keyCode, gameAction );
	//# }
	//#endif
	
	//#if polish.hasPointerEvents && !tmp.forceDirectInput
	//# /**
	 //# * Handles the event when a pointer has been pressed at the specified position.
	 //# * The default method translates the pointer-event into an artificial
	 //# * pressing of the FIRE game-action, which is subsequently handled
	 //# * bu the handleKeyPressed(-1, Canvas.FIRE) method.
	 //# * This method needs should be overwritten only when the "polish.hasPointerEvents"
	 //# * preprocessing symbol is defined: "//#ifdef polish.hasPointerEvents".
	 //# *    
	 //# * @param x the x position of the pointer pressing
	 //# * @param y the y position of the pointer pressing
	 //# * @return true when the pressing of the pointer was actually handled by this item.
	 //# */
	//# protected boolean handlePointerPressed( int x, int y ) {
		//# if (isInItemArea(x, y)) {
			//# return notifyItemPressedStart();
		//# } else {
			//# return false;
		//# }
	//# }
	//#endif
	
	//#if polish.hasPointerEvents && !tmp.forceDirectInput
	//# /**
	 //# * Handles the event when a pointer has been pressed at the specified position.
	 //# * The default method translates the pointer-event into an artificial
	 //# * pressing of the FIRE game-action, which is subsequently handled
	 //# * bu the handleKeyPressed(-1, Canvas.FIRE) method.
	 //# * This method needs should be overwritten only when the "polish.hasPointerEvents"
	 //# * preprocessing symbol is defined: "//#ifdef polish.hasPointerEvents".
	 //# *    
	 //# * @param x the x position of the pointer pressing
	 //# * @param y the y position of the pointer pressing
	 //# * @return true when the pressing of the pointer was actually handled by this item.
	 //# */
	//# protected boolean handlePointerReleased( int x, int y ) {
		//# if (isInItemArea(x, y)) {
			//# notifyItemPressedEnd();
			//# showTextBox();
			//# return true;
		//# } else {
			//# return false;
		//# }
	//# }
	//#endif

	
	//#ifdef tmp.directInput
	//# /**
	 //# * Removes the current character.
	 //# * 
	 //# * @return true when a character could be deleted.
	 //# */
	//# private synchronized boolean deleteCurrentChar() {
		//#debug
		//# System.out.println("deleteCurrentChar: caretColumn=" + this.caretColumn + ", caretPosition=" + this.caretPosition + ", caretChar=" + this.caretChar );
		//# String myText;
		//# if (this.isPassword) {
			//# myText = this.passwordText; 
		//# } else {
			//# myText = this.text;
		//# }
		//# int position = this.caretPosition;
		//# if (this.caretChar != this.editingCaretChar) {
			//# myText = myText.substring( 0, position )
				//# + myText.substring( position + 1);
			//# this.caretChar = this.editingCaretChar;
			//# this.caretPosition = position;
			//# setString( myText );
			//# return true;
		//# }
		//# if (position > 0) {
			//# position--;
			//# myText = myText.substring( 0, position )
				//# + myText.substring( position + 1);
			//# this.caretPosition = position;
			//# setString( myText );
			//#if polish.css.textfield-show-length  && tmp.useInputInfo
				//# if (this.showLength) {
					//# updateInfo();
				//# }
			//#endif
			//# notifyStateChanged();
			//# return true;
		//# } else {
			//# return false;
		//# }
	//# }
	//#endif

	//#if tmp.useNativeTextBox
	/**
	 * Shows the TextBox for entering texts.
	 */
	private void showTextBox() {
		if (this.midpTextBox == null) {
			createTextBox();
		}
		if (StyleSheet.currentScreen != null) {
			this.screen = StyleSheet.currentScreen;
		} else if (this.screen == null) {
			this.screen = getScreen();
		}
		StyleSheet.display.setCurrent( this.midpTextBox );
	}
	//#endif

	//#if tmp.implementsCommandListener
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable box) {
		//#if tmp.usePredictiveInput
			//# if (this.predictiveAccess.commandAction(cmd, box)) {
				//# return;
			//# }
		//#endif
		
		
		//#if tmp.supportsSymbolEntry
			//# if (box instanceof List) {
				//# if (cmd != StyleSheet.CANCEL_CMD) {
					//# int index = symbolsList.getSelectedIndex();
//# 					
					//# insert(definedSymbols[index],this.caretPosition);
//# 					
					//# //insertCharacter( definedSymbols.charAt(index), true, true );
					//# StyleSheet.currentScreen = this.screen;
					//#if tmp.updateDeleteCommand
						//# updateDeleteCommand( this.text );
					//#endif
				//# } else {
					//# StyleSheet.currentScreen = this.screen;
				//# }
				//# StyleSheet.display.setCurrent( this.screen );
				//# notifyStateChanged();
				//# return;
			//# }
		//#endif
						
		//#if tmp.useNativeTextBox
			if (cmd == StyleSheet.CANCEL_CMD) {
				this.midpTextBox.setString( this.text );
			} else if (!this.isUneditable) {
				setString( this.midpTextBox.getString() );
				setCaretPosition( size() );
				notifyStateChanged();
			}
			StyleSheet.display.setCurrent( this.screen );
		//#endif
	}
	//#endif
	
	//#if (!tmp.suppressCommands && !tmp.supportsSymbolEntry) || tmp.supportsSymbolEntry
		public void setItemCommandListener(ItemCommandListener l) {
			this.additionalItemCommandListener = l;
		}
		
		public ItemCommandListener getItemCommandListener()
		{
			return this.additionalItemCommandListener;
		}
	//#endif
	
	//#if tmp.supportsSymbolEntry
	//# public static void initSymbolsList() {
		//# if (symbolsList == null) {
			//#style textFieldSymbolList?, textFieldSymbolTable?
			//# symbolsList = new List( ENTER_SYMBOL_CMD.getLabel(), Choice.IMPLICIT );
			//# for (int i = 0; i < definedSymbols.length; i++) {
				//#style textFieldSymbolItem?
				//# symbolsList.append( definedSymbols[i], null );
			//# }
			//# //TODO check localization when using dynamic localization
			//# symbolsList.addCommand( StyleSheet.CANCEL_CMD );
		//# }			
	//# }
//# 		
	//# private void showSymbolsList() {
		//# if (this.caretChar != this.editingCaretChar) {
			//# commitCurrentCharacter();
		//# }
//# 		
		//# initSymbolsList();
//# 		
		//# symbolsList.setCommandListener( this );
		//# StyleSheet.display.setCurrent( symbolsList );			
	//# }
	//#endif
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.ItemCommandListener#commandAction(javax.microedition.lcdui.Command, de.enough.polish.ui.Item)
	 */
	public void commandAction(Command cmd, Item item) {
		//#debug
		//# System.out.println("TextField.commandAction( " + cmd.getLabel() + ", " + this + " )");
		//#if tmp.usePredictiveInput
			//# if (this.predictiveInput && this.predictiveAccess.commandAction(cmd, item)) {			
				//# return;
			//# }
		//#endif
		
		//#if tmp.implementsItemCommandListener
			//#if tmp.supportsSymbolEntry 
				//# if (cmd == ENTER_SYMBOL_CMD ) {
					//#if !polish.TextField.ignoreSymbolCommand
						//# showSymbolsList();
					//#endif
					//# return;
				//# } 
			//#endif
			//#ifndef tmp.suppressCommands
				if ( cmd == DELETE_CMD ) {
					if (this.text != null && this.text.length() > 0) {
						//#ifdef tmp.directInput
							//#ifdef tmp.allowDirectInput
								//# if (this.enableDirectInput) {
							//#endif
									//#ifdef polish.key.ClearKey:defined
										//#= handleKeyClear(${polish.key.ClearKey},0);
									//#else
										//# handleKeyClear(-8,0);
									//#endif
							//#ifdef tmp.allowDirectInput
								//# } else {
									//# String myText = getString();
									//# setString( myText.substring(0, myText.length() - 1));
									//# notifyStateChanged();
								//# }
							//#endif
						//#else
							String myText = getString();
							setString( myText.substring(0, myText.length() - 1));
							notifyStateChanged();
						//#endif
						return;
					}
				} else if ( cmd == CLEAR_CMD ) {
					setString( null );
					notifyStateChanged();
				} else if ( this.additionalItemCommandListener != null ) {
					this.additionalItemCommandListener.commandAction(cmd, item);
				} else {
					// forward command to the screen's orginal command listener:
					Screen scr = getScreen();
					if (scr != null) {
						CommandListener listener = scr.getCommandListener();
						if (listener != null) {
							listener.commandAction(cmd, scr);
						}
					}
				}
			//#endif		
			//#if polish.key.maybeSupportsAsciiKeyMap
				//# if (cmd == SWITCH_KEYBOARD_CMD) {
					//# useAsciiKeyMap = !useAsciiKeyMap;
				//# }
			//#endif
		//#endif
	}
		
	//#if (tmp.directInput && (polish.TextField.showInputInfo != false)) || polish.blackberry || polish.TextField.activateUneditableWithFire
	//# protected void defocus(Style originalStyle) {
		//# super.defocus(originalStyle);
		//#if polish.blackberry
			//#if polish.Bugs.ItemStateListenerCalledTooEarly
				//# String newText = this.editField.getText();
				//# String oldText = this.isPassword ? this.passwordText : this.text;
//# 				
				//# if (( this.lastFieldChangedEvent != 0) || (!newText.equals(oldText ) && !(oldText == null && newText.length()==0) )) {
					//# this.lastFieldChangedEvent = 0;
					//# setString( newText );
					//# notifyStateChanged();
				//# }
			//#endif
			//# this.editField.focusRemove();
		//#elif polish.TextField.showInputInfo != false && !tmp.includeInputInfo
			//# if (this.screen != null) {
				//# this.screen.setInfo(null);
			//# }
		//#endif
		//#if tmp.directInput
			//# if (this.editingCaretChar != this.caretChar) {
				//# commitCurrentCharacter();
				//# notifyStateChanged();
			//# }
		//#endif
//# 
	//# }
	//#endif
	
	//#if tmp.directInput || !(polish.TextField.suppressDeleteCommand || polish.blackberry) 
	protected Style focus(Style focStyle, int direction) {
		//#if tmp.directInput
			//#ifdef tmp.allowDirectInput
				//# if (this.enableDirectInput) {
			//#endif
					//#if tmp.useInputInfo
						//# updateInfo();
					//#endif
					//#if polish.TextField.jumpToStartOnFocus
						//# if (this.caretPosition != 0) {
							//# setCaretPosition( 0 );
						//# }
					//#elif !polish.TextField.keepCaretPosition
						//# setCaretPosition( getString().length() );
					//#endif
			//#ifdef tmp.allowDirectInput
				//# }
			//#endif
		//#endif
		
		Style unfocusedStyle = super.focus(focStyle, direction);
		//#if tmp.updateDeleteCommand
			updateDeleteCommand( this.text );
		//#endif
			
		return unfocusedStyle;
	}
	//#endif

	//#if polish.blackberry
	//# /**
	 //# * Notifies the TextField about changes in its native BlackBerry component.
	 //# * @param field the native field
	 //# * @param context the context of the change
	 //# */
	//# public void fieldChanged(Field field, int context) {
		//# if (context != FieldChangeListener.PROGRAMMATIC && this.isInitialized ) {
			//#if polish.Bugs.ItemStateListenerCalledTooEarly
				//# long currentTime = System.currentTimeMillis();
				//# this.lastFieldChangedEvent = currentTime;
				//# Screen scr = getScreen();
				//# if (scr != null) {
					//# scr.lastInteractionTime = currentTime;
				//# }
			//#else
				//# setString( this.editField.getText() );
				//# notifyStateChanged();
			//#endif
		//# }
	//# }
	//#endif

	/**
	 * Sets the input mode for this TextField.
	 * Is ignored when no direct input mode is used.
	 * 
	 * @param inputMode the input mode
	 */
	public void setInputMode(int inputMode) {
		this.inputMode = inputMode;
		//#if tmp.directInput
			//#if tmp.useInputInfo
				//# if (this.isFocused) {
					//# updateInfo();
				//# }
			//#endif
			//# if (this.caretChar != this.editingCaretChar) {
				//# commitCurrentCharacter();
			//# }
			//# if (inputMode == MODE_FIRST_UPPERCASE) {
				//# this.nextCharUppercase = true;
			//# } else {
				//# this.nextCharUppercase = false;
			//# }
		//#endif
	}
	
	/**
	 * Returns the current input mode.
	 * 
	 * @return the current input mode
	 */
	public int getInputMode() {
		return this.inputMode;
	}

	/**
	 * (De)activates the input info element for this TextField.
	 * Note that the input info cannot be shown when it is deactivated by setting the "polish.TextField.includeInputInfo"
	 * preprocessing variable to "false".
	 * 
	 * @param show true when the input info should be shown
	 */
	public void setShowInputInfo(boolean show) {
		this.isShowInputInfo = show;
		
	}

	protected void showNotify() {
		//#if tmp.updateDeleteCommand
			updateDeleteCommand(this.text);
		//#endif
		
		//#if polish.TextField.useExternalInfo && !polish.blackberry
			//# if(this.isFocused && this.infoItem != null)
			//# {
				//# updateInfo();
			//# }
		//#endif
		super.showNotify();
	}

	//#if  !polish.blackberry && tmp.directInput
		//# /* (non-Javadoc)
		 //# * @see de.enough.polish.ui.StringItem#hideNotify()
		 //# */
		//# protected void hideNotify() {
			//# if (this.caretChar != this.editingCaretChar) {
				//# commitCurrentCharacter();
			//# }
			//# super.hideNotify();
		//# }	
	//#endif
		
	//#if tmp.usePredictiveInput
	//# public PredictiveAccess getPredictiveAccess() {
		//# return this.predictiveAccess;
	//# }
//# 
	//# public void setPredictiveAccess(PredictiveAccess predictive) {
		//# this.predictiveAccess = predictive;
	//# }
	//#endif

	/**
	 * Checks if commands are currently suppressed by this TextField.
	 * @return true when commands are suppressed
	 */
	public boolean isSuppressCommands() {
		return this.suppressCommands;
	}

	/**
	 * Toggles the surpressing of commands for this TextField
	 * This has no effect when commands are globally suppressed by settting
	 * the preprocessing variable "polish.TextField.suppressCommands" to "true".
	 * 
	 * @param suppressCommands true when commands should be suppressed
	 */
	public void setSuppressCommands(boolean suppressCommands) {
		this.suppressCommands = suppressCommands;
		setConstraints( this.constraints );
	}

	//#if polish.TextField.showHelpText
	//# public void setHelpText(String text)
	//# {
		//# this.helpItem.setText(text);
	//# }
	//#endif
	
	//#if polish.TextField.showHelpText
	//# public void setHelpStyle(Style style)
	//# {
		//# this.helpItem.setStyle(style);
	//# }
	//#endif

	//#if polish.TextField.useExternalInfo && !polish.blackberry
	//# public StringItem getInfoItem() {
		//# return this.infoItem;
	//# }
//# 
	//# public void setInfoItem(StringItem infoItem) {
		//# this.infoItem = infoItem;
	//# }
	//#endif

	//#if !polish.blackberry && tmp.supportsSymbolEntry
	//# /**
	 //# * Returns the defined symbols as a String array
	 //# * @return the string array 
	 //# */
	//# public static String[] getDefinedSymbols() {
		//# return definedSymbols;
	//# }
	//#endif
	
	/*
	public boolean keyChar(char key, int status, int time) {
		Screen scr = getScreen();
		if (!this.isFocused || scr == null || !scr.isShown()) {
			return false;
		}
		int currentLength = (this.text == null ? 0 : this.text.length());
		if (currentLength < this.maxSize) { 
			this.caretChar = key;
			insertCharacter();
			return true;
		} else {
			return false;
		}
	}
	*/
	
//#ifdef polish.TextField.additionalMethods:defined
	//#include ${polish.TextField.additionalMethods}
//#endif

}
