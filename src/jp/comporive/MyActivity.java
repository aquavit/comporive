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
	// INTERVAL 秒に1度、BPMを変更する
	public static final double INTERVAL = 3.0;	// とりあえず3秒
	
	private SensorManager sensorManager;
	private SensorHandler sensorHandler;
	private Sensor sensor;
	
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager)getSystemService( SENSOR_SERVICE  );
        sensor = sensorManager.getDefaultSensor( Sensor.TYPE_LINEAR_ACCELERATION /*Sensor.TYPE_ACCELEROMETER*/ );
        
        BlockingQueue<Velocity> queue = new ArrayBlockingQueue<Velocity>(70 * 60); // 70FPS を仮定して、1分間分 (とりあえず)
        sensorHandler = new SensorHandler(queue);
        SoundThread sound = new SoundThread(queue);

        //コンテキストを取得する
        getContext();
        
        sound.start();

       // testPlay();
        setContentView(R.layout.main);
        onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager!=null && sensorHandler!=null)
        	sensorManager.unregisterListener(sensorHandler);
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	final int RATE = 16670;	// 16.67 msec in micro seconds
    	// TYPE_ACCELEROMETER -> 出力に「重力」が含まれる (机に置きっぱなしでも 9.8 前後の値が出る)
    	// Sensor.TYPE_LINEAR_ACCELERATION -> 重力を除いた補正値が出る (Androidバージョンによって使えなかったり値がいいかげんらしい?)
    	if(sensorManager!=null && sensor!=null)
    		sensorManager.registerListener(sensorHandler, sensor, RATE /*SensorManager.SENSOR_DELAY_UI*/);
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


class SensorHandler implements SensorEventListener{
    private final BlockingQueue<Velocity> queue;
    private VelocityAccumlator velocity = new VelocityAccumlator(0.0, 0.0, 0.0); // 速度ベクトルの初期値は (0, 0, 0) を仮定
    public SensorHandler(BlockingQueue<Velocity> queue){
        this.queue = queue;
    }
    
    //@Override
    //public void run(){
    //	final int RATE = 16670;	// 16.67 msec in micro seconds
    	// TYPE_ACCELEROMETER -> 出力に「重力」が含まれる (机に置きっぱなしでも 9.8 前後の値が出る)
    	// Sensor.TYPE_LINEAR_ACCELERATION -> 重力を除いた補正値が出る (Androidバージョンによって使えなかったり値がいいかげんらしい?)
   //     sensor = sensorManager.getDefaultSensor( Sensor.TYPE_LINEAR_ACCELERATION /*Sensor.TYPE_ACCELEROMETER*/ );
   //     sensorManager.registerListener(this, sensor, RATE /*SensorManager.SENSOR_DELAY_UI*/);
   //     
    //}

    public void onAccuracyChanged (Sensor sensor, int accuracy) {

    }
    
    private long prevTimestamp = 0L;
    final double NANOSEC_SCALE = 1000 * 1000 * 1000;
    private long count = 0L;

	private double elapsed = 0.0;	// 直前にBPMを変更してからの経過時間 (秒)

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
			
			elapsed += velocity.current().dt;
			if (elapsed >= MyActivity.INTERVAL) {;
				// 速度の積算機をリセットする
				// ※ このスレッドで独立に elapsed time を加算・判定する方法だと、
				// SoundThread 側と完全な同期が保証されないが、今回は目をつぶる
				velocity = new VelocityAccumlator(0.0, 0.0, 0.0);
				// clear the stats for the next interval
				elapsed = 0.0;
			}
			
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