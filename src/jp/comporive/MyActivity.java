package jp.comporive;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

        BlockingQueue<Velocity> queue = new ArrayBlockingQueue<Velocity>(70 * 60); // 70FPS を仮定して、1分間分 (とりあえず)
        SensorThread sensor = new SensorThread(sensorManager, queue);
        SoundThread sound = new SoundThread(queue);

        //コンテキストを取得する
        getContext();
        
//        sensor.start();
        sound.start();

       // testPlay();
        setContentView(R.layout.main);
    }
    
    private void testPlay() {
        int minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        final AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);

        try {
//            FileInputStream fin = new FileInputStream(filepath + "/REFERENCE.wav");
        	InputStream in = getResources().openRawResource(R.raw.a0);
            DataInputStream dis = new DataInputStream(in);

            final byte[] s = new byte[in.available()];
            dis.readFully(s);
            
            at.play();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
	                at.write(s, 0, s.length);
					
				}
			}, 0, 250);
            dis.close();
            in.close();

            // App 終了時に
//            at.stop();
//            at.release();
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }     		
	}

	/**コンテキストを取得する*/
    private void getContext(){
    	AppliData.context = this;
    }
}


class SensorThread extends Thread implements SensorEventListener{
    SensorManager sensorManager;
    Sensor sensor;

    private final BlockingQueue<Velocity> queue;
    private VelocityAccumlator velocity = new VelocityAccumlator(0.0, 0.0, 0.0); // 速度ベクトルの初期値は (0, 0, 0) を仮定
    public SensorThread(SensorManager sensorManager, BlockingQueue<Velocity> queue){
        this.sensorManager = sensorManager;
        this.queue = queue;
    }
    
    @Override
    public void run(){
    	final int RATE = 16670;	// 16.67 msec in micro seconds
    	// TYPE_ACCELEROMETER -> 出力に「重力」が含まれる (机に置きっぱなしでも 9.8 前後の値が出る)
    	// Sensor.TYPE_LINEAR_ACCELERATION -> 重力を除いた補正値が出る (Androidバージョンによって使えなかったり値がいいかげんらしい?)
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
    		// 最初のデータは、timestamp と加速度の値だけ覚えておいて読みとばす
    		initialValues = Arrays.copyOf(sensorEvent.values, sensorEvent.values.length);
        	count++;
    		return;
    	}
    	double deltaTinSec = deltaT / NANOSEC_SCALE;
    	
    	float values[] = Arrays.copyOf(sensorEvent.values, sensorEvent.values.length);
    	// initialValues でキャリブレート
    	for (int i=0; i<values.length; i++) {
    		values[i] -= initialValues[i];
    	}
    	velocity.update(values[0], values[1], values[2], deltaTinSec);
    	
    	try {
			queue.put(velocity.current());
//			Log.d("comporive", velocity.current().toString());
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