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
 * File: onl.netfishers.blt.bgp.netty.handlers.NotificationEvent.java 
 */
package onl.netfishers.blt.bgp.netty.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NotificationEvent extends BgpEvent {

	private List<NotificationPacket> notifications = new LinkedList<NotificationPacket>();
	
	public NotificationEvent(NotificationPacket packet) {
		notifications.add(packet);
	}
	
	public NotificationEvent(Collection<NotificationPacket> notifications) {
		this.notifications.addAll(notifications);
	}

	/**
	 * @return the notifications
	 */
	public List<NotificationPacket> getNotifications() {
		return Collections.unmodifiableList(notifications);
	}
}
