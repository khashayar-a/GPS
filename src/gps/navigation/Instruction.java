package gps.navigation;

import gps.bean.Node;
import gps.bean.Point;

public class Instruction {
	Node location;
	String action;

	public Instruction(double longitude, double latitude, String action) {
		this.location = new Node(longitude, latitude);
		this.action = action;
	}

	public Node getLocation() {
		return location;
	}

	public String getAction() {
		return action;
	}

}
