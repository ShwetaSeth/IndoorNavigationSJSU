package com.sample.parser;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {
    private int weight;  
	 
	private String startNode;

	private List<String> nodes = new ArrayList<String>(); 
	private int lengthPath;  
	private List<String> relationshipBetNodesOfPath = new ArrayList<String>(); 
	private String endNode;
	private List<Relationship> relationshipData = new ArrayList<Relationship>(); 
	
	@Override
	public String toString() {
		
		StringBuffer relationshipsString = new StringBuffer();
		for(int i = 0;i<relationshipData.size();i++)
			relationshipsString.append("Relationship "+ i+1+": "+relationshipData.get(i).toString());
			
		
		return "ShortestPath [weight=" + weight + ", startNode=" + startNode
				+ ", nodes=" + nodes + ", lengthPath=" + lengthPath + ", end="
				+ endNode + ", relationshipData=" + relationshipsString.toString() + "]";
	}
	public List<Relationship> getRelationshipData() {
		return relationshipData;
	}
	public void setRelationshipData(List<Relationship> relationshipData) {
		this.relationshipData = relationshipData;
	}
	/*public ShortestPath(int weight, String startNode, List<String> nodes,
			int lengthPath, List<String> relationshipBetNodesOfPath, String end) {
		super();
		this.weight = weight;
		this.startNode = startNode;
		this.nodes = nodes;
		this.lengthPath = lengthPath;
		this.relationshipBetNodesOfPath = relationshipBetNodesOfPath;
		this.end = end;
	}*/
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getStartNode() {
		return startNode;
	}
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	public List<String> getNodes() {
		return nodes;
	}
	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}
	public int getLengthPath() {
		return lengthPath;
	}
	public void setLengthPath(int lengthPath) {
		this.lengthPath = lengthPath;
	}
	public List<String> getRelationshipBetNodesOfPath() {
		return relationshipBetNodesOfPath;
	}
	public void setRelationshipBetNodesOfPath(
			List<String> relationshipBetNodesOfPath) {
		this.relationshipBetNodesOfPath = relationshipBetNodesOfPath;
	}
	public String getEnd() {
		return endNode;
	}
	public void setEnd(String end) {
		this.endNode = end;
	}
	
	
	
	
	
}
