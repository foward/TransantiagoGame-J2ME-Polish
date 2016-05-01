//#condition polish.mmapi || polish.midp2
/*
 * Created on Nov 21, 2006 at 6:16:24 PM.
 * 
 * Copyright (c) 2007 Robert Virkus / Enough Software
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
package de.enough.polish.multimedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/**
 * <p>
 * Plays back audio files - at the moment this is only supported for MIDP 2.0
 * and devices that support the MMAPI
 * </p>
 * 
 * <p>
 * Copyright Enough Software 2006 - 2008
 * </p>
 * 
 * <pre>
 * history
 *        Nov 21, 2006 - rob creation
 * </pre>
 * 
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class AudioPlayer implements PlayerListener {

	private final static Hashtable AUDIO_TYPES = new Hashtable();

	private final boolean doCachePlayer;

	private Player player;

	private PlayerListener listener;

	private final String defaultContentType;

	/**
	 * Creates a new audio player with no default content type and no caching.
	 */
	public AudioPlayer() {
		this(false, null, null);
	}

	/**
	 * Creates a new audio player with no default content type.
	 * 
	 * @param doCachePlayer caches the player even though the end of the media is reached
	 */
	public AudioPlayer(boolean doCachePlayer) {
		this(doCachePlayer, null, null);
	}

	/**
	 * Creates a new audio player without caching and with no listener.
	 * @param contentType the type of the referenced media, this is being resolved to the phone's expected type automatically.
	 *        You can, for example, use the type "audio/mp3" and this method resolves the type to "audio/mpeg3", if this
	 *        is expected by the device.
	 */
	public AudioPlayer(String contentType) {
		this(false, contentType, null);
	}

	/**
	 * Creates a new audio player with no listener
	 * @param doCachePlayer caches the player even though the end of the media is reached
	 * @param contentType the type of the referenced media, this is being resolved to the phone's expected type automatically.
	 *        You can, for example, use the type "audio/mp3" and this method resolves the type to "audio/mpeg3", if this
	 *        is expected by the device.
	 */
	public AudioPlayer(boolean doCachePlayer, String contentType) {
		this(doCachePlayer, contentType, null);
	}

	/**
	 * Creates a new audio player
	 * @param doCachePlayer caches the player even though the end of the media is reached
	 * @param contentType the type of the referenced media, this is being resolved to the phone's expected type automatically.
	 *        You can, for example, use the type "audio/mp3" and this method resolves the type to "audio/mpeg3", if this
	 *        is expected by the device.
	 * @param listener an optional PlayerListener
	 */
	public AudioPlayer(boolean doCachePlayer, String contentType, PlayerListener listener) {
		this.listener = listener;
		this.doCachePlayer = doCachePlayer;
		if (contentType != null) {
			if (!contentType.startsWith("audio/")) {
				contentType = "audio/" + contentType;
			}
			String correctContentType = getAudioType(contentType, null);
			if (correctContentType != null) {
				contentType = correctContentType;
			}
		}
		this.defaultContentType = contentType;
	}

	/**
	 * Plays the media taken from the specified URL.
	 * @param url the URL of the media 
	 * @param type the type of the referenced media, this is being resolved to the phone's expected type automatically.
	 *        You can, for example, use the type "audio/mp3" and this method resolves the type to "audio/mpeg3", if this
	 *        is expected by the device.
	 * @throws MediaException when the media is not supported
	 * @throws IOException when the URL cannot be resolved
	 */
	public void play(String url, String type) throws MediaException,
			IOException {
		InputStream in = getClass().getResourceAsStream(url);
		if (in == null) {
			throw new IOException("not found: " + url);
		}
		play(in, type);
	}

	/**
	 * Plays the media taken from the specified input stream.
	 * @param in the media input 
	 * @param type the type of the referenced media, this is being resolved to the phone's expected type automatically.
	 *        You can, for example, use the type "audio/mp3" and this method resolves the type to "audio/mpeg3", if this
	 *        is expected by the device.
	 * @throws MediaException when the media is not supported
	 * @throws IOException when the input cannot be read
	 */
	public void play(InputStream in, String type) throws MediaException,
			IOException {
		String correctType = getAudioType(type, "file");
		if (correctType == null) {
			//#debug warn
			//# System.out.println("Unable to find correct type for " + type
					//# + " with the file protocol");
			correctType = getAudioType(type, null);
			if (correctType == null) {
				//#debug warn
				//# System.out.println("Unable to find correct type for " + type);
				correctType = type;
			}
		}
		this.player = Manager.createPlayer(in, correctType);
		this.player.addPlayerListener(this);
		this.player.start();
	}

	/**
	 * Plays the media taken from the specified URL  with the content type specified in the constructor.
	 * @param url the URL of the media 
	 * @throws MediaException when the media is not supported
	 * @throws IOException when the URL cannot be resolved
	 */
	public void play(String url) throws MediaException, IOException {
		InputStream in = getClass().getResourceAsStream(url);
		if (in == null) {
			throw new IOException("not found: " + url);
		}
		play(in);
	}

	/**
	 * Plays the media taken from the specified input stream with the content type specified in the constructor.
	 * @param in the media input 
	 * @throws MediaException when the media is not supported
	 * @throws IOException when the input cannot be read
	 */
	public void play(InputStream in) throws MediaException, IOException {
		String correctType = this.defaultContentType;
		this.player = Manager.createPlayer(in, correctType);
		this.player.addPlayerListener(this);
		this.player.start();
	}

	/**
	 * Plays back the last media again. This can only be used when doCachePlayer
	 * is set to true in the constructor
	 * 
	 * @throws MediaException
	 *             when the player cannot be started
	 * @see #AudioPlayer(boolean)
	 * @see #AudioPlayer(boolean, String)
	 * @see #AudioPlayer(boolean, String, PlayerListener)
	 * @see #AudioPlayer(String)
	 */
	public void play() throws MediaException {
		if (this.player != null) {
			this.player.start();
		}
	}

	/**
	 * Returns the original player.
	 * 
	 * @return the original player, this can be null when no audio has been
	 *         played back so far.
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Helper function for getting a supported media type.
	 * 
	 * @param type
	 *            the type like "audio/mp3"
	 * @param protocol
	 *            the protocol, when null is given the content type will be
	 *            returned for any protocol
	 * @return the type supported by the device, for example "audio/mpeg3" -
	 *         null when the given type is not supported by the device.
	 */
	public static String getAudioType(String type, String protocol) {
		if (AUDIO_TYPES.size() == 0) {
			addTypes(new String[] { "audio/3gpp", "audio/3gp" });
			addTypes(new String[] { "audio/x-mp3", "audio/mp3", "audio/x-mp3",
					"audio/mpeg3", "audio/x-mpeg3", "audio/mpeg-3" });
			addTypes(new String[] { "audio/midi", "audio/x-midi", "audio/mid",
					"audio/x-mid", "audio/sp-midi" });
			addTypes(new String[] { "audio/wav", "audio/x-wav" });
			addTypes(new String[] { "audio/amr", "audio/x-amr" });
			addTypes(new String[] { "audio/mpeg4", "audio/mpeg-4", "audio/mp4",
					"audio/mp4a-latm" });
			addTypes(new String[] { "audio/imelody", "audio/x-imelody",
					"audio/imy", "audio/x-imy" });
		}
		String[] supportedContentTypes = Manager
				.getSupportedContentTypes(protocol);
		if (supportedContentTypes == null || supportedContentTypes.length == 0) {
			return null;
		}
		Hashtable mappings = (Hashtable) AUDIO_TYPES.get(type);
		if (mappings == null) {
			//#debug warn
			//# System.out.println("The audio content type " + type
					//# + " has no known synonyms.");
			for (int i = 0; i < supportedContentTypes.length; i++) {
				String contentType = supportedContentTypes[i];
				if (contentType.equals(type)) {
					return type;
				}
			}
		} else {
			for (int i = 0; i < supportedContentTypes.length; i++) {
				String contentType = supportedContentTypes[i];
				if (mappings.containsKey(contentType)) {
					return contentType;
				}
			}
		}

		return null;
	}

	/**
	 * Determines if the audio player is currently playing music
	 * @return true when audio is played back
	 */
	public boolean isPlaying() {
		if (this.player == null) {
			return false;
		} else {
			return this.player.getState() == Player.STARTED;
		}
	}

	private static final void addTypes(String[] types) {
		Hashtable nestedMap = new Hashtable();
		for (int i = 0; i < types.length; i++) {
			String type = types[i];
			nestedMap.put(type, type);
			AUDIO_TYPES.put(type, nestedMap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.media.PlayerListener#playerUpdate(javax.microedition.media.Player,
	 *      java.lang.String, java.lang.Object)
	 */
	public void playerUpdate(Player p, String event, Object data) {

		if (this.listener != null) {
			this.listener.playerUpdate(p, event, data);
		}

		if (!this.doCachePlayer && PlayerListener.END_OF_MEDIA.equals(event)) {
			p.removePlayerListener(this);
			cleanUpPlayer();
		}
	}

	/**
	 * Closes and deallocates the player.
	 */
	public void cleanUpPlayer() {
		if (this.player != null) {
			this.player.deallocate();
			this.player.close(); // necessary for some Motorola devices
			this.player = null;
		}
	}
}
