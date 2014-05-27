package gps.calculation;

import gps.bean.Node;
import gps.bean.Way;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.location.Location;

public class BlockReader {
	
	public void readForCars(Context con , HashMap<Long, Way> ways , HashMap<Long, Node> nodes , ArrayList<Location> locations) {

		Reader reader = new Reader();
		
		for (int i = 0; i < locations.size(); i++) {

			Double top = locations.get(i).getLongitude();
			Double left = locations.get(i).getLatitude();
			
			String fileName = "x" + top + "." + left;
			fileName = fileName.replace(".", "x");
			fileName += "xzoom";
//			System.out.println(fileName);
			InputStream inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName+"1", "drawable",con.getPackageName()));
			InputStreamReader inputreader = new InputStreamReader(inputStream);
			BufferedReader inputFile = new BufferedReader(inputreader, 1000);

			reader.readForCars(inputFile, ways , nodes);
			
			inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName+"2", "drawable",	con.getPackageName()));
			inputreader = new InputStreamReader(inputStream);
			inputFile = new BufferedReader(inputreader, 1000);
			
			reader.readForCars(inputFile, ways , nodes);
			
			inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName+"3", "drawable",con.getPackageName()));
			inputreader = new InputStreamReader(inputStream);
			inputFile = new BufferedReader(inputreader, 1000);
			
			reader.readForCars(inputFile, ways , nodes);
			
			inputStream = con.getResources().openRawResource(con.getResources().getIdentifier(fileName+"4", "drawable",con.getPackageName()));
			inputreader = new InputStreamReader(inputStream);
			inputFile = new BufferedReader(inputreader, 1000);
			
			reader.readForCars(inputFile, ways , nodes);

		}
		
		
		
		
		
	}

}
