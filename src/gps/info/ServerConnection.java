package gps.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerConnection {

	@SuppressWarnings("unused")
	private Thread thread;
	private User user = null;
	private String server = "http://www.recallin.com/_action/_phone.jsp?drowssap=X43KKjjdsl3w24oo&action=";
	private JSONObject jObject;

	public ServerConnection() {
	}

	public User getUser() {
		return user;
	}

	public void turnOnLocation() {
		thread = new Thread() {
			public void run() {
				while (true != false) {

					// / USER POSITION IS MISSING !

					updateLocation(Double.toString(Math.random()),
							Double.toString(Math.random()));
					try {
						Thread.sleep(3600000);
					} catch (InterruptedException e) {
					}
				}
			}
		};
	}

	public void turnOffLocation() {
		this.thread = null;
	}

	public void addFriend(String friendID) {
		String URL = server + "add_friend&userID=" + user.getUser_id()
				+ "&friendID=" + friendID;
		postInfo(URL);
	}

	public void deleteFriend(String friendID) {
		String URL = server + "delete_friend&userID=" + user.getUser_id()
				+ "&friendID=" + friendID;
		postInfo(URL);
	}

	public void rejectFriend(String friendID) {
		String URL = server + "reject_friend&userID=" + user.getUser_id()
				+ "&friendID=" + friendID;
		postInfo(URL);
	}

	public void acceptFriend(String friendID) {
		String URL = server + "accept_friend&userID=" + user.getUser_id()
				+ "&friendID=" + friendID;
		postInfo(URL);
	}

	public String login(String phone_number, String password) {
		String check = receiveInfo(server + "login&phone_number="
				+ phone_number + "&password=" + password);
		if (check.trim().equals("User not found.")) {
			return check.trim();
		} else {
			try {
				jObject = new JSONObject(check);
				JSONArray obj = jObject.getJSONArray("users");
				for (int i = 0; i < obj.length(); i++) {
					user = new User(obj.getJSONObject(i).getInt("user_id"),
							obj.getJSONObject(i).getString("name"), obj
							.getJSONObject(i).getString("phone_number"),
					obj.getJSONObject(i).getString("email"), obj
							.getJSONObject(i).getInt("hidden_all"));
				}
				return user.getName();
			} catch (JSONException e) {
				check = e.getMessage();
			}
		}
		return check;
	}

	private void updateLocation(String longitude, String latitude) {
		String URL = server + "set&phone_number=" + this.user.getPhone_number()
				+ "&longitude=" + longitude + "&latitude=" + latitude;
		postInfo(URL);
	}

	public UserLocation getLocation(String phone_number) {
		String[] split = receiveInfo(
				server + "get&phone_number=" + phone_number).split("##");
		try {
			return new UserLocation(Double.parseDouble(split[0]),
					Double.parseDouble(split[1]));
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public String getResultSet(String query, int attributes) {
		String rs = receiveInfo(server + "ResultSet&query=" + query + "&attr="
				+ Integer.toString(attributes));
		return rs;
	}

	public ArrayList<User> searchFriends(String search) {
		ArrayList<User> list = new ArrayList<User>();
		try {
			jObject = new JSONObject(receiveInfo(this.server
					+ "search_friends&userID=" + user.getUser_id() + "&search="
					+ search));
			JSONArray friends = jObject.getJSONArray("friends");
			for (int i = 0; i < friends.length(); i++) {
				list.add(new User(friends.getJSONObject(i).getInt("user_id"),
						friends.getJSONObject(i).getString("name"), friends
								.getJSONObject(i).getString("phone_number"),
						friends.getJSONObject(i).getString("email"), friends
								.getJSONObject(i).getDouble("longitude"),
						friends.getJSONObject(i).getDouble("latitude"), friends
								.getJSONObject(i).getString("last_time"),
						friends.getJSONObject(i).getInt("location_accuracy"), friends
								.getJSONObject(i).getInt("hidden_all"), friends
								.getJSONObject(i).getInt("request_status"),
								friends.getJSONObject(i).getInt("hidden_friend"),
								friends.getJSONObject(i).getInt("me_hidden")));
				// "user_id":"2" , "name":"Andreas" ,
				// "phone_number":"0709665538" ,
				// "email":"andreas.axen.kruger@gmail.com" ,
				// "longitude":"13.1267402" ,
				// "latitude":"58.537214" , "last_time":"2012-01-01 00:00:00.0"
				// ,
				// "location_accuracy":"0", "hidden_all":"0",
				// "request_status":"1",
				// "hidden_friend":"0", "me_hidden":"1"
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	private void postInfo(String URL) {
		HttpClient client = new DefaultHttpClient();
		try {
			client.execute(new HttpGet(URL));
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}

	private String receiveInfo(String URL) {
		HttpClient client = new DefaultHttpClient();
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(new HttpGet(URL));

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				return "read: " + e.getMessage();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					return "close: " + e.getMessage();
				}
			}
		} catch (ClientProtocolException e) {
			return "client: " + e.getMessage();
		} catch (IOException e) {
			return "whole: " + e.getMessage();
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
}
