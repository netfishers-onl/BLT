/**
 *  Copyright 2012, 2014 Rainer Bieniek (Rainer.Bieniek@web.de)
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
 * File: onl.netfishers.blt.bgp.netty.fsm.FSMEvent.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum FSMEventType {
	// Administrative events
	ManualStart,                           // covers RFC 4271 events 1, 4,  
	ManualStop,                            // RFC4271 event 2
	AutomaticStart,                        // covers RFC4271 event 3, 5, 6, 7
	AutomaticStop,                         // RFC4271 event 8
	
	// Timer events
	ConnectRetryTimer_Expires,             // RFC4271 event 9
	HoldTimer_Expires,                     // RFC4271 event 10
	KeepaliveTimer_Expires,                // RFC4271 event 11
	DelayOpenTimer_Expires,                // RFC4271 event 12
	IdleHoldTimer_Expires,                 // RFC4271 event 13
	
	// TCP connection-based events. 
	// The underlying transport layer abstraction doesn't expose any other events than confirmed and failed
	// Therefore event 15 is not implemented
//	Tcp_CR_Invalid,                        // RFC4271 event 15
    Tcp_CR_Acked,                          // RFC4271 event 16
	TcpConnectionConfirmed,                // covers RFC4271 event 14, 17
	TcpConnectionFails,                    // RFC4271 event 18
	
	// BGP Message-based events
	// The OpenCollisionDump event is never fired explicitly but handled internally int the FSM
	BGPOpen,                               // covers RFC4271 event 19, 20
	BGPHeaderErr,                          // RFC4271 event 21
	BGPOpenMsgErr,                         // RFC4271 event 22
	// OpenCollisionDump,                     // RFC4271 event 23
	NotifyMsgVerErr,                       // RFC4271 event 24
	NotifyMsg,                             // RFC4271 event 25
	KeepAliveMsg,                          // RFC4271 event 26 
	UpdateMsg,                             // RFC4271 event 27
	UpdateMsgErr,                          // RFC4271 event 28
	
}
