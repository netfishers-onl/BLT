/**
 * 
 */
package org.blt.bgp.config.nodes;

/**
 * @author nitinb
 *
 */
public interface PeerConfigurationTimer {
	
	/**
	 * @return
	 */
	public int getHoldTime();

	/**
	 * @return
	 */
	public int getIdleHoldTime();
	
	/**
	 * @return
	 */
	public int  getDelayOpenTime();

	/**
	 * @return
	 */
	public int getConnectRetryTime();

	/**
	 * @return
	 */
	public int getAutomaticStartInterval();
}
