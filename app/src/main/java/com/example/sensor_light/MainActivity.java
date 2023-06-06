package com.example.sensor_light;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm = null;
    EditText txtX = null;
    EditText txtY = null;
    EditText txtZ = null;
    List list;
    float[] value;
    SensorEventListener sel;
    CameraManager cameraM;
    String getCameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraM = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            getCameraID = cameraM.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        txtX = this.findViewById(R.id.txtX);
        txtY = this.findViewById(R.id.txtY);
        txtZ = this.findViewById(R.id.txtZ);

        sel = new SensorEventListener(){
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            @SuppressLint("SetTextI18n")
            public void onSensorChanged(SensorEvent event) {
                value = event.values;
                txtX.setText(": "+value[0]);
                txtY.setText(": "+value[1]);
                txtZ.setText(": "+value[2]);

                if(value[1] > 5){
                    try {
                        cameraM.setTorchMode(getCameraID, true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        cameraM.setTorchMode(getCameraID,false);
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

            }
        };
        sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop(){
        super.onStop();
        sm.unregisterListener(sel);
    }
}