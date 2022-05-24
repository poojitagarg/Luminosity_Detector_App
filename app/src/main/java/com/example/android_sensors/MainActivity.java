package com.example.android_sensors;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

        // System sensor manager instance.
        private SensorManager mSensorManager;

        // light sensors, as retrieved from the sensor manager.
        private Sensor mSensorLight;

        // TextViews to display current sensor values.
        private TextView mTextSensorLight;

        float maxValue;
        View root;
        //int d = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);

        // Initialize all view variables.
        mTextSensorLight = (TextView) findViewById(R.id.label_light);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get light and proximity sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If mSensorLight is null, those sensors
        // are not available in the device.  Set the text to the error message
        if (mSensorLight == null) { mTextSensorLight.setText(sensor_error); }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        maxValue = mSensorLight.getMaximumRange();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // The new data value of the sensor.  The light
        // sensors report one value at a time, which is always the first
        // element in the values array.
        float currentValue = sensorEvent.values[0];
        int newValue = (int) (255f * currentValue / maxValue);
        root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
        double luminance = (0.299 * newValue + 0.587 * newValue + 0.114 * newValue)/255;

        mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                // Counting the perceptive luminance - human eye favors green color...
                if (luminance > 0.5)
                    mTextSensorLight.setTextColor(Color.BLACK); // bright colors - black font
                else
                    mTextSensorLight.setTextColor(Color.WHITE);// dark colors - white font

    }

    /**
     * Abstract method in SensorEventListener.  It must be implemented, but is
     * unused in this app.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}