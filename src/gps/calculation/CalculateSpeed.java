package gps.calculation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class CalculateSpeed {

	Context context;

	public CalculateSpeed(Context context) {
		this.context = context;
	}

	public double speed() {

		double speed = 0;

		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		double longitude1 = location.getLongitude();
		double latitude1 = location.getLatitude();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double longitude2 = location.getLongitude();
		double latitude2 = location.getLatitude();

		double d = Math.pow((Math.pow((longitude2 - longitude1), 2) + Math.pow(
				(latitude2 - latitude1), 2)), 2);
		speed = d / 2;

		return speed;
	}
}
