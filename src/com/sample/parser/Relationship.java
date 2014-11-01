package com.sample.parser;

public class Relationship {
	private String toNode;    
	private String orientation;
	private String fromNode;  
	private String cost;
	
	public String getToNode() {
		return toNode;
	}
	@Override
	public String toString() {
		return "Relationship [toNode=" + toNode + ", orientation="
				+ orientation + ", fromNode=" + fromNode + ", cost=" + cost
				+ "]";
	}
	public void setToNode(String toNode) {
		this.toNode = toNode;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getFromNode() {
		return fromNode;
	}
	public void setFromNode(String fromNode) {
		this.fromNode = fromNode;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}  
	
	


}
