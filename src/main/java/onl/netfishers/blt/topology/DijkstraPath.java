/*******************************************************************************
 * Copyright (c) 2016 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.topology;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import onl.netfishers.blt.topology.DijkstraAlgorithm;
import onl.netfishers.blt.topology.net.Network;
import onl.netfishers.blt.topology.net.Router;

@XmlRootElement(name = "shortestpath")
@XmlAccessorType(value = XmlAccessType.NONE)
public class DijkstraPath {
	private LinkedList<Router> path ;
	private int weight = 0;
	private long targetId = 0;
	private String originName = null;
	private String targetName = null;
	
	public DijkstraPath(){
		
	}
	
	public DijkstraPath(Network network, Router origin, Router target) {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(network);
		dijkstra.execute(origin);
		this.path = dijkstra.getPathList(target);
    	this.weight = dijkstra.getPathWeight(target);
		this.targetId = target.getId();
		this.originName = origin.getName();
	    this.targetName = target.getName();	 
	}
	
	@XmlAttribute
	public long getTargetId() {
	    return targetId;
	}
	
	@XmlElement(name = "targetname")
	public String getTargetName() {
	    return targetName;
	}
	
	public String getOriginName() {
	    return originName;
	}
	
	@XmlElementWrapper(name = "vertices")
	@XmlElement(name = "vertex")
	public LinkedList<Router> getPath() {
	    return this.path;
	}
	
	
	@XmlElement(name = "weight")
	public int getWeight() {
	    return this.weight;
	}
	
	@Override
	public String toString() {
		return "shortest path from " + originName + " to " + targetName 
				+ ": " +path+" - weight: "  + weight;
	}
}
