package gps.application;

import gps.gui.MenuGUI;
import gps.navigation.CurrentLocation;
import gps.rendering.MapView;
import gps.talkback.Talkback;

import java.text.DecimalFormat;
import java.text.ParseException;

import android.app.Activity;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

	@SuppressWarnings("unused")
public class GPSActivity extends Activity {

	private final int SWIPE_MIN_DISTANCE = 5;
	private final int SWIPE_THRESHOLD_VELOCITY = 300;
	private GestureDetector mGestureDetector;
	private PointF mid = new PointF();
	private PointF start = new PointF();
	private float oldDist;
	private final int NONE = 0;
	private final int DRAG = 1;
	private final int ZOOM = 2;
	private int mode = NONE;
	private Double lastLatitude = 0.0;
	private Double lastLongitude = 0.0;
	public MenuGUI menuGUI;
	public Display display;
	public MapView mapView;
	public Talkback tb;
	private Handler handler;
	private Thread mapRender;
	private Location temp;
	private DecimalFormat df = new DecimalFormat("###.###");

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		display = getWindowManager().getDefaultDisplay();
		ZoomControls zoomControls = new ZoomControls(this);
		mapView = new MapView(this);
		mapView.setZoomControls(zoomControls);
		
		menuGUI = new MenuGUI(this);
		FrameLayout frame = (FrameLayout) findViewById(R.id.mainFrame);
		frame.addView(mapView);
		frame.addView(zoomControls);
		frame.addView(menuGUI);
		mapRender = new Thread(mapView);
		mapView.setParentThread(mapRender);
		mapRender.start();
		temp = mapView.getCurrentLocation();
		
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (temp == null) {
					temp = mapView.getCurrentLocation();
//					System.out.println("IN THE HANDLER");
					if(mapRender.isAlive()){
						mapView.setAllZero();
					}else{
						mapRender = null;
						System.gc();
						mapRender = new Thread(mapView);
						mapRender.start();
					}
				}
				mapView.updateLocation();
				
			}
		};
		
		CurrentLocation currentLocation = new CurrentLocation(this, handler);
		
		
		tb = new Talkback(this.getApplicationContext());
		tb.start();
	}

	/*--------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------------METHODS FOR UI--------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/

	private Double getBoxTop(Double lon) {
		DecimalFormat df = new DecimalFormat("###.###");
		if(lon != null){
			try {
				lon = (Double) df.parse(df.format(lon));
				int x = (int) ((lon - 11.890) * 1000);
				int d = x / 8;
				Double finalLon = 11.890 + ((d-2) * 0.008);
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
		DecimalFormat df = new DecimalFormat("###.###");
		if(lat != null){
			try {
				lat = (Double) df.parse(df.format(lat));
				int x = (int) ((lat - 57.680) * 1000);
				int d = x / 4;
				Double finalLat = 57.680 + ((d-2) * 0.004);
				finalLat = (Double) df.parse(df.format(finalLat));
				return finalLat;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 57.680;
	}
	

}