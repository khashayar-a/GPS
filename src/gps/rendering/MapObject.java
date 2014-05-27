package gps.rendering;

import java.util.ArrayList;

import android.graphics.Point;

public class MapObject {
	private float preX;
	private float preY;
	private float curX;
	private float curY;
	private String name;
	private String type;
	private ArrayList<Point> points;
	
	public MapObject(float curX, float curY, String type) {
		this.curX = curX;
		this.curY = curY;
		this.type = type;
	}
	
	public MapObject(ArrayList<Point> points, String type, String name) {
		this.name = name;
		this.type = type;
		this.points = points;
	}

	public MapObject(float preX, float preY, float curX, float curY, String type, String name ) {
		this.preX = preX;
		this.preY = preY;
		this.curX = curX;
		this.curY = curY;
		this.type = type;
		this.name = name;
	}
	
	public MapObject(float preX, float preY, float curX, float curY ) {
		this.preX = preX;
		this.preY = preY;
		this.curX = curX;
		this.curY = curY;
	}

	public ArrayList<Point> getPoints() {
		return this.points;
	}

	public String getType() {
		return this.type;
	}

	public float getPreX() {
		return this.preX;
	}

	public float getPreY() {
		return this.preY;
	}

	public float getCurX() {
		return this.curX;
	}

	public float getCurY() {
		return this.curY;
	}

	public String getName() {
		return this.name;
	}
}