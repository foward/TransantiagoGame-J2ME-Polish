/*
 * Created on Sep 15, 2007 at 3:46:41 AM.
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
package de.enough.polish.event;

import java.util.Hashtable;

/**
 * <p>Manages events and forwards them to appropriate listeners</p>
 *
 * <p>Copyright Enough Software 2007 - 2008</p>
 * <pre>
 * history
 *        Sep 15, 2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class EventManager {
	
	/** name of the pressed state event **/
	public static final String EVENT_PRESSED = "pressed";
	
	
	private static EventManager INSTANCE = new EventManager();
	private final Hashtable eventListenersByEvent;
	
	private EventManager() {
		this.eventListenersByEvent = new Hashtable();
	}
	
	/**
	 * Retrieves the instance of the event manager (singleton pattern).
	 * 
	 * @return the instance of the event manager (singleton pattern).
	 */
	public static EventManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Forwards an event-start to any listeners.
	 * @param name the name of the event, e.g. EVENT_PRESSED
	 * @param source the source of the event, for example an item
	 * @param data additional optional data, depends on the event - might be null!
	 */
	public void triggerEventStart( String name, Object source, Object data ) {
		EventListener[] listeners = (EventListener[]) this.eventListenersByEvent.get( name );
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				EventListener listener = listeners[i];
				try {
					listener.handleEventStart(name, source, data);
				} catch (Exception e) {
					//#debug error
					//# System.out.println("Unable to forward event " + name + " to " + listener + e );
				}
			}
		}
	}

	/**
	 * Forwards an event-end to any listeners.
	 * @param name the name of the event, e.g. EVENT_PRESSED
	 * @param source the source of the event, for example an item
	 * @param data additional optional data, depends on the event - might be null!
	 */
	public void triggerEventEnd( String name, Object source, Object data ) {
		EventListener[] listeners = (EventListener[]) this.eventListenersByEvent.get( name );
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				EventListener listener = listeners[i];
				try {
					listener.handleEventEnd(name, source, data);
				} catch (Exception e) {
					//#debug error
					//# System.out.println("Unable to forward event " + name + " to " + listener + e );
				}
			}
		}
	}

	/**
	 * Adds a event listener.
	 * 
	 * @param eventName the name of events that the listener is interested in
	 * @param listener the listener
	 * @throws NullPointerException  when the eventName or listener is null
	 */
	public void addEventListener( String eventName, EventListener listener ) {
		EventListener[] listeners = (EventListener[]) this.eventListenersByEvent.get( eventName );
		if (listeners == null) {
			this.eventListenersByEvent.put( eventName, new EventListener[]{ listener } );
		} else {
			EventListener[] newListeners = new EventListener[ listeners.length + 1 ];
			System.arraycopy( listeners, 0, newListeners, 0, listeners.length );
			newListeners[ listeners.length ] = listener;
			this.eventListenersByEvent.put( eventName, newListeners );
		}
		
	}

}
