package gps.gui;

import gps.application.GPSActivity;
import gps.application.R;
import gps.rendering.MapView;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.FrameLayout.LayoutParams;

	@SuppressWarnings("unused")
public class MenuGUI extends FrameLayout implements OnClickListener,
		OnTouchListener {

	private static final int MAP = 5003;
	private static final int FRIENDS = 5004;
	private static final int SETTINGS = 5005;
	private static final int FAVORITES = 5001;
	private static final int GPS = 5002;
	private static final int SCROLL_MENU = 2001;
	private static final int DOWN_BUTTON = 1001;
	public static final int START_BUTTON = 2222;
	private final int SWIPE_MIN_DISTANCE = 5;
	private final int SWIPE_THRESHOLD_VELOCITY = 300;
	private GestureDetector mGestureDetector;
	private HorizontalScrollView menuScroll;
	private HorizontalScrollView contentScroll;
	public static int screenWidth;
	public static int screenHeight;
	private int scrollTo;
	private int width = 335;
	private int snap = (width / 2);
	private int activeFeatures = 0;
	private int scrollTo2;
	public SlidingDrawer slidingDrawer;
	private EditText startEdit;
	private EditText endEdit;
	
	public MenuGUI(final Context context) {
		super(context);
		GPSActivity main = (GPSActivity) context;
		Display display = main.display;
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
//		System.out.println(screenWidth+"   "+screenHeight);
		mGestureDetector = new GestureDetector(new MyGestureDetector());
		
/*###################### Creating top menu ######################*/
		View mapIcon = new View(context);
		View friendsIcon = new View(context);
		final View settingsIcon = new View(context);
		final View favoritesIcon = new View(context);
		View gpsIcon = new View(context);
		
		LayoutParams iconSize = new LayoutParams(335, 125);
		mapIcon.setLayoutParams(iconSize);
		friendsIcon.setLayoutParams(iconSize);
		settingsIcon.setLayoutParams(iconSize);
		favoritesIcon.setLayoutParams(iconSize);
		gpsIcon.setLayoutParams(iconSize);
		
		mapIcon.setBackgroundResource(R.drawable.ic_map256);
		friendsIcon.setBackgroundResource(R.drawable.ic_friends256);
		settingsIcon.setBackgroundResource(R.drawable.ic_settings256);
		favoritesIcon.setBackgroundResource(R.drawable.ic_favorites256);
		gpsIcon.setBackgroundResource(R.drawable.ic_gps256);
		
		LinearLayout menuLayout = new LinearLayout(context);
		menuLayout.addView(favoritesIcon);
		menuLayout.addView(gpsIcon);
		menuLayout.addView(mapIcon);
		menuLayout.addView(friendsIcon);
		menuLayout.addView(settingsIcon);
		
/*###################### Creating bottom menu ######################*/		
		FrameLayout mapFrame = new FrameLayout(context);
		FrameLayout friendsFrame = new FrameLayout(context);
		FrameLayout settingsFrame = new FrameLayout(context);
		FrameLayout favoritesFrame = new FrameLayout(context);
		FrameLayout gpsFrame = new FrameLayout(context);
		
		
		LayoutParams size = new LayoutParams(screenWidth, LayoutParams.FILL_PARENT);
		mapFrame.setLayoutParams(size);
		friendsFrame.setLayoutParams(size);
		settingsFrame.setLayoutParams(size);
		favoritesFrame.setLayoutParams(size);
		gpsFrame.setLayoutParams(size);
		
		LinearLayout contentLayout = new LinearLayout(context);
		contentLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		contentLayout.addView(favoritesFrame);
		contentLayout.addView(gpsFrame);
		contentLayout.addView(mapFrame);
		contentLayout.addView(friendsFrame);
		contentLayout.addView(settingsFrame);
		
/*###################### Putting content into GPSframe ######################*/
		SearchView searchView = new SearchView(context);
		gpsFrame.addView(searchView);
		
		
/*###################### Assigning scrollViews ######################*/
		contentScroll = new HorizontalScrollView(context);
		contentScroll.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		menuScroll = new HorizontalScrollView(context);
		menuScroll.setHorizontalScrollBarEnabled(false);
		menuScroll.setFadingEdgeLength(screenWidth/6);
		menuScroll.setId(SCROLL_MENU);
		menuScroll.setOnTouchListener(this);
		menuScroll.setPadding(0, 30, 0, 20);
		menuScroll.addView(menuLayout);
		menuScroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

					public void onGlobalLayout() {
						menuScroll.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						width = favoritesIcon.getWidth();
						LinearLayout.LayoutParams sMargin = (LinearLayout.LayoutParams) settingsIcon
								.getLayoutParams();
						LinearLayout.LayoutParams fMargin = (LinearLayout.LayoutParams) favoritesIcon
								.getLayoutParams();
						int margin = (int) ((screenWidth / 2) - (width / 2));
						sMargin.setMargins(0, 0, margin, 0);
						fMargin.setMargins(margin, 0, 0, 0);
						settingsIcon.setLayoutParams(sMargin);
						favoritesIcon.setLayoutParams(fMargin);
						scrollRight();
						scrollRight();
					}
				});

		contentScroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (screenHeight-menuScroll.getHeight())));
		contentScroll.setFadingEdgeLength(0);
		contentScroll.setHorizontalScrollBarEnabled(false);
		contentScroll.addView(contentLayout);
		
/*###################### Creating the sliding drawer ######################*/

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        slidingDrawer = (SlidingDrawer)inflater.inflate(R.layout.sliding_drawer, this, false);
        this.addView(slidingDrawer);
        
/*###################### Adding to the main frame ######################*/
		
		LinearLayout mainFrame = (LinearLayout) findViewById(R.id.content);
		mainFrame.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mainFrame.setOrientation(LinearLayout.VERTICAL);
		
		View gradientLine = new View(context);
		gradientLine.setBackgroundResource(R.drawable.line);
		gradientLine.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
		Button down = new Button(context);
		down.setWidth(screenWidth);
		down.setHeight(50);
		down.setId(DOWN_BUTTON);
		down.setText("DOWN");
		down.setOnClickListener(this);
		
		
		
		mainFrame.addView(menuScroll);
		mainFrame.addView(gradientLine);
		mainFrame.addView(contentScroll);
		mainFrame.addView(down);
		
	}
	
	public String getStartText() {
		return startEdit.getText().toString();
	}
	public String getEndText() {
		return endEdit.getText().toString();
	}
	

	/*--------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------METHODS FOR CONTROLLING UI----------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------*/

	public void onClick(View v) {
		if (v.getId() == DOWN_BUTTON) {
			slidingDrawer.animateClose();
		}
	}

	private void scrollRight() {
		if (activeFeatures != 4) {
			scrollTo += width;
			activeFeatures = (scrollTo / width);
			menuScroll.smoothScrollTo(scrollTo, 0);
			snap += width;
			scrollTo2 += screenWidth;
			contentScroll.smoothScrollTo(scrollTo2, 0);
		}
	}

	private void scrollLeft() {
		if (activeFeatures != 0) {
			scrollTo -= width;
			activeFeatures = (scrollTo / width);
			menuScroll.smoothScrollTo(scrollTo, 0);
			snap -= width;
			scrollTo2 -= screenWidth;
			contentScroll.smoothScrollTo(scrollTo2, 0);
		}
	}

	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == SCROLL_MENU) {
			if (mGestureDetector.onTouchEvent(event)) {
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				if (menuScroll.getScrollX() > snap && activeFeatures != 4) {
					scrollRight();
				} else if ((menuScroll.getScrollX() + width) < snap
						&& activeFeatures != 0) {
					scrollLeft();
				} else {
					menuScroll.smoothScrollTo(scrollTo, 0);
				}

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					scrollRight();
					return true;
				}
				// left to right
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					scrollLeft();
					return true;
				}
			} catch (Exception e) {
			}
			return false;
		}
	}
	
}