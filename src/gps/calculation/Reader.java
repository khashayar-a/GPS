package gps.calculation;

import gps.bean.Node;
import gps.bean.Way;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {

	private static Double decodeDouble(String s) {
		int count = Integer.parseInt(s.substring(0, 1));

		String l = s.substring(1);

		long basis = 1;

		long total = 0;
		for (int i = l.length() - 1; i > -1; i--) {
			char c = l.charAt(i);
			int t = (int) c;
			t = t - 33;
			total += t * basis;
			basis *= 94;
		}

		return (total / Math.pow(10, count));

	}

	private static Long decodeLong(String s) {

		long basis = 1;

		long total = 0;
		for (int i = s.length() - 1; i > -1; i--) {
			char c = s.charAt(i);
			int t = (int) c;
			t = t - 33;
			total += t * basis;
			basis *= 94;
		}

		return total;
	}

	public void readForCars(BufferedReader inputFile, HashMap<Long, Way> ways , HashMap<Long, Node> nodes) {

		try {
			String currentLine;
			currentLine = inputFile.readLine();

			Long lastWayId = null;
			ArrayList<Long> buildings = new ArrayList<Long>();
			
			while (currentLine != null) {

				String[] temp = currentLine.split(" ");

				if (temp[0].equals("w")) {
					
					
					Way way = new Way();
					lastWayId = decodeLong(temp[1]);
					HashMap<String, String> tags = new HashMap<String , String>();
					if (temp.length > 2) {
						for (int i = 2; i < temp.length; i++) {
							String[] tagPart = temp[i].split("#");
							String key = tagPart[0];
							String value = tagPart[1];
							tags.put(key, value);
						}
						way.setTags(tags);
					}
					ways.put(lastWayId, way);
					if(ways.get(lastWayId).getTags().get("highway") == null){
						buildings.add(lastWayId);
					}
				}

				if (temp[0].equals("d")) { 	

					Node wayNode = new Node();
					Long nodeId = decodeLong(temp[1]);
					wayNode.setLatitude(decodeDouble(temp[2]));
					wayNode.setLongitude(decodeDouble(temp[3]));
					nodes.put(nodeId, wayNode);
					if(ways.get(lastWayId).getTags().get("highway") != null){
						ways.get(lastWayId).getNodes().add(nodeId);
					}
				}

				currentLine = inputFile.readLine();
			}
			
			for (Long l : buildings) {
				ways.remove(l);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void read(BufferedReader inputFile, HashMap<Long, Way> ways , HashMap<Long, Node> nodes) {

		try {
			String currentLine;
			currentLine = inputFile.readLine();

			Long lastWayId = null;
			
			while (currentLine != null) {

				String[] temp = currentLine.split(" ");

				if (temp[0].equals("w")) {
					
					
					Way way = new Way();
					lastWayId = decodeLong(temp[1]);
					HashMap<String, String> tags = new HashMap<String , String>();
					if (temp.length > 2) {
						for (int i = 2; i < temp.length; i++) {
							String[] tagPart = temp[i].split("#");
							String key = tagPart[0];
							String value = tagPart[1];
							tags.put(key, value);
						}
						way.setTags(tags);
					}
					ways.put(lastWayId, way);
				}

				if (temp[0].equals("d")) {

					Node wayNode = new Node();
					Long nodeId = decodeLong(temp[1]);
					wayNode.setLatitude(decodeDouble(temp[2]));
					wayNode.setLongitude(decodeDouble(temp[3]));
					nodes.put(nodeId, wayNode);
					ways.get(lastWayId).getNodes().add(nodeId);
				}

				currentLine = inputFile.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
