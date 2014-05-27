package gps.gui;

import gps.application.LoginActivity;
import gps.application.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginGUI extends FrameLayout{
	
	private static final int FILL_PARENT = LayoutParams.FILL_PARENT;
	private static final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	@SuppressWarnings("unused")
	private static final int GRAVITY_CENTER = Gravity.CENTER;
	@SuppressWarnings("unused")
	private int screenWidth;
	@SuppressWarnings("unused")
	private int screenHeight;

	@SuppressWarnings("unused")
	public LoginGUI(Context context) {
		super(context);
		LoginActivity loginActivity = (LoginActivity) context;
		screenWidth = loginActivity.display.getWidth();
		screenHeight = loginActivity.display.getHeight();
		LinearLayout layout = new LinearLayout(context);
		LinearLayout innerLayout = new LinearLayout(context);
		ImageView logo = new ImageView(context);
		TextView usernameText = new TextView(context);
		EditText usernameTextField = new EditText(context);
		TextView passwordText = new TextView(context);
		EditText passwordTextField = new EditText(context);
		Button loginButton = new Button(context);
		Button newAccountButton = new Button(context);
		Button offlineButton = new Button(context);
		
		

//		layout.setLayoutParams(new FrameLayout.LayoutParams(FILL_PARENT, FILL_PARENT, GRAVITY_CENTER));
		layout.setOrientation(LinearLayout.VERTICAL);
		
//		innerLayout.setLayoutParams(new FrameLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT, GRAVITY_CENTER));
		innerLayout.setOrientation(LinearLayout.VERTICAL);
		
		logo.setBackgroundResource(R.drawable.logo);
		logo.setLayoutParams(new LayoutParams(WRAP_CONTENT,dp(50)));
		logo.setPadding(0, 0, 0, dp(10));

		Typeface font = Typeface.createFromAsset(context.getAssets(), "GeosansLight.ttf");
		
		usernameText.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		usernameText.setTextColor(Color.rgb(143, 143, 143));
		usernameText.setText("Username");
		usernameText.setTextAppearance(context, android.R.attr.textAppearanceSmall);
		usernameText.setTypeface(font);

		passwordText.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		passwordText.setTextColor(Color.rgb(143, 143, 143));
		passwordText.setText("Password");
		passwordText.setTextAppearance(context, android.R.attr.textAppearanceSmall);
		passwordText.setTypeface(font);
		
		
		usernameTextField.setLayoutParams(new LayoutParams(FILL_PARENT, WRAP_CONTENT));
		passwordTextField.setLayoutParams(new LayoutParams(FILL_PARENT, WRAP_CONTENT));
		
		loginButton.setLayoutParams(new LayoutParams(FILL_PARENT, WRAP_CONTENT));
		loginButton.setTextSize(dp(15));
		loginButton.setText("Login");
		loginButton.setTypeface(font);
		
		
		newAccountButton.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT, Gravity.RIGHT));
		newAccountButton.setTextSize(dp(10));
		newAccountButton.setTextColor(Color.rgb(143, 143, 143));
		newAccountButton.setBackgroundColor(Color.TRANSPARENT);
		newAccountButton.setTypeface(font);
		newAccountButton.setText("Register a new account?");
		
		
		innerLayout.addView(logo);
		innerLayout.addView(usernameText);
		innerLayout.addView(usernameTextField);
		innerLayout.addView(passwordText);
		innerLayout.addView(passwordTextField);
		innerLayout.addView(loginButton);
		layout.addView(innerLayout);
		layout.addView(newAccountButton);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.setBackgroundResource(R.drawable.gradient);
		this.addView(layout);
		
	}
	
	
	private int dp(int dp) {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		float pixel = dp;
		int pixels = (int) (metrics.density * pixel + 0.5f);
		return pixels;
	}

}
