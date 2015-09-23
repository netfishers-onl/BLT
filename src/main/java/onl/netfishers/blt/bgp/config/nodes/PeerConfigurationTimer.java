/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

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
