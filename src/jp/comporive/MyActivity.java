package jp.comporive;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorThread sensor = new SensorThread();
        SoundThread sound = new SoundThread();

        sensor.start();
        sound.start();

        setContentView(R.layout.main);
    }
}

class SensorThread extends Thread{
	@Override
	public void run() {
	}
}