/*******************************************************************************
 * Copyright (c) 2016 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Lists;
import onl.netfishers.blt.topology.net.Link;
import onl.netfishers.blt.topology.net.Network;
import onl.netfishers.blt.topology.net.Router;

public class DijkstraAlgorithm {
  private List<Router> nodes = Lists.newArrayList();
  private List<Link> edges = Lists.newArrayList();
  private Set<Router> settledNodes;
  private Set<Router> unSettledNodes;
  private Map<Router, Router> predecessors;
  private Map<Router, Integer> distance;
  private Network network;
  
  
  public DijkstraAlgorithm(Network network) {
	  this.nodes.addAll(network.getRouters());
	  this.edges.addAll(network.getLinks());
	  this.network = network;
  }

  public void execute(Router source) {
    settledNodes = new HashSet<Router>();
    unSettledNodes = new HashSet<Router>();
    distance = new HashMap<Router, Integer>();
    predecessors = new HashMap<Router, Router>();
    distance.put(source, 0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
    	Router node = getMinimum(unSettledNodes);
    	settledNodes.add(node);
    	unSettledNodes.remove(node);
    	findMinimalDistances(node);
    }
  }

  private void findMinimalDistances(Router node) {
    List<Router> adjacentNodes = getNeighbors(node);
    for (Router target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  private int getDistance(Router node, Router target) {
    for (Link edge : edges) {
      if (edge.getLocalRouter().equals(node.getRouterId())
          && edge.getRemoteRouter().equals(target.getRouterId())) {
        return edge.getMetric();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<Router> getNeighbors(Router node) {
    List<Router> neighbors = new ArrayList<Router>();
    for (Link edge : edges) {
    	Router candidate = network.findRouterById(edge.getRemoteRouter());
    	if (edge.getLocalRouter().equals(node.getRouterId()) && !isSettled(candidate)) {
    		neighbors.add(candidate);
    	}
    }
    return neighbors;
  }

  private Router getMinimum(Set<Router> vertexes) {
	  Router minimum = null;
	  for (Router vertex : vertexes) {
		  if (minimum == null) {
			  minimum = vertex;
		  } else {
			  if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
				  minimum = vertex;
			  }
		  }
	  }
	  return minimum;
  }

  private boolean isSettled(Router vertex) {
    return settledNodes.contains(vertex);
  }

  private int getShortestDistance(Router destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the vertices on the path from the source 
   * to the selected target and NULL if no path exists
   */
  public LinkedList<Router> getPathList(Router target) {
    LinkedList<Router> path = new LinkedList<Router>();
    Router step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
    	return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    
    return path;
  }
  /*
   * This method returns the cumulative path weight from the source 
   * to the selected target vertex and
   * 0 if no path exists
   */
  public int getPathWeight(Router target) {
	  LinkedList<Router> path = getPathList(target);
	  if (path == null) return 0 ;
	  int distance = 0;
	  Router.RouterIdentifier previous = null;
	  
	  for (int i = 0; i < path.size(); i++) {
		  if (path.get(i) == null ) continue;
		  for (Link e:edges) {
			  if (e.getLocalRouter().equals(previous) 
					  && e.getRemoteRouter().equals(path.get(i).getRouterId())) {
				  distance += e.getMetric();
				  break;
			  }
		  }
		  previous = path.get(i).getRouterId();
	  }
	  return distance;
  }
  
} 