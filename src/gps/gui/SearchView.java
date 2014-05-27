package gps.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchView extends LinearLayout {
	
	private ListView topListView;
	private ListView bottomListView;
	private ArrayAdapter<String> content1;
	private ArrayAdapter<String> content2;
	private boolean topOpen = false;
	private boolean bottomOpen = false;
	private Display display;
	private EditText topSearchField;
	private EditText bottomSearchField;
	
	public SearchView(final Context context) {
		super(context);
		Activity a = (Activity)context;
		display = a.getWindowManager().getDefaultDisplay();
		
		topSearchField = new EditText(context);
		topSearchField.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		topSearchField.addTextChangedListener(new TextWatcher() {
			
			
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (count == 1 && before == 0) {
					ArrayList<String> list = new ArrayList<String>();
					SQLiteDatabase test = SQLiteDatabase.openDatabase("/mnt/sdcard/Recallin/Database/addressinformation.db", null, SQLiteDatabase.OPEN_READONLY);
					Cursor cur = test.rawQuery("select * from address where name like '"+s.toString()+"%'", null);
					while (cur.moveToNext()) {
						list.add(cur.getString(0));
					}
					cur.close();
					test.close();
					if (content1 != null) 
						content1.clear();
					content1 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
					topListView.setAdapter(content1);
					if(!topOpen){
						Animation show = new ShowAnim(topListView, display.getHeight());
						show.setDuration(900);
						topListView.startAnimation(show);
						topOpen = true;
					}
				}else if(count == 0){
					if(topOpen){
						Animation close = new CloseAnim(topListView, display.getHeight());
						close.setDuration(900);
						topListView.startAnimation(close);
						topOpen = false;
					}
				}else
					content1.getFilter().filter(s);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		topListView = new ListView(context);
		topListView.setTextFilterEnabled(true);
		topListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 0));
		topListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				SQLiteDatabase test = SQLiteDatabase.openDatabase("/mnt/sdcard/Recallin/Database/addressinformation.db", null, SQLiteDatabase.OPEN_READONLY);
				Cursor cur = test.rawQuery("select name, id from address where name = '"+topListView.getItemAtPosition(arg2)+"'", null);
				ArrayList<Object> list = new ArrayList<Object>();
				while (cur.moveToNext()) {
					list.add(cur.getString(0)+"-"+cur.getInt(1));
				}
				final CharSequence[] items = new CharSequence[list.size()];
				for (int i = 0; i < list.size(); i++) {
					items[i] = (CharSequence) list.get(i);
				}
				cur.close();
				test.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Make your selection");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	topSearchField.setText(items[item]);
				    	Animation close = new CloseAnim(topListView, display.getHeight());
						close.setDuration(900);
						topListView.startAnimation(close);
						topOpen = false;
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				System.out.println(topListView.getItemAtPosition(arg2));
			}
		});
	
		
		bottomSearchField = new EditText(context);
		bottomSearchField.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		bottomSearchField.addTextChangedListener(new TextWatcher() {
			
			
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count == 1 && before == 0) {
					ArrayList<String> list = new ArrayList<String>();
					SQLiteDatabase test = SQLiteDatabase.openDatabase("/mnt/sdcard/Recallin/Database/addressinformation.db", null, SQLiteDatabase.OPEN_READONLY);
					Cursor cur = test.rawQuery("select * from address where name like '"+s.toString()+"%'", null);
					while (cur.moveToNext()) {
						list.add(cur.getString(0));
					}
					cur.close();
					test.close();
					if (content2 != null) 
						content2.clear();
					content2 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
					bottomListView.setAdapter(content2);
					if(!bottomOpen){
						Animation show = new ShowAnim(bottomListView, display.getHeight());
						show.setDuration(900);
						bottomListView.startAnimation(show);
						bottomOpen = true;
					}
				}else if(count == 0){
					if(bottomOpen){
						Animation close = new CloseAnim(bottomListView, display.getHeight());
						close.setDuration(900);
						bottomListView.startAnimation(close);
						bottomOpen = false;
					}
				}else
					content2.getFilter().filter(s);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		bottomListView = new ListView(context);
		bottomListView.setTextFilterEnabled(true);
		bottomListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 0));
		bottomListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				SQLiteDatabase test = SQLiteDatabase.openDatabase("/mnt/sdcard/Recallin/Database/addressinformation.db", null, SQLiteDatabase.OPEN_READONLY);
				Cursor cur = test.rawQuery("select name, id from address where name = '"+bottomListView.getItemAtPosition(arg2)+"'", null);
				ArrayList<Object> list = new ArrayList<Object>();
				while (cur.moveToNext()) {
					list.add(cur.getString(0)+"-"+cur.getInt(1));
				}
				final CharSequence[] items = new CharSequence[list.size()];
				for (int i = 0; i < list.size(); i++) {
					items[i] = (CharSequence) list.get(i);
				}
				cur.close();
				test.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Make your selection");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	bottomSearchField.setText(items[item]);
				    	Animation close = new CloseAnim(bottomListView, display.getHeight());
						close.setDuration(900);
						bottomListView.startAnimation(close);
						bottomOpen = false;
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				System.out.println(bottomListView.getItemAtPosition(arg2));
			}
		});
	
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(topSearchField);
		this.addView(topListView);
		this.addView(bottomSearchField);
		this.addView(bottomListView);
	}

}
