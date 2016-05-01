//#condition polish.usePolishGui

/*
 * Created on 03-Jun-2005 at 18:18:19.
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

//import de.enough.polish.ui.Alert; import de.enough.polish.ui.StyleSheet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

//#if polish.api.sensor && polish.Screen.AutomaticOrientationChange
	//#define tmp.automaticScreenOrientationChange
	//# // import de.enough.polish.util.sensor.AccelationInfoItem;
	//# import de.enough.polish.util.sensor.AccelerationListener;
//# import de.enough.polish.util.sensor.AccelerationUtil;
//#endif

/**
 * <p>Is used for only displaying a single canvas for devices that flicker between screen changes.</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2008</p>
 * <pre>
 * history
 *        03-Jun-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class MasterCanvas
//#if polish.useFullScreen && (polish.midp2 && !polish.Bugs.needsNokiaUiForSystemAlerts)  && (!polish.useMenuFullScreen || polish.hasCommandKeyEvents)
	//#define tmp.fullScreen
	extends Canvas
//#elif polish.useFullScreen && polish.classes.fullscreen:defined
	//#define tmp.fullScreen
	//#= extends ${polish.classes.fullscreen}
//#else
	//# extends Canvas
//#endif
{
	/** the master canvas that actually displays the canvas that should be shown */
	public static MasterCanvas instance;
	protected AccessibleCanvas currentCanvas;
	protected Displayable currentDisplayable;
	//#if tmp.fullScreen && polish.midp2 && polish.Bugs.fullScreenInPaint
		//#define tmp.fullScreenInPaint
		//# private boolean isInFullScreenMode;
	//#endif
	//#if tmp.automaticScreenOrientationChange
		//# private int screenOrientation;
	//#endif
	private int screenWidth;
	private int screenHeight;
	//#if polish.Bugs.noSoftKeyReleasedEvents
		//# private boolean isIgnoreReleasedEvent;
	//#endif
	
	private MasterCanvas() {
		// disallow instantiation...
		//#if tmp.automaticScreenOrientationChange
			//# ScreenOrientationDetector detector = new ScreenOrientationDetector();
			//# AccelerationUtil.addAccelerationListener(detector);
//# //			//#style screenInfo
//# //			AccelationInfoItem item = new AccelationInfoItem();
//# //			ScreenInfo.setItem( item );
		//#endif
			
		//#if tmp.fullScreen && polish.midp2 && !polish.Bugs.fullScreenInShowNotify
			super.setFullScreenMode(true);
		//#endif

	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#hideNotify()
	 */
	protected void hideNotify() {
		if (this.currentCanvas != null) { 
			this.currentCanvas.hideNotify();
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#showNotify()
	 */
	protected void showNotify() {
		//#if polish.midp2 && tmp.fullScreen
			//#if polish.ScreenOrientationCanChangeManually
				//# if (this.currentCanvas instanceof Screen) {
					//# ((Screen)this.currentCanvas).isSetFullScreenCalled = true;
				//# }
			//#endif
			setFullScreenMode( true );
			//#if polish.ScreenOrientationCanChangeManually
				//# if (this.currentCanvas instanceof Screen) {
					//# ((Screen)this.currentCanvas).isSetFullScreenCalled = false;
				//# }
			//#endif
		//#endif
		if (this.currentCanvas != null) { 
			this.currentCanvas.showNotify();
			if (this.screenHeight != 0) {
				this.currentCanvas.sizeChanged( this.screenWidth, this.screenHeight );
			}
			if (StyleSheet.currentScreen != this.currentCanvas && this.currentCanvas instanceof Screen) {
				// this can happen in some circumstances on Nokia Series 60 Second Edition phones:
				StyleSheet.currentScreen = (Screen) this.currentCanvas;
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#keyPressed(int)
	 */
	protected void keyPressed(int keyCode) {
		//#if polish.Bugs.noSoftKeyReleasedEvents
			//# this.isIgnoreReleasedEvent = false;
		//#endif
		if (this.currentCanvas != null) { 
			this.currentCanvas.keyPressed( keyCode );
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#keyRepeated(int)
	 */
	protected void keyRepeated(int keyCode) {
		if (this.currentCanvas != null) { 
			//#if polish.Bugs.noSoftKeyReleasedEvents
				//# if (this.isIgnoreReleasedEvent) {
					//# // also ignore repeated events:
					//# return;
				//# }
			//#endif
			this.currentCanvas.keyRepeated( keyCode );
		}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#keyReleased(int)
	 */
	protected void keyReleased(int keyCode) {
		//#if polish.Bugs.noSoftKeyReleasedEvents
			//# if (this.isIgnoreReleasedEvent) {
				//# this.isIgnoreReleasedEvent = false;
				//# return;
			//# }
		//#endif
		this.currentCanvas.keyReleased( keyCode );
	}
	

	//#if polish.hasPointerEvents
	//# /* (non-Javadoc)
	 //# * @see javax.microedition.lcdui.Canvas#pointerPressed(int,int)
	 //# */
	//# protected void pointerPressed(int x, int y) {
		//# if (this.currentCanvas != null) { 
			//# this.currentCanvas.pointerPressed( x, y );
		//# }
	//# }
	//#endif
	
	//#if polish.hasPointerEvents
	//# /* (non-Javadoc)
	 //# * @see javax.microedition.lcdui.Canvas#pointerPressed(int,int)
	 //# */
	//# protected void pointerReleased(int x, int y) {
		//# if (this.currentCanvas != null) { 
			//# this.currentCanvas.pointerReleased( x, y );
		//# }
	//# }
	//#endif

	//#if polish.hasPointerEvents
	//# /* (non-Javadoc)
	 //# * @see javax.microedition.lcdui.Canvas#pointerPressed(int,int)
	 //# */
	//# protected void pointerDragged(int x, int y) {
		//# if (this.currentCanvas != null) {
			//# this.currentCanvas.pointerDragged(x, y);
		//# }
	//# }
	//#endif

	protected void sizeChanged(int width, int height) {
		this.screenHeight = height;
		this.screenWidth = width;
		//#if polish.midp2 && !polish.Bugs.needsNokiaUiForSystemAlerts
		if (this.currentCanvas != null) { 
			this.currentCanvas.sizeChanged( width, height );
		}
		//#endif
	}
	
	public static int getScreenHeight() {
		int h = 0;
		if (instance != null) {
			h = instance.screenHeight;
			if (h == 0) {
				h = instance.getHeight();
			}
		}
		return h;
	}
	
	public static int getScreenWidth() {
		int w = 0;
		if (instance != null) {
			w = instance.screenWidth;
			if (w == 0) {
				w = instance.getWidth();
			}
		}
		return w;
	}
		
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
	 */
	protected void paint(Graphics g) {
		//#if tmp.fullScreenInPaint
			//# if (!this.isInFullScreenMode) {
				//# setFullScreenMode(true);
				//# this.isInFullScreenMode = true;
			//# }
		//#endif
		if (this.currentCanvas != null) {	
			this.currentCanvas.paint( g );
		}
	}
	
	/**
	 * Sets the current screen/displayable.
	 * 
	 * @param display the display
	 * @param nextDisplayable the screen/displayable that should be shown
	 */
	public static void setCurrent( Display display, Displayable nextDisplayable ) {
		//#debug
		//# System.out.println("MasterCanvas: setCurrent " + nextDisplayable  + ", on display " + display );
		if (nextDisplayable == null) {
			display.setCurrent( null );
			return;
		}
		if (instance != null && instance.currentCanvas == nextDisplayable) {
			instance.repaint();
			return;
		}
		//if ( nextDisplayable == instance ) {
		//	display.setCurrent( nextDisplayable );
		//	return;
		//}
		if ( ! (nextDisplayable instanceof AccessibleCanvas) ) {
			if (instance != null && instance.currentCanvas != null) {
				if (instance.currentDisplayable != nextDisplayable ) {
					//#debug
					//# System.out.println("MasterCanvas: setting instance.currentCanvas to NULL!!!");
					instance.currentCanvas.hideNotify();
					instance.currentCanvas = null;
					instance.currentDisplayable = null;
				}
			}
			//#debug
			//# System.out.println("MasterCanvas: setting native next displayable " + nextDisplayable );
			display.setCurrent( nextDisplayable );
			return;
		}
		AccessibleCanvas oldCanvas = null;
		if ( instance == null ) {
			instance = new MasterCanvas();
		} else {
			oldCanvas = instance.currentCanvas;
		}
					
		AccessibleCanvas canvas = ( (AccessibleCanvas) nextDisplayable );
		//#if polish.usePolishGui
			if (nextDisplayable instanceof Alert && instance.currentDisplayable != nextDisplayable) {
				Alert alert = (Alert)nextDisplayable;
				if (alert.nextDisplayable == null) {
					alert.nextDisplayable = (instance.currentDisplayable != null ? instance.currentDisplayable : display.getCurrent() );
				}
			}
		//#endif
		//#if tmp.automaticScreenOrientationChange
			//# if (canvas instanceof Screen) {
				//# ((Screen)canvas).setScreenOrientation( instance.screenOrientation );
			//# }
		//#endif
		if (instance.isShown()) {
			canvas.showNotify();
			if (instance.screenHeight != 0) {
				canvas.sizeChanged( instance.screenWidth, instance.screenHeight );
			}
		}
		instance.currentCanvas = canvas;
		instance.currentDisplayable = nextDisplayable;
//		if (nextDisplayable instanceof Alert) {
//			System.out.println("MasterCanvas: setting Alert " + nextDisplayable + " as next screen!");
//		}
		
		if ( oldCanvas != null ) {
			oldCanvas.hideNotify();
		}

		//#if polish.css.repaint-previous-screen
			//# // de-register an old remaining J2ME Polish screen for situations, in
			//# // which an AccessibleCanvas which is not a Screen is shown next.
			//# // Otherwise the AnimationThread will continue to animate the old
			//# // screen.
			//# if (StyleSheet.currentScreen != nextDisplayable &&  (!(nextDisplayable instanceof Screen))  ) {
				//# StyleSheet.currentScreen = null;
			//# }
		//#endif
		//#if polish.Bugs.noSoftKeyReleasedEvents
			//# instance.isIgnoreReleasedEvent = true;
		//#endif
		if ( !instance.isShown() ) {
			display.setCurrent( instance );
		} else {
			instance.repaint();
		}
	}
	
	// not needed anymore, since display.setCurrent( Alert, Displayable ) is now mapped
	// during the preprocessing phase.
//	public static void setCurrent( Display display, Alert alert, Displayable nextDisplayable ) {
//		//#debug
//		System.out.println("MasterCanvas: setCurrent of " + nextDisplayable.getClass().getName() );
//		//if ( nextDisplayable == instance ) {
//		//	display.setCurrent( nextDisplayable );
//		//	return;
//		//}
//		alert.setNextDisplayable(display, nextDisplayable);
//		setCurrent( display, alert );
////		if ( ! (nextDisplayable instanceof AccessibleCanvas) ) {
////			if (instance != null && instance.currentCanvas != null) {
////				if (instance.currentDisplayable != nextDisplayable ) {
////					//#debug
////					System.out.println("MasterCanvas: setting instance.currentCanvas to NULL!!!");
////					instance.currentCanvas.hideNotify();
////					instance.currentCanvas = null;
////					instance.currentDisplayable = null;
////				}
////			}
////			alert.setNextDisplayable(display, nextDisplayable);
////			display.setCurrent( alert );
////			display.setCurrent( alert, nextDisplayable );
////			return;
////		}
////		if ( instance == null ) {
////			instance = new MasterCanvas();
////		} else if ( instance.currentCanvas != null ) {
////			instance.currentCanvas.hideNotify();
////		}
////		AccessibleCanvas canvas = ( (AccessibleCanvas) nextDisplayable );
////		instance.currentCanvas = canvas;
////		instance.currentDisplayable = nextDisplayable;
////		canvas.showNotify();
////		display.setCurrent( alert, instance );
//	}
	
	/**
	 * Retrieves the currently shown displayable.
	 * @param display the display that is being used to change displayables
	 * @return the currently shown displayable.
	 */
	public static Displayable getCurrent( Display display ) {
		//#debug
		//# System.out.println("MasterCanvas: getCurrent");
		if (instance != null) {
			return instance.currentDisplayable;
		} else {
			return display.getCurrent();
		}
	}
	
	/**
	 * Repaints an accessible canvas.
	 * @param canvas the canvas that requires a repaint
	 */
	public static void repaintAccessibleCanvas( AccessibleCanvas canvas ) {
		if (canvas == null) {
			//#debug warn
			//# System.out.println("MasterCanvas: repaintAccessibleCanvas got [null] canvas." );
			return;
		}
		//#debug
		//# System.out.println("MasterCanvas: repaintAccessibleCanvas");
		if ( instance != null ) {
			instance.repaint();
		} else {
			((Canvas) canvas).repaint(); 
		}
	}

	/**
	 * Repaints a normal canvas
	 * @param canvas the canvas that requires a repaint
	 */
	public static void repaintCanvas( Canvas canvas ) {
		if (canvas == null) {
			//#debug warn
			//# System.out.println("MasterCanvas: repaintCanvas got [null] canvas." );
			return;
		}
		//#debug
		//# System.out.println("MasterCanvas: repaintCanvas " + canvas + ", MasterCanvas.instance != null: " + ( instance != null) + ", instance == canvas: " +  ( instance == null) );
		if ( !(canvas instanceof AccessibleCanvas) ) {
			canvas.repaint();
		} else if ( instance != null ) {
			instance.repaint();
		} else {
			//System.out.println("native canvas repaint");
			canvas.repaint();
		}
	}
	/**
	 * Repaints the specified area of a normal canvas
	 * @param canvas the canvas that should be repainted partically
	 * @param x horizontal start
	 * @param y vertical start
	 * @param width width
	 * @param height height of the the repaint area in pixels
	 */
	public static void repaintCanvas( Canvas canvas, int x, int y, int width, int height ) {
		if (canvas == null) {
			//#debug warn
			//# System.out.println("MasterCanvas: repaintCanvas got [null] canvas." );
			return;
		}
		//#debug
		//# System.out.println("MasterCanvas: repaintCanvas " + canvas + ", MasterCanvas.instance != null: " + ( instance != null) + ", instance == canvas: " +  ( instance == null) );
		if ( !(canvas instanceof AccessibleCanvas) ) {
			canvas.repaint( x, y, width, height );
		} else if ( instance != null ) {
			instance.repaint( x, y, width, height );
		} else {
			//System.out.println("native canvas repaint");
			canvas.repaint( x, y, width, height );
		}
	}
	
	/**
	 * Determines if the specified canvas is currently being shown
	 * @param canvas the canvas
	 * @return true when the canvas is currently the first display on the screen
	 */
	public static boolean isAccessibleCanvasShown(AccessibleCanvas canvas) {
		//#debug
		//# System.out.println("MasterCanvas: isAccessibleCanvasShown");
		if ( instance != null ) {
			return (canvas == instance.currentCanvas);
		} else {
			return ((Canvas)canvas).isShown();
		}
		
	}

	
	/**
	 * Determines if the specified canvas is currently being shown
	 * @param canvas the canvas
	 * @return true when the canvas is currently the first display on the screen
	 */
	public static boolean isCanvasShown(Canvas canvas) {
		//#debug
		//# System.out.println("MasterCanvas: isCanvasShown");
		if ( instance != null && instance.isShown() ) {
			return (canvas == instance.currentDisplayable);
		} else {
			return canvas.isShown();
		}
	}

	/**
	 * Determines if the specified displayable is currently being shown
	 * @param displayable the displayayable
	 * @return true when the canvas is currently the first display on the screen
	 */
	public static boolean isDisplayableShown(Displayable displayable) {
		//#debug
		//# System.out.println("MasterCanvas: isDisplayableShown");
		if ( instance != null && instance.isShown() ) {
			return (displayable == instance.currentDisplayable);
		} else {
			return displayable.isShown();
		}
	}
	
	//#if tmp.automaticScreenOrientationChange
	//# class ScreenOrientationDetector implements AccelerationListener {
//# 		
		//# /* (non-Javadoc)
		 //# * @see de.enough.polish.util.sensor.AccelerationListener#notifyAcceleration(int, int, int, int, int, int, int, int, int)
		 //# */
		//# public void notifyAcceleration(int x, int minimumX, int maximumX,
				//# int y, int minimumY, int maximumY, int z, int minimumZ,
				//# int maximumZ)
		//# {
			//# if (MasterCanvas.this.currentCanvas instanceof Screen) {
				//# if (x > 800 && y < 250) {
					//# instance.screenOrientation = 90;
					//# ((Screen)MasterCanvas.this.currentCanvas).setScreenOrientation( 90 ); 
				//# } else if (x < 200 && y > 750) {
					//# instance.screenOrientation = 0;
					//# ((Screen)MasterCanvas.this.currentCanvas).setScreenOrientation( 0 ); 
				//# } else if (x < 200 && y < -800) {
					//# instance.screenOrientation = 180;
					//# ((Screen)MasterCanvas.this.currentCanvas).setScreenOrientation( 180 );
				//# } else if (x < -800 && y < 250) {
					//# instance.screenOrientation = 270;
					//# ((Screen)MasterCanvas.this.currentCanvas).setScreenOrientation( 270 );
				//# }
			//# }
		//# }		
	//# }
	//#endif

}
