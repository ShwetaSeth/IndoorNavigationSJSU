package com.sample.parser;

public class ClassNode {

	private int classNumber;
	private int floor;
	
	
	public ClassNode(int classNumber, int floor) {
		super();
		this.classNumber = classNumber;
		this.floor = floor;
	}
	public int getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
}
