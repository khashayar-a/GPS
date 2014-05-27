package gps.rendering;

import java.util.ArrayList;

public class FindType {
	private ArrayList<MapType> types = new ArrayList<MapType>();
	
	public FindType() {
		
		// border_type/boundary
		/**
		 * Layer 1
		 */
		types.add(new MapType("border_type","nation","border_nation", false, 0));
		types.add(new MapType("border_type","province","border_province", false, 0));
		types.add(new MapType("natural","coastline","natural_coastline", true, 0));
		types.add(new MapType("natural","water","natural_water", true, 0));



		/**
		 * Layer 2
		 */
		types.add(new MapType("highway","motorway","highway_motorway", false, 1));
		types.add(new MapType("highway","motorway_link","highway_motorway_link", false, 1));
		types.add(new MapType("landuse","forest","landuse_forest", true, 1));
		types.add(new MapType("place","suburb","place_suburb", false, 1));
		types.add(new MapType("boundary","city","border_city", false, 1));
		types.add(new MapType("landuse","city","landuse_city", true, 1));
		types.add(new MapType("highway","tertiary","highway_tertiary", false, 1));
		types.add(new MapType("highway","secondary","highway_secondary", false, 1));
		types.add(new MapType("highway","residential","highway_residential", false, 1));

		/**
		 * Layer 3
		 */
		types.add(new MapType("building","yes","building", true, 2));
		types.add(new MapType("amenity","place_of_worship","building_place_of_worship", true, 2));
		types.add(new MapType("highway","living_street","highway_living_street", false, 2));
		types.add(new MapType("natural","bay","natural_bay", true, 2));
		types.add(new MapType("natural","beach","natural_beach", true, 2));
		types.add(new MapType("route","ferry","route_ferry", false, 2));
		types.add(new MapType("highway","pedestrian","highway_pedestrian", false, 2));
		types.add(new MapType("highway","unclassified","highway_unclassified", false, 2));
		types.add(new MapType("highway","trunk","highway_trunk", false, 2));
		types.add(new MapType("highway","trunk_link","highway_trunk_link", false, 2));
		types.add(new MapType("railway","rail","railway_rail", true, 2));
		types.add(new MapType("railway","platform","railway_platform", true, 2));
		types.add(new MapType("amenity","parking","amenity_parking", true, 2));
		types.add(new MapType("amenity","bus_station","amenity_bus_station", true, 2));
		types.add(new MapType("landuse","construction","landuse_construction", true, 2));
		types.add(new MapType("leisure","golf_course","leisure_golf_course", true, 2));
		types.add(new MapType("leisure","park","leisure_park", true, 2));
		types.add(new MapType("leisure","playground","leisure_playground", true, 2));
		types.add(new MapType("leisure","pitch","leisure_pitch", true, 2));

		/**
		 * Layer 4
		 */
		types.add(new MapType("highway","cycleway","highway_cycleway", false, 3));
		types.add(new MapType("highway","footway","highway_footway", false, 3));
		types.add(new MapType("highway","service","highway_service", false, 3));
		types.add(new MapType("highway","steps","highway_steps", false, 3));
		types.add(new MapType("highway","path","highway_path", false, 3));
		types.add(new MapType("highway","road","highway_road", false, 3));


		/**
		 * Layer 5
		 */
		types.add(new MapType("amenity","bicycle_parking","amenity_bicycle_parking", true, 4));
		types.add(new MapType("public_transport","platform","public_transport_platform", true, 4));


		/**
		 * rest
		 */
		types.add(new MapType("landuse","meadow","landuse_meadow", true, 2));
		types.add(new MapType("landuse","recreation_ground","landuse_recreation_ground", true, 2));
		types.add(new MapType("landuse","residential","landuse_residential", true, 2));
		types.add(new MapType("landuse","park","landuse_park", true, 2));

//		types.add(new MapType("amenity","atm","amenity_atm", true, -1));
//		types.add(new MapType("amenity","bank","amenity_bank", true, -1));
//		types.add(new MapType("amenity","cafe","amenity_cafe", true, -1));
//		types.add(new MapType("amenity","fuel","amenity_fuel", true, -1));
//		types.add(new MapType("amenity","library","amenity_library", true, -1));
//		types.add(new MapType("amenity","pub","amenity_pub", true, -1));
//		types.add(new MapType("amenity","restaurant","amenity_restaurant", true, -1));
//		types.add(new MapType("amenity","school","amenity_school", true, -1));
//		types.add(new MapType("amenity","taxi","amenity_taxi", true, -1));
//		types.add(new MapType("amenity","university","amenity_university", true, -1));

	}
	public String getType(String key,String value) {
		for (int i = 0; i < this.types.size() ; i++) {
			if(this.types.get(i).isType(key, value)){
				return this.types.get(i).getType();
			}
		}
		return null;
	}
	public boolean isPath(String type) {
		for (int i = 0; i < this.types.size() ; i++) {
			if(this.types.get(i).getType().equals(type) && this.types.get(i).isPath()){
				return true;
			}
		}
		return false;
	}
	public int getLoop(String type) {
		for (int i = 0; i < this.types.size() ; i++) {
			if(this.types.get(i).getType().equals(type)){
				return this.types.get(i).getLoop();
			}
		}
		return -1;
	}

}
