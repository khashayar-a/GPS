package gps.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class Way {

	private ArrayList<Long> nodes = new ArrayList<Long>();
	private HashMap<String, String> tags = new HashMap<String , String>();

	public ArrayList<Long> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Long> nodes) {
		this.nodes = nodes;
	}

	public HashMap<String, String> getTags() {
		return tags;
	}

	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Way [nodes=" + nodes + ", tags=" + tags + "]";
	}
	
}
