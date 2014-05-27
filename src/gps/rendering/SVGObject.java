package gps.rendering;

import java.util.ArrayList;

import android.graphics.Point;

public class SVGObject {
	private String colorIsland = "dedede";

	// border_type/boundary
	private String colorBorder_nation = "00CC99";	
	private String colorBorder_province = "00FFCC";
	private String colorBorder_suburb = colorBorder_province;
	private String colorBorder_city = colorBorder_province;

	// Landuse
	private String colorLanduse_forest = "#C9DFAF";
	private String colorLanduse_meadow = "#C9DFAF";
	private String colorLanduse_recreation_ground = "33FF66";
	private String colorLanduse_residential = "66CC66";
	private String colorLanduse_park = "#C9DFAF";

	// Leisure
	private String colorLeisure_park = "#C9DFAF";
	private String colorLeisure_playground = "#C9DFAF";
	private String colorLeisure_pitch = "#C9DFAF";
	private String colorLeisure_golf_course = "#C9DFAF";

	// Natural
	private String colorNatural_coastline = "#A5BFDD";
	private String colorNatural_water = colorNatural_coastline;
	private String colorNatural_bay = "99FF66";
	private String colorNatural_beach = colorNatural_bay;

	// Highway
	private String colorHighway_road = "FFFFFF";
	private String colorHighway_motorway = "FFFFFF";
	private String colorHighway_motorway_link = "FFFFFF";
	private String colorHighway_cycleway = "FFCCFF";
	private String colorHighway_footway = "CCCCFF";
	private String colorHighway_living_street = "FFFFFF";
	private String colorHighway_pedestrian = "FFFFFF";
	private String colorHighway_residential = "FFFFFF";
	private String colorHighway_construction = "FF9900";
	private String colorHighway_secondary = "FFFFFF";
	private String colorHighway_service = "FFFFFF";
	private String colorHighway_trunk = "#FFFD8B";
	private String colorHighway_trunk_link = colorHighway_trunk;
	private String colorHighway_tertiary = colorHighway_motorway_link;
	private String colorHighway_unclassified = colorHighway_road;
	private String colorHighway_steps = "#FFC345";
	private String colorHighway_path = "000000";

	// Building
	private String colorBuilding = "BEBEBE";

	// Route
	private String colorRoute_ferry = "FFFFFF";
	private String colorRoute_route = "00FFE1d";

	// Amenity
	private String colorAmenity_parking = "737373";

	// Width of highways (lines)
	private int widthHighway_road = 3;
	private int widthHighway_motorway = 7;
	private int widthHighway_motorway_link = widthHighway_motorway;
	private int widthHighway_cycleway = 2;
	private int widthHighway_footway = widthHighway_cycleway;
	private int widthHighway_living_street = 3;
	private int widthHighway_pedestrian = widthHighway_living_street;
	private int widthHighway_residential = widthHighway_living_street;
	private int widthHighway_construction = widthHighway_motorway;
	private int widthHighway_secondary = widthHighway_motorway;
	private int widthHighway_service = widthHighway_road;
	private int widthHighway_trunk = widthHighway_motorway;
	private int widthHighway_trunk_link = widthHighway_motorway;
	private int widthHighway_tertiary = widthHighway_motorway;
	private int widthHighway_unclassified = widthHighway_road;
	private int widthHighway_steps = widthHighway_living_street;
	private int widthHighway_path = widthHighway_cycleway;

	// Width of routes
	private int widthRoute_ferry = widthHighway_road;
	private int widthRoute_route = 1;

	// Width of borders/boundary
	private int widthBorder_nation = 1;
	private int widthBorder_province = widthBorder_nation;
	private int widthBorder_city = widthBorder_nation;
	private int widthBorder_suburb = widthBorder_nation;

	private ArrayList<Point> points;
	private String key;
	private String value;

	private String str = "ready";
	private String strPoints = " d=\"M";
	private String strStroke = " stroke=\"#";
	private String strFill = "";
	private String strStrokeWith = " stroke-width=\"";

	public SVGObject() {
	}

	public String getString(String key, String value, ArrayList<Point> points) {
		this.key = key;
		this.value = value;
		this.points = points;
		generateString();
		if( this.str == null )
			return null;
		else if(!this.strStrokeWith.equals(" stroke-width=\""))
			return "<path " + strStroke + strStrokeWith + strPoints + " />";
		else
			return "<path " + strStroke + strFill + strPoints + " />";
	}

	private void generateString() {
		this.str = "ready";
		this.strPoints = " d=\"M";
		this.strStroke = " stroke=\"#";
		this.strFill = "";
		this.strStrokeWith = " stroke-width=\"";
		
		this.strPoints += Integer.toString(this.points.get(0).x) + " "
				+ Integer.toString(this.points.get(0).y) + " ";
		for (int i = 1; i < this.points.size(); i++) {
			this.strPoints += " L"+Integer.toString(this.points.get(i).x) + " "
					+ Integer.toString(this.points.get(i).y) + " ";
		}
		this.strPoints += "\"";

		if (this.key.equals("island") && this.value.equals("yes")) {
			this.strStroke += colorIsland + "\"";
			this.strFill = " fill=\"#" + colorIsland + "\"";
			return;
		}
		
		// Border
		else if (this.key.equals("border") && this.value.equals("nation")) {
			this.strStroke += colorBorder_nation + "\"";
			this.strStrokeWith += widthBorder_nation + "\"";
			return;
		} else if (this.key.equals("border") && this.value.equals("province")) {
			this.strStroke += colorBorder_province + "\"";
			this.strStrokeWith += widthBorder_province + "\"";
			return;
		} else if (this.key.equals("border") && this.value.equals("suburb")) {
			this.strStroke += colorBorder_suburb + "\"";
			this.strStrokeWith += widthBorder_suburb + "\"";
			return;
		} else if (this.key.equals("border") && this.value.equals("city")) {
			this.strStroke += colorBorder_city + "\"";
			this.strStrokeWith += widthBorder_city + "\"";
			return;
		}

		// Landuse
		else if (this.key.equals("landuse") && this.value.equals("forest")) {
			this.strStroke += colorLanduse_forest + "\"";
			this.strFill = " fill=\"#" + colorLanduse_forest + "\"";
			return;
		} else if (this.key.equals("landuse") && this.value.equals("meadow")) {
			this.strStroke += colorLanduse_meadow + "\"";
			this.strFill = " fill=\"#" + colorLanduse_meadow + "\"";
			return;
		} else if (this.key.equals("landuse")
				&& this.value.equals("recreation_ground")) {
			this.strStroke += colorLanduse_recreation_ground + "\"";
			this.strFill = " fill=\"#" + colorLanduse_recreation_ground + "\"";
			return;
		} else if (this.key.equals("landuse")
				&& this.value.equals("residential")) {
			this.strStroke += colorLanduse_residential + "\"";
			this.strFill = " fill=\"#" + colorLanduse_residential + "\"";
			return;
		} else if (this.key.equals("landuse") && this.value.equals("park")) {
			this.strStroke += colorLanduse_park + "\"";
			this.strFill = " fill=\"#" + colorLanduse_park + "\"";
			return;
		}

		// Leisure
		else if (this.key.equals("leisure") && this.value.equals("park")) {
			this.strStroke += colorLeisure_park + "\"";
			this.strFill = " fill=\"#" + colorLeisure_park + "\"";
			return;
		} else if (this.key.equals("leisure")
				&& this.value.equals("playground")) {
			this.strStroke += colorLeisure_playground + "\"";
			this.strFill = " fill=\"#" + colorLeisure_playground + "\"";
			return;
		} else if (this.key.equals("leisure") && this.value.equals("pitch")) {
			this.strStroke += colorLeisure_pitch + "\"";
			this.strFill = " fill=\"#" + colorLeisure_pitch + "\"";
			return;
		} else if (this.key.equals("leisure")
				&& this.value.equals("golf_course")) {
			this.strStroke += colorLeisure_golf_course + "\"";
			this.strFill = " fill=\"#" + colorLeisure_golf_course + "\"";
			return;
		}

		// Natural
		else if (this.key.equals("natural") && this.value.equals("coastline")) {
			this.strStroke += colorNatural_coastline + "\"";
			this.strFill = " fill=\"#" + colorNatural_coastline + "\"";
			return;
		} else if (this.key.equals("natural") && this.value.equals("water")) {
			this.strStroke += colorNatural_water + "\"";
			this.strFill = " fill=\"#" + colorNatural_water + "\"";
			return;
		} else if (this.key.equals("natural") && this.value.equals("bay")) {
			this.strStroke += colorNatural_bay + "\"";
			this.strFill = " fill=\"#" + colorNatural_bay + "\"";
			return;
		} else if (this.key.equals("natural") && this.value.equals("beach")) {
			this.strStroke += colorNatural_beach + "\"";
			this.strFill = " fill=\"#" + colorNatural_beach + "\"";
			return;
		}

		// Highway
		else if (this.key.equals("highway") && this.value.equals("road")) {
			this.strStroke += colorHighway_road + "\"";
			this.strStrokeWith += widthHighway_road + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("motorway")) {
			this.strStroke += colorHighway_motorway + "\"";
			this.strStrokeWith += widthHighway_motorway + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("motorway_link")) {
			this.strStroke += colorHighway_motorway_link + "\"";
			this.strStrokeWith += widthHighway_motorway_link + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("cycleway")) {
			this.strStroke += colorHighway_cycleway + "\"";
			this.strStrokeWith += widthHighway_cycleway + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("footway")) {
			this.strStroke += colorHighway_footway + "\"";
			this.strStrokeWith += widthHighway_footway + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("living_street")) {
			this.strStroke += colorHighway_living_street + "\"";
			this.strStrokeWith += widthHighway_living_street + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("pedestrian")) {
			this.strStroke += colorHighway_pedestrian + "\"";
			this.strStrokeWith += widthHighway_pedestrian + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("residential")) {
			this.strStroke += colorHighway_residential + "\"";
			this.strStrokeWith += widthHighway_residential + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("construction")) {
			this.strStroke += colorHighway_construction + "\"";
			this.strStrokeWith += widthHighway_construction + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("secondary")) {
			this.strStroke += colorHighway_secondary + "\"";
			this.strStrokeWith += widthHighway_secondary + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("service")) {
			this.strStroke += colorHighway_service + "\"";
			this.strStrokeWith += widthHighway_service + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("trunk")) {
			this.strStroke += colorHighway_trunk + "\"";
			this.strStrokeWith += widthHighway_trunk + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("trunk_link")) {
			this.strStroke += colorHighway_trunk_link + "\"";
			this.strStrokeWith += widthHighway_trunk_link + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("tertiary")) {
			this.strStroke += colorHighway_tertiary + "\"";
			this.strStrokeWith += widthHighway_tertiary + "\"";
			return;
		} else if (this.key.equals("highway")
				&& this.value.equals("unclassified")) {
			this.strStroke += colorHighway_unclassified + "\"";
			this.strStrokeWith += widthHighway_unclassified + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("steps")) {
			this.strStroke += colorHighway_steps + "\"";
			this.strStrokeWith += widthHighway_steps + "\"";
			return;
		} else if (this.key.equals("highway") && this.value.equals("path")) {
			this.strStroke += colorHighway_path + "\"";
			this.strStrokeWith += widthHighway_path + "\"";
			return;
		}

		// Building
		else if (this.key.equals("building") && this.value.equals("yes")) {
			this.strStroke += colorBuilding + "\"";
			this.strFill = " fill=\"#" + colorBuilding + "\"";
			return;
		}

		// Route
		else if (this.key.equals("route") && this.value.equals("ferry")) {
			this.strStroke += colorRoute_ferry + "\"";
			this.strStrokeWith += widthRoute_ferry + "\"";
			return;
		}
		
		else if (this.key.equals("route") && this.value.equals("path")) {
			this.strStroke += colorRoute_route + "\"";
			this.strStrokeWith += widthRoute_route + "\"";
			return;
		}

		// Amenity
		else if (this.key.equals("amenity") && this.value.equals("parking")) {
			this.strStroke += colorAmenity_parking + "\"";
			this.strFill = " fill=\"#" + colorAmenity_parking + "\"";
			return;
		}

		this.str = null;
	}
}
