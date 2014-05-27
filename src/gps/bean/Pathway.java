package gps.bean;

import java.util.ArrayList;

public class Pathway {
	

	private Long wayId;
	private ArrayList<InnerNode> nodes =  new ArrayList<InnerNode>();
	private Double lenght;
	
	public Long getWayId() {
		return wayId;
	}
	public void setWayId(Long wayId) {
		this.wayId = wayId;
	}
	public ArrayList<InnerNode> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<InnerNode> nodes) {
		this.nodes = nodes;
	}
	public Double getLenght() {
		return lenght;
	}
	public void setLenght(Double lenght) {
		this.lenght = lenght;
	}
	
}