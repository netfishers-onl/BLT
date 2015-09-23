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
 * File: onl.netfishers.blt.bgp.netty.fsm.InternalFSMCallbacks.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;

/**
 * Callback interface used for triggering actions from the internal state machine to
 * the connection management and messagng code
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface InternalFSMCallbacks {

	/**
	 * The remote connection to the remote peer shall be initiated
	 */
	void fireConnectRemotePeer();
	
	/**
	 * The remote connection to the peer (if established) shall be disconnected and closed
	 */
	public void fireDisconnectRemotePeer(FSMChannel channel);

	/**
	 * Sent an <code>OPEN</code> message to the remote peer.
	 */
	public void fireSendOpenMessage(FSMChannel channel);

	/**
	 * send an FSM error notification to the remote peer
	 */
	public void fireSendInternalErrorNotification(FSMChannel channel);

	/**
	 * send a CEASE notification to the remote peer
	 */
	public void fireSendCeaseNotification(FSMChannel channel);

	/**
	 * send a keepalive message to the remote peer
	 */
	public void fireSendKeepaliveMessage(FSMChannel channel);

	/**
	 * release all resources hold on behalf of the remote peer
	 */
	public void fireReleaseBGPResources();
	
	/**
	 * complete the initialization for the local end
	 */
	public void fireCompleteBGPLocalInitialization();

	/**
	 * complete the initialization for the remote end
	 */
	public void fireCompleteBGPPeerInitialization();

	/**
	 * fire a notification to the peer that the hold timer expired
	 */
	public void fireSendHoldTimerExpiredNotification(FSMChannel channel);

	/**
	 * fire an notification to the peer that it sent a bad update
	 */
	public void fireSendUpdateErrorNotification(FSMChannel channel);

	/**
	 * The connection has been established and an initial update packet can be sent
	 */
	public void fireEstablished();
}
