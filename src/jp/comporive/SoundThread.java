package jp.comporive;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class SoundThread extends Thread{
	public boolean shutdown = false;
	Drummer drum = new Drummer();
	
	@Override
	public void run() {
	
	//	testThread test = new testThread();
		
		//�h�����̏�����
		drum.initialize();
				
		//test.start();
		double bpm = 30.0;
		while (!shutdown){
			//�h��������
			
			drum.setBpm(bpm);
			
		}
		
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