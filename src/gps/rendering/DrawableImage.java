package gps.rendering;

import gps.application.R;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

public class DrawableImage {

	private int transp		 						= 150;
	private int colorBackground 					= Color.rgb(220,220,220);

	// border_type/boundary
	private int colorBorder_nation					= Color.rgb(240,120,180);
	private int colorBorder_province				= Color.rgb(240,160,200);
	private int colorBorder_suburb					= colorBorder_province;
	private int colorBorder_city					= colorBorder_province;
	
	// Landuse
	private int colorLanduse_Forest					= Color.argb(transp,70,200,80);
	private int colorLanduse_Meadow					= Color.argb(transp, 150,220,80);
	private int colorLanduse_Recreation_ground		= Color.argb(transp, 110,210,120);
	private int colorLanduse_residential			= Color.argb(transp, 200,240,210);
	private int colorLanduse_park					= colorLanduse_Recreation_ground;

	// Leisure
	private int colorLeisure_park					= colorLanduse_Recreation_ground;
	private int colorLeisure_playground				= colorLanduse_residential;
	private int colorLeisure_pitch					= colorLanduse_Recreation_ground;
	private int colorLeisure_golf_course			= colorLanduse_Recreation_ground;
	
	// Natural
	private int colorNatural_Coastline				= Color.argb(transp, 110,140,250);
	private int colorNatural_Water 					= colorNatural_Coastline;
	private int colorNatural_Bay 					= Color.argb(transp, 220,210,140);
	private int colorNatural_Beach 					= colorNatural_Bay;
	
	// Building
	private int colorBuilding 						= Color.argb(transp, 190,190,190);

	// Highway 
	private int colorHighway_Road 					= Color.rgb(255,255,255);
	private int colorHighway_Motorway 				= Color.rgb(255,255,145);
	private int colorHighway_Motorway_Link 			= Color.rgb(255,255,180);
	private int colorHighway_Cycleway 				= Color.rgb(245,245,245);
	private int colorHighway_Footway 				= Color.rgb(235,235,235);
	private int colorHighway_Living_street 			= Color.rgb(240,150,80);
	private int colorHighwayPedestrian	 			= Color.rgb(240,180,140);
	private int colorHighway_Residential 			= Color.rgb(250,200,180);
	private int colorHighway_Construction 			= Color.rgb(150,0,0);
	private int colorHighway_Secondary	 			= colorHighway_Motorway_Link;
	private int colorHighway_Service	 			= colorHighway_Road;
	private int colorHighway_Trunk		 			= Color.rgb(240,120,180);
	private int colorHighway_Trunk_Link 			= colorHighway_Trunk;
	private int colorHighway_Tertiary	 			= colorHighway_Motorway_Link;
	private int colorHighway_Unclassified 			= colorHighway_Road;
	private int colorHighway_Steps		 			= Color.rgb(255,190,0);
	private int colorHighway_Path		 			= Color.rgb(0,0,0);
	
	// Route
	private int colorRouteFerry						= colorHighway_Road;
	
	// Amenity
	private int colorAmenityParking					= Color.argb(transp, 40, 40, 40);
	

	// Width of highways (lines)
	private int widthHighway_Road 					= 3;
	private int widthHighway_Motorway 				= 7;
	private int widthHighway_Motorway_Link 			= widthHighway_Motorway;
	private int widthHighway_Cycleway 				= 2;
	private int widthHighway_Footway 				= widthHighway_Cycleway;
	private int widthHighway_Living_street			= 3;
	private int widthHighway_Pedestrian				= widthHighway_Living_street;
	private int widthHighway_Residential			= widthHighway_Living_street;
	private int widthHighway_Construction			= widthHighway_Motorway;
	private int widthHighway_Secondary				= widthHighway_Motorway;
	private int widthHighway_Service				= widthHighway_Road;
	private int widthHighway_Trunk					= widthHighway_Motorway;
	private int widthHighway_Trunk_Link				= widthHighway_Motorway;
	private int widthHighway_Tertiary				= widthHighway_Motorway;
	private int widthHighway_Unclassified			= widthHighway_Road;
	private int widthHighway_Steps					= widthHighway_Living_street;
	private int widthHighway_Path					= widthHighway_Cycleway;

	// Width of routes
	private int widthRoute_Ferry					= widthHighway_Road;
	
	
	// Width of borders/boundary
	private int widthBorder_nation					= 1;
	private int widthBorder_province				= widthBorder_nation;
	private int widthBorder_city					= widthBorder_nation;
	private int widthBorder_suburb					= widthBorder_nation;

	private Drawable draw;

	public DrawableImage(Resources resource,ArrayList<MapObject> waters,ArrayList<MapObject> islands,ArrayList<MapObject> parks,ArrayList<MapObject> roadsAndHouses,ArrayList<MapObject> borders,ArrayList<MapObject> icons) {

		final Resources res = resource;
		final ArrayList<MapObject> wa = waters;
		final ArrayList<MapObject> is = islands;
		final ArrayList<MapObject> pa = parks;
		final ArrayList<MapObject> ro = roadsAndHouses;
		final ArrayList<MapObject> bo = borders;
		final ArrayList<MapObject> ic = icons;
		draw = new Drawable() {

			public void setColorFilter(ColorFilter cf) {
			}
			public void setAlpha(int alpha) {
			}
			public int getOpacity() {
				return 0;
			}
			public void draw(Canvas canvas) {
				canvas.drawColor(colorBackground);

				// Waters
				for (int i = 0; i < wa.size(); i++) {
					MapObject object = wa.get(i);
					String type = object.getType();
					
					// Natural - Coastline
					if (type.equals("natural_coastline"))
						drawPath(canvas,object.getPoints(),colorNatural_Coastline);
					// Natural - Water
					else if (type.equals("natural_water"))
						drawPath(canvas,object.getPoints(),colorNatural_Water);
					
				}
				
				// Islands
				for (int i = 0; i < is.size(); i++) {
					MapObject object = is.get(i);
					String type = object.getType();
					
					// K - Island
					if (type.equals("island"))
						drawPath(canvas,object.getPoints(),colorBackground);
				}
				
				// Parks
				for (int i = 0; i < pa.size(); i++) {
					MapObject object = pa.get(i);
					String type = object.getType();
									
					// Natural - Bay
					if (type.equals("natural_bay"))
						drawPath(canvas,object.getPoints(),colorNatural_Bay);
					// Natural - Beach
					else if (type.equals("natural_beach"))
						drawPath(canvas,object.getPoints(),colorNatural_Beach);
					// Landuse - Forest
					else if (type.equals("landuse_forest"))
						drawPath(canvas,object.getPoints(),colorLanduse_Forest);
					// Landuse - Meadow
					else if (type.equals("landuse_meadow"))
						drawPath(canvas,object.getPoints(),colorLanduse_Meadow);
					// Landuse - recreation_ground
					else if (type.equals("landuse_recreation_ground"))
						drawPath(canvas,object.getPoints(),colorLanduse_Recreation_ground);
					// Landuse - Landuse_residential
					else if (type.equals("landuse_residential"))
						drawPath(canvas,object.getPoints(),colorLanduse_residential);
					// Landuse - Landuse_park
					else if (type.equals("landuse_park"))
						drawPath(canvas,object.getPoints(),colorLanduse_park);

					// Leisure - Leisure_pitch
					else if (type.equals("leisure_pitch"))
						drawPath(canvas,object.getPoints(),colorLeisure_pitch);
					// Leisure - Park
					else if (type.equals("leisure_park"))
						drawPath(canvas,object.getPoints(),colorLeisure_park);
					// Leisure - Playground
					else if (type.equals("leisure_playground"))
						drawPath(canvas,object.getPoints(),colorLeisure_playground);
					// Leisure - Golf Courese
					else if (type.equals("leisure_golf_course"))
						drawPath(canvas,object.getPoints(),colorLeisure_golf_course);
				}
								
				// Roads and Houses
				for (int i = 0; i < ro.size(); i++) {
					MapObject object = ro.get(i);
					String type = object.getType();
					
					// Route - Ferry
					if (type.equals("route_ferry"))
						drawDashedLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthRoute_Ferry,colorRouteFerry,10,20);
					
					// Amenity - Parking
					else if (type.equals("amenity_parking"))
						drawPath(canvas,object.getPoints(),colorAmenityParking);
					
					// Building - Yes
					else if (type.equals("building"))
						drawPath(canvas,object.getPoints(),colorBuilding);
					// Building - Place of worship
					else if (type.equals("building_place_of_worship"))
						drawPath(canvas,object.getPoints(),colorBuilding);
					
					// Highway - Trunk
					else if (type.equals("highway_trunk"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Trunk,colorHighway_Trunk);
					// Highway - Trunk_Link
					else if (type.equals("highway_trunk_link"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Trunk_Link,colorHighway_Trunk_Link);
					// Highway - Motorway
					else if (type.equals("highway_motorway"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Motorway,colorHighway_Motorway);
					// Highway - Motorway_Link
					else if (type.equals("highway_motorway_link"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Motorway_Link,colorHighway_Motorway_Link);
					// Highway - Cycleway
					else if (type.equals("highway_cycleway"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Cycleway,colorHighway_Cycleway);
					// Highway - Footway
					else if (type.equals("highway_footway"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Footway,colorHighway_Footway);
					// Highway - Living_street
					else if (type.equals("highway_living_street"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Living_street,colorHighway_Living_street);
					// Highway - Pedestrian
					else if (type.equals("highway_pedestrian"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Pedestrian,colorHighwayPedestrian);
					// Highway - Residential
					else if (type.equals("highway_residential"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Residential,colorHighway_Residential);
					// Highway - Residential
					else if (type.equals("highway_construction"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Construction,colorHighway_Construction);
					// Highway - Road
					else if (type.equals("highway_road"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Road,colorHighway_Road);
					// Highway - Secondary
					else if (type.equals("highway_secondary"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Secondary,colorHighway_Secondary);
					// Highway - Service
					else if (type.equals("highway_service"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Service,colorHighway_Service);
					// Highway - Tertiary
					else if (type.equals("highway_tertiary"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Tertiary,colorHighway_Tertiary);
					// Highway - Unclassified
					else if (type.equals("highway_unclassified"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Unclassified,colorHighway_Unclassified);
					// Highway - Steps
					else if (type.equals("highway_steps"))
						drawDashedLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Steps,colorHighway_Steps,1,2);
					// Highway - Path
					else if (type.equals("highway_path"))
						drawDashedLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthHighway_Path,colorHighway_Path,2,4);
				}
				
				// Borders
				for (int i = 0; i < bo.size(); i++) {
					MapObject object = bo.get(i);
					String type = object.getType();
					
					// border_type - nation
					if (type.equals("border_nation"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthBorder_nation,colorBorder_nation);
					// border_type - city
					else if (type.equals("border_city"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthBorder_city,colorBorder_city);
					// border_type - nation
					else if (type.equals("border_suburb"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthBorder_suburb,colorBorder_suburb);
					// border_type - nation
					else if (type.equals("border_province"))
						drawLine(object.getName(),canvas,object.getPreX(), object.getPreY(),object.getCurX(), object.getCurY(),widthBorder_province,colorBorder_province);
				}
				
				// Icons
				for (int i = 0; i < ic.size(); i++) {
					MapObject object = ic.get(i);
					String type = object.getType();
					
					Paint paint = new Paint();
					Bitmap bmp = null;
					
					if( type.equals("amenity_atm") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_atm);
					else if( type.equals("amenity_bank") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_bank);
					else if( type.equals("amenity_cafe") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_cafe);
					else if( type.equals("amenity_fuel") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_fuel);
					else if( type.equals("amenity_library") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_library);
					else if( type.equals("amenity_restaurant") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_restaurant);
					else if( type.equals("amenity_parking") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_parking);
					else if( type.equals("amenity_pub") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_pub);
					else if( type.equals("amenity_school") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_school);
					else if( type.equals("amenity_taxi") )
						bmp = BitmapFactory.decodeResource(res, R.drawable.icon_taxi);
					else if( type.equals("amenity_university") )
					bmp = BitmapFactory.decodeResource(res, R.drawable.icon_university);
				
					canvas.drawBitmap(bmp, object.getCurX(), object.getCurY(), paint);
				}
			}
		};

	}
	
	private void drawLine(String name,Canvas canvas,float preX,float preY,float curX,float curY,float strokeWidth,int color) {
		Paint paint = new Paint();
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);
		canvas.drawLine(preX, preY, curX, curY, paint);
		canvas.drawCircle(preX, preY, strokeWidth/2, paint);
		canvas.drawCircle(curX, curY, strokeWidth/2, paint);
		
//		if( Math.abs(preX-curX) > 40 || Math.abs(preY-curY) > 40 ) {
//			Path path = new Path();
//			if( curY > preY ) {
//				path.moveTo(curX, curY);
//				path.lineTo(preX,preY);
//			}
//			else {
//				path.moveTo(preX,preY);
//				path.lineTo(curX, curY);
//			}
//			
//			paint.setColor(Color.BLACK);
//			paint.setTextSize(strokeWidth * (30/23));
//			canvas.drawTextOnPath(name,path,0,0,paint);
//		}
	}
	
	private void drawDashedLine(String name,Canvas canvas,float preX,float preY,float curX,float curY,float strokeWidth,int color, int d1, int d2) {
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);
		paint.setPathEffect(new DashPathEffect(new float[] {d1,d2}, 0));
		Path path = new Path();
		path.moveTo(preX, preY);
		path.lineTo(curX, curY);
		canvas.drawPath(path, paint);
	}
	
	private void drawPath(Canvas canvas,ArrayList<Point> points,int color) {
		Paint paint = new Paint();
		paint.setColor(color);
		Path path = new Path();
		for( int i = 0 ; i < points.size() ; i++ ) {
			if( i == 0 )
				path.moveTo(points.get(i).x, points.get(i).y);
			else
				path.lineTo(points.get(i).x, points.get(i).y);
		}
		canvas.drawPath(path, paint);
	}
	
	@SuppressWarnings("unused")
	private Bitmap getResizedBitmap(Bitmap bmp, int newHeight, int newWidth) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false);

		return resizedBitmap;
	}

	public Drawable getDrawable() {
		return draw;
	}
	

}
