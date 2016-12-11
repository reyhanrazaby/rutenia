package org.debatkusir.rutenia.Fragment;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class RouteFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private Location myLocation;

    private LatLng departCoordinate;
    private LatLng destinationCoordinate;

    private AutoCompleteTextView departAutoComplete;
    ArrayAdapter<String> departAutoCompleteAdapter;
    String[] departPlaceItem = new String[] {""};
    int departPlaceId;

    private AutoCompleteTextView destinationAutoComplete;
    ArrayAdapter<String> destinationAutoCompleteAdapter;
    String[] destinationPlaceItem = new String[] {""};
    int destinationPlaceId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);

        MapView mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        myLocation = getLastKnownLocation();
        locationManager = (LocationManager) Rutenia.getAppContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        departAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.departAutoCompleteTextView);
        destinationAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.destinationAutoCompleteTextView);

        departAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    departPlaceItem = Rutenia.getLocalDatabase().getAllPlaces(s.toString());
                    departAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, departPlaceItem);
                    departAutoComplete.setAdapter(departAutoCompleteAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        departAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(Rutenia.getAppContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            myLocation = locationManager.getLastKnownLocation(provider);
            LatLng currentLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
            //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Aku di sini"));
        }


        PolylineOptions polylineOptionsD11 = new PolylineOptions().
                geodesic(true).
                color(Color.BLUE).
                width(10);

        ArrayList<LatLng> punyaD11 = Rutenia.getLocalDatabase().getWaypointsByAngkotName("D11");
        polylineOptionsD11.addAll(punyaD11);

        mMap.addPolyline(polylineOptionsD11);
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) Rutenia.getAppContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(Rutenia.getAppContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Location l = locationManager.getLastKnownLocation(provider);

                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known myLocation: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.myLocation = location;
        Log.d("anjas",""+location.getLatitude());
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
}
