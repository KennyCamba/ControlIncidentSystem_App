package com.app.computacionysociedad.systemcontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class ReportFragment extends Fragment  {
    GPSTracker gpsTracker;
    Spinner incidents;
    String incident, date, time;
    SupportMapFragment mapFragment;
    public static TwitterSession session;
    TwitterSession clientSession;
    Marker marker = null;
    Geocoder geocoder;
    String[] list = {
            "Choque de dos vehiculos",
            "Choque multiple",
            "Automovil Volcado",
            "Incendio en la vía",
            "Peaton atropellado",
            "Peatones atropellados"
    };
    ArrayAdapter<String> adapter;
    double latitude;
    double longitude;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int year = c.get(Calendar.YEAR);
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    EditText dateText;
    EditText timeText;
    ImageButton btnEditDate;
    ImageButton btnEditTime;
    CheckBox useLocation;
    EditText locationText;
    private GoogleMap mMap;
    Button sendTw;
    List<Address> addresses;
    Address address;
    ImageButton btnGo;
    RadioGroup radioGroup;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateText = view.findViewById(R.id.date_report);
        timeText = view.findViewById(R.id.time_report);
        btnEditDate = view.findViewById(R.id.get_date);
        btnEditTime = view.findViewById(R.id.get_time);
        useLocation = view.findViewById(R.id.check_my_location);
        locationText = view.findViewById(R.id.search);
        sendTw = view.findViewById(R.id.send);
        radioGroup = view.findViewById(R.id.group);
        final long SIPACI_ID = 946153172791386112L;
        final String SIPACI_USER_NAME = "sipeci911";


        btnGo = view.findViewById(R.id.btn_go);
        geocoder = new Geocoder(this.getContext());

        setStatusChange(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fast_report:
                        setStatusChange(false);
                        break;
                    case R.id.details_report:
                        setStatusChange(true);
                        break;
                }
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationText.getText().toString();
                if(location.isEmpty()){
                    Toast.makeText(ReportFragment.this.getContext(), "No ha ingresado nunguna ubicación", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        addresses = geocoder.getFromLocationName(location + ", Guayaquil", 1);
                        System.out.println(addresses);
                        if(addresses.size() < 1){
                            Toast.makeText(ReportFragment.this.getContext(), "No se encontro la ubicación", Toast.LENGTH_LONG).show();
                        }else{
                            address = addresses.get(0);
                            addMarker(address.getLatitude(), address.getLongitude(), address.getAddressLine(0), true);
                        }
                    } catch (IOException e) {
                        Toast.makeText(ReportFragment.this.getContext(), "Ubicación incorrecta", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        String diaFormat = (dia < 10) ? CERO + dia : "" + dia;
        String mesFormat = (mes < 10) ? CERO + mes : "" + mes;
        String dateNow = diaFormat + BARRA + mesFormat + BARRA + year;
        setTextEditText(dateText, dateNow);


        String hourFormat = (hour < 10) ? CERO + hour : "" + hour;
        String minuteFormat = (minute < 10) ? CERO + minute : "" + minute;
        String timeNow = hourFormat + DOS_PUNTOS + minuteFormat;
        setTextEditText(timeText, timeNow);

        btnEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        btnEditTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });
        TwitterAuthToken token = new TwitterAuthToken(getResources().getString(R.string.token),
                getResources().getString(R.string.secret));

        final TwitterSession MY_SESSION = new TwitterSession(token, SIPACI_ID, SIPACI_USER_NAME);



        incidents = view.findViewById(R.id.list_incidents);
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, list);
        incidents.setAdapter(adapter);
        incidents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        incident = list[0];
                        break;
                    case 1:
                        incident = list[1];
                        break;
                    case 2:
                        incident = list[2];
                        break;
                    case 3:
                        incident = list[3];
                        break;
                    case 4:
                        incident = list[4];
                        break;
                    case 5:
                        incident = list[5];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_location);

        useLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    locationText.setEnabled(false);
                    try {
                        getLocalization();
                    }catch (Exception e){
                        setStatusChange(true);
                        Toast.makeText(ReportFragment.this.getContext(), "No es posible encntrar su ubicación", Toast.LENGTH_LONG).show();
                    }

                }else{
                    locationText.setEnabled(true);
                }
            }
        });

        sendTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = timeText.getText().toString();
                date = dateText.getText().toString();
                String addressLine = address.getAddressLine(0);
                //double lat = marker.getPosition().latitude;
                //double lon = marker.getPosition().longitude;
                clientSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                String clientName = clientSession.getUserName();
                String message = incident + " en " +
                        addressLine + ", reportado el " + date + " a las " + time + " por " + "@"+clientName;
                //StaticMap staticMap = new StaticMap(getResources().getString(R.string.satatic_map), 600, 600,
                //1, marker.getPosition(), 15);
                final Intent intent = new ComposerActivity.Builder(ReportFragment.this.getContext())
                        .session(MY_SESSION)
                        .text(message)
                        //.image(staticMap.getMapUrl())
                        .createIntent();
                startActivity(intent);
            }
        });
    }

    private void setStatusChange(boolean status){
        if(status){
            radioGroup.check(R.id.details_report);
        }else {
            radioGroup.check(R.id.fast_report);
           try {
                getLocalization();
            }catch (Exception e){
                Toast.makeText(ReportFragment.this.getContext(), "No es posible enctrar su ubicación", Toast.LENGTH_LONG).show();
                setStatusChange(true);
            }
        }
        btnEditDate.setEnabled(status);
        btnEditTime.setEnabled(status);
        useLocation.setChecked(!status);
        useLocation.setEnabled(status);
        locationText.setEnabled(status);
        btnGo.setEnabled(status);
    }

    private void getLocalization()throws NotLocationException {
        try{
            gpsTracker = new GPSTracker(ReportFragment.this.getContext());
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            addMarker(latitude, longitude, "Ubicación actual", true);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0);
        }catch(Exception ex){
            throw new NotLocationException();
        }
    }


    private void addMarker(final double lat, final double lng, String title, boolean moveCam){

        LatLng place = new LatLng(lat, lng);
        if(marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(place)
                .title(title)
                .draggable(true)
        );
        if(moveCam) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));
        }
    }

    private void getTime() {
        final TimePickerDialog timePicker = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourFormat =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minuteFormat = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String text = hourFormat + DOS_PUNTOS + minuteFormat;
                setTextEditText(timeText, text);
            }
        }, hour, minute, false);
        timePicker.show();
    }

    private void getDate() {
        DatePickerDialog datePicker = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String dayFormat = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormat = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                String text = dayFormat + BARRA + mesFormat + BARRA + year;
                setTextEditText(dateText, text);
            }
        }, year, mes, dia);
        datePicker.show();
    }

    public void setTextEditText(EditText editText, String text){
        editText.setText(text);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMinZoomPreference(10);
                    LatLng gyeLocation = new LatLng(-2.2058400, -79.9079500);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(gyeLocation));

                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            try {
                                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                address = addresses.get(0);
                            } catch (IOException e) {
                                String string = "Error: Ubicación no encontrada";
                                Toast.makeText(ReportFragment.this.getContext(), string, Toast.LENGTH_LONG).show();
                            }
                            addMarker(latLng.latitude, latLng.longitude, address.getAddressLine(0), false);
                        }
                    });
                }
            });
        }

        getChildFragmentManager().beginTransaction().replace(R.id.map_location, mapFragment).commit();

        return rootView;
    }

}
