package gps.calculation;


public class CalculateDistance {

	//ArrayList<Way> ways = new ArrayList<Way>();
	
	public double findDistance(double x2, double x1, double y2, double y1){
		
		double distance = Math.pow((Math.pow((x2 - x1),2) + Math.pow((y2 - y1),2)), 2);
				
		return distance;
	}
	/*
	public double calculateTime(ArrayList<Way>ways){
		
		double x1;
		double x2;
		double y1;
		double y2;
		int speed = 0;
		
		double distance;
		double time = 0;
		
		FindType findtype = new FindType();
		
		for (int i = 0; i < ways.size(); i++){
			
			x1 = ways.get(i).getNodes().get(i).getLatitude();
			y1 = ways.get(i).getNodes().get(i).getLongitude();
			x2 = ways.get(i+1).getNodes().get(i+1).getLatitude();
			y2 = ways.get(i+1).getNodes().get(i+1).getLatitude();
			
			distance = findDistance(x2, x1, y2, y1);	
			
			String key = ways.get(i).getTags().get(i).getKey();
			String value = ways.get(i).getTags().get(i).getValue();
			
			String type = findtype.getType(key, value);
			
			if (type.equals("motorway"))
				speed = 120;
			else if (type.equals("cycleway"))
				speed = 10;
			else if (type.equals("footway"))
				speed = 3;
			else if (type.equals("living_street"))
				speed = 50;
			else if (type.equals("Pedestrian"))
				speed = 0;
			else if (type.equals("Residential"))
				speed = 70;
			else if (type.equals("secondary"))
				speed = 90;
			
			 time += distance/speed;

		}
		
		return time;
	}
	*/
}
