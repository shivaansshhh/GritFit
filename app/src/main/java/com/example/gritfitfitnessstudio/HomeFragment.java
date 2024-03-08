package com.example.gritfitfitnessstudio;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment implements SensorEventListener {
    TextView txtUserName ,txtDayDateMonth, txtSteps, txtDistance;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    double averageStrideLength = 0.67;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtUserName = view.findViewById(R.id.txtUserName);
        txtDayDateMonth = view.findViewById(R.id.txtDayDateMonth);
        txtSteps = view.findViewById(R.id.txtSteps);
        txtDistance = view.findViewById(R.id.txtDistance);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        sensorManager = (SensorManager) Objects.requireNonNull(requireActivity().getSystemService(SENSOR_SERVICE));


// to store username in firebase by getting name from mail id and set the username in main screen
        if (firebaseUser != null) {
            txtUserName.setText(firebaseUser.getDisplayName());

        } else {
            // Handle the case where the user is not authenticated
            Intent intent = new Intent(getActivity() , LoginActivity.class);
            startActivity(intent);
        }


// fetch current date, month and day
        String currentDate = getCurrentDate();
        String currentMonth = getCurrentMonth();
        String currentDay = getCurrentDay();

//        Displaying
        String DayDateMonth = currentDay + ", " + currentDate + " "+ currentMonth;
        txtDayDateMonth.setText(DayDateMonth);





//        check if the device has step counter sensor
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor == null){
            txtSteps.setText("Step counter sensor not available on this device");
        }



        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Display the step count in the TextView
            int stepCount = (int) event.values[0];
            txtSteps.setText("Step Count: " + stepCount);

            double distanceMeters = stepCount * averageStrideLength;
            double distanceKilometers = distanceMeters / 1000.0; //convert meters into km

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String formattedDistance = decimalFormat.format(distanceKilometers);
            txtDistance.setText("Distance Covered: "+ formattedDistance +"km");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


//    function to get the Date from system
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

//    function to get Month from system
    private String getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return monthFormat.format(calendar.getTime());
    }

//    function to get the Day from system
    private String getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayFormat.format(calendar.getTime());
    }
}