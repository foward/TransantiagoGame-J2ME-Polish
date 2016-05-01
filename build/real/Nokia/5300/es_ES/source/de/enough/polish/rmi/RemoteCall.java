/*
 * Created on Dec 20, 2006 at 1:33:40 AM.
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
package de.enough.polish.rmi;

/**
 * <p>Wraps information about a remote call that should be/is being processed.</p>
 * 
 *
 * <p>Copyright Enough Software 2006, 2007 - 2008</p>
 * <pre>
 * history
 *        Dec 20, 2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class RemoteCall {
	private final String name;
	private final long primitivesFlag;
	private final Object[] parameters;
	private RemoteException raisedException;
	private Object returnValue;

	/**
	 * Creates a new remote call
	 * 
	 * @param name the name of the call
	 * @param primitivesFlag for each element of the parameters which is originally a primitive the bit will be one: 
	 *        element n = primitive means that (primitiveFlags & 2^n) != 0 
	 * @param parameters any parameters, can be null
	 */
	public RemoteCall( String name, long primitivesFlag, Object[] parameters ) {
		this.name = name;
		this.primitivesFlag = primitivesFlag;
		this.parameters = parameters;
		
	}

	/**
	 * @return  the checked exception that has been thrown by the remote method
	 */
	public RemoteException getRaisedException() {
		return this.raisedException;
	}

	/**
	 * @param raisedException the checked exception that has been thrown by the remote method
	 */
	public void setRaisedException(RemoteException raisedException) {
		this.raisedException = raisedException;
	}

	/**
	 * @return the the return value of the remote method
	 */
	public Object getReturnValue() {
		return this.returnValue;
	}

	/**
	 * @param returnValue the return value of the remote method
	 */
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retrieves the flag indicating usage of primitive types for the first 64 parameters
	 * @return a long with a 1 for each parameter that is primitive
	 */
	public long getPrimitivesFlag() {
		return this.primitivesFlag;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return this.parameters;
	}
}
