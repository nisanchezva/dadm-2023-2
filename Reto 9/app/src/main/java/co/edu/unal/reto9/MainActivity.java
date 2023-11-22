package co.edu.unal.reto9;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private EditText edtLatitude, edtLongitude;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        edtLatitude = findViewById(R.id.edtLatitude);
        edtLongitude = findViewById(R.id.edtLongitude);
        Button searchButton = findViewById(R.id.btnSearch);
        Button myLocationButton = findViewById(R.id.btnMyLocation);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPointsOfInterest();
            }
        });

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLocationProvider();
            }
        });

        // Verificar y solicitar permisos de ubicación si es necesario
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            // Permiso ya concedido, configurar el proveedor de ubicación
            setupLocationProvider();
        }

    }

    private void setupLocationProvider() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mapView.getOverlays().clear();
                // Actualizar y mostrar la ubicación en el mapa
                showLocationOnMap(location);

                // Detener las actualizaciones de ubicación después de recibir la primera ubicación
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Solicitar actualizaciones de ubicación
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );

            // Obtener la última ubicación conocida y centrar el mapa en ella
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                showLocationOnMap(lastKnownLocation);
            }
        }
    }


    private void showLocationOnMap(Location location) {

        mapView.getOverlays().clear();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        GeoPoint currentLocation = new GeoPoint(latitude, longitude);
        // Centrar el mapa en la ubicación actual
        int zoomLevel = 18; // Puedes ajustar este valor según lo necesites

        mapView.getController().setCenter(currentLocation);
        mapView.getController().setZoom(zoomLevel);

        // Añadir un marcador en la ubicación actual
        Marker marker = new Marker(mapView);
        marker.setPosition(new org.osmdroid.util.GeoPoint(latitude, longitude));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);

        // Actualizar el mapa
        mapView.invalidate();
    }


    private void searchPointsOfInterest() {
        String latitudeText = edtLatitude.getText().toString();
        String longitudeText = edtLongitude.getText().toString();

        if (isValidCoordinates(latitudeText, longitudeText)) {
            double latitude = Double.parseDouble(latitudeText);
            double longitude = Double.parseDouble(longitudeText);

            GeoPoint location = new GeoPoint(latitude, longitude);
            int zoomLevel = 18;

            // Limpiar overlays anteriores
            mapView.getOverlays().clear();

            mapView.getController().setCenter(location);
            mapView.getController().setZoom(zoomLevel);

            // Añadir un marcador en la ubicación actual
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(latitude, longitude));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);

            // Actualizar el mapa
            mapView.invalidate();
        } else {
            Toast.makeText(this, "Ingresa coordenadas válidas", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCoordinates(String latitudeText, String longitudeText) {
        try {
            double latitude = Double.parseDouble(latitudeText);
            double longitude = Double.parseDouble(longitudeText);

            // Verificar si las coordenadas están dentro de un rango válido
            if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, configurar el proveedor de ubicación
                setupLocationProvider();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }

        mapView.onDetach();
    }
}