//#condition polish.usePolishGui && polish.api.mmapi



/*

 * Created on Sep 8, 2006 at 5:00:10 PM.

 *

 * Copyright (c) 2006 Robert Virkus / Enough Software

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

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;



import de.enough.polish.util.Arrays;
import de.enough.polish.util.TextUtil;



import de.enough.polish.ui.Screen;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.MasterCanvas;
import java.util.Date;


/**
 * <p>A convenience screen for taking snapshots. This screen requires support of the MMAPI by the current target device!</p>
 * <pre>
 * //#if polish.api.mmapi
 *    import de.enough.polish.ui.SnapshotScreen;
 * //#endif
 * ...
 * //#if polish.api.mmapi
 *    //#style snapshotScreen
 *    SnapshotScreen screen = new SnapshotScreen("Snapshot");
 * //#endif
 * </pre>
 *
 * <p>Copyright Enough Software 2006 - 2008</p>
 * <pre>
 * history
 *        Sep 8, 2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class SnapshotScreen extends Screen implements Runnable {

	private Player player;
	private VideoControl videoControl;
	private boolean isHiding;
	private boolean takeSnapshot;
	private boolean wait;
	private String snapshotEncoding;
	private byte[] snapshotData;
	private MediaException error;


	/**
	 * Creates a new screen for taking screenshots.
	 *
	 * @param title the title of the screen
	 */
	public SnapshotScreen(String title) {
		this(title, null);
	}


	/**
	 * Creates a new screen for taking screenshots.
	 *
	 * @param title the title of the screen
	 * @param style the style
	 */
	public SnapshotScreen(String title, Style style ) {
		super(title, false, style );
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#hideNotify()
	 */
	public void hideNotify() {
        //#debug info
		//# System.out.println("<<<HIDE NOTIFY");
		super.hideNotify();
		if (!this.takeSnapshot) {
			// this is propably just a system alert that is shown to the user:
			this.isHiding = true;
			//#if !polish.Bugs.SingleCapturePlayer
                //#debug info
                //# System.out.println("This device has no SingleCapturePlayer Bug");
				Thread thread = new Thread( this );
				if (!this.wait) {
					thread.start();
				} else {
					try {
						thread.wait();

					} catch(Exception e) {
						//#debug error
						//# System.out.println("Problem while Waiting in hideNotify() "+e);
					}
				}
			//#endif
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#showNotify()
	 */
	public void showNotify() {
        //#debug info
		//# System.out.println(">>>SHOW NOTIFY");
		super.showNotify();
		if (!this.takeSnapshot) {
			// this is propably just a system alert that is shown to the user:
			this.isHiding = false;
			Thread thread = new Thread( this );
			if (!this.wait) {
				thread.start();
			} else {
				try {
					thread.wait(); 
				} catch(Exception e) {
					//#debug error
					//# System.out.println("Problem while Waiting in showNotify() "+e);
				}
			}
		}
	}


	public void run() {
	synchronized(Thread.currentThread())
	{
			this.wait = true;
			//#debug info
            //# System.out.println("Check if player is null " + (new Date()).toString());
            if (!this.isHiding && this.player == null) {
            	try {
					String[] contentTypes = Manager.getSupportedContentTypes("capture");
					if (contentTypes == null || contentTypes.length == 0) {
						this.error = new MediaException("capture not supported");
						return;
					}
					String protocol;
					//#if polish.identifier.motorola/v3xx
						//# protocol = "capture://camera";
					//#elif polish.group.series60e3
						//# protocol = "capture://devcam0";
					//#else
						protocol = "capture://video";
	                //#endif
					//#debug info
					//# System.out.println("Here are the supported contentTypes");
					for (int i = 0; i < contentTypes.length; i++) {
						String contentType = contentTypes[i];
                        //#debug info
                        //# System.out.println("ContentType " + i + " " + contentType);
						if ("image".equals(contentType)) { // this is the case on Series 40, for example
							protocol = "capture://image";
						}
					}
                    //#debug info
                    //# System.out.println("The capture protocol is " + protocol);
					this.player = Manager.createPlayer(protocol);
					this.player.realize();
					this.videoControl = (VideoControl) this.player.getControl("VideoControl");
					if (this.videoControl != null) {
						try {
							//#if polish.Bugs.displaySetCurrentFlickers && polish.useFullScreen
								//# this.videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, MasterCanvas.instance);
							//#else
								this.videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
							//#endif
							int width = this.contentWidth;//getWidth() - 5;
							int height = this.contentHeight; //getHeight() - (this.titleHeight + 2);
							this.videoControl.setDisplayLocation(this.contentX, this.contentY );
							this.videoControl.setDisplaySize( width, height );
							this.videoControl.setVisible(true);
							this.player.prefetch();
							this.player.start();
						} catch (MediaException e) {
							this.error = e;
			                //#debug error
			                //# System.out.println("Cannot start video player. The error is: " + e);
			                return;
						}
					}
            	} catch (IOException e) {
					//#debug error
					//# System.out.println("Unable to establish player" + e);
					this.error = new MediaException( e.toString() );
            	} catch (MediaException e) {
					//#debug error
					//# System.out.println("Unable to establish player" + e);
					this.error = e;
					return;
            	}
            } else if (!this.isHiding && this.player.getState() != Player.STARTED) {
            	try {
            		this.player.start();
            	} catch (MediaException e) {
					//#debug error
					//# System.out.println("Unable to start player" + e);
            	}
            }
            //#debug info
            //# System.out.println("Check if this is a capture command " + (new Date()).toString());
            if (this.takeSnapshot) {
				if (this.videoControl == null) {
					this.error = new MediaException("Unable to init player: " + (this.error != null ? this.error.toString() : "unknown"));
				} else {
					try {
	                    //#debug info
	                    //# System.out.println("Start to capture data " + (new Date()).toString());
	                    this.snapshotData = this.videoControl.getSnapshot(this.snapshotEncoding);
                        //#debug info
                        //# System.out.println("End capture data " + (new Date()).toString());
                        if (this.snapshotData == null) {
							// retry:
							this.snapshotData = this.videoControl.getSnapshot(this.snapshotEncoding);
							if (this.snapshotData == null) {
								this.error = new MediaException("No Data");
							}
                        }
					} catch (MediaException e) {
						//#debug error
						//# System.out.println("Unable to take snapshot " + e);
						this.error = e;
					} catch (SecurityException e) {
						//#debug error
						//# System.out.println("Unable to take snapshot " + e);
						this.error = new MediaException( e.toString() );
						discardPlayer();
					}
				}
            }
		//#if !polish.Bugs.SingleCapturePlayer
			if (this.isHiding && this.player != null) {
				//this.videoControl.setVisible(false);
				try {
					try {
						this.player.stop();
					} catch (MediaException e) {
						//#debug error
						//# System.out.println("Unable to stop player" + e);
					}
				} catch (Exception e) {
					//#debug error
					//# System.out.println("Unable to close player" + e);
				} finally {
					//this.player = null;
				}
			}
		//#endif
        //#debug info
        //# System.out.println("Done at " + (new Date()).toString());

        this.wait = false;
		try {
      		//Thread.currentThread().notifyAll();
      		notifyAll();
		} catch(Exception e) {
			 //#debug error
			 //# System.out.println("Problem in notifying the thrreads ");
		}
	 }





	}

	/**
	 * Retrieves the supported snapshot encodings available on the current device.
	 *
	 * @return an array of encodings.
	 *         When the "video.snapshot.encodings" system property is null, an empty array is returned.
	 */
	public static String[] getSnapshotEncodings(){
		String supportedEncodingsStr = System.getProperty("video.snapshot.encodings");
		if(supportedEncodingsStr == null){
			return new String[0];
		}
		String[] encodings = TextUtil.split(supportedEncodingsStr, ' ');
		Arrays.sort(encodings);
		return encodings;
	}

	/**
	 * Takes a snapshot in the default encoding
	 *
	 * @return the image taken
	 * @throws MediaException when taking the snapshot fails
	 */
	public Image getSnapshotImage() throws MediaException {
		return getSnapshotImage( null );
	}

	/**
	 * Takes a snapshot in the desired encoding/settings
	 *
	 * @param encoding the encoding and optionally size
	 * @return the image taken
	 * @throws MediaException when taking the snapshot fails
	 */
	public Image getSnapshotImage( String encoding ) throws MediaException {
		//System.out.println("GET SNAPSHOT IMAGE");
		byte[] data = getSnapshot(encoding);
		return Image.createImage( data, 0, data.length );
	}

	/**
	 * Takes a snapshot in the default encoding
	 *
	 * @return the image taken
	 * @throws MediaException when taking the snapshot fails
	 */
	public byte[] getSnapshot() throws MediaException {
		return getSnapshot( null );
	}



	/**
	 * Takes a snapshot in the desired encoding/settings
	 *
	 * @param encoding the encoding and optionally size
	 * @return the image taken
	 * @throws MediaException when taking the snapshot fails
	 */
	public byte[] getSnapshot( String encoding ) throws MediaException {
		if (this.error != null) {
			throw this.error;
		}
		try {
			this.snapshotEncoding = encoding;
			this.takeSnapshot = true;
			//System.out.println("STARTING SNAPSHOT THREAD...");
			Thread thread = new Thread( this );
			if (!this.wait) {
				thread.start();
			} else {
				try {
					thread.wait();
				} catch(Exception e) {
					//#debug error
					//# System.out.println("Problem while Waiting in hideNotify() "+e);
				}
			}

			while (this.snapshotData == null && this.error == null) {
				try {
					Thread.sleep(40);
					//System.out.print('0');
				} catch (InterruptedException e) {
					// ignore
				}
			}

			if (this.error != null) {
				throw this.error;
			}
			byte[] data = this.snapshotData;
			this.snapshotData = null;
			return data;
		} finally {
			//System.out.println("GETTING OUT OF SNAPSHOT HELL...!");
			this.takeSnapshot = false;
		}
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintScreen(javax.microedition.lcdui.Graphics)
	 */
	protected void paintScreen(Graphics g) {
		if (this.error != null) {
			g.drawString( this.error.toString(), getWidth() - 10, getHeight()/2, Graphics.RIGHT | Graphics.TOP );
		}
	}


	//#ifdef polish.useDynamicStyles
	//# /* (non-Javadoc)
	 //# * @see de.enough.polish.ui.Screen#createCssSelector()
	 //# */
	//# protected String createCssSelector() {
		//# return "snapshotscreen";
	//# }
	//#endif


	private void discardPlayer()
	{
			if (this.player!=null)
			{
				this.player.close();
				this.player = null;
			}
	}



}

