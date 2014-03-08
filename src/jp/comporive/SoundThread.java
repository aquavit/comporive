package jp.comporive;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.concurrent.BlockingQueue;


class SoundThread extends Thread{
	private final BlockingQueue<Velocity> queue;
	
	public SoundThread(BlockingQueue<Velocity> queue) {
		this.queue = queue;
	}
	
	public boolean shutdown = false;
	Drummer drum = new Drummer();
	
	@Override
	public void run() {
	
	//	testThread test = new testThread();
		
		//ドラムの初期化
		drum.initialize();
		
		// INTERVAL 秒に1度、BPMを変更する
		final double INTERVAL = 3.0;	// とりあえず3秒
		
		double elapsed = 0.0;	// 直前にBPMを変更してからの経過時間 (秒)
		double vsum = 0.0; 	// 直近 INTERVAL 秒内の速度値の和
		
		BPMCalculator bpmOf = new BoundedLinearBPM(3.0, 300);
		while (true) {
			try {
				Velocity v = queue.take();
				elapsed += v.dt;
				vsum += v.magnitude();
				Log.d("comporive" , vsum + ", " + v.magnitude());
				if (elapsed >= INTERVAL) {;
					double bpm = bpmOf.call(vsum / elapsed);
					// change BPM
					drum.setBpm(bpm);
					
					// clear the stats for the next interval
					elapsed = 0.0;
					vsum = 0.0;
				}
			} catch (InterruptedException e) {
				Log.e("comporive", e.toString());
			}
		}
		//test.start();
		/*
		double bpm = 30.0;
		while (!shutdown){
			//ドラム処理
			
			drum.setBpm(bpm);
			
		}
		*/
		
		//ドラムの終了処理
		//drum.shutdown();
		
	}
	
	class testThread extends Thread {
	    
	    public testThread(){
	    }
	    
	    @Override
	    public void run(){
	    	while (!shutdown){
	    		drum.calcBpm();
	    		drum.calcTime();
	    	}
	    }

	    public void onAccuracyChanged (Sensor sensor, int accuracy) {

	    }
	    public void onSensorChanged(SensorEvent sensorEvent) {

	    }
	}
}