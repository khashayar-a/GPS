package gps.calculation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;

public class SearchZone {

	private ArrayList<String> returnList = new ArrayList<String>();
	private ArrayList<String> stringList = new ArrayList<String>();
	File dir = Environment.getExternalStorageDirectory();
	String[] zone = {"Recallin/NewXmlVastragotaland.txt"};
	private File file;
	
	
	
	public ArrayList<String> readZone(String name) {
		System.out.println("IN READ ZONE");
		for (int i= 1; i<=21; i++){
			try {
				file = new File(dir, zone[0]);
				FileReader location;
				location = new FileReader(file);
				BufferedReader inputFile = new BufferedReader(location,1000);
	
				String currentLine = "";
	
				while (inputFile.ready()) {
	
					currentLine = inputFile.readLine();
					stringList.add(currentLine);
	 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int j = 0; j < stringList.size(); j++) {
				if (stringList.contains(name)) {
					returnList.add((stringList.get(j).substring(10,stringList.size()-29)) + ", " + zone);
							//+ (stringList.get(j).substring(stringList.size()-21,stringList.size())));
					System.out.println((stringList.get(j).substring(10,stringList.size()-29)) + ", " + zone);
				}	
			}
		}
			return returnList;
		}
}