package gps.rendering;

import gps.application.GPSActivity;
import gps.application.R;
import gps.bean.ArrPic;
import gps.bean.Way;
import gps.calculation.FindPath;
import gps.gui.MenuGUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * @author PhilipMasek This MapView is custom made just for Recall:in gps
 *         application
 **/
public class MapView extends View implements Runnable, OnClickListener {

	private static final int MOVE_LENGTH = 500;
	private static final int MOVE_LENGTH2 = -500;
	private static boolean ZOOM_IS_SHOWN = false;
	private PointF mid = new PointF();
	private PointF start = new PointF();
	private float oldDist;
	private final int NONE = 0;
	private final int DRAG = 1;
	private final int ZOOM = 2;
	private int mode = NONE;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private GestureDetector gestureDetector;
	private Vibrator vibrator;
	private DecimalFormat df = new DecimalFormat("###.###");
	public Object[] o = new Object[8];
	public Double top = 0.0;
	public Double left = 0.0;
	private BlockRendering blockRendering;
	private ArrayList<ArrPic> pictures = new ArrayList<ArrPic>();
	public Picture[] picture = new Picture[6];
	public LinkedList<HashMap<String, String>> pictureData = new LinkedList<HashMap<String,String>>();
	private Stack<String> queForRender = new Stack<String>();
	private ArrayList<String> thoseNeeded = new ArrayList<String>();
	private Handler h;
	private Picture grid;
	private boolean done = false;
	private Rect dst2 = new Rect(0, 0, 400, 400);
	private ZoomControls zoomControls;
	private Display display;
	private int screenHeight;
	private int screenWidth;
	private String[] mapStreamString;
	private FindPath findPath;
	private Picture route;
	private Picture pin;
	private GPSActivity mainActivity;
	@SuppressWarnings("unused")
	private Context context;
	private Handler routeHandler;
	private Location currentLocation;
	private Thread parentThread;
	private boolean isInterruped = true;
	private int width;
	private int height;
	private int zoomLevel = 5;
	private PointF point;

	@SuppressWarnings("static-access")
	public MapView(GPSActivity context) {
		super(context);
		this.context = (Context) context.getApplicationContext();
		mainActivity = context;
		SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.pin);
		pin = svg.getPicture();
		width = pin.getWidth() / 10;
		height = pin.getHeight() / 10;
		this.display = context.display;

		this.pictureData = null;
		this.pictureData = new LinkedList<HashMap<String,String>>();
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		screenWidth = this.display.getWidth();
		screenHeight = this.display.getHeight();
		gestureDetector = new GestureDetector(context, new GestureListener());
		vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		o[2] = context;
		o[3] = this;
		o[4] = 1;
		SVG svg2 = null;
		svg2 = SVGParser.getSVGFromResource(getResources(), R.raw.grid);
		grid = svg2.getPicture();
		h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				updateMap();
			}
		};
		routeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				updateRoute();
			}
		};

		nextNodeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				updateNextNode();
			}
		};

	}

	
	private void updateNextNode() {
		// TODO Auto-generated method stub
		try {
			point = findPath.nav.getNextTurn();
			Toast toast = Toast
					.makeText(this.context, "Distance to next point: "
							+ findPath.nav.getDistance() + ", CurrentDist: "
							+ findPath.nav.getCurrentDist(), Toast.LENGTH_LONG);
			toast.show();
		} catch (NullPointerException e) {
		}
		invalidate();
	}

	private void updateRoute() {
		InputStream routeStream = findPath.getRouteStream();

		SVG routeSVG = SVGParser.getSVGFromInputStream(routeStream);
		route = routeSVG.getPicture();
	}

	public void run() {
			top = 11.890;
			left = 57.684;
			int size = 5;
			o[0] = top;
			o[1] = left;
			o[4] = 4;
			o[5] = top;
			o[6] = left;
			o[7] = size;
			
			updateQue(11.906, 57.692 , size);
			while(true){
				while(queForRender.size() > 0){
					if(queForRender.size() == size * size){
						cleanDataSet();
					}
					String blockName = queForRender.pop();
					System.out.println("Size    " + queForRender.size() + " FILE :    " + blockName);
					/**
					 * Zoom 1
					 */
					o[4] = 1;
					if(!pictureData.get(0).containsKey(blockName)){
						String[] tempName = blockName.split(" ");
						o[0] = Double.parseDouble(tempName[0]);
						o[1] = Double.parseDouble(tempName[1]);
						blockRendering = new BlockRendering(o);
						Thread renderThread = new Thread(blockRendering);
						blockRendering.setParent(renderThread);
						renderThread.start();
						synchronized (renderThread) {
							try {
								renderThread.wait();
							} catch (InterruptedException e) {
							}
						SVG svg = null;
						String[] newData = this.getMapStreamString();
						
						pictureData.get(0).put(blockName, newData[0]);
						String start = "<svg width=\""+5*400 +"px\" height=\""+5*400 +"px\" viewBox=\"0 0 "+5*400 +" "+5*400 +"\">\n";
						start += "<polyline fill=\"#dedede\" points=\"-10000,-10000 -10000,10000 10000,10000 10000,-10000\"/>";
						String totalData = "";
						String end = "</svg>";
						for (String block : thoseNeeded) {
							if(pictureData.get(0).get(block) != null){
								totalData += pictureData.get(0).get(block);
							}
						}
						List<ByteArrayInputStream> streams = Arrays.asList(new ByteArrayInputStream(start.getBytes()),
								new ByteArrayInputStream(totalData.getBytes()),
								new ByteArrayInputStream(end.getBytes()));
						InputStream readyToUse = new SequenceInputStream(Collections.enumeration(streams));
						
						svg = SVGParser.getSVGFromInputStream(readyToUse);
						picture[0] = svg.getPicture();
						
						pictureData.get(1).put(blockName, newData[1]);
						start = "<svg width=\""+5*400 +"px\" height=\""+5*400 +"px\" viewBox=\"0 0 "+5*400 +" "+5*400 +"\">\n";
						totalData = "";
						end = "</svg>";
						for (String block : thoseNeeded) {
							if(pictureData.get(1).get(block) != null){
								totalData += pictureData.get(1).get(block);
							}
						}
						streams = Arrays.asList(new ByteArrayInputStream(start.getBytes()),
								new ByteArrayInputStream(totalData.getBytes()),
								new ByteArrayInputStream(end.getBytes()));
						readyToUse = new SequenceInputStream(Collections.enumeration(streams));
						
						svg = SVGParser.getSVGFromInputStream(readyToUse);
						picture[1] = svg.getPicture();
						
						pictureData.get(2).put(blockName, newData[2]);
						start = "<svg width=\""+5*400 +"px\" height=\""+5*400 +"px\" viewBox=\"0 0 "+5*400 +" "+5*400 +"\">\n";
						totalData = "";
						end = "</svg>";
						for (String block : thoseNeeded) {
							if(pictureData.get(2).get(block) != null){
								totalData += pictureData.get(2).get(block);
							}
						}
						streams = Arrays.asList(new ByteArrayInputStream(start.getBytes()),
								new ByteArrayInputStream(totalData.getBytes()),
								new ByteArrayInputStream(end.getBytes()));
						readyToUse = new SequenceInputStream(Collections.enumeration(streams));
						
						svg = SVGParser.getSVGFromInputStream(readyToUse);
						picture[2] = svg.getPicture();
						}
						
					}
					/**
					 * End of Zoom 1
					 */
					h.sendEmptyMessage(0);

					/**
					 * Zoom 2
					 */
					o[4] = 2;
					if(!pictureData.get(3).containsKey(blockName)){
						String[] tempName = blockName.split(" ");
						o[0] = Double.parseDouble(tempName[0]);
						o[1] = Double.parseDouble(tempName[1]);
						blockRendering = new BlockRendering(o);
						Thread renderThread = new Thread(blockRendering);
						blockRendering.setParent(renderThread);
						renderThread.start();
						synchronized (renderThread) {
							try {
								renderThread.wait();
							} catch (InterruptedException e) {
							}
						}
						SVG svg = null;
						String[] newData = this.getMapStreamString();
						
						pictureData.get(3).put(blockName, newData[0]);
						String start = "<svg width=\""+5*400 +"px\" height=\""+5*400 +"px\" viewBox=\"0 0 "+5*400 +" "+5*400 +"\">\n";
						String totalData = "";
						String end = "</svg>";
						for (String block : thoseNeeded) {
							if(pictureData.get(3).get(block) != null){
								totalData += pictureData.get(3).get(block);
							}
						}
						List<ByteArrayInputStream> streams = Arrays.asList(new ByteArrayInputStream(start.getBytes()),
								new ByteArrayInputStream(totalData.getBytes()),
								new ByteArrayInputStream(end.getBytes()));
						InputStream readyToUse = new SequenceInputStream(Collections.enumeration(streams));
						
						svg = SVGParser.getSVGFromInputStream(readyToUse);
						picture[3] = svg.getPicture();

						
					}
					/**
					 * End of Zoom 2
					 */

					h.sendEmptyMessage(0);

					/**
					 * Zoom 3
					 */
					o[4] = 3;
					if(!pictureData.get(4).containsKey(blockName)){
						String[] tempName = blockName.split(" ");
						o[0] = Double.parseDouble(tempName[0]);
						o[1] = Double.parseDouble(tempName[1]);
						blockRendering = new BlockRendering(o);
						Thread renderThread = new Thread(blockRendering);
						blockRendering.setParent(renderThread);
						renderThread.start();
						synchronized (renderThread) {
							try {
								renderThread.wait();
							} catch (InterruptedException e) {
							}
						}
						SVG svg = null;
						String[] newData = this.getMapStreamString();
						
						pictureData.get(4).put(blockName, newData[0]);
						String start = "<svg width=\""+5*400 +"px\" height=\""+5*400 +"px\" viewBox=\"0 0 "+5*400 +" "+5*400 +"\">\n";
						String totalData = "";
						String end = "</svg>";
						for (String block : thoseNeeded) {
							if(pictureData.get(4).get(block) != null){
								totalData += pictureData.get(4).get(block);
							}
						}
						List<ByteArrayInputStream> streams = Arrays.asList(new ByteArrayInputStream(start.getBytes()),
								new ByteArrayInputStream(totalData.getBytes()),
								new ByteArrayInputStream(end.getBytes()));
						InputStream readyToUse = new SequenceInputStream(Collections.enumeration(streams));
						
						svg = SVGParser.getSVGFromInputStream(readyToUse);
						picture[4] = svg.getPicture();
					}
					/**
					 * End of Zoom 3
					 */
					h.sendEmptyMessage(0);
				}
			}
			
			
		
	}

	private void updateMap() {
		done = true;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.drawRGB(171, 171, 171);
		canvas.setMatrix(matrix);
		if (mode != ZOOM)
			canvas.drawPicture(grid, dst);
		

		if (done) {
			if (picture[0] != null && getZoomLevel() > 1) {
				dst2 = new Rect(0, 0, 2000, 2000);
				canvas.drawPicture(picture[0], dst2);
			}
			if (picture[1] != null && getZoomLevel() > 1) {
				dst2 = new Rect(0, 0, 2000, 2000);
				canvas.drawPicture(picture[1], dst2);
			}
			if (picture[2] != null && getZoomLevel() > 1) {
				dst2 = new Rect(0, 0, 2000, 2000);
				canvas.drawPicture(picture[2], dst2);
			}
			if (picture[3] != null && getZoomLevel() > 2) {
				dst2 = new Rect(0, 0, 2000, 2000);
				canvas.drawPicture(picture[3], dst2);
			}
			if (picture[4] != null && getZoomLevel() > 3) {
				dst2 = new Rect(0, 0, 2000, 2000);
				canvas.drawPicture(picture[4], dst2);
			}
//			if(picture[5] != null && getZoomLevel() > 4){
//				dst2 = new Rect(0, 0, 2000, 2000);
//				canvas.drawPicture(picture[5],dst2);
//			}
			if (route != null) {
				canvas.drawPicture(route);
			}

			if (getCurrentLocation() != null) {
				float eX = 0;
				float eY = 0;
				if (getCurrentLocation() != null) {
					eX = (float) ((getCurrentLocation().getLongitude() - top) * 50000);
					eY = (float) (2000 - ((getCurrentLocation().getLatitude() - left) * 100000));
				}
				RectF pinRect = new RectF(eX - (width / 2), (eY - height), eX
						+ (width / 2), eY);
				canvas.drawPicture(pin, pinRect);
			}

			if (point != null) {
				// System.out.println("PAINTING");
				Paint paint = new Paint();
				paint.setColor(Color.MAGENTA);
				canvas.drawCircle((float) (point.x - top) * 50000,
						(float) (2000 - (point.y - left) * 100000), 5, paint);
			}

		}
		canvas.restore();
		super.onDraw(canvas);
	}

	/* ####################### Controlling the grid ####################### */
	/* #################################################################### */
	private Rect dst = new Rect(-10000, -10000, 10000, 10000);
	private int leftDistance = MOVE_LENGTH;
	private float rightDistance = MOVE_LENGTH2;
	private float topDistance = MOVE_LENGTH;
	private int bottomDistance = MOVE_LENGTH2;
	private float[] size = { dst.left, dst.top, dst.right, dst.bottom };
	private float matrixScale;
	private float globalY;
	private float globalX;
	private Handler nextNodeHandler;

	private void moveGrid() {
		float[] values = new float[9];
		matrix.getValues(values);
		setGlobalX(values[2]);
		setGlobalY(values[5]);
		setMatrixScale(values[0] * 1000);

		// if (getMatrixScale()>500) {
		// setZoomLevel(5);
		// }else
		// if (getMatrixScale()<800 && getMatrixScale()>500) {
		// setZoomLevel(4);
		// }else
		// if (getMatrixScale()<500 && getMatrixScale()>300) {
		// setZoomLevel(3);
		// }else
		// if (getMatrixScale()<300 && getMatrixScale()>10) {
		// setZoomLevel(2);
		// }else
		// if (getMatrixScale()<10) {
		// setZoomLevel(1);
		// }

		int[] v = { dst.left, dst.top, dst.right, dst.bottom };
		if (getGlobalX() > leftDistance) {
			dst.set(v[0] - MOVE_LENGTH, v[1], v[2] - MOVE_LENGTH, v[3]);
			leftDistance += MOVE_LENGTH;
			rightDistance += MOVE_LENGTH;
		} else if (getGlobalX() < rightDistance) {
			dst.set(v[0] + MOVE_LENGTH, v[1], v[2] + MOVE_LENGTH, v[3]);
			leftDistance -= MOVE_LENGTH;
			rightDistance -= MOVE_LENGTH;
		} else if (getGlobalY() > topDistance) {
			dst.set(v[0], v[1] - MOVE_LENGTH, v[2], v[3] - MOVE_LENGTH);
			topDistance += MOVE_LENGTH;
			bottomDistance += MOVE_LENGTH;
		} else if (getGlobalY() < bottomDistance) {
			dst.set(v[0], v[1] + MOVE_LENGTH, v[2], v[3] + MOVE_LENGTH);
			topDistance -= MOVE_LENGTH;
			bottomDistance -= MOVE_LENGTH;
		}
		dst.set((int) (size[0] / getMatrixScale()),
				(int) (size[1] / getMatrixScale()),
				(int) (size[2] / getMatrixScale()),
				(int) (size[3] / getMatrixScale()));
	}

	/* #################################################################### */
	/* #################################################################### */
	/* #################################################################### */
	/* #################################################################### */

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
				e.printStackTrace();
			}
		}
		return 57.684;
	}

	public void setZoomControls(ZoomControls zoomControls) {
		this.zoomControls = zoomControls;
		this.zoomControls.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));
		this.zoomControls.setPadding(0, 0, 0, 55);
		this.zoomControls.setOnZoomInClickListener(new OnClickListener() {
			public void onClick(View v) {
				float scale = (float) 1.4;
				matrix.postScale(scale, scale, (screenWidth / 2),
						(screenHeight / 2));
				invalidate();
			}
		});
		this.zoomControls.setOnZoomOutClickListener(new OnClickListener() {

			public void onClick(View v) {
				float scale = (float) 0.7;
				matrix.postScale(scale, scale, (screenWidth / 2),
						(screenHeight / 2));
				invalidate();
			}
		});
		this.zoomControls.hide();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			if (ZOOM_IS_SHOWN) {
				zoomControls.hide();
				ZOOM_IS_SHOWN = false;
			}
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			mode = NONE;
			invalidate();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			invalidate();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
				invalidate();
				double boxTop = (getGlobalX()  *(-1)) ;
				boxTop = boxTop * (1/(matrixScale / 1000));
				boxTop = boxTop / 50000;
				boxTop = boxTop + top;
				double boxLeft = (getGlobalY() * (-1));
				boxLeft = boxLeft * (1/(matrixScale / 1000));
				boxLeft = boxLeft / 100000;
				boxLeft = left - boxLeft ;
				boxTop = getBoxTop(boxTop + 0.008);
				boxLeft = getBoxLeft(boxLeft + 0.004);
				updateQue(boxTop, boxLeft , 3);
				System.out.println("New Size : " +queForRender.size() + "       " + getGlobalX() + "   ---   " + getGlobalY());
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					matrix.set(savedMatrix);
					matrix.postScale(scale, scale, mid.x, mid.y);
					invalidate();
				}
			}
			break;
		}
		moveGrid();
		return gestureDetector.onTouchEvent(event);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public ArrayList<ArrPic> getPictures() {
		return pictures;
	}

	public void setPictures(ArrayList<ArrPic> pictures) {
		this.pictures = pictures;
	}

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent e) {
			Log.i("GestureListener", "Long press");
			vibrator.vibrate(60);
			super.onLongPress(e);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Timer timer = new Timer();
			final Handler h1 = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (ZOOM_IS_SHOWN) {
						zoomControls.hide();
						ZOOM_IS_SHOWN = false;
					}
					super.handleMessage(msg);
				}
			};

			if (ZOOM_IS_SHOWN) {
				zoomControls.hide();
				ZOOM_IS_SHOWN = false;
			} else {
				zoomControls.show();
				ZOOM_IS_SHOWN = true;
			}
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					h1.sendEmptyMessage(0);
				}
			}, 7000);

			return super.onSingleTapConfirmed(e);
		}

		// event when double tap occurs
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			mode = ZOOM;
			matrix.set(savedMatrix);
			float scale = (float) 1.5;
			matrix.postScale(scale, scale, e.getX(), e.getY());
			invalidate();
			return false;
		}
	}

	@SuppressWarnings("unused")
	public void onClick(View v) {
		if (v.getId() == MenuGUI.START_BUTTON) {
			// String[] startLL =
			// mainActivity.menuGUI.getStartText().split("#");
			Location start = new Location("");
			start.setLatitude(57.7068102/* Double.parseDouble(startLL[0]) */);
			start.setLongitude(11.9374346/* Double.parseDouble(startLL[1]) */);
			start.setTime(344124917);

			// String[] endLL = mainActivity.menuGUI.getEndText().split("#");
			Location end = new Location("");
			end.setLatitude(57.7045235/* Double.parseDouble(endLL[0]) */);
			end.setLongitude(11.9376559/* Double.parseDouble(endLL[1]) */);
			end.setTime(366091918);

			findPath = new FindPath(mainActivity, start, end, routeHandler,
					(Double) o[5], (Double) o[6], nextNodeHandler);
			Thread t = new Thread(findPath);
			t.start();
			mainActivity.menuGUI.slidingDrawer.animateClose();
		}
	}

	public void setAllZero() {
		this.picture[0] = null;
		this.picture[1] = null;
		this.picture[2] = null;
		this.picture[3] = null;
		this.picture[4] = null;
		this.picture[5] = null;
		this.pictureData = null;
		this.pictureData = new LinkedList<HashMap<String,String>>();
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		pictureData.add(new HashMap<String, String>());
		isInterruped = true;

		try {
			this.top = getBoxTop(this.getCurrentLocation().getLongitude());
			this.left = getBoxLeft(this.getCurrentLocation().getLatitude());
			this.top = this.top - (((5 - 1) / 2) * 0.008);
			this.top = (Double) df.parse(df.format(this.top));
			this.left = this.left - (((5 - 1) / 2) * 0.004);
			this.left = (Double) df.parse(df.format(this.left));
			this.o[0] = this.top;
			this.o[1] = this.left;
			this.o[5] = this.top;
			this.o[6] = this.left;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cleanDataSet() {
		thoseNeeded.clear();
		thoseNeeded = new ArrayList<String>();
		synchronized (queForRender) {
			for (String block : queForRender) {
				thoseNeeded.add(block);
			}
		}
	}
	
	public void updateQue( double boxTop , double boxLeft , int size) {
		Stack<String> que = new Stack<String>();
		int i = size/2;
		int j = size/2;
		que.add(i + " " + j);
		int circle = 1;
		boolean circleEnd = false;
		int iCounter;
		int jCounter;
		while(circle < (size+1) / 2 ){
			j = (size-1) / 2 - circle;
			i = (size-1) / 2;
			circleEnd = false;
			jCounter = 0;
			iCounter = 1;
			while(!circleEnd){
				que.add(i + " " + j);
				if(i == (size-1) / 2 + circle && j == (size-1) / 2 - circle ){
					jCounter = 1;
					iCounter = 0;
				}
				if(i == (size-1) / 2 + circle && j == (size-1) / 2 + circle ){
					jCounter = 0;
					iCounter = -1;
				}
				if(i == (size-1) / 2 - circle && j == (size-1) / 2 + circle ){
					jCounter = -1;
					iCounter = 0;
				}
				if(i == (size-1) / 2 - circle && j == (size-1) / 2 - circle ){
					jCounter = 0;
					iCounter = 1;
				}
				if(i == (size-1) / 2 - 1 && j == (size-1) / 2 - circle){
					circleEnd = true;
					circle++;
				}
				
				i += iCounter;
				j += jCounter;
				
			}
		}
		queForRender.removeAllElements();
		queForRender = new Stack<String>();
		int sizeOfQue = que.size();
		double lon = 0.0;
		double lat = 0.0;
		try {
			for (int k = 0; k < sizeOfQue; k++) {
				String[] temp = que.pop().split(" ");
				int x = Integer.parseInt(temp[0]);
				int y = Integer.parseInt(temp[1]);
				lon = ((x - ( (size-1) /2 )) * 0.008) + boxTop; 
				lon = (Double) df.parse(df.format(lon));
				lat = ((y - ( (size-1) /2 )) * 0.004) + boxLeft; 
				lat = (Double) df.parse(df.format(lat));
				queForRender.add(lon + " " + lat);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void updateLocation() {
		invalidate();
		h.sendEmptyMessage(0);
	}

	public void setCurrentLocation(Location currentLocation) {
		// System.out.println(currentLocation.getLongitude() + "    "
		// + currentLocation.getLatitude());
		this.currentLocation = currentLocation;
	}

	public Thread getParentThread() {
		return parentThread;
	}

	public void setParentThread(Thread parentThread) {
		this.parentThread = parentThread;
	}

	public String[] getMapStreamString() {
		return mapStreamString;
	}

	public void setMapStreamString(String[] mapStreamString) {
		this.mapStreamString = mapStreamString;
	}

	public float getGlobalY() {
		return globalY;
	}

	public void setGlobalY(float globalY) {
		this.globalY = globalY;
	}

	public float getGlobalX() {
		return globalX;
	}

	public void setGlobalX(float globalX) {
		this.globalX = globalX;
	}

	public float getMatrixScale() {
		return matrixScale;
	}

	public void setMatrixScale(float matrixScale) {
		this.matrixScale = matrixScale;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

}