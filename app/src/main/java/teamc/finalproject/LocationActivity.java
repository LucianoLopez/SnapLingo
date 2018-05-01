package teamc.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, mLocationListener);


    }

    private void updateLocation() {
        String currentGameID = "games/5961";
        String randomWordID = "omgrandom";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(currentGameID).child("locations").child(randomWordID);
        ArrayList<Double> location = new ArrayList<Double>();
        location.add(currentLocation.getLatitude());
        location.add(currentLocation.getLongitude());
        myRef.setValue(location);
    }


    private LocationManager mLocationManager;
    static android.location.Location currentLocation;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final android.location.Location location) {
            if (currentLocation == null) {
                currentLocation = location;
                updateLocationAction();
            }
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void updateLocationAction() {

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, mLocationListener);
        }
    }


}
