package gps.navigation;

import gps.application.GPSActivity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class CurrentLocation {

	@SuppressWarnings("static-access")
	public CurrentLocation(final GPSActivity gpsActivity, final Handler handler) {
		LocationManager locationManager = (LocationManager) gpsActivity.getApplicationContext()
				.getSystemService(gpsActivity.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 1000, 1,
				new LocationListener() {

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub

					}

					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
					}

					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						Toast.makeText(gpsActivity.getApplicationContext(),
								"GPS is disabled", Toast.LENGTH_LONG).show();
					}

					public void onLocationChanged(
							android.location.Location location) {
						// TODO Auto-generated method stub
						gpsActivity.mapView.setCurrentLocation(location);
						handler.sendEmptyMessage(0);
					}
				});
	}
	
}
