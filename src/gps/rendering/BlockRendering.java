package gps.rendering;

import gps.bean.Node;
import gps.bean.Way;
import gps.calculation.Reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AndroidException;

public class BlockRendering implements Runnable {

	private Thread parent;
	@SuppressWarnings("unused")
	private DrawableImage drawableImage;
	private Context con;
	private Double top;
	private Double left;
	private Double boxTop;
	private Double boxLeft;
	private Drawable drawable;
	private InputStream mapStream;
	private MapView mapView;
	private Integer zoom;
	private Integer size;

	public void run() {
		mapView.setMapStreamString(createMap(boxTop , boxLeft , top, left , zoom , size));
		synchronized (parent) {
			parent.notify();
		}
	}

	public Drawable getBlocks() {
		return drawable;
	}

	public BlockRendering(Object[] o) {
		top = (Double) o[0];
		left = (Double) o[1];
		con = (Context) o[2];
		mapView = (MapView) o[3];
		zoom = (Integer) o[4];
		boxTop = (Double) o[5];
		boxLeft = (Double) o[6];
		size = (Integer) o[7];
	}

	public void setParent(Thread parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unused")
	public String[] read(Double boxTop, Double boxLeft, Double top, Double left, Integer zoom , Integer size) {

		Reader reader = new Reader();

		InputStream inputStream;
		InputStreamReader inputreader;
		BufferedReader inputFile;

		Double tempTop = top;
		Double tempLeft = left;

		// String fileName = "x" + top + "." + left;
		// fileName = fileName.replace(".", "x");
		// fileName += "xzoom";
		// System.out.println(fileName);

		DecimalFormat df = new DecimalFormat("###.###");

		if (zoom == 1) {
			HashMap<Long, Node> nodes = new HashMap<Long, Node>();
			HashMap<Long, Way> ways = new HashMap<Long, Way>();

			String fileName = "x" + top + "." + left;
			fileName = fileName.replace(".", "x");
			fileName += "xzoom1";

			try {
				
				inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName, "drawable",	con.getPackageName()));
				inputreader = new InputStreamReader(inputStream);
				inputFile = new BufferedReader(inputreader,1000);

				reader.read(inputFile, ways, nodes);
			} catch (Exception e) {
				System.err.println("File Not Found : " + fileName);
			}
						
			
			String[] stream = convertInfoToMeaningfullMapInfo(nodes, ways,
					boxTop, boxLeft, zoom , size);
			
			return stream;
		}

		if (zoom == 2) {
			HashMap<Long, Node> nodes = new HashMap<Long, Node>();
			HashMap<Long, Way> ways = new HashMap<Long, Way>();
			
			String fileName = "x" + top + "." + left;
			fileName = fileName.replace(".", "x");
			fileName += "xzoom2";
			try{
				inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName, "drawable",	con.getPackageName()));
				inputreader = new InputStreamReader(inputStream);
				inputFile = new BufferedReader(inputreader,1000);
				reader.read(inputFile, ways, nodes);
			} catch (Exception e) {
				System.err.println("File Not Found : " + fileName);
			}
			
			
			String[] stream = convertInfoToMeaningfullMapInfo(nodes, ways,
					boxTop, boxLeft, zoom , size);
			
			return stream;
		}
		
		if (zoom == 3) {
			HashMap<Long, Node> nodes = new HashMap<Long, Node>();
			HashMap<Long, Way> ways = new HashMap<Long, Way>();

			String fileName = "x" + top + "." + left;
			fileName = fileName.replace(".", "x");
			fileName += "xzoom3";
			try{
				inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName, "drawable",	con.getPackageName()));
				inputreader = new InputStreamReader(inputStream);
				inputFile = new BufferedReader(inputreader,1000);
				reader.read(inputFile, ways, nodes);
			} catch (Exception e) {
				System.err.println("File Not Found : " + fileName);
			}
			
			
			String[] stream = convertInfoToMeaningfullMapInfo(nodes, ways,
					boxTop, boxLeft, zoom , size);
			
			return stream;
		}

		if (zoom == 4) {
			HashMap<Long, Node> nodes = new HashMap<Long, Node>();
			HashMap<Long, Way> ways = new HashMap<Long, Way>();
			
			String fileName = "x" + top + "." + left;
			fileName = fileName.replace(".", "x");
			fileName += "xzoom4";

			try{
				inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName, "drawable",	con.getPackageName()));
				inputreader = new InputStreamReader(inputStream);
				inputFile = new BufferedReader(inputreader,1000);
				reader.read(inputFile, ways, nodes);
			} catch (Exception e) {
				System.err.println("File Not Found : " + fileName);
			}
			
			
			String[] stream = convertInfoToMeaningfullMapInfo(nodes, ways,
					boxTop, boxLeft, zoom , size);
			
			return stream;
		}
		
		return null;

	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public String[] convertInfoToMeaningfullMapInfo(HashMap<Long, Node> nodes, HashMap<Long, Way> ways, Double boxTop, Double boxLeft, int zoom , int size) {

		FindType findType = new FindType();
		SVGObject svgObject = new SVGObject();
		Long endNodeId = 0l;
		Long startNodeId = 0l;

		float endNodeX = 0f;
		float endNodeY = 0f;
		float startNodeX = 0f;
		float startNodeY = 0f;

		boolean water;
		boolean island;
		String type = "road";

		if (zoom == 1) {
			ArrayList<Way> waters = new ArrayList<Way>();

			ArrayList<String> loop1 = new ArrayList<String>();
			ArrayList<String> loop1_5 = new ArrayList<String>();
			ArrayList<String> loop2 = new ArrayList<String>();

			Iterator iterator = ways.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
				Way way = waySet.getValue();
				water = false;
				island = false;
				String key = "";
				String value = "";

				Iterator iteratorTag = way.getTags().entrySet().iterator();
				while (iteratorTag.hasNext()) {
					Entry<String, String> tag = (Entry<String, String>) iteratorTag.next();
					String typeS = findType.getType(tag.getKey(),tag.getValue());

					if (typeS != null) {
						type = typeS;
						key = tag.getKey();
						value = tag.getValue();
					}
				}
				if (type.contains("natural")) {
					waters.add(way);
				} else {
					ArrayList<Point> points = new ArrayList<Point>();
					for (int i = 0; i < way.getNodes().size(); i++) {
						endNodeId = way.getNodes().get(i);
						endNodeX = (float) ((nodes.get(endNodeId)
								.getLongitude() - boxTop) * 50000);
						endNodeY = (float) ((nodes.get(endNodeId).getLatitude() - boxLeft) * 100000);

						points.add(new Point((int) endNodeX,
								(int) ( (size*400) - endNodeY)));
					}
					String svgLine = svgObject.getString(key, value, points);
					if (svgLine != null) {
						loop2.add(svgLine);
					}
				}
			}
			for (Way w : waters) {

				ArrayList<Point> points = new ArrayList<Point>();

				ArrayList<Integer> areas = new ArrayList<Integer>();
				Double midX = nodes.get(w.getNodes().get(0)).getLongitude();
				Double midY = nodes.get(w.getNodes().get(0)).getLatitude();
				areas.add(0);
				for (int i = 1; i < w.getNodes().size() - 1; i++) {

					double difX = nodes.get(w.getNodes().get(i)).getLongitude()
							- midX;
					double difY = nodes.get(w.getNodes().get(i)).getLatitude()
							- midY;

					if (difX > 0 && difY >= 0) {
						if (areas.get(areas.size() - 1) != 1) {
							areas.add(1);
						}
					}
					if (difX > 0 && difY < 0) {
						if (areas.get(areas.size() - 1) != 2) {
							areas.add(2);
						}
					}
					if (difX < 0 && difY >= 0) {
						if (areas.get(areas.size() - 1) != 4) {
							areas.add(4);
						}
					}
					if (difX < 0 && difY < 0) {
						if (areas.get(areas.size() - 1) != 3) {
							areas.add(3);
						}
					}
				}

				areas = clearAreas(areas);

				int x = isClockWise(areas);
				if (x == 1) {

					for (int i = 0; i < w.getNodes().size(); i++) {

						endNodeX = (float) ((nodes.get(w.getNodes().get(i))
								.getLongitude() - boxTop) * 50000);
						endNodeY = (float) ((nodes.get(w.getNodes().get(i))
								.getLatitude() - boxLeft) * 100000);

						Point point = new Point((int) endNodeX,
								(int) ( (size*400) - endNodeY));
						points.add(point);
					}
					String svgLine = svgObject.getString("natural",
							"coastline", points);
					if (svgLine != null) {
						loop1.add(svgLine);
					}
				}
				if (x == 2) {

					for (int i = 0; i < w.getNodes().size(); i++) {
						endNodeX = (float) ((nodes.get(w.getNodes().get(i))
								.getLongitude() - boxTop) * 50000);
						endNodeY = (float) ((nodes.get(w.getNodes().get(i))
								.getLatitude() - boxLeft) * 100000);

						Point point = new Point((int) endNodeX,
								(int) ( (size*400) - endNodeY));
						points.add(point);
					}
					String svgLine = svgObject.getString("island", "yes",
							points);
					if (svgLine != null) {
						loop1_5.add(svgLine);
					}
				}
			}

			String[] outputForInputStream = new String[3] ; //"<svg width=\"400px\" height=\"1600px\" viewBox=\"0 0 1600 1600\">\n";
			outputForInputStream[0] = "";
			outputForInputStream[1] = "";
			outputForInputStream[2] = "";
//			outputForInputStream += "<polyline fill=\"#dedede\" points=\"0,0 0,1600 1600,1600 1600,0\"/>";
			for (String s : loop1) {
				outputForInputStream[0] += s + "\n";
			}
			for (String s : loop1_5) {
				outputForInputStream[1] += s + "\n";
			}
			for (String s : loop2) {
				outputForInputStream[2] += s + "\n";
			}

//			outputForInputStream += "</svg>";
//			InputStream stream = null;
//			stream = new ByteArrayInputStream(outputForInputStream.getBytes());
			return outputForInputStream;

		}

		if (zoom == 2) {
			ArrayList<String> loop2 = new ArrayList<String>();

			Iterator iterator = ways.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
				Way way = waySet.getValue();
				water = false;
				island = false;
				String key = "";
				String value = "";

				Iterator iteratorTag = way.getTags().entrySet().iterator();
				while (iteratorTag.hasNext()) {
					Entry<String, String> tag = (Entry<String, String>) iteratorTag
							.next();
					String typeS = findType.getType(tag.getKey(),
							tag.getValue());

					if (typeS != null) {
						type = typeS;
						key = tag.getKey();
						value = tag.getValue();
					}
				}

				ArrayList<Point> points = new ArrayList<Point>();
				for (int i = 0; i < way.getNodes().size(); i++) {
					endNodeId = way.getNodes().get(i);
					endNodeX = (float) ((nodes.get(endNodeId).getLongitude() - boxTop) * 50000);
					endNodeY = (float) ((nodes.get(endNodeId).getLatitude() - boxLeft) * 100000);
					points.add(new Point((int) endNodeX, (int) ( (size*400) - endNodeY)));
				}

				String svgLine = svgObject.getString(key, value, points);
				if (svgLine != null) {
					loop2.add(svgLine);
				}
			}
			String[] outputForInputStream = new String[1] ; //"<svg width=\"800px\" height=\"800px\" viewBox=\"0 0 800 800\">\n";
			outputForInputStream[0] = "";
//			outputForInputStream += "<polyline fill=\"#dedede\" points=\"0,0 0,800 800,800 800,0\"/>";
			for (String s : loop2) {
				outputForInputStream[0] += s + "\n";
			}

//			outputForInputStream += "</svg>";
//			InputStream stream = null;
//			stream = new ByteArrayInputStream(outputForInputStream.getBytes());
			return outputForInputStream;
		}

		if (zoom == 3) {
			ArrayList<String> loop3 = new ArrayList<String>();

			Iterator iterator = ways.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
				Way way = waySet.getValue();
				water = false;
				island = false;
				String key = "";
				String value = "";

				Iterator iteratorTag = way.getTags().entrySet().iterator();
				while (iteratorTag.hasNext()) {
					Entry<String, String> tag = (Entry<String, String>) iteratorTag
							.next();
					String typeS = findType.getType(tag.getKey(),
							tag.getValue());

					if (typeS != null) {
						type = typeS;
						key = tag.getKey();
						value = tag.getValue();
					}
				}

				ArrayList<Point> points = new ArrayList<Point>();
				for (int i = 0; i < way.getNodes().size(); i++) {
					endNodeId = way.getNodes().get(i);
					endNodeX = (float) ((nodes.get(endNodeId).getLongitude() - boxTop) * 50000);
					endNodeY = (float) ((nodes.get(endNodeId).getLatitude() - boxLeft) * 100000);
					points.add(new Point((int) endNodeX, (int) ( (size*400) - endNodeY)));
				}

				String svgLine = svgObject.getString(key, value, points);
				if (svgLine != null) {
					loop3.add(svgLine);
				}
			}
			String[] outputForInputStream = new String[1];//"<svg width=\"400px\" height=\"400px\" viewBox=\"0 0 400 400\">\n";
			outputForInputStream[0] = "";
//			outputForInputStream += "<polyline fill=\"#dedede\" points=\"0,0 0,400 400,400 400,0\"/>";
			for (String s : loop3) {
				outputForInputStream[0] += s + "\n";
			}

//			outputForInputStream += "</svg>";
//			InputStream stream = null;
//			stream = new ByteArrayInputStream(outputForInputStream.getBytes());
			return outputForInputStream;
		}

		if (zoom == 4) {
			ArrayList<String> loop4 = new ArrayList<String>();

			Iterator iterator = ways.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
				Way way = waySet.getValue();
				water = false;
				island = false;
				String key = "";
				String value = "";

				Iterator iteratorTag = way.getTags().entrySet().iterator();
				while (iteratorTag.hasNext()) {
					Entry<String, String> tag = (Entry<String, String>) iteratorTag
							.next();
					String typeS = findType.getType(tag.getKey(),
							tag.getValue());

					if (typeS != null) {
						type = typeS;
						key = tag.getKey();
						value = tag.getValue();
					}
				}

				ArrayList<Point> points = new ArrayList<Point>();
				for (int i = 0; i < way.getNodes().size(); i++) {
					endNodeId = way.getNodes().get(i);
					endNodeX = (float) ((nodes.get(endNodeId).getLongitude() - boxTop) * 50000);
					endNodeY = (float) ((nodes.get(endNodeId).getLatitude() - boxLeft) * 100000);
					points.add(new Point((int) endNodeX, (int) ( (size*400) - endNodeY)));
				}

				String svgLine = svgObject.getString(key, value, points);
				if (svgLine != null) {
					loop4.add(svgLine);
				}
			}
			String[] outputForInputStream = new String[1];//"<svg width=\"1600px\" height=\"1600px\" viewBox=\"0 0 1600 1600\">\n";
			outputForInputStream[0] = "";
//			outputForInputStream += "<polyline fill=\"#dedede\" points=\"0,0 0,1600 1600,1600 1600,0\"/>";
			for (String s : loop4) {
				outputForInputStream[0] += s + "\n";
			}

//			outputForInputStream += "</svg>";
//			InputStream stream = null;
//			stream = new ByteArrayInputStream(outputForInputStream.getBytes());
			return outputForInputStream;
		}

		if (zoom == 5) {
			ArrayList<String> loop5 = new ArrayList<String>();

			Iterator iterator = ways.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Way> waySet = (Entry<Long, Way>) iterator.next();
				Way way = waySet.getValue();
				water = false;
				island = false;
				String key = "";
				String value = "";

				Iterator iteratorTag = way.getTags().entrySet().iterator();
				while (iteratorTag.hasNext()) {
					Entry<String, String> tag = (Entry<String, String>) iteratorTag
							.next();
					String typeS = findType.getType(tag.getKey(),
							tag.getValue());

					if (typeS != null) {
						type = typeS;
						key = tag.getKey();
						value = tag.getValue();
					}
				}

				ArrayList<Point> points = new ArrayList<Point>();
				for (int i = 0; i < way.getNodes().size(); i++) {
					endNodeId = way.getNodes().get(i);
					endNodeX = (float) ((nodes.get(endNodeId).getLongitude() - boxTop) * 50000);
					endNodeY = (float) ((nodes.get(endNodeId).getLatitude() - boxLeft) * 100000);
					points.add(new Point((int) endNodeX, (int) ( (size*400) - endNodeY)));
				}

				String svgLine = svgObject.getString(key, value, points);
				if (svgLine != null) {
					loop5.add(svgLine);
				}
			}
			String[] outputForInputStream = new String[1];//"<svg width=\"1600px\" height=\"1600px\" viewBox=\"0 0 1600 1600\">\n";
			outputForInputStream[0] = "";
//			outputForInputStream += "<polyline fill=\"#dedede\" points=\"0,0 0,1600 1600,1600 1600,0\"/>";
			for (String s : loop5) {
				outputForInputStream[0] += s + "\n";
			}

//			outputForInputStream += "</svg>";
//			InputStream stream = null;
//			stream = new ByteArrayInputStream(outputForInputStream.getBytes());
			return outputForInputStream;
		}

		return null;
	}

	private ArrayList<Integer> clearAreas(ArrayList<Integer> areas) {

		boolean correct = false;

		while (!correct) {
			correct = true;
			int len = areas.size();
			for (int i = 0; i < len - 3; i++) {
				if (areas.get(i) == areas.get(i + 2)) {
					correct = false;
					areas.remove(i + 2);
					areas.remove(i + 1);
					i = len;
				}
			}
		}

		return areas;
	}

	private int isClockWise(ArrayList<Integer> areas) {

		boolean clockWise = true;
		boolean clockUnWise = true;
		for (int i = 1; i < areas.size() - 1; i++) {
			if (areas.get(i) > areas.get(i + 1)) {
				clockWise = false;
			}
			if (areas.get(i) < areas.get(i + 1)) {
				clockUnWise = false;
			}
		}

		if (clockWise)
			return 1;
		if (clockUnWise)
			return 2;
		return -1;
	}

	private String[] createMap(Double boxTop , Double boxLeft , Double top, Double left , Integer zoom , Integer size) {
		String[] str = read(boxTop , boxLeft , top, left , zoom , size);

/*		InputStreamReader strR = new InputStreamReader(str);
		BufferedReader br = new BufferedReader(strR);
		String read;
		StringWriter sw = null;
		PrintWriter pw;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			read = br.readLine();
			while (read != null) {
//				System.out.println(read);
				pw.println(read);
				read = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] barray = sw.toString().getBytes();
		InputStream is = new ByteArrayInputStream(barray);
			return is;
*/
		return str;
		}

	public InputStream getMapStream() {
		return mapStream;
	}

	public void setMapStream(InputStream mapStream) {
		this.mapStream = mapStream;
	}

}
