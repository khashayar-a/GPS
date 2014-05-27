package gps.navigation;

import gps.application.GPSActivity;
import gps.bean.Node;
import gps.calculation.FindPath;
import gps.rendering.MapView;
import gps.talkback.Talkback;

import java.util.Queue;

import android.graphics.PointF;
import android.location.Location;
import android.os.Handler;

public class Navigator extends Thread {

	private Queue<Instruction> instructions;
	private MapView map;
	private Talkback tb;
	private Handler handler;
	private PointF nextTurn;
	private long distance;
	private String currentDist = "Unknown";
	private boolean[] warned = { false, false, false, false, false };

	public Navigator(Queue<Instruction> instructions, GPSActivity gpsa,
			Handler handler) {
		this.instructions = instructions;
		this.map = gpsa.mapView;
		this.tb = gpsa.tb;
		this.handler = handler;
	}

	public void run() {
		while (!instructions.isEmpty()) {
			Location current = map.getCurrentLocation();
			if (current == null) {
				// System.out.println("Current location is null");
				// System.out.println("Waiting for location");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				continue;
			} else {
				Instruction next = instructions.peek();
				Node node = next.getLocation();
				this.setNextTurn(new PointF(Float.parseFloat(node
						.getLongitude().toString()), Float.parseFloat(node
						.getLatitude().toString())));

				distance = FindPath
						.CalculateDistance(
								next.getLocation(),
								new Node(current.getLongitude(), current
										.getLatitude()));

				// System.out.println("We have a location and the distance is: "
				// + distance);
				handler.sendEmptyMessage(0);
				if (distance < 10 && !warned[0]) {
					warned[0] = true;
					currentDist = "10";
					next = instructions.poll();
					tb.queueSound(1, 0, next.getAction());
					this.resetWarned();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				} else if (distance < 20 && !warned[1]) {
					warned[1] = true;
					currentDist = "20";
					next = instructions.peek();
					tb.queueSound(1, 0, next.getAction());
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				} else if (distance < 35 && !warned[2]) {
					warned[2] = true;
					currentDist = "35";
					next = instructions.peek();
					tb.queueSound(1, 0, next.getAction());
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				} else if (distance < 50 && !warned[3]) {
					warned[3] = true;
					currentDist = "50";
					next = instructions.peek();
					tb.queueSound(1, 0, next.getAction());
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				} else if (distance < 100 && !warned[4]) {
					warned[4] = true;
					currentDist = "100";
					next = instructions.peek();
					tb.queueSound(1, 0, next.getAction());
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				} else {
					if (distance > 100) {
						currentDist = "Unknown";
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	private void resetWarned() {
		for (int i = 0; i < warned.length; i++) {
			warned[i] = false;
		}
	}

	public PointF getNextTurn() {
		return nextTurn;
	}

	public void setNextTurn(PointF nextTurn) {
		this.nextTurn = nextTurn;
	}

	public long getDistance() {
		return distance;
	}

	public String getCurrentDist() {
		return currentDist;
	}

	public void setCurrentDist(String currentDist) {
		this.currentDist = currentDist;
	}

}
