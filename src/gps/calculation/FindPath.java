package gps.calculation;

import gps.application.GPSActivity;
import gps.bean.InnerNode;
import gps.bean.Node;
import gps.bean.Pathway;
import gps.bean.Way;
import gps.navigation.Instruction;
import gps.navigation.Navigator;
import gps.rendering.SVGObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import android.graphics.Point;
import android.location.Location;
import android.os.Handler;

class Vertex implements Comparable<Vertex> {
	public final String name;
	public Edge[] adjacencies = new Edge[0];
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;
	public static String User_paht1;
	public static String User_path2;
	public long[] ways = new long[0];
	public Double latitude;
	public Double longitude;

	public Vertex(String argName, Double lat, Double lon) {
		name = argName;
		latitude = lat;
		longitude = lon;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Vertex other) {
		return Double.compare(minDistance, other.minDistance);
	}

}

class Edge {
	public final Vertex target;
	public final double weight;

	public Edge(Vertex argTarget, double argWeight) {

		target = argTarget;
		weight = argWeight;
	}
}

public class FindPath implements Runnable {

	private Location end;
	private DecimalFormat df = new DecimalFormat("###.###");
	private Location start;
	private GPSActivity gpsa;
	private InputStream routeStream;
	private Handler handler;
	private Double boxTop;
	private Double boxLeft;
	private Handler handler2;
	public Navigator nav;

	public void run() {
		setRouteStream(getWay(gpsa, start, end, boxTop, boxLeft));
		handler.sendEmptyMessage(0);
	}

	public FindPath(GPSActivity gpsa, Location start, Location end,
			Handler handler, Double boxTop, Double boxLeft, Handler handler2) {
		this.gpsa = gpsa;
		this.start = start;
		this.end = end;
		this.handler2 = handler2;
		this.handler = handler;
		this.boxTop = boxTop;
		this.boxLeft = boxLeft;
	}

	public static void computePaths(Vertex source) {
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);

				}

			}
		}
	}

	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;

	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public InputStream getWay(GPSActivity gpsa, Location start, Location end,
			Double boxTop, Double boxLeft) {

		HashMap<Long, Way> ways = new HashMap<Long, Way>();
		HashMap<Long, Node> nodes = new HashMap<Long, Node>();

		Double left;
		Double top;
		Double right;
		Double bottom;

		if (start.getLatitude() < end.getLatitude()) {
			left = getBoxLeft(start.getLatitude());
			right = getBoxLeft(end.getLatitude());
		} else {
			left = getBoxLeft(end.getLatitude());
			right = getBoxLeft(start.getLatitude());
		}
		if (start.getLongitude() < end.getLongitude()) {
			top = getBoxTop(start.getLongitude());
			bottom = getBoxTop(end.getLongitude());
		} else {
			top = getBoxTop(end.getLongitude());
			bottom = getBoxTop(start.getLongitude());
		}

		ArrayList<Location> locations = getAllBoxes(top, left, bottom, right);
		BlockReader blockReader = new BlockReader();
		blockReader.readForCars(gpsa.getApplicationContext(), ways, nodes,
				locations);

		// System.out.println(top + " " + left + " BOXXXXXXXXXx");

		HashMap<Long, Vertex> hashMap = new HashMap<Long, Vertex>();

		// Vertex[] allVertices = new Vertex[nodes.size()];
		Iterator iteratorNode = nodes.entrySet().iterator();
		int countNode = 0;
		while (iteratorNode.hasNext()) {
			Entry<Long, Node> nodeSet = (Entry<Long, Node>) iteratorNode.next();
			Node node = nodeSet.getValue();
			// allVertices[countNode] = new
			// Vertex(nodeSet.getKey().toString(),node.getLatitude(),
			// node.getLongitude());
			hashMap.put(nodeSet.getKey(), new Vertex(nodeSet.getKey()
					.toString(), node.getLatitude(), node.getLongitude()));
			countNode++;
		}

		boolean flag;
		int flagPosition = 0;

		Long lastNodeId;
		Long curruntNodeId;
		Long beforeNodeId;
		Long afterNodeId;
		double beforeDistance;
		double afterDistance;

		int curruntIndex = -1;
		int beforeIndex = -1;
		int afterIndex = -1;

		Iterator iterator = ways.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
			Way way = waySet.getValue();
			Long thisWayId = waySet.getKey();

			boolean oneWay = false;

			if (way.getTags().containsKey("oneway")) {
				oneWay = true;
			}

			if (oneWay) {
				for (int i = 0; i < way.getNodes().size() - 1; i++) {
					flag = false;

					curruntNodeId = way.getNodes().get(i);
					afterNodeId = way.getNodes().get(i + 1);
					afterDistance = (CalculateDistance(
							nodes.get(curruntNodeId), nodes.get(afterNodeId)));

					/**
					 * New Implementation
					 */
					Vertex currentVertex = hashMap.get(curruntNodeId);
					Vertex afterVertex = hashMap.get(afterNodeId);

					Edge[] tempCurrentVertexEdges = currentVertex.adjacencies;
					currentVertex.adjacencies = new Edge[tempCurrentVertexEdges.length + 1];
					for (int k = 0; k < tempCurrentVertexEdges.length; k++) {
						currentVertex.adjacencies[k] = tempCurrentVertexEdges[k];
					}
					currentVertex.adjacencies[tempCurrentVertexEdges.length] = new Edge(
							afterVertex, afterDistance);

					long[] tempCurrentVertexWays = currentVertex.ways;
					currentVertex.ways = new long[tempCurrentVertexWays.length + 1];
					for (int k = 0; k < tempCurrentVertexWays.length; k++) {
						currentVertex.ways[k] = tempCurrentVertexWays[k];
					}
					currentVertex.ways[tempCurrentVertexWays.length] = thisWayId;

					/**
					 * End Of This New Thing
					 */

					/*
					 * Previous Implementation curruntIndex = -1; afterIndex =
					 * -1;
					 * 
					 * for (int j = 0; j < allVertices.length; j++) { if
					 * (allVertices[j].name.equalsIgnoreCase(curruntNodeId
					 * .toString())) { curruntIndex = j; } if
					 * (allVertices[j].name.equalsIgnoreCase(afterNodeId
					 * .toString())) { afterIndex = j; } }
					 * 
					 * long[] tempWay = allVertices[curruntIndex].ways;
					 * allVertices[curruntIndex].ways = new long[tempWay.length
					 * + 1]; for (int k = 0; k < tempWay.length; k++) {
					 * allVertices[curruntIndex].ways[k] = tempWay[k]; }
					 * allVertices[curruntIndex].ways[tempWay.length] =
					 * thisWayId;
					 * 
					 * if (allVertices[curruntIndex].adjacencies != null) {
					 * Edge[] temp = allVertices[curruntIndex].adjacencies;
					 * allVertices[curruntIndex].adjacencies = new
					 * Edge[temp.length + 1]; for (int k = 0; k < temp.length;
					 * k++) { allVertices[curruntIndex].adjacencies[k] =
					 * temp[k]; }
					 * allVertices[curruntIndex].adjacencies[temp.length] = new
					 * Edge( allVertices[afterIndex], afterDistance); } else {
					 * allVertices[curruntIndex].adjacencies = new Edge[1];
					 * allVertices[curruntIndex].adjacencies[0] = new Edge(
					 * allVertices[afterIndex], afterDistance); }
					 */
				}

			} else {
				lastNodeId = way.getNodes().get(way.getNodes().size() - 1);
				beforeNodeId = way.getNodes().get(way.getNodes().size() - 2);
				beforeDistance = (CalculateDistance(nodes.get(lastNodeId),
						nodes.get(beforeNodeId)));

				/**
				 * New Implementation
				 */
				Vertex lastVertex = hashMap.get(lastNodeId);
				Vertex previousVertex = hashMap.get(beforeNodeId);

				Edge[] tempLastVertexEdges = lastVertex.adjacencies;
				lastVertex.adjacencies = new Edge[tempLastVertexEdges.length + 1];
				for (int k = 0; k < tempLastVertexEdges.length; k++) {
					lastVertex.adjacencies[k] = tempLastVertexEdges[k];
				}
				lastVertex.adjacencies[tempLastVertexEdges.length] = new Edge(
						previousVertex, beforeDistance);

				long[] tempLastVertexWays = lastVertex.ways;
				lastVertex.ways = new long[tempLastVertexWays.length + 1];
				for (int k = 0; k < tempLastVertexWays.length; k++) {
					lastVertex.ways[k] = tempLastVertexWays[k];
				}
				lastVertex.ways[tempLastVertexWays.length] = thisWayId;

				/**
				 * End Of This New Thing
				 */

				/*
				 * Previous Implementation
				 * 
				 * curruntIndex = -1; beforeIndex = -1;
				 * 
				 * for (int j = 0; j < allVertices.length; j++) { if
				 * (allVertices[j].name.equalsIgnoreCase(lastNodeId
				 * .toString())) { curruntIndex = j; } if
				 * (allVertices[j].name.equalsIgnoreCase(beforeNodeId
				 * .toString())) { beforeIndex = j; } }
				 * 
				 * long[] tempWay = allVertices[curruntIndex].ways;
				 * allVertices[curruntIndex].ways = new long[tempWay.length +
				 * 1]; for (int k = 0; k < tempWay.length; k++) {
				 * allVertices[curruntIndex].ways[k] = tempWay[k]; }
				 * allVertices[curruntIndex].ways[tempWay.length] = thisWayId;
				 * 
				 * if (allVertices[curruntIndex].adjacencies != null) { Edge[]
				 * tempLast = allVertices[curruntIndex].adjacencies;
				 * allVertices[curruntIndex].adjacencies = new
				 * Edge[tempLast.length + 1]; for (int k = 0; k <
				 * tempLast.length; k++) {
				 * allVertices[curruntIndex].adjacencies[k] = tempLast[k]; }
				 * allVertices[curruntIndex].adjacencies[tempLast.length] = new
				 * Edge( allVertices[beforeIndex], beforeDistance); } else {
				 * allVertices[curruntIndex].adjacencies = new Edge[1];
				 * allVertices[curruntIndex].adjacencies[0] = new Edge(
				 * allVertices[beforeIndex], beforeDistance); }
				 */

				curruntNodeId = way.getNodes().get(0);
				afterNodeId = way.getNodes().get(1);
				afterDistance = (CalculateDistance(nodes.get(curruntNodeId),
						nodes.get(afterNodeId)));

				Vertex firstVertex = hashMap.get(lastNodeId);
				Vertex secondVertex = hashMap.get(beforeNodeId);

				Edge[] tempFirstVertexEdges = firstVertex.adjacencies;
				firstVertex.adjacencies = new Edge[tempFirstVertexEdges.length + 1];
				for (int k = 0; k < tempFirstVertexEdges.length; k++) {
					firstVertex.adjacencies[k] = tempFirstVertexEdges[k];
				}
				firstVertex.adjacencies[tempFirstVertexEdges.length] = new Edge(
						secondVertex, beforeDistance);

				long[] tempFirstVertexWays = firstVertex.ways;
				firstVertex.ways = new long[tempFirstVertexWays.length + 1];
				for (int k = 0; k < tempFirstVertexWays.length; k++) {
					firstVertex.ways[k] = tempFirstVertexWays[k];
				}
				firstVertex.ways[tempFirstVertexWays.length] = thisWayId;

				/**
				 * End Of This New Thing
				 */
				/*
				 * Previous Implementation curruntIndex = -1; afterIndex = -1;
				 * 
				 * for (int j = 0; j < allVertices.length; j++) { if
				 * (allVertices[j].name.equalsIgnoreCase(curruntNodeId
				 * .toString())) { curruntIndex = j; } if
				 * (allVertices[j].name.equalsIgnoreCase(afterNodeId
				 * .toString())) { afterIndex = j; } }
				 * 
				 * long[] tempWayZero = allVertices[curruntIndex].ways;
				 * allVertices[curruntIndex].ways = new long[tempWayZero.length
				 * + 1]; for (int k = 0; k < tempWayZero.length; k++) {
				 * allVertices[curruntIndex].ways[k] = tempWayZero[k]; }
				 * allVertices[curruntIndex].ways[tempWayZero.length] =
				 * thisWayId;
				 * 
				 * if (allVertices[curruntIndex].adjacencies != null) { Edge[]
				 * tempZero = allVertices[curruntIndex].adjacencies;
				 * allVertices[curruntIndex].adjacencies = new
				 * Edge[tempZero.length + 1]; for (int k = 0; k <
				 * tempZero.length; k++) {
				 * allVertices[curruntIndex].adjacencies[k] = tempZero[k]; }
				 * allVertices[curruntIndex].adjacencies[tempZero.length] = new
				 * Edge( allVertices[afterIndex], afterDistance); } else {
				 * allVertices[curruntIndex].adjacencies = new Edge[1];
				 * allVertices[curruntIndex].adjacencies[0] = new Edge(
				 * allVertices[afterIndex], afterDistance); }
				 */

				for (int i = 1; i < way.getNodes().size() - 1; i++) {
					flag = false;

					curruntNodeId = way.getNodes().get(i);
					beforeNodeId = way.getNodes().get(i - 1);
					afterNodeId = way.getNodes().get(i + 1);
					beforeDistance = (CalculateDistance(
							nodes.get(curruntNodeId), nodes.get(beforeNodeId)));
					afterDistance = (CalculateDistance(
							nodes.get(curruntNodeId), nodes.get(afterNodeId)));

					/**
					 * New Implementation
					 */
					Vertex currentVertex = hashMap.get(curruntNodeId);
					Vertex afterVertex = hashMap.get(afterNodeId);
					Vertex beforeVertex = hashMap.get(beforeNodeId);

					Edge[] tempCurrentVertexEdges = currentVertex.adjacencies;
					currentVertex.adjacencies = new Edge[tempCurrentVertexEdges.length + 2];
					for (int k = 0; k < tempCurrentVertexEdges.length; k++) {
						currentVertex.adjacencies[k] = tempCurrentVertexEdges[k];
					}
					currentVertex.adjacencies[tempCurrentVertexEdges.length] = new Edge(
							afterVertex, afterDistance);
					currentVertex.adjacencies[tempCurrentVertexEdges.length + 1] = new Edge(
							beforeVertex, beforeDistance);

					long[] tempCurrentVertexWays = currentVertex.ways;
					currentVertex.ways = new long[tempCurrentVertexWays.length + 1];
					for (int k = 0; k < tempCurrentVertexWays.length; k++) {
						currentVertex.ways[k] = tempCurrentVertexWays[k];
					}
					currentVertex.ways[tempCurrentVertexWays.length] = thisWayId;

					/**
					 * End Of This New Thing
					 */

					/*
					 * Previous Impelementation curruntIndex = -1; beforeIndex =
					 * -1; afterIndex = -1;
					 * 
					 * for (int j = 0; j < allVertices.length; j++) { if
					 * (allVertices[j].name.equalsIgnoreCase(curruntNodeId
					 * .toString())) { curruntIndex = j; } if
					 * (allVertices[j].name.equalsIgnoreCase(beforeNodeId
					 * .toString())) { beforeIndex = j;
					 * 
					 * } if (allVertices[j].name.equalsIgnoreCase(afterNodeId
					 * .toString())) { afterIndex = j; } } long[] tempWay2 =
					 * allVertices[curruntIndex].ways;
					 * allVertices[curruntIndex].ways = new long[tempWay2.length
					 * + 1]; for (int k = 0; k < tempWay2.length; k++) {
					 * allVertices[curruntIndex].ways[k] = tempWay2[k]; }
					 * allVertices[curruntIndex].ways[tempWay2.length] =
					 * thisWayId;
					 * 
					 * if (allVertices[curruntIndex].adjacencies != null) {
					 * Edge[] temp = allVertices[curruntIndex].adjacencies;
					 * allVertices[curruntIndex].adjacencies = new
					 * Edge[temp.length + 2]; for (int k = 0; k < temp.length;
					 * k++) { allVertices[curruntIndex].adjacencies[k] =
					 * temp[k]; }
					 * allVertices[curruntIndex].adjacencies[temp.length] = new
					 * Edge( allVertices[beforeIndex], beforeDistance);
					 * allVertices[curruntIndex].adjacencies[temp.length + 1] =
					 * new Edge( allVertices[afterIndex], afterDistance); } else
					 * { allVertices[curruntIndex].adjacencies = new Edge[2];
					 * allVertices[curruntIndex].adjacencies[0] = new Edge(
					 * allVertices[beforeIndex], beforeDistance);
					 * allVertices[curruntIndex].adjacencies[1] = new Edge(
					 * allVertices[afterIndex], afterDistance); }
					 */
				}

			}
		}
		int theOneWeWantStart = 0;
		int theOneWeWantEnd = 0;
		// System.out.println("PLACESSS     " + start + " " + end);
		/*
		 * for (int i = 0; i < allVertices.length; i++) { if
		 * (allVertices[i].latitude.equals(start.getLatitude()) &&
		 * allVertices[i].longitude.equals(start.getLongitude())) {
		 * theOneWeWantStart = i; } if
		 * (allVertices[i].latitude.equals(end.getLatitude()) &&
		 * allVertices[i].longitude.equals(end.getLongitude())) {
		 * theOneWeWantEnd = i; } }
		 */
		// System.out.println("The One We Want Index = " + theOneWeWantStart);

		// computePaths(allVertices[theOneWeWantStart]);
		computePaths(hashMap.get(start.getTime()));

		// System.out.println("Distance to " + hashMap.get(end.getTime())/*
		// * allVertices[
		// * theOneWeWantEnd
		// * ]
		// */
		// + "   Index =   " + theOneWeWantEnd + ": "
		// + hashMap.get(end.getTime()).minDistance /*
		// * allVertices[
		// * theOneWeWantEnd
		// * ].minDistance
		// */);

		List<Vertex> path = getShortestPathTo(hashMap.get(end.getTime())/*
																		 * allVertices
																		 * [
																		 * theOneWeWantEnd
																		 * ]
																		 */);
		// System.out.println("Path: " + path);

		ArrayList<Pathway> pathWays = new ArrayList<Pathway>();
		Long wayId = 0l;
		Long tempwayId = 0l;
		for (int i = 0; i < path.size() - 1; i++) {
			Vertex now = path.get(i);
			Vertex then = path.get(i + 1);

			for (long l1 : now.ways) {
				for (long l2 : then.ways) {
					if (l1 == l2) {
						tempwayId = l1;
					}
				}
			}

			if (wayId.equals(tempwayId)) {
				pathWays.get(pathWays.size() - 1)
						.getNodes()
						.add(new InnerNode(Long.parseLong(then.name),
								then.latitude, then.longitude));
			} else {
				Pathway pathWay = new Pathway();
				pathWay.setWayId(tempwayId);
				wayId = tempwayId;
				pathWays.add(pathWay);
				pathWays.get(pathWays.size() - 1)
						.getNodes()
						.add(new InnerNode(Long.parseLong(now.name),
								now.latitude, now.longitude));
				pathWays.get(pathWays.size() - 1)
						.getNodes()
						.add(new InnerNode(Long.parseLong(then.name),
								then.latitude, then.longitude));
			}
		}

		Queue<Instruction> instructions = new LinkedList<Instruction>();
		Instruction temp = null;
		for (int i = 0; i < pathWays.size() - 1; i++) {

			// Long distance =
			// CalculateDistance(pathWays.get(i).getNodes().get(pathWays.get(i).getNodes().size()
			// - 1),
			// pathWays.get(i).getNodes().get(pathWays.get(i).getNodes().size()
			// - 2));
			// double beforedis = Math.round(distance) * 0.2;
			// System.out.println("In: " + Math.round(beforedis)+ " meters" );

			Double midX = pathWays.get(i).getNodes()
					.get(pathWays.get(i).getNodes().size() - 1).getLongitude() * 1000;
			Double midY = pathWays.get(i).getNodes()
					.get(pathWays.get(i).getNodes().size() - 1).getLatitude() * 1000;

			Double preX = pathWays.get(i).getNodes()
					.get(pathWays.get(i).getNodes().size() - 2).getLongitude() * 1000;
			Double preY = pathWays.get(i).getNodes()
					.get(pathWays.get(i).getNodes().size() - 2).getLatitude() * 1000;

			Double afterX = pathWays.get(i + 1).getNodes().get(1)
					.getLongitude() * 1000;
			Double afterY = pathWays.get(i + 1).getNodes().get(1).getLatitude() * 1000;

			// double mA = (midY - afterY) / (midX - afterX);
			// double mP = (preY - midY) / (preX - midX);

			double mP = (midX - preX) * (afterY - preY);
			double mA = (midY - preY) * (afterX - preX);
			Double res = mP - mA;

			// Double degree = (Math.atan((mP - mA) / (1 + mP * mA)))
			// * (180 / Math.PI);
			// // System.out.println("Degreeee  " + degree);

			if (res < 0) {
				temp = new Instruction(midX / 1000, midY / 1000, "right");
				System.out.println("Degreeee  " + res + " RIGHT first");
			} else if (res > 0) {
				temp = new Instruction(midX / 1000, midY / 1000, "left");
				System.out.println("Degreeee  " + res + " LEFT first");
			}

			if (res.equals(Double.NaN)) {
				afterX += 1;
				afterY += 1;

				// mA = (midY - afterY) / (midX - afterX);
				mA = (midY - preY) * (afterX - preX);
				// res = (Math.atan((mP - mA) / (1 + mP * mA)))
				// * (180 / Math.PI);
				// System.out.println("Degreeee  " + degree);

				res = mP - mA;
				if (res < 0) {
					temp = new Instruction(midX / 1000, midY / 1000, "right");
					System.out.println("Degreeee  " + res + " RIGHT second");

				} else if (res > 0) {
					temp = new Instruction(midX / 1000, midY / 1000, "left");
					System.out.println("Degreeee  " + res + " LEFT second");
				}
			}
			if (temp != null) {
				instructions.add(temp);
				temp = null;
			}
		}

		Long endNodeId;
		Long startNode;
		float endNodeX;
		float endNodeY;
		float startNodeX;
		float startNodeY;

		SVGObject svgObject = new SVGObject();
		String output = "";
		output += "<svg width=\"2000px\" height=\"2000px\" viewBox=\"0 0 2000 2000\">\n";
		for (Pathway pathway : pathWays) {
			ArrayList<Point> points = new ArrayList<Point>();

			for (int ii = 0; ii < pathway.getNodes().size(); ii++) {

				endNodeX = (float) ((pathway.getNodes().get(ii).getLongitude() - boxTop) * 50000);
				endNodeY = (float) ((pathway.getNodes().get(ii).getLatitude() - boxLeft) * 100000);

				points.add(new Point((int) endNodeX, (int) (2000 - endNodeY)));
			}
			String svg = svgObject.getString("route", "path", points);

			if (svg != null) {
				output += svg + "\n";
			}

		}

		output += "</svg>";
		// System.out.println(output);

		InputStream stream = null;

		try {
			stream = new ByteArrayInputStream(output.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		nav = new Navigator(instructions, gpsa, handler2);
		nav.start();
		return stream;
	}

	public static long CalculateDistance(Node innerNode, Node innerNode2) {
		double R = 6371; // km
		double dLat = Math.toRadians((innerNode2.getLatitude() - innerNode
				.getLatitude()));
		double dLon = Math.toRadians(innerNode2.getLongitude()
				- innerNode.getLongitude());
		double lat1 = Math.toRadians(innerNode.getLatitude());
		double lat2 = Math.toRadians(innerNode2.getLatitude());

		double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1)
				* Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));
		double d = R * c;
		return (long) (d * 1000);

	}

	private Double getBoxTop(Double lon) {
		if (lon != null) {
			try {
				lon = (Double) df.parse(df.format(lon));
				int x = (int) ((lon - 11.890) * 1000);
				int d = x / 8;

				Double finalLon = 11.890 + ((int) d * 0.008);

				finalLon = (Double) df.parse(df.format(finalLon));
				return finalLon;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 11.890;
	}

	private Double getBoxLeft(Double lat) {
		if (lat != null) {
			try {
				lat = (Double) df.parse(df.format(lat));
				int x = (int) ((lat - 57.680) * 1000);
				int d = x / 4;

				Double finalLat = 57.680 + ((int) d * 0.004);

				finalLat = (Double) df.parse(df.format(finalLat));
				return finalLat;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 57.684;
	}

	private ArrayList<Location> getAllBoxes(Double top, Double left,
			Double bottom, Double right) {
		ArrayList<Location> locations = new ArrayList<Location>();

		try {
			Double topCounter = top - 0.008;
			topCounter = Double.parseDouble(df.parse(df.format(topCounter))
					.toString());
			Double bottomCounter = bottom + 0.008;
			bottomCounter = Double.parseDouble(df.parse(
					df.format(bottomCounter)).toString());
			Double leftCounter = left - 0.004;
			leftCounter = Double.parseDouble(df.parse(df.format(leftCounter))
					.toString());
			Double rightCounter = right + 0.004;
			rightCounter = Double.parseDouble(df.parse(df.format(rightCounter))
					.toString());

			// System.err.println(topCounter+"    "+bottomCounter+"    "+leftCounter+"    "+rightCounter+""
			// +
			// "\n##############################################");
			while (topCounter < bottomCounter) {
				leftCounter = left - 0.004;
				leftCounter = Double.parseDouble(df.parse(
						df.format(leftCounter)).toString());
				while (leftCounter < rightCounter) {
					// System.out.println(topCounter+"    "+bottomCounter+"    "+leftCounter+"    "+rightCounter);
					Location loc = new Location("HA");
					loc.setLatitude(leftCounter);
					loc.setLongitude(topCounter);
					locations.add(loc);

					leftCounter = leftCounter + 0.004;
					leftCounter = Double.parseDouble(df.parse(
							df.format(leftCounter)).toString());
				}
				topCounter = topCounter + 0.008;
				topCounter = Double.parseDouble(df.parse(df.format(topCounter))
						.toString());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return locations;
	}

	public InputStream getRouteStream() {
		return routeStream;
	}

	public void setRouteStream(InputStream routeStream) {
		this.routeStream = routeStream;
	}

}