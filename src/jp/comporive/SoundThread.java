package jp.comporive;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
		
		//�h�����̏�����
		drum.initialize();
				
		//test.start();
		/*
		double bpm = 30.0;
		while (!shutdown){
			//�h��������
			
			drum.setBpm(bpm);
			
		}
		*/
		
		//�h�����̏I������
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