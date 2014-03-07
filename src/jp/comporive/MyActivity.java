package jp.comporive;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sensorManager = (SensorManager)getSystemService( SENSOR_SERVICE  );

        BlockingQueue<Velocity> queue = new ArrayBlockingQueue<Velocity>(70 * 60); // 70FPS �����肵�āA1���ԕ� (�Ƃ肠����)
        SensorThread sensor = new SensorThread(sensorManager, queue);
        SoundThread sound = new SoundThread(queue);

        sensor.start();
        sound.start();

        setContentView(R.layout.main);
    }
}


class SensorThread extends Thread implements SensorEventListener{
    SensorManager sensorManager;
    Sensor sensor;

    private final BlockingQueue<Velocity> queue;
    private VelocityAccumlator velocity = new VelocityAccumlator(0.0, 0.0, 0.0); // ���x�x�N�g���̏����l�� (0, 0, 0) ������
    public SensorThread(SensorManager sensorManager, BlockingQueue<Velocity> queue){
        this.sensorManager = sensorManager;
        this.queue = queue;
    }
    @Override
    public void run(){
    	final int RATE = 16670;	// 16.67 msec in micro seconds
    	// TYPE_ACCELEROMETER -> �o�͂Ɂu�d�́v���܂܂�� (���ɒu�����ςȂ��ł� 9.8 �O��̒l���o��)
    	// Sensor.TYPE_LINEAR_ACCELERATION -> �d�͂��������␳�l���o�� (Android�o�[�W�����ɂ���Ďg���Ȃ�������l������������炵��?)
        sensor = sensorManager.getDefaultSensor( Sensor.TYPE_LINEAR_ACCELERATION /*Sensor.TYPE_ACCELEROMETER*/ );
        sensorManager.registerListener(this, sensor, RATE /*SensorManager.SENSOR_DELAY_UI*/);
    }

    public void onAccuracyChanged (Sensor sensor, int accuracy) {

    }
    
    private long prevTimestamp = 0L;
    final double NANOSEC_SCALE = 1000 * 1000 * 1000;
    private long count = 0L;
    
    private float initialValues[];
    public void onSensorChanged(SensorEvent sensorEvent) {
    	long deltaT = sensorEvent.timestamp - prevTimestamp;
    	prevTimestamp = sensorEvent.timestamp;
    	
    	if (count == 0) {
    		// �ŏ��̃f�[�^�́Atimestamp �Ɖ����x�̒l�����o���Ă����ēǂ݂Ƃ΂�
    		initialValues = Arrays.copyOf(sensorEvent.values, sensorEvent.values.length);
        	count++;
    		return;
    	}
    	double deltaTinSec = deltaT / NANOSEC_SCALE;
    	
    	float values[] = Arrays.copyOf(sensorEvent.values, sensorEvent.values.length);
    	// initialValues �ŃL�����u���[�g
    	for (int i=0; i<values.length; i++) {
    		values[i] -= initialValues[i];
    	}
    	velocity.update(values[0], values[1], values[2], deltaTinSec);
    	
    	try {
			queue.put(velocity.current());
			Log.d("comporive", velocity.current().toString());
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
//    	Log.d("comporive", 
//    		sensorEvent.timestamp + " (delta = " + deltaTinSec + " sec.): " + 
//    		Arrays.toString(values) + 
//    		" velocity = " + velocity.magnitude() +
//    		"(" + velocity.velocityX() + ", " + velocity.velocityY() + ", " + velocity.velocityZ() + ")");
    	
    	count++;
    }
}