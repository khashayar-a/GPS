package gps.rendering;

import gps.application.GPSActivity;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class DrawRoute {
	private int colorBackground = Color.TRANSPARENT;
	private int colorRoute = Color.BLUE;
	private int widthRoute = 3;
	
	@SuppressWarnings("unused")
	private Drawable draw;

	public DrawRoute(ArrayList<MapObject> route, GPSActivity main) {
		final ArrayList<MapObject> ro = route;
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
				Paint paint = new Paint();
				paint.setColor(colorRoute);
				paint.setStrokeWidth(widthRoute);
				for (MapObject object : ro) {
					canvas.drawLine(object.getPreX(), object.getPreY(), object.getCurX(), object.getCurY(), paint);
				}
			}
		};
	}

}
