package org.debatkusir.rutenia.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.debatkusir.rutenia.Model.Angkot;
import org.debatkusir.rutenia.Model.BestRoute;
import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class RouteFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private Context appContext;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private Location myLocation;

    private LatLng originCoordinate;
    private LatLng destinationCoordinate;

    private AutoCompleteTextView originAutoComplete;
    ArrayAdapter<String> originAutoCompleteAdapter;
    String[] originPlaceItem = new String[] {""};

    private AutoCompleteTextView destinationAutoComplete;
    ArrayAdapter<String> destinationAutoCompleteAdapter;
    String[] destinationPlaceItem = new String[] {""};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);

        appContext = Rutenia.getAppContext();

        MapView mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        myLocation = getLastKnownLocation();
        locationManager = (LocationManager) appContext.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        mMapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        originAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.departAutoCompleteTextView);
        destinationAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.destinationAutoCompleteTextView);

        originAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    originPlaceItem = Rutenia.getLocalDatabase().getAllPlaces(s.toString());
                    originAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, originPlaceItem);
                    originAutoComplete.setAdapter(originAutoCompleteAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        originAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();

                String selected = originPlaceItem[0];

                originCoordinate = Rutenia.getLocalDatabase().getCoordinatePlaceByName(selected);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originCoordinate, 18));
                mMap.addMarker(new MarkerOptions().position(originCoordinate).title(selected));
            }
        });

        destinationAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    destinationPlaceItem = Rutenia.getLocalDatabase().getAllPlaces(s.toString());
                    destinationAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, destinationPlaceItem);
                    destinationAutoComplete.setAdapter(destinationAutoCompleteAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destinationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationCoordinate = Rutenia.getLocalDatabase().getCoordinatePlaceByName(destinationPlaceItem[0]);

                if (ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, RouteFragment.this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, RouteFragment.this);
                }

                drawNearbyAngkotRoute();
            }
        });

        return rootView;
    }

    private void drawNearbyAngkotRoute(){
        mMap.clear();

        PolylineOptions polylineOptions = new PolylineOptions().
            geodesic(true).
            color(Color.BLUE).
            width(10);

        BestRoute nearbyAngkotRoute = Rutenia.getLocalDatabase().getNearbyAngkotRoute(originCoordinate, destinationCoordinate);

        if (nearbyAngkotRoute == null) {
            Toast.makeText(getActivity(), "Rute tidak ditemukan. Silakan pilih lokasi lain :)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int idAngkot = nearbyAngkotRoute.getIdAngkot();
        ArrayList<LatLng> route = nearbyAngkotRoute.getRoute();
        LatLng originNearbyWaypoint = nearbyAngkotRoute.getOriginNearestWaypoint();
        LatLng destNearbyWaypoint = nearbyAngkotRoute.getDestNearestWaypoint();

        Angkot angkot = Rutenia.getLocalDatabase().getAngkotById(idAngkot);

        int routeSize = route.size();
        int i = nearbyAngkotRoute.getOriginNearestWaypointIdx();
        boolean done = false;
        while (!done) {

            if (i == routeSize) {
                i = 0;
            }
            polylineOptions.add(route.get(i));
            Log.d("tescetak",""+i);

            if (i == nearbyAngkotRoute.getDestNearestWaypointIdx()) {
                done = true;
            }
            i++;
        }

        mMap.addPolyline(polylineOptions);

        View marker = ((LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.angkot_marker, null);
        TextView angkotName = (TextView)  marker.findViewById(R.id.angkotName);
        ImageView angkotImage = (ImageView) marker.findViewById(R.id.angkotImage);
        TextView terminalName = (TextView)  marker.findViewById(R.id.terminal);

        String terminal = Rutenia.getLocalDatabase().getTerminalById(angkot.getTransitStop1());
        terminal += " - " + Rutenia.getLocalDatabase().getTerminalById(angkot.getTransitStop2());

        terminalName.setText(terminal);
        angkotName.setText(angkot.getName());

        mMap.addMarker(new MarkerOptions().
                position(originNearbyWaypoint).
                icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(marker)))
        );
        mMap.addMarker(new MarkerOptions().position(destNearbyWaypoint).title("Turun di sini"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(originCoordinate);
        builder.include(originNearbyWaypoint);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.d("tescetak","listener ");
                    return false;
                }
            });
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            myLocation = locationManager.getLastKnownLocation(provider);

            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();

            LatLng currentLocation = new LatLng(latitude, longitude);
            originCoordinate = currentLocation;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(appContext, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                originAutoComplete.setText(address);
            } catch (IOException io) {

            }
        }
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
        myLocation = location;
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

    public Bitmap createDrawableFromView(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
