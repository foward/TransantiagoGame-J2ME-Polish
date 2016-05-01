//#condition polish.usePolishGui

/*
 * Created on 27-May-2005 at 17:14:01.
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
package de.enough.polish.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * <p>Paints a transition of two screens for a nice effect.</p>
 * <p>Using a screen change animation is easy:
 *    <br />
 *    Use the <code>screen-change-animation</code> CSS attribute for specifying which
 *    animation you would like to have. You can also finetune some animations. Note
 *    that some animations have certain conditions like support of the MIDP 2.0 profile.
 * <pre>
 * screen-change-animation: left;
 * left-screen-change-animation-speed: 5;
 * </pre>
 * </p>
 * <p>You can easily implement your own screen change animations by following these
 *    steps:
 * </p>
 * <ol>
 * 	<li>Extend de.enough.polish.ui.ScreenChangeAnimation</li>
 *  <li>Implement the animate() method for doing the animation, use the fields lastCanvasImage 
 *      and nextCanvasImage for your manipulation and consider the isForwardAnimation field.</li>
 *  <li>Implement the paintAnimation(Graphics) method.</li>
 *  <li>Override the show() method if you need to get parameters from the style.</li>
 *  <li>In case you want to manipulate the RGB data, you should set the useNextCanvasRgb and/or useLastCanvasRgb fields
 *  to true - you can then access the nextCanvasRgb and lastCanvasRgb fields for manupulating the data. 
 *  </li>
 * </ol>
 * <p>You can now use your animation by specifying the <code>screen-change-animation</code> CSS attribute 
 *    accordingly:
 * <pre>
 * screen-change-animation: new com.company.ui.MyScreenChangeAnimation();
 * </pre>
 * </p>
 * <p>You can also ease the usage by registering your animation in ${polish.home}/custom-css-attributes:
 * <pre>
 * &lt;attribute name=&quot;screen-change-animation&quot;&gt;
 * 		&lt;mapping from=&quot;myanimation&quot; to=&quot;com.company.ui.MyScreenChangeAnimation()&quot; /&gt;
 * &lt;/attribute&gt;
 * </pre>
 * </p>
 * <p>Now your animation is easier to use:
 * <pre>
 * screen-change-animation: myanimation;
 * </pre>
 * </p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2008</p>
 * <pre>
 * history
 *        27-May-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 * @see #show(Style, Display, int, int, Image, Image, AccessibleCanvas, Displayable, boolean)
 * @see #animate()
 */
public abstract class ScreenChangeAnimation
//#if polish.midp2
	extends Canvas
//#elif polish.classes.fullscreen:defined
	//#= extends ${polish.classes.fullscreen}
//#else
	//#= extends Canvas 
//#endif
implements Runnable
//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
	, AccessibleCanvas
//#endif
{
	protected Display display;
	protected AccessibleCanvas nextCanvas;
	protected Image lastCanvasImage;
	protected int[] lastCanvasRgb;
	/** set to true in subclasses for populating lastCanvasRgb */
	protected boolean useLastCanvasRgb;
	protected Image nextCanvasImage;
	protected int[] nextCanvasRgb;
	/** set to true in subclasses for populating nextCanvasRgb */
	protected boolean useNextCanvasRgb;
	protected int screenWidth;
	protected int screenHeight;
	//#if polish.Bugs.fullScreenInPaint
		//# protected boolean fullScreenModeSet;
	//#endif
	protected Displayable nextDisplayable;
	protected boolean isForwardAnimation;
	

	/**
	 * Creates a new ScreenChangeAnimation.
	 * All subclasses need to implement the default constructor.
	 */
	public ScreenChangeAnimation() {
		// default constructor
		//#if polish.midp2 && !polish.Bugs.fullScreenInPaint
			setFullScreenMode(true);
		//#endif
	}
	
	/**
	 * Starts the animation.
	 * Please note that an animation can be re-used for several screens.
	 * 
	 * @param style the associated style.
	 * @param dsplay the display, which is used for setting this animation
	 * @param width the screen's width
	 * @param height the screen's height
	 * @param lstScreenImage an image of the last screen
	 * @param nxtScreenImage an image of the next screen
	 * @param nxtCanvas the next screen that should be displayed when this animation finishes (as an AccessibleCanvas)
	 * @param nxtDisplayable the next screen that should be displayed when this animation finishes (as a Displayable)
	 * @param isForward true when the animation should run in the normal direction/mode - false if it should run backwards
	 */
	protected void show( Style style, Display dsplay, final int width, final int height, Image lstScreenImage, Image nxtScreenImage, AccessibleCanvas nxtCanvas, Displayable nxtDisplayable, boolean isForward ) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.display = dsplay;
		this.nextCanvas = nxtCanvas;
		this.nextDisplayable = nxtDisplayable;
		this.lastCanvasImage = lstScreenImage;
		if (this.useLastCanvasRgb) {
			this.lastCanvasRgb = new int[ width * height ];
			//#if polish.midp2
				lstScreenImage.getRGB(this.lastCanvasRgb, 0, width, 0, 0, width, height );
			//#endif
		}
		this.nextCanvasImage = nxtScreenImage;
		if (this.useNextCanvasRgb) {
			this.nextCanvasRgb = new int[ width * height ];
			//#if polish.midp2
				nxtScreenImage.getRGB(this.nextCanvasRgb, 0, width, 0, 0, width, height );
			//#endif
		}
		this.isForwardAnimation = isForward;
		setStyle( style );
		//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
			MasterCanvas.setCurrent( dsplay, this );
		//#else
			//# dsplay.setCurrent( this );
		//#endif
	}
	
	/**
	 * Sets the style for this animation.
	 * Subclasses can override this for adapting to different design settings.
	 * 
	 * @param style the style
	 */
	protected void setStyle(Style style)
	{
		// let subclasses override this
	}

	/**
	 * Animates this animation.
	 * 
	 * @return true when the animation should continue, when false is returned the animation
	 *         will be stopped and the next screen will be shown instead.
	 */
	protected abstract boolean animate();
	
	/**
	 * Paints the animation.
	 * 
	 * @param g the graphics context
	 */
	protected abstract void paintAnimation( Graphics g );
	
	public final void paint( Graphics g ) {
		//#if polish.Bugs.fullScreenInPaint
			//# if (! this.fullScreenModeSet ) {
				//# setFullScreenMode(true);
				//# this.fullScreenModeSet = true;
			//# }
		//#endif
		try {			
			if (this.nextCanvasImage != null) {
				paintAnimation( g );
				this.display.callSerially( this );
			}	
		} catch (Exception e) {
			//#debug error
			//# System.out.println("Unable to paint animation" + e );
		}
	}

	
	
	//#if polish.hasPointerEvents
	//# /**
	 //# * Forwards pointer pressed events to the next screen.
	 //# * 
	 //# * @param x the horizontal coordinate of the clicked pixel
	 //# * @param y the vertical coordinate of the clicked pixel
	 //# * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 //# */
	//# public void pointerPressed( int x, int y ) {
		//# AccessibleCanvas next = this.nextCanvas;
		//# Image nextImage = this.nextCanvasImage;
		//# if (next != null) {
			//# next.pointerPressed( x, y );
			//# updateNextScreen(next, nextImage, this.nextCanvasRgb);
		//# }
	//# }
	//#endif
	
	//#if polish.hasPointerEvents
	//# /**
	 //# * Forwards pointer pressed events to the next screen.
	 //# * 
	 //# * @param x the horizontal coordinate of the clicked pixel
	 //# * @param y the vertical coordinate of the clicked pixel
	 //# * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 //# */
	//# public void pointerReleased( int x, int y ) {
		//# AccessibleCanvas next = this.nextCanvas;
		//# Image nextImage = this.nextCanvasImage;
		//# if (next != null) {
			//# next.pointerReleased( x, y );
			//# updateNextScreen(next, nextImage, this.nextCanvasRgb);
		//# }
	//# }
	//#endif
	
	//#if polish.hasPointerEvents
	//# /**
	 //# * Forwards pointer dragged events to the next screen.
	 //# * 
	 //# * @param x the horizontal coordinate of the clicked pixel
	 //# * @param y the vertical coordinate of the clicked pixel
	 //# * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 //# */
	//# public void pointerDragged( int x, int y ) {
		//# AccessibleCanvas next = this.nextCanvas;
		//# Image nextImage = this.nextCanvasImage;
		//# if (next != null) {
			//# next.pointerDragged( x, y );
			//# updateNextScreen(next, nextImage, this.nextCanvasRgb);
		//# }
	//# }
	//#endif
	
	
	/**
	 * Notifies this animation that it will be shown shortly.
	 * The default implementation switches into fullscreen mode
	 */
	public void showNotify() {
		//#if polish.midp2 && !polish.Bugs.fullScreenInPaint
			setFullScreenMode(true);
		//#endif
	}
	
	/**
	 * Notifies this animation that it will be hidden shortly.
	 * This is ignored by the default implementation.
	 */
	public void hideNotify() {
		// ignore
	}
	
	//#if polish.midp2 && !polish.Bugs.needsNokiaUiForSystemAlerts 
	/**
	 * Notifies this animation that the screen space has been changed.
	 * This is ignored by the default implementation.
   * 
   * @param width the width
   * @param height the height
	 */
	public void sizeChanged( int width, int height ) {
		// ignore
	}
	//#endif

	/**
	 * Handles key repeat events.
	 * The implementation forwards this event to the next screen and then updates the nextCanvasImage field.
	 * 
	 * @param keyCode the code of the key
	 * @see #nextCanvasImage
	 * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 */
	public void keyRepeated( int keyCode ) {
		AccessibleCanvas next = this.nextCanvas;
		Image nextImage = this.nextCanvasImage;
		try {
			if (next != null) {
				next.keyRepeated( keyCode );
				updateNextScreen( next, nextImage, this.nextCanvasRgb );
			}
		} catch (Exception e) {
			//#debug error
			//# System.out.println("Error while handling keyRepeated event" + e );
		}
	}

	/**
	 * Handles key released events.
	 * The implementation forwards this event to the next screen and then updates the nextCanvasImage field.
	 * 
	 * @param keyCode the code of the key
	 * @see #nextCanvasImage
	 * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 */
	public void keyReleased( int keyCode ) {
		AccessibleCanvas next = this.nextCanvas;
		Image nextImage = this.nextCanvasImage;
		try {
			if (next != null) {
				next.keyReleased( keyCode );
				updateNextScreen( next, nextImage, this.nextCanvasRgb );
			}
		} catch (Exception e) {
			//#debug error
			//# System.out.println("Error while handling keyReleased event" + e );
		}
	}

	/**
	 * Handles key pressed events.
	 * The implementation forwards this event to the next screen and then updates the nextCanvasImage field.
	 * 
	 * @param keyCode the code of the key
	 * @see #nextCanvasImage
	 * @see #updateNextScreen(AccessibleCanvas, Image, int[])
	 */
	public void keyPressed( int keyCode ) {
		AccessibleCanvas next = this.nextCanvas;
		Image nextImage = this.nextCanvasImage;
		try {
			if (next != null) {
				next.keyPressed( keyCode );
				updateNextScreen( next, nextImage, this.nextCanvasRgb );
			}
		} catch (Exception e) {
			//#debug error
			//# System.out.println("Error while handling keyPressed event" + e );
		}
	}
	
	
	/**
	 * Updates the image and possibly the RGB data of the next screen.
	 * 
	 * @param next the next screen
	 * @param nextImage the image to which the screen should be painted
	 * @param rgb the RGB data, can be null
	 */
	protected void updateNextScreen( AccessibleCanvas next, Image nextImage, int[] rgb ) {
		Graphics g = nextImage.getGraphics();
		next.paint( g );
		//#if polish.midp2
			if (rgb != null) {
				nextImage.getRGB(rgb, 0, this.screenWidth, 0, 0, this.screenWidth, this.screenHeight );
			}
		//#endif
	}
	
	/**
	 * Runs this animation - subclasses need to ensure to call this.display.callSerially( this ); at the end of the paint method.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			if (this.nextCanvas != null && animate()) {
				//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
					MasterCanvas.instance.repaint();
				//#else
					//# repaint();
				//#endif
			} else {
				//#debug
				//# System.out.println("ScreenChangeAnimation: setting next screen");
				this.lastCanvasImage = null;
				this.lastCanvasRgb = null;
				this.nextCanvasImage = null;
				this.nextCanvasRgb = null;
				this.nextCanvas = null;
				Display disp = this.display;
				this.display = null;
				Displayable next = this.nextDisplayable;
				this.nextDisplayable = null;
				System.gc();
				if (disp != null) {
					// checking out if the animation is still shown is sweet in theory but it fails when there are security dialogs and the like in the way..
					//Displayable current = disp.getCurrent();
					//if (current == this && next != null) {
					if (next != null) {
						//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
							MasterCanvas.setCurrent( disp, next );
						//#else
							//# disp.setCurrent( next );
						//#endif
					}
				}
			}
		} catch (Exception e) {
			//#debug error
			//# System.out.println("Unable to animate" + e);
			Display disp = this.display;
			Displayable next = this.nextDisplayable;
			if (disp != null && next != null) {
				//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
					MasterCanvas.setCurrent( disp, next );
				//#else
					//# disp.setCurrent( next );
				//#endif
			}
		}
	}

}
