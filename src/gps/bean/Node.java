package gps.bean;

import java.util.HashMap;

public class Node {

	private Double latitude;
	private Double longitude;
	private HashMap<String, String> tags = new HashMap<String , String>();
	
	public Node(double longitude, double latitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Node() {
		// TODO Auto-generated constructor stub
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public HashMap<String, String> getTags() {
		return tags;
	}

	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Node [latitude=" + latitude + ", longitude=" + longitude
				+ ", tags=" + tags + "]";
	}

}
