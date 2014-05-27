package gps.info;

public class User {
	private int user_id;
	private String name;
	private String phone_number = null;
	private String email = null;
	private double longitude = 0.0;
	private double latitude = 0.0;
	private String last_time = null;
	private double location_accuracy = 0.0;
	private int request_status = 0;
	private int hidden_all = 0;
	private int me_hidden = 0;
	private int hidden_friend = 0;

	public User(int user_id, String name, String phone_number, String email,
			double longitude, double latitude, String last_time,
			double location_accuracy, int hidden_all, int request_status,
			int hidden_friend, int me_hidden) {
		this.user_id = user_id;
		this.name = name;
		this.phone_number = phone_number;
		this.email = email;
		this.longitude = longitude;
		this.latitude = latitude;
		this.last_time = last_time;
		this.location_accuracy = location_accuracy;
		this.request_status = request_status;
		this.hidden_all = hidden_all;
		this.me_hidden = me_hidden;
		this.hidden_friend = hidden_friend;
	}

	public User(int user_id, String name, String phone_number, String email,
			int hidden_all) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.phone_number = phone_number;
		this.email = email;
		this.hidden_all = hidden_all;
	}

	public User(int user_id, String name) {
		this.user_id = user_id;
		this.name = name;
	}

	public boolean isFriendAccepted() {
		if (this.request_status == 1)
			return true;
		return false;
	}

	public boolean isFriendHidingAll() {
		if (this.hidden_all == 1)
			return true;
		return false;
	}

	public boolean isFriendHiding() {
		if (this.hidden_friend == 1)
			return true;
		return false;
	}

	public boolean isMeHiding() {
		if (this.me_hidden == 1)
			return true;
		return false;
	}

	public int getUser_id() {
		return user_id;
	}

	public String getName() {
		return name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getEmail() {
		return email;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getLast_time() {
		return last_time;
	}

	public double getLocation_accuracy() {
		return location_accuracy;
	}

}
