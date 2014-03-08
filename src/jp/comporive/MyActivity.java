package jp.comporive;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sensorManager = (SensorManager)getSystemService( SENSOR_SERVICE  );

        SensorThread sensor = new SensorThread(sensorManager);
        SoundThread sound = new SoundThread();

        //�R���e�L�X�g���擾����
        getContext();
        
        sensor.start();
        sound.start();

        setContentView(R.layout.main);
    }
    
    /**�R���e�L�X�g���擾����*/
    private void getContext(){
    	AppliData.context = this;
    }
}


class SensorThread extends Thread implements SensorEventListener{
    SensorManager sensorManager;
    Sensor sensor;

    public SensorThread(SensorManager sensorManager){
        this.sensorManager = sensorManager;
    }
    
    @Override
    public void run(){
        sensor = sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void onAccuracyChanged (Sensor sensor, int accuracy) {

    }
    public void onSensorChanged(SensorEvent sensorEvent) {

    }
}