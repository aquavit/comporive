package jp.comporive;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;

/**ドラマー*/
public class Drummer {

	private static double SEC = 1000000000.0;
	static double baseBpm = 60.0;	//ベースフレーム
	
	//BPMの算出用
	double nowBpm = 60.0;	//現在のbpm
	double targetBpm = 0.0;	//ターゲットとなるbpm

	SoundPool sound = null;
	
	//楽器のデータ
	class SoundData{
		byte[] s = null;
		Timer timer = null;
		int bufSize = 10;	//音楽の同時利用数
		AudioTrack at;
	//	int id = 0;	//ID
		long waitTime = 0;	//待機時間
		long targetTime = 0;	//時間算出
		long sub = 0;
		//boolean play = false;
	}
	
	Timer timer = new Timer();
	SoundData drum = new SoundData();//ドラムデータ
	SoundData snear = new SoundData();	//スネアデータ

	
	class DrumTask extends TimerTask{
		
		long waitTime = 0;
		public DrumTask(long rate){
			
			this.waitTime = (long)((double)rate * 0.5);
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//long progress = System.currentTimeMillis();
			
	//		sound.play(drum.id, 1.0F, 1.0F, 1, 0, 1.f);
			drum.at.write(drum.s, 0, drum.s.length);
			//
			//progress = System.currentTimeMillis() - progress;
			
			try {
				//待機時間-経過時間
				//long wait = waitTime - progress;
				
				//待機時間があったら、待機
				//if (wait > 0){
					Thread.sleep(waitTime);
		//		}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//スネア読み込み
			snear.at.write(snear.s, 0, snear.s.length);
			
			
//			sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	class SnearTask extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
	//		sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	int minBufferSize = 0;
	
	
	public Drummer(){		
		
	}
	/**初期化*/
	public void initialize(){
		
		//sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
		
		initDrum();
		initSnear();
		
		samMill = 0;
		long rate = (long) (1000.0*(60.0 / nowBpm));
		drum.at.play(); 
		snear.at.play();
		timer.schedule(new DrumTask(rate), 0, rate);
        
	//	drum.timer.schedule(new DrumTask(), 0, 500);
	//	snear.timer.schedule(new SnearTask(), 0, 250);
		
		//sound.setLoop(drum.id, -1);
	}
	
	/**終了処理*/
	public void shutdown(){
		
	}
	
	/**処理*/
	public void process(){
		
		//bpm数を計算する
		playSound();
	}
	

	/**音を鳴らす*/
	private void playSound(){
		/*
		if (!drum.play){
			//sound.setLoop(drum.id, -1);
			sound.play(drum.id, 1.0F, 1.0F, 1, 0, 1.f);
			drum.play = true;
		}
		*/
		
		
		//BPMを算出する
	//	calcBpm();
		
		//時間を算出する
	//	calcTime();
		
		//bpmを鳴らす
		//playDrum();
	
	//	playSnear();
	}
	
	/**ドラムを鳴らす*/
	private void playDrum(){
		if (drum == null) return;
		//Log.e("Time", "target = "+target);
		//Log.e("Time", "sam = "+samMill);
		
		//ターゲットまでの音が出ていない
		if (drum.targetTime > samMill)
			return;

	//	Log.e("Sound", "PLAY SOUND="+(target - samMill));
		
		//
		/*
		//samMill = drum.targetTime;
		{
			//long sub = drum.targetTime - samMill + drum.sub -100;
			//drum.sub = (0>sub)? 0:sub;
			drum.targetTime += drum.waitTime;
		}
		*/
		
	}
	
	private void playSnear(){
		if (snear == null) return;
		
		//ターゲットまでの音が出ていない
		if (snear.targetTime > samMill)
			return;

		snear.targetTime += drum.waitTime;
		
		//音を鳴らす
	//	sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
	}
	
	/**bpmを渡す*/
	public void setBpm(double bpm){
		final double offset = 5.0;	//オフセット値
		
		//bpmが0以下の場合は、再生しない
		if (bpm <= 0.0){
			timer.cancel();
			return;
		}
		
		//一定値以上変化がない場合は返す
		if (nowBpm+offset >= bpm && nowBpm-offset<=bpm){
			return;
		}
		
		//終了処理
		timer.cancel();
	
		//Bpm
		long rate = (long) (1000.0*(60.0 / bpm));
		timer.schedule(new DrumTask(rate), 0, rate);
	}
		
	/**BPM算出*/
	public void calcBpm(){
		return;
		/*
		//仮代入
		//ドラムを鳴らすミリ秒を計算する
		if (nowBpm <= 0.0){
			drum.waitTime = 0;
		}else{
			//60BPM は 1000ミリ(1秒)に一回なので、割合を算出する
			double rate = 60.0 / nowBpm;
			
			
	
			drum.waitTime = (long)(SEC * rate);
		}*/
	}
	
	/**サウンドデータの読み込み*/
	private void readSoundData(int soundId, SoundData soundData){
		
		//オーディオトラックの作成
		soundData.at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
	
		//ドラムデータを呼び出す
        try {
        	InputStream in = AppliData.context.getResources().openRawResource(soundId);
            DataInputStream dis = new DataInputStream(in);
			soundData.s = new byte[in.available()];
			dis.readFully(soundData.s);	
			in.close();
			dis.close();
		} catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        }
         catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	/**ドラム初期化*/
	private void initDrum(){
		drum.timer = new Timer();
		
		//サウンドデータの読み込み
		readSoundData(R.raw.a0, drum);

		//60BPM は 1000ミリ(1秒)に一回なので、割合を算出する
		double rate = 60.0 / nowBpm;
		rate = SEC * rate;
		
		drum.waitTime = (long)rate;
		drum.targetTime = (long)rate;

		//音を鳴らす
		//sound.setRate(drum.id, 1.f);		
	}
	
	/**スネア初期化*/
	private void initSnear(){
		snear.timer = new Timer();

		//サウンドデータの読み込み
		readSoundData(R.raw.a0, snear);

		//sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		//snear.id = sound.load(AppliData.context, R.raw.a4, 1);
		
		double rate = 60.0 / nowBpm;
		rate = (SEC * rate)*1.5;
		
		snear.waitTime = (long)rate;
		snear.targetTime = (long)rate;
	}
	
	/**時間計測*/
	public void calcTime(){
		return;
		/*
		//初期時の処理
		if (oldMill == -1){
			oldMill = getTime();
			return;
		}
			
		//現在の時間を取得する
		//long nowMill = System.currentTimeMillis();
		long nowMill = getTime();

		//現在の時間 -　1フレーム前の時間
		samMill += nowMill - oldMill;
		
		//
		oldMill = nowMill;
		*/
	}
	
	private long getTime(){return System.nanoTime();}

	private long oldMill = -1; //昔のミリサイズ
	private long samMill = 0;	//総ミ
}
