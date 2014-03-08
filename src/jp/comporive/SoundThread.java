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
		
		//�h�����̏�����
		drum.initialize();
		
		// INTERVAL �b��1�x�ABPM��ύX����
		final double INTERVAL = 3.0;	// �Ƃ肠����3�b
		
		double elapsed = 0.0;	// ���O��BPM��ύX���Ă���̌o�ߎ��� (�b)
		double vsum = 0.0; 	// ���� INTERVAL �b���̑��x�l�̘a
		
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