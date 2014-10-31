/**

*  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.blt.bgp.netty.protocol.refresh.ORFEntry.java 
 */
package org.blt.bgp.net;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class ORFEntry {
	private ORFAction action;
	private ORFMatch match;

	protected ORFEntry(ORFAction action, ORFMatch match) {
		this.action = action;
		this.match = match;
	}
	
	/**
	 * get the ORF type code
	 * 
	 * @return
	 */
	public abstract ORFType getORFType();
	
	/**
	 * @return the action
	 */
	public ORFAction getAction() {
		return action;
	}

	/**
	 * @return the match
	 */
	public ORFMatch getMatch() {
		return match;
	}
}
