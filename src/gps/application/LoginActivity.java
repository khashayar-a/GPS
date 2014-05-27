package gps.application;

import gps.info.ServerConnection;
import gps.voice.RecognitionListener;
import gps.voice.RecognizerTask;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
		OnTouchListener, RecognitionListener {
	static {
		System.loadLibrary("pocketsphinx_jni");
	}
	RecognizerTask rec;
	Thread rec_thread;
	Date start_date;
	float speech_dur;
	boolean listening;

	private TextView userT;
	private TextView passwordT;
	private Button loginB;
	private Button newAccountB;
	private Button offlineB;
	public Display display;

	private EditText phone;
	private EditText pass;
	public static ServerConnection connection = new ServerConnection();

	private Button voice_test;
	private Handler voice_handler;
	private String voice;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		// setContentView(new LoginGUI(this));
		setContentView(R.layout.login);

		this.rec = new RecognizerTask();
		this.rec_thread = new Thread(this.rec);
		this.listening = false;
		this.rec.setRecognitionListener(this);
		this.rec_thread.start();
		voice_test = (Button) findViewById(R.id.voice_test);
		voice_test.setOnTouchListener(this);
		voice_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				displayVoice();
			}
		};

		userT = (TextView) findViewById(R.id.usernameText);
		passwordT = (TextView) findViewById(R.id.passwordText);
		loginB = (Button) findViewById(R.id.login);
		newAccountB = (Button) findViewById(R.id.newAccount);
		offlineB = (Button) findViewById(R.id.offline);

		phone = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"GeosansLight.ttf");

		offlineB.setTypeface(font);
		userT.setTypeface(font);
		passwordT.setTypeface(font);
		loginB.setTypeface(font);
		newAccountB.setTypeface(font);

		offlineB.setOnClickListener(this);
		loginB.setOnClickListener(this);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.login) {
			try {
				String user = phone.getText().toString();
				String password = pass.getText().toString();
				String login = connection.login(user, password);
				if (!login.trim().equals("User not found.")) {
					Toast.makeText(this, "Welcome " + login + ".",
							Toast.LENGTH_LONG).show();
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(getApplicationContext(),
							GPSActivity.class);
					finish();
					startActivity(intent);

				} else {
					Toast.makeText(this, login, Toast.LENGTH_LONG).show();
				}
			} catch (NullPointerException n) {
			}
		} else if (v.getId() == R.id.offline) {
			Intent intent = new Intent(getApplicationContext(),
					GPSActivity.class);
			finish();
			startActivity(intent);
		}
	}
	
	public void displayVoice() {
		System.out.println("Display");
		Toast.makeText(this, voice, Toast.LENGTH_SHORT).show();
	}

	public void onPartialResults(Bundle b) {
		// TODO Auto-generated method stub
	}

	public void onResults(Bundle b) {
		// TODO Auto-generated method stub
		final String hyp = b.getString("hyp");
		Date end_date = new Date();
		long nmsec = end_date.getTime() - this.start_date.getTime();
		float rec_dur = (float) nmsec / 1000;
		String performance = String.format("%.2f seconds %.2f xRT", this.speech_dur, rec_dur / this.speech_dur);
				
		this.voice = hyp;
		voice_handler.sendEmptyMessage(0);
	}

	public void onError(int err) {
		// TODO Auto-generated method stub
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			start_date = new Date();
			this.listening = true;
			this.rec.start();
			break;
		case MotionEvent.ACTION_UP:
			Date end_date = new Date();
			long nmsec = end_date.getTime() - start_date.getTime();
			this.speech_dur = (float) nmsec / 1000;
			if (this.listening) {
				Log.d(getClass().getName(), "Showing Dialog");
				this.listening = false;
			}
			this.rec.stop();
			break;
		default:
			;
		}
		/* Let the button handle its own state */
		return false;
	}

}
